package ua.com.agroswit.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.JwtResponseDTO;
import ua.com.agroswit.authservice.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    JwtResponseDTO register(@RequestBody RegistrationDTO dto) {
        log.info("Received registration request with dto: {}", dto);
        return service.register(dto);
    }

    @PostMapping("/login")
    JwtResponseDTO login(@RequestBody LoginDTO dto) {
        log.info("Received login request with dto: {}", dto);
        return service.login(dto);
    }
}
