package com.dontgojunbao.bossoverhere.global.adapter.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일을 업로드하고, 퍼블릭 URL을 리턴합니다.
     */
    public String uploadFile(MultipartFile multipartFile) {
        String key = createFileKey(multipartFile.getOriginalFilename());
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(multipartFile.getContentType());
        meta.setContentLength(multipartFile.getSize());

        try {
            amazonS3.putObject(bucket, key, multipartFile.getInputStream(), meta);
            return amazonS3.getUrl(bucket, key).toString();
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.", e);
        }
    }
    /**
     * S3에 파일을 업로드하고, 퍼블릭 URL을 리턴합니다.
     * 업로드 후 파일의 접근 권한을 퍼블릭으로 설정합니다.
     */
    private String createFileKey(String originalFilename) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uuid = java.util.UUID.randomUUID().toString();
        return String.format("%s/%s-%s", date, uuid, originalFilename);
    }
}
