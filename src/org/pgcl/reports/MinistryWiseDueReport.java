package org.pgcl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.pgcl.actions.BaseAction;
import org.pgcl.dto.MinistryDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.dto.VatRebateITFFDTO;
import org.pgcl.enums.Area;
import org.pgcl.enums.Month;
import org.pgcl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MinistryWiseDueReport extends BaseAction {

	private static final long serialVersionUID = 1L;
	MinistryDTO ministryDTO=null;
	
	private ArrayList<MinistryDTO> ministryInfo = new ArrayList<MinistryDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
		private  String customer_category;
		private  String customer_ministry;
		private  String from_date;;
		private  String to_date;
		private  String report_for; 
		private  String category_name;
		private  String ministry_name;
		
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.0000");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.0000");
		static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		
		public String execute() throws Exception {




			String fileName="MinistryWise_Govt_Due_Report"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1])+".pdf";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Document document = new Document(PageSize.LEGAL.rotate() );
			document.setMargins(20,20,30,72);
			PdfPCell pcell=null;
			
			
			try{
				
				ReportFormat eEvent = new ReportFormat(getServletContext());
				
				//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
				
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				/*Image img = Image.getInstance("H:/workspaceNew/PGCL/PGCL_WEB/resources/images/logo.png");
				img.scaleAbsolute(65, 79);
	            img.setAbsolutePosition(100f, 510f);*/
				
				PdfPTable headerTable = new PdfPTable(3);
				   
				
				headerTable.setWidths(new float[] {
					40,120,40
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
				Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f8B);
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
				
				PdfPTable headLinetable = null;
				headLinetable = new PdfPTable(3);
				headLinetable.setWidthPercentage(100);
				headLinetable.setWidths(new float[]{5,90,5});
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
				pcell.setBorder(0);
				headLinetable.addCell(pcell);
				
				if(report_for.equals("area_wise"))
				{
					pcell=new PdfPCell(new Paragraph("STAEMENT SHOWING THE ARREAR AMOUNT TO DIFFERENT GOVT. CUSTOMERS FOR THE PERIOD OF "+from_date+" To "+to_date,ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pcell.setBorder(0);
					headLinetable.addCell(pcell);
				}else if(report_for.equals("category_wise"))
				{
					pcell=new PdfPCell(new Paragraph("STAEMENT SHOWING THE ARREAR AMOUNT TO DIFFERENT GOVT. CUSTOMERS FOR THE PERIOD OF "+from_date+" To "+to_date,ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pcell.setBorder(0);
					headLinetable.addCell(pcell);
				}else if(report_for.equals("ministry_wise"))
				{
					pcell=new PdfPCell(new Paragraph("STAEMENT SHOWING THE ARREAR AMOUNT TO ("+ministry_name+") CUSTOMERS FOR THE PERIOD OF "+from_date+" To "+to_date,ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pcell.setBorder(0);
					headLinetable.addCell(pcell);
				}else if(report_for.equals("customer_category"))
				{
					pcell=new PdfPCell(new Paragraph("STAEMENT SHOWING THE ARREAR AMOUNT TO ("+category_name+") CUSTOMERS FOR THE PERIOD OF "+from_date+" To "+to_date,ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pcell.setBorder(0);
					headLinetable.addCell(pcell);
				}
								
				pcell = new PdfPCell(new Paragraph(""));
				pcell.setBorder(0);
				headLinetable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setMinimumHeight(8f);
				pcell.setBorder(0);
				pcell.setColspan(3);
				headLinetable.addCell(pcell);
				
				
				document.add(headLinetable);
				
				///////////////////////////////////////////////////////////////////////
				
				
				/* End of the table */
				
				
				if(report_for.equals("area_wise"))
				{
					generateArea_wiseMinistryReport(document);
				}else if(report_for.equals("category_wise"))
				{
					generateCustomer_wiseMinistryReport(document);
				}else if(report_for.equals("ministry_wise"))
				{
					generateMinistry_wiseMinistryReport(document);
				}else if(report_for.equals("customer_category"))
				{
					generateCategory_wiseMinistryReport(document);
				}
				
				/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
				
				
				
				
				document.close();		
				document.close();
				ReportUtil rptUtil = new ReportUtil();
				rptUtil.downloadPdf(baos, getResponse(),fileName);
				document=null;
				
			    
			}catch(Exception e){e.printStackTrace();}
			
			return null;
			
		}
		
		private void generateArea_wiseMinistryReport(Document document)throws DocumentException{
			
			String day_mon_year=getday_mon_year();
			
			String fYearlast="";
            String fYearmid="";
            String fYearfirst="";
			
			//String day=day_mon_year.substring(0, 2);
            String month=day_mon_year.substring(3, 5);
            String year=day_mon_year.substring(6,10);
            
            int intYear=Integer.parseInt(year);
            
            if(month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06") ){
            	
            	fYearlast=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearmid=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            	fYearfirst=String.valueOf(intYear-3)+"-"+String.valueOf(intYear-2);
            }else{
            	
            	fYearlast=String.valueOf(intYear)+"-"+String.valueOf(intYear+1);
            	fYearmid=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearfirst=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            }
			
			document.setMargins(20,20,30,72);
			PdfPCell pcell=new PdfPCell();
			PdfPTable pTable = new PdfPTable(14);
			pTable.setWidthPercentage(100);
			pTable.setWidths(new float[]{2,13,7,8,7,7,7,7,8,7,7,7,7,6});
			pTable.setHeaderRows(4);
			
			pcell = new PdfPCell(new Paragraph("Amount in lack",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setRowspan(1);
			pcell.setColspan(14);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer ID No",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance as on "+from_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment (if Any)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Balance (After Adj.)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sales During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Collection During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Arrear as on "+to_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Monthly Avg. Sales",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("EQU. Arrear Month",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Period of Arrear",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearfirst,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearmid,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearlast,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			ministryInfo=getMinistryData();
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7=(4+5)-6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("9",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("11",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("12",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			double subtotalPreBalance=0.0;
			double subtotalAdjustment=0.0;
			double subtotalSales=0.0;
			double subtotalCollection=0.0;
			double subtotalArrear=0.0;
			double subtotalAvgSales=0.0;
			double subtotalEQUMonth=0.0;
			double subtotalFirstiscal=0.0;
			double subtotalSecondiscal=0.0;
			double subtotalThirdiscal=0.0;
			
			double grandtotalPreBalance=0.0;
			double grandtotalAdjustment=0.0;
			double grandtotalSales=0.0;
			double grandtotalCollection=0.0;
			double grandtotalArrear=0.0;
			double grandtotalAvgSales=0.0;
			double grandtotalEQUMonth=0.0;
			double grandtotalFirstiscal=0.0;
			double grandtotalSecondiscal=0.0;
			double grandtotalThirdiscal=0.0;
			
			
			int listSize=ministryInfo.size();
			
			String priviousMinistry= new String("");
			
			for (int i = 0; i < listSize; i++) {
				
				String currentMinistry=ministryInfo.get(i).getMinistry_id();
				
				if(i==0){
					pcell = new PdfPCell(new Paragraph("Name of Ministry : "+ministryInfo.get(i).getMinistry_name(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(14);
					pTable.addCell(pcell);
				}
				
				if(!currentMinistry.equals(priviousMinistry)){
					
					if(i>0){
						pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(3);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						if(subtotalAvgSales==0){
							pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}else{
							pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						subtotalPreBalance=0.0;
						subtotalAdjustment=0.0;
						subtotalSales=0.0;
						subtotalCollection=0.0;
						subtotalArrear=0.0;
						subtotalAvgSales=0.0;
						subtotalEQUMonth=0.0;
						subtotalFirstiscal=0.0;
						subtotalSecondiscal=0.0;
						subtotalThirdiscal=0.0;
						
						pcell = new PdfPCell(new Paragraph("Name of Ministry : "+ministryInfo.get(i).getMinistry_name(),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(14);
						pTable.addCell(pcell);
						
					}
					
					priviousMinistry=ministryInfo.get(i).getMinistry_id();
				}
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_name(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_id(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()+ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_sales()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_collection()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				double avgsales=0.0;
				if(ministryInfo.get(i).getTotal_due_month()==0.0){
					avgsales=0;
				}else{
					avgsales=ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month();
				}
				
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(avgsales),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getTotal_due_month()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getFirst_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getSecond_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				
				grandtotalPreBalance=grandtotalPreBalance+ministryInfo.get(i).getOpening_balance();
				grandtotalAdjustment=grandtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
				grandtotalSales=grandtotalSales+ministryInfo.get(i).getCurrent_month_sales();
				grandtotalCollection=grandtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
				grandtotalArrear=grandtotalArrear+ministryInfo.get(i).getClosing_balance();
				if(ministryInfo.get(i).getTotal_due_month()==0){
					
					grandtotalAvgSales=grandtotalAvgSales+0;
				}else{
					grandtotalAvgSales=grandtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
				}
								
				grandtotalEQUMonth=grandtotalEQUMonth+ministryInfo.get(i).getTotal_due_month();
				grandtotalFirstiscal=grandtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
				grandtotalSecondiscal=grandtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
				grandtotalThirdiscal=grandtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				if(currentMinistry.equals(priviousMinistry)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				}else if(!currentMinistry.equals(priviousMinistry)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
					
				}
				if(i==listSize-1){
					
					pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					if(subtotalAvgSales==0){
						pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}else{
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
				}
				
				
			}
			
			pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance+grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalCollection),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear/grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalFirstiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSecondiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalThirdiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			document.add(pTable);
			
		}
		
		private void  generateCategory_wiseMinistryReport(Document document)throws DocumentException{
			
			String day_mon_year=getday_mon_year();
			
			String fYearlast="";
            String fYearmid="";
            String fYearfirst="";
			
			//String day=day_mon_year.substring(0, 2);
            String month=day_mon_year.substring(3, 5);
            String year=day_mon_year.substring(6,10);
            
            int intYear=Integer.parseInt(year);
            
            if(month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06") ){
            	
            	fYearlast=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearmid=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            	fYearfirst=String.valueOf(intYear-3)+"-"+String.valueOf(intYear-2);
            }else{
            	
            	fYearlast=String.valueOf(intYear)+"-"+String.valueOf(intYear+1);
            	fYearmid=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearfirst=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            }
			
			document.setMargins(20,20,30,72);
			PdfPCell pcell=new PdfPCell();
			PdfPTable pTable = new PdfPTable(14);
			pTable.setWidthPercentage(100);
			pTable.setWidths(new float[]{2,13,7,8,7,7,7,7,8,7,7,7,7,6});
			pTable.setHeaderRows(4);
			
			pcell = new PdfPCell(new Paragraph("Amount in lack",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setRowspan(1);
			pcell.setColspan(14);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer ID No",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance as on "+from_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment (if Any)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Balance (After Adj.)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sales During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Collection During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Arrear as on "+to_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Monthly Avg. Sales",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("EQU. Arrear Month",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Period of Arrear",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearfirst,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearmid,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearlast,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			ministryInfo=getMinistryData();
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7=(4+5)-6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("9",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("11",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("12",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			double subtotalPreBalance=0.0;
			double subtotalAdjustment=0.0;
			double subtotalSales=0.0;
			double subtotalCollection=0.0;
			double subtotalArrear=0.0;
			double subtotalAvgSales=0.0;
			double subtotalEQUMonth=0.0;
			double subtotalFirstiscal=0.0;
			double subtotalSecondiscal=0.0;
			double subtotalThirdiscal=0.0;
			
			double grandtotalPreBalance=0.0;
			double grandtotalAdjustment=0.0;
			double grandtotalSales=0.0;
			double grandtotalCollection=0.0;
			double grandtotalArrear=0.0;
			double grandtotalAvgSales=0.0;
			double grandtotalEQUMonth=0.0;
			double grandtotalFirstiscal=0.0;
			double grandtotalSecondiscal=0.0;
			double grandtotalThirdiscal=0.0;
			
			
			int listSize=ministryInfo.size();
			
			String priviousMinistry= new String("");
			
			for (int i = 0; i < listSize; i++) {
				
				
				String currentCategory=ministryInfo.get(i).getCustomer_categoryId();
				
				if(i==0){
					pcell = new PdfPCell(new Paragraph("Name of Category : "+ministryInfo.get(i).getCustomer_category_name(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(14);
					pTable.addCell(pcell);
				}
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_name(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_id(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()+ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_sales()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_collection()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getTotal_due_month()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getFirst_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getSecond_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				
				grandtotalPreBalance=grandtotalPreBalance+ministryInfo.get(i).getOpening_balance();
				grandtotalAdjustment=grandtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
				grandtotalSales=grandtotalSales+ministryInfo.get(i).getCurrent_month_sales();
				grandtotalCollection=grandtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
				grandtotalArrear=grandtotalArrear+ministryInfo.get(i).getClosing_balance();
				if(ministryInfo.get(i).getTotal_due_month()==0){
					
					grandtotalAvgSales=grandtotalAvgSales+0;
				}else{
					grandtotalAvgSales=grandtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
				}
								
				grandtotalEQUMonth=grandtotalEQUMonth+ministryInfo.get(i).getTotal_due_month();
				grandtotalFirstiscal=grandtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
				grandtotalSecondiscal=grandtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
				grandtotalThirdiscal=grandtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance+grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalCollection),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear/grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalFirstiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSecondiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalThirdiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			document.add(pTable);
			
		}
		
		private void generateMinistry_wiseMinistryReport(Document document)throws DocumentException{
			
			String day_mon_year=getday_mon_year();
			
			String fYearlast="";
            String fYearmid="";
            String fYearfirst="";
			
			//String day=day_mon_year.substring(0, 2);
            String month=day_mon_year.substring(3, 5);
            String year=day_mon_year.substring(6,10);
            
            int intYear=Integer.parseInt(year);
            
            if(month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06") ){
            	
            	fYearlast=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearmid=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            	fYearfirst=String.valueOf(intYear-3)+"-"+String.valueOf(intYear-2);
            }else{
            	
            	fYearlast=String.valueOf(intYear)+"-"+String.valueOf(intYear+1);
            	fYearmid=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearfirst=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            }
			
			document.setMargins(20,20,30,72);
			PdfPCell pcell=new PdfPCell();
			PdfPTable pTable = new PdfPTable(14);
			pTable.setWidthPercentage(100);
			pTable.setWidths(new float[]{2,13,7,8,7,7,7,7,8,7,7,7,7,6});
			pTable.setHeaderRows(4);
			
			pcell = new PdfPCell(new Paragraph("Amount in lack",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setRowspan(1);
			pcell.setColspan(14);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer ID No",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance as on "+from_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment (if Any)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Balance (After Adj.)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sales During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Collection During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Arrear as on "+to_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Monthly Avg. Sales",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("EQU. Arrear Month",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Period of Arrear",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearfirst,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearmid,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearlast,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			ministryInfo=getMinistryData();
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7=(4+5)-6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("9",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("11",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("12",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			double subtotalPreBalance=0.0;
			double subtotalAdjustment=0.0;
			double subtotalSales=0.0;
			double subtotalCollection=0.0;
			double subtotalArrear=0.0;
			double subtotalAvgSales=0.0;
			double subtotalEQUMonth=0.0;
			double subtotalFirstiscal=0.0;
			double subtotalSecondiscal=0.0;
			double subtotalThirdiscal=0.0;
			
			double grandtotalPreBalance=0.0;
			double grandtotalAdjustment=0.0;
			double grandtotalSales=0.0;
			double grandtotalCollection=0.0;
			double grandtotalArrear=0.0;
			double grandtotalAvgSales=0.0;
			double grandtotalEQUMonth=0.0;
			double grandtotalFirstiscal=0.0;
			double grandtotalSecondiscal=0.0;
			double grandtotalThirdiscal=0.0;
			
			
			int listSize=ministryInfo.size();
			
			String priviousMinistry= new String("");
			
			for (int i = 0; i < listSize; i++) {
				
				String currentMinistry=ministryInfo.get(i).getCustomer_categoryId();
				
				if(i==0){
					pcell = new PdfPCell(new Paragraph("Name of Category : "+ministryInfo.get(i).getCustomer_category_name(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(14);
					pTable.addCell(pcell);
				}
				
				if(!currentMinistry.equals(priviousMinistry)){
					
					if(i>0){
						pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(3);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						if(subtotalAvgSales==0){
							pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}else{
							pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						subtotalPreBalance=0.0;
						subtotalAdjustment=0.0;
						subtotalSales=0.0;
						subtotalCollection=0.0;
						subtotalArrear=0.0;
						subtotalAvgSales=0.0;
						subtotalEQUMonth=0.0;
						subtotalFirstiscal=0.0;
						subtotalSecondiscal=0.0;
						subtotalThirdiscal=0.0;
						
						pcell = new PdfPCell(new Paragraph("Name of Customer Category : "+ministryInfo.get(i).getCustomer_category_name(),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(14);
						pTable.addCell(pcell);
						
					}
					
					priviousMinistry=ministryInfo.get(i).getCustomer_categoryId();
				}
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_name(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_id(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()+ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_sales()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_collection()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()/(ministryInfo.get(i).getTotal_due_month())),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getTotal_due_month()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getFirst_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getSecond_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				
				grandtotalPreBalance=grandtotalPreBalance+ministryInfo.get(i).getOpening_balance();
				grandtotalAdjustment=grandtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
				grandtotalSales=grandtotalSales+ministryInfo.get(i).getCurrent_month_sales();
				grandtotalCollection=grandtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
				grandtotalArrear=grandtotalArrear+ministryInfo.get(i).getClosing_balance();
				if(ministryInfo.get(i).getTotal_due_month()==0){
					
					grandtotalAvgSales=grandtotalAvgSales+0;
				}else{
					grandtotalAvgSales=grandtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
				}
								
				grandtotalEQUMonth=grandtotalEQUMonth+ministryInfo.get(i).getTotal_due_month();
				grandtotalFirstiscal=grandtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
				grandtotalSecondiscal=grandtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
				grandtotalThirdiscal=grandtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				if(currentMinistry.equals(priviousMinistry)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				}else if(!currentMinistry.equals(priviousMinistry)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
					
				}
				if(i==listSize-1){
					
					pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					if(subtotalAvgSales==0){
						pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}else{
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}
					
					
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
				}
				
				
			}
			
			pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance+grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalCollection),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear/grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalFirstiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSecondiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalThirdiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			document.add(pTable);
			
		}
		
		
private void generateCustomer_wiseMinistryReport(Document document)throws DocumentException{
			
			String day_mon_year=getday_mon_year();
			
			String fYearlast="";
            String fYearmid="";
            String fYearfirst="";
			
			//String day=day_mon_year.substring(0, 2);
            String month=day_mon_year.substring(3, 5);
            String year=day_mon_year.substring(6,10);
            
            int intYear=Integer.parseInt(year);
            
            if(month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06") ){
            	
            	fYearlast=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearmid=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            	fYearfirst=String.valueOf(intYear-3)+"-"+String.valueOf(intYear-2);
            }else{
            	
            	fYearlast=String.valueOf(intYear)+"-"+String.valueOf(intYear+1);
            	fYearmid=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
            	fYearfirst=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
            }
			
			document.setMargins(20,20,30,72);
			PdfPCell pcell=new PdfPCell();
			PdfPTable pTable = new PdfPTable(14);
			pTable.setWidthPercentage(100);
			pTable.setWidths(new float[]{2,13,7,8,7,7,7,7,8,7,7,7,7,6});
			pTable.setHeaderRows(4);
			
			pcell = new PdfPCell(new Paragraph("Amount in lack",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setRowspan(1);
			pcell.setColspan(14);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Customer ID No",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance as on "+from_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment (if Any)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Balance (After Adj.)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sales During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Collection During "+String.valueOf(Month.values()[Integer.valueOf(month)-1])+" "+year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Arrear as on "+to_date,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Monthly Avg. Sales",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("EQU. Arrear Month",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(2);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Period of Arrear",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearfirst,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearmid,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(fYearlast,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(1);
			pTable.addCell(pcell);
			
			ministryInfo=getMinistryData();
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7=(4+5)-6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("9",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("11",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("12",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			double subtotalPreBalance=0.0;
			double subtotalAdjustment=0.0;
			double subtotalSales=0.0;
			double subtotalCollection=0.0;
			double subtotalArrear=0.0;
			double subtotalAvgSales=0.0;
			double subtotalEQUMonth=0.0;
			double subtotalFirstiscal=0.0;
			double subtotalSecondiscal=0.0;
			double subtotalThirdiscal=0.0;
			
			double grandtotalPreBalance=0.0;
			double grandtotalAdjustment=0.0;
			double grandtotalSales=0.0;
			double grandtotalCollection=0.0;
			double grandtotalArrear=0.0;
			double grandtotalAvgSales=0.0;
			double grandtotalEQUMonth=0.0;
			double grandtotalFirstiscal=0.0;
			double grandtotalSecondiscal=0.0;
			double grandtotalThirdiscal=0.0;
			
			
			int listSize=ministryInfo.size();
			
			String priviousCategory= new String("");
			
			for (int i = 0; i < listSize; i++) {
				
				String currentCategory=ministryInfo.get(i).getCustomer_categoryId();
				
				if(i==0){
					pcell = new PdfPCell(new Paragraph("Name of Category : "+ministryInfo.get(i).getCustomer_category_name(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(14);
					pTable.addCell(pcell);
				}
				
				if(!currentCategory.equals(priviousCategory)){
					
					if(i>0){
						pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(3);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						if(subtotalAvgSales==0){
							pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}else{
							pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pTable.addCell(pcell);
						}
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
						
						subtotalPreBalance=0.0;
						subtotalAdjustment=0.0;
						subtotalSales=0.0;
						subtotalCollection=0.0;
						subtotalArrear=0.0;
						subtotalAvgSales=0.0;
						subtotalEQUMonth=0.0;
						subtotalFirstiscal=0.0;
						subtotalSecondiscal=0.0;
						subtotalThirdiscal=0.0;
						
						pcell = new PdfPCell(new Paragraph("Name of Customer Category : "+ministryInfo.get(i).getCustomer_category_name(),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(14);
						pTable.addCell(pcell);
						
					}
					
					priviousCategory=ministryInfo.get(i).getCustomer_categoryId();
				}
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_name(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryInfo.get(i).getCustomer_id(),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getOpening_balance()+ministryInfo.get(i).getAdjustment_amount()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_sales()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_month_collection()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getClosing_balance()/(ministryInfo.get(i).getTotal_due_month())),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getTotal_due_month()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getFirst_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getSecond_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(ministryInfo.get(i).getCurrent_economic_year()),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				
				grandtotalPreBalance=grandtotalPreBalance+ministryInfo.get(i).getOpening_balance();
				grandtotalAdjustment=grandtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
				grandtotalSales=grandtotalSales+ministryInfo.get(i).getCurrent_month_sales();
				grandtotalCollection=grandtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
				grandtotalArrear=grandtotalArrear+ministryInfo.get(i).getClosing_balance();
				if(ministryInfo.get(i).getTotal_due_month()==0){
					
					grandtotalAvgSales=grandtotalAvgSales+0;
				}else{
					grandtotalAvgSales=grandtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
				}
								
				grandtotalEQUMonth=grandtotalEQUMonth+ministryInfo.get(i).getTotal_due_month();
				grandtotalFirstiscal=grandtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
				grandtotalSecondiscal=grandtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
				grandtotalThirdiscal=grandtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				if(currentCategory.equals(priviousCategory)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
				
				}else if(!currentCategory.equals(priviousCategory)){
					
					subtotalPreBalance=subtotalPreBalance+ministryInfo.get(i).getOpening_balance();
					subtotalAdjustment=subtotalAdjustment+ministryInfo.get(i).getAdjustment_amount();
					subtotalSales=subtotalSales+ministryInfo.get(i).getCurrent_month_sales();
					subtotalCollection=subtotalCollection+ministryInfo.get(i).getCurrent_month_collection();
					subtotalArrear=subtotalArrear+ministryInfo.get(i).getClosing_balance();
					if(ministryInfo.get(i).getTotal_due_month()==0){
						subtotalAvgSales=subtotalAvgSales+0;
					}else{
						subtotalAvgSales=subtotalAvgSales+(ministryInfo.get(i).getClosing_balance()/ministryInfo.get(i).getTotal_due_month());
							
					}
					subtotalEQUMonth=ministryInfo.get(i).getClosing_balance()/subtotalAvgSales;
					subtotalFirstiscal=subtotalFirstiscal+ministryInfo.get(i).getFirst_economic_year();
					subtotalSecondiscal=subtotalSecondiscal+ministryInfo.get(i).getSecond_economic_year();
					subtotalThirdiscal=subtotalThirdiscal+ministryInfo.get(i).getCurrent_economic_year();
					
				}
				if(i==listSize-1){
					
					pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreBalance+subtotalAdjustment),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollection),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalAvgSales),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					if(subtotalAvgSales==0){
						pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}else{
						pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalArrear/subtotalAvgSales),ReportUtil.f9B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pTable.addCell(pcell);
					}
					
					
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalFirstiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSecondiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalThirdiscal),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
				}
				
				
			}
			
			pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalPreBalance+grandtotalAdjustment),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalCollection),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalArrear/grandtotalAvgSales),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalFirstiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSecondiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalThirdiscal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			document.add(pTable);
			
		}
		
		
		public String getday_mon_year(){
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			 String date1=new String();
		
			 try {

		            Date date = formatter.parse(to_date);
		            
		            date1=formatter.format(date);
		           		            
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }

			
			return date1;
		}
		
		
		private ArrayList<MinistryDTO> getMinistryData(){
			ArrayList<MinistryDTO> getMinistryDataList= new ArrayList<MinistryDTO>();
			
			try {
				String area=loggedInUser.getArea_id();
				
				String day_mon_year=getday_mon_year();
				
				String fYearlast="";
	            String fYearmid="";
	            String fYearfirst="";
				
				//String day=day_mon_year.substring(0, 2);
	            String month=day_mon_year.substring(3, 5);
	            String year=day_mon_year.substring(6,10);
	            
	            int intYear=Integer.parseInt(year);
	            
	            if(month.equals("01") || month.equals("02") || month.equals("03") || month.equals("04") || month.equals("05") || month.equals("06") ){
	            	
	            	fYearlast=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
	            	fYearmid=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
	            	fYearfirst=String.valueOf(intYear-3)+"-"+String.valueOf(intYear-2);
	            }else{
	            	
	            	fYearlast=String.valueOf(intYear)+"-"+String.valueOf(intYear+1);
	            	fYearmid=String.valueOf(intYear-1)+"-"+String.valueOf(intYear);
	            	fYearfirst=String.valueOf(intYear-2)+"-"+String.valueOf(intYear-1);
	            }
				
	            String[] fromDate=from_date.split("-");
				String[] toDate=to_date.split("-");
				
					String fromDateMonth=fromDate[1].toString();
					String fromDateYear=fromDate[2].toString();
					
					String toDateMonth=toDate[1].toString();
					String toDateYear=toDate[2].toString();
					
					String fromYearMonth1=fromDateYear+""+fromDateMonth;
					String toYearMonth1=toDateYear+""+toDateMonth;
					
					int fromYearMonth=Integer.parseInt(fromYearMonth1);
					int toYearMonth=Integer.parseInt(toYearMonth1);
					
					String wClause="";
					
					if(report_for.equals("area_wise"))
					{
						wClause="AND SUBSTR(MM.CUSTOMER_ID,1,2)='"+area+"' order by  MINISTRY_ID,MM.CUSTOMER_ID desc ";
					}else if(report_for.equals("category_wise"))
					{
						wClause="AND SUBSTR(MM.CUSTOMER_ID,1,2)='"+area+"'  order by  MM.CUSTOMER_ID";
					}else if(report_for.equals("ministry_wise"))
					{
						wClause="AND SUBSTR(MM.CUSTOMER_ID,1,2)='"+area+"' AND MINISTRY_ID='"+customer_ministry+"' order by MM.CUSTOMER_ID";
					}
	            
	            
				String disconnection_info_sql="SELECT MM.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE / 100000 OPENING_BALANCE,ADJUSTMENT/100000 ADJUSTMENT,TOTAL_SALES / 100000 TOTAL_SALES,TOTAL_COLLECTION / 100000 TOTAL_COLLECTION, " +
						"  CLOSING_BALANCE / 100000 CLOSING_BALANCE,NVL (TOTAL_DUE_MONTH, 0) TOTAL_DUE_MONTH,ISMETERED,STATUS,first_balance / 100000 first_balance,second_balance / 100000 second_balance, " +
						"  last_balance / 100000 last_balance,MINISTRY_NAME, MINISTRY_ID FROM ( SELECT TT.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE,nvl(ADJ_Sales,0)+nvl(SECURITY_ADJ,0)+nvl(SEC_SURCHARGE,0) ADJUSTMENT, " +
						" TOTAL_SALES,TOTAL_COLLECTION,CLOSING_BALANCE,TOTAL_DUE_MONTH,ISMETERED,STATUS,first_balance,second_balance,last_balance FROM( " +
						" select TA.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE,ADJ_Sales,TOTAL_SALES,TOTAL_COLLECTION,CLOSING_BALANCE,TOTAL_DUE_MONTH,ISMETERED, " +
						" STATUS,first_balance,second_balance,nvl(CLOSING_BALANCE,0)-nvl(first_balance,0)-nvl(second_balance,0) last_balance from(SELECT T1.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE, " +
						"  TOTAL_SALES,TOTAL_COLLECTION,CLOSING_BALANCE,TOTAL_DUE_MONTH,ISMETERED,STATUS,getFiscalYearBal (T1.CUSTOMER_ID,'"+day_mon_year+"','"+fYearfirst+"') first_balance, " +
						"  getFiscalYearBalNEXT (T1.CUSTOMER_ID,'"+day_mon_year+"','"+fYearmid+"') second_balance " +
						" FROM (SELECT BB.CUSTOMER_ID, CATEGORY_NAME, CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,TOTAL_COLLECTION,OPENING_BALANCE,CLOSING_BALANCE, ISMETERED, STATUS " +
						" FROM (  SELECT CUSTOMER_ID,SUM (NVL (DEBIT, 0))- SUM (NVL (SURCHARGE, 0))  TOTAL_COLLECTION FROM bank_account_ledger " +
						" WHERE     SUBSTR (CUSTOMER_ID, 3, 2) IN ('02','04','06','11','12') AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy') " +
						" AND TRANS_TYPE = 1 GROUP BY customer_id) AA,(SELECT CC.CUSTOMER_ID, MCI.CATEGORY_NAME, MCI.CATEGORY_ID,MCI.FULL_NAME,MCI.ADDRESS_LINE1, " +
						" getLedgerBal (CC.CUSTOMER_ID, '"+from_date+"') OPENING_BALANCE,getLedgerBalClosing (CC.CUSTOMER_ID,'"+to_date+"') CLOSING_BALANCE, CC.ISMETERED,CC.STATUS " +
						" FROM CUSTOMER_CONNECTION CC,MVIEW_CUSTOMER_INFO MCI " +
						" WHERE CC.CUSTOMER_ID = MCI.CUSTOMER_ID AND SUBSTR (CC.CUSTOMER_ID, 3, 2) IN ('02','04','06','11','12')) BB " +
						" WHERE AA.CUSTOMER_ID(+) = BB.CUSTOMER_ID) T1,(SELECT BB.CUSTOMER_ID, TOTAL_SALES, TOTAL_DUE_MONTH " +
						" FROM (  SELECT CUSTOMER_ID,SUM (NVL (TOTAL_AMOUNT, 0)) TOTAL_SALES FROM sales_report " +
						" WHERE SUBSTR (customer_id, 3, 2) IN ('02','04','06','11','12') " +
						" AND TO_NUMBER (billing_year || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+"  " +
						" GROUP BY CUSTOMER_ID) AA,(SELECT CUSTOMER_ID, getDueMonth_Govt (CUSTOMER_ID, '"+to_date+"') TOTAL_DUE_MONTH FROM customer_connection " +
						" WHERE SUBSTR (customer_id, 3, 2) IN ('02','04','06','11','12')) BB " +
						" WHERE AA.CUSTOMER_ID(+) = BB.CUSTOMER_ID) T2  " +
						" WHERE T1.CUSTOMER_ID = T2.CUSTOMER_ID(+)) TA,(select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"'  " +
						" AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY')  " +
						" group by CUSTOMER_ID) TB " +
						" where ta.CUSTOMER_ID=tb.CUSTOMER_ID(+)) TT,( " +
						" select customer_id cust,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger   " +
						"  where trans_type=666 AND account_no<>'420420'   " +
						" AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'   " +
						" group by customer_id) SS " +
						" where TT.CUSTOMER_ID=SS.cust(+)) MM, " +
						"         CUSTOMER_MINISTRY CM " +
						"   WHERE     MM.CUSTOMER_ID = CM.CUSTOMER_ID " +wClause;

						
						
						
						
						
						
						
						
						
						
						
						
						
						
						/*"SELECT MM.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE/100000 OPENING_BALANCE,TOTAL_SALES/100000 TOTAL_SALES,TOTAL_COLLECTION/100000 TOTAL_COLLECTION,CLOSING_BALANCE/100000 CLOSING_BALANCE,nvl(TOTAL_DUE_MONTH,0) TOTAL_DUE_MONTH,ISMETERED,STATUS, " +
						"first_balance/100000 first_balance,second_balance/100000 second_balance,last_balance/100000 last_balance,MINISTRY_NAME,MINISTRY_ID from( " +
						"SELECT T1.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,OPENING_BALANCE,TOTAL_SALES,TOTAL_COLLECTION,CLOSING_BALANCE,TOTAL_DUE_MONTH,ISMETERED,STATUS, " +
						"getFiscalYearBal(T1.CUSTOMER_ID,'"+day_mon_year+"','"+fYearfirst+"') first_balance, " +
						"getFiscalYearBalNEXT(T1.CUSTOMER_ID,'"+day_mon_year+"','"+fYearmid+"') second_balance, " +
						"getFiscalYearBalNEXT(T1.CUSTOMER_ID,'"+day_mon_year+"','"+fYearlast+"') last_balance FROM( " +
						"SELECT BB.CUSTOMER_ID,CATEGORY_NAME,CATEGORY_ID,FULL_NAME,ADDRESS_LINE1,TOTAL_COLLECTION,OPENING_BALANCE,CLOSING_BALANCE,ISMETERED,STATUS FROM ( " +
						"select CUSTOMER_ID,sum(nvl(DEBIT,0))-sum(nvl(SURCHARGE,0)) TOTAL_COLLECTION from bank_account_ledger " +
						"where substr(CUSTOMER_ID,3,2) in ('02','04','06','11','12') " +
						"AND TRANS_DATE between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') " +
						" AND TRANS_TYPE=1 group by customer_id) AA, " +
						"(select CC.CUSTOMER_ID,MCI.CATEGORY_NAME,MCI.CATEGORY_ID,MCI.FULL_NAME,MCI.ADDRESS_LINE1,getLedgerBal(CC.CUSTOMER_ID,'"+from_date+"') OPENING_BALANCE,getLedgerBalClosing(CC.CUSTOMER_ID,'"+to_date+"') CLOSING_BALANCE,  " +
						"CC.ISMETERED,CC.STATUS from CUSTOMER_CONNECTION CC,MVIEW_CUSTOMER_INFO MCI " +
						"WHERE CC.CUSTOMER_ID=MCI.CUSTOMER_ID " +
						"and SUBSTR(CC.CUSTOMER_ID,3,2) in ('02','04','06','11','12')) BB " +
						"WHERE AA.CUSTOMER_ID(+)=BB.CUSTOMER_ID) T1, " +
						"(SELECT AA.CUSTOMER_ID,TOTAL_SALES,TOTAL_DUE_MONTH FROM( " +
						"select CUSTOMER_ID,sum(nvl(TOTAL_AMOUNT,0)) TOTAL_SALES from sales_report " +
						"where substr(customer_id,3,2) in ('02','04','06','11','12') " +
						"AND TO_NUMBER (billing_year || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+"  " +
						"group by CUSTOMER_ID) AA, " +
						"(SELECT CUSTOMER_ID,getDueMonth_Govt(CUSTOMER_ID,'"+to_date+"' ) TOTAL_DUE_MONTH FROM customer_connection"+
                        "     WHERE     SUBSTR (customer_id, 3, 2) IN ('02','04','06','11','12')) BB " +
						"WHERE AA.CUSTOMER_ID=BB.CUSTOMER_ID(+)) T2 " +
						"WHERE T1.CUSTOMER_ID=T2.CUSTOMER_ID(+)) MM,CUSTOMER_MINISTRY CM " +
						"where MM.CUSTOMER_ID=CM.CUSTOMER_ID " +wClause ;*/






				
				PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
			
	        	
	        	ResultSet resultSet=ps1.executeQuery();
	        	
	        	
	        	while(resultSet.next())
	        	{
	        		ministryDTO = new MinistryDTO();
	        		
	        		ministryDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
	        		ministryDTO.setCustomer_name(resultSet.getString("FULL_NAME"));
	        		ministryDTO.setCustomer_address(resultSet.getString("ADDRESS_LINE1"));
	        		ministryDTO.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
	        		ministryDTO.setCustomer_categoryId(resultSet.getString("CATEGORY_ID"));
	        		ministryDTO.setOpening_balance(resultSet.getDouble("OPENING_BALANCE"));
	        		ministryDTO.setCurrent_month_sales(resultSet.getDouble("TOTAL_SALES"));
	        		ministryDTO.setCurrent_month_collection(resultSet.getDouble("TOTAL_COLLECTION"));
	        		ministryDTO.setClosing_balance(resultSet.getDouble("CLOSING_BALANCE"));
	        		ministryDTO.setTotal_due_month(resultSet.getDouble("TOTAL_DUE_MONTH"));
	        		ministryDTO.setActive_status(resultSet.getString("STATUS"));
	        		ministryDTO.setMeter_status(resultSet.getString("ISMETERED"));
	        		ministryDTO.setFirst_economic_year(resultSet.getDouble("FIRST_BALANCE"));
	        		ministryDTO.setSecond_economic_year(resultSet.getDouble("SECOND_BALANCE"));
	        		ministryDTO.setCurrent_economic_year(resultSet.getDouble("LAST_BALANCE"));
	        		ministryDTO.setMinistry_name(resultSet.getString("MINISTRY_NAME"));
	        		ministryDTO.setMinistry_id(resultSet.getString("MINISTRY_ID"));
	        		ministryDTO.setAdjustment_amount(resultSet.getDouble("ADJUSTMENT"));
	        		
	        		
	        		getMinistryDataList.add(ministryDTO);

	   
	        		
	        		
	        		
	        	}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return getMinistryDataList;
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

		public String getCustomer_ministry() {
			return customer_ministry;
		}

		public void setCustomer_ministry(String customer_ministry) {
			this.customer_ministry = customer_ministry;
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

		public String getMinistry_name() {
			return ministry_name;
		}

		public void setMinistry_name(String ministry_name) {
			this.ministry_name = ministry_name;
		}
		
		
	
		
		
		
}