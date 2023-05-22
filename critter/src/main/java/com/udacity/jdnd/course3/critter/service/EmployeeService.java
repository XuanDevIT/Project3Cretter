package com.udacity.jdnd.course3.critter.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.entitty.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
		String employeeName = employeeDTO.getName();

		String employeeSkills = "";
		for (EmployeeSkill employeeSkill : employeeDTO.getSkills()) {
			employeeSkills += "," + employeeSkill.name();
		}
		employeeSkills = employeeSkills.substring(1);

		String dayAvailable = "";
		if (employeeDTO.getDaysAvailable() != null) {
			for (DayOfWeek dayOfWeek : employeeDTO.getDaysAvailable()) {
				dayAvailable += "," + dayOfWeek.name();
			}
			dayAvailable = dayAvailable.substring(1);
		}

		Employee employee = new Employee(employeeName, employeeSkills, dayAvailable);
		Employee employeeSaved = employeeRepository.save(employee);

		return mapEmployeeToEmployeeDTO(employeeSaved);
	}

	public EmployeeDTO getEmployee(long employeeId) {
		Employee employee = employeeRepository.getOne(employeeId);
		return mapEmployeeToEmployeeDTO(employee);
	}

	private Set<EmployeeSkill> convertStringToSetSkill(String skills) {
		Set<EmployeeSkill> empSkills = new HashSet<>();
		for (String skill : skills.split(","))
			empSkills.add(EmployeeSkill.valueOf(skill));
		return empSkills;
	}

	public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
		List<EmployeeDTO> employeeDTOs = new ArrayList<>();
		employeeRepository.findAll().forEach(emp -> {
			Set<EmployeeSkill> employeeSkills = convertStringToSetSkill(emp.getEmployeeSkills());
			if (employeeSkills.containsAll(employeeDTO.getSkills())
					&& emp.getdaysAvailable().contains(employeeDTO.getDate().getDayOfWeek().name())) {
				employeeDTOs.add(mapEmployeeToEmployeeDTO(emp));
			}
		});
		return employeeDTOs;
	}

	public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
		Employee employee = employeeRepository.getOne(employeeId);
		String daysAvailableNew = "";
		for (DayOfWeek dayWeek : daysAvailable) {
			daysAvailableNew += "," + dayWeek.name();
		}
		daysAvailableNew = daysAvailableNew.substring(1);
		employee.setdaysAvailable(daysAvailableNew);

		employeeRepository.save(employee);
	}

	private EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee) {
		EmployeeDTO empDTO = new EmployeeDTO();
		empDTO.setId(employee.getId());
		empDTO.setName(employee.getName());

		Set<DayOfWeek> dayOfWeeks = null;
		if (employee.getdaysAvailable().length() != 0) {
			dayOfWeeks = new HashSet<>();
			String[] dayAvailables = employee.getdaysAvailable().split(",");
			for (String dayAvailable : dayAvailables) {
				dayOfWeeks.add(DayOfWeek.valueOf(dayAvailable));
			}
		}
		empDTO.setDaysAvailable(dayOfWeeks);

		String[] skills = employee.getEmployeeSkills().split(",");
		Set<EmployeeSkill> employeeSkills = new HashSet<>();
		//
		for (String skill : skills) {
			employeeSkills.add(EmployeeSkill.valueOf(skill));
		}
		empDTO.setSkills(employeeSkills);

		return empDTO;
	}

}
