package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService service;

    // Create product (POST /api/products)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return service.create(product);
    }

    // Get all products (GET /api/products)
    @GetMapping
    public List<Product> getAllProducts() {
        return service.findAll();
    }

    // Get product by ID (GET /api/products/{id})
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return service.findById(id);
    }

    // Update product by ID (PUT /api/products/{id})
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        product.setProductId(id); // set ID agar sesuai
        return service.edit(product);
    }

    // Delete product by ID (DELETE /api/products/{id})
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        Product product = service.findById(id);
        service.delete(product);
    }
}
