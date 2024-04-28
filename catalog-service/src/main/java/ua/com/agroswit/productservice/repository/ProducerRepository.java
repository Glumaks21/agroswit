package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Producer;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    Optional<Producer> findByNameIgnoreCase(String name);
    Boolean existsByName(String name);
}
