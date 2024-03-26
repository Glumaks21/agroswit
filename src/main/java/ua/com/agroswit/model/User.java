package ua.com.agroswit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.agroswit.model.enums.RoleE;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String login;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String surname;

    @Enumerated(EnumType.STRING)
    private RoleE role;

}
