package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.FilterDTO;
import ua.com.agroswit.productservice.model.Filter;

@Mapper(componentModel = "spring")
public interface FilterMapper {

    @Mapping(target = "parentFilterId", source = "parentFilter.id")
    FilterDTO toDTO(Filter filter);

    Filter toEntity(FilterDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    void update(FilterDTO dto, @MappingTarget Filter entity);
}
