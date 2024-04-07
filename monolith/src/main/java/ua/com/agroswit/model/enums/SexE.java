package ua.com.agroswit.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

public enum SexE {
    MALE, FEMALE;

    @Converter(autoApply = true)
    public static class SexConverter implements AttributeConverter<SexE, Boolean> {

        @Override
        public Boolean convertToDatabaseColumn(SexE sex) {
            return sex == null ? null : sex.ordinal() != 0;
        }

        @Override
        public SexE convertToEntityAttribute(Boolean sex) {
            if (sex == null) {
                return null;
            }
            return sex? SexE.FEMALE: SexE.MALE;
        }
    }
}
