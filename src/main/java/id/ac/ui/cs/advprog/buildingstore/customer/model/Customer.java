package id.ac.ui.cs.advprog.buildingstore.customer.model;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter @Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;
}
