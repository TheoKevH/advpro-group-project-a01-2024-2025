package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/list")
    @ResponseBody
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PostMapping("/add")
    public String addSupplier(@ModelAttribute SupplierDTO dto) {
        supplierService.addSupplier(dto);
        return "redirect:/supplier/list";
    }

    @PostMapping("/edit/{id}")
    public String editSupplier(@PathVariable("id") Long id, @ModelAttribute SupplierDTO dto) {
        supplierService.editSupplier(id, dto);
        return "redirect:/supplier/list";
    }
}
