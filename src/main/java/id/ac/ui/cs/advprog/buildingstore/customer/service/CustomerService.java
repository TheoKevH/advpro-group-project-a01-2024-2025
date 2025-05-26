package id.ac.ui.cs.advprog.buildingstore.customer.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;

import java.util.Iterator;
import java.util.List;

public interface CustomerService {
    public Customer addCustomer(Customer customer);
    public List<Customer> getAllCustomers();
    public Customer getCustomer(Long id);
    public Customer getCustomerByUser(User user);
    public Customer updateCustomer(Customer customer);
    public void deleteCustomer(Long id);
}
