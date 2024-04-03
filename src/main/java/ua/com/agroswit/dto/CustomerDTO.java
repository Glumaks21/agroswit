package ua.com.agroswit.dto;

import lombok.Builder;
import ua.com.agroswit.model.enums.CustomerTypeE;

import java.util.Map;

@Builder
public record CustomerDTO(
        Integer id,
        CustomerTypeE type,
        String region,
        String district,
        String settlement,
        Map<String, String> info
) {
}
