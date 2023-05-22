package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
@Data
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	private String activities;

	@ManyToMany
	private List<Pet> pets;
	
	@ManyToMany
	private List<Employee> employees;

}
