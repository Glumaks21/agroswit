package ua.com.agroswit.dto.mappers;

import org.mapstruct.Mapper;
import ua.com.agroswit.dto.ProducerDTO;
import ua.com.agroswit.model.Producer;

@Mapper(componentModel = "spring")
public interface ProducerMapper {

    ProducerDTO toDTO(Producer entity);
    Producer toEntity(ProducerDTO dto);

}
