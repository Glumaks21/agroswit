package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.response.InventoryServiceDTO;
import ua.com.agroswit.productservice.dto.request.ProductModifiableDTO;
import ua.com.agroswit.productservice.dto.response.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.model.Package;
import ua.com.agroswit.productservice.model.Product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "producerId", source = "producer.id")
    @Mapping(target = "filterIds", source = "filters")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    ProductDTO toDTO(Product product, @Context String imageUrl);

    default Set<Integer> mapToFilterIds(List<Filter> filters) {
        return filters.stream()
                .map(Filter::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "categoryId", source = "entity.category.id")
    @Mapping(target = "producerId", source = "entity.producer.id")
    @Mapping(target = "filterIds", source = "entity.filters")
    @Mapping(target = "inStock", source = "idto.quantity")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    DetailedProductDTO toDetailedDTO(Product entity, InventoryServiceDTO idto, @Context String imageUrl);

    default Boolean mapToInStock(Integer quantity) {
        return quantity > 0;
    }

    @Mapping(target = "producer", source = "entity.producer.name")
    @Mapping(target = "price", source = "entity.packages")
    @Mapping(target = "article1CId", source = "idto.article1CId")
    @Mapping(target = "inStock", source = "idto.quantity")
    @Mapping(target = "imageUrl", expression = "java( imageUrl) ")
    SimpleDetailedProductDTO toSimpleDetailedDTO(Product entity, InventoryServiceDTO idto, @Context String imageUrl);

    default Double mapToPrice(List<Package> packages) {
        return packages.stream()
                .map(Package::getPrice)
                .min(Double::compare)
                .orElse(null);
    }

    @Mapping(target = "packages", nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
    @Mapping(target = "propertyGroups", nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
    Product toEntity(ProductModifiableDTO dto);

    void fullUpdate(ProductModifiableDTO dto, @MappingTarget Product product);

    @AfterMapping
    default void configureRelations(@MappingTarget Product product, ProductModifiableDTO dto) {
        product.getPackages().forEach(p -> p.setProduct(product));
        product.getPropertyGroups().forEach(g -> g.setProduct(product));
    }
}
