package ua.com.agroswit.authservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.com.agroswit.authservice.model.enums.Role;

import java.util.Collection;
import java.util.List;

public record UserDetailsImpl  (
    String email,
    String password,
    Role role) implements UserDetails {

    public static UserDetailsImpl from(User user) {
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    @Override
    public String getUsername() {
        return email();
    }

    @Override
    public String getPassword() {
        return password();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}