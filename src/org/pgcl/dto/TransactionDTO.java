package org.pgcl.dto;

import com.google.gson.Gson;

public class TransactionDTO {
	
	private String trans_id;
	private String trans_date;
	private String trans_type;
	private String particulars;
	private String bank_id;
	private String branch_id;
	private String account_no;
	private String bank_name;
	private String branch_name;
	private String account_name;
	private Double debit;
	private Double credit;
	private Double balance;
	private String ref_id;
	private String inserted_on;
	private String inserted_by;
	private String customer_id;
	private String full_name;
	private String billMonth;
	
	private String unauth_count;
	
	private String recon_cause;//reconcilation cause
	
	CustomerDTO customer;
	
	
	
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public String getRecon_cause() {
		return recon_cause;
	}
	public void setRecon_cause(String recon_cause) {
		this.recon_cause = recon_cause;
	}

	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String transId) {
		trans_id = transId;
	}
	public String getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(String transDate) {
		trans_date = transDate;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String accountNo) {
		account_no = accountNo;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bankName) {
		bank_name = bankName;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branchName) {
		branch_name = branchName;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String accountName) {
		account_name = accountName;
	}

	public String getRef_id() {
		return ref_id;
	}
	public void setRef_id(String refId) {
		ref_id = refId;
	}
	public String getInserted_on() {
		return inserted_on;
	}
	public void setInserted_on(String insertedOn) {
		inserted_on = insertedOn;
	}
	public String getInserted_by() {
		return inserted_by;
	}
	public void setInserted_by(String insertedBy) {
		inserted_by = insertedBy;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}	
	public String getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(String transType) {
		trans_type = transType;
	}	
	
	public String getUnauth_count() {
		return unauth_count;
	}
	public void setUnauth_count(String unauthCount) {
		unauth_count = unauthCount;
	}
	
	public Double getDebit() {
		return debit;
	}
	public void setDebit(Double debit) {
		this.debit = debit;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}
