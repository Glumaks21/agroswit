package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.request.PesticideModifiableDTO;
import ua.com.agroswit.productservice.dto.response.PesticideDTO;
import ua.com.agroswit.productservice.model.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface PesticideMapper {

    @Mapping(target = "pests", qualifiedByName = "mapPestsToDTO")
    @Mapping(target = "cultures", qualifiedByName = "mapCulturesToDTO")
    @Mapping(target = "product", source = "pesticide")
    PesticideDTO toDTO(Pesticide pesticide, @Context String logoUrl);

    @Named("mapPestsToDTO")
    default Set<String> mapPestsToDTO(List<Pest> pests) {
        return pests.stream()
                .map(Pest::getName)
                .collect(toSet());
    }

    @Named("mapCulturesToDTO")
    default Set<String> mapCulturesToDTO(List<PesticideCulture> cultures) {
        return cultures.stream()
                .map(p -> p.getCulture().getName())
                .collect(toSet());
    }

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "cultures", ignore = true)
    @Mapping(target = "propertyGroups",
            nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
    @Mapping(target = "packages",
            nullValuePropertyMappingStrategy = IGNORE, nullValueCheckStrategy = ALWAYS)
    Pesticide toEntity(PesticideModifiableDTO dto);

    void merge(Product product, @MappingTarget Pesticide pesticide);

}
