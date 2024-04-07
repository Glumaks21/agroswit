package ua.com.agroswit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
