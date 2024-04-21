package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.CultureDTO;
import ua.com.agroswit.productservice.dto.validation.Groups.Create;
import ua.com.agroswit.productservice.service.CultureService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Culture management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/cultures")
@RequiredArgsConstructor
public class CultureController {

    private final CultureService cultureService;


    @Operation(summary = "Retrieve all cultures", parameters = @Parameter(
            name = "name", description = "Name of then return culture"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    List<CultureDTO> getAll() {
        return cultureService.getAll();
    }

    @GetMapping(params = "name", produces = APPLICATION_JSON_VALUE)
    CultureDTO getByName(@RequestParam String name) {
        return cultureService.getByName(name);
    }

    @Operation(summary = "Create new culture")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CultureDTO create(@Validated(Create.class) @RequestBody CultureDTO dto) {
        log.info("Received culture creating request with dto: {}", dto);
        return cultureService.create(dto);
    }

    @Operation(summary = "Delete culture by ID")
    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Integer id) {
        log.info("Received culture deleting request for id: {}", id);
        cultureService.deleteById(id);
    }
}
