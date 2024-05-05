package ua.com.agroswit.userdetails.model;

import jakarta.persistence.*;
import lombok.*;
import ua.com.agroswit.userdetails.model.enums.UkrainianDistrict;

import static jakarta.persistence.EnumType.STRING;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "customer_id")
public class Manager {

    @Id
    private Integer userId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UkrainianDistrict district;

}

