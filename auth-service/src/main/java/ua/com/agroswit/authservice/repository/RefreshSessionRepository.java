package ua.com.agroswit.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.agroswit.authservice.model.RefreshSession;

public interface RefreshSessionRepository extends CrudRepository<RefreshSession, Integer> {
}
