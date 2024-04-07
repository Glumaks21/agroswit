package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.service.FileStorageService;

import java.net.URLConnection;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


@Tag(name = "File management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService service;

    @Operation(summary = "Retrieve file by name")
    @GetMapping("/{fileName}")
    ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        var resource = service.loadAsResource(fileName);
        var mediaType = MediaType.parseMediaType(URLConnection.guessContentTypeFromName(resource.getFilename()));
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
