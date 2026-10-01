package com.hcl.Venue_Management.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.hcl.Venue_Management.entity.Customer;
import com.hcl.Venue_Management.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getOne(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted";
    }
}