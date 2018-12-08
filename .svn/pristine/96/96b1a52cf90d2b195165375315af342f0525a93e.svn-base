package org.pgcl.actions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.pgcl.dto.ClearnessDTO;
import org.pgcl.dto.CollectionDTO;
import org.pgcl.dto.CustomerDTO;
import org.pgcl.enums.Month;
import org.pgcl.models.BillingService;
import org.pgcl.models.CustomerService;
import org.pgcl.reports.DefaulterCertificate;


public class WebSite extends BaseAction{
	
	private static final long serialVersionUID = 1793004616091761918L;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.0");
	
	public String getCustomerBillInfo()
	{

		CustomerService customerService=new CustomerService();
		CustomerDTO customer=customerService.getCustomerInfo(customer_id);
		
		BillingService billingService=new BillingService();
		DefaulterCertificate defaulterCertificate=new DefaulterCertificate();
		
		String responseHtml="<style type=text/css>"+
		"#rounded-corner"+
		"{"+
		"font-family: 'Lucida Sans Unicode', 'Lucida Grande', Sans-Serif;"+
		"font-size: 12px;"+
		"margin: 45px;"+
		"text-align: left;"+
		"border-collapse: collapse;"+
		"}"+
		"#rounded-corner thead th.rounded-company"+
		"{"+
		"background: #b9c9fe url(\'table-images/left.png\') left -1px no-repeat;"+
		"}"+
		"#rounded-corner thead th.rounded-q4"+
		"{"+
		"background: #b9c9fe url(\'table-images/right.png\') right -1px no-repeat;"+
		"}"+
		"#rounded-corner th"+
		"{"+
		"padding: 8px;"+
		"font-weight: normal;"+
		"font-size: 13px;"+
		"color: #039;"+
		"background: #b9c9fe;"+
		"}"+
		"#rounded-corner td"+
		"{"+
		"padding: 8px;"+
		"background: #e8edff;"+
		"border-top: 1px solid #fff;"+
		"color: #669;"+
		"}"+
		"#rounded-corner tfoot td.rounded-foot-left"+
		"{"+
		"background: #e8edff url(\'table-images/botleft.png\') left bottom no-repeat;"+
		"}"+
		"#rounded-corner tfoot td.rounded-foot-right"+
		"{"+
		"background: #e8edff url(\'table-images/botright.png\') right bottom no-repeat;"+
		"}"+
		"#rounded-corner tbody tr:hover td"+
		"{"+
		"background: #d0dafd;"+
		"}"+
		"#newspaper-a"+
		"{"+
		"font-family: 'Lucida Sans Unicode', 'Lucida Grande', Sans-Serif;"+
		"font-size: 12px;"+
		"margin: 45px;"+
		"text-align: left;"+
		"border-collapse: collapse;"+
		"border: 1px solid #69c;"+
		"}"+
		"#newspaper-a th"+
		"{"+
		"padding: 12px 17px 12px 17px;"+
		"font-weight: normal;"+
		"font-size: 14px;"+
		"color: #039;"+
		"border-bottom: 1px dashed #69c;"+
		"}"+
		"#newspaper-a td"+
		"{"+
		"padding: 7px 17px 7px 17px;"+
		"color: #669;"+
		"}"+
		"#newspaper-a tbody tr:hover td"+
		"{"+
		"color: #339;"+
		"background: #d0dafd;"+
		"}"+
		"</style>";
		if(customer==null ){
			responseHtml+="This is not a valid customer id.";
		}
		else{
//			CollectionDTO billInfo=billingService.getBillInfo(bill_month,bill_year,customer_id);
			ArrayList<CollectionDTO> paidBill=new ArrayList<CollectionDTO>();
			ArrayList<ClearnessDTO> dueInfo= new ArrayList<ClearnessDTO>();
			paidBill=billingService.getPaidBillInfo(customer_id);
			dueInfo=defaulterCertificate.getDueMonthWeb(customer_id, "");
			int dueSize=dueInfo.size();
//			if(billInfo==null){
//				responseHtml+="No bill found for the given Month-Year.";
//			}
//			else{
							
				responseHtml +="<script type='text/javascript'> " +
						" function showHideDivs(divId){ " +
						"  document.getElementById('div__1').style.display='none'; " +
						"  document.getElementById('div__2').style.display='none'; " +
						" document.getElementById('div__3').style.display='none'; " +
						"  document.getElementById('div__'+divId).style.display='block';} " +
						" </script>";
				
				
				
				
			
	    responseHtml+="<input type='radio' name='selectionType' value='gbStatus' checked='checked' onclick='showHideDivs(1)' />Customer Information " +
	    		"<input type='radio' name='selectionType' value='gbInfo' onclick='showHideDivs(2)' /> Gas Bill Information " +
	    		"<input type='radio' name='selectionType' value='pPatra' onclick='showHideDivs(3)' /> Prottayan Patra ";
			
		responseHtml+="<div id='div__1'><table width='700px' border='0' id='rounded-corner'>"+
						"<tbody>"+
						   
								"<tr>"+
								   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Customer Information</td>"+
								"</tr>"+	
								"<tr>"+
								   "<td width='300px;' align=right colspan=3>Customer Code : </td>"+
								   "<td width='400px;' align=left colspan=4>"+customer.getCustomer_id()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Customer Name : </td>"+
								   "<td colspan=4>"+customer.getPersonalInfo().getFull_name()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Address : </td>"+
								   "<td colspan=4>"+customer.getAddress()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Telephone Number : </td>"+
								   "<td colspan=4>"+customer.getPersonalInfo().getPhone()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Category : </td>"+
								   "<td colspan=4>"+customer.getCustomer_category_name().substring(0,customer.getCustomer_category_name().length()-6)+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Customer Type : </td>"+
								   "<td colspan=4>"+customer.getCustomer_category_name().substring(customer.getCustomer_category_name().length()-6)+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Connection Date : </td>"+
								   "<td colspan=4>"+customer.getConnectionInfo().getConnection_date()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Minimum Monthy Load : </td>"+
								   "<td colspan=4 style='color:green;'>"+customer.getConnectionInfo().getMin_load()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Maximum Monthy Load : </td>"+
								   "<td colspan=4 style='color:green;'>"+customer.getConnectionInfo().getMax_load()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=3>Metered Status : </td>"+
								   "<td colspan=4>"+customer.getConnectionInfo().getIsMetered_name()+"</td>"+
								"</tr>";
								   if(!customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){
								responseHtml+="<tr>"+
								   "<td align=right colspan=3>Number of Burners : </td>"+
								   "<td colspan=4>"+customer.getConnectionInfo().getDouble_burner_qnt()+"</td>"+
								"</tr>";
								}
								responseHtml+="<tr>"+
								   "<td align=right colspan=3>Regional Office : </td>"+
								   "<td colspan=4>"+customer.getArea_name()+"</td>"+
								"</tr>"+
								   "</tbody></table></div>";
								
		responseHtml+="<div id='div__2' style='display:none;'><table width='700px' border='0' id='rounded-corner'>"+
				"<tbody>"+								   
								"<tr>"+
								   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Gas Bill Status </td>"+
								"</tr>"+								
								"<tr>"+
								   "<td align=right colspan=7></td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=7 align=left colspan=7 style='color:black;font-weight:bold;'>Due Month's Gas Bill</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center colspan=2 style='color:green;font-weight:bold;'><b>Billing Month</b></td>"+
								   "<td align=center colspan=3 style='color:green;font-weight:bold;'><b>Due Amount</b></td>"+
								   "<td align=center colspan=2 style='color:green;font-weight:bold;'></td>"+
								"</tr> ";
									double totalDueAmount=0.0;
								for (int i = 0; i < dueSize; i++) {
									
									String bill_amount=taka_format.format(dueInfo.get(i).getDueAmount());
									
									responseHtml+="<tr> "+
											"<td align=center colspan=2 style='color:blue;font-weight:bold;'>"+dueInfo.get(i).getDueMonth()+", "+dueInfo.get(i).getBillYear()+"</td>"+
											   "<td align=center colspan=3 style='color:blue;font-weight:bold;'>"+bill_amount+"</td>"+
											   "<td align=right colspan=2 style='color:blue;font-weight:bold;'><button class=btn btn-success type=button onclick=window.location='https://rsm.pgcl.org.bd:8443/PGCL_WEB/downloadMeteredBill.action?customer_id="+customer_id+"&bill_month="+dueInfo.get(i).getBillMonth()+"&bill_year="+dueInfo.get(i).getBillYear()+"&download_type=O'>Download Bill</button></td>"+
											"</tr>";
									totalDueAmount=totalDueAmount+dueInfo.get(i).getDueAmount();
								}
										
								responseHtml+="<tr>"+
								
										
								   "<td align=right colspan=2 style='color:blue;font-weight:bold;'>Total = </td>"+
								   "<td align=center colspan=3 style='color:blue;font-weight:bold;'>"+taka_format.format(totalDueAmount)+"</td>"+
								   "<td align=center colspan=2 style='color:green;font-weight:bold;'></td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=7></td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center colspan=7 style='color:red;'>N.B Gas bill payment posted up to "+Month.values()[Integer.valueOf(paidBill.get(0).getBill_month())-1]+" "+paidBill.get(0).getBill_year()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=right colspan=7></td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=7 style='color:black;font-weight:bold;'>Last three month's payment details</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center rowspan=2>Billing Month</td>"+
								   "<td align=center colspan=3>Paid Amount</td>"+
								   "<td align=center rowspan=2 colspan=2>Bank</td>"+
								   "<td align=center rowspan=2>Date of Payment</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center>Bill</td>"+
								   "<td align=center>Surcharge</td>"+
								   "<td align=center>Total</td>"+
								"</tr>"+								
								"<tr>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+Month.values()[Integer.valueOf(paidBill.get(0).getBill_month())-1]+" "+paidBill.get(0).getBill_year()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(0).getCollected_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(0).getSurcharge_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(0).getCollected_amount()+paidBill.get(0).getSurcharge_amount())+"</td>"+
								   "<td align=center colspan=2 style='color:blue;'>"+paidBill.get(0).getBank_id()+", "+paidBill.get(0).getBranch_id()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+paidBill.get(0).getCollection_date()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+Month.values()[Integer.valueOf(paidBill.get(1).getBill_month())-1]+" "+paidBill.get(1).getBill_year()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(1).getCollected_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(1).getSurcharge_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(1).getCollected_amount()+paidBill.get(1).getSurcharge_amount())+"</td>"+
								   "<td align=center colspan=2 style='color:blue;'>"+paidBill.get(1).getBank_id()+", "+paidBill.get(1).getBranch_id()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+paidBill.get(1).getCollection_date()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+Month.values()[Integer.valueOf(paidBill.get(2).getBill_month())-1]+" "+paidBill.get(2).getBill_year()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(2).getCollected_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(2).getSurcharge_amount())+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(2).getCollected_amount()+paidBill.get(2).getSurcharge_amount())+"</td>"+
								   "<td align=center colspan=2 style='color:blue;'>"+paidBill.get(2).getBank_id()+", "+paidBill.get(2).getBranch_id()+"</td>"+
								   "<td align=center style='color:blue;font-weight:bold;'>"+paidBill.get(2).getCollection_date()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center colspan=7 style='color:green;height=20px;'>If any anomaly is found, Please contact to your own Regional Office</td>"+
								"</tr>"+
								
								"</tbody></table></div>";
//			}
			
			
			responseHtml+="<div id='div__3'  style='display:none;'><table width='700px' border='0' id='rounded-corner'>"+
					"<tbody>"+
					"<tr>"+
					   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Prottayan Patra Download</td>"+
					"</tr>"+	
					   
										"<tr> "+
										"<td style='color:blue;font-weight:bold;'><button class=btn btn-success type=button onclick=window.location='https://rsm.pgcl.org.bd:8443/PGCL_WEB/clearnessCertificateInfo.action?customer_id="+customer_id+"&download_type=individual_wise&print_type=01'>Download</button></td>"+
								        "</tr>"+
								    "</tbody></table></div>";
		}
		
		setJsonResponse("jsonPresponse({\"response\":\""+responseHtml+"\"})");
		//setJsonResponse(responseHtml);
		return null;
	}


	public String getCustomer_id() {
		return customer_id;	
	}


	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}


	public String getBill_month() {
		return bill_month;
	}


	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}


	public String getBill_year() {
		return bill_year;
	}


	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}


	

}
