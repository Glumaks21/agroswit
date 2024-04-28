package ua.com.agroswit.productservice.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MeasurementUnitE {
    @JsonProperty("seed")
    SEED,
    @JsonProperty("kg")
    KG,
    @JsonProperty("liter")
    LITER
}
