package com.accelone.customer.controller;

import com.accelone.customer.entity.Customer;
import com.accelone.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping()
    @Operation(
            description = "Get list with all Customers",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Not exist customers in the DB"),
                    @ApiResponse(responseCode = "200", description = "Get the list of all customers", content =
                            {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = Customer.class))}),
            }
    )
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        if (!customerList.isEmpty()) {
            return ResponseEntity.ok(customerList);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{name}")
    @Operation(
            description = "Get customer by name",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Not exist customer by name in the DB"),
                    @ApiResponse(responseCode = "200", description = "Get customer by NAME", content =
                            {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = Customer.class))}),
            }
    )
    public ResponseEntity<?> getCustomerByName(@PathVariable String name) {
        Optional<Customer> customer = customerService.getCustomerByName(name);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(
                        "message", ": Not exist the customer with the name: " + name));
    }
    @Operation(
            description = "Save a NEW Customer in the DB",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer saved", content =
                            {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = Customer.class))}),
            }
    )
    @PostMapping
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCustomer(customer));
    }

    @Operation(
            description = "Update a Customer in the DB",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Not exist customers in the DB"),
                    @ApiResponse(responseCode = "201", description = "Customer updated", content =
                            {@Content(mediaType = "application/json", schema =
                            @Schema(implementation = Customer.class))}),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @PathVariable Long id) {
        Optional<Customer> customerById = customerService.getById(id);
        if (customerById.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.updateCustomer(customer, customerById.get(), id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(
                        "message", ": Not exist the customer with the id: " + id));
    }

}
