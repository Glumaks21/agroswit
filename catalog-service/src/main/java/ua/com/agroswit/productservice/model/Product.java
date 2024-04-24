package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ua.com.agroswit.productservice.dto.validation.Groups;
import ua.com.agroswit.productservice.model.enums.MeasurementUnitE;
import ua.com.agroswit.productservice.model.enums.ProductType;

import java.util.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = JOINED)
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Size(max = 45, message = "Image must be lower than 45 symbols")
    @Column(length = 45)
    private String image;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 symbols")
    @NotBlank(message = "Name must not be blank")
    @Column(length = 50, nullable = false)
    private String name;

    @Size(min = 10, max = 100, message = "Short description must be between 10 and 100 symbols")
    @Column(length = 100)
    private String shortDescription;

    @Size(min = 20, max = 1000, message = "Full description must be between 20 and 1000 symbols")
    @Column(length = 1000)
    private String fullDescription;

    @Size(min = 20, max = 400, message = "Recommendations must be between 20 and 400 symbols")
    @Column(length = 400)
    private String recommendations;

    @Enumerated(STRING)
    private ProductType type;

    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    private Integer volume;

    @NotNull(message = "Measurement unit is required")
    @Enumerated(STRING)
    private MeasurementUnitE unit;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ToString.Exclude
    @NotEmpty(message = "Packages must not be empty")
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<Package> packages = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)
    private List<ProductPropertyGroup> propertyGroups = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "product_filters",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "filter_id"))
    private List<Filter> filters = new ArrayList<>();


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }

    public void setFullDescription(String fullDescription) {
        if (fullDescription != null && !fullDescription.isEmpty()) {
            fullDescription = fullDescription.trim();
        }
        this.fullDescription = fullDescription;
    }

    public void setShortDescription(String shortDescription) {
        if (shortDescription != null && !shortDescription.isEmpty()) {
            shortDescription = shortDescription.trim();
        }
        this.shortDescription = shortDescription;
    }

    public void setRecommendations(String recommendations) {
        if (recommendations != null && !recommendations.isEmpty()) {
            recommendations = recommendations.trim();
        }
        this.recommendations = recommendations;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
