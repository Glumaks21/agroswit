package ua.com.agroswit.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.LoginDTO;
import ua.com.agroswit.dto.request.RegistrationDTO;
import ua.com.agroswit.model.RoleE;
import ua.com.agroswit.model.User;
import ua.com.agroswit.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public JwtResponseDTO register(RegistrationDTO dto) {
        if (userRepository.existsByLogin(dto.login())) {
            //TODO Add own exception
            throw new RuntimeException();
        }

        var user = User.builder()
                .login(dto.login())
                .password(passwordEncoder.encode(dto.password()))
                .role(RoleE.USER)
                .build();

        log.info("Saving user to db: {}", user);
        userRepository.save(user);

        var accessToken = jwtService.generate(user);
        var refreshToken = jwtService.generate(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public JwtResponseDTO login(LoginDTO dto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password()));

        var user = userRepository.findByLogin(dto.login()).get();
        var accessToken = jwtService.generate(user);
        var refreshToken = jwtService.generate(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }
}
