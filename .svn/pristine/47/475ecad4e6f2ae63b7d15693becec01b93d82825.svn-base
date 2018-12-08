package org.pgcl.actions;

import org.pgcl.dto.ResponseDTO;
import org.pgcl.models.LedgerService;

public class Ledger extends BaseAction{

	private static final long serialVersionUID = 3888419044139471548L;	
	private String customer_id;
	
	public String processCustomerLedgerBalance()
	{
		LedgerService ledgerService=new LedgerService();
		ResponseDTO response;
		response=ledgerService.processCustomerLedgerBalance(customer_id);
		setJsonResponse(response);
		return null;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	
}
