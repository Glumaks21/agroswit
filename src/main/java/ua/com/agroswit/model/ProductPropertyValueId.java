package ua.com.agroswit.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductPropertyValueId implements Serializable {

    private Long productId;

    private Integer propertyId;

}
