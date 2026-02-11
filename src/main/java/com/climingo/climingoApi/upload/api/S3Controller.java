package com.climingo.climingoApi.upload.api;

import com.climingo.climingoApi.upload.S3Service;
import com.climingo.climingoApi.upload.api.request.PresignedUrlCreateRequest;
import com.climingo.climingoApi.upload.api.response.PresignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="S3Controller", description = "영상 업로드(S3) api")
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/s3/presigned-url")
    @Operation(summary = "s3 presignedUrl 발행", description = "s3 presignedUrl 발행합니다.")
    @Parameter(name="fileName", description = "파일 이름")
    @Parameter(name="extension", description = "파일 확장자")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
        @RequestBody PresignedUrlCreateRequest request) {
        String presignedUrl = s3Service.generatePresignedUrl(request).toString();
        String videoUrl = parseUrlWithoutQuery(presignedUrl);

        return ResponseEntity.ok(new PresignedUrlResponse(presignedUrl, videoUrl));
    }

    private String parseUrlWithoutQuery(String url) {
        int queryIndex = url.indexOf("?");

        if (queryIndex != -1) {
            return url.substring(0, queryIndex);
        }

        return url;
    }
}