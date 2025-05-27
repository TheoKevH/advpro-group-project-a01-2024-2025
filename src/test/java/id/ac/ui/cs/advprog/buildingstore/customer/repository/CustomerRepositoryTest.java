package id.ac.ui.cs.advprog.buildingstore.customer.repository;

import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // Use test profile if you have one
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        // Create test customers
        customer1 = createCustomer("John Doe", "john.doe@example.com", "081234567890");
        customer2 = createCustomer("Jane Smith", "jane.smith@example.com", "082345678901");
    }

    // Helper method to create customer
    private Customer createCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        return customer;
    }

    @Test
    @DisplayName("Should save and retrieve customer successfully")
    void testSaveAndFindById() {
        // Given
        Customer savedCustomer = customerRepository.save(customer1);
        entityManager.flush();

        // When
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(customer1.getName(), foundCustomer.get().getName());
        assertEquals(customer1.getEmail(), foundCustomer.get().getEmail());
        assertEquals(customer1.getPhone(), foundCustomer.get().getPhone());
    }

    @Test
    @DisplayName("Should find all customers")
    void testFindAll() {
        // Given
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        entityManager.flush();

        // When
        List<Customer> customers = customerRepository.findAll();

        // Then
        assertEquals(2, customers.size());
        assertTrue(customers.stream().anyMatch(c -> c.getName().equals("John Doe")));
        assertTrue(customers.stream().anyMatch(c -> c.getName().equals("Jane Smith")));
    }

    @Test
    @DisplayName("Should delete customer by id")
    void testDeleteById() {
        // Given
        Customer savedCustomer = customerRepository.save(customer1);
        entityManager.flush();
        Long customerId = savedCustomer.getId();

        // When
        customerRepository.deleteById(customerId);
        entityManager.flush();

        // Then
        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        assertFalse(deletedCustomer.isPresent());
    }

    // Test custom query methods
    @Test
    @DisplayName("Should check if customer exists by email")
    void testExistsByEmail() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When & Then
        assertTrue(customerRepository.existsByEmail("john.doe@example.com"));
        assertFalse(customerRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should check if customer exists by phone")
    void testExistsByPhone() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When & Then
        assertTrue(customerRepository.existsByPhone("081234567890"));
        assertFalse(customerRepository.existsByPhone("089999999999"));
    }

    @Test
    @DisplayName("Should check if customer exists by name")
    void testExistsByName() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When & Then
        assertTrue(customerRepository.existsByName("John Doe"));
        assertFalse(customerRepository.existsByName("Non Existent"));
    }

    @Test
    @DisplayName("Should check if email exists excluding specific customer id")
    void testExistsByEmailAndIdNot() {
        // Given
        Customer savedCustomer1 = customerRepository.save(customer1);
        Customer savedCustomer2 = customerRepository.save(customer2);
        entityManager.flush();

        // When & Then
        // Email exists but belongs to the excluded customer - should return false
        assertFalse(customerRepository.existsByEmailAndIdNot("john.doe@example.com", savedCustomer1.getId()));

        // Email exists and belongs to different customer - should return true
        assertTrue(customerRepository.existsByEmailAndIdNot("john.doe@example.com", savedCustomer2.getId()));

        // Email doesn't exist at all - should return false
        assertFalse(customerRepository.existsByEmailAndIdNot("nonexistent@example.com", savedCustomer1.getId()));
    }

    @Test
    @DisplayName("Should check if phone exists excluding specific customer id")
    void testExistsByPhoneAndIdNot() {
        // Given
        Customer savedCustomer1 = customerRepository.save(customer1);
        Customer savedCustomer2 = customerRepository.save(customer2);
        entityManager.flush();

        // When & Then
        // Phone exists but belongs to the excluded customer - should return false
        assertFalse(customerRepository.existsByPhoneAndIdNot("081234567890", savedCustomer1.getId()));

        // Phone exists and belongs to different customer - should return true
        assertTrue(customerRepository.existsByPhoneAndIdNot("081234567890", savedCustomer2.getId()));

        // Phone doesn't exist at all - should return false
        assertFalse(customerRepository.existsByPhoneAndIdNot("089999999999", savedCustomer1.getId()));
    }

    @Test
    @DisplayName("Should check if name exists excluding specific customer id")
    void testExistsByNameAndIdNot() {
        // Given
        Customer savedCustomer1 = customerRepository.save(customer1);
        Customer savedCustomer2 = customerRepository.save(customer2);
        entityManager.flush();

        // When & Then
        // Name exists but belongs to the excluded customer - should return false
        assertFalse(customerRepository.existsByNameAndIdNot("John Doe", savedCustomer1.getId()));

        // Name exists and belongs to different customer - should return true
        assertTrue(customerRepository.existsByNameAndIdNot("John Doe", savedCustomer2.getId()));

        // Name doesn't exist at all - should return false
        assertFalse(customerRepository.existsByNameAndIdNot("Non Existent", savedCustomer1.getId()));
    }

    @Test
    @DisplayName("Should find customer by email")
    void testFindByEmail() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When
        Optional<Customer> foundCustomer = customerRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
        assertEquals("081234567890", foundCustomer.get().getPhone());
    }

    @Test
    @DisplayName("Should find customer by phone")
    void testFindByPhone() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When
        Optional<Customer> foundCustomer = customerRepository.findByPhone("081234567890");

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
        assertEquals("john.doe@example.com", foundCustomer.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when finding by non-existent email")
    void testFindByNonExistentEmail() {
        // When
        Optional<Customer> foundCustomer = customerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundCustomer.isPresent());
    }

    // Test unique constraints
    @Test
    @DisplayName("Should throw exception when saving customer with duplicate email")
    void testDuplicateEmailConstraint() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        Customer duplicateEmailCustomer = createCustomer("Different Name", "john.doe@example.com", "083456789012");

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.save(duplicateEmailCustomer);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("Should throw exception when saving customer with duplicate phone")
    void testDuplicatePhoneConstraint() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        Customer duplicatePhoneCustomer = createCustomer("Different Name", "different@example.com", "081234567890");

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.save(duplicatePhoneCustomer);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("Should throw exception when saving customer with duplicate name")
    void testDuplicateNameConstraint() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        Customer duplicateNameCustomer = createCustomer("John Doe", "different@example.com", "083456789012");

        // When & Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            customerRepository.save(duplicateNameCustomer);
            entityManager.flush();
        });
    }

    @Test
    @DisplayName("Should update customer successfully")
    void testUpdateCustomer() {
        // Given
        Customer savedCustomer = customerRepository.save(customer1);
        entityManager.flush();

        // When
        savedCustomer.setName("Updated Name");
        savedCustomer.setEmail("updated@example.com");
        savedCustomer.setPhone("089876543210");
        Customer updatedCustomer = customerRepository.save(savedCustomer);
        entityManager.flush();

        // Then
        assertEquals("Updated Name", updatedCustomer.getName());
        assertEquals("updated@example.com", updatedCustomer.getEmail());
        assertEquals("089876543210", updatedCustomer.getPhone());
    }

    @Test
    @DisplayName("Should count customers correctly")
    void testCount() {
        // Given
        assertEquals(0, customerRepository.count());

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        entityManager.flush();

        // When & Then
        assertEquals(2, customerRepository.count());
    }

    @Test
    @DisplayName("Should handle case-sensitive email queries")
    void testCaseSensitiveEmail() {
        // Given
        customerRepository.save(customer1);
        entityManager.flush();

        // When & Then
        assertTrue(customerRepository.existsByEmail("john.doe@example.com"));
        // This behavior depends on your database collation settings
        // Adjust the assertion based on your requirements
        assertFalse(customerRepository.existsByEmail("JOHN.DOE@EXAMPLE.COM"));
    }

    @Test
    @DisplayName("Should handle null parameters gracefully")
    void testNullParameters() {
        // When & Then
        assertFalse(customerRepository.existsByEmail(null));
        assertFalse(customerRepository.existsByPhone(null));
        assertFalse(customerRepository.existsByName(null));

        assertEquals(Optional.empty(), customerRepository.findByEmail(null));
        assertEquals(Optional.empty(), customerRepository.findByPhone(null));
    }

    @Test
    @DisplayName("Should handle empty string parameters")
    void testEmptyStringParameters() {
        // When & Then
        assertFalse(customerRepository.existsByEmail(""));
        assertFalse(customerRepository.existsByPhone(""));
        assertFalse(customerRepository.existsByName(""));
    }
}