package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "product/createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute ProductDTO productDTO) {
        service.create(productDTO);
        return "redirect:/product";
    }

    @GetMapping("")
    public String productListPage(Model model) {
        List<Product> products = service.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductFactory::toDTO)
                .toList();
        model.addAttribute("products", productDTOs);
        return "product/productList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute("product", ProductFactory.toDTO(product));
        return "product/editProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute ProductDTO productDTO) {
        service.edit(productDTO);
        return "redirect:/product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        service.delete(id);
        return "redirect:/product";
    }
}