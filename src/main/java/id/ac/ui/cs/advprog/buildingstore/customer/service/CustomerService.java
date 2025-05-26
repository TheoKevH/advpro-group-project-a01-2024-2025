package id.ac.ui.cs.advprog.buildingstore.customer.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public Customer addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
    public Customer getCustomer(Long id);
    public Customer updateCustomer(Customer customer);
    public void deleteCustomer(Long id);

    // Checker
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByName(String name);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    boolean existsByNameAndIdNot(String name, Long id);
}
