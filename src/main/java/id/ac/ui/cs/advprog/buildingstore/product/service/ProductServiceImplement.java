package id.ac.ui.cs.advprog.buildingstore.product.service;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImplement implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public Product create(ProductDTO dto) {
        Product product = ProductFactory.fromDTO(dto);

        // Cek apakah nama produk sudah ada
        if (productRepository.findByProductNameIgnoreCase(product.getProductName()).isPresent()) {
            throw new IllegalArgumentException("Produk dengan nama ini sudah ada");
        }

        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll(); // Tidak perlu pakai Iterator
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produk tidak ditemukan"));
    }

    @Override
    public Product edit(ProductDTO dto) {
        Product existingProduct = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produk tidak ditemukan"));

        existingProduct.setProductName(dto.getProductName());
        existingProduct.setProductDescription(dto.getProductDescription());
        existingProduct.setProductQuantity(dto.getProductQuantity());
        existingProduct.setProductPrice(dto.getProductPrice());

        return productRepository.save(existingProduct);
    }

    @Override
    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Produk tidak ditemukan");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product insert(ProductRequestDTO request) {
        String name = request.getProductName();
        int quantity = request.getProductQuantity();

        return productRepository.findByProductNameIgnoreCase(name)
                .map(existingProduct -> {
                    existingProduct.setProductQuantity(existingProduct.getProductQuantity() + quantity);
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setProductName(name);
                    newProduct.setProductQuantity(quantity);
                    newProduct.setProductPrice(BigDecimal.ZERO);
                    return productRepository.save(newProduct);
                });
    }
}