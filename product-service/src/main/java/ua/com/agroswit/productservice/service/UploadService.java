package ua.com.agroswit.productservice.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    Resource load(String name);
    String uploadImage(MultipartFile file);
    void remove(String name);
}
