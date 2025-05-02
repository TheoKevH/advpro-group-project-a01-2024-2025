package id.ac.ui.cs.advprog.buildingstore.customer.repository;

import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class CustomerRepository {
    private List<Customer> customerData = new ArrayList<>();

    public Customer addCustomer(Customer customer) {
        customerData.add(customer);
        return customer;
    }

    public Iterator<Customer> getAllCustomers() {
        return customerData.iterator();
    }
}
