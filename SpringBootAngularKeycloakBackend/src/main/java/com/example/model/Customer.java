package com.example.model;

public class Customer {
    private int id;
    private String name;
    private int age = 18;
    
    
    public Customer(int id, String name, int age) {
	super();
	this.id = id;
	this.name = name;
	this.age = age;
    }
    /**
     * @return the name
     */
    public String getName() {
	return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }
    /**
     * @return the id
     */
    public int getId() {
	return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
	this.id = id;
    }
    /**
     * @return the age
     */
    public int getAge() {
	return age;
    }
    /**
     * @param age the age to set
     */
    public void setAge(int age) {
	this.age = age;
    }
    
}
