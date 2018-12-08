package org.pgcl.actions;

import org.pgcl.dto.ResponseDTO;
import org.pgcl.models.BankBookBalanceService;

public class BalanceUpdate extends BaseAction {

	private static final long serialVersionUID = -4441750153097656958L;

	private String bank_id;
	private String branch_id;
	private String account_no;
	
	
	public String bankBalanceUpdate()
	{
		ResponseDTO response;
		BankBookBalanceService balanceService=new BankBookBalanceService();
		response=balanceService.updateBalance(bank_id, branch_id, account_no);
		setJsonResponse(response);
		return null;
	}

	public String bankBalanceUpdateHome(){
		return SUCCESS;
	}

	public String getBank_id() {
		return bank_id;
	}


	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}


	public String getBranch_id() {
		return branch_id;
	}


	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}


	public String getAccount_no() {
		return account_no;
	}


	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	
	
}
