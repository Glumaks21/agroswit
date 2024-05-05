package ua.com.agroswit.authservice.service;

import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.UserDTO;

public interface AuthService {
    UserDTO register(RegistrationDTO registration);
}
