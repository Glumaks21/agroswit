package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class ProductProperty {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String value;
}
