package ua.com.agroswit.authservice.service;

import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.JwtResponseDTO;

public interface AuthService {
    JwtResponseDTO register(RegistrationDTO registration);
    JwtResponseDTO login(LoginDTO login);
}
