package ua.com.agroswit.dto.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.ProductDTO;
import ua.com.agroswit.model.Product;

import java.util.function.Function;

@Component
public class ProductDTOConverter implements Function<Product, ProductDTO> {
    @Override
    public ProductDTO apply(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .build();
    }
}
