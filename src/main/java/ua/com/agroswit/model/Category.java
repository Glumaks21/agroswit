package ua.com.agroswit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private List<SubCategory> subCategories;

}
