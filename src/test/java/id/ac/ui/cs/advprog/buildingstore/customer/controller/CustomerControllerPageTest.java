package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerControllerPage.class)
@WithMockUser
public class CustomerControllerPageTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AuthService authService;

    private Customer sampleCustomer;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("John Doe");
        sampleCustomer.setEmail("john@example.com");
        sampleCustomer.setPhone("081234567890");

        sampleUser = new User();
        sampleUser.setUsername("john");
    }

    @Test
    void testCustomersHomepage() throws Exception {
        Mockito.when(authService.getUserByUsername(anyString())).thenReturn(sampleUser);
        Mockito.when(customerService.getAllCustomers()).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/homepage"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("customers"));
    }

    @Test
    void testCreateCustomerPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void testViewCustomerPage() throws Exception {
        Mockito.when(customerService.getCustomer(1L)).thenReturn(sampleCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/view/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/view_customer"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void testEditCustomerPage() throws Exception {
        Mockito.when(customerService.getCustomer(1L)).thenReturn(sampleCustomer);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/edit_customer"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    void testCreateCustomerPost_Valid() throws Exception {
        Mockito.when(customerService.existsByName(any())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(any())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
    }

    @Test
    void testEditCustomerPost_Valid() throws Exception {
        Mockito.when(customerService.existsByNameAndIdNot(any(), any())).thenReturn(false);
        Mockito.when(customerService.existsByEmailAndIdNot(any(), any())).thenReturn(false);
        Mockito.when(customerService.existsByPhoneAndIdNot(any(), any())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
    }

    @Test
    void testDeleteCustomerPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customers"));
    }

    @Test
    void testCreateCustomerPost_DuplicateNameEmailPhone_ShouldReturnFormWithErrors() throws Exception {
        Mockito.when(customerService.existsByName(any())).thenReturn(true);
        Mockito.when(customerService.existsByEmail(any())).thenReturn(true);
        Mockito.when(customerService.existsByPhone(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeHasFieldErrors("customer", "name", "email", "phone"));
    }

    @Test
    void testCreateCustomerPost_DataIntegrityViolationException_NameError() throws Exception {
        Mockito.when(customerService.existsByName(any())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(any())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(any())).thenReturn(false);
        Mockito.doThrow(new DataIntegrityViolationException("constraint violation on name"))
                .when(customerService).addCustomer(any(Customer.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeHasFieldErrors("customer", "name"));
    }

    @Test
    void testCreateCustomerPost_DataIntegrityViolationException_OtherError() throws Exception {
        Mockito.when(customerService.existsByName(any())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(any())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(any())).thenReturn(false);
        Mockito.doThrow(new DataIntegrityViolationException("other constraint"))
                .when(customerService).addCustomer(any(Customer.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void testCreateCustomerPost_GenericException() throws Exception {
        Mockito.when(customerService.existsByName(any())).thenReturn(false);
        Mockito.when(customerService.existsByEmail(any())).thenReturn(false);
        Mockito.when(customerService.existsByPhone(any())).thenReturn(false);
        Mockito.doThrow(new RuntimeException("random error"))
                .when(customerService).addCustomer(any(Customer.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", sampleCustomer.getName())
                        .param("email", sampleCustomer.getEmail())
                        .param("phone", sampleCustomer.getPhone())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void testCreateCustomerPost_FormValidationError() throws Exception {
        // Kirim data kosong agar validasi javax @Valid gagal
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")  // kosong
                        .param("email", "notanemail")  // invalid format
                        .param("phone", "")  // kosong
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/create_customer"))
                .andExpect(model().attributeHasFieldErrors("customer", "name", "email", "phone"));
    }

}
