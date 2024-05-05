package ua.com.agroswit.userdetails.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record EntrepreneurDTO(
        @JsonUnwrapped
        CustomerDTO customer,
        String companyName,
        String idNumber
) {
}
