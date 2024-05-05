package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "pesticide_cultures")
public class PesticideCulture {

    @EmbeddedId
    private PesticideCultureId id;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @MapsId("pesticideId")
    @JoinColumn(name = "pesticide_id")
    private Pesticide pesticide;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @MapsId("cultureId")
    @JoinColumn(name = "culture_id")
    private Culture culture;

    @Column(nullable = false)
    private Double minVolume;

    @Column(nullable = false)
    private Double maxVolume;


    public void setPesticide(Pesticide pesticide) {
        if (this.getId() == null) this.id = new PesticideCultureId();
        this.id.setPesticideId(pesticide.getId());
        this.pesticide = pesticide;
    }

    public void setCulture(Culture culture) {
        if (this.getId() == null) this.id = new PesticideCultureId();
        this.id.setCultureId(culture.getId());
        this.culture = culture;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PesticideCulture that = (PesticideCulture) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
