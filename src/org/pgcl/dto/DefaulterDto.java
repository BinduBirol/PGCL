package org.pgcl.dto;

public class DefaulterDto {
private String customer_id;
private String full_name;
private String category_id;
private String category_name;
private String address;
private String contact_no;
private String due_month;
private float amount;
private int total_month;
private String status;
public String getCustomer_id() {
	return customer_id;
}
public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
}
public String getFull_name() {
	return full_name;
}
public void setFull_name(String full_name) {
	this.full_name = full_name;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getContact_no() {
	return contact_no;
}
public void setContact_no(String contact_no) {
	this.contact_no = contact_no;
}
public String getDue_month() {
	return due_month;
}
public void setDue_month(String due_month) {
	this.due_month = due_month;
}
public float getAmount() {
	return amount;
}
public void setAmount(float amount) {
	this.amount = amount;
}


public String getCategory_id() {
	return category_id;
}
public void setCategory_id(String category_id) {
	this.category_id = category_id;
}
public String getCategory_name() {
	return category_name;
}
public void setCategory_name(String category_name) {
	this.category_name = category_name;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getTotal_month() {
	return total_month;
}
public void setTotal_month(int total_month) {
	this.total_month = total_month;
}


}
