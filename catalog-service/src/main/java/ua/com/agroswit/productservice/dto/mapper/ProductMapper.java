package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.response.DetailedProductDTO;
import ua.com.agroswit.productservice.dto.response.InventoryServiceDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.response.SimpleDetailedProductDTO;
import ua.com.agroswit.productservice.model.Filter;
import ua.com.agroswit.productservice.model.Package;
import ua.com.agroswit.productservice.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(nullValueCheckStrategy = ALWAYS, nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
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

    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "producerId", source = "product.producer.id")
    @Mapping(target = "filterIds", source = "product.filters")
    @Mapping(target = "article1CId", source = "inventory.article1CId")
    @Mapping(target = "inStock", source = "inventory.quantity")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    DetailedProductDTO toDetailedDTO(Product product, InventoryServiceDTO inventory, @Context String imageUrl);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "producerId", source = "producer.id")
    @Mapping(target = "filterIds", source = "filters")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    DetailedProductDTO toDetailedDTO(Product product, @Context String imageUrl);

    default Boolean mapInStock(Integer quantity) {
        return quantity != null ? quantity > 0: null;
    }

    @Mapping(target = "producer", source = "product.producer.name")
    @Mapping(target = "price", source = "product.packages")
    @Mapping(target = "article1CId", source = "inventory.article1CId")
    @Mapping(target = "inStock", source = "inventory.quantity")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    SimpleDetailedProductDTO toSimpleDetailedDTO(Product product, InventoryServiceDTO inventory, @Context String imageUrl);

    @Mapping(target = "producer", source = "producer.name")
    @Mapping(target = "price", source = "packages")
    @Mapping(target = "imageUrl", expression = "java( imageUrl )")
    SimpleDetailedProductDTO toSimpleDetailedDTO(Product product, @Context String imageUrl);

    default Double mapPackagesToPrice(List<Package> packages) {
        return packages.stream()
                .map(Package::getPrice)
                .min(Double::compare)
                .orElse(null);
    }

    @InheritInverseConfiguration
    Product toEntity(ProductDTO dto);

    default List<Filter> mapToFilter(Set<Integer> ids) {
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
    default void configureRelations(@MappingTarget Product product, ProductDTO dto) {
        product.getPackages().forEach(p -> p.setProduct(product));
        product.getPropertyGroups().forEach(g -> g.setProduct(product));
    }
}
