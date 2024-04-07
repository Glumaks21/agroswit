package ua.com.agroswit.orderservice.dto;

import ua.com.agroswit.orderservice.model.enums.OrderStateE;

import java.time.LocalDateTime;

public record OrderHistoryDTO(
        Integer userId,
        OrderStateE state,
        LocalDateTime modifiedAt) {
}
