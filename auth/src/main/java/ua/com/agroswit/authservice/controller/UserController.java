package ua.com.agroswit.authservice.controller;

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
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;


    @Operation(description = "Retrieve all users")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDTO>> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        var page = service.getAll(pageable);
        var headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, OK);
    }

    @Operation(description = "Retrieve by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    UserDTO getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping(params = "id", produces = APPLICATION_JSON_VALUE)
    Collection<UserDTO> getByIds(@RequestParam(name = "id") Set<Integer> ids) {
        return service.getByIds(ids);
    }
}
