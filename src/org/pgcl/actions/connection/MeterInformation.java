package org.pgcl.actions.connection;

import java.util.ArrayList;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.CustomerMeterDTO;
import org.pgcl.dto.EVCModelDTO;
import org.pgcl.dto.MeterGRatingDTO;
import org.pgcl.dto.MeterMfgDTO;
import org.pgcl.dto.MeterTypeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.MeterInformationService;
import org.pgcl.models.MeterService;
import org.pgcl.utils.Utils;

import com.google.gson.Gson;

public class MeterInformation extends BaseAction {

	private static final long serialVersionUID = -31040100891842890L;
	private String customer_id;
	private String meter_id;
	private ArrayList<MeterMfgDTO>  mfgList;
	private ArrayList<MeterTypeDTO> meterTypeList;
	private ArrayList<MeterGRatingDTO> gRatingList;
	private ArrayList<EVCModelDTO> evcModelList;
	private CustomerMeterDTO meter;
	
	public void prepareList(){		
		MeterService meterService=new MeterService();
		mfgList=meterService.getMfgList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		meterTypeList=meterService.getMeterTypeList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		gRatingList=meterService.getGRatingList(0,0,Utils.EMPTY_STRING,"VIEW_ORDER","ASC",0);
		evcModelList=meterService.getEvcModelList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public String meterInformationHome()
	{
		prepareList();
		return SUCCESS;
	}
	public String saveMeterInfo(){
		
		MeterInformationService miService=new MeterInformationService();
		meter.setInsert_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		if(meter.getMeter_id()==null || meter.getMeter_id().equalsIgnoreCase(""))
			response=miService.saveMeterInfo(meter);
		else
			response=miService.updateMeterInfo(meter);
			
		setJsonResponse(response);		

		return null;
	}
	public String deleteMeter(){
		ResponseDTO response=MeterInformationService.deleteMeter(meter.getMeter_id());
		setJsonResponse(response);		
		return null;
	}
	
	public String meterEditStatusCheck(){
		ResponseDTO response=MeterService.isMeterBasicInfoChangeValid(meter.getMeter_id());
		Gson gson = new Gson();
		String json = gson.toJson(response);
		setJsonResponse(json);
        return null;

	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
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

	public CustomerMeterDTO getMeter() {
		return meter;
	}

	public void setMeter(CustomerMeterDTO meter) {
		this.meter = meter;
	}
	
	
}
