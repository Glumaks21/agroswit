package ua.com.agroswit.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.request.LoginDTO;
import ua.com.agroswit.dto.request.RegistrationDTO;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    JwtResponseDTO register(@RequestBody RegistrationDTO dto) {
        return service.register(dto);
    }

    @PostMapping("/login")
    JwtResponseDTO login(@RequestBody LoginDTO dto) {
        return service.login(dto);
    }
}
