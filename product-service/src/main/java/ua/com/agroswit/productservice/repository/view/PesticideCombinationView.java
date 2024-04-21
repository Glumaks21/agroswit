package ua.com.agroswit.productservice.repository.view;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public interface PesticideCombinationView {
    @JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "pesticideId")
    Integer getPesticideId();

    @JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "mainProportion")
    Integer getMainProportion();

    @JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "pesticideProportion")
    Integer getPesticideProportion();
}
