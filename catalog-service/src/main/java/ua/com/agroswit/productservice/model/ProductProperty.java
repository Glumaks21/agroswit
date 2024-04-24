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

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50")
    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 255, message = "Value must be lower then 255 symbols")
    @NotBlank( message = "Value must not be blank")
    @Column(nullable = false, length = 255)
    private String value;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }
}
