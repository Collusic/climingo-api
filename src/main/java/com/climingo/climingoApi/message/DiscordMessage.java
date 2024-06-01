package com.climingo.climingoApi.message;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

public class DiscordMessage {

    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private List<EmbedObject> embeds;

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    @Builder
    public DiscordMessage(String content, String username, String avatarUrl, boolean tts,
        List<EmbedObject> embeds) {
        this.content = content;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.tts = tts;
        this.embeds = embeds;
    }

    public String toJson() {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Set content or add at least one EmbedObject");
        }

        EmbedObject.JSONObject json = new EmbedObject.JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);

        if (!this.embeds.isEmpty()) {
            List<EmbedObject.JSONObject> embedObjects = new ArrayList<>();

            for (EmbedObject embed : this.embeds) {
                EmbedObject.JSONObject jsonEmbed = new EmbedObject.JSONObject();

                jsonEmbed.put("title", embed.getTitle());
                jsonEmbed.put("description", embed.getDescription());
                jsonEmbed.put("url", embed.getUrl());

                if (embed.getColor() != null) {
                    Color color = embed.getColor();
                    int rgb = color.getRed();
                    rgb = (rgb << 8) + color.getGreen();
                    rgb = (rgb << 8) + color.getBlue();

                    jsonEmbed.put("color", rgb);
                }

                EmbedObject.Footer footer = embed.getFooter();
                EmbedObject.Image image = embed.getImage();
                EmbedObject.Thumbnail thumbnail = embed.getThumbnail();
                EmbedObject.Author author = embed.getAuthor();
                List<EmbedObject.Field> fields = embed.getFields();

                if (footer != null) {
                    EmbedObject.JSONObject jsonFooter = new EmbedObject.JSONObject();

                    jsonFooter.put("text", footer.text);
                    jsonFooter.put("icon_url", footer.iconUrl);
                    jsonEmbed.put("footer", jsonFooter);
                }

                if (image != null) {
                    EmbedObject.JSONObject jsonImage = new EmbedObject.JSONObject();

                    jsonImage.put("url", image.url);
                    jsonEmbed.put("image", jsonImage);
                }

                if (thumbnail != null) {
                    EmbedObject.JSONObject jsonThumbnail = new EmbedObject.JSONObject();

                    jsonThumbnail.put("url", thumbnail.url);
                    jsonEmbed.put("thumbnail", jsonThumbnail);
                }

                if (author != null) {
                    EmbedObject.JSONObject jsonAuthor = new EmbedObject.JSONObject();

                    jsonAuthor.put("name", author.name);
                    jsonAuthor.put("url", author.url);
                    jsonAuthor.put("icon_url", author.iconUrl);
                    jsonEmbed.put("author", jsonAuthor);
                }

                List<EmbedObject.JSONObject> jsonFields = new ArrayList<>();
                for (EmbedObject.Field field : fields) {
                    EmbedObject.JSONObject jsonField = new EmbedObject.JSONObject();

                    jsonField.put("name", field.name);
                    jsonField.put("value", field.value);
                    jsonField.put("inline", field.inline);

                    jsonFields.add(jsonField);
                }

                jsonEmbed.put("fields", jsonFields.toArray());
                embedObjects.add(jsonEmbed);
            }

            json.put("embeds", embedObjects.toArray());
        }

        return json.toString();
    }

    @Getter
    public static class EmbedObject {

        private String title;
        private String description;
        private String url;
        private Color color;

        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;
        private List<Field> fields;

        public EmbedObject() {
            this.fields = new ArrayList<>();
        }

        private static class JSONObject {

            private final Map<String, Object> map = new HashMap<>();

            void put(String key, Object value) {
                if (value != null) {
                    map.put(key, value);
                }
            }

            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder();
                Set<Entry<String, Object>> entrySet = map.entrySet();
                builder.append("{");

                int i = 0;
                for (Map.Entry<String, Object> entry : entrySet) {
                    Object val = entry.getValue();
                    builder.append(quote(entry.getKey())).append(":");

                    if (val instanceof String) {
                        builder.append(quote(String.valueOf(val)));
                    } else if (val instanceof Integer) {
                        builder.append(Integer.valueOf(String.valueOf(val)));
                    } else if (val instanceof Boolean) {
                        builder.append(val);
                    } else if (val instanceof JSONObject) {
                        builder.append(val.toString());
                    } else if (val.getClass().isArray()) {
                        builder.append("[");
                        int len = Array.getLength(val);
                        for (int j = 0; j < len; j++) {
                            builder.append(Array.get(val, j).toString())
                                .append(j != len - 1 ? "," : "");
                        }
                        builder.append("]");
                    }

                    builder.append(++i == entrySet.size() ? "}" : ",");
                }

                return builder.toString();
            }
        }

        private static String quote(String string) {
            return "\"" + string + "\"";
        }

        @Builder
        public EmbedObject(String title, String description, String url, Color color, Footer footer,
            Thumbnail thumbnail, Image image, Author author, List<Field> fields) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.color = color;
            this.footer = footer;
            this.thumbnail = thumbnail;
            this.image = image;
            this.author = author;
            this.fields = fields == null ? new ArrayList<>() : fields;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        private record Footer(String text, String iconUrl) {

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }
        }

        private record Thumbnail(String url) {

            private Thumbnail(String url) {
                this.url = url;
            }
        }

        private record Image(String url) {

            private Image(String url) {
                this.url = url;
            }
        }

        private record Author(String name, String url, String iconUrl) {

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }
        }

        private record Field(String name, String value, boolean inline) {

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }
        }
    }
}
