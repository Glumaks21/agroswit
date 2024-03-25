package ua.com.agroswit.repository.view;

import ua.com.agroswit.model.PropertyType;

public interface ProductPropertyView {
    String getName();
    PropertyType getType();
    String getValue();
}
