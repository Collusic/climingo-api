package com.climingo.climingoApi.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

// Reference: https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb#file-discordwebhook-java

@Getter
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

    @Getter
    public static class EmbedObject {

        private String title;
        private String description;
        private String url;
        private int color;

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
        }

        @Builder
        public EmbedObject(String title, String description, String url, int color, Footer footer,
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
