package id.ac.ui.cs.advprog.buildingstore.customer.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @PostConstruct
    public void createTestCustomer() {
        System.out.println("Creating test customer");
        if (userRepo.findByUsername("test").isPresent()) {
            System.out.println("User test found! Deleting test customer");
            User user = userRepo.findByUsername("test").get();
            userRepo.delete(user);
        }

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setRole(Role.CASHIER);
        userRepo.save(user);

        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customer.setUser(user);

        customerRepo.createCustomer(customer);

        System.out.println("Created test user and customer.");
    }
}

