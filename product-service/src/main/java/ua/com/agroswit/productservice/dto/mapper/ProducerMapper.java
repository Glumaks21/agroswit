package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.model.Producer;

@Mapper(componentModel = "spring")
public interface ProducerMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "logoUrl", source = "logoUrl")
    ProducerDTO toDTO(Producer entity, String logoUrl);

    Producer toEntity(ProducerDTO dto);

    @Mapping(target = "id", ignore = true)
    void update(ProducerDTO dto, @MappingTarget Producer entity);

}
