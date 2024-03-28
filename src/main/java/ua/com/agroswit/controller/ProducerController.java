package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.dto.ProducerDTO;
import ua.com.agroswit.service.ProducerService;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Producer", description = "Producer management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService service;

    @Operation(summary = "Retrieve all producers")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<ProducerDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Retrieve producer by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ProducerDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve producer by name")
    @GetMapping(produces = APPLICATION_JSON_VALUE, params = "name")
    ProducerDTO getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @Operation(summary = "Create new producer")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    ProducerDTO create(@RequestPart ProducerDTO json, @RequestPart MultipartFile logo) {
        log.info("Received producer creation request with json {} and logo {}", json, logo.getOriginalFilename());
        return service.create(json, logo);
    }

    @Operation(summary = "Delete producer by ID")
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        log.info("Received producer deleting request for id: {}", id);
        service.delete(id);
    }
}
