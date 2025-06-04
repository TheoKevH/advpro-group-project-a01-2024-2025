package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CustomerControllerREST.class)
public class CustomerControllerRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("John Doe");
        sampleCustomer.setEmail("john@example.com");
        sampleCustomer.setPhone("081234567890");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllCustomers_OK() throws Exception {
        List<Customer> customers = List.of(sampleCustomer);
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleCustomer.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(sampleCustomer.getName()))
                .andExpect(jsonPath("$[0].email").value(sampleCustomer.getEmail()))
                .andExpect(jsonPath("$[0].phone").value(sampleCustomer.getPhone()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllCustomers_NoContent() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllCustomers_Exception() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCustomerById_Found() throws Exception {
        Mockito.when(customerService.getCustomer(1L)).thenReturn(sampleCustomer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()))
                .andExpect(jsonPath("$.name").value(sampleCustomer.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCustomerById_NotFound() throws Exception {
        Mockito.when(customerService.getCustomer(1L)).thenReturn(null);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCustomer_Success() throws Exception {
        Mockito.when(customerService.existsByName(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(anyString())).thenReturn(false);
        Mockito.when(customerService.addCustomer(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCustomer_ConflictByName() throws Exception {
        Mockito.when(customerService.existsByName(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCustomer_ConflictByEmail() throws Exception {
        Mockito.when(customerService.existsByName(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCustomer_ConflictByPhone() throws Exception {
        Mockito.when(customerService.existsByName(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCustomer_BadRequest() throws Exception {
        Mockito.when(customerService.existsByName(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(anyString())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(anyString())).thenReturn(false);
        Mockito.when(customerService.addCustomer(any(Customer.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCustomer_Success() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByPhoneAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.updateCustomer(any(Customer.class))).thenReturn(sampleCustomer);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleCustomer.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCustomer_ConflictByName() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCustomer_ConflictByEmail() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCustomer_ConflictByPhone() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByPhoneAndIdNot(anyString(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateCustomer_BadRequest() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.existsByPhoneAndIdNot(anyString(), anyLong())).thenReturn(false);
        Mockito.when(customerService.updateCustomer(any(Customer.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/customers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCustomer_Success() throws Exception {
        Mockito.doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteCustomer_InternalServerError() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1").with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}
