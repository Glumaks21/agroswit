package ua.com.agroswit.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.SignInRequest;
import ua.com.agroswit.dto.request.SignUpRequest;
import ua.com.agroswit.exception.ResourceInConflictStateException;
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

    public JwtResponseDTO signUp(SignUpRequest dto) {
        if (userRepository.existsByLogin(dto.login())) {
            throw new ResourceInConflictStateException(
                    "User with login " + dto.login() + " already exists");
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

    public JwtResponseDTO signIn(SignInRequest dto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password()));

        var user = userRepository.findByLogin(dto.login()).get();
        var accessToken = jwtService.generate(user);
        var refreshToken = jwtService.generate(user);

        return new JwtResponseDTO(accessToken, refreshToken);
    }
}
