package com.minhduc.tuto.microservices.currencyconversion.ribbon;

import java.math.BigDecimal;

/**
 * 
 * @author Minh Duc Ngo
 *
 */
public class CurrencyConversionBean {
    private Long id;
    private String from;
    private String to;
    private BigDecimal conversionMultiple;
    private BigDecimal quantity;
    private BigDecimal totalCalculatedAmount;
    private int port;

    public CurrencyConversionBean() {

    }

    public CurrencyConversionBean(Long id, String from, String to, BigDecimal conversionMultiple, BigDecimal quantity,
	    BigDecimal totalCalculatedAmount, int port) {
	super();
	this.setId(id);
	this.setFrom(from);
	this.setTo(to);
	this.setConversionMultiple(conversionMultiple);
	this.setQuantity(quantity);
	this.setTotalCalculatedAmount(totalCalculatedAmount);
	this.setPort(port);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the from
     */
    public String getFrom() {
	return from;
    }

    /**
     * @param from
     *            the from to set
     */
    public void setFrom(String from) {
	this.from = from;
    }

    /**
     * @return the conversionMultiple
     */
    public BigDecimal getConversionMultiple() {
	return conversionMultiple;
    }

    /**
     * @param conversionMultiple
     *            the conversionMultiple to set
     */
    public void setConversionMultiple(BigDecimal conversionMultiple) {
	this.conversionMultiple = conversionMultiple;
    }

    /**
     * @return the totalCalculatedAmount
     */
    public BigDecimal getTotalCalculatedAmount() {
	return totalCalculatedAmount;
    }

    /**
     * @param totalCalculatedAmount
     *            the totalCalculatedAmount to set
     */
    public void setTotalCalculatedAmount(BigDecimal totalCalculatedAmount) {
	this.totalCalculatedAmount = totalCalculatedAmount;
    }

    /**
     * @return the to
     */
    public String getTo() {
	return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(String to) {
	this.to = to;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
	return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
	this.quantity = quantity;
    }

    /**
     * @return the port
     */
    public int getPort() {
	return port;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
	this.port = port;
    }
}
