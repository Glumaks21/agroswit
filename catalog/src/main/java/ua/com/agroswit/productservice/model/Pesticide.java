package ua.com.agroswit.productservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "product_id")
public class Pesticide extends Product {

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "pesticide_pests",
            joinColumns = @JoinColumn(name = "pesticide_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "pest_id", nullable = false))
    private List<Pest> pests = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "pesticide", cascade = ALL, orphanRemoval = true)
    private List<PesticideCulture> cultures = new ArrayList<>();

}
