package ua.com.agroswit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class OrderHistory {

    @Id
    @Column(nullable = false)
    private Integer orderId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User changedBy;

    @OneToOne
    @JoinColumn(name = "state", nullable = false)
    private OrderState state;

    private LocalDateTime modifiedAt;

}
