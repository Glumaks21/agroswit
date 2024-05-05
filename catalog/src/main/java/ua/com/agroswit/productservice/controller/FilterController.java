package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.FilterDTO;
import ua.com.agroswit.productservice.service.FilterService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Filter management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/filters")
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;


    @Operation(summary = "Retrieve all filters", parameters = @Parameter(
            name = "id", description = "Id of requested filters"))
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<FilterDTO> getAll() {
        return filterService.getAllHighLevel();
    }

    @GetMapping(params = "id", produces = APPLICATION_JSON_VALUE)
    Collection<FilterDTO> getAllByIds(@RequestParam(name = "id") Set<Integer> ids) {
        return filterService.getAllByIds(ids);
    }

    @Operation(summary = "Retrieve by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    FilterDTO getById(@PathVariable Integer id) {
        return filterService.getById(id);
    }

    @Operation(summary = "Create new filter")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    FilterDTO create(@RequestBody @Valid FilterDTO dto) {
        log.info("Received filter creation request with dto: {}", dto);
        return filterService.create(dto);
    }

    @Operation(summary = "Update filter by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    FilterDTO update(@PathVariable Integer id,
                     @RequestBody @Valid FilterDTO dto) {
        log.info("Received filter updating request for id {} with dto: {}", id, dto);
        return filterService.update(id, dto);
    }

    @Operation(summary = "Delete filter by ID")
    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable Integer id) {
        log.info("Received filter deletion request with id: {}", id);
        filterService.deleteById(id);
    }
}
