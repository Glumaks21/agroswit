package ua.com.agroswit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.agroswit.model.Producer;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    Optional<Producer> findByName(String name);
}
