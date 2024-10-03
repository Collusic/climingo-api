package com.climingo.climingoApi.message.error;

import com.climingo.climingoApi.message.DiscordMessage;
import com.climingo.climingoApi.message.DiscordMessage.EmbedObject;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorAlertMessage {

        private final String requestTime;
        private final String requestIp;
        private final String requestUrl;
        private final String requestQuery;
        private final String requestData;
        private final String errorMessage;

    public DiscordMessage toDiscordMessage() {
        EmbedObject embed = EmbedObject.builder()
            .color(16711680)
            .build();
        embed
            .addField("요청 시간", this.requestTime, false)
            .addField("REQUEST QUERY",this.requestQuery, false)
            .addField("REQUEST ENDPOINT", this.requestUrl, false)
            .addField("REQUEST IP", this.requestIp, false)
            .addField("REQUEST DATA", this.requestData, false)
            .addField("에러 내용", Optional.ofNullable(this.errorMessage).orElse("null"), false);

        return DiscordMessage.builder()
            .content("서버 에러 발생.. :cry: :pray:")
            .embeds(List.of(embed))
            .build();
    }
}
