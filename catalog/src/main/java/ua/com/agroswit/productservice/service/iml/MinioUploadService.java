package ua.com.agroswit.productservice.service.iml;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.config.MinioProperties;
import ua.com.agroswit.productservice.exceptions.UploadServiceException;
import ua.com.agroswit.productservice.service.UploadService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioUploadService implements UploadService {

    private final static String jsonAnonymousReadPolicy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":\"*\",\"Action\":\"s3:GetObject\",\"Resource\":\"arn:aws:s3:::${bucketName}/*\"}]}";

    private final MinioClient minioClient;
    private final MinioProperties minioProps;

    public String getUrl(String name) {
        if (name == null || name.isBlank()) return null;
        return minioProps.endpoint() + "/" + minioProps.bucket() + "/" + name;
    }

    @Override
    public Resource load(String name) {
        try {
            log.info("Trying to load object {} from bucket {}", name, minioProps.bucket());
            var response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProps.bucket())
                    .object(name)
                    .build());
            return new ByteArrayResource(response.readAllBytes());
        } catch (IOException | GeneralSecurityException | MinioException e) {
            throw new UploadServiceException("Failed to load image: " + name, e);
        }
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if (file.isEmpty() || !StringUtils.hasText(file.getName())) {
            throw new IllegalArgumentException("File must not be empty");
        }

        if (!isStaticImage(file)) {
            throw new IllegalArgumentException("File not a static image");
        }

        initializeBucket();
        log.info("Trying to upload image object {} to bucket {}", file.getOriginalFilename(), minioProps.bucket());
        try (var is = file.getInputStream()) {
            var response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProps.bucket())
                    .stream(is, is.available(), -1)
                    .object(generateName(file))
                    .contentType(file.getContentType())
                    .build());

            return response.object();
        } catch (GeneralSecurityException | IOException | MinioException e) {
            throw new UploadServiceException("Failed to upload file", e);
        }
    }

    private void initializeBucket() {
        try {
            log.info("Trying to initialize bucket: {}", minioProps.bucket());
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProps.bucket())
                    .build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProps.bucket())
                        .build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(minioProps.bucket())
                        .config(jsonAnonymousReadPolicy.replace("${bucketName}", minioProps.bucket()))
                        .build());
            }
        } catch (GeneralSecurityException | IOException | MinioException e) {
            throw new UploadServiceException("Unable to initialize bucket", e);
        }
    }

    private boolean isStaticImage(MultipartFile file) {
        var contentType = file.getContentType();
        return IMAGE_JPEG_VALUE.equals(contentType) ||
                IMAGE_PNG_VALUE.equals(contentType);
    }

    private String generateName(MultipartFile file) {
        return UUID.randomUUID() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void remove(String name) {
        try {
            log.info("Trying to delete objet {} from bucket {}", name, minioProps.bucket());
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProps.bucket())
                    .object(name)
                    .build());
        } catch (GeneralSecurityException | IOException | MinioException e) {
            throw new UploadServiceException(String.format(
                    "Unable to delete object %s from bucket %s", name, minioProps.bucket()), e);
        }
    }
}
