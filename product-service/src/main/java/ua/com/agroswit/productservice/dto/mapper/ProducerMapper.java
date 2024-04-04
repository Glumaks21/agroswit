package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.productservice.dto.ProducerDTO;
import ua.com.agroswit.productservice.model.Producer;

@Mapper(componentModel = "spring")
public interface ProducerMapper {

    ProducerDTO toDTO(Producer entity);

    Producer toEntity(ProducerDTO dto);

}
