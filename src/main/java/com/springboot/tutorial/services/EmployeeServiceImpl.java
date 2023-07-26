package com.springboot.tutorial.services;

import com.springboot.tutorial.models.Employee;
import com.springboot.tutorial.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void createNewEmployee(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(long id) {
        Optional<Employee> existEmployee = employeeRepository.findById(id);
        Employee employee = null;
        if (existEmployee.isPresent()) {
            employee = existEmployee.get();
        } else {
            throw new RuntimeException("Not found employee with this id = "+ id);
        }
        return employee;
    }

    @Override
    public void deleteEmployeeById(long id) {
        this.employeeRepository.deleteById(id);
    }
    // Delete failed
}
