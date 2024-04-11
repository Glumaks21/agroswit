package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

import lombok.*;
import ua.com.agroswit.productservice.dto.validation.constraint.NameUnique;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(length = 300)
    private String description;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentCategory", cascade = CascadeType.REMOVE)
    private Set<Category> subcategories = new HashSet<>();

    @NameUnique
    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryProperty> properties = new HashSet<>();

    public void setProperties(Set<CategoryProperty> properties) {
        this.properties = properties;
        this.properties.forEach(p -> p.setCategory(this));
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
