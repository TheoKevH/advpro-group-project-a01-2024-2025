package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;
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
                .andExpect(view().name("supplier/supplier_list"))
                .andExpect(model().attributeExists("suppliers"))
                .andExpect(model().attribute("suppliers", mockSuppliers));
    }

    @Test
    @WithMockUser
    void showAddSupplierForm_shouldReturnFormPageWithEmptyDTO() throws Exception {
        mockMvc.perform(get("/supplier/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("supplier/add_supplier"))
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
    void addSupplier_shouldReturnFormIfValidationFails() throws Exception {
        mockMvc.perform(post("/supplier/add")
                        .param("name", "")
                        .param("address", "Jakarta")
                        .param("contact", "abc")
                        .param("category", "KAYU")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("supplier/add_supplier"))
                .andExpect(model().attributeHasFieldErrors("supplierDTO", "name", "contact"));
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
                .andExpect(view().name("supplier/edit_supplier"))
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
    void editSupplier_shouldReturnFormIfValidationFails() throws Exception {
        Long supplierId = 1L;

        mockMvc.perform(post("/supplier/edit/" + supplierId)
                        .param("name", "")
                        .param("address", "")
                        .param("contact", "abc") 
                        .param("category", "KAYU")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("supplier/edit_supplier"))
                .andExpect(model().attributeHasFieldErrors("supplierDTO", "name", "address", "contact"))
                .andExpect(model().attribute("id", supplierId));
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
