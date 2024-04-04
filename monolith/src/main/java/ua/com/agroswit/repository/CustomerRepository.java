package ua.com.agroswit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
