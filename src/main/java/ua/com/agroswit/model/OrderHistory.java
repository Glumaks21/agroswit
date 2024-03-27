package ua.com.agroswit.model;

import jakarta.persistence.*;
import ua.com.agroswit.model.enums.OrderStateE;

import java.time.LocalDateTime;

@Entity
public class OrderHistory {

    @Id
    private Integer orderId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User changedBy;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "order_state",
            joinColumns = @JoinColumn(name = "state_id", nullable = false)
    )
    private OrderStateE state;

    private LocalDateTime modifiedAt;

}
