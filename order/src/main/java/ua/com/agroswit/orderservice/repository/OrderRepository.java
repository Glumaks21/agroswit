package ua.com.agroswit.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.agroswit.orderservice.model.Order;
import ua.com.agroswit.orderservice.model.enums.OrderState;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Modifying
    @Query("UPDATE Order SET state = :state WHERE id = :id")
    void updateOrderStateBy(Integer id, OrderState state);
}
