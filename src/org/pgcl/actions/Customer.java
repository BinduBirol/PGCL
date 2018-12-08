package org.pgcl.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.pgcl.dto.AddressDTO;
import org.pgcl.dto.ConnectionLedgerDTO;
import org.pgcl.dto.CustomerConnectionDTO;
import org.pgcl.dto.CustomerDTO;
import org.pgcl.dto.CustomerLedgerDTO;
import org.pgcl.dto.CustomerMeterDTO;
import org.pgcl.dto.CustomerPersonalDTO;
import org.pgcl.dto.DepositDTO;
import org.pgcl.dto.DepositTypeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.BillingService;
import org.pgcl.models.ConnectionService;
import org.pgcl.models.LedgerService;
import org.pgcl.models.CustomerService;
import org.pgcl.models.DepositService;
import org.pgcl.models.MeterService;
import org.pgcl.utils.AC;
import org.pgcl.utils.Utils;
import org.pgcl.utils.cache.CacheUtil;

import com.google.gson.Gson;

public class Customer extends BaseAction implements SessionAware{
	
	private static final long serialVersionUID = -5599690018556916682L;
	private String customer_id;
	private String customer_categoty;
	private CustomerDTO customer;
	private CustomerMeterDTO meter;
	private CustomerPersonalDTO personal;
	private AddressDTO address;
	private String meter_id;
	private CustomerMeterDTO meter_info;		
	private ArrayList<DepositTypeDTO> depositTypeList=new ArrayList<DepositTypeDTO>();
	private ArrayList<CustomerMeterDTO> meterList=new ArrayList<CustomerMeterDTO>();
	private String isDedaulterOrNot;
	public String validateCustomerId()
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		boolean validate=false;
		
		if(customer_id.length()!=9)
			validate=false;
		else
			validate=CustomerService.validateCustomerId(customer_id);
	     try{
	    	 response.setContentType("text/html");
	    	 response.getWriter().write(validate==true?"true":"false");
	          }
	        catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	public String createNewCustomer()
	{
		ResponseDTO response;
		if(session.get("role").equals("Sales User")){
			response=CustomerService.createNewCustomer(customer, personal, address);
			setJsonResponse(response);
			/*
			if(response.isResponse()==true){
				try {
						FileUtils.copyFile(new File((String)session.get("customer_photo")), new File("D:\\Workspaces\\MyEclipse 8.x\\PGCL\\PGCL_WEB\\resources\\userImages\\"+customer.getCustomer_id()+".jpeg") );
						setJsonResponse(response);
					} 
				catch (IOException e) {
					e.printStackTrace();
					response.setMessasge(response.getMessasge()+" "+e.getMessage());
					setJsonResponse(response);
				}
			}
			*/
		}
		return null;
	}
	public String updateOwnershipInfo()
	{
		ResponseDTO response;
		response=CustomerService.updateOwnerShipInfo(customer, personal);
		setJsonResponse(response);		
		CacheUtil.clear("CUSTOMER_INFO_"+customer.getCustomer_id());
		return null;
	}
	
	public String updateCustomerInfo()
	{
		ResponseDTO response;
		response=CustomerService.updateCustomerInformation(customer, personal,address);
		setJsonResponse(response);		
		CacheUtil.clear("CUSTOMER_INFO_"+customer.getCustomer_id());
		return null;
	}
	
	
	
	public String saveConnectionInfo()
	{
		ResponseDTO response;
		ConnectionService cs=new ConnectionService();
		response=cs.saveConnectionInfo(customer.getConnectionInfo());
		setJsonResponse(response);
		
		if(response.isResponse()==true){
			String area_id=((UserDTO)session.get("user")).getArea_id();
			if(customer.getConnectionInfo().getIsMetered_str().equalsIgnoreCase("1")){
				CacheUtil.clear("ALL_METERED_CUSTOMER_ID_"+area_id);
				CacheUtil.clear("ALL_METERED_CONNECTED_CUSTOMER_ID_"+area_id);
				CacheUtil.clear("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+area_id);
			}
			else{
				CacheUtil.clear("ALL_NON_METERED_CUSTOMER_ID_"+area_id);
				CacheUtil.clear("ALL_NON_METERED_CONNECTED_CUSTOMER_ID_"+area_id);
				CacheUtil.clear("ALL_NON_METERED_DISCONNECTED_CUSTOMER_ID_"+area_id);
			}
			
			CacheUtil.clear("ALL_CUSTOMER_ID_"+area_id);
		}
		return null;
	}
	
	
	
	public String viewCustomer()
	{
		CustomerService cs=new CustomerService();
		customer=cs.getCustomerInfo(this.customer_id);
		DepositService ds=new DepositService();
		String whereClause= " Status=1 ";
		depositTypeList=ds.getDepositTypeList(0, 0, whereClause, "VIEW_ORDER", "ASC", 0);
		BillingService billingService=new BillingService();
		isDedaulterOrNot=billingService.isDefaulterOrNot(customer_id);
		return SUCCESS;
	}
	
	public String getMeterInfo()	
	{
		MeterService ms=new MeterService();
		meterList=ms.getCustomerMeterList(customer_id, meter_id,Utils.EMPTY_STRING);
		
		if(Utils.isNullOrEmpty(meter_id)){	
			meter_info=new CustomerMeterDTO();
			meter_info=meterList.size()>0?meterList.get(0):null;
		}
			
			
		return SUCCESS;
	}
	public String newMeter(){
		return SUCCESS;
	}
	
	public String getCustomerInfoAsJson()
	{
		CustomerService cs=new CustomerService();
			try{
				CustomerDTO customer=new CustomerDTO();
				customer=cs.getCustomerInfo(customer_id);
				if(customer==null)
				{
					setJsonResponse(AC.STATUS_ERROR,"Not a Valid Customer Id.");		
				}
				else
				{
					customer.getPersonalInfo().setImg_url("http://localhost/PGCL_PHOTO/customer/"+customer.getCustomer_id()+".jpg");
					ServletActionContext.getServletContext().setAttribute("customer-"+customer.getCustomer_id(),customer);
					
			        String resp=customer.toString();
			        setJsonResponse(resp);
				}
		   	        
			}
			catch(Exception ex){
				ex.printStackTrace();
				 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		}
	             return null;
	     
	}
	
	public String getCustomerLedger(){
		
		LedgerService customerLedger=new LedgerService();
		ArrayList<CustomerLedgerDTO> customerList=customerLedger.getCustomerLedger(customer_id);		
		Gson gson = new Gson();
		String json = gson.toJson(customerList);
		setJsonResponse(json);
        return null;
	}
	
	public String getConnectionLedger(){
		
		LedgerService connectionLedger=new LedgerService();
		ArrayList<ConnectionLedgerDTO> ledger=connectionLedger.getConnectionLedger(customer_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(ledger);
		setJsonResponse(json);
        return null;
	}
	
	
	public String getDefaultSurchargePayWithin()
	{
		
		CustomerService cs=new CustomerService();
		CustomerConnectionDTO surchargePayWithinInfo=cs.getDefaultSurchargePayWithin(customer_categoty);
		
		Gson gson = new Gson();
		String json = gson.toJson(surchargePayWithinInfo);
		setJsonResponse(json);
		
		return null;
	}
	
	



	public String getCustomer_categoty() {
		return customer_categoty;
	}

	public void setCustomer_categoty(String customer_categoty) {
		this.customer_categoty = customer_categoty;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public CustomerPersonalDTO getPersonal() {
		return personal;
	}

	public void setPersonal(CustomerPersonalDTO personal) {
		this.personal = personal;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}	
	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}

	public ArrayList<CustomerMeterDTO> getMeterList() {
		return meterList;
	}

	public void setMeterList(ArrayList<CustomerMeterDTO> meterList) {
		this.meterList = meterList;
	}

	public CustomerMeterDTO getMeter_info() {
		return meter_info;
	}

	public void setMeter_info(CustomerMeterDTO meterInfo) {
		meter_info = meterInfo;
	}

	public ArrayList<DepositTypeDTO> getDepositTypeList() {
		return depositTypeList;
	}

	public void setDepositTypeList(ArrayList<DepositTypeDTO> depositTypeList) {
		this.depositTypeList = depositTypeList;
	}
	public CustomerMeterDTO getMeter() {
		return meter;
	}

	public void setMeter(CustomerMeterDTO meter) {
		this.meter = meter;
	}

	public String getIsDedaulterOrNot() {
		return isDedaulterOrNot;
	}

	public void setIsDedaulterOrNot(String isDedaulterOrNot) {
		this.isDedaulterOrNot = isDedaulterOrNot;
	}

	
}
