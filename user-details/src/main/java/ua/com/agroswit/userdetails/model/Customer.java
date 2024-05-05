package ua.com.agroswit.userdetails.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ua.com.agroswit.userdetails.model.enums.CustomerType;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Inheritance(strategy = JOINED)
@Entity
public class Customer {

    @Id
    private Integer userId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private CustomerType type;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UkrainianDistrict district;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String settlement;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Customer customer = (Customer) o;
        return getUserId() != null && Objects.equals(getUserId(), customer.getUserId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
