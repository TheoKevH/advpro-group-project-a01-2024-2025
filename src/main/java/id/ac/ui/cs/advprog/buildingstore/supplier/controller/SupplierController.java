package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String showSupplierList(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10); // 10 supplier per halaman
        Page<Supplier> supplierPage = supplierService.getAllSuppliers(pageable);

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", supplierPage.getTotalPages());

        return "supplier/supplier_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddSupplierForm(Model model) {
        model.addAttribute("supplierDTO", new SupplierDTO());
        return "supplier/add_supplier";
    }

    @PostMapping("/add")
    public String addSupplier(@Valid @ModelAttribute SupplierDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("supplierDTO", dto);
            return "supplier/add_supplier";
        }

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
        return "supplier/edit_supplier";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editSupplier(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("supplierDTO") SupplierDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "supplier/edit_supplier";
        }

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
