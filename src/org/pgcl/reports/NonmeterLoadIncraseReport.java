package org.pgcl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.CustomerCategoryDTO;
import org.pgcl.dto.DepositDTO;
import org.pgcl.dto.NonMeterReportDTO;
import org.pgcl.enums.Area;
import org.pgcl.enums.Month;
import org.pgcl.models.CustomerService;
import org.pgcl.models.DepositService;
import org.pgcl.reports.ReportFormat;
import org.pgcl.reports.ReportUtil;
import org.pgcl.utils.Utils;
import org.pgcl.utils.connection.ConnectionManager;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class NonmeterLoadIncraseReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<NonMeterReportDTO> loadIncreaseReport=new ArrayList<NonMeterReportDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for;
	    private  String report_for2;
	    private  String from_date; 
	    private  String to_date; 
	    private  String customer_type;
	    private  String category_name;
	    DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		
		
		
		String fileName="LoadInformation.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,60,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			NonMeterReportDTO loadIncraseDTO = new NonMeterReportDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				50,100,50
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("PASHCHIMANCHAL GAS COMPANY LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A Company of Petrobangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("Revenue Section :",ReportUtil.f8B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f8B);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			if(customer_type.equals("01"))
			{
				getMeterCustomerLoadInfo(document);
			}else
			{
				getNonMeterCustomerLoadInfo(document);
			}
		
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	private void getNonMeterCustomerLoadInfo(Document document) throws DocumentException
	{
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,80,30});
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		if(report_for2.equals("date_wise"))
		{
			headLine="BURNER CHANGE INFORMATION FROM DATE"+from_date+" TO DATE"+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="BURNER CHANGE INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="BURNER CHANGE INFORMATION FOR YEAR OF "+bill_year;
		}
		
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
	
		loadIncreaseReport =getNonMeterLoadIncraseInformation();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_amount=0;

		int expireListSize=loadIncreaseReport.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = loadIncreaseReport.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
												
					pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
					
				
					totalRecordsPerCategory=0;
					total_burner=0;
					total_amount=0;
				}
				
			}
			ptable = new PdfPTable(8);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,75,75,25,25,30,30,40});
			ptable.setSpacingBefore(10);
			
			if(i==0)// only for very beginng of the table for printing Area name
			{
				pcell=new PdfPCell(new Paragraph("Area/Region Name:"+String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
			}
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(6);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Address",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Burner Qty. ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Effective Date",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comments",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getFull_name(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getAddress(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			total_burner=total_burner+loadIncraseDTO.getBurner_qnt();
			pcell = new PdfPCell(new Paragraph(String.valueOf(loadIncraseDTO.getBurner_qnt()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			total_amount=total_amount+loadIncraseDTO.getPertial_bill();
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPertial_bill()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getComments(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									
		pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
		
		
		
		
		
	}
	
	private void getMeterCustomerLoadInfo(Document document) throws DocumentException
	{
		/////////////////////////////////////////////////////
		
		//////////////////Meter Customer//////////////////////
		
		////////////////////////////////////////////////////
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,80,30});
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		if(report_for2.equals("date_wise"))
		{
			headLine="LOAD CHANGE INFORMATION FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="LOAD CHANGE INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="LOAD CHANGE INFORMATION FOR YEAR OF "+bill_year;
		}
		
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
	
		loadIncreaseReport =getMeterLoadIncraseInformation();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;

		int expireListSize=loadIncreaseReport.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = loadIncreaseReport.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
												

					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(9);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				
					totalRecordsPerCategory=0;
					total_burner=0;
				}
				
			}
			ptable = new PdfPTable(11);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,80,25,25,30,30,30,30,30,30,30});
			ptable.setSpacingBefore(10);
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(9);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Effective Date",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Previous Load",ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Present Load",ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comments",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Contructual",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Contructual",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Previous",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Present",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getName_address(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getOld_min_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getOld_max_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getNew_min_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getNew_max_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPrevious_security()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPresent_security()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
	
								
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getComments(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									

		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(9);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
	
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
	
	}
	
	private ArrayList<NonMeterReportDTO> getMeterLoadIncraseInformation()
	{
	ArrayList<NonMeterReportDTO> loadIncraseInfo=new ArrayList<NonMeterReportDTO>();
	
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(lpc.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(lpc.customer_id,1,2)="+area+" And substr(lpc.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'mm')="+bill_month+" and to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}
			
			
		
			
			
			String defaulterSql="select lpc.CUSTOMER_ID,mci.FULL_NAME,mci.CATEGORY_ID,mci.CATEGORY_NAME,METER_ID,NVL(OLD_MIN_LOAD,0) OLD_MIN_LOAD ,NVL(OLD_MAX_LOAD,0) OLD_MAX_LOAD,NVL(NEW_MIN_LOAD,0) NEW_MIN_LOAD,NVL(NEW_MAX_LOAD,0) NEW_MAX_LOAD, " +
								"to_char(EFFECTIVE_DATE,'dd-MM-YYYY') EFFECTIVE_DATE,get_sec_deposit_Load_Change( lpc.CUSTOMER_ID,NVL(OLD_MAX_LOAD,0),to_char(EFFECTIVE_DATE,'dd-MM-YYYY')) previous_security, " +
								"get_sec_deposit_Load_Change( lpc.CUSTOMER_ID,NVL(NEW_MAX_LOAD,0),to_char(EFFECTIVE_DATE,'dd-MM-YYYY')) present_security,REMARKS  " +
								"from LOAD_PRESSURE_CHANGE lpc,MVIEW_CUSTOMER_INFO mci " +
								"where lpc.customer_id=mci.customer_id " +
								"AND CHANGE_TYPE in(0,2) "+wClause+w2Clause ;



			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO loadIncraseDTO=new NonMeterReportDTO();
        		loadIncraseDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		loadIncraseDTO.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		loadIncraseDTO.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		loadIncraseDTO.setName_address(resultSet.getString("FULL_NAME"));
        		loadIncraseDTO.setOld_min_load(resultSet.getFloat("OLD_MIN_LOAD"));
        		loadIncraseDTO.setOld_max_load(resultSet.getFloat("OLD_MAX_LOAD"));
        		loadIncraseDTO.setNew_min_load(resultSet.getFloat("NEW_MIN_LOAD"));
        		loadIncraseDTO.setNew_max_load(resultSet.getFloat("NEW_MAX_LOAD"));
        		loadIncraseDTO.setEffective_date(resultSet.getString("EFFECTIVE_DATE"));
        		loadIncraseDTO.setPrevious_security(resultSet.getFloat("previous_security"));
        		loadIncraseDTO.setPresent_security(resultSet.getFloat("present_security"));
        		loadIncraseDTO.setComments(resultSet.getString("REMARKS"));
        		
        		
   
        		
        		loadIncraseInfo.add(loadIncraseDTO);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadIncraseInfo;
	}
	
	private ArrayList<NonMeterReportDTO> getNonMeterLoadIncraseInformation()
	{
	ArrayList<NonMeterReportDTO> loadIncraseInfo=new ArrayList<NonMeterReportDTO>();
	//PreparedStatement ps1=null;
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(bqc.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(bqc.customer_id,1,2)="+area+" And substr(bqc.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'mm')="+bill_month+" and to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}
			
			
		
			
			
			String defaulterSql="select bqc.CUSTOMER_ID,MCC.CATEGORY_TYPE,MCC.CATEGORY_ID,MCC.CATEGORY_NAME,cpi.Full_name,ca.ADDRESS_LINE1 ADDRESS,bqc.NEW_INCREASED_QNT,to_char(bqc.EFFECTIVE_DATE,'dd-MM-YYYY') EFFECTIVE_DATE,round(calculate_partial_bill(bqc.CUSTOMER_ID,bqc.NEW_INCREASED_QNT,"+
								"'L',to_char(bqc.EFFECTIVE_DATE,'dd-MM-YYYY')),0) Partial_bill from burner_qnt_change bqc,customer_personal_info cpi,CUSTOMER_ADDRESS ca,MST_CUSTOMER_CATEGORY mcc"+
								" where "+
								" bqc.customer_id=cpi.customer_id"+
								" and "+
								" bqc.customer_id=ca.customer_id"+
								" and"+
								" substr(bqc.customer_id,3,2)=MCC.CATEGORY_ID"+w2Clause+
								
								" and NEW_INCREASED_QNT>0 "+wClause+
								" order by MCC.CATEGORY_ID,EFFECTIVE_DATE,bqc.CUSTOMER_ID";


			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO loadIncraseDTO=new NonMeterReportDTO();
        		loadIncraseDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		loadIncraseDTO.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		loadIncraseDTO.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		//loadIncraseDTO.setName_address(resultSet.getString("NAME_ADDRESS"));
        		loadIncraseDTO.setFull_name(resultSet.getString("Full_name"));
        		loadIncraseDTO.setAddress(resultSet.getString("ADDRESS"));
        		loadIncraseDTO.setBurner_qnt(resultSet.getInt("NEW_INCREASED_QNT"));
        		loadIncraseDTO.setPertial_bill(resultSet.getFloat("Partial_bill"));
        		loadIncraseDTO.setEffective_date(resultSet.getString("EFFECTIVE_DATE"));
        		loadIncraseDTO.setComments("");
        		
        		
   
        		
        		loadIncraseInfo.add(loadIncraseDTO);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadIncraseInfo;
	}
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}



	
	public void setCustomerCategoryList(ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getCustomer_category() {
		return customer_category;
	}


	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
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


	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getCategory_name() {
		return category_name;
	}


	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}


	public String getReport_for2() {
		return report_for2;
	}


	public void setReport_for2(String report_for2) {
		this.report_for2 = report_for2;
	}


	public String getFrom_date() {
		return from_date;
	}


	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}


	public String getTo_date() {
		return to_date;
	}


	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}


	public String getCustomer_type() {
		return customer_type;
	}


	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	
	


	
  }


