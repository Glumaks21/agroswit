package ua.com.agroswit.userdetails.dto;

public record OrderServiceDTO(
        Integer id,
        Integer packageId,
        Integer userId,
        Double price,
        Integer count
) {
}
