package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDate;

public record CompanyDTO(
        @JsonUnwrapped
        CustomerDTO customer,
        String companyName,
        String egrpou,
        LocalDate incorporationDate
) {
}
