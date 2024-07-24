package com.accelone.customer;

import com.accelone.customer.entity.Customer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.accelone.customer.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CustomerApplicationTests extends SpringBaseIT {

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectMapper mapper;

	@BeforeEach
	void prepare() {
		initialize();
	}

	@Test
	void test_get_all_customer_success() throws Exception {
		List<Customer> listCustomers = (List<Customer>) createObjectListFromFile("customer/", "customers.json", Customer.class);
		when(customerRepository.findAll()).thenReturn(listCustomers);
		mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.notNullValue()))
				.andExpect(jsonPath("$.size()", Matchers.is(3)));
	}

	@Test
	void test_get_all_customer_not_found() throws Exception {
		when(customerRepository.findAll()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/customers"))
				.andExpect(status().isNotFound());
	}

	@Test
	void test_get_customer_by_name_success() throws Exception {
		Customer customer = (Customer) createObjectFromFile("customer/", "customer.json", Customer.class);
		when(customerRepository.findCustomerByName(any())).thenReturn(Optional.of(customer));
		mockMvc.perform(get("/customers/Jesus"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.notNullValue()))
				.andExpect(jsonPath("$.name", Matchers.is("Jesus")));
	}

	@Test
	void test_get_customer_by_name_not_found() throws Exception {
		when(customerRepository.findCustomerByName(any())).thenReturn(Optional.empty());
		mockMvc.perform(get("/customers/Jesus"))
				.andExpect(status().isNotFound());
	}


	@Test
	void test_save_customer_success() throws Exception {
		Customer customer = (Customer) createObjectFromFile("customer/", "customer.json", Customer.class);
		when(customerRepository.save(any())).thenReturn(customer);
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(customer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", Matchers.notNullValue()))
				.andExpect(jsonPath("$.lastName", Matchers.is("Arias")));
	}


	@Test
	void test_update_customer_success() throws Exception {
		Customer customer = (Customer) createObjectFromFile("customer/", "customer.json", Customer.class);
		Customer customerUpdated = (Customer) createObjectFromFile("customer/", "customerUpdate.json", Customer.class);
		when(customerRepository.save(any())).thenReturn(customer);
		when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
		mockMvc.perform(put("/customers/3")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(customerUpdated)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", Matchers.notNullValue()))
				.andExpect(jsonPath("$.lastName", Matchers.is("Arias Alfaro")))
				.andExpect(jsonPath("$.name", Matchers.is("Yeudy Gerardo")))
				.andExpect(jsonPath("$.age", Matchers.is(37)));
	}

	@Test
	void test_update_customer_not_found() throws Exception {
		Customer customer = (Customer) createObjectFromFile("customer/", "customer.json", Customer.class);
		Customer customerUpdated = (Customer) createObjectFromFile("customer/", "customerUpdate.json", Customer.class);
		when(customerRepository.save(any())).thenReturn(customer);
		when(customerRepository.findById(any())).thenReturn(Optional.empty());
		mockMvc.perform(put("/customers/3")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(customerUpdated)))
				.andExpect(status().isNotFound());
	}






}
