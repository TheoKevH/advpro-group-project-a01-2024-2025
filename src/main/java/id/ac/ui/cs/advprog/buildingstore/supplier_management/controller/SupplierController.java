package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String showSupplierList(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "admin/supplier_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddSupplierForm(Model model) {
        model.addAttribute("supplierDTO", new SupplierDTO());
        return "admin/add_supplier";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addSupplier(@ModelAttribute SupplierDTO dto) {
        supplierService.addSupplier(dto);
        return "redirect:/supplier";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditSupplierForm(@PathVariable Long id, Model model) {
        Supplier supplier = supplierService.findById(id);
        SupplierDTO dto = SupplierDTO.fromEntity(supplier);
        model.addAttribute("supplierDTO", dto);
        model.addAttribute("id", id);
        return "admin/edit_supplier";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editSupplier(@PathVariable("id") Long id, @ModelAttribute SupplierDTO dto) {
        supplierService.editSupplier(id, dto);
        return "redirect:/supplier";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return "redirect:/supplier";
    }
}
