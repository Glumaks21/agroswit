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
import ua.com.agroswit.userdetails.dto.request.CompanyModifyingDTO;
import ua.com.agroswit.userdetails.dto.request.PersonModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.CompanyDTO;
import ua.com.agroswit.userdetails.dto.response.PersonDTO;
import ua.com.agroswit.userdetails.dto.view.Views;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.service.PersonService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Person details management API")
@Slf4j
@RequestMapping("/api/v1/persons")
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;


    @Operation(summary = "Retrieve all persons details")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<PersonDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve person details by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    PersonDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Add person details")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    PersonDTO create(@RequestBody @JsonView(CreateView.class) PersonModifyingDTO dto) {
        log.info("Received person creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Update person details by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    PersonDTO update(@PathVariable Integer id, @RequestBody @JsonView(UpdateView.class) PersonModifyingDTO dto) {
        log.info("Received person updating request for id: {id}");
        return service.update(id, dto);
    }

    @Operation(summary = "Delete person details by ID")
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received person deleting request for id: {id}");
        service.deleteById(id);
    }
}
