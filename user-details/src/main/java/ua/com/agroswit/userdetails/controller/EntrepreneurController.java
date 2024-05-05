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
import ua.com.agroswit.userdetails.dto.request.EntrepreneurModifyingDTO;
import ua.com.agroswit.userdetails.dto.response.EntrepreneurDTO;
import ua.com.agroswit.userdetails.dto.view.Views;
import ua.com.agroswit.userdetails.dto.view.Views.CreateView;
import ua.com.agroswit.userdetails.dto.view.Views.UpdateView;
import ua.com.agroswit.userdetails.service.EntrepreneurService;

import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@Tag(name = "Entrepreneur details management API")
@Slf4j
@RequestMapping("/api/v1/entrepreneurs")
@RestController
@RequiredArgsConstructor
public class EntrepreneurController {

    private final EntrepreneurService service;


    @Operation(summary = "Retrieve all entrepreneurs")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<EntrepreneurDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(summary = "Retrieve entrepreneur by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    EntrepreneurDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @Operation(summary = "Add entrepreneur details")
    @ResponseStatus(CREATED)
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    EntrepreneurDTO create(@RequestBody @JsonView(CreateView.class) EntrepreneurModifyingDTO dto) {
        log.info("Received entrepreneur creation request with dto: {}", dto);
        return service.create(dto);
    }

    @Operation(summary = "Update entrepreneur details by ID")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    EntrepreneurDTO update(@PathVariable Integer id,
                           @RequestBody @JsonView(UpdateView.class) EntrepreneurModifyingDTO dto) {
        log.info("Received entrepreneur updating request with dtp {} for id: {}", dto, id);
        return service.update(id, dto);
    }

    @Operation(summary = "Deleting entrepreneur details by ID")
    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        log.info("Received entrepreneur deleting request for id: {}", id);
        service.deleteById(id);
    }
}
