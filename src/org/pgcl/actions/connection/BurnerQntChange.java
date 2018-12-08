package org.pgcl.actions.connection;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.BurnerQntChangeDTO;
import org.pgcl.dto.MeterRentChangeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.BurnerQntChangeService;
import org.pgcl.models.MeterRentService;

import com.google.gson.Gson;

public class BurnerQntChange  extends BaseAction {

	private static final long serialVersionUID = -8986098148135098574L;
	private String pId; //Burner Change Id
	private BurnerQntChangeDTO bqc;
	
	public String burnerQntChangeHome()
	{
		return SUCCESS;
	}

	public String saveBurnerQntChangeInfo()
	{
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		bqc.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		if(bqc.getPid()==null || bqc.getPid().equalsIgnoreCase(""))
			response=burnerQntService.saveBurnerQntChangeInfo(bqc);
		else
			response=burnerQntService.updateBurnerQntChangeInfo(bqc);
			
		setJsonResponse(response);		

		return null;
	}

	public String getBurnerQntChangeInfo()
	{
		
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		BurnerQntChangeDTO burnerQntChange=burnerQntService.getBurnerQntChangeInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(burnerQntChange);
		setJsonResponse(json);
		
		return null;
	}

	public String deleteBurnerQntChagneInfo(){
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		ResponseDTO response=burnerQntService.deleteBurnerQntChangeInfo(pId);
		
		setJsonResponse(response);
		return null;
	}
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public BurnerQntChangeDTO getBqc() {
		return bqc;
	}

	public void setBqc(BurnerQntChangeDTO bqc) {
		this.bqc = bqc;
	}
	
}
