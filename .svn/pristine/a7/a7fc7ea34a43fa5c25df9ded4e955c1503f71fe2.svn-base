package org.pgcl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import oracle.jdbc.driver.OracleCallableStatement;

import org.pgcl.actions.BaseAction;
import org.pgcl.dto.CollectionReportDTO;
import org.pgcl.enums.Area;
import org.pgcl.enums.Month;

import org.pgcl.utils.Utils;
import org.pgcl.utils.connection.ConnectionManager;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class MonthlyCollectionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	
    private static String area;
    private static String customer_category;
    private static String bill_month;
    private static String bill_year;
    private String report_for; 
    private static String category_name;
	static ArrayList<CollectionReportDTO> collectionInfoList=new ArrayList<CollectionReportDTO>();

	Connection conn = ConnectionManager.getConnection();
	String sql = "";
	ArrayList<String>customerType=new ArrayList<String>();
	
	PreparedStatement ps=null;
	ResultSet rs=null;
	//String[] areaName=new String[10];
	int a=0;
	


	public ServletContext servlet;
	ServletContext servletContext = null;

	static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat("##########0.000");


	public String execute() throws Exception {




		String fileName = "CollectionStatement.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,60,72);
		
		try {

			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;
			
			document.open();
			PdfPTable headerTable = new PdfPTable(3);
		    Rectangle page = document.getPageSize();
		    headerTable.setTotalWidth(page.getWidth());
			float a=((page.getWidth()*15)/100)/2;
			float b=((page.getWidth()*30)/100)/2;
				
			headerTable.setWidths(new float[] {
				a,b,a
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{b});
			pcell=new PdfPCell(new Paragraph("PASHCHIMANCHAL GAS COMPANY LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A Company of Petrobangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
			
			Chunk chunk1 = new Chunk("Revenue Section :",font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(area)-1]),font3);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
						
			pcell=new PdfPCell(mTable);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			
			
			String bmont = "00".substring(bill_month.length()) + bill_month;

		
        	int month_year=Integer.valueOf(bill_year+bmont);
			if(report_for.equals("area_wise")&& month_year>=201605)
			{
				
				area_wise(document);
			}
		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;



		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;




	}
	
	
	private static void area_wise(Document document) throws DocumentException
	{
		
		PdfPTable headlineTable = new PdfPTable(3);
		headlineTable.setSpacingBefore(5);
		headlineTable.setSpacingAfter(10);
		headlineTable.setWidths(new float[] {
				40,70,40
			});
		PdfPCell pcell = null;
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Monthly Collection Statement For the month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
	
	
		
		Double totalOpeningBalance=0.0;
		Double totalAdjustment=0.0;
		Double totalCurrSales=0.0;
		Double totalCurrSurcharge=0.0;
		Double totalAccountReceivale=0.0;
		Double totalGasBill=0.0;
		Double totalMeterRent=0.0;
		Double totalSurcharge=0.0;
		Double totalIncomeTax=0.0;
		Double totalVatRebate=0.0;
		Double totalHhvNhv =0.0;
		Double totalSecurityAdj=0.0;
		Double totalTotalCollection =0.0;
		Double totalPreviousDue =0.0;
		Double totalAvgMonthlySales =0.0;
		Double totalAvgDue =0.0;
		
		
		
		Double subSumOpeningBalance=0.0;
		Double subSumAdjustment=0.0;
		Double subSumCurrSales=0.0;
		Double subSumCurrSurcharge=0.0;
		Double subSumAccountReceivale=0.0;
		Double subSumGasBill=0.0;
		Double subSumMeterRent=0.0;
		Double subSumSurcharge=0.0;
		Double subSumIncomeTax=0.0;
		Double subSumVatRebate=0.0;
		Double subSumHhvNhv =0.0;
		Double subSecurityAdj=0.0;
		Double subSumTotalCollection =0.0;
		Double subSumPreviousDue =0.0;
		Double subSumAvgMonthlySales =0.0;
		Double subSumAvgDue =0.0;
		
		
	
		
		PdfPTable datatable1 = new PdfPTable(18);
		
		datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {2,8,6,6,6,6,6,6,6,6,6,5,5,5,6,6,6,3});
		
		
		pcell=new PdfPCell(new Paragraph("Sl.No",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		String dateChunk="";
	    String dateChunk2="";
	  
	    int month_prev=Integer.valueOf(bill_month)-1;
	    
	    int numDays1 = Utils.getLastDayOfMonth(month_prev,Integer.valueOf(bill_year));
	    int numDays2= Utils.getLastDayOfMonth(Integer.valueOf(bill_month),Integer.valueOf(bill_year));
	    
	    if(bill_month.equals("01"))
	    {
	     dateChunk="Balance as on 31-12-"+(Integer.valueOf(bill_year)-1);
	     dateChunk2="Total Due as on 31-12-"+(Integer.valueOf(bill_year)-1);
	    }else
	    {
	     dateChunk="Balance as on "+numDays1+"-"+(Integer.valueOf(bill_month)-1)+"-"+bill_year;
	     dateChunk2="Total Due as on "+numDays2+"-"+(Integer.valueOf(bill_month))+"-"+bill_year;
	    }
		pcell=new PdfPCell(new Paragraph(dateChunk,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Adjustment",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Sales For "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("A/R as on DATE",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Collection For Month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,font3));
		pcell.setColspan(8);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph(dateChunk2,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Average monthly Sales",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Average due month",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
	
		pcell=new PdfPCell(new Paragraph("Gas bill",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Income Tax",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Vat rebate/FF Waiver",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Security Adjustment",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Total Collection",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
	    
		pcell=new PdfPCell(new Paragraph("01",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("02",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("03",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("04",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("05",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("06=10",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("07=(3+4+5+6)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("08",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("09",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("10",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("11",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("12",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("13",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("14",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("15=(8+9+10+11+12+14)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("16=(7-15)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("17",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("18=(16/17)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
		generateCollectionDataForReport();
		collectionInfoList=getCollectionInfoList();
		int expireListSize=collectionInfoList.size();
		

		pcell=new PdfPCell(new Paragraph("1",font3));
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph(Area.values()[Integer.valueOf(area)-1]+" AREA",font3));
		pcell.setColspan(17);
		datatable1.addCell(pcell);
		
		int cusCatCount=97;
		String previousType="GOVT";
		
		String currnertType="";
		try {
		
			   			
			for(int i=0;i<expireListSize;i++)
			
        	{
				CollectionReportDTO collectionDto=collectionInfoList.get(i);
				
    			if(cusCatCount==97)
    			{
    				pcell=new PdfPCell(new Paragraph(previousType.equals("PVT")?"A) PRIVATE":"A) GOVERNMENT",font3));
    				pcell.setColspan(2);
    				datatable1.addCell(pcell);
    				pcell=new PdfPCell(new Paragraph("",font3));
    				pcell.setColspan(16);
    				datatable1.addCell(pcell);
    			}
    			
    			currnertType=collectionDto.getCategory_type();
    			if(!currnertType.equals(previousType))
    			{
    			
	    			if(cusCatCount!=97){
	    				
	    				pcell=new PdfPCell(new Paragraph("Sub Total (A)",font3));
	    				pcell.setColspan(2);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    			
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSecurityAdj),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
	    				 totalAdjustment=totalAdjustment+subSumAdjustment;
	    				 totalCurrSales=totalCurrSales+subSumCurrSales;
	    				 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
	    				 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
	    				 totalGasBill=totalGasBill+subSumGasBill;
	    				 totalMeterRent=totalMeterRent+subSumMeterRent;
	    				 totalSurcharge=totalSurcharge+subSumSurcharge;
	    				 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
	    				 totalVatRebate=totalVatRebate+subSumVatRebate;
	    				 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
	    				 totalSecurityAdj=totalSecurityAdj+subSecurityAdj;
	    				 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
	    				 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
	    				 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
	    				 totalAvgDue =totalAvgDue+subSumAvgDue;
	    				
	    				
	    				 
	    				 subSumOpeningBalance=0.0;
	    				 subSumAdjustment=0.0;
	    				 subSumCurrSales=0.0;
	    				 subSumCurrSurcharge=0.0;
	    				 subSumAccountReceivale=0.0;
	    				 subSumGasBill=0.0;
	    				 subSumMeterRent=0.0;
	    				 subSumSurcharge=0.0;
	    				 subSumIncomeTax=0.0;
	    				 subSumVatRebate=0.0;
	    				 subSumHhvNhv =0.0;
	    				 subSecurityAdj=0.0;
	    				 subSumTotalCollection =0.0;
	    				 subSumPreviousDue =0.0;
	    				 subSumAvgMonthlySales =0.0;
	    				 subSumAvgDue =0.0;

	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(currnertType.equals("PVT")?"B) PRIVATE":"B) GOVERNMENT",font3));
	    				pcell.setColspan(2);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph("",font3));
	    				pcell.setColspan(16);
	    				datatable1.addCell(pcell);
	    				cusCatCount=97;
	    				previousType=currnertType;
	    			}
					
    			}
    			
    			pcell=new PdfPCell(new Paragraph(Character.toString ((char) cusCatCount)+")",font3));
    			datatable1.addCell(pcell);
    			pcell=new PdfPCell(new Paragraph(collectionDto.getCategory_name(),font3));
    			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
    			datatable1.addCell(pcell);
    				    		
    			
    			
    	        
    			
    			subSumOpeningBalance=subSumOpeningBalance+collectionDto.getOpening_balance();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getOpening_balance()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAdjustment=subSumAdjustment+collectionDto.getAdjustment();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAdjustment()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSales=subSumCurrSales+collectionDto.getCurr_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSurcharge=subSumCurrSurcharge+collectionDto.getCurr_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAccountReceivale=subSumAccountReceivale+collectionDto.getAccount_receivable();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAccount_receivable()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumGasBill=subSumGasBill+collectionDto.getGas_bill();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getGas_bill()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumMeterRent=subSumMeterRent+collectionDto.getMeter_rent();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getMeter_rent()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumSurcharge=subSumSurcharge+collectionDto.getColl_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getColl_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumIncomeTax=subSumIncomeTax+collectionDto.getIncome_tax();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getIncome_tax()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumVatRebate=subSumVatRebate+collectionDto.getVat_rebate();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getVat_rebate()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumHhvNhv=subSumHhvNhv+collectionDto.getHhv_nhv();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getHhv_nhv()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSecurityAdj=subSecurityAdj+collectionDto.getSecurityCollection();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getSecurityCollection()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumTotalCollection=subSumTotalCollection+collectionDto.getTotal_collection();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getTotal_collection()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumPreviousDue=subSumPreviousDue+collectionDto.getPrevious_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getPrevious_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgMonthlySales=subSumAvgMonthlySales+collectionDto.getAvg_monthly_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_monthly_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgDue=subSumAvgDue+collectionDto.getAvg_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    		
    			cusCatCount++;
        	}	
    		
		
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSecurityAdj),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
			 totalAdjustment=totalAdjustment+subSumAdjustment;
			 totalCurrSales=totalCurrSales+subSumCurrSales;
			 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
			 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
			 totalGasBill=totalGasBill+subSumGasBill;
			 totalMeterRent=totalMeterRent+subSumMeterRent;
			 totalSurcharge=totalSurcharge+subSumSurcharge;
			 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
			 totalVatRebate=totalVatRebate+subSumVatRebate;
			 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
			 totalSecurityAdj=totalSecurityAdj+subSecurityAdj;
			 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
			 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
			 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
			 totalAvgDue =totalAvgDue+subSumAvgDue;
			
			pcell=new PdfPCell(new Paragraph("Total Sales Of "+Area.values()[Integer.valueOf(area)-1]+"(A+B)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalSecurityAdj),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue/totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
		
			
			        
			        
			    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		
		document.add(datatable1);

		
	}
	
	

	
	
	private static void generateCollectionDataForReport()
	{
		OracleCallableStatement stmt=null;
		Connection conn = ConnectionManager.getConnection();
		try {
			int month=Integer.parseInt(bill_month);
			String billMonth="";
			if(month<10){
				billMonth="0"+Integer.toString(month);
			}else{
				billMonth=Integer.toString(month);
			}
			
			String monthyear=bill_year+billMonth;
			int yearmon=Integer.parseInt(monthyear);
			
			if(yearmon>201604){
			
			//System.out.println("Procedure Save_Multi_Month_Collection Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call Collection_Report_Helper(?,?,?) }");
			 
			
			stmt.setString(1, area);
			stmt.setInt(2,Integer.valueOf(bill_month));
			stmt.setInt(3, Integer.valueOf(bill_year));
			stmt.executeUpdate();
		
	
			}
        	
   
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
				try{
				stmt.close();
				ConnectionManager.closeConnection(conn);
				} catch (Exception e){
					e.printStackTrace();
					}
				conn = null;
				stmt=null;
				}
		
		
	}
	 
	
	private static ArrayList<CollectionReportDTO> getCollectionInfoList()
	{
	ArrayList<CollectionReportDTO> collectionInfoList=new ArrayList<CollectionReportDTO>();
	Connection conn = ConnectionManager.getConnection();
	//PreparedStatement ps1=null;
		try {
		
			
		String defaulterSql="select AREA_NAME,cr.* ,CATEGORY_TYPE from COLLECTION_RRPORT cr,MST_CUSTOMER_CATEGORY mcc,MST_AREA ma " +
					"where CR.CATEGORY_ID=mcc.CATEGORY_ID " +
					"and  cr.area_id=MA.AREA_ID " +
					"order by CR.AREA_ID,CATEGORY_TYPE,CR.CATEGORY_ID DESC " ;
//     bill_month and Bill_year conside korte hobe
			
		PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		CollectionReportDTO collectionDto=new CollectionReportDTO();
        		collectionDto.setArea_id(resultSet.getString("AREA_ID"));
        		collectionDto.setArea_name(resultSet.getString("AREA_NAME"));
        		collectionDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
        		//loadIncraseDTO.setName_address(resultSet.getString("NAME_ADDRESS"));
        		collectionDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		collectionDto.setCategory_type(resultSet.getString("CATEGORY_TYPE"));
        		collectionDto.setOpening_balance(resultSet.getDouble("BALANCE_PREV_MONTH"));
        		collectionDto.setAdjustment(resultSet.getDouble("ADJUSTMENT"));
        		collectionDto.setCurr_sales(resultSet.getDouble("SALES_CURR_MONTH"));
        		collectionDto.setCurr_surcharge(resultSet.getDouble("SURCHARGE"));
        		collectionDto.setAccount_receivable(resultSet.getDouble("AR_CURR_MONTH"));
        		collectionDto.setGas_bill(resultSet.getDouble("GAS_BILL_COLL"));
        		collectionDto.setMeter_rent(resultSet.getDouble("METER_RENT_COLL"));
        		collectionDto.setColl_surcharge(resultSet.getDouble("SURCHARGE_COLL"));
        		collectionDto.setIncome_tax(resultSet.getDouble("TAX_COLL"));
        		collectionDto.setVat_rebate(resultSet.getDouble("VAT_REBATE"));
        		collectionDto.setHhv_nhv(resultSet.getDouble("NHV_HHV_COLL"));
        		collectionDto.setTotal_collection(resultSet.getDouble("TOTAL_COLL"));
        		collectionDto.setPrevious_due(resultSet.getDouble("TOTAL_DUE"));
        		collectionDto.setAvg_monthly_sales(resultSet.getDouble("AVG_MONTHLY_SALES"));
        		collectionDto.setAvg_due(resultSet.getDouble("AVERAGE_DUE"));
        		collectionDto.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		
        		
        		
   
        		
        		collectionInfoList.add(collectionDto);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return collectionInfoList;
	}


				public String getArea() {
					return area;
				}


				public void setArea(String area) {
					this.area = area;
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


				public String getCustomer_category() {
					return customer_category;
				}


				public void setCustomer_category(String customer_category) {
					this.customer_category = customer_category;
				}


				public String getCategory_name() {
					return category_name;
				}


				public void setCategory_name(String category_name) {
					this.category_name = category_name;
				}


		
				


	}