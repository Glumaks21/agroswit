package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Pest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 symbols")
    @NotBlank(message = "Name must not be blank")
    @Column(length = 50, nullable = false, unique = true)
    private String name;


    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            name = name.trim();
        }
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Pest pest = (Pest) o;
        return getId() != null && Objects.equals(getId(), pest.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
