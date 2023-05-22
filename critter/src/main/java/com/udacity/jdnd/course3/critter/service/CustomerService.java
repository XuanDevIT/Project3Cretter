package com.udacity.jdnd.course3.critter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PetRepository petRepository;

	public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
		String name = customerDTO.getName();
		String notes = customerDTO.getNotes();
		String phone = customerDTO.getPhoneNumber();
		Customer customer = new Customer(name, phone, notes);
		Customer customerSaved = customerRepository.save(customer);
		customerDTO.setId(customerSaved.getId());
		return customerDTO;
	}

	public List<CustomerDTO> getAllCustomers() {
		List<Customer> customerList = customerRepository.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		customerList.forEach(cus -> {
			CustomerDTO customerDTO = mapCustomerToCustomerDTO(cus);
			customerDTOs.add(customerDTO);
		});
		return customerDTOs;
	}

	private List<Long> getPetIdsByOwnerId(Long ownerId) {
		List<Long> petIdList = new ArrayList<Long>();
		petRepository.findAll().forEach(pet -> {
			if (pet.getCustomer().getId().equals(ownerId))
				petIdList.add(pet.getId());
		});
		return petIdList;
	}
	
	public CustomerDTO getOwnerByPet(long petId) {
		CustomerDTO customerDTO = null;
		List<Pet> petList = petRepository.findAll();

		for (Pet pet : petList) {
			if (pet.getId() == petId) {
				customerDTO = mapCustomerToCustomerDTO(pet.getCustomer());
				break;
			}
		}
		
		return customerDTO;
	}

	private CustomerDTO mapCustomerToCustomerDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		
		customerDTO.setId(customer.getId());
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhone());
		if (customer.getNotes() != null)
			customerDTO.setNotes(customer.getNotes());
		customerDTO.setPetIds(getPetIdsByOwnerId(customer.getId()));
		
		return customerDTO;
	}
}
