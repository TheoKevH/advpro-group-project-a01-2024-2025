package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @Test
    @WithMockUser
    void getAllSuppliers_shouldReturnListAsJson() throws Exception {
        List<Supplier> mockSuppliers = List.of(
                Supplier.builder().id(1L).name("Supplier A").build(),
                Supplier.builder().id(2L).name("Supplier B").build()
        );

        Mockito.when(supplierService.getAllSuppliers()).thenReturn(mockSuppliers);

        mockMvc.perform(get("/supplier/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Supplier A")))
                .andExpect(jsonPath("$[1].name", is("Supplier B")));
    }

    @Test
    @WithMockUser
    void addSupplier_shouldCallService() throws Exception {
        SupplierDTO dto = new SupplierDTO("PT Baru", "Bandung", "08123456789", "Elektronik");

        mockMvc.perform(post("/supplier/add")
                        .param("name", dto.getName())
                        .param("address", dto.getAddress())
                        .param("contact", dto.getContact())
                        .param("category", dto.getCategory())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());


        verify(supplierService).addSupplier(Mockito.any(SupplierDTO.class));
    }

    @Test
    @WithMockUser
    void editSupplier_shouldUpdateAndRedirect() throws Exception {
        Long supplierId = 1L;
        SupplierDTO updatedDTO = new SupplierDTO("PT Update", "Jakarta", "0812121212", "Furniture");

        mockMvc.perform(post("/supplier/edit/" + supplierId)
                        .param("name", updatedDTO.getName())
                        .param("address", updatedDTO.getAddress())
                        .param("contact", updatedDTO.getContact())
                        .param("category", updatedDTO.getCategory())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        verify(supplierService).editSupplier(eq(supplierId), Mockito.<SupplierDTO>any());
    }


}
