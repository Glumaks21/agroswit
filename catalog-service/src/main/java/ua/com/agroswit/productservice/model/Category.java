package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

import lombok.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Size(max = 45, message = "Logo must be lower than 45 symbols")
    @Column(length = 45)
    private String logo;

    @Size(min = 2, max = 50, message = "Name length must be between 2 and 50")
    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Size(min = 2, max = 300, message = "Description must be between 2 and 300")
    @Column(length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.REMOVE)
    private List<Category> subcategories = new ArrayList<>();

    @ToString.Exclude
    @Valid
    @OneToMany(mappedBy = "category", cascade = ALL, orphanRemoval = true)
    private List<CategoryPropertyGroup> propertyGroups = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "category_filters",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "filter_id"))
    private List<Filter> filters = new ArrayList<>();


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            description = description.trim();
        }
        this.description = description;
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
