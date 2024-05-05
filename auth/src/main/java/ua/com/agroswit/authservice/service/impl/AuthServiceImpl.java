package ua.com.agroswit.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.authservice.config.JwtProperties;
import ua.com.agroswit.authservice.dto.mapper.UserMapper;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.request.RequestMeta;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.exception.ResourceInConflictException;
import ua.com.agroswit.authservice.model.Session;
import ua.com.agroswit.authservice.model.User;
import ua.com.agroswit.authservice.repository.SessionRepository;
import ua.com.agroswit.authservice.repository.UserRepository;
import ua.com.agroswit.authservice.service.AuthService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static ua.com.agroswit.authservice.model.enums.Role.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProps;
    private final UserMapper mapper;


    @Override
    @Transactional
    public UserDTO register(RegistrationDTO registration) {
        if (userRepo.existsByEmail(registration.email())) {
            throw new ResourceInConflictException(String.format(
                    "User with email %s already exists", registration.email())
            );
        }
        if (userRepo.existsByPhone(registration.phone())) {
            throw new ResourceInConflictException(String.format(
                    "User with phone %s already exists", registration.phone())
            );
        }

        var user = mapper.toEntity(registration);
        user.setPassword(passwordEncoder.encode(registration.password()));
        user.setRole(USER);

        log.info("Saving user to db: {}", user);
        return mapper.toDTO(userRepo.save(user));
    }

    private Session createSession(User user, RequestMeta meta) {
        var refreshToken = UUID.randomUUID();
        return Session.builder()
                .userId(user.getId())
                .refreshToken(refreshToken)
                .ip(meta.ip())
                .footprint(meta.footprint())
                .createdAt(LocalDateTime.now())
                .expiresIn(jwtProps.refreshExpiration())
                .build();
    }


    void validateRequest(RequestMeta meta, User user) {
        var sessions = sessionRepo.findAllByUserId(user.getId());

        var usedIps = new HashSet<String>();
        var usedDevices = new HashSet<String>();
        for (var session : sessions) {
            usedIps.add(session.getIp());
            usedDevices.add(session.getFootprint());
        }

        if (!usedIps.contains(meta.ip()) && usedIps.size() == 5) {
            log.trace("Login attempt from IP {} is suspected, used IP: {}", meta.ip(), usedIps);
            log.info("Invalidating sessions: {}", sessions);
            sessionRepo.deleteAll(sessions);
        } else if (!usedDevices.contains(meta.footprint()) && usedDevices.size() == 5) {
            log.trace("Login attempt from device {} is suspected, used devices: {}", meta.footprint(), usedDevices);
            log.info("Invalidating sessions: {}", sessions);
            sessionRepo.deleteAll(sessions);
        }
    }
}
