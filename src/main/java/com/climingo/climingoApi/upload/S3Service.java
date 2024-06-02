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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional(propagation = Propagation.REQUIRED)
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

        return videoUrl.toString().substring(0, videoUrl.toString().indexOf("?"));
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

    public String uploadImageFile(File image) {
        StringBuilder fileName = new StringBuilder();
        fileName.append("썸네일_")
            .append(LocalDateTime.now())
            .append(".")
            .append(StringUtils.getFilenameExtension(image.getName()));

        s3Client.putObject(new PutObjectRequest(bucket, fileName.toString(), image).withCannedAcl(
            CannedAccessControlList.PublicRead));
        image.delete();

        URL url = generatePermanentPresignedUrl(fileName.toString());

        return url.toString().substring(0, url.toString().indexOf("?"));
    }
}
