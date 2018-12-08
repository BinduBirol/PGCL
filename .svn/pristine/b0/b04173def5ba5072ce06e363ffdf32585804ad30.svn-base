package org.pgcl.dto;

import com.google.gson.Gson;

public class CustomerDTO {

	private String customer_id;
	private String customer_category;
	private String area;
	private String zone;
	private CustomerPersonalDTO personalInfo;
	private AddressDTO addressInfo;
	private CustomerConnectionDTO connectionInfo;
	private DisconnectDTO latestDisconnectInfo;	
	private String address;
	/*Extra attribute*/
	private String area_name;
	private String zone_name;
	private String customer_category_name;
	private String app_sl_no;
	private String Inserted_by;
	
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public CustomerPersonalDTO getPersonalInfo() {
		return personalInfo;
	}
	public void setPersonalInfo(CustomerPersonalDTO personalInfo) {
		this.personalInfo = personalInfo;
	}	
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String areaName) {
		area_name = areaName;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zoneName) {
		zone_name = zoneName;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customerCategoryName) {
		customer_category_name = customerCategoryName;
	}	
	public AddressDTO getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(AddressDTO addressInfo) {
		this.addressInfo = addressInfo;
	}
	
	public CustomerConnectionDTO getConnectionInfo() {
		return connectionInfo;
	}
	public void setConnectionInfo(CustomerConnectionDTO connectionInfo) {
		this.connectionInfo = connectionInfo;
	}		
	public String getApp_sl_no() {
		return app_sl_no;
	}
	public void setApp_sl_no(String appSlNo) {
		app_sl_no = appSlNo;
	}
	public DisconnectDTO getLatestDisconnectInfo() {
		return latestDisconnectInfo;
	}
	public void setLatestDisconnectInfo(DisconnectDTO latestDisconnectInfo) {
		this.latestDisconnectInfo = latestDisconnectInfo;
	}	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getInserted_by() {
		return Inserted_by;
	}
	public void setInserted_by(String inserted_by) {
		Inserted_by = inserted_by;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}
