package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.FilterDTO;
import ua.com.agroswit.productservice.model.Filter;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FilterMapper {

    @Mapping(target = "parentFilterId", source = "parentFilter.id")
    FilterDTO toDTO(Filter filter);

    List<FilterDTO> toDTOs(List<Filter> filters);

    Filter toEntity(FilterDTO dto);

    @AfterMapping
    default void configureRelations(FilterDTO dto, @MappingTarget Filter filter) {
        for (var child : filter.getChildren()) {
            child.setParentFilter(filter);
            configureRelations(dto, child);
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    void update(FilterDTO dto, @MappingTarget Filter entity);
}
