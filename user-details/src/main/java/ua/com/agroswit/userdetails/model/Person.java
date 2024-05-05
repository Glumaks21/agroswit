package ua.com.agroswit.userdetails.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.agroswit.userdetails.model.enums.Sex;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "customer_id")
public class Person extends Customer {

    @Column(nullable = false, unique = true)
    private String idNumber;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate birthDate;
}
