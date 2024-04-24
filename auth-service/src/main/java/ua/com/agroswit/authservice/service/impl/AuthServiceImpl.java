package ua.com.agroswit.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.agroswit.authservice.dto.mapper.UserMapper;
import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.response.JwtResponseDTO;
import ua.com.agroswit.authservice.exception.ResourceInConflictException;
import ua.com.agroswit.authservice.model.enums.Role;
import ua.com.agroswit.authservice.repository.UserRepository;
import ua.com.agroswit.authservice.service.AuthService;
import ua.com.agroswit.authservice.service.JwtService;

import static ua.com.agroswit.authservice.model.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper mapper;
    private final RedisTemplate<Integer, String> redisTemplate;

    @Override
    public JwtResponseDTO register(RegistrationDTO registration) {
        if (userRepo.existsByEmail(registration.getEmail())) {
            throw new ResourceInConflictException(String.format(
                    "User with email %s already exists", registration.getEmail())
            );
        } else if (userRepo.existsByPhone(registration.getPhone())) {
            throw new ResourceInConflictException(String.format(
                    "User with phone %s already exists", registration.getPhone())
            );
        }

        var user = mapper.toEntity(registration);
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRole(USER);

        log.info("Saving user to db: {}", user);
        userRepo.save(user);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    @Override
    public JwtResponseDTO login(LoginDTO login) {
        return null;
    }

}
