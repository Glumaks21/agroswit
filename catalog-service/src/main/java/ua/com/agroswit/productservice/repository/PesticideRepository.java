package ua.com.agroswit.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.productservice.model.Pesticide;

import java.util.List;

@Repository
public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {

}
