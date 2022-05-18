package com.soap.webservices.soapcoursemanagement.soap.bean;

// In this class we want all the details that we want to store for a specific course
// For now the details we have are: id, name, description;

// After we have defined for this class our stuff [id, name, description]
// we want to get this course from a service,
// this means that we want the endpoint talking with a service

public class Course {
	
	private int id;
	private String name;
	private String description;
	
	// CTOR
	public Course(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	// GETTERS and SETTERS
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	// toString METHOD
	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	
}
