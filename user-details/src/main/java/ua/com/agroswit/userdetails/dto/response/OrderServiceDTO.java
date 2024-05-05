package ua.com.agroswit.userdetails.dto.response;

public record OrderServiceDTO(
        Integer id,
        Integer packageId,
        Integer userId,
        Double price,
        Integer count
) {
}
