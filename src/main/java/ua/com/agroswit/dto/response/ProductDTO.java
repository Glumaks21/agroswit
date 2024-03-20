package ua.com.agroswit.dto.response;


import lombok.Builder;
import ua.com.agroswit.model.Producer;

@Builder
public record ProductDTO(Long id, String name, Double price, Producer producer) {
}
