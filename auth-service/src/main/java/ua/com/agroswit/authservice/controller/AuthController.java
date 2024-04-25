package ua.com.agroswit.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.com.agroswit.authservice.config.JwtProperties;
import ua.com.agroswit.authservice.dto.request.RequestMeta;
import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.JwtResponse;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.service.AuthService;

import java.util.UUID;

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

    @Operation(summary = "Authenticate by user credentials")
    @PostMapping(path = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    JwtResponse login(@RequestBody @Valid LoginDTO dto, HttpServletResponse response) {
        log.info("Received login request with dto: {}", dto);
        var tokens = service.login(dto);
        addRefreshCookie(tokens.refreshToken(), response);
        return tokens;
    }

    private void addRefreshCookie(String refreshToken, HttpServletResponse response) {
        var refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setMaxAge(jwtProps.refreshExpiration());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/v1/auth");
        response.addCookie(refreshCookie);
    }

    @Operation(summary = "Refresh session by refresh token")
    @PostMapping(path = "/refresh", produces = APPLICATION_JSON_VALUE)
    JwtResponse refresh(@RequestBody @Valid RequestMeta meta,
                        @CookieValue(name = "refresh_token") UUID refreshToken,
                        HttpServletResponse response) {
        log.info("Received refresh request with refresh token: {}", refreshToken);
        var tokens = service.refresh(refreshToken, meta);
        addRefreshCookie(tokens.refreshToken(), response);
        return tokens;
    }

    @Operation(summary = "Remove session by refresh token")
    @PostMapping(path = "/logout")
    void logout(@CookieValue(name = "refresh_token") UUID refreshToken, HttpServletResponse response) {
        log.info("Received logout request with token: {}", refreshToken);
        service.logout(refreshToken);

        var removedRefreshCookie = new Cookie("refresh_token", null);
        removedRefreshCookie.setMaxAge(0);
        response.addCookie(removedRefreshCookie);
    }
}
