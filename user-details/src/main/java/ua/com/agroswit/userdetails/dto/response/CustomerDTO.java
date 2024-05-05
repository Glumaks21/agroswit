package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ua.com.agroswit.userdetails.model.enums.CustomerType;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

public record CustomerDTO(
        Integer userId,
        @JsonUnwrapped
        UserServiceDTO user,
        CustomerType type,
        UkrainianDistrict district,
        String region,
        String settlement
) {
}
