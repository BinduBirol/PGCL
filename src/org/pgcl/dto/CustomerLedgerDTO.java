package org.pgcl.dto;

import com.google.gson.Gson;

public class CustomerLedgerDTO {

	private String customer_id;
	private String entry_type;
	private String issue_paid_date;
	private String particulars;
	private String gas_sold;
	private String sales_amount;
	private String meter_rent;
	private String surcharge;
	private String debit_amount;
	private String credit_amount;
	private double balance_amount;
	private String due_date;
	private String status;
	private String vat_rebate;
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getEntry_type() {
		return entry_type;
	}
	public void setEntry_type(String entryType) {
		entry_type = entryType;
	}
	public String getIssue_paid_date() {
		return issue_paid_date;
	}
	public void setIssue_paid_date(String issuePaidDate) {
		issue_paid_date = issuePaidDate;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public String getGas_sold() {
		return gas_sold;
	}
	public void setGas_sold(String gasSold) {
		gas_sold = gasSold;
	}
	public String getSales_amount() {
		return sales_amount;
	}
	public void setSales_amount(String salesAmount) {
		sales_amount = salesAmount;
	}
	public String getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(String meterRent) {
		meter_rent = meterRent;
	}
	public String getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(String surcharge) {
		this.surcharge = surcharge;
	}
	public String getDebit_amount() {
		return debit_amount;
	}
	public void setDebit_amount(String debitAmount) {
		debit_amount = debitAmount;
	}
	public String getCredit_amount() {
		return credit_amount;
	}
	public void setCredit_amount(String creditAmount) {
		credit_amount = creditAmount;
	}
	public double getBalance_amount() {
		return balance_amount;
	}
	public void setBalance_amount(double balanceAmount) {
		balance_amount = balanceAmount;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String dueDate) {
		due_date = dueDate;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVat_rebate() {
		return vat_rebate;
	}
	public void setVat_rebate(String vat_rebate) {
		this.vat_rebate = vat_rebate;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }		
}
