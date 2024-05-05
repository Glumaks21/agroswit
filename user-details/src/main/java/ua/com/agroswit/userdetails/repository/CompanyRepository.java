package ua.com.agroswit.userdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.userdetails.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsByEgrpou(String grpou);
    boolean existsByCompanyName(String companyName);
}
