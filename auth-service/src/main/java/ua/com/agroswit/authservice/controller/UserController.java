package ua.com.agroswit.authservice.controller;

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
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDTO>> getAll(@PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);

        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    UserDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }
}
