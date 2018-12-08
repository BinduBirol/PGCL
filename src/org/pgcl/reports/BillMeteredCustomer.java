package org.pgcl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.pgcl.actions.BaseAction;
import org.pgcl.dto.MBillDTO;
import org.pgcl.dto.MeterReadingDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.models.BillingService;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class BillMeteredCustomer extends BaseAction implements ServletContextAware{

	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String download_type;
	private String bill_for;
	public static int textDiff=597;
	private boolean water_mark=false;
	
	public ServletContext getServlet() {
		return servlet;
	}
	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}
	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	
	public String downloadBill() throws Exception
	{	
		int fSize=11;
		String file_customer_id="";
		String file_bill_month="";
		String file_bill_year="";
		int counter=0;
		
		UserDTO loggedInUser=(UserDTO)session.get("user");
		if((area_id==null || area_id.equalsIgnoreCase("")) && loggedInUser!=null)
			area_id=loggedInUser.getArea_id();
		
		BillingService bs=new BillingService();
		ArrayList<MBillDTO> billList=bs.getBill(this.bill_id,this.customer_category,this.area_id,this.customer_id,this.bill_month,this.bill_year,this.download_type);
		HttpServletResponse response = ServletActionContext.getResponse();
		PdfReader reader =null;
		ByteArrayOutputStream certificate = null;
		
		//ByteArrayOutputStream out1=new ByteArrayOutputStream(pass1.size);
		
		List<PdfReader> readers = new ArrayList<PdfReader>();
		String printType="Single";
		//String realPath=servlet.getRealPath("/resources/staticPdf/Bill_Meter.pdf");   //For double page billing print
		String realPath=servlet.getRealPath("/resources/staticPdf/Final_Bill_Format.pdf");  //For single page billing print
		
		reader = new PdfReader(new FileInputStream(realPath));
		//certificate = new ByteArrayOutputStream();
		PdfStamper stamp =null;//= new PdfStamper(reader,certificate);
		
		
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//Rectangle one = new Rectangle(1202.40f,828.00f); // For double page
		Rectangle one = new Rectangle(601.40f,830.00f); // For Single Page
		document.setPageSize(one);
		//document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 25, 2);
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
		DecimalFormat reading_format = new DecimalFormat("##########0");
		
		for (int j = 0; j < 2; j++) {
			
		
		try
		{
			if(billList.size()>0)
			{
				if(billList.get(0).getBill_status()==null ||billList.get(0).getBill_status().getId()==0 )
					water_mark=true;
			}
			
			for(MBillDTO bill : billList){	
				//System.out.println(bill.getCustomer_name());
				
				if(counter==0){
					file_customer_id=bill.getCustomer_id();
					file_bill_month=bill.getBill_month()+"";
					file_bill_year=bill.getBill_year()+"";
				}
			counter++;
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);		     
			BaseFont bfBold = BaseFont.createFont(BaseFont.TIMES_BOLD,BaseFont.WINANSI,BaseFont.EMBEDDED);
			
			reader = new PdfReader(new FileInputStream(realPath));
			certificate = new ByteArrayOutputStream();
			stamp = new PdfStamper(reader,certificate);
			over = stamp.getOverContent(1);
					
			over.beginText();
			
			if(bill.getCustomer_category_name().equalsIgnoreCase("Captive (PVT.)")){
				over.setFontAndSize(bfBold, 11);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,"CAPTIVE POWER (PVT)",250, 725,0);
			}else{
			over.setFontAndSize(bfBold, 11);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,bill.getCustomer_category_name().toUpperCase(),258, 725,0);
			}
			
			over.setFontAndSize(bf, fSize);
			over.setTextMatrix(347, 756);
			over.showText(bill.getArea_name());	
			if(j==0){
				over.setFontAndSize(bfBold, 11);
				over.setTextMatrix(489, 760);
				over.showText("Customer Copy");
			}else{
				over.setFontAndSize(bfBold, 11);
				over.setTextMatrix(490, 760);
				over.showText("Office Copy");	
			}
			
			over.setFontAndSize(bf, fSize);
			over.setTextMatrix(495, 697);
			over.showText(bill.getInvoice_no());			
			over.setTextMatrix(495, 676);
			over.showText(bill.getIssue_date());			
			over.setTextMatrix(495, 655);
			over.setFontAndSize(bfBold, 11);
			over.showText(bill.getLast_pay_date_wo_sc());
			over.setTextMatrix(495, 635);
			over.showText(bill.getLast_pay_date_w_sc());
			over.setFontAndSize(bf, fSize);
			over.setTextMatrix(495, 615);
			over.showText(bill.getConnectionDate());
			over.setTextMatrix(495, 597);
			over.showText(bill.getLast_disconn_reconn_date()==null?"":bill.getLast_disconn_reconn_date());
			//over.showText("recon/discon");
			//over.setTextMatrix(285, 393);
			//over.showText(bill.getMonthly_contractual_load()+"");
			
//			over.setFontAndSize(bf, fSize);
//			over.setTextMatrix(508, 747);
//			over.showText(bill.getInvoice_no());
			
			over.setTextMatrix(126, 697);
			over.showText(bill.getBill_month_name().toUpperCase()+", "+bill.getBill_year());
			over.setTextMatrix(126, 676);
			over.showText(bill.getCustomer_id());
			//over.setTextMatrix(126, 680);
			//over.showText(bill.getProprietor_name()==null?"":bill.getProprietor_name());	
			over.setTextMatrix(126, 556);
			over.showText(bill.getPhone()==null?"":bill.getPhone());	
			
			if(printType.equals("double")){
				over.setTextMatrix(508+textDiff, 747);
				over.showText(bill.getInvoice_no());
				over.setTextMatrix(508+textDiff, 728);
				over.showText(bill.getIssue_date());
				over.setTextMatrix(508+textDiff, 710);
				over.showText(bill.getLast_pay_date_wo_sc());
				over.setTextMatrix(508+textDiff, 690);
				over.showText(bill.getLast_pay_date_w_sc());
				over.setTextMatrix(508+textDiff, 690);
				over.showText(bill.getLast_disconn_reconn_date()==null?"":bill.getLast_disconn_reconn_date());
				over.setTextMatrix(508+textDiff, 650);
				over.showText(bill.getMonthly_contractual_load()+"");
				over.setTextMatrix(110+textDiff, 745);
				over.showText(bill.getBill_month_name()+", "+bill.getBill_year());
				over.setTextMatrix(110+textDiff, 725);
				over.showText(bill.getCustomer_id());
				over.setTextMatrix(110+textDiff, 707);
				over.showText(bill.getCustomer_name());
				over.setTextMatrix(110+textDiff, 690);
				over.showText(bill.getProprietor_name()==null?"":bill.getProprietor_name());

			}

			double minimumLoad=0.0;

			ArrayList<MeterReadingDTO> readingList=bill.getReadingList();
			for(int i=0;i<readingList.size();i++)
			{
			
				minimumLoad=minimumLoad+readingList.get(i).getMin_load(); 
			
				
				over.setFontAndSize(bf, 8);
				over.setTextMatrix(46,492-15*i); over.showText(readingList.get(i).getPrevious_reading_date()+"");
				over.setTextMatrix(105,492-15*i); over.showText(readingList.get(i).getCurrent_reading_date()+"");
				over.setTextMatrix(161,492-15*i); over.showText(readingList.get(i).getMeter_sl_no());
				
				over.setFontAndSize(bf, fSize);
				over.setTextMatrix(222,492-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?readingList.get(i).getReading_purpose_name()+"  "+readingList.get(i).getActual_consumption():reading_format.format(readingList.get(i).getCurr_reading())+"");
				over.setTextMatrix(278,492-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?"":reading_format.format(readingList.get(i).getPrev_reading())+"");
				
				over.setTextMatrix(340,492-15*i); over.showText((readingList.get(i).getReading_purpose_str().equals("3")||readingList.get(i).getReading_purpose_str().equals("8")||readingList.get(i).getReading_purpose_str().equals("6"))?"":reading_format.format(readingList.get(i).getDifference())+"");
				
				//over.setTextMatrix(330,492-15*i); over.showText(readingList.get(i).getTemperature()+"");
				
				over.setTextMatrix(424,492-15*i); over.showText(factor_format.format(readingList.get(i).getPressure_factor())+"");
				over.setTextMatrix(480,492-15*i); over.showText("1.0");
				over.setTextMatrix(525,492-15*i); over.showText(readingList.get(i).getHhv_nhv()+"");
				
				//over.setTextMatrix(540,492-15*i); over.showText(readingList.get(i).getRate()+"");
				
				//over.setTextMatrix(285,492-15*i); over.showText(readingList.get(i).getPressure()+"");
				
				if(printType.equals("double")){
				over.setTextMatrix(15+textDiff,560+15*i); over.showText(readingList.get(i).getMeter_sl_no());				
				over.setTextMatrix(90+textDiff,560+15*i); over.showText(readingList.get(i).getCurr_reading()+"");
				over.setTextMatrix(140+textDiff,560+15*i); over.showText(readingList.get(i).getPrev_reading()+"");
				
				over.setTextMatrix(200+textDiff,560+15*i); over.showText(readingList.get(i).getDifference()+"");
				over.setTextMatrix(260+textDiff,560+15*i); over.showText(readingList.get(i).getPressure()+"");
				over.setTextMatrix(310+textDiff,560+15*i); over.showText(readingList.get(i).getTemperature()+"");
				
				over.setTextMatrix(350+textDiff,560+15*i); over.showText(factor_format.format(readingList.get(i).getPressure_factor())+"");
				over.setTextMatrix(410+textDiff,560+15*i); over.showText(readingList.get(i).getTemperature_factor()+"");
				over.setTextMatrix(470+textDiff,560+15*i); over.showText(readingList.get(i).getHhv_nhv()+"");
				over.setTextMatrix(530+textDiff,560+15*i); over.showText(readingList.get(i).getRate()+"");
				}
				
			}
			if(readingList.size()>1){
				if(minimumLoad>bill.getActual_minimum_load()){
					minimumLoad=bill.getActual_minimum_load();
				}
			}
			
		
			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(readingList.get(0).getPressure()),300, 436,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(readingList.get(0).getRate()),300, 416,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getMonthly_contractual_load()),300, 395,0);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getMinimum_load()),300, 377,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(minimumLoad),300, 377,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getActual_gas_consumption()),300, 337,0);				
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getOther_consumption()),300, 317,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getMixed_consumption()),300, 298,0);
			over.setFontAndSize(bfBold, 11);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getBilled_consumption()),300, 280,0);
			over.setFontAndSize(bf, fSize);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(bill.getHhv_nhv_adj_qnt()),300, 257,0);
			
			double totalBillQty=bill.getBilled_consumption()+bill.getHhv_nhv_adj_qnt();
			over.setFontAndSize(bfBold, 11);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,consumption_format.format(totalBillQty),300, 237,0);

			over.setFontAndSize(bf, fSize);
			//Govt. Margin
			
			double gasBill=bill.getPbMarginDTO().getGas_bill()+bill.getGovtMarginDTO().getVat_amount()+bill.getGovtMarginDTO().getSd_amount();
			
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getVat_amount()),285, 282,0);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getSd_amount()),285, 258,0);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getOthers_amount()),285, 238,0);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getGovtMarginDTO().getTotal_amount()),285, 215,0);
			
			//Petro Bangla Margin			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(gasBill),562, 416,0);				
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMin_load_bill()),562, 395,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getMeter_rent()),562, 376,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getHhv_nhv_bill()),562, 357,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getAdjustment()),562, 337,0);
			over.setFontAndSize(bfBold, 7);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,bill.getPbMarginDTO().getAdjustment_comments()==null?"":"("+bill.getPbMarginDTO().getAdjustment_comments()+")",562, 329,0);
			over.setFontAndSize(bf, fSize);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getSurcharge_percentage()),445, 350,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getSurcharge_amount()),562, 256,0);
			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"12",445, 256,0);
			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getOthers()),562, 318,0);
			over.setFontAndSize(bfBold, 7);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,bill.getPbMarginDTO().getOther_comments()==null?"":"("+bill.getPbMarginDTO().getOther_comments().substring(0,bill.getPbMarginDTO().getOther_comments().length()-1)+")",562, 311,0);
			over.setFontAndSize(bf, fSize);
			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getVat_rebate_percent()),447, 279,0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPbMarginDTO().getVat_rebate_amount()),562, 279,0);
			
			double totalBill=gasBill+bill.getPbMarginDTO().getMin_load_bill()+bill.getPbMarginDTO().getHhv_nhv_bill()+bill.getPbMarginDTO().getMeter_rent()+bill.getPbMarginDTO().getAdjustment()+bill.getPbMarginDTO().getOthers();
			over.setFontAndSize(bfBold, 11);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(totalBill),562, 299,0);
			
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,taka_format.format(bill.getPayable_amount()),562, 238,0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Taka "+bill.getAmount_in_word(),94, 208,0);
			
			/*
			over.showText(bill.getPbMarginDTO().getOthers()+"");					
			over.setTextMatrix(432, 314);
			over.showText(bill.getPbMarginDTO().getVat_rebate_percent()+"");			
			over.setTextMatrix(525, 314);
			over.showText(bill.getPbMarginDTO().getVat_rebate_amount()+"");			
			over.setTextMatrix(525, 295);
			over.showText(bill.getPbMarginDTO().getTotal_amount()+"");			
			over.setTextMatrix(525, 238);
			//over.showText(bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getTotal_amount()+"");
			over.showText(bill.()+"");						
			//Total Amount In Word
			over.setTextMatrix(86, 201);
			over.showText(bill.getAmount_in_word()+"");
			*/
			if(printType.equals("double")){
				over.setTextMatrix(240+textDiff, 487);
				over.showText(bill.getMinimum_load()+"");
				over.setTextMatrix(240+textDiff, 468);
				over.showText(bill.getActual_gas_consumption()+"");
				over.setTextMatrix(240+textDiff, 450);
				over.showText(bill.getOther_consumption()+"");
				over.setTextMatrix(240+textDiff, 430);
				over.showText(bill.getMixed_consumption()+"");
				over.setTextMatrix(240+textDiff, 413);
				over.showText(bill.getBilled_consumption()+"");
				over.setTextMatrix(240+textDiff, 397);
				over.showText(bill.getHhv_nhv_adj_qnt()+"");
				over.setTextMatrix(240+textDiff, 335);
				over.showText(bill.getGovtMarginDTO().getVat_amount()+"");
				over.setTextMatrix(240+textDiff, 318);
				over.showText(bill.getGovtMarginDTO().getSd_amount()+"");
				over.setTextMatrix(240+textDiff, 302);
				over.showText(bill.getGovtMarginDTO().getOthers_amount()+"");
				over.setTextMatrix(240+textDiff, 282);
				over.showText(bill.getGovtMarginDTO().getTotal_amount()+"");
				over.setTextMatrix(525+textDiff, 485);
				over.showText(bill.getPbMarginDTO().getGas_bill()+"");
				over.setTextMatrix(525+textDiff, 468);
				over.showText(bill.getPbMarginDTO().getMin_load_bill()+"");
				over.setTextMatrix(525+textDiff, 452);
				over.showText(bill.getPbMarginDTO().getMeter_rent()+"");
				over.setTextMatrix(525+textDiff, 432);
				over.showText(bill.getPbMarginDTO().getHhv_nhv_bill()+"");
				over.setTextMatrix(525+textDiff, 413);
				over.showText(bill.getPbMarginDTO().getAdjustment()+"");
				over.setTextMatrix(430+textDiff, 397);
				//over.showText(bill.getPbMarginDTO().getSurcharge_percentage()+"");
				over.setTextMatrix(525+textDiff, 397);
				over.showText(bill.getPbMarginDTO().getSurcharge_amount()+"");
				over.setTextMatrix(525+textDiff, 378);
				over.showText(bill.getPbMarginDTO().getOthers()+"");
				over.setTextMatrix(430+textDiff, 360);
				//over.showText(bill.getPbMarginDTO().getVat_rebate_percent()+"");
				over.setTextMatrix(525+textDiff, 360);
				over.showText(bill.getPbMarginDTO().getVat_rebate_amount()+"");
				over.setTextMatrix(525+textDiff, 342);
				over.showText(bill.getPbMarginDTO().getTotal_amount()+"");
				over.setTextMatrix(525+textDiff, 303);
				//over.showText(bill.getGovtMarginDTO().getTotal_amount()+bill.getPbMarginDTO().getTotal_amount()+"");
				over.showText(bill.getPayable_amount()+"");
				over.setTextMatrix(105+textDiff, 250);
				over.showText(bill.getAmount_in_word()+"");
			}
			
			
			ColumnText ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getAddress().toUpperCase()==null?"":bill.getAddress().toUpperCase(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     126, 628, 300, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getCustomer_name().toUpperCase()!=null? bill.getCustomer_name().toUpperCase():(bill.getProprietor_name().toUpperCase()==null?"":bill.getProprietor_name().toUpperCase()), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD))),
			                     126, 667, 350, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getProprietor_name().toUpperCase()==null?"":bill.getProprietor_name().toUpperCase(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     126, 646, 350, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			
			/* for double
			ColumnText ct1 = new ColumnText(over);						
			ct1.setSimpleColumn(new Phrase(new Chunk(bill.getAddress(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     860, 683, 700, 400, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct1.go(); 
			*/
			
			  
			over.setFontAndSize(bf, 15);
			over.setTextMatrix(600, 607);
			over.showText(bill.getInvoice_no());
			
			
			
			
			
			over.endText();
			
			
			
			stamp.close();
			readers.add(new PdfReader(certificate.toByteArray()));
			
			
			}
						
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
		if(readers.size()>0)
		{
			PdfWriter writer = PdfWriter.getInstance(document, out);
			if(water_mark==true)
				writer.setPageEvent(new Watermark());
			document.open();
			
			PdfContentByte cb = writer.getDirectContent();
			PdfReader pdfReader = null;
			PdfImportedPage page;
			
			for(int k=0;k<readers.size();k++)
			{
				document.newPage();
				pdfReader = readers.get(k);
				page = writer.getImportedPage(pdfReader, 1);
				cb.addTemplate(page, 0, 0);
			}
			

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			
			if(billList.size()>1)
				file_customer_id="";
			
			rptUtil.downloadPdf(out, response,"BILL"+((bill_id==null || bill_id.equalsIgnoreCase(""))?"":"-"+bill_id)+"-"+file_bill_month+"-"+file_bill_year+((file_customer_id==null || file_customer_id.equalsIgnoreCase(""))?"":"-"+file_customer_id)+".pdf");
			document=null;	
					
		}
		
		
		return null;	
		
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getDownload_type() {
		return download_type;
	}
	public void setDownload_type(String downloadType) {
		download_type = downloadType;
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
	public void setBill_month(String billMonth) {
		bill_month = billMonth;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String billYear) {
		bill_year = billYear;
	}
	

	public String getBill_for() {
		return bill_for;
	}
	public void setBill_for(String billFor) {
		bill_for = billFor;
	}


	public class Watermark extends PdfPageEventHelper {
		 
        protected Phrase watermark = new Phrase("SAMPLE BILL (NOT FOR PRINT)", FontFactory.getFont(FontFactory.HELVETICA, 45, BaseColor.LIGHT_GRAY));
 
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, textDiff+298, 421, 45);
            
        }
    }
}
