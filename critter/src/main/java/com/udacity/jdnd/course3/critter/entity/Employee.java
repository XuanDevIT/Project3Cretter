package com.udacity.jdnd.course3.critter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User {
	private String employeeSkills;
	private String daysAvailable;

	public Employee(String name, String employeeSkills, String daysAvailable) {
		super(name);
		this.employeeSkills = employeeSkills;
		this.daysAvailable = daysAvailable;
	}

}
