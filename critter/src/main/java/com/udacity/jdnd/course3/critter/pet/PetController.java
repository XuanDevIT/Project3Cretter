package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.service.PetService;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
	@Autowired
	private PetService petService;

	@PostMapping
	public PetDTO savePet(@RequestBody PetDTO petDTO) {
		return petService.savePet(petDTO);
	}

	@GetMapping("/{petId}")
	public PetDTO getPet(@PathVariable long petId) {
		return petService.getPet(petId);
	}

	@GetMapping
	public List<PetDTO> getPets() {
		return petService.getPets();
	}

	@GetMapping("/owner/{ownerId}")
	public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
		return petService.getPetsByOwner(ownerId);
	}
}
