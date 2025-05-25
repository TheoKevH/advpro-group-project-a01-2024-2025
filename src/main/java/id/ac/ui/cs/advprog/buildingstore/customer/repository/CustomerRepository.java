package id.ac.ui.cs.advprog.buildingstore.customer.repository;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {
    // Mapping of Customer based id
    private Map<String, Customer> customerMap = new ConcurrentHashMap<>();
    // Mapping of Customer based user
    private Map<User, Customer> userMap = new ConcurrentHashMap<>();

    public Customer createCustomer(Customer customer) {
        if (customerMap.containsKey(customer.getId())) {
            return customerMap.get(customer.getId());
        }

        customerMap.put(customer.getId(), customer);
        return customer;
    }

    public Iterator<Customer> getAllCustomers() {
        return customerMap.values().iterator();
    }

    public Customer getCustomer(String id) {
        if (customerMap.containsKey(id)) {
            return customerMap.get(id);
        }

        return null;
    }

    public Customer updateCustomer(Customer customer) {
        customerMap.put(customer.getId(), customer);
        return customer;
    }

    public void removeCustomer(String customerId) {
        customerMap.remove(customerId);
    }
}
