package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class CategoryPropertyGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(length = 30, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ElementCollection
    @CollectionTable(name = "category_property",
            joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "name")
    private Set<String> properties = new HashSet<>();

}
