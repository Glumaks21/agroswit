package ua.com.agroswit.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.agroswit.authservice.model.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends CrudRepository<Session, Integer> {
    List<Session> findAllByUserId(Integer userId);
    Optional<Session> findByRefreshToken(UUID refreshToken);
}
