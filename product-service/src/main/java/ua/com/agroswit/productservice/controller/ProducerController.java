package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.request.ProducerModifiableDTO;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;
import ua.com.agroswit.productservice.dto.validation.Groups.FullUpdate;
import ua.com.agroswit.productservice.service.ProducerService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;

@Tag(name = "Producer", description = "Producer management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService service;

    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "If name param is not specified, then return type is array otherwise producer object")
    })
    @Operation(summary = "Retrieve all producers",
            parameters = @Parameter(name = "name", description = "Name of the producer"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    List<ProducerDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Retrieve producer by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ProducerDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Retrieve producer by name", hidden = true)
    @GetMapping(produces = APPLICATION_JSON_VALUE, params = "name")
    ProducerDTO getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @Operation(summary = "Create new producer")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProducerDTO create(@Validated(Create.class) @RequestBody ProducerModifiableDTO dto) {
        log.info("Received producer creation request with dto {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Save producer logo")
    @PostMapping(path = "/{producerId}/logo", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    ProducerDTO saveLogo(@PathVariable Integer producerId, MultipartFile logo) {
        log.info("Received producer logo saving request with logo: {}", logo.getOriginalFilename());
        return service.saveLogoById(producerId, logo);
    }

    @Operation(summary = "Updating producer by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ProducerDTO update(@PathVariable Integer id,
                       @Validated(FullUpdate.class) @RequestBody ProducerModifiableDTO dto) {
        log.info("Received producer updating request with dto {}", dto);
        return service.update(id, dto);
    }

    @Operation(summary = "Delete producer by ID")
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        log.info("Received producer deleting request for id: {}", id);
        service.deleteById(id);
    }

    @Operation(summary = "Deleting producer logo by ID")
    @DeleteMapping(path = "/{producerId}/logo", produces = APPLICATION_JSON_VALUE)
    void deleteLogo(@PathVariable Integer producerId) {
        log.info("Received producer logo deleting request with logo for id: {}", producerId);
        service.removeLogoById(producerId);
    }
}
