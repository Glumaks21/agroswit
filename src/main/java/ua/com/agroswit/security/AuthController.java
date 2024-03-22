package ua.com.agroswit.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.agroswit.dto.request.SignInRequest;
import ua.com.agroswit.dto.request.SignUpRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication", description = "Authentication management API")
@RestController
@RequestMapping(path = "/api/v1/auth",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully registered"),
            @ApiResponse(
                    responseCode = "409",
                    description = "When user login already registered")
    })
    @PostMapping("/signup")
    JwtResponseDTO signUp(@RequestBody SignUpRequest dto) {
        return service.signUp(dto);
    }

    @Operation(summary = "Authenticate already registered user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logined"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credentials are incorrect")
    })
    @PostMapping("/signin")
    JwtResponseDTO signIn(@RequestBody SignInRequest dto) {
        return service.signIn(dto);
    }
}
