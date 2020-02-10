package com.minhduc.tuto.springboot.hibernate.web.model;

public class AddBankAccoutForm {
    private String fullName = "";

    private Double balance = 0d;

    /**
     * @return the fullName
     */
    public String getFullName() {
	return fullName;
    }

    /**
     * @param fullName
     *            the fullName to set
     */
    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    /**
     * @return the balance
     */
    public Double getBalance() {
	return balance;
    }

    /**
     * @param balance
     *            the balance to set
     */
    public void setBalance(Double balance) {
	this.balance = balance;
    }
}
