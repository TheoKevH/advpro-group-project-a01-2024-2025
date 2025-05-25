package id.ac.ui.cs.advprog.buildingstore.product.repository;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByProductNameIgnoreCase(String productName);
}


