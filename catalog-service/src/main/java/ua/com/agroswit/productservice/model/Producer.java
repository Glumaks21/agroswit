package ua.com.agroswit.productservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.proxy.HibernateProxy;
import lombok.*;
import ua.com.agroswit.productservice.dto.validation.Groups;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Producer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100")
    @NotBlank(message = "Name must not be blank")
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Size(max = 45, message = "Logo must be lower than 45")
    @Column(length = 45, nullable = false, unique = true)
    private String logo;


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
        Producer producer = (Producer) o;
        return getId() != null && Objects.equals(getId(), producer.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
