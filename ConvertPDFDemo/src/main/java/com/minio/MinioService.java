package com.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;


    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void uploadFile(String folder, String name, InputStream inputStream) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        Path tempFile = Files.createTempFile(name, "");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        UploadObjectArgs.Builder builder = UploadObjectArgs.builder().bucket(defaultBucketName)
                .object(folder + name).filename(tempFile.toString());
        minioClient.uploadObject(builder.build());
    }


    public byte[] getFile(String key) {
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(defaultBucketName)
                    .object(key).build();
            InputStream obj = minioClient.getObject(objectArgs);
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
