package ua.com.agroswit.dto;


import java.time.LocalDateTime;
import java.util.Set;

public record OrderDTO(
        Integer id,
        Integer customerId,
        String state,
        Set<OrderItemDTO> items,
        LocalDateTime createdAt) {

    public record OrderItemDTO(
            Integer productId,
            Integer packageId,
            Integer count) {

    }
}
