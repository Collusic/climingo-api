package com.climingo.climingoApi.upload.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {

    private final String presignedUrl;
    private final String videoUrl;
}
