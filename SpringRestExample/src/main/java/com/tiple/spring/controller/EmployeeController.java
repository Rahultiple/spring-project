package com.tiple.spring.controller;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tiple.spring.controller.EmpRestURIConstants;
import com.tiple.spring.controller.EmployeeController;
import com.tiple.spring.model.Employee;
import com.tiple.spring.model.Person;
import com.tiple.spring.service.PersonService;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
/**
 * Handles requests for the Employee service.
 */
@Controller
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	/*for the Autowiring of Person
	 * 
	 */
	private PersonService personService;
	
	
	@Autowired(required=true)
	@Qualifier(value="personService")
	public void setPersonService(PersonService ps){
		this.personService = ps;
	}
	
	@RequestMapping(value = EmpRestURIConstants.GET_LIST_PERSON, method = RequestMethod.GET)
	public @ResponseBody List<Person> getListPersons() {
		logger.info("-----Start getListPersons----");
		return this.personService.listPersons();
	}
	
	/*
	 * To add the Person 
	 */
	@RequestMapping(value = EmpRestURIConstants.ADD_PERSON, method = RequestMethod.POST)
	public @ResponseBody boolean addPersons(@RequestBody Person person) {
		logger.info(person.getName()+"-----Start Adding Person----"+person.getCountry());
			personService.addPerson(person);
			return true;
	}
	@RequestMapping(value = EmpRestURIConstants.DELETE_PERSON, method = RequestMethod.GET)
	public @ResponseBody boolean deletePerson(@PathVariable("id") int id) {
		logger.info("Start deleteEmployee.");
		personService.removePerson(id);
		
		return true;
	}
	@RequestMapping(value = EmpRestURIConstants.UPLOAD_PERSON, method = RequestMethod.POST)
	public @ResponseBody boolean uploadImage(@RequestParam(value = "file")  byte[] file,
			@RequestParam(value="person", required=true) String person) {    
	 System.out.println("----Start uploadImage----"+file);

	 logger.info("-----Start uploadImage----"+file);
			//personService.addPerson(person);
		System.out.println("request received to upload image " + file);
	    if (null != file) {
	       // final InputStream inputStream = new ByteArrayInputStream(file);
	        //System.out.println("uploading image wuth stream" + inputStream);
	       try {
	    	String filename="D:/image/"+person+"_"+(UUID.randomUUID()+"").split("-")[0]+".jpg";
			FileOutputStream fos= new FileOutputStream(filename);
			fos.write(file);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	    } else {
	    	return false;
	    }	
		
		
		return true;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}	
	
	//Map to store employees, ideally we should use database
	Map<Integer, Employee> empData = new HashMap<Integer, Employee>();
	
	@RequestMapping(value = EmpRestURIConstants.DUMMY_EMP, method = RequestMethod.GET)
	public @ResponseBody Employee getDummyEmployee() {
		logger.info("Start getDummyEmployee");
		Employee emp = new Employee();
		emp.setId(9999);
		emp.setName("Rahul Tiple");
		emp.setCreatedDate(new Date());
		empData.put(9999, emp);
		Calendar cal=Calendar.getInstance();
		return emp;
	}
	
	@RequestMapping(value = EmpRestURIConstants.GET_EMP, method = RequestMethod.GET)
	public @ResponseBody Employee getEmployee(@PathVariable("id") int empId) {
		logger.info("-----Start getEmployee. ID="+empId);
		
		return empData.get(empId);
	}
	
	@RequestMapping(value = EmpRestURIConstants.GET_ALL_EMP, method = RequestMethod.GET)
	public @ResponseBody List<Employee> getAllEmployees() {
		logger.info("Start getAllEmployees.");
		List<Employee> emps = new ArrayList<Employee>();
		Set<Integer> empIdKeys = empData.keySet();
		for(Integer i : empIdKeys){
			emps.add(empData.get(i));
		}
		return emps;
	}
	
	@RequestMapping(value = EmpRestURIConstants.CREATE_EMP, method = RequestMethod.POST ,consumes="application/json")
	public @ResponseBody Employee createEmployee(@RequestBody Employee emp) {
		logger.info("Start createEmployee.");
		emp.setCreatedDate(new Date());
		empData.put(emp.getId(), emp);
		return emp;
	}
	
	@RequestMapping(value = EmpRestURIConstants.DELETE_EMP, method = RequestMethod.PUT)
	public @ResponseBody Employee deleteEmployee(@PathVariable("id") int empId) {
		logger.info("Start deleteEmployee.");
		Employee emp = empData.get(empId);
		empData.remove(empId);
		return emp;
	}
	
}
