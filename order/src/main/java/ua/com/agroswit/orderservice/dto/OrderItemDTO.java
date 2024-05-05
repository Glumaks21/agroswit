package ua.com.agroswit.orderservice.dto;

public record OrderItemDTO(
        Integer id,
        Integer packageId,
        Double price,
        Integer count
) {
}
