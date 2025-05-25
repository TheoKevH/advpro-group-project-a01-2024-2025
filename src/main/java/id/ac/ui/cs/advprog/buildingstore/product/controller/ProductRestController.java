package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    @Autowired
    private ProductService service;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Create product (POST /api/products)
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        Product createdProduct = service.create(dto);
        return ProductFactory.toDTO(createdProduct);
    }

    // Get all products (GET /api/products)
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return service.findAll().stream()
                .map(ProductFactory::toDTO)
                .toList();
    }

    // Get product by ID (GET /api/products/{id})
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable String id) {
        Product product = service.findById(id);
        return ProductFactory.toDTO(product);
    }

    // Update product by ID (PUT /api/products/{id})
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable String id, @RequestBody ProductDTO dto) {
        dto.setProductId(id); // set ID agar konsisten
        Product updated = service.edit(dto);
        return ProductFactory.toDTO(updated);
    }

    // Delete product by ID (DELETE /api/products/{id})
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/insert")
    public void insertProduct(@RequestBody ProductRequestDTO request) {
        service.insert(request);
    }
}
