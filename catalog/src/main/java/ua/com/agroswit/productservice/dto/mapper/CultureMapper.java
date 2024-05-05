package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.Mapper;
import ua.com.agroswit.productservice.dto.CultureDTO;
import ua.com.agroswit.productservice.model.Culture;

@Mapper(componentModel = "spring")
public interface CultureMapper {
    CultureDTO toDTO(Culture culture);
    Culture toEntity(CultureDTO cultureDTO);
}
