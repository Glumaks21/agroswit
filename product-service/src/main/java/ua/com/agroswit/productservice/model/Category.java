package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
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

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.REMOVE)
    private Set<Category> subcategories = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryProperty> properties = new ArrayList<>();

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
