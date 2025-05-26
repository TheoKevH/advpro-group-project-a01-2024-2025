package id.ac.ui.cs.advprog.buildingstore.customer.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Valid test data
    static Stream<Arguments> validCustomerData() {
        return Stream.of(
                Arguments.of("John Doe", "john.doe@example.com", "081234567890"),
                Arguments.of("Jane Smith", "jane.smith@gmail.com", "082345678901"),
                Arguments.of("Ali Rahman", "ali.rahman@company.co.id", "083456789012"),
                Arguments.of("Maria Garcia", "maria.garcia@university.edu", "084567890123"),
                Arguments.of("Aa", "a@b.co", "0812345678"), // Minimum valid lengths
                Arguments.of("This Is A Very Looooong Name But Still Valid Fr Fr",
                        "very.long.email.address.but.still.valid@example-domain.com",
                        "081234567890"), // Maximum valid lengths
                Arguments.of("Anne Marie", "anne-marie@test.org", "089876543210"),
                Arguments.of("O Connor", "o.connor@irish.ie", "087654321098")
        );
    }

    // Invalid name test data
    static Stream<Arguments> invalidNameData() {
        return Stream.of(
                Arguments.of("", "Name is required"), // Empty name
                Arguments.of("   ", "Name is required"), // Blank name
                Arguments.of("A", "Name must be between 2 and 50 characters"), // Too short
                Arguments.of("This Name Is Way Too Long And Exceeds The Maximum Character Limit For Names",
                        "Name must be between 2 and 50 characters"), // Too long
                Arguments.of("John123", "Name must contain only letters and spaces"), // Numbers
                Arguments.of("John@Doe", "Name must contain only letters and spaces"), // Special chars
                Arguments.of("John_Doe", "Name must contain only letters and spaces"), // Underscore
                Arguments.of("John-Doe", "Name must contain only letters and spaces") // Hyphen
        );
    }

    // Invalid email test data
    static Stream<Arguments> invalidEmailData() {
        return Stream.of(
                Arguments.of("", "Email is required"), // Empty email - @NotBlank
                Arguments.of("   ", "Email is required"), // Blank email - @NotBlank
                Arguments.of("invalid-email", "must be a well-formed email address"), // No @ - @Email default message
                Arguments.of("@invalid.com", "must be a well-formed email address"), // No local part - @Email
                Arguments.of("invalid@", "must be a well-formed email address"), // No domain - @Email
                Arguments.of("invalid@.com", "must be a well-formed email address"), // Invalid domain - @Email
                Arguments.of("invalid..email@test.com", "must be a well-formed email address"), // Double dots - @Email
                Arguments.of("a".repeat(89) + "@example.com", "Email must not exceed 100 characters") // 89 + 12 (@example.com) = 101 chars, exceeds 100 limit
        );
    }

    // Invalid phone test data
    static Stream<Arguments> invalidPhoneData() {
        return Stream.of(
                Arguments.of("", "Phone is required"), // Empty phone
                Arguments.of("   ", "Phone is required"), // Blank phone
                Arguments.of("12345678901", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Doesn't start with 08
                Arguments.of("0812345", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Too short
                Arguments.of("081234567890123456789", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Too long
                Arguments.of("08abcdefghij", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Contains letters
                Arguments.of("08-1234-5678", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Contains hyphens
                Arguments.of("08 1234 5678", "Phone number format is 08XXXXXXXXXX (10-16 digits long)"), // Contains spaces
                Arguments.of("+081234567890", "Phone number format is 08XXXXXXXXXX (10-16 digits long)") // Contains plus
        );
    }

    @ParameterizedTest
    @MethodSource("validCustomerData")
    void testValidCustomer(String name, String email, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidNameData")
    void testInvalidName(String name, String expectedMessage) {
        Customer customer = createValidCustomer();
        customer.setName(name);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @ParameterizedTest
    @MethodSource("invalidEmailData")
    void testInvalidEmail(String email, String expectedMessage) {
        Customer customer = createValidCustomer();
        customer.setEmail(email);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @ParameterizedTest
    @MethodSource("invalidPhoneData")
    void testInvalidPhone(String phone, String expectedMessage) {
        Customer customer = createValidCustomer();
        customer.setPhone(phone);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals(expectedMessage)));
    }

    @Test
    void testCustomerSettersAndGetters() {
        Customer customer = new Customer();

        customer.setId(1L);
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPhone("081234567890");

        assertEquals(1L, customer.getId());
        assertEquals("Test User", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("081234567890", customer.getPhone());
    }

    @Test
    void testMultipleViolations() {
        Customer customer = new Customer();
        customer.setName(""); // Invalid name
        customer.setEmail("invalid-email"); // Invalid email
        customer.setPhone("123"); // Invalid phone

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertEquals(5, violations.size());
    }

    // Test data for boundary conditions
    @ParameterizedTest
    @ValueSource(strings = {
            "0812345678", // Minimum length (10 digits)
            "0812345678901234" // Maximum length (16 digits)
    })
    void testPhoneBoundaryConditions(String phone) {
        Customer customer = createValidCustomer();
        customer.setPhone(phone);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "081234567", // Just below minimum (9 digits)
            "08123456789012345" // Just above maximum (18 digits)
    })
    void testPhoneInvalidBoundaryConditions(String phone) {
        Customer customer = createValidCustomer();
        customer.setPhone(phone);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertFalse(violations.isEmpty());
    }

    // Helper method to create a valid customer
    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("081234567890");
        return customer;
    }

    // Edge case tests
    @Test
    void testNameWithMultipleSpaces() {
        Customer customer = createValidCustomer();
        customer.setName("John    Doe"); // Multiple spaces

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertTrue(violations.isEmpty()); // Should be valid as pattern allows spaces
    }

    @Test
    void testEmailSpecialCases() {
        String[] validEmails = {
                "test+tag@example.com",
                "user.name@example.com",
                "user_name@example.com",
                "123@example.com",
                "test@example-domain.com"
        };

        for (String email : validEmails) {
            Customer customer = createValidCustomer();
            customer.setEmail(email);

            Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
            assertTrue(violations.isEmpty(),
                    "Email " + email + " should be valid but got violations: " + violations);
        }
    }

    @Test
    void testNullValues() {
        Customer customer = new Customer();
        // All fields are null by default

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Should have violations for name, email, and phone (all required)
        assertEquals(3, violations.size());
    }
}