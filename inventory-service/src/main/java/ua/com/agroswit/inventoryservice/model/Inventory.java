package ua.com.agroswit.inventoryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@EqualsAndHashCode(of = "product1CId")
@ToString
@Table
public class Inventory {
    @Id
    @Column("product_1с_id")
    private Integer product1CId;

    private Integer quantity;

}
