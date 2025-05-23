package id.ac.ui.cs.advprog.buildingstore.supplier_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String contact;
    @Enumerated(EnumType.STRING)
    private SupplierCategory category;
}