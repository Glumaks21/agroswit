package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@IdClass(ProductPropertyValue.ProductPropertyValueId.class)
public class ProductPropertyValue {

    @Id
    @Column(name = "product_id")
    private Integer productId;


    @Id
    @Column(name = "property_id")
    private Integer propertyId;


    @Column(name = "prop_value", nullable = false)
    private String value;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "property_id")
    private CategoryProperty categoryProperty;

    @Data
    public static class ProductPropertyValueId implements Serializable {

        private Integer productId;
        private Integer propertyId;

    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProductPropertyValue that = (ProductPropertyValue) o;
        return getProductId() != null && Objects.equals(getProductId(), that.getProductId())
                && getPropertyId() != null && Objects.equals(getPropertyId(), that.getPropertyId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(productId, propertyId);
    }
}
