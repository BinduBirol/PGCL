package org.pgcl.actions;
import org.pgcl.dto.DuesSurchargeDTO;
import org.pgcl.dto.OthersDto;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.TariffDTO;
import org.pgcl.models.BillingService;
import org.pgcl.models.ReportService;

import com.google.gson.Gson;

public class Report extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	
	TariffDTO adjustmetAccountPayable=new TariffDTO();

	
	public String saveAdjustmentAccountPayable(){

		ReportService reportService=new ReportService();
		
		ResponseDTO response=reportService.saveAdjustmentAccountPayable(adjustmetAccountPayable);
		setJsonResponse(response);
        return null;
	}


	public TariffDTO getAdjustmetAccountPayable() {
		return adjustmetAccountPayable;
	}


	public void setAdjustmetAccountPayable(TariffDTO adjustmetAccountPayable) {
		this.adjustmetAccountPayable = adjustmetAccountPayable;
	}
	


}
