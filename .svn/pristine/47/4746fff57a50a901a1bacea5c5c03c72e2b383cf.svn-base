package org.pgcl.reports;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.ClearnessDTO;
import org.pgcl.dto.DuesSurchargeDTO;
import org.pgcl.dto.OthersDto;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.models.BillingService;
import org.pgcl.models.ClarificationCertificateService;
import org.pgcl.models.ReconciliationService;
import org.pgcl.utils.Utils;
import org.pgcl.utils.connection.TransactionManager;

import com.google.gson.Gson;

public class ClarificationCertificate extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	private String customer_id;
	private String issue_date;
	private String area;
	private String category;
	private String nondistributed;
	private String approve_type;


	
	public String approveCC(){
		
		ResponseDTO response=ClarificationCertificateService.approveCC(customer_id,issue_date,area,category,nondistributed,approve_type);
		setJsonResponse(response);
		return null;
	}


	

	public String getApprove_type() {
		return approve_type;
	}




	public void setApprove_type(String approve_type) {
		this.approve_type = approve_type;
	}




	public String getCustomer_id() {
		return customer_id;
	}



	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}



	public String getIssue_date() {
		return issue_date;
	}



	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
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



	public String getNondistributed() {
		return nondistributed;
	}



	public void setNondistributed(String nondistributed) {
		this.nondistributed = nondistributed;
	}




	

	
}
