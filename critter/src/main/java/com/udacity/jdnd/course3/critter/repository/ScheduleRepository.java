package com.udacity.jdnd.course3.critter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udacity.jdnd.course3.critter.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	// find schedule by employee
	List<Schedule>  findByEmployees_Id(Long employeeId);
	// find schedule by pet
    List<Schedule>  findByPets_Id(Long petId);
}
