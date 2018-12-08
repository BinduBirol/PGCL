package org.pgcl.utils.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pgcl.dto.CustomerCategoryDTO;
import org.pgcl.dto.ReconciliationDTO;
import org.pgcl.enums.BankAccountTransactionMode;
import org.pgcl.enums.BankAccountTransactionType;
import org.pgcl.enums.BankPaymentParticulars;
import org.pgcl.enums.BankReceiveParticualrs;
import org.pgcl.enums.BankMarginPaymentParticualers;
import org.pgcl.enums.BillPurpose;
import org.pgcl.enums.ConnectionStatus;
import org.pgcl.enums.ConnectionType;
import org.pgcl.enums.DepositPurpose;
import org.pgcl.enums.DepositType;
import org.pgcl.enums.DisconnCause;
import org.pgcl.enums.DisconnCauseNonMeter;
import org.pgcl.enums.DisconnType;
import org.pgcl.enums.LoadChangeType;
import org.pgcl.enums.MeterMeasurementType;
import org.pgcl.enums.MeteredStatus;
import org.pgcl.enums.Month; 
import org.pgcl.enums.ReconciliationCauseAdd;
import org.pgcl.enums.ReconciliationCasuseLess;
import org.pgcl.enums.AccountReconciliation;
import org.pgcl.enums.ReadingPurpose;
import org.pgcl.models.AddressService;
import org.pgcl.models.AreaService;
import org.pgcl.models.BankBranchService;
import org.pgcl.models.CustomerService;
import org.pgcl.models.EmployeeService;
import org.pgcl.models.IpgService;
import org.pgcl.models.MeterService;
import org.pgcl.models.MinistryService;
import org.pgcl.models.ReconciliationService;
import org.pgcl.models.ZoneService;
import org.pgcl.utils.Utils;
import org.pgcl.utils.cache.CacheUtil;


public class StartupResources  extends HttpServlet {
	
	private static final long serialVersionUID = -7227671581272961718L;
	private static final Logger logger = LogManager.getLogger(StartupResources.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
		  
	  }

	public void init(ServletConfig config)  throws ServletException
	{			
		
		loadAndSetEnvironmentConfig(config);		
		
		CustomerService customerService=new CustomerService();
				
		ArrayList<CustomerCategoryDTO> customerCategoryList=customerService.getCustomerCategoryList(0,0,"status=1",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		config.getServletContext().setAttribute("ACTIVE_CUSTOMER_CATEGORY",customerCategoryList);
		config.getServletContext().setAttribute("ACTIVE_AREA",AreaService.getAreaList(0,0,"status=1",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
		config.getServletContext().setAttribute("ALL_MINISTRY",MinistryService.getAllMinistry());
		
		//CacheUtil.setListToCache("ALL_DIVISION", AddressService.getAllDivision());
		config.getServletContext().setAttribute("ALL_DIVISION",AddressService.getAllDivision());
		
		//config.getServletContext().setAttribute("ALL_BANK",BankBranchService.getBankList(0,0,"",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
		

		ZoneService zoneService=new ZoneService();
		config.getServletContext().setAttribute("ALL_ZONE",zoneService.getZoneList(Utils.EMPTY_STRING));

		
		try {Thread.sleep(2000);logger.info("Startup Thread Sleep(2 sec.)");} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		MeterService meterService=new MeterService();
		config.getServletContext().setAttribute("ACTIVE_METER_TYPE",meterService.getMeterTypeList(0, 0, "status=1", "TYPE_NAME", "ASC", 0));
		config.getServletContext().setAttribute("METER_STATUS",meterService.getMeterStatus());
		config.getServletContext().setAttribute("ALL_EMPLOYEE",EmployeeService.getEmployeeList(0, 0, "status=1", "FULL_NAME", "ASC", 0));
		
		try {Thread.sleep(2000);logger.info("Startup Thread Sleep(2 sec.)");} 
		catch (InterruptedException e) {e.printStackTrace();}
		
		/*ArrayList<ReconciliationDTO> causeList=ReconciliationService.getReconciliationCauseList(0, 0, "", "", "ASC", 0);*/
		config.getServletContext().setAttribute("ALL_BANK",BankBranchService.getBankList(0, 0, "bank.status=1", "BANK_NAME", "ASC", 0));
		/*config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE",causeList);*/
		
//		ArrayList<ChangeTypeDTO> changeTypeList=new ArrayList<ChangeTypeDTO>();
//		ChangeTypeDTO[] typeArr = {new ChangeTypeDTO("L","Load Change"),new ChangeTypeDTO("M","Meter Change"),new ChangeTypeDTO("MR","Meter Rent Change"),new ChangeTypeDTO("P","Pressure Change"),new ChangeTypeDTO("L&P","Load & Pressure Change")};		
//		Collections.addAll(changeTypeList, typeArr);
//		config.getServletContext().setAttribute("CHANGE_TYPE",changeTypeList);
		
		HashMap<String, String>  customerCategoryMap = new HashMap<String, String>();		
		for(int i=0;i<customerCategoryList.size();i++)
			customerCategoryMap.put(customerCategoryList.get(i).getCategory_id(), customerCategoryList.get(i).getCategory_name());
		config.getServletContext().setAttribute("CUSTOMER_CATEGORY_MAP", customerCategoryMap);
		
				
		// Storing Enum Values in Servlet Context
		config.getServletContext().setAttribute("DISCONN_CAUSE",DisconnCause.values());
		config.getServletContext().setAttribute("DISCONN_CAUSE_NONMETER",DisconnCauseNonMeter.values());
		config.getServletContext().setAttribute("DISCONN_TYPE",DisconnType.values());
		config.getServletContext().setAttribute("METERED_STATUS",MeteredStatus.values());
		config.getServletContext().setAttribute("LOAD_CHANGE_TYPE",LoadChangeType.values());
		config.getServletContext().setAttribute("DEPOSIT_TYPE",DepositType.values());
		config.getServletContext().setAttribute("DEPOSIT_PURPOSE",DepositPurpose.values());
		config.getServletContext().setAttribute("CONNECTION_STATUS",ConnectionStatus.values());
		config.getServletContext().setAttribute("CONNECTION_TYPE",ConnectionType.values());
		config.getServletContext().setAttribute("BILL_PURPOSE",BillPurpose.values());
		config.getServletContext().setAttribute("READING_PURPOSE",ReadingPurpose.values());
		config.getServletContext().setAttribute("MONTHS",Month.values());
		config.getServletContext().setAttribute("METER_MEASUREMENT_TYPE",MeterMeasurementType.values());
		config.getServletContext().setAttribute("TRANSACTION_MODE",BankAccountTransactionMode.values());
		config.getServletContext().setAttribute("TRANSACTION_TYPE",BankAccountTransactionType.values());
		config.getServletContext().setAttribute("BANK_RECEIVE_PARTICULARS",BankReceiveParticualrs.values());
		config.getServletContext().setAttribute("BANK_PAYMENT_PARTICULARS",BankPaymentParticulars.values());	
		config.getServletContext().setAttribute("BANK_MARGIN_PAYMENT_PARTICULARS",BankMarginPaymentParticualers.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE_ADD",ReconciliationCauseAdd.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_CAUSE_LESS",ReconciliationCasuseLess.values());
		config.getServletContext().setAttribute("ALL_RECONCILIATION_ACCOUNT",AccountReconciliation.values());
		
		

		
		
		config.getServletContext().setAttribute("YEARS",getYearList());
		config.getServletContext().setAttribute("FISCAL_YEARS",getFiscalYearList());
		config.getServletContext().setAttribute("CONNECTION_SIZE",getConnectionSize());
		
		//config.getServletContext().setAttribute("IPG_METHODS",IpgService.getPaymentMethods());
				

	}

	public ArrayList<String> getYearList(){
		ArrayList<String> yearList=new ArrayList<String>();
		int year=Calendar.getInstance().get(Calendar.YEAR)+1;
		for(int i=year;i>year-15;i--){
			yearList.add(String.valueOf(i));
		}
		return yearList;
	}
	
	public ArrayList<String> getFiscalYearList(){
		ArrayList<String> yearList=new ArrayList<String>();
		int year=Calendar.getInstance().get(Calendar.YEAR)+1;
		for(int i=year;i>year-15;i--){
			yearList.add(String.valueOf(i-1)+"-"+String.valueOf(i));
		}
		return yearList;
	}
	public ArrayList<String> getConnectionSize(){
		ArrayList<String> sizeList=new ArrayList<String>();
		sizeList.add(new String(".75"));
		sizeList.add(new String("1"));
		sizeList.add(new String("2"));
		sizeList.add(new String("3"));
		sizeList.add(new String("4"));
		sizeList.add(new String("6"));
		sizeList.add(new String("8"));
		return sizeList;
	}
	public void loadAndSetEnvironmentConfig(ServletConfig config){
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
		    Context ctx = new InitialContext();
		    ctx = (Context) ctx.lookup("java:comp/env");
		    String env = (String) ctx.lookup("environment");		    
		    String configFileLocation=config.getServletContext().getRealPath(Utils.EMPTY_STRING)+File.separator+"WEB-INF"+File.separator+"classes"+File.separator;
		    input = new FileInputStream(configFileLocation+env+".config.properties");
		    prop.load(input);
		    if(env.equalsIgnoreCase("dev")){
				config.getServletContext().setAttribute("PHOTO_DIR",prop.getProperty("photo.dir"));
			}
		}
		catch (NamingException e) {
		    logger.error("Naming Exception",e);
		}
		catch (IOException ex) {
			logger.error("IO Exception",ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

	}
}