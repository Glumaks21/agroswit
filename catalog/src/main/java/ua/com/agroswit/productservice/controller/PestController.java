package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.PestDTO;
import ua.com.agroswit.productservice.service.PestService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Pest management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/pests")
@RequiredArgsConstructor
public class PestController {

    private final PestService pestService;


    @Operation(summary = "Retrieve all pests")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    List<PestDTO> getAll() {
        return pestService.getAll();
    }

    @Operation(summary = "Create new pest")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    PestDTO create(@Valid @RequestBody PestDTO dto) {
        log.info("Received pest creation request with dto: {}", dto);
        return pestService.create(dto);
    }

    @Operation(summary = "Delete pest by ID")
    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Integer id) {
        log.info("Received pest deleting request with id: {}", id);
        pestService.deleteById(id);
    };
}
