package ua.com.agroswit.authservice.service;

import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.request.RequestMeta;
import ua.com.agroswit.authservice.dto.response.JwtResponse;
import ua.com.agroswit.authservice.dto.response.UserDTO;

import java.util.UUID;

public interface AuthService {
    UserDTO register(RegistrationDTO registration);
    JwtResponse login(LoginDTO login);
    JwtResponse refresh(UUID refreshToken, RequestMeta meta);
    void logout(UUID refreshToken);
}
