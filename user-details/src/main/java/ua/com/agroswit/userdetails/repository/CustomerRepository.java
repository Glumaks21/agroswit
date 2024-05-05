package ua.com.agroswit.userdetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.userdetails.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
