package ua.com.agroswit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.CustomerDTO;
import ua.com.agroswit.service.CustomerService;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<CustomerDTO> getAll(@PageableDefault Pageable pageable) {
        if (pageable.getPageNumber() > 0) {
            pageable = pageable.withPage(pageable.getPageNumber() - 1);
        }

        return service.getAll(pageable);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    CustomerDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }
}
