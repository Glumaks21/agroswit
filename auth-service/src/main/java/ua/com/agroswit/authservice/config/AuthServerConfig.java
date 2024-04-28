package ua.com.agroswit.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.OPENID;

@Configuration
@RequiredArgsConstructor
public class AuthServerConfig {

    private final JwtProperties jwtProps;


    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // Redirect to the login page when not authenticated from the authorization endpoint
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        )
                .oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                RegisteredClient.withId("admin-panel")
                        .clientName("Administration panel")
                        .clientId("client")
                        .clientSecret("$2a$12$WB.OsMfEo6ZewzOE0O86Yew/8Uj0y5AocVDXmqy0dWO0XCLLK3VWS")
                        .redirectUri("http://localhost:9000/code")
                        .scopes(scopes -> scopes
                                .addAll(List.of(OPENID, "read", "write", "delete"))
                        )
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .authorizationGrantType(AUTHORIZATION_CODE)
                        .authorizationGrantType(REFRESH_TOKEN)
                        .tokenSettings(TokenSettings.builder()
                                .authorizationCodeTimeToLive(Duration.of(30, SECONDS))
                                .accessTokenTimeToLive(Duration.of(jwtProps.accessExpiration(), MINUTES))
                                .refreshTokenTimeToLive(Duration.of(jwtProps.refreshExpiration(), MINUTES))
                                .reuseRefreshTokens(false)
                                .build())
                        .build()
//                RegisteredClient.withId("mobile-client")
//                        .clientName("Mobile client")
//                        .clientId("client")
//                        .clientSecret("$2a$12$WB.OsMfEo6ZewzOE0O86Yew/8Uj0y5AocVDXmqy0dWO0XCLLK3VWS")
//                        .redirectUri("http://localhost:3000/code")
//                        .scopes(scopes -> scopes
//                                .addAll(List.of(OPENID, "read", "write"))
//                        )
//                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
//                        .authorizationGrantType(CLIENT_CREDENTIALS)
//                        .authorizationGrantType(AUTHORIZATION_CODE)
//                        .authorizationGrantType(REFRESH_TOKEN)
//                        .tokenSettings(TokenSettings.builder()
//                                .authorizationCodeTimeToLive(Duration.of(30, SECONDS))
//                                .accessTokenTimeToLive(Duration.of(jwtProps.accessExpiration(), SECONDS))
//                                .refreshTokenTimeToLive(Duration.of(jwtProps.refreshExpiration(), SECONDS))
//                                .reuseRefreshTokens(false)
//                                .build())
//                        .build()
        );
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            var userDetails = context.getPrincipal();
            var authorities = userDetails.getAuthorities();
            context.getClaims().claims(claims ->
                    claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList()));
        };
    }

//    @Bean
//    public JWKSource<SecurityContext> jwkSource() {
//        JWKSet jwkSet = new JWKSet(rsaKey());
//        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//    }
//
//    @Bean
//    public RSAKey rsaKey() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        return new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }
//
//    private static KeyPair generateRsaKey() {
//        KeyPair keyPair;
//        try {
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            keyPair = keyPairGenerator.generateKeyPair();
//        }
//        catch (Exception ex) {
//            throw new IllegalStateException(ex);
//        }
//        return keyPair;
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//    }
//
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder().build();
//    }
}
