package com.udacity.jdnd.course3.critter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User {
	private String notes;
	private String phone;

	@OneToMany(mappedBy = "customer")
	private List<Pet> pets;

	public Customer(String name, String phone, String notes) {
		super(name);
		this.phone = phone;
		this.notes = notes;
	}

}
