package org.pgcl.actions;

import org.pgcl.dto.JvBankAccountCorrectionDTO;
import org.pgcl.dto.JvCustomerAccountCorrectionDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.JournalVoucherService;

public class JournalVoucher extends BaseAction {

	private static final long serialVersionUID = -978583962839915195L;
	private JvCustomerAccountCorrectionDTO customerAccountCorrection;
	private JvBankAccountCorrectionDTO bankAccountCorrection;
	
	public String saveCustomerAccountCorrection()
	{
		ResponseDTO response;
		customerAccountCorrection.setInserted_by(((UserDTO)session.get("user")).getUserId());
		JournalVoucherService jvService=new JournalVoucherService();
		response=jvService.saveCustomerAccountCorrection(customerAccountCorrection);
		setJsonResponse(response);
		return null;
	}
	public String saveBankAccountCorrection()
	{
		ResponseDTO response;
		bankAccountCorrection.setInserted_by(((UserDTO)session.get("user")).getUserId());
		JournalVoucherService jvService=new JournalVoucherService();
		response=jvService.saveBankAccountCorrection(bankAccountCorrection);
		setJsonResponse(response);
		return null;
	}	
	
	

	public JvCustomerAccountCorrectionDTO getCustomerAccountCorrection() {
		return customerAccountCorrection;
	}

	public void setCustomerAccountCorrection(
			JvCustomerAccountCorrectionDTO customerAccountCorrection) {
		this.customerAccountCorrection = customerAccountCorrection;
	}
	public JvBankAccountCorrectionDTO getBankAccountCorrection() {
		return bankAccountCorrection;
	}
	public void setBankAccountCorrection(
			JvBankAccountCorrectionDTO bankAccountCorrection) {
		this.bankAccountCorrection = bankAccountCorrection;
	}
	
}
