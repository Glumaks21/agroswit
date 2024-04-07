package ua.com.agroswit.dto;

import ua.com.agroswit.model.enums.SexE;

import java.time.LocalDate;

public record PersonInfoDTO(
        String name,
        String surname,
        String patronymic,
        SexE sex,
        LocalDate birthDate
) {
}
