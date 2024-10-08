package com.climingo.climingoApi.upload.api;

import com.climingo.climingoApi.upload.S3Service;
import com.climingo.climingoApi.upload.api.request.PresignedUrlCreateRequest;
import com.climingo.climingoApi.upload.api.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/s3/presigned-url")
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