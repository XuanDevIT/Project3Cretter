package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Employee extends User {
	private String employeeSkills;
	private String daysAvailable;

}
