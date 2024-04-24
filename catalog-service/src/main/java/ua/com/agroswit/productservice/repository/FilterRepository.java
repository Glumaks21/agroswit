package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Filter;

import java.util.List;
import java.util.Set;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Integer> {
    Long countByIdIn(Set<Integer> ids);
    List<Filter> findAllByParentFilterNull();
    boolean existsByName(String name);
}
