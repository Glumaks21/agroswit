package ua.com.agroswit.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.authservice.config.JwtProperties;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.service.AuthService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication management API")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final JwtProperties jwtProps;


    @Operation(summary = "Create new user")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    UserDTO register(@RequestBody @Valid RegistrationDTO dto) {
        log.info("Received registration request with dto: {}", dto);
        return service.register(dto);
    }
}
