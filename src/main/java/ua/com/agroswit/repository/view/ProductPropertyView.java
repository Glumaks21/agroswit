package ua.com.agroswit.repository.view;

import ua.com.agroswit.model.enums.PropertyTypeE;

public interface ProductPropertyView {
    String getName();
    PropertyTypeE getType();
    String getValue();
}
