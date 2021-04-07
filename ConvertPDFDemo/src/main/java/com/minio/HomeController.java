package com.minio;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    MinioService minioService;

    @Autowired
    FileService fileService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/upload")
    public void upload(@RequestBody MultipartFile multipartFile) throws IOException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, InvalidKeyException {
        System.out.println("aa");
        minioService.uploadFile("/test",multipartFile.getOriginalFilename(),new ByteArrayInputStream(multipartFile.getBytes()));
    }

    @PostMapping("/convert-pdf")
    public void upload() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {
        minioService.uploadFile("/test","pdf-test",new ByteArrayInputStream(fileService.toPdf("test1. ADD CK NHA DAT CHINH CHU 1NG.docx")));
    }

}
