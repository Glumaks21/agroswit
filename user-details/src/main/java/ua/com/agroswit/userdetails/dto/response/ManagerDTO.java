package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;



public record ManagerDTO(
        Integer userId,
        @JsonUnwrapped
        UserServiceDTO user,
        UkrainianDistrict district
) {
}
