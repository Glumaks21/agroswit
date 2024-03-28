package ua.com.agroswit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record SimplifiedCategoryDTO(
        Integer id,
        String name,
        String description
) {
}
