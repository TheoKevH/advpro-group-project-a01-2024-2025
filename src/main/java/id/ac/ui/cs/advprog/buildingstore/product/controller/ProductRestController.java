package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    @Autowired
    private ProductService service;

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
                .filter(product -> product.getProductPrice() != null &&
                        product.getProductPrice().compareTo(BigDecimal.ZERO) > 0 &&
                        product.getProductQuantity() > 0)
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
    public ProductDTO updateProductQuantity(@PathVariable String id, @RequestBody Integer quantity) {
        Product existingProduct = service.findById(id);
        existingProduct.setProductQuantity(existingProduct.getProductQuantity() - quantity);
        Product updatedProduct = service.edit(ProductFactory.toDTO(existingProduct));
        return ProductFactory.toDTO(updatedProduct);
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
