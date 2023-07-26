package com.springboot.tutorial.controllers;

import com.springboot.tutorial.models.Employee;
import com.springboot.tutorial.models.ResponseObject;
import com.springboot.tutorial.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RestController
// @RestController annotation tell Java Spring that this class is a Controller
//@RequestMapping(path = "/api/v1/Employees")
//Routing It means that with link "../api/v1/Employees" sent request to this Controller (EmployeeController)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //display all employees (list of employees)
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        return "index";
    }

//    @PostMapping("/create")
//    ResponseEntity<ResponseObject> insertProduct(@RequestBody Employee newEmployee) {
//        return "a";
//    }

    @GetMapping("/newEmployeeForm")
    public String createNewEmployee(Model model) {
        //Create model attribute to bind form data
        Employee employee = new Employee();
        model.addAttribute("employee",employee);
        // Thymeleaf template will access this empty employee object for binding form data
        return "new_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        // save  new employee to database
        employeeService.createNewEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
        // Get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("employee",employee);
        return "update_employee";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value = "id") long id) {
//        call delete employee method
        this.employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }
    // Delete failed
}
