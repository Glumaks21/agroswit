package ua.com.agroswit.productservice.model;



import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class PesticideCultureId implements Serializable {

    @Column(name = "pesticide_id", nullable = false)
    private Integer pesticideId;

    @Column(name = "culture_id", nullable = false)
    private Integer cultureId;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PesticideCultureId that = (PesticideCultureId) o;
        return pesticideId != null && Objects.equals(pesticideId, that.pesticideId)
                && cultureId != null && Objects.equals(cultureId, that.cultureId);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(pesticideId, cultureId);
    }
}
