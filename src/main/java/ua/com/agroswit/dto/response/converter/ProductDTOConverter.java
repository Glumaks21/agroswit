package ua.com.agroswit.dto.response.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.com.agroswit.dto.response.ProductDTO;
import ua.com.agroswit.dto.response.ProductDTO.PackageDTO;
import ua.com.agroswit.dto.response.ProductDTO.ProductPropertyDTO;
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
                .imageUrl(product.getImageUrl())
                .name(product.getName())
                .producer(product.getProducer())
                .subcategoryId(product.getCategory().getId())
                .properties(properties.stream()
                        .map(p -> new ProductPropertyDTO(
                                p.getName(), p.getType(), p.getValue())
                        )
                        .collect(Collectors.toSet())
                )
                .packages(product.getPackages().stream()
                        .map(p -> new PackageDTO(p.getPrice(), p.getVolume(), p.getUnit())
                        )
                        .collect(Collectors.toSet()))
                .article1CId(product.getArticle1CId())
                .build();
    }


}
