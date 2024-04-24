package ua.com.agroswit.productservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Product;

import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
    Page<Product> findAllByCategoryId(Integer id, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.active=:state WHERE p.id IN :ids")
    void updateActiveByIds(Collection<Integer> ids, Boolean state);

    @Modifying
    @Query("DELETE FROM ProductPropertyGroup WHERE product.id = :id")
    void deleteAllPropertyGroupsById(Integer id);

}
