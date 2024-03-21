package ua.com.agroswit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class ProductProperty {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String name;

}
