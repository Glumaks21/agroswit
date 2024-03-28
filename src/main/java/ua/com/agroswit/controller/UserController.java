package ua.com.agroswit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.UserDTO;
import ua.com.agroswit.service.UserService;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Retrieve all users")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Collection<UserDTO> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Retrieve user by ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    UserDTO getById(@PathVariable Integer id) {
        return userService.getById(id);
    }
}
