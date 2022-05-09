package com.minhduc.tuto.angularspring;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String email;

    public User() {
	this.name = "";
	this.email = "";
    }

    public User(String name, String email) {
	this.name = name;
	this.email = email;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    @Override
    public String toString() {
	return "User{" + "id=" + id + ", name=" + name + ", email=" + email + '}';
    }

}
