package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "product/productList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute("product", product);
        return "product/editProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.edit(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        service.delete(service.findById(id));
        return "redirect:/product/list";
    }
}