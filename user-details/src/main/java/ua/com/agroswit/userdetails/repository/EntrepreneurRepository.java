package ua.com.agroswit.userdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.userdetails.model.Entrepreneur;

@Repository
public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Integer> {
    boolean existsByIdNumber(String idNumber);
    boolean existsByCompanyName(String companyName);
}
