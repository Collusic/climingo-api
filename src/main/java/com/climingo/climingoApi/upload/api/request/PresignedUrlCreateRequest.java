package com.climingo.climingoApi.upload.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PresignedUrlCreateRequest {

    @NotNull
    private String fileName;

    @NotNull
    private String extension;
}
