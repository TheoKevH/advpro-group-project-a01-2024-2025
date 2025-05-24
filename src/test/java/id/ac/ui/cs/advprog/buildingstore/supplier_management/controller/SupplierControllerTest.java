package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.SupplierCategory;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @Test
    @WithMockUser
    void showSupplierList_shouldAddSuppliersToModelAndReturnView() throws Exception {
        List<Supplier> mockSuppliers = List.of(
                Supplier.builder().id(1L).name("Supplier A").build(),
                Supplier.builder().id(2L).name("Supplier B").build()
        );

        Mockito.when(supplierService.getAllSuppliers()).thenReturn(mockSuppliers);

        mockMvc.perform(get("/supplier"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/supplier_list"))
                .andExpect(model().attributeExists("suppliers"))
                .andExpect(model().attribute("suppliers", mockSuppliers));
    }

    @Test
    @WithMockUser
    void showAddSupplierForm_shouldReturnFormPageWithEmptyDTO() throws Exception {
        mockMvc.perform(get("/supplier/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add_supplier"))
                .andExpect(model().attributeExists("supplierDTO"));
    }

    @Test
    @WithMockUser
    void addSupplier_shouldCallService() throws Exception {
        SupplierDTO dto = new SupplierDTO("PT Baru", "Bandung", "08123456789", SupplierCategory.KAYU);

        mockMvc.perform(post("/supplier/add")
                        .param("name", dto.getName())
                        .param("address", dto.getAddress())
                        .param("contact", dto.getContact())
                        .param("category", dto.getCategory().name())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());


        verify(supplierService).addSupplier(Mockito.any(SupplierDTO.class));
    }

    @Test
    @WithMockUser
    void showEditSupplierForm_shouldReturnFormWithSupplierData() throws Exception {
        Long supplierId = 1L;
        Supplier supplier = Supplier.builder()
                .id(supplierId)
                .name("PT XYZ")
                .address("Bandung")
                .contact("0812345678")
                .category(SupplierCategory.PLUMBING)
                .build();

        Mockito.when(supplierService.findById(supplierId)).thenReturn(supplier);

        mockMvc.perform(get("/supplier/edit/" + supplierId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit_supplier"))
                .andExpect(model().attributeExists("supplierDTO"))
                .andExpect(model().attribute("id", supplierId));
    }
    
    @Test
    @WithMockUser
    void editSupplier_shouldUpdateAndRedirect() throws Exception {
        Long supplierId = 1L;
        SupplierDTO updatedDTO = new SupplierDTO("PT Update", "Jakarta", "0812121212", SupplierCategory.KAYU);

        mockMvc.perform(post("/supplier/edit/" + supplierId)
                        .param("name", updatedDTO.getName())
                        .param("address", updatedDTO.getAddress())
                        .param("contact", updatedDTO.getContact())
                        .param("category", updatedDTO.getCategory().name())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(supplierService).editSupplier(eq(supplierId), Mockito.<SupplierDTO>any());
    }

    @Test
    @WithMockUser
    void deleteSupplier_shouldDeleteAndRedirect() throws Exception {
        Long supplierId = 1L;

        mockMvc.perform(post("/supplier/delete/" + supplierId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(supplierService).deleteSupplier(supplierId);
    }
}
