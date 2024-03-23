package ua.com.agroswit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(ProductPropertyValueId.class)
public class ProductPropertyValue {

    @Id
    private Integer productId;

    @Id
    private Integer propertyId;


    @Column(name = "prop_value", nullable = false)
    private String value;

}
