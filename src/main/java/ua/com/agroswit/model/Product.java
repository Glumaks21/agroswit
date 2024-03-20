package ua.com.agroswit.model;

import jakarta.persistence.*;
import lombok.*;


import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", length = 300, nullable = false, unique = true)
    private String name;

    @Column(name =  "price", nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

//    @Column(name = "added_at", nullable = false)
//    private LocalDate addedAt;

//    @Column(name = "added_at")
//    private LocalDate modifiedAd;

}
