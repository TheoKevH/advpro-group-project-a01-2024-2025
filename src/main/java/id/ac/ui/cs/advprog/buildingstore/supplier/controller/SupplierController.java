package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private static final String REDIRECT_SUPPLIER = "redirect:/supplier";

    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String showSupplierList(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Supplier> supplierPage = supplierService.getAllSuppliers(pageable);

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", supplierPage.getTotalPages());

        log.info("Accessing supplier list, page {}", page);
        return "supplier/supplier_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String showAddSupplierForm(Model model) {
        log.info("Opening add supplier form");
        model.addAttribute("supplierDTO", new SupplierDTO());
        return "supplier/add_supplier";
    }

    @PostMapping("/add")
    public String addSupplier(@Valid @ModelAttribute SupplierDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("supplierDTO", dto);
            log.warn("Failed to add supplier due to validation errors: {}", result.getAllErrors());
            return "supplier/add_supplier";
        }

        supplierService.addSupplier(dto);
        log.info("Added supplier: {}", dto.getName());
        return REDIRECT_SUPPLIER;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditSupplierForm(@PathVariable Long id, Model model) {
        Supplier supplier = supplierService.findById(id);
        SupplierDTO dto = SupplierDTO.fromEntity(supplier);
        model.addAttribute("supplierDTO", dto);
        model.addAttribute("id", id);
        log.info("Opening edit form for supplier id {}", id);
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
            log.warn("Failed to edit supplier id {} due to validation errors: {}", id, result.getAllErrors());
            return "supplier/edit_supplier";
        }

        supplierService.editSupplier(id, dto);
        log.info("Edited supplier id {} with new data: {}", id, dto.getName());
        return REDIRECT_SUPPLIER;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        log.info("Deleted supplier id {}", id);
        return REDIRECT_SUPPLIER;
    }
}
