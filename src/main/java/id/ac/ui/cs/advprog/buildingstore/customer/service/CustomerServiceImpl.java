package id.ac.ui.cs.advprog.buildingstore.customer.service;

import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public Customer addCustomer(Customer customer) {
        customerRepository.createCustomer(customer);
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        Iterator<Customer> customerIterator = customerRepository.getAllCustomers();
        List<Customer> allCustomer = new ArrayList<>();
        customerIterator.forEachRemaining(allCustomer::add);
        return allCustomer;
    }

    @Override
    public Customer getCustomer(String id) {
        return customerRepository.getCustomer(id);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.updateCustomer(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.removeCustomer(id);
    }
}
