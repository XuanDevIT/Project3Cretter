package com.udacity.jdnd.course3.critter.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

@Service
public class ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PetRepository petRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public List<ScheduleDTO> getAllSchedules() {
		List<Schedule> schedules = scheduleRepository.findAll();
		return mapSchedulesToScheduleDTOs(schedules);
	}

	public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
		List<Employee> employeeList = new ArrayList<>();
		scheduleDTO.getEmployeeIds().forEach(employeeId -> {
			employeeList.add(employeeRepository.findById(employeeId).get());
		});

		List<Pet> petList = new ArrayList<>();
		//
		scheduleDTO.getPetIds().forEach(petId -> {
			petList.add(petRepository.findById(petId).get());
		});

		Schedule schedule = new Schedule();
		schedule.setActivities(convertSetActivitesToString(scheduleDTO.getActivities()));
		schedule.setDate(scheduleDTO.getDate());
		schedule.setEmployees(employeeList);
		schedule.setPets(petList);

		scheduleRepository.save(schedule);

		scheduleDTO.setId(schedule.getId());
		return scheduleDTO;
	}

	public List<ScheduleDTO> getScheduleForPet(long petId) {
		List<Schedule> schedules = scheduleRepository.findByPets_Id(petId);
		return mapSchedulesToScheduleDTOs(schedules);
	}

	public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
		
		Customer customer = customerRepository.findById(customerId).get();
		List<Pet> petList = customer.getPets();

		//
		List<Schedule> scheduleList = new ArrayList<>();
		for (Pet pet : petList) {
			scheduleList.addAll(scheduleRepository.findByPets_Id(pet.getId()));
		}

		return mapSchedulesToScheduleDTOs(scheduleList);
	}
	
	private String convertSetActivitesToString(Set<EmployeeSkill> employeeSkills) {
		String activities = "";
		for (EmployeeSkill employeeSkill : employeeSkills) {
			activities += "," + employeeSkill.name();
		}
		return activities.substring(1);
	}

	public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
		List<Schedule> scheduleList = scheduleRepository.findByEmployees_Id(employeeId);
		return mapSchedulesToScheduleDTOs(scheduleList);
	}
	

	private List<ScheduleDTO> mapSchedulesToScheduleDTOs(List<Schedule> schedules) {
		List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

		schedules.forEach(schedule -> {
			ScheduleDTO scheduleDTO = new ScheduleDTO();

			scheduleDTO.setId(schedule.getId());
			scheduleDTO.setDate(schedule.getDate());

			Set<EmployeeSkill> employeeSkills = new HashSet<>();
			for (String skill : schedule.getActivities().split(",")) {
				employeeSkills.add(EmployeeSkill.valueOf(skill));
			}
			scheduleDTO.setActivities(employeeSkills);

			List<Long> employeeIds = new ArrayList<>();
			for (Employee employee : schedule.getEmployees()) {
				employeeIds.add(employee.getId());
			}
			scheduleDTO.setEmployeeIds(employeeIds);

			List<Long> petIds = new ArrayList<>();
			for (Pet pet : schedule.getPets()) {
				petIds.add(pet.getId());
			}
			scheduleDTO.setPetIds(petIds);

			scheduleDTOs.add(scheduleDTO);
		});
		return scheduleDTOs;
	}
}
