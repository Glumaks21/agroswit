package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.dto.request.ProducerModifiableDTO;
import ua.com.agroswit.productservice.model.Producer;

@Mapper(componentModel = "spring")
public interface ProducerMapper {

    @Mapping(target = "logoUrl", expression = "java( logoUrl )")
    ProducerDTO toDTO(Producer entity, @Context String logoUrl);

    Producer toEntity(ProducerModifiableDTO dto);

    @Mapping(target = "id", ignore = true)
    void update(ProducerModifiableDTO dto, @MappingTarget Producer entity);

}
