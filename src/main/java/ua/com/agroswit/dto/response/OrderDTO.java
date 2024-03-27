package ua.com.agroswit.dto.response;

import ua.com.agroswit.model.enums.OrderStateE;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderDTO(
        Integer id,
        Integer userId,
        OrderStateE state,
        Set<OrderItemDTO> items,
        LocalDateTime createdAt) {

    public record OrderItemDTO(
            Integer productId,
            Integer packageId,
            Integer count) {

    }
}
