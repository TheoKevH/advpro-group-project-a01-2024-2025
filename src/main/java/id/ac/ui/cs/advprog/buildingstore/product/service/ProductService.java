package id.ac.ui.cs.advprog.buildingstore.product.service;

import  id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import  java.util.List;

public interface ProductService {
    public Product create(Product product);
    public List<Product> findAll();
    public Product findById(String id);
    public Product edit(Product product);
    public void delete(Product product);
}
