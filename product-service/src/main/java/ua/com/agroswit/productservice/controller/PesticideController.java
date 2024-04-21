package ua.com.agroswit.productservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.productservice.dto.request.PesticideModifiableDTO;
import ua.com.agroswit.productservice.dto.response.PesticideDTO;
import ua.com.agroswit.productservice.service.PesticideService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Pesticide management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/products/pesticides")
@RequiredArgsConstructor
public class PesticideController {

    private final PesticideService pesticideService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<PesticideDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        var page = pesticideService.getAll(pageable);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve pesticide by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    PesticideDTO getById(@PathVariable Integer id) {
        return pesticideService.getById(id);
    }

    @Operation(summary = "Create pesticide product")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    PesticideDTO create(@RequestBody PesticideModifiableDTO dto) {
        log.info("Received pesticide creation request with dto: {}", dto);
        return pesticideService.create(dto);
    }
}
