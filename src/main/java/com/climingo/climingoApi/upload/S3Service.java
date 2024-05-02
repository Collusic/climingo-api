package com.climingo.climingoApi.upload;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadVideoFile(MultipartFile videoFile) throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append("비디오_")
                .append(LocalDateTime.now())
                .append(".")
                .append(StringUtils.getFilenameExtension(videoFile.getOriginalFilename()));

        File file = convertMultipartFileToFile(videoFile);
        s3Client.putObject(new PutObjectRequest(bucket, fileName.toString(), file).withCannedAcl(CannedAccessControlList.PublicRead));
        file.delete();

        URL videoUrl = generatePermanentPresignedUrl(fileName.toString());

        return videoUrl.toString();
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    public URL generatePermanentPresignedUrl(String objectKey) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(HttpMethod.GET);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

}
