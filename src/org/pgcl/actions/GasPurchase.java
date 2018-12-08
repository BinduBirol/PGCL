package org.pgcl.actions;

import java.util.ArrayList;

import org.pgcl.dto.DemandNoteDTO;
import org.pgcl.dto.GasPurchaseDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.models.DemandNoteService;
import org.pgcl.models.GasPurchaseService;

public class GasPurchase extends BaseAction {

	private GasPurchaseDTO gasPurchase;

	public String saveGasPurchaseInfo()
	{
		GasPurchaseService gasPurchaseServoce=new GasPurchaseService();
		ResponseDTO response=gasPurchaseServoce.saveGasPurchaseInfo(gasPurchase);
		setJsonResponse(response);
		return null;
	}

	public GasPurchaseDTO getGasPurchase() {
		return gasPurchase;
	}

	public void setGasPurchase(GasPurchaseDTO gasPurchase) {
		this.gasPurchase = gasPurchase;
	}
	
}
