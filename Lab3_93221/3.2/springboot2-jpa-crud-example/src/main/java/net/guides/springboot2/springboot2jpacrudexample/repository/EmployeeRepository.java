package net.guides.springboot2.springboot2jpacrudexample.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.guides.springboot2.springboot2jpacrudexample.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
		
	Employee findByEmailId(String emailId);

}