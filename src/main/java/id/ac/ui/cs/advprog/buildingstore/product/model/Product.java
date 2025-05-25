package id.ac.ui.cs.advprog.buildingstore.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String productId;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false)
    private BigDecimal productPrice;

    @Column(nullable = false)
    private int productQuantity;

    @Column(length = 500)
    private String productDescription;

    @PrePersist
    public void ensureId() {
        if (this.productId == null || this.productId.isBlank()) {
            this.productId = UUID.randomUUID().toString();
        }
    }
}
