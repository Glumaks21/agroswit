package ua.com.agroswit.inventoryservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@EqualsAndHashCode(of = "article1CId")
@ToString
@Table
public class Inventory implements Persistable<Integer> {
    @Id
    @Column("article_1c_id")
    private Integer article1CId;

    private Integer productId;

    private Integer quantity;

    @Transient
    private Boolean isNew = false;


    @Override
    public Integer getId() {
        return getArticle1CId();
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
