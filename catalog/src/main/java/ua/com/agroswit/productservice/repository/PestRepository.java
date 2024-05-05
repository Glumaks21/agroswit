package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Pest;

@Repository
public interface PestRepository extends JpaRepository<Pest, Integer> {
    boolean existsByName(String name);
}
