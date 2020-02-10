package com.minhduc.tuto.springboot.hibernate.web.model;

import com.minhduc.tuto.springboot.hibernate.entity.BankAccount;

public class BankAccountModel {
    private long id;
    private String fullName;
    private double balance;

    public BankAccountModel() {

    }

    public BankAccountModel(long id, String fullName, double balance) {
	this.id = id;
	this.fullName = fullName;
	this.balance = balance;
    }

    public BankAccountModel(BankAccount entity) {
	this.id = entity.getId();
	this.fullName = entity.getFullName();
	this.balance = entity.getBalance();
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public double getBalance() {
	return balance;
    }

    public void setBalance(double balance) {
	this.balance = balance;
    }
}
