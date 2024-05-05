package ua.com.agroswit.userdetails.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.userdetails.dto.response.CustomerDTO;
import ua.com.agroswit.userdetails.service.CustomerService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Customer details management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<CustomerDTO>> getAll(@PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    CustomerDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }
}
