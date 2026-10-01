package com.hcl.Venue_Management.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hcl.Venue_Management.entity.Customer;
import com.hcl.Venue_Management.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    public Customer addCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("A customer with this email already exists");
        }
        return customerRepository.save(customer);
    }

    public List<Customer> addCustomers(List<Customer> customers) {
        customers.forEach(c -> {
            if (customerRepository.existsByEmail(c.getEmail())) {
                throw new RuntimeException("Email already exists: " + c.getEmail());
            }
        });
        return customerRepository.saveAll(customers);
    }

    public Customer updateCustomer(Long id, Customer newData) {
        Customer customer = getCustomerById(id);
        customer.setName(newData.getName());
        customer.setEmail(newData.getEmail());
        customer.setPhone(newData.getPhone());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}