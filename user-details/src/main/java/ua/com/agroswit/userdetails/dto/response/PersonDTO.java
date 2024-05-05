package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ua.com.agroswit.userdetails.model.enums.CustomerType;
import ua.com.agroswit.userdetails.model.enums.Sex;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.time.LocalDate;



public record PersonDTO(
        @JsonUnwrapped
        CustomerDTO customer,
        String idNumber,
        Sex sex,
        LocalDate birthDate
) {
}
