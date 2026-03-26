package com.mininetflix.ministreaming.infrastructure.playback.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.MinioProperties;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

@Component
public class MinioVideoStorageService implements VideoStorageService {

    private final MinioClient minioClient;
    private final String internalHost; // URL interna do Compose
    private final String publicHost; // URL pública que o navegador vai acessar
    private final String bucket; // bucket padrão

    public MinioVideoStorageService(
            MinioClient minioClient,
            @Value("${minio.url}") String internalHost,
            @Value("${minio.public-url}") String publicHost,
            @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.internalHost = internalHost;
        this.publicHost = publicHost;
        this.bucket = bucket;
    }

    @Override
    public String generatePresignedUrl(String objectKey) {
        try {
            // Gera URL assinada usando host interno
            String internalUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(10, TimeUnit.MINUTES)
                            .build());

            // Substitui apenas o host interno pelo público
            return internalUrl.replace(internalHost, publicHost);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar URL do vídeo", e);
        }
    }

    @Override
    public void upload(String objectKey, MultipartFile file) {
        try {
            // Cria o bucket caso não exista
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build());

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build());
            }

            // Faz o upload do arquivo
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public File download(String objectKey) {
        try {
            File tempFile = File.createTempFile("video-", ".mp4");

            minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build())
                    .transferTo(new FileOutputStream(tempFile));

            return tempFile;

        } catch (Exception e) {
            throw new RuntimeException("Download failed", e);
        }
    }

    @Override
    public void uploadFile(String objectKey, File file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(new FileInputStream(file), file.length(), -1)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Upload file failed", e);
        }
    }
}