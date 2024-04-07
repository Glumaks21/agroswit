package ua.com.agroswit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.view.ProductPropertyView;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);
    boolean existsByArticle1CId(Integer article1CId);
    Optional<Product> findByName(String name);

    Page<Product> findByProducerId(Integer id, Pageable pageable);

    @Query(value = "SELECT pp.name AS name, pp.type as type, ppv.value AS value FROM Product p " +
            "INNER JOIN ProductPropertyValue ppv ON p.id = ppv.productId " +
            "INNER JOIN CategoryProperty pp ON ppv.propertyId = pp.id " +
            "WHERE p.id = ?1")
    List<ProductPropertyView> findAllPropertiesById(Integer id);
}
