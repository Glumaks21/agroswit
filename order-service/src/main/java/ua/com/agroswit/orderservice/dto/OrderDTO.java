package ua.com.agroswit.orderservice.dto;


import java.time.LocalDateTime;
import java.util.Set;

public record OrderDTO(
        Integer id,
        Integer customerId,
        String state,
        Set<OrderItemDTO> items,
        LocalDateTime createdAt) {
}
