package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Culture;

import java.util.Optional;

@Repository
public interface CultureRepository extends JpaRepository<Culture, Integer> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Culture> findByNameIgnoreCase(String name);
}
