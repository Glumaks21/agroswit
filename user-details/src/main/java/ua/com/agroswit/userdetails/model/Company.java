package ua.com.agroswit.userdetails.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "customer_id")
public class Company extends Customer {
    @Column(name = "companyName", unique = true, nullable = false)
    private String companyName;

    @Column(unique = true, nullable = false)
    private String egrpou;

    @Column(nullable = false)
    private LocalDate incorporationDate;
}
