package com.soap.webservices.soapcoursemanagement.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.soap.webservices.soapcoursemanagement.soap.bean.Course;

@Component
public class CourseDetailsService {
	
	// as we have defined for the delete method in the .xsd file
	// to have an output of SUCCESS or DELETE
	// [instead of the name="status" that returns the return value of the deleteById method in here]
	// we do the same thing here creating an Enum
	public enum Status {
		SUCCESS, FAILURE;
	}

	// In this class with @Component annotation
	// we will define a few methods to manage my courses.
	
	// Initially we create them in memory
	// so we create a static array list
	// and hold it in memory
	private static List<Course> courses = new ArrayList();
	
	static {
		Course course1 = new Course(1, "Spring", "10 Steps");
		courses.add(course1);
		
		Course course2 = new Course(2, "Spring MCV", "10 Example");
				courses.add(course2);
		
		Course course3 = new Course(3, "Spring Boot", "6K Students");
				courses.add(course3);
		
		Course course4 = new Course(4, "Maven", "Most popular maven course on internet");
				courses.add(course4);
	}
	
	// We create some services
	
	// 1) Get the course details on a given id
	// Course findById(int id)
	public Course findById(int id) {
		for(Course course : courses) {
			if(course.getId() == id)
				return course;
		}
		return null;
	}
	
	// 2) Get all courses details
	// List<Course> findAll()
	public List<Course> findAll() {
		 return courses;
	 }
	
	// 3) Delete a course
	// int deleteById(int id)
	public Status deleteById(int id) {
		// The best way to delete an object if through an Iterator
		Iterator<Course> iterator = courses.iterator();
		while(iterator.hasNext()) {
			Course course = iterator.next();
			if(course.getId() == id) {
				iterator.remove();
				return Status.SUCCESS;
			}
		}
		return Status.FAILURE;
	}
	
	// Updating course & new course
	
}
