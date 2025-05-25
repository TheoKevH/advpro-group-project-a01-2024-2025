package id.ac.ui.cs.advprog.buildingstore.product.service;

import  id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import  id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import  id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import  java.util.List;

public interface ProductService {
    public Product create(ProductDTO dto);
    public List<Product> findAll();
    public Product findById(String id);
    public Product edit(ProductDTO dto);
    public void delete(String id);
    public Product insert(ProductRequestDTO request);
}
