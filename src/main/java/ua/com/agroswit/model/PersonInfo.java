package ua.com.agroswit.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ua.com.agroswit.model.enums.SexE;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class PersonInfo {

    @Id
    @Column(nullable = false, unique = true)
    private Integer customerId;

    @Column(nullable = false)
    private SexE sex;

    @Column(nullable = false)
    private LocalDate birthDate;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Customer customer;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PersonInfo that = (PersonInfo) o;
        return getCustomerId() != null && Objects.equals(getCustomerId(), that.getCustomerId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
