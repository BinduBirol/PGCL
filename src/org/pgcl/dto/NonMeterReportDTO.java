package org.pgcl.dto;

public class NonMeterReportDTO {
private String customer_id;
private String customer_category_id;
private String customer_category_name;
private String name_address;
private String full_name;
private String address;
private int burner_qnt;
///Meter
private float old_min_load;
private float old_max_load;
private float new_min_load;
private float new_max_load;
private float previous_security;
private float present_security;
private float min_load;
private float max_load;
private float previous_reading;
private float current_reading;
private float actual_consumption;
private float meter_rent;
private String meter_sl_no;
private String effective_date;
private float pertial_bill;
private String comments;
private String disconn_type;
private String disconn_cause;
private double rate;
private String lastDay;



 
public double getRate() {
	return rate;
}
public void setRate(double rate) {
	this.rate = rate;
}
public String getLastDay() {
	return lastDay;
}
public void setLastDay(String lastDay) {
	this.lastDay = lastDay;
}
public String getDisconn_cause() {
	return disconn_cause;
}
public void setDisconn_cause(String disconn_cause) {
	this.disconn_cause = disconn_cause;
}
public String getDisconn_type() {
	return disconn_type;
}
public void setDisconn_type(String disconn_type) {
	this.disconn_type = disconn_type;
}
public float getMax_load() {
	return max_load;
}
public void setMax_load(float max_load) {
	this.max_load = max_load;
}
public String getMeter_sl_no() {
	return meter_sl_no;
}
public void setMeter_sl_no(String meter_sl_no) {
	this.meter_sl_no = meter_sl_no;
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
public String getCustomer_id() {
	return customer_id;
}
public void setCustomer_id(String customer_id) {
	this.customer_id = customer_id;
}


public String getCustomer_category_id() {
	return customer_category_id;
}
public void setCustomer_category_id(String customer_category_id) {
	this.customer_category_id = customer_category_id;
}
public String getCustomer_category_name() {
	return customer_category_name;
}
public void setCustomer_category_name(String customer_category_name) {
	this.customer_category_name = customer_category_name;
}
public String getName_address() {
	return name_address;
}
public void setName_address(String name_address) {
	this.name_address = name_address;
}


public float getOld_min_load() {
	return old_min_load;
}
public void setOld_min_load(float old_min_load) {
	this.old_min_load = old_min_load;
}
public float getOld_max_load() {
	return old_max_load;
}
public void setOld_max_load(float old_max_load) {
	this.old_max_load = old_max_load;
}
public float getNew_min_load() {
	return new_min_load;
}
public void setNew_min_load(float new_min_load) {
	this.new_min_load = new_min_load;
}
public float getNew_max_load() {
	return new_max_load;
}
public void setNew_max_load(float new_max_load) {
	this.new_max_load = new_max_load;
}
public int getBurner_qnt() {
	return burner_qnt;
}
public void setBurner_qnt(int burner_qnt) {
	this.burner_qnt = burner_qnt;
}
public String getEffective_date() {
	return effective_date;
}
public void setEffective_date(String effective_date) {
	this.effective_date = effective_date;
}

public float getPertial_bill() {
	return pertial_bill;
}
public void setPertial_bill(float pertial_bill) {
	this.pertial_bill = pertial_bill;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public float getPrevious_security() {
	return previous_security;
}
public void setPrevious_security(float previous_security) {
	this.previous_security = previous_security;
}
public float getPresent_security() {
	return present_security;
}
public void setPresent_security(float present_security) {
	this.present_security = present_security;
}
public float getPrevious_reading() {
	return previous_reading;
}
public void setPrevious_reading(float previous_reading) {
	this.previous_reading = previous_reading;
}
public float getCurrent_reading() {
	return current_reading;
}
public void setCurrent_reading(float current_reading) {
	this.current_reading = current_reading;
}
public float getActual_consumption() {
	return actual_consumption;
}
public void setActual_consumption(float actual_consumption) {
	this.actual_consumption = actual_consumption;
}
public float getMeter_rent() {
	return meter_rent;
}
public void setMeter_rent(float meter_rent) {
	this.meter_rent = meter_rent;
}
public float getMin_load() {
	return min_load;
}
public void setMin_load(float min_load) {
	this.min_load = min_load;
}

}
