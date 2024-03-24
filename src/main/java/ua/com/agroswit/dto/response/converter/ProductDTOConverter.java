package ua.com.agroswit.dto.response.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.model.Product;
import ua.com.agroswit.repository.view.ProductPropertyView;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class ProductDTOConverter {

    public ProductDTO convert(Product product, Collection<ProductPropertyView> properties) {
        return ProductDTO.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .producer(product.getProducer())
                .subcategoryId(product.getCategory().getId())
                .description(properties.stream()
                        .map(p -> new ProductDTO.ProductPropertyDTO(p.getName(), p.getValue()))
                        .collect(Collectors.toSet())
                )
                .article1CId(product.getArticle1CId())
                .build();
    }


}
