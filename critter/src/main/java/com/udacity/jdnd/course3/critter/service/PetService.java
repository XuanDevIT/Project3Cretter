package com.udacity.jdnd.course3.critter.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.pet.PetType;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;

@Service
public class PetService {
	@Autowired
	private PetRepository petRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public PetDTO savePet(PetDTO petDTO) {
		PetType type = petDTO.getType();
		String name = petDTO.getName();
		LocalDate birthDate = petDTO.getBirthDate();
		String notes = petDTO.getNotes();
		Customer customer = customerRepository.findById(petDTO.getOwnerId()).get();

		Pet pet = new Pet(type, name, birthDate, notes, customer);
		Pet petSave = petRepository.save(pet);

		customer = petSave.getCustomer();
		customer.addPet(petSave);
		customerRepository.save(customer);

		PetDTO outputPetDTO = new PetDTO();
		BeanUtils.copyProperties(petSave, outputPetDTO);

		return outputPetDTO;
	}

	public List<PetDTO> getPetsByOwner(long ownerId) {
		List<Pet> petList = petRepository.findAll();
		List<PetDTO> petDTOList = new ArrayList<>();

		petList.forEach(pet -> {
			if (pet.getCustomer().getId() == ownerId) {
				petDTOList.add(mapTo(pet));
			}
		});

		return petDTOList;
	}

	private PetDTO mapTo(Pet pet) {

		PetDTO petDTO = new PetDTO();
		petDTO.setId(pet.getId());
		petDTO.setBirthDate(pet.getBirthDate());
		petDTO.setName(pet.getName());
		petDTO.setNotes(pet.getNotes());
		petDTO.setOwnerId(pet.getCustomer().getId());
		petDTO.setType(pet.getType());

		return petDTO;
	}

	public PetDTO getPet(long petId) {
		Optional<Pet> optionalPet = petRepository.findById(petId);
		if (optionalPet.isPresent()) {
			return mapTo(optionalPet.get());
		}
		return null;
	}

	public List<PetDTO> getPets() {
		List<Pet> petList = petRepository.findAll();
		List<PetDTO> petDTOList = new ArrayList<>();
		petList.forEach(pet -> petDTOList.add(mapTo(pet)));

		return petDTOList;
	}

}
