package ua.com.agroswit.userdetails.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.userdetails.dto.response.CompanyDTO;
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.service.CompanyService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Company details management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;


    @Operation(summary = "Retrieve all companies details")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<CompanyDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve company details by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    CompanyDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Add company details")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CompanyDTO create(@RequestBody @JsonView(CreateView.class) CompanyModifyingDTO dto) {
        log.info("Received company creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Update company details by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    CompanyDTO update(@PathVariable Integer id, @RequestBody @JsonView(UpdateView.class) CompanyModifyingDTO dto) {
        log.info("Received company updating request for id: {id}");
        return service.update(id, dto);
    }

    @Operation(summary = "Delete company details by ID")
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received company deleting request for id: {id}");
        service.deleteById(id);
    }
}
