package ua.com.agroswit.dto;


import lombok.Builder;

@Builder
public record ProductDTO(Long id, String name, Double price) {
}
