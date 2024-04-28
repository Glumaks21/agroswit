package ua.com.agroswit.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.authservice.model.AuthorizedUser;
import ua.com.agroswit.authservice.config.JwtProperties;
import ua.com.agroswit.authservice.dto.mapper.UserMapper;
import ua.com.agroswit.authservice.dto.request.LoginDTO;
import ua.com.agroswit.authservice.dto.request.RegistrationDTO;
import ua.com.agroswit.authservice.dto.request.RequestMeta;
import ua.com.agroswit.authservice.dto.response.JwtResponse;
import ua.com.agroswit.authservice.dto.response.UserDTO;
import ua.com.agroswit.authservice.exception.RequestSuspectedException;
import ua.com.agroswit.authservice.exception.ResourceInConflictException;
import ua.com.agroswit.authservice.exception.ResourceNotFoundException;
import ua.com.agroswit.authservice.model.Session;
import ua.com.agroswit.authservice.model.User;
import ua.com.agroswit.authservice.repository.SessionRepository;
import ua.com.agroswit.authservice.repository.UserRepository;
import ua.com.agroswit.authservice.service.AuthService;
import ua.com.agroswit.authservice.utils.JwtUtil;

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
    private final JwtUtil jwtUtil;
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

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(LoginDTO login) {
        if (login.email() == null && login.phone() == null) {
            throw new IllegalArgumentException("Email or phone number is required to login");
        }

        var user = login.email() != null
                ? userRepo.findByEmail(login.email())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with email %s not found", login.email()))
                )
                : userRepo.findByPhone(login.phone())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with phone number %s not found", login.phone()))
                );
        validateRequest(login.meta(), user);

        var session = createSession(user, login.meta());
        log.info("Saving session: {}", session);
        sessionRepo.save(session);

        var refreshToken = session.getRefreshToken();
        var accessToken = jwtUtil.generate(AuthorizedUser.from(user));
        return new JwtResponse(accessToken, refreshToken.toString());
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

    @Override
    @Transactional(readOnly = true)
    public JwtResponse refresh(UUID refreshToken, RequestMeta meta) {
        var sessionOptional = sessionRepo.findByRefreshToken(refreshToken);
        if (sessionOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Session for token %s not found", refreshToken.toString()));
        }

        var currentSession = sessionOptional.get();
        log.info("Removing current session {}", currentSession);
        sessionRepo.delete(currentSession);

        if (!currentSession.getFootprint().equals(meta.footprint())) {
            throw new RequestSuspectedException("Footprint are not the same");
        }

        var user = userRepo.findById(currentSession.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "User with id %d not found", currentSession.getUserId()))
                );
        var newSession = createSession(user, meta);
        log.info("Saving new session: {}", newSession);
        sessionRepo.save(newSession);

        var newRefreshToken = newSession.getRefreshToken();
        var accessToken = jwtUtil.generate(AuthorizedUser.from(user));
        return new JwtResponse(accessToken, newRefreshToken.toString());
    }

    @Override
    public void logout(UUID refreshToken) {
        sessionRepo.findByRefreshToken(refreshToken)
                .ifPresent(s -> {
                    log.info("Removing session: {}", s);
                    sessionRepo.delete(s);
                });
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
