package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.PackageDTO;
import ua.com.agroswit.productservice.dto.enums.ProductState;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.response.InventoryServiceDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.model.Package;
import ua.com.agroswit.productservice.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT,
        componentModel = SPRING, uses = PackageMapper.class)
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

    @Mapping(target = "product", source = "product")
    @Mapping(target = "article1CId", source = "inventory.article1CId", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "inStock", source = "inventory.quantity", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "state", expression = "java( state )")
    DetailedProductDTO toDetailedDTO(Product product, InventoryServiceDTO inventory, @Context ProductState state, @Context String imageUrl);

    default Boolean mapInStock(Integer quantity) {
        return quantity > 0;
    }

    default DetailedProductDTO toDetailedDTO(Product product, ProductState state, @Context String imageUrl) {
        return toDetailedDTO(product, null, state, imageUrl);
    }

    @Mapping(target = "producer", source = "product.producer.name")
    @Mapping(target = "price", source = "product.packages")
    @Mapping(target = "article1CId", source = "inventory.article1CId", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "inStock", source = "inventory.quantity", nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "state", expression = "java( state )")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    SimpleDetailedProductDTO toSimpleDetailedDTO(Product product, InventoryServiceDTO inventory, @Context ProductState state, @Context String imageUrl);

    default Double mapPackagesToPrice(List<Package> packages) {
        return packages.stream()
                .map(Package::getPrice)
                .min(Double::compare)
                .orElse(null);
    }

    default SimpleDetailedProductDTO toSimpleDetailedDTO(Product product, @Context ProductState state, @Context String imageUrl) {
        return toSimpleDetailedDTO(product, null, state, imageUrl);
    }

    @InheritInverseConfiguration
    Product toEntity(ProductDTO dto);

    default List<Filter> mapToFilter(Set<Integer> ids) {
        if (ids == null) return new ArrayList<>();
        return ids.stream()
                .map(id -> {
                    var filter = new Filter();
                    filter.setId(id);
                    return filter;
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>(ids.size())));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "active", ignore = true)
    void fullUpdate(ProductDTO dto, @MappingTarget Product product);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "active", ignore = true)
    void partialUpdate(ProductDTO dto, @MappingTarget Product product);

    @AfterMapping
    default void configureRelations(ProductDTO dto, @MappingTarget Product product) {
        product.getPackages().forEach(p -> p.setProduct(product));
        product.getPropertyGroups().forEach(g -> g.setProduct(product));
    }
}
