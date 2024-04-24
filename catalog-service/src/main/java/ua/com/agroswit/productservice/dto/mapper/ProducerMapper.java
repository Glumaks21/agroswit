package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.model.Producer;

@Mapper(componentModel = "spring")
public interface ProducerMapper {

    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    ProducerDTO toDTO(Producer entity, @Context String logoUrl);

    Producer toEntity(ProducerDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    void update(ProducerDTO dto, @MappingTarget Producer entity);

}
