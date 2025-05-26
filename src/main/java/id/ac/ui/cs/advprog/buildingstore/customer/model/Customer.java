package id.ac.ui.cs.advprog.buildingstore.customer.model;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter @Setter
public class Customer {
    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private String id = UUID.randomUUID().toString();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    public Customer() {}

    public Customer(User user) {
        this.user = user;
    }
}
