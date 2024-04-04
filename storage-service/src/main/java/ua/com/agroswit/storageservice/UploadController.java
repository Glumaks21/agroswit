package ua.com.agroswit.storageservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLConnection;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


@Tag(name = "File management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService service;

    @Operation(summary = "Load file by name")
    @GetMapping("/{fileName}")
    ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        var resource = service.loadAsResource(fileName);
        var mediaType = MediaType.parseMediaType(URLConnection.guessContentTypeFromName(resource.getFilename()));
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Store file")
    @PostMapping
    ResponseEntity<Void> store(@RequestParam MultipartFile file) {
        var fileName = service.store(file);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{fileName}")
                        .buildAndExpand(fileName)
                        .toUri())
                .build();
    }
}
