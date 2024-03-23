package ua.com.agroswit.dto.response;


import lombok.Builder;
import ua.com.agroswit.model.Producer;

import java.util.Collection;
import java.util.Map;

@Builder
public record ProductDTO(int id,
                         String name,
                         double price,
                         Producer producer,
                         Collection<ProductPropertyDTO> description,
                         Integer article1CId,
                         Integer subcategoryId) {
    public record ProductPropertyDTO(String name, String value) {}
}
