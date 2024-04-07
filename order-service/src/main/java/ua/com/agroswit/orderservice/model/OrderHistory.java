package ua.com.agroswit.orderservice.model;

import jakarta.persistence.*;
import ua.com.agroswit.orderservice.model.enums.OrderStateE;

import java.time.LocalDateTime;

@Entity
public class OrderHistory {

    @Id
    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private OrderStateE state;

    private LocalDateTime modifiedAt;

}
