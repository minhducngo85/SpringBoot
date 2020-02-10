package com.minhduc.tuto.springboot.hibernate.web.model;

public class SendMoneyForm {
    private long fromAccountId;
    private long toAccountId;
    private Double amount;

    public SendMoneyForm() {

    }

    public SendMoneyForm(long fromAccountId, long toAccountId, Double amount) {
	this.fromAccountId = fromAccountId;
	this.toAccountId = toAccountId;
	this.amount = amount;
    }

    public long getFromAccountId() {
	return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
	this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
	return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
	this.toAccountId = toAccountId;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }
}
