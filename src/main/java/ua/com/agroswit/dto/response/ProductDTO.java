package ua.com.agroswit.dto.response;


import lombok.Builder;
import ua.com.agroswit.model.Producer;

import java.util.Map;

@Builder
public record ProductDTO(Long id,
                         String name,
                         Double price,
                         Producer producer,
                         Map<String, String> description,
                         SubCategoryDTO subcategory) {
}
