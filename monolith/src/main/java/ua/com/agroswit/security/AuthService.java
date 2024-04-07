package ua.com.agroswit.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.LoginRequest;
import ua.com.agroswit.dto.request.RegistrationRequest;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.model.enums.MailType;
import ua.com.agroswit.model.enums.RoleE;
import ua.com.agroswit.model.User;
import ua.com.agroswit.repository.UserRepository;
import ua.com.agroswit.service.MailSenderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSenderService mailService;
    private final AuthenticationManager authManager;

    public JwtResponseDTO register(RegistrationRequest dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceInConflictStateException(
                    "User with email " + dto.email() + " already exists");
        }
        if (userRepository.existsByPhone(dto.phone())) {
            throw new ResourceInConflictStateException(
                    "User with phone " + dto.phone() + " already exists");
        }

        var user = User.builder()
                .email(dto.email())
                .phone(dto.phone())
                .password(passwordEncoder.encode(dto.password()))
                .role(RoleE.USER)
                .build();

        log.info("Saving user to db: {}", user);
        userRepository.save(user);

        mailService.sendEmail(user, MailType.REGISTRATION);

        var accessToken = jwtService.generate(user);
        var refreshToken = jwtService.generate(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public JwtResponseDTO login(LoginRequest dto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        var user = userRepository.findByEmail(dto.email()).get();
        var accessToken = jwtService.generate(user);
        var refreshToken = jwtService.generate(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }
}
