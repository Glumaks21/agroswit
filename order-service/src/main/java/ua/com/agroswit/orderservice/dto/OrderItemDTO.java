package ua.com.agroswit.orderservice.dto;

public record OrderItemDTO(
        Integer productId,
        Integer packageId,
        Integer count) {

}
