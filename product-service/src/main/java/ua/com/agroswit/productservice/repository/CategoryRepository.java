package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE " +
            "(SELECT count(*) FROM Category sc WHERE sc.parentCategory.id = c.id) = 0")
    List<Category> findAllLowLevelCategories();
}
