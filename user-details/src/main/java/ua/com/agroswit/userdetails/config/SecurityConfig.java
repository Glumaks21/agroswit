package ua.com.agroswit.userdetails.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@Profile("!dev")
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(req -> req
                        .requestMatchers(POST).hasAuthority("SCOPE_write")
                        .requestMatchers(PUT).hasAuthority("SCOPE_write")
                        .requestMatchers(PATCH).hasAuthority("SCOPE_write")
                        .requestMatchers(DELETE).hasAuthority("SCOPE_delete")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(config -> config
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter()))
                )
                .build();
    }

    @Bean
    JWTAuthencactionConverter jwtConverter() {
        return new JWTAuthencactionConverter();
    }

    public static class JWTAuthencactionConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            var scopes = jwt.getClaimAsStringList("scope");
            var roles = jwt.getClaimAsStringList("roles");
            var authorities = new ArrayList<SimpleGrantedAuthority>();
            var scopeAuthorities = scopes.stream()
                    .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                    .toList();
            var roleAuthorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .toList();
            authorities.addAll(scopeAuthorities);
            authorities.addAll(roleAuthorities);
            return new JwtAuthenticationToken(jwt, authorities);
        }
    }
}
