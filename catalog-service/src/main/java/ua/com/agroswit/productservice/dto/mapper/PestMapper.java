package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.productservice.dto.PestDTO;
import ua.com.agroswit.productservice.model.Pest;

@Mapper(componentModel = "spring")
public interface PestMapper {
    PestDTO toDTO(Pest pest);
    Pest toEntity(PestDTO dto);
}
