package com.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
    @Value("${minio.url}")
    String minioUrl;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            MinioClient minioClient = new MinioClient.Builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, accessSecret)
                    .build();
//            boolean isExist = minioClient
//                    .bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
//            if (isExist) {
//                System.out.println("Bucket already exists.");
//            }
//            else {
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
//            }
            return minioClient;
        } catch (Exception e) {


            e.printStackTrace();
            return null;
        }
    }
}
