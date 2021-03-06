package org.pgcl.dto;

import com.google.gson.Gson;

public class ClearnessDTO {

	private String area;
	private String category;
	private String customerID;
	private String customerName;
	private String customerAddress;
	private String fathersName;
	private String todaysDate;
	private String nextDuesDay;
	private String priviousYear;
	private String dueMonth;
	private double dueAmount;
	private double dueSurcharge;
	private double burner;
	private String payDate;
	private String nonDistributed;
	private String billYear;
	private String prattayanDate;
	private String billMonth;
	private String phoneNo;
	private String billId;
	private String due_status;
	
	

	
	public String getDue_status() {
		return due_status;
	}
	public void setDue_status(String due_status) {
		this.due_status = due_status;
	}
	public double getDueSurcharge() {
		return dueSurcharge;
	}
	public void setDueSurcharge(double dueSurcharge) {
		this.dueSurcharge = dueSurcharge;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getPrattayanDate() {
		return prattayanDate;
	}
	public void setPrattayanDate(String prattayanDate) {
		this.prattayanDate = prattayanDate;
	}
	public String getBillYear() {
		return billYear;
	}
	public void setBillYear(String billYear) {
		this.billYear = billYear;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getNonDistributed() {
		return nonDistributed;
	}
	public void setNonDistributed(String nonDistributed) {
		this.nonDistributed = nonDistributed;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public double getBurner() {
		return burner;
	}
	public void setBurner(double burner) {
		this.burner = burner;
	}
	public String getDueMonth() {
		return dueMonth;
	}
	public void setDueMonth(String dueMonth) {
		this.dueMonth = dueMonth;
	}
	public double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}
	public String getPriviousYear() {
		return priviousYear;
	}
	public void setPriviousYear(String priviousYear) {
		this.priviousYear = priviousYear;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getFathersName() {
		return fathersName;
	}
	public void setFathersName(String fathersName) {
		this.fathersName = fathersName;
	}
	public String getTodaysDate() {
		return todaysDate;
	}
	public void setTodaysDate(String todaysDate) {
		this.todaysDate = todaysDate;
	}
	public String getNextDuesDay() {
		return nextDuesDay;
	}
	public void setNextDuesDay(String nextDuesDay) {
		this.nextDuesDay = nextDuesDay;
	}
	
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}
