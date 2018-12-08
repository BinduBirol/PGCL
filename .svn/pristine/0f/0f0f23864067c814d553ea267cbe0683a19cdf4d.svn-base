package org.pgcl.actions.connection;

import java.util.ArrayList;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.CustomerMeterDTO;
import org.pgcl.dto.EVCModelDTO;
import org.pgcl.dto.MeterGRatingDTO;
import org.pgcl.dto.MeterMfgDTO;
import org.pgcl.dto.MeterRentChangeDTO;
import org.pgcl.dto.MeterReplacementDTO;
import org.pgcl.dto.MeterTypeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.MeterInformationService;
import org.pgcl.models.MeterRentService;
import org.pgcl.models.MeterReplacementService;
import org.pgcl.models.MeterService;
import org.pgcl.utils.Utils;

import com.google.gson.Gson;

public class MeterReplacement extends BaseAction {

	private static final long serialVersionUID = 4949543267364711284L;
	private String pId;
	private String customer_id;
	private ArrayList<MeterMfgDTO>  mfgList;
	private ArrayList<MeterTypeDTO> meterTypeList;
	private ArrayList<MeterGRatingDTO> gRatingList;
	private ArrayList<EVCModelDTO> evcModelList;
	private CustomerMeterDTO newMeter;
	private CustomerMeterDTO oldMeter;
	
	public void prepareList(){		
		MeterService meterService=new MeterService();
		mfgList=meterService.getMfgList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		meterTypeList=meterService.getMeterTypeList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		gRatingList=meterService.getGRatingList(0,0,Utils.EMPTY_STRING,"VIEW_ORDER","ASC",0);
		evcModelList=meterService.getEvcModelList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public String meterReplacementHome(){
		prepareList();
		return SUCCESS;
	}

	public String saveMeterReplacementInfo(){
		
		MeterReplacementService mrService=new MeterReplacementService();
		newMeter.setInsert_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		response=mrService.saveMeterReplacementInfo(oldMeter,newMeter);
			
		setJsonResponse(response);		

		return null;
	}
	public String getMeterReplacementInfo()
	{
		
		MeterReplacementService mrService=new MeterReplacementService();
		MeterReplacementDTO replacementInfo=mrService.getMeterReplacementInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(replacementInfo);
		setJsonResponse(json);
		
		return null;
	}

	public String deleteMeterReplacementInfo(){
		MeterReplacementService mrService=new MeterReplacementService();		
		ResponseDTO response=mrService.deleteReplacementInfo(pId);
		setJsonResponse(response);		
		return null;
	}
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public ArrayList<MeterMfgDTO> getMfgList() {
		return mfgList;
	}
	public void setMfgList(ArrayList<MeterMfgDTO> mfgList) {
		this.mfgList = mfgList;
	}
	public ArrayList<MeterTypeDTO> getMeterTypeList() {
		return meterTypeList;
	}
	public void setMeterTypeList(ArrayList<MeterTypeDTO> meterTypeList) {
		this.meterTypeList = meterTypeList;
	}
	public ArrayList<MeterGRatingDTO> getgRatingList() {
		return gRatingList;
	}
	public void setgRatingList(ArrayList<MeterGRatingDTO> gRatingList) {
		this.gRatingList = gRatingList;
	}
	public ArrayList<EVCModelDTO> getEvcModelList() {
		return evcModelList;
	}
	public void setEvcModelList(ArrayList<EVCModelDTO> evcModelList) {
		this.evcModelList = evcModelList;
	}

	public CustomerMeterDTO getNewMeter() {
		return newMeter;
	}

	public void setNewMeter(CustomerMeterDTO newMeter) {
		this.newMeter = newMeter;
	}

	public CustomerMeterDTO getOldMeter() {
		return oldMeter;
	}

	public void setOldMeter(CustomerMeterDTO oldMeter) {
		this.oldMeter = oldMeter;
	}
	
	
}
