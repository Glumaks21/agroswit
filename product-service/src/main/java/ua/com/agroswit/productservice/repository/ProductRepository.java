package ua.com.agroswit.productservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllByActiveTrue(Pageable pageable);
    Page<Product> findAllByProducerId(Integer id, Pageable pageable);
    Page<Product> findAllByActiveTrueAndProducerId(Integer id, Pageable pageable);
    Page<Product> findAllByArticle1CIdIn(Collection<Integer> ids, Pageable pageable);
    boolean existsByArticle1CId(Integer article1CId);
    Optional<Product> findByArticle1CId(Integer article1CId);
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.active=false WHERE p.id=:id")
    void deactivateById(@Param("id") Integer id);
}
