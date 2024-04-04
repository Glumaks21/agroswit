package ua.com.agroswit.storageservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadDir;


    public LocalFileStorageService(FileStorageProperties properties) {
        this.uploadDir = Path.of(properties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }

    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file.");
        }

        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Cannot store file outside current directory: " + fileName);
            }

            var targetLocation = this.uploadDir.resolve(fileName);
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, targetLocation, REPLACE_EXISTING);
            }

            return fileName;
        } catch (IOException ex) {
            log.error("File storing error: {}", ex.getMessage());
            throw new FileStorageException(ex.getLocalizedMessage());
        }
    }

    @Override
    public Resource loadAsResource(String fileName) {
        try {
            var filePath = this.uploadDir.resolve(fileName).normalize();
            var resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not load file: " + fileName);

            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not load file: " + fileName);
        }
    }
}
