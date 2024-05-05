package ua.com.agroswit.userdetails.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "customer_id")
public class Entrepreneur extends Customer {
    @Column(name = "companyName", unique = true, nullable = false)
    private String companyName;

    @Column(unique = true, nullable = false)
    private String idNumber;
}
