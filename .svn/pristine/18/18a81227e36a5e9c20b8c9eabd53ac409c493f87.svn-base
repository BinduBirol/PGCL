package org.pgcl.actions;

import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.SupplyOffDTO;
import org.pgcl.models.SupplyOffService;

public class SupplyOff extends BaseAction{
	
	private static final long serialVersionUID = -4026297230147889320L;
	private SupplyOffDTO supplyOff;
	
	public String supplyOffHome()
	{
		
			return "success";
	}
	
	public String saveSupplyOff()
	{
		ResponseDTO response=new ResponseDTO();
		response=SupplyOffService.saveSupplyOff(supplyOff);
		setJsonResponse(response);
		return null;	
	}

	public SupplyOffDTO getSupplyOff() {
		return supplyOff;
	}

	public void setSupplyOff(SupplyOffDTO supplyOff) {
		this.supplyOff = supplyOff;
	}
	

}