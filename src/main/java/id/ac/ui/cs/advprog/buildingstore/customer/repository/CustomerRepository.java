package id.ac.ui.cs.advprog.buildingstore.customer.repository;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {
    // Mapping of Customer based id
    private Map<String, Customer> customerMap = new ConcurrentHashMap<>();
    // Mapping of Customer based user
    private Map<Long, Customer> userMap = new ConcurrentHashMap<>();

    public Customer createCustomer(Customer customer) {
        if (customerMap.containsKey(customer.getId())) {
            return customerMap.get(customer.getId());
        }

        customerMap.put(customer.getId(), customer);
        userMap.put(customer.getUser().getId(), customer);
        System.out.println("Customer created: " + customer.getId());
        return customer;
    }

    public Iterator<Customer> getAllCustomers() {
        return customerMap.values().iterator();
    }

    public Customer getCustomer(String id) {
        if (customerMap.containsKey(id)) {
            System.out.println("Customer found: " + id);
            return customerMap.get(id);
        }
        System.out.println("Customer not found: " + id);
        return null;
    }

    public Customer getCustomerByUser(User user) {
        if (userMap.containsKey(user.getId())) {
            System.out.println("Customer found by user: " + user.getId());
            return userMap.get(user.getId());
        }
        System.out.println("Customer not found by user: " + user.getId());
        return null;
    }

    public Customer updateCustomer(Customer customer) {
        customerMap.put(customer.getId(), customer);
        userMap.put(customer.getUser().getId(), customer);
        return customer;
    }

    public void removeCustomer(String customerId) {
        Customer customer = customerMap.get(customerId);
        customerMap.remove(customerId);
        userMap.remove(customer.getUser().getId());
    }
}
