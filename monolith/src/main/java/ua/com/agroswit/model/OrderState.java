package ua.com.agroswit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrderState {

    @Id
    @Column(nullable = false, length = 30)
    private String name;
}

