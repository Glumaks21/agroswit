package ua.com.agroswit.orderservice.dto;

import ua.com.agroswit.orderservice.model.enums.OrderState;

import java.time.LocalDateTime;

public record OrderHistoryDTO(
        Integer userId,
        OrderState state,
        LocalDateTime modifiedAt) {
}
