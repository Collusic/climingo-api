package com.climingo.climingoApi.upload;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.climingo.climingoApi.upload.api.request.PresignedUrlCreateRequest;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadThumbnailImageFile(File image) {
        String fileName = new StringBuilder().append("썸네일_")
            .append(LocalDateTime.now())
            .append(".")
            .append(StringUtils.getFilenameExtension(image.getName()))
            .toString();

        s3Client.putObject(new PutObjectRequest(bucket, fileName, image).withCannedAcl(
            CannedAccessControlList.PublicRead));

        image.delete();

        URL url = s3Client.getUrl(bucket, fileName);

        return url.toString();
    }

    public URL generatePresignedUrl(PresignedUrlCreateRequest request) {
        String fileName = parseFileName("비디오_", request.getFileName(), request.getExtension());
        return s3Client.generatePresignedUrl(getGeneratePreSignedUrlRequest(bucket, fileName));
    }

    private String parseFileName(String prefix, String fileName, String extension) {
        return prefix
            + fileName
            + "_"
            + LocalDateTime.now()
            + "."
            + extension;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
            bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter("x-amz-acl", "public-read");

        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 3; // 3분
        expiration.setTime(expTimeMillis);

        return expiration;
    }
}
