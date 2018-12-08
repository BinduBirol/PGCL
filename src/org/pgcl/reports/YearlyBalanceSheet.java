package org.pgcl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.pgcl.actions.BaseAction;
import org.pgcl.dto.BalanceSheetDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.enums.Area;
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

public class YearlyBalanceSheet extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	ArrayList<BalanceSheetDTO> balanceSheetInfoList = new ArrayList<BalanceSheetDTO>();
	ArrayList<BalanceSheetDTO> balanceSheetInfoListDiscon = new ArrayList<BalanceSheetDTO>();
	
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	

	    private  String fiscal_year;
	    private  String report_for; 
	    private  String area="01";
	    private  String from_date;
	    private  String to_date;
	    private  String customer_type;
	    private  String customer_category;
	    private  String category_id;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	public String execute() throws Exception
	{
				
		String fileName="YearlyBalance_Sheet.pdf";
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
			
			
			pcell=new PdfPCell(new Paragraph("Area Wise Accounts Receivable Statement of Different Categories as on "+from_date+" To "+to_date,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
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
			
			
			if(report_for.equals("details")){
				if(customer_type.equals("01")){
					generatePdfDetails(document);
				}else{
					generatePdfDetailsNonmetered(document);
				}
				
			}else if(report_for.equals("area_wise")){
				generatePdfArea_wise(document);
			}else if(report_for.equals("category_wise")){
				generatePdfCategory_wise(document);
			}else if(report_for.equals("category_wisef")){
				generatePdfPGCLReceivable(document);
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
	
	private void generatePdfDetailsNonmetered(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(18);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{3,8,6,5,6,6,6,5,6,6,5,6,5,5,5,6,6,5});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Burner Qty",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales Adj.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(7);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Freedom Fighter/ Waiver",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Security Adj.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		String previousCategory= new String("");
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesAdjustment=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalCollectedSurcharge=0.0;
		double subtotalchalanIT=0.0;
		double subTotalWaiver=0.0;
		double subTotalSecurityAjustment=0.0;
		double subTotalCollectionAmt=0.0;
		
		double grandTotalPreviousBalance=0.0;
		double grandTotalSalesAdjustment=0.0;
		double grandTotalGasBill=0.0;
		double grandTotalSurcharge=0.0;
		double grandTotalSales=0.0;
		double grandTotalGasBillCollection=0.0;
		double grandTotalCollection1=0.0;
		double grandTotalChalanIT=0.0;
		double grandTotalVatRebate=0.0;
		double grandTotalSecurityAdjustment=0.0;
		double grandTotalCollection2=0.0;
		double grandTotalEndingBalance=0.0;
		double grandTotalCollectedSurcharge=0.0;
		
		
		balanceSheetInfoList=getYearlyBalanceSheetInfoNonMetered();
		
		
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=balanceSheetInfoList.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(18);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(4);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesAdjustment),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAjustment),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver+subTotalSecurityAjustment;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subtotalSalesAdjustment=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subtotalCollectedSurcharge=0.0;
					subTotalWaiver=0.0;
					subTotalSecurityAjustment=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(18);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(18);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoList.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			

			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBurner()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSalesAdjustment()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getCollectedSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getWaiver()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSecurityCollection()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getWaiver()+balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getSecurityCollection());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance=grandTotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			grandTotalSalesAdjustment=grandTotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
			grandTotalGasBill=grandTotalGasBill+balanceSheetInfoList.get(i).getBillAmount();
			grandTotalSurcharge=grandTotalSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalSales=grandTotalSales+balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalGasBillCollection=grandTotalGasBillCollection+balanceSheetInfoList.get(i).getCollectedAmount();
			grandTotalCollectedSurcharge=grandTotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
			grandTotalCollection1=grandTotalCollection1+balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getCollectedSurcharge();
			grandTotalChalanIT=grandTotalChalanIT+balanceSheetInfoList.get(i).getChalanIT();
			grandTotalVatRebate=grandTotalVatRebate+balanceSheetInfoList.get(i).getWaiver();
			grandTotalSecurityAdjustment=grandTotalSecurityAdjustment+balanceSheetInfoList.get(i).getSecurityCollection();
			grandTotalCollection2=grandTotalCollection2+balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getWaiver()+balanceSheetInfoList.get(i).getCollectedAmount()
					+balanceSheetInfoList.get(i).getCollectedSurcharge()+balanceSheetInfoList.get(i).getSecurityCollection();
			grandTotalEndingBalance=grandTotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesAdjustment=subtotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoList.get(i).getWaiver();
				subTotalSecurityAjustment=subTotalSecurityAjustment+balanceSheetInfoList.get(i).getSecurityCollection();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesAdjustment=subtotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoList.get(i).getVateRebate();
				subTotalSecurityAjustment=subTotalSecurityAjustment+balanceSheetInfoList.get(i).getSecurityCollection();
			}
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(4);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesAdjustment),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAjustment),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver+subTotalSecurityAjustment;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdjustment),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill+grandTotalSurcharge),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection+grandTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdjustment),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection+grandTotalCollectedSurcharge+grandTotalChalanIT+grandTotalVatRebate+grandTotalSecurityAdjustment),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);	
		
		/*--------------------------------------------------------------------Disconnected Customer List---------------------------------------------*/
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Dis-Connected Customer",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		
		previousCategory= new String("");
		totalBillAmount=0.0;
		totalCollectionAmount=0.0;
		
		subtotalPreviousBalance=0.0;
		subtotalSalesAdjustment=0.0;
		subtotalSalesBill=0.0;
		subtotalSaleSurcharge=0.0;
		subtotalSalesTotal=0.0;
		subtotalCollectionAmt=0.0;
		subtotalCollectionTotal=0.0;
		subtotalEndingBalance=0.0;
		totalCollectionEnd=0.0;
		subtotalMeterRent=0.0;
		subtotalHHV=0.0;
		subtotalCollectedSurcharge=0.0;
		subtotalchalanIT=0.0;
		subTotalWaiver=0.0;
		subTotalSecurityAjustment=0.0;
		subTotalCollectionAmt=0.0;
		
		double grandTotalPreviousBalance2=0.0;
		double grandTotalSalesAdjustmentDis=0.0;
		double grandTotalGasBill2=0.0;
		double grandTotalSurcharge2=0.0;
		double grandTotalSales2=0.0;
		double grandTotalGasBillCollection2=0.0;
		double grandTotalCollection12=0.0;
		double grandTotalChalanIT2=0.0;
		double grandTotalVatRebate2=0.0;
		double grandTotalCollection22=0.0;
		double grandTotalSecurityAdjustmentDis=0.0;
		double grandTotalEndingBalance2=0.0;
		double grandTotalCollectedSurcharge2=0.0;
		
		
		balanceSheetInfoListDiscon=getYearlyBalanceSheetInfoDisconnectedNonMetered();
		
		
		
		int listSize1=balanceSheetInfoListDiscon.size();
		
		for (int i = 0; i < listSize1; i++) {
			
			String currentCategory=balanceSheetInfoListDiscon.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoListDiscon.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(18);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(4);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesAdjustment),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAjustment),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver+subTotalSecurityAjustment;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subtotalSalesAdjustment=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subtotalCollectedSurcharge=0.0;
					subTotalWaiver=0.0;
					subTotalSecurityAjustment=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(18);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoListDiscon.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(18);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoListDiscon.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoListDiscon.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoListDiscon.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			

			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getBurner()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSalesAdjustment()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoListDiscon.get(i).getBillAmount()+balanceSheetInfoListDiscon.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getCollectedSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			totalCollectionAmount=balanceSheetInfoListDiscon.get(i).getCollectedAmount()+balanceSheetInfoListDiscon.get(i).getCollectedSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getWaiver()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSecurityCollection()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoListDiscon.get(i).getWaiver()+balanceSheetInfoListDiscon.get(i).getChalanIT()+balanceSheetInfoListDiscon.get(i).getSecurityCollection());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance2=grandTotalPreviousBalance2+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
			grandTotalSalesAdjustmentDis=grandTotalSalesAdjustmentDis+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
			grandTotalGasBill2=grandTotalGasBill2+balanceSheetInfoListDiscon.get(i).getBillAmount();
			grandTotalSurcharge2=grandTotalSurcharge2+balanceSheetInfoListDiscon.get(i).getSurcharge();
			grandTotalSales2=grandTotalSales2+balanceSheetInfoListDiscon.get(i).getBillAmount()+balanceSheetInfoListDiscon.get(i).getSurcharge();
			grandTotalGasBillCollection2=grandTotalGasBillCollection2+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
			grandTotalCollectedSurcharge2=grandTotalCollectedSurcharge2+balanceSheetInfoListDiscon.get(i).getCollectedSurcharge();
			grandTotalCollection12=grandTotalCollection12+balanceSheetInfoListDiscon.get(i).getCollectedAmount()+balanceSheetInfoListDiscon.get(i).getCollectedSurcharge();
			grandTotalChalanIT2=grandTotalChalanIT2+balanceSheetInfoListDiscon.get(i).getChalanIT();
			grandTotalVatRebate2=grandTotalVatRebate2+balanceSheetInfoListDiscon.get(i).getWaiver();
			grandTotalSecurityAdjustmentDis=grandTotalSecurityAdjustmentDis+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
			grandTotalCollection22=grandTotalCollection22+grandTotalChalanIT2+grandTotalVatRebate2+grandTotalCollection12+grandTotalSecurityAdjustmentDis;
			grandTotalEndingBalance2=grandTotalEndingBalance2+balanceSheetInfoListDiscon.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
				subtotalSalesAdjustment=subtotalSalesAdjustment+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoListDiscon.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoListDiscon.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoListDiscon.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoListDiscon.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoListDiscon.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoListDiscon.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoListDiscon.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoListDiscon.get(i).getWaiver();
				subTotalSecurityAjustment=subTotalSecurityAjustment+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
				subtotalSalesAdjustment=subtotalSalesAdjustment+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoListDiscon.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoListDiscon.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoListDiscon.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoListDiscon.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoListDiscon.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoListDiscon.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoListDiscon.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoListDiscon.get(i).getVateRebate();
				subTotalSecurityAjustment=subTotalSecurityAjustment+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
			}
			
			if(i==listSize1-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(4);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesAdjustment),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAjustment),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver+subTotalSecurityAjustment;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance2),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2+grandTotalSurcharge2+grandTotalSalesAdjustmentDis),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollectedSurcharge2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalCollectedSurcharge2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalCollectedSurcharge2+grandTotalChalanIT2+grandTotalVatRebate2+grandTotalSecurityAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);	
		
		/*-----------------------------------------------------Total Connected and Disconnected customer--------------------------------------------------*/
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Dis-Connected And Connected Customer",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(18);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total = ",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(4);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance2+grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdjustment+grandTotalSalesAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2+grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2+grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2+grandTotalGasBill+grandTotalSurcharge2+grandTotalSurcharge),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollectedSurcharge2+grandTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalGasBillCollection+grandTotalCollectedSurcharge2+grandTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT2+grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate2+grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdjustment+grandTotalSecurityAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalGasBillCollection+grandTotalCollectedSurcharge2+grandTotalCollectedSurcharge
				+grandTotalChalanIT2+grandTotalChalanIT+grandTotalVatRebate2+grandTotalVatRebate+grandTotalSecurityAdjustment+grandTotalSecurityAdjustmentDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance2+grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);	
		
		
		
		document.add(pTable);
		
	}
	
	private void generatePdfDetails(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(19);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,9,5,5,5,5,5,5,6,5,5,5,6,5,5,5,7,5,5});
		pTable.setHeaderRows(4);
		
		pcell = new PdfPCell(new Paragraph("Connected Customer",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales Adjustment",ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(9);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sec. Adjustment",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Connected Customer",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pTable.addCell(pcell);
		
		String previousCategory= new String("");
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subTotalSalesAdj=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subTotalSecurityAdj=0.0;
		double subTotalCollectionAmt=0.0;
		
		double grandTotalPreviousBalance=0.0;
		double grandTotalGasBill=0.0;
		double grandTotalSurcharge=0.0;
		double grandTotalSalesAdj=0.0;
		double grandTotalSales=0.0;
		double grandTotalGasBillCollection=0.0;
		double grandTotalMeterRent=0.0;
		double grandTotalHHV=0.0;
		double grandTotalCollection1=0.0;
		double grandTotalChalanIT=0.0;
		double grandTotalVatRebate=0.0;
		double grandTotalSecurityAdj=0.0;
		double grandTotalCollection2=0.0;
		double grandTotalEndingBalance=0.0;
		
		
		balanceSheetInfoList=getYearlyBalanceSheetInfo();
		
		
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=balanceSheetInfoList.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(19);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSalesAdj),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAdj),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate+subTotalSecurityAdj;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subTotalSalesAdj=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subTotalVatRebate=0.0;
					subTotalSecurityAdj=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(19);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(19);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoList.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSalesAdjustment()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSecurityCollection()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getSecurityCollection());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance  =grandTotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			grandTotalSalesAdj		   =grandTotalSalesAdj+balanceSheetInfoList.get(i).getSalesAdjustment();
			grandTotalGasBill          =grandTotalGasBill+balanceSheetInfoList.get(i).getBillAmount();
			grandTotalSurcharge        =grandTotalSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalSales			   =grandTotalSales+balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalGasBillCollection=grandTotalGasBillCollection+balanceSheetInfoList.get(i).getCollectedAmount();
			grandTotalMeterRent=grandTotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			grandTotalHHV=grandTotalHHV+balanceSheetInfoList.get(i).getHhv();
			grandTotalCollection1=grandTotalCollection1+balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv()+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalChalanIT=grandTotalChalanIT+balanceSheetInfoList.get(i).getChalanIT();
			grandTotalVatRebate=grandTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			grandTotalSecurityAdj=grandTotalSecurityAdj+balanceSheetInfoList.get(i).getSecurityCollection();
			grandTotalCollection2=grandTotalCollection2+balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv()+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecurityCollection();
			grandTotalEndingBalance=grandTotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subTotalSalesAdj=subTotalSalesAdj+balanceSheetInfoList.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				subTotalSecurityAdj=subTotalSecurityAdj+balanceSheetInfoList.get(i).getSecurityCollection();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subTotalSalesAdj=subTotalSalesAdj+balanceSheetInfoList.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				subTotalSecurityAdj=subTotalSecurityAdj+balanceSheetInfoList.get(i).getSecurityCollection();
			}
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(3);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSalesAdj),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAdj),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate+subTotalSecurityAdj;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdj),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSales),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection1),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdj),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		/*--------------------------------------------------------Disconnected Customer List Start here---------------------------------------------------------------------------------------------------*/
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Dis-Connected Customer",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pTable.addCell(pcell);
		
		
		 previousCategory= new String("");
		 totalBillAmount=0.0;
		 totalCollectionAmount=0.0;
		
		 subtotalPreviousBalance=0.0;
		 subTotalSalesAdj=0.0;
		 subtotalSalesBill=0.0;
		 subtotalSaleSurcharge=0.0;
		 subtotalSalesTotal=0.0;
		 subtotalCollectionAmt=0.0;
		 subtotalCollectionTotal=0.0;
		 subtotalEndingBalance=0.0;
		 totalCollectionEnd=0.0;
		 subtotalMeterRent=0.0;
		 subtotalHHV=0.0;
		 subtotalchalanIT=0.0;
		 subTotalVatRebate=0.0;
		 subTotalSecurityAdj=0.0;
		 subTotalCollectionAmt=0.0;
		 double grandTotalPreviousBalance2=0.0;
		 double grandTotalSalesAdjDis=0.0;
		 double grandTotalGasBill2=0.0;
		 double grandTotalSurcharge2=0.0;
		 double grandTotalSales2=0.0;
		 double grandTotalGasBillCollection2=0.0;
		 double grandTotalMeterRent2=0.0;
		 double grandTotalHHV2=0.0;
		 double grandTotalCollection12=0.0;
		 double grandTotalChalanIT2=0.0;
		 double grandTotalVatRebate2=0.0;
		 double grandTotalSecurityAdjDis=0.0;
		 double grandTotalCollection22=0.0;
		 double grandTotalEndingBalance2=0.0;
		
		
		balanceSheetInfoListDiscon=getDisconnectedBalanceSheetInfo();
		
		
		
		int listSize1=balanceSheetInfoListDiscon.size();
		
		for (int i = 0; i < listSize1; i++) {
			
			String currentCategory=balanceSheetInfoListDiscon.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoListDiscon.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(19);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSalesAdj),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAdj),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate+subTotalSecurityAdj;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subTotalSalesAdj=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subTotalVatRebate=0.0;
					subTotalSecurityAdj=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(19);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoListDiscon.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(19);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoListDiscon.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoListDiscon.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoListDiscon.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSalesAdjustment()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoListDiscon.get(i).getBillAmount()+balanceSheetInfoListDiscon.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getMeterRent()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getHhv()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalCollectionAmount=balanceSheetInfoListDiscon.get(i).getCollectedAmount()+balanceSheetInfoListDiscon.get(i).getSurcharge()
					+balanceSheetInfoListDiscon.get(i).getMeterRent()+balanceSheetInfoListDiscon.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getVateRebate()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getSecurityCollection()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoListDiscon.get(i).getVateRebate()+balanceSheetInfoListDiscon.get(i).getChalanIT()+balanceSheetInfoListDiscon.get(i).getSecurityCollection());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoListDiscon.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance2=grandTotalPreviousBalance2+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
			grandTotalSalesAdjDis=grandTotalSalesAdjDis+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
			grandTotalGasBill2=grandTotalGasBill2+balanceSheetInfoListDiscon.get(i).getBillAmount();
			grandTotalSurcharge2=grandTotalSurcharge2+balanceSheetInfoListDiscon.get(i).getSurcharge();
			grandTotalSales2=grandTotalSales2+balanceSheetInfoListDiscon.get(i).getBillAmount()+balanceSheetInfoListDiscon.get(i).getSurcharge();
			grandTotalGasBillCollection2=grandTotalGasBillCollection2+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
			grandTotalMeterRent2=grandTotalMeterRent2+balanceSheetInfoListDiscon.get(i).getMeterRent();
			grandTotalHHV2=grandTotalHHV2+balanceSheetInfoListDiscon.get(i).getHhv();
			grandTotalCollection12=grandTotalCollection12+balanceSheetInfoListDiscon.get(i).getCollectedAmount()+balanceSheetInfoListDiscon.get(i).getMeterRent()+balanceSheetInfoListDiscon.get(i).getHhv()+balanceSheetInfoListDiscon.get(i).getSurcharge();
			grandTotalChalanIT2=grandTotalChalanIT2+balanceSheetInfoListDiscon.get(i).getChalanIT();
			grandTotalVatRebate2=grandTotalVatRebate2+balanceSheetInfoListDiscon.get(i).getVateRebate();
			grandTotalSecurityAdjDis=grandTotalSecurityAdjDis+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
			grandTotalCollection22=grandTotalCollection22+balanceSheetInfoListDiscon.get(i).getChalanIT()+balanceSheetInfoListDiscon.get(i).getVateRebate()
					+balanceSheetInfoListDiscon.get(i).getCollectedAmount()+balanceSheetInfoListDiscon.get(i).getMeterRent()+balanceSheetInfoListDiscon.get(i).getHhv()
					+balanceSheetInfoListDiscon.get(i).getSurcharge()+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
			grandTotalEndingBalance2=grandTotalEndingBalance2+balanceSheetInfoListDiscon.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
				subTotalSalesAdj=subTotalSalesAdj+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoListDiscon.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoListDiscon.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoListDiscon.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoListDiscon.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoListDiscon.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoListDiscon.get(i).getChalanIT();
				subTotalSecurityAdj=subTotalSecurityAdj+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoListDiscon.get(i).getVateRebate();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoListDiscon.get(i).getPreviousBalance();
				subTotalSalesAdj=subTotalSalesAdj+balanceSheetInfoListDiscon.get(i).getSalesAdjustment();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoListDiscon.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoListDiscon.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoListDiscon.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoListDiscon.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoListDiscon.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoListDiscon.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoListDiscon.get(i).getChalanIT();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoListDiscon.get(i).getVateRebate();
				subTotalSecurityAdj=subTotalSecurityAdj+balanceSheetInfoListDiscon.get(i).getSecurityCollection();
			}
			
			if(i==listSize1-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(3);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSalesAdj),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecurityAdj),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate+subTotalSecurityAdj;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance2),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);		
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdjDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSales2),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalMeterRent2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalHHV2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection12),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdjDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection22),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		/*-----------------------------------------------------Total of connected and Disconnected-----------------------------------------------*/
		
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("-",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pcell.setBorder(0);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Dis-Connected And Connected Customer",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(19);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total = ",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance2+grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSalesAdj+grandTotalSalesAdjDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill2+grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2+grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSales2+grandTotalSales),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection2+grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalMeterRent2+grandTotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge2+grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalHHV2+grandTotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection12+grandTotalCollection1),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT2+grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate2+grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityAdj+grandTotalSecurityAdjDis),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection22+grandTotalCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance2+grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		
		
		document.add(pTable);
		
	}
	
	private void generatePdfArea_wise(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,8,8,6});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Area",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subtotalCollectionEnd=0.0;
		
		balanceSheetInfoList=getAreawiseBalanceSheetInfo();
		
		String areaName="";
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			areaName=String.valueOf(Area.values()[Integer.valueOf(balanceSheetInfoList.get(i).getArea_id())-1]);
			
			pcell = new PdfPCell(new Paragraph(areaName,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
	}
	private void generatePdfCategory_wise(Document document)throws DocumentException{

		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(18);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,7,6,5,6,5,6,6,5,5,6,6,6,6,5,7,6,5});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales Adj.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(9);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate/FF Waiver",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sec.Adj",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);	
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
	
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subTotalSalesAdjustment=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subTotalCollectedSurcharge=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subTotalSecAdj=0.0;
		double subtotalCollectionEnd=0.0;
		String previousSubCategory=new String("");
		String CategoryId=new String("");
		double subCategorytotalPreviousBalance=0.0;
		double subcategoryTotalSalesAdjustment=0.0;
		double subCategorytotalSalesBill=0.0;
		double subCategorytotalSaleSurcharge=0.0;
		double subCategorytotalSalesTotal=0.0;
		double subCategorytotalCollectionAmt=0.0;
		double subCategorytotalCollectionTotal=0.0;
		double subCategorytotalEndingBalance=0.0;
		double totalCategoryCollectionEnd=0.0;
		double subCategorytotalMeterRent=0.0;
		double subCategoryTotalCollectedSurcharge=0.0;
		double subCategorytotalHHV=0.0;
		double subCategorytotalchalanIT=0.0;
		double subCategoryTotalVatRebate=0.0;
		double subCategoryTotalSecAdj=0.0;
		double subCategorytotalCollectionEnd=0.0;
		double totalCategoryBillAmount=0.0;
		double totalCategoryCollectionAmount=0.0;
		
		balanceSheetInfoList=getCategorywiseBalanceSheetInfo();
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String CurrentSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			CategoryId=balanceSheetInfoList.get(i).getCustomer_category();
			
			
			if(!CurrentSubCategory.equals(previousSubCategory)){
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subcategoryTotalSalesAdjustment),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalCollectedSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalSecAdj),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					subCategorytotalPreviousBalance=0.0;
					subcategoryTotalSalesAdjustment=0.0;
					subCategorytotalSalesBill=0.0;
					subCategorytotalSaleSurcharge=0.0;
					subCategorytotalSalesTotal=0.0;
					subCategorytotalCollectionAmt=0.0;
					subCategorytotalCollectionTotal=0.0;
					subCategorytotalEndingBalance=0.0;
					totalCategoryCollectionEnd=0.0;
					subCategorytotalMeterRent=0.0;
					subCategoryTotalCollectedSurcharge=0.0;
					subCategorytotalHHV=0.0;
					subCategorytotalchalanIT=0.0;
					subCategoryTotalVatRebate=0.0;
					subCategorytotalCollectionEnd=0.0;
					subCategoryTotalSecAdj=0.0;
					totalCategoryBillAmount=0.0;
					totalCategoryCollectionAmount=0.0;
					
				}
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSalesAdjustment()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalSalesAdjustment=subTotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecuritySurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecuritySurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecuritySurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalCollectedSurcharge=subTotalCollectedSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSecurityCollection()+balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalSecAdj=subTotalSecAdj+balanceSheetInfoList.get(i).getSecurityCollection()+balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getSecurityCollection()+balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			if(CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subcategoryTotalSalesAdjustment=subcategoryTotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecuritySurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();	
				subCategoryTotalCollectedSurcharge=subCategoryTotalCollectedSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				subCategoryTotalSecAdj=subCategoryTotalSecAdj+balanceSheetInfoList.get(i).getSecurityCollection()+balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getSecurityCollection()+
						balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				
			}else if(!CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subcategoryTotalSalesAdjustment=subcategoryTotalSalesAdjustment+balanceSheetInfoList.get(i).getSalesAdjustment();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge()+balanceSheetInfoList.get(i).getSecuritySurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subCategoryTotalCollectedSurcharge=subCategoryTotalCollectedSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				subCategoryTotalSecAdj=subCategoryTotalSecAdj+balanceSheetInfoList.get(i).getSecurityCollection()+balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getSecurityCollection()+
						balanceSheetInfoList.get(i).getSecurityMeterRent()+balanceSheetInfoList.get(i).getSecuritySurcharge());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
			}
			previousSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(2);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subcategoryTotalSalesAdjustment),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalCollectedSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalSecAdj),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
			}
			
			
			if(CategoryId.equals("02") || CategoryId.equals("04") || CategoryId.equals("06") || CategoryId.equals("08") ||
					CategoryId.equals("10") || CategoryId.equals("12")){
				
			}
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSalesAdjustment),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalSecAdj),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
	
	}
	
	private void generatePdfPGCLReceivable(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,8,8,6});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subtotalCollectionEnd=0.0;
		String previousSubCategory=new String("");
		String CategoryId=new String("");
		double subCategorytotalPreviousBalance=0.0;
		double subCategorytotalSalesBill=0.0;
		double subCategorytotalSaleSurcharge=0.0;
		double subCategorytotalSalesTotal=0.0;
		double subCategorytotalCollectionAmt=0.0;
		double subCategorytotalCollectionTotal=0.0;
		double subCategorytotalEndingBalance=0.0;
		double totalCategoryCollectionEnd=0.0;
		double subCategorytotalMeterRent=0.0;
		double subCategorytotalHHV=0.0;
		double subCategorytotalchalanIT=0.0;
		double subCategoryTotalVatRebate=0.0;
		double subCategorytotalCollectionEnd=0.0;
		double totalCategoryBillAmount=0.0;
		double totalCategoryCollectionAmount=0.0;
		
		double gsubCategorytotalPreviousBalance=0.0;
		double gsubCategorytotalSalesBill=0.0;
		double gsubCategorytotalSaleSurcharge=0.0;
		double gsubCategorytotalSalesTotal=0.0;
		double gsubCategorytotalCollectionAmt=0.0;
		double gsubCategorytotalCollectionTotal=0.0;
		double gsubCategorytotalEndingBalance=0.0;
		double gtotalCategoryCollectionEnd=0.0;
		double gsubCategorytotalMeterRent=0.0;
		double gsubCategorytotalHHV=0.0;
		double gsubCategorytotalchalanIT=0.0;
		double gsubCategoryTotalVatRebate=0.0;
		double gsubCategorytotalCollectionEnd=0.0;
		double gtotalCategoryBillAmount=0.0;
		double gtotalCategoryCollectionAmount=0.0;
		
		double psubCategorytotalPreviousBalance=0.0;
		double psubCategorytotalSalesBill=0.0;
		double psubCategorytotalSaleSurcharge=0.0;
		double psubCategorytotalSalesTotal=0.0;
		double psubCategorytotalCollectionAmt=0.0;
		double psubCategorytotalCollectionTotal=0.0;
		double psubCategorytotalEndingBalance=0.0;
		double ptotalCategoryCollectionEnd=0.0;
		double psubCategorytotalMeterRent=0.0;
		double psubCategorytotalHHV=0.0;
		double psubCategorytotalchalanIT=0.0;
		double psubCategoryTotalVatRebate=0.0;
		double psubCategorytotalCollectionEnd=0.0;
		double ptotalCategoryBillAmount=0.0;
		double ptotalCategoryCollectionAmount=0.0;
		
		balanceSheetInfoList=getCategorywiseFullBalanceSheetInfo();
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String CurrentSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			CategoryId=balanceSheetInfoList.get(i).getCustomer_category();
			
			
			if(!CurrentSubCategory.equals(previousSubCategory)){
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					subCategorytotalPreviousBalance=0.0;
					subCategorytotalSalesBill=0.0;
					subCategorytotalSaleSurcharge=0.0;
					subCategorytotalSalesTotal=0.0;
					subCategorytotalCollectionAmt=0.0;
					subCategorytotalCollectionTotal=0.0;
					subCategorytotalEndingBalance=0.0;
					totalCategoryCollectionEnd=0.0;
					subCategorytotalMeterRent=0.0;
					subCategorytotalHHV=0.0;
					subCategorytotalchalanIT=0.0;
					subCategoryTotalVatRebate=0.0;
					subCategorytotalCollectionEnd=0.0;
					totalCategoryBillAmount=0.0;
					totalCategoryCollectionAmount=0.0;
					
				}
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			if(CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				
			}else if(!CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
			}
			previousSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(2);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
			}
			
			
			if(CategoryId.equals("02") || CategoryId.equals("04") || CategoryId.equals("06") || CategoryId.equals("08") ||
					CategoryId.equals("10") || CategoryId.equals("12")){
				
				gsubCategorytotalPreviousBalance=gsubCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				gsubCategorytotalSalesBill=gsubCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				gsubCategorytotalSaleSurcharge=gsubCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				gtotalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				gsubCategorytotalSalesTotal=gsubCategorytotalSalesTotal+gtotalCategoryBillAmount;
				gsubCategorytotalCollectionAmt=gsubCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				gsubCategorytotalMeterRent=gsubCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				gsubCategorytotalHHV=gsubCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				gtotalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				gsubCategorytotalCollectionTotal=gsubCategorytotalCollectionTotal+gtotalCategoryCollectionAmount;
				gsubCategorytotalchalanIT=gsubCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				gsubCategoryTotalVatRebate=gsubCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				gtotalCategoryCollectionEnd=gtotalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				gsubCategorytotalCollectionEnd=gsubCategorytotalCollectionEnd+gtotalCategoryCollectionEnd;
				gsubCategorytotalEndingBalance=gsubCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
			}else{
				
				psubCategorytotalPreviousBalance=psubCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				psubCategorytotalSalesBill=psubCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				psubCategorytotalSaleSurcharge=psubCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				ptotalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				psubCategorytotalSalesTotal=psubCategorytotalSalesTotal+ptotalCategoryBillAmount;
				psubCategorytotalCollectionAmt=psubCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				psubCategorytotalMeterRent=psubCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				psubCategorytotalHHV=psubCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				ptotalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				psubCategorytotalCollectionTotal=psubCategorytotalCollectionTotal+ptotalCategoryCollectionAmount;
				psubCategorytotalchalanIT=psubCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				psubCategoryTotalVatRebate=psubCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				ptotalCategoryCollectionEnd=ptotalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				psubCategorytotalCollectionEnd=psubCategorytotalCollectionEnd+ptotalCategoryCollectionEnd;
				psubCategorytotalEndingBalance=psubCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
				
			}
			
			
		}
		
		/*------------------------Sub Total Govt---------------------------*/
		
		pcell = new PdfPCell(new Paragraph(" Sub Total Govt.",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategoryTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		/*------------------------Sub Total PVT.----------------------------*/
		
		pcell = new PdfPCell(new Paragraph(" Sub Total PVT.",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategoryTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		/*------------------------Grand Total------------------------------*/
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
		
	} 
	
	
	private ArrayList<BalanceSheetDTO> getYearlyBalanceSheetInfo(){
				
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		//PreparedStatement ps1=null;
		String area=loggedInUser.getArea_id();
		
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			 
			
			
			String disconnection_info_sql="SELECT TA.CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,FULL_NAME,ADDRESS_LINE1,bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,LAST_Bal,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE FROM(SELECT tab.CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME, " +
					"FULL_NAME,ADDRESS_LINE1,bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT-NVL(HHV_NHV_BILL,0) DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT, " +
					"LAST_Bal FROM(SELECT T2.CUSTOMER_ID,getLedgerBal (t2.CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT, METER_RENT, SURCHARGE, TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,getLedgerBalClosing (t2.CUSTOMER_ID, '"+to_date+"') LAST_Bal,CUSTOMER_CATEGORY,DECODE (CUSTOMER_CATEGORY,'01', 'Domestic (PVT.)', " +
					"'02', 'Domestic (GOVT.)','03', 'Commercial Pvt','04', 'Commercial Govt','05', 'Industry Pvt','06', 'Industry Govt','07', 'Captive Pvt','09', 'CNG Pvt',    " +
					"'11', 'Power Pvt','12', 'Power Govt') CUSTOMER_CATEGORY_NAME FROM (SELECT CC.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,SUBSTR (CC.CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY FROM (SELECT decode(tab2.customer_id,null,tab1.customer_id,tab2.customer_id) customer_id, " +
					"BILLED_AMOUNT,ADJ_Sales,nvl(DEBIT,0)-nvl(ADJUSTMENT_AMOUNT,0) DEBIT,METER_RENT,nvl(SURCHARGE,0)+nvl(ADJUSTMENT_AMOUNT,0) SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL, " +
					"VAT_REBATE_AMOUNT FROM (SELECT aa.customer_id,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,ADJUSTMENT_AMOUNT FROM (SELECT CUSTOMER_ID, " +
					"SUM (NVL (DEBIT, 0))- SUM (NVL (METER_RENT, 0))- SUM (NVL (SURCHARGE, 0))- SUM (NVL (MISCELLANEOUS, 0)) DEBIT,SUM (NVL (METER_RENT, 0)) METER_RENT, " +
					"SUM (NVL (SURCHARGE, 0)) SURCHARGE FROM bank_Account_ledger WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')    " +
					"AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"'  GROUP BY customer_id) aa,    " +
					"(select ee.CUSTOMER_ID,TAX_AMOUNT,nvl(HHV_NHV_BILL,0)+nvl(HHV_NHV_ADJ_QNT,0) HHV_NHV_BILL,ADJUSTMENT_AMOUNT from( "+
					"SELECT CUSTOMER_ID,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL,sum(nvl(ADJUSTMENT_AMOUNT,0)) ADJUSTMENT_AMOUNT  "+ 
					"FROM bill_collection_metered BCM,summary_margin_pb PB   "+  
					"WHERE BCM.BILL_ID = PB.BILL_ID AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')  "+   
					"AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' GROUP BY customer_id) ee, "+
					"(select sa.customer_id,sum(nvl(HHV_NHV_ADJ_QNT,0)) HHV_NHV_ADJ_QNT from sales_adjustment sa,bill_collection_metered bcm "+
					"where sa.customer_id=bcm.customer_id "+
					"AND sa.bill_id=bcm.bill_id "+
					"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy') "+
					"AND SUBSTR (sa.CUSTOMER_ID, 1, 2) = '"+area+"' group by sa.CUSTOMER_ID) ff "+
					"where ee.customer_id=ff.customer_id(+)) bb    " +
					"WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) tab1 full outer join     " +
					"(SELECT pp.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,VAT_REBATE_AMOUNT from( " +
					"SELECT CUSTOMER_ID,SUM (TOTAL_AMOUNT) BILLED_AMOUNT,SUM (VAT_REBATE) VAT_REBATE_AMOUNT FROM sales_report    " +
					"WHERE SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' AND TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN  "+fromYearMonth+" AND "+toYearMonth+" " +
					"GROUP BY CUSTOMER_ID) pp, " +
					"(select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)+nvl(HHV_NHV_ADJ_QNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"group by CUSTOMER_ID) qq " +
					"where pp.CUSTOMER_ID=qq.CUSTOMER_ID(+)) tab2    " +
					"ON TAB1.CUSTOMER_ID = tab2.CUSTOMER_ID) t1,    " +
					"CUSTOMER_CONNECTION CC    " +
					"WHERE CC.CUSTOMER_ID = T1.CUSTOMER_ID(+)    " +
					"AND SUBSTR (CC.CUSTOMER_ID, 1, 2) = '"+area+"'   " +
					"AND CC.ISMETERED = 1    " +
					"AND CC.STATUS = 1) t2,MVIEW_CUSTOMER_INFO    " +
					"WHERE MVIEW_CUSTOMER_INFO.CUSTOMER_ID = T2.CUSTOMER_ID) tab,MVIEW_CUSTOMER_INFO mv   " +
					"where TAB.CUSTOMER_ID=MV.CUSTOMER_ID) TA,  " +
					"(select customer_id,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT, " +
					"sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"where trans_type=666  " +
					"AND account_no<>'420420'  " +
					"AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY')  " +
					"AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  " +
					"group by customer_id) TB  " +
					"WHERE TA.CUSTOMER_ID=TB.CUSTOMER_ID(+)  " +
					"AND SUBSTR(TA.CUSTOMER_ID,3,2)='"+customer_category+"' " +
					"ORDER BY TA.CUSTOMER_ID " ;

			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_f"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("DEBIT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("LAST_Bal"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		balanceSheetDTO.setSalesAdjustment(resultSet.getDouble("ADJ_Sales"));
        		balanceSheetDTO.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}
	
	private ArrayList<BalanceSheetDTO> getDisconnectedBalanceSheetInfo(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		//PreparedStatement ps1=null;
		String area=loggedInUser.getArea_id();
		
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="SELECT TA.CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,FULL_NAME,ADDRESS_LINE1,bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,LAST_Bal,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE FROM(SELECT tab.CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME, " +
					"FULL_NAME,ADDRESS_LINE1,bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT-NVL(HHV_NHV_BILL,0) DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT, " +
					"LAST_Bal FROM(SELECT T2.CUSTOMER_ID,getLedgerBal (t2.CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT, METER_RENT, SURCHARGE, TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,getLedgerBalClosing (t2.CUSTOMER_ID, '"+to_date+"') LAST_Bal,CUSTOMER_CATEGORY,DECODE (CUSTOMER_CATEGORY,'01', 'Domestic (PVT.)', " +
					"'02', 'Domestic (GOVT.)','03', 'Commercial Pvt','04', 'Commercial Govt','05', 'Industry Pvt','06', 'Industry Govt','07', 'Captive Pvt','09', 'CNG Pvt',    " +
					"'11', 'Power Pvt','12', 'Power Govt') CUSTOMER_CATEGORY_NAME FROM (SELECT CC.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, " +
					"HHV_NHV_BILL,VAT_REBATE_AMOUNT,SUBSTR (CC.CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY FROM (SELECT decode(tab2.customer_id,null,tab1.customer_id,tab2.customer_id) customer_id, " +
					"BILLED_AMOUNT,ADJ_Sales,nvl(DEBIT,0)-nvl(ADJUSTMENT_AMOUNT,0) DEBIT,METER_RENT,nvl(SURCHARGE,0)+nvl(ADJUSTMENT_AMOUNT,0) SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL, " +
					"VAT_REBATE_AMOUNT FROM (SELECT aa.customer_id,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,ADJUSTMENT_AMOUNT FROM (SELECT CUSTOMER_ID, " +
					"SUM (NVL (DEBIT, 0))- SUM (NVL (METER_RENT, 0))- SUM (NVL (SURCHARGE, 0))- SUM (NVL (MISCELLANEOUS, 0)) DEBIT,SUM (NVL (METER_RENT, 0)) METER_RENT, " +
					"SUM (NVL (SURCHARGE, 0)) SURCHARGE FROM bank_Account_ledger WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')    " +
					"AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"'  GROUP BY customer_id) aa,    " +
					"(select ee.CUSTOMER_ID,TAX_AMOUNT,nvl(HHV_NHV_BILL,0)+nvl(HHV_NHV_ADJ_QNT,0) HHV_NHV_BILL,ADJUSTMENT_AMOUNT from( "+
					"SELECT CUSTOMER_ID,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL,sum(nvl(ADJUSTMENT_AMOUNT,0)) ADJUSTMENT_AMOUNT  "+ 
					"FROM bill_collection_metered BCM,summary_margin_pb PB   "+  
					"WHERE BCM.BILL_ID = PB.BILL_ID AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')  "+   
					"AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' GROUP BY customer_id) ee, "+
					"(select sa.customer_id,sum(nvl(HHV_NHV_ADJ_QNT,0)) HHV_NHV_ADJ_QNT from sales_adjustment sa,bill_collection_metered bcm "+
					"where sa.customer_id=bcm.customer_id "+
					"AND sa.bill_id=bcm.bill_id "+
					"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy') "+
					"AND SUBSTR (sa.CUSTOMER_ID, 1, 2) = '"+area+"' group by sa.CUSTOMER_ID) ff "+
					"where ee.customer_id=ff.customer_id(+)) bb    " +
					"WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) tab1 full outer join     " +
					"(SELECT pp.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,VAT_REBATE_AMOUNT from( " +
					"SELECT CUSTOMER_ID,SUM (TOTAL_AMOUNT) BILLED_AMOUNT,SUM (VAT_REBATE) VAT_REBATE_AMOUNT FROM sales_report    " +
					"WHERE SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' AND TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN  "+fromYearMonth+" AND "+toYearMonth+" " +
					"GROUP BY CUSTOMER_ID) pp, " +
					"(select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)+nvl(HHV_NHV_ADJ_QNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"group by CUSTOMER_ID) qq " +
					"where pp.CUSTOMER_ID=qq.CUSTOMER_ID(+)) tab2    " +
					"ON TAB1.CUSTOMER_ID = tab2.CUSTOMER_ID) t1,    " +
					"CUSTOMER_CONNECTION CC    " +
					"WHERE CC.CUSTOMER_ID = T1.CUSTOMER_ID(+)    " +
					"AND SUBSTR (CC.CUSTOMER_ID, 1, 2) = '"+area+"'   " +
					"AND CC.ISMETERED = 1    " +
					"AND CC.STATUS = 0) t2,MVIEW_CUSTOMER_INFO    " +
					"WHERE MVIEW_CUSTOMER_INFO.CUSTOMER_ID = T2.CUSTOMER_ID) tab,MVIEW_CUSTOMER_INFO mv   " +
					"where TAB.CUSTOMER_ID=MV.CUSTOMER_ID) TA,  " +
					"(select customer_id,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT, " +
					"sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"where trans_type=666  " +
					"AND account_no<>'420420'  " +
					"AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY')  " +
					"AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  " +
					"group by customer_id) TB  " +
					"WHERE TA.CUSTOMER_ID=TB.CUSTOMER_ID(+)  " +
					"AND SUBSTR(TA.CUSTOMER_ID,3,2)='"+customer_category+"' " +
					"ORDER BY TA.CUSTOMER_ID ";
					
					
					
					
					
					/*"SELECT TA.*,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE from( " +
					"SELECT MVIEW_CUSTOMER_INFO.CUSTOMER_ID,FULL_NAME,ADDRESS_LINE1 ADDRESS,getLedgerBal (t2.CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,  " +
					"VAT_REBATE_AMOUNT,getLedgerBalClosing (t2.CUSTOMER_ID, '"+to_date+"') bal_n,CUSTOMER_CATEGORY,  " +
					"DECODE(CUSTOMER_CATEGORY,'01', 'Domestic Pvt','02','Domestic Govt','03','Commercial Pvt','04','Commercial Govt','05','Industry Pvt','06','Industry Govt','07','Captive Pvt','09','CNG Pvt','11','Power Pvt','12','Power Govt'  " +
					") CUSTOMER_CATEGORY_NAME FROM(  " +
					"SELECT CC.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT,SUBSTR (CC.CUSTOMER_ID,3,2) CUSTOMER_CATEGORY FROM(  " +
					"Select tab2.customer_id,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT from(  " +
					"select aa.customer_id,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL from(  " +
					"select CUSTOMER_ID,sum(nvl(DEBIT,0))-sum(nvl(METER_RENT,0))-sum(nvl(SURCHARGE,0))-sum(nvl(MISCELLANEOUS,0)) DEBIT,sum(nvl(METER_RENT,0)) METER_RENT,sum(nvl(SURCHARGE,0)) SURCHARGE from bank_Account_ledger   " +
					"WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy') AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"' group by customer_id) aa, " +
					"(Select CUSTOMER_ID,sum(nvl(TAX_AMOUNT,0)) TAX_AMOUNT,sum(nvl(HHV_NHV_BILL,0)) HHV_NHV_BILL  from bill_collection_metered BCM,summary_margin_pb PB  " +
					"WHERE BCM.BILL_ID=PB.BILL_ID  " +
					"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy') AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"' group by customer_id) bb  " +
					"where aa.CUSTOMER_ID=bb.CUSTOMER_ID(+)) tab1,  " +
					"(SELECT pp.CUSTOMER_ID,BILLED_AMOUNT,ADJ_Sales,VAT_REBATE_AMOUNT from( " +
					"SELECT CUSTOMER_ID,SUM (TOTAL_AMOUNT) BILLED_AMOUNT,SUM (VAT_REBATE) VAT_REBATE_AMOUNT FROM sales_report    " +
					"WHERE SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' AND TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN  "+fromYearMonth+" AND "+toYearMonth+" " +
					"GROUP BY CUSTOMER_ID) pp, " +
					"(select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"group by CUSTOMER_ID) qq " +
					"where pp.CUSTOMER_ID=qq.CUSTOMER_ID(+)) tab2  " +
					"WHERE TAB1.CUSTOMER_ID(+)=tab2.CUSTOMER_ID) t1,CUSTOMER_CONNECTION CC  " +
					"WHERE CC.CUSTOMER_ID = T1.CUSTOMER_ID(+)  " +
					"AND SUBSTR (CC.CUSTOMER_ID, 1, 2) =  '"+area+"'  " +
					"AND CC.ISMETERED=1  " +
					"AND CC.STATUS=0) t2,MVIEW_CUSTOMER_INFO  " +
					"WHERE MVIEW_CUSTOMER_INFO.CUSTOMER_ID = T2.CUSTOMER_ID  " +
					"order by T2.CUSTOMER_ID) TA, " +
					"(select customer_id cust_id,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"where trans_type=666  " +
					"AND account_no<>'420420'  " +
					"AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  " +
					"group by customer_id) TB " +
					"WHERE TA.customer_id=TB.cust_id(+) " +
					"AND SUBSTR(TA.customer_id,3,2)='"+customer_category+"' " +
					"ORDER BY TA.customer_id " ;*/

				
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_f"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("DEBIT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("LAST_Bal"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		balanceSheetDTO.setSalesAdjustment(resultSet.getDouble("ADJ_Sales"));
        		balanceSheetDTO.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}
	
	private ArrayList<BalanceSheetDTO> getYearlyBalanceSheetInfoNonMetered(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		//PreparedStatement ps1=null;
		String area=loggedInUser.getArea_id();
		
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="SELECT TA.CUSTOMER_ID,FULL_NAME,ADDRESS,bal_f, CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,  " +
					"BILLED_AMOUNT,ADJ_Sales, COLLECTED_BILLED_AMOUNT,COLLECTED_SURCHARGE, WAIVER,NEW_DOUBLE_BURNER_QNT,bal_next,  " +
					"NEW_DOUBLE_BURNER_QNT_BILLCAL,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE from( " +
					"SELECT TAB1.CUSTOMER_ID,FULL_NAME,ADDRESS,getLedgerBal (TAB1.CUSTOMER_ID, '"+from_date+"') bal_f, " +
					"					         CUSTOMER_CATEGORY,DECODE (CUSTOMER_CATEGORY,'01', 'Domestic Pvt','02', 'Domestic Govt') CUSTOMER_CATEGORY_NAME,  " +
					"					         BILLED_AMOUNT,ADJ_Sales, COLLECTED_BILLED_AMOUNT,COLLECTED_SURCHARGE, WAIVER,NEW_DOUBLE_BURNER_QNT,getLedgerBalClosing (TAB1.CUSTOMER_ID, '"+to_date+"') bal_next,  " +
					"					         NEW_DOUBLE_BURNER_QNT_BILLCAL  " +
					"					    FROM (SELECT aa.CUSTOMER_ID CUSTOMER_ID,  " +
					"					                 SUBSTR (aa.CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY,  " +
					"					                 BILLED_AMOUNT,ADJ_Sales,  " +
					"					                 COLLECTED_BILLED_AMOUNT,  " +
					"					                 COLLECTED_SURCHARGE,  " +
					"					                 WAIVER  " +
					"					            FROM (SELECT aa.CUSTOMER_ID,  " +
					"					                         BILLED_AMOUNT,ADJ_Sales,  " +
					"					                         COLLECTED_BILLED_AMOUNT,  " +
					"					                         COLLECTED_SURCHARGE  " +
					"					                    FROM (SELECT cc.CUSTOMER_ID,  " +
					"					                                 BILLED_AMOUNT,ADJ_Sales  " +
					"					                           FROM (select pp.customer_id,CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales from( " +
					"                                                SELECT CUSTOMER_ID,SUBSTR (CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY, SUM (NVL (TOTAL_AMOUNT, 0)) BILLED_AMOUNT FROM SALES_REPORT " +
					"                                                WHERE TO_NUMBER (billing_year || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+" " +
					"                                                AND SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"                                                GROUP BY CUSTOMER_ID) pp, " +
					"                                                (select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"                                                AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"                                                group by CUSTOMER_ID) qq " +
					"                                                where pp.customer_id=qq.customer_id(+)) aa, customer_connection cc  " +
					"					                            WHERE cc.customer_id = aa.CUSTOMER_ID(+)  " +
					"					                                 AND SUBSTR (cc.customer_id, 1, 2) = '"+area+"'  " +
					"					                                 AND ismetered = 0  " +
					"					                                 AND status = 1) aa,  " +
					"					                         (SELECT CUSTOMER_ID,  " +
					"					                                   SUM (NVL (DEBIT, 0))-SUM (NVL (SURCHARGE, 0))-SUM (NVL (MISCELLANEOUS, 0))  " +
					"					                                      COLLECTED_BILLED_AMOUNT,  " +
					"				                                   SUM (NVL (SURCHARGE, 0))  " +
					"					                                      COLLECTED_SURCHARGE  " +
					"					                              FROM bank_account_ledger  " +
					"					                             WHERE  TRANS_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','DD-MM-YYYY')  " +
					"					                                   AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) AND SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"					                          GROUP BY CUSTOMER_ID) bb  " +
					"					                   WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) aa,  " +
					"					                 (SELECT customer_id,SUM (NVL (TOTAL_COLLECTED_AMOUNT, 0)) WAIVER  " +
					"					                     FROM bill_collection_non_metered  " +
					"					                     WHERE     bank_id = '1'  " +
					"					                     AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"					                     GROUP BY customer_id) bb  " +
					"					           WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) tab1,  " +
					"					         ( SELECT bqc.CUSTOMER_ID CUSTOMER_ID,MCI.FULL_NAME FULL_NAME,MCI.ADDRESS_LINE1 ADDRESS,bqc.NEW_DOUBLE_BURNER_QNT NEW_DOUBLE_BURNER_QNT,  " +
					"					           bqc.NEW_DOUBLE_BURNER_QNT_BILLCAL NEW_DOUBLE_BURNER_QNT_BILLCAL  " +
					"					           FROM BURNER_QNT_CHANGE BQC, MVIEW_CUSTOMER_INFO MCI  " +
					"					           WHERE PID IN (SELECT MAX (PID)  " +
					"					                         FROM BURNER_QNT_CHANGE  " +
					"					                         WHERE SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"					                         AND effective_date <= TO_DATE ('"+to_date+"','DD-MM-YYYY')  " +
					"					                         GROUP BY CUSTOMER_ID)  " +
					"					                  AND MCI.CUSTOMER_ID = BQC.CUSTOMER_ID  " +
					"					          ORDER BY CUSTOMER_ID DESC) tab2  " +
					"					   WHERE TAB1.CUSTOMER_ID = TAB2.CUSTOMER_ID  " +
					"					ORDER BY TAB1.CUSTOMER_ID ) TA, " +
					"					(select customer_id cust,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"                    where trans_type=666  " +
					"                    AND account_no<>'420420'  " +
					"                    AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  " +
					"                    group by customer_id) TB " +
					"                    WHERE TA.customer_id=TB.cust(+) " +
					"                    AND substr(TA.customer_id,3,2)='"+customer_category+"' " +
					"                    ORDER BY TA.customer_id " ;

			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("BAL_F"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("COLLECTED_SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTED_BILLED_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("bal_next"));
        		balanceSheetDTO.setBurner(resultSet.getDouble("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
        		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("COLLECTED_SURCHARGE"));
        		balanceSheetDTO.setWaiver(resultSet.getDouble("WAIVER"));
        		balanceSheetDTO.setSalesAdjustment(resultSet.getDouble("ADJ_Sales"));
        		balanceSheetDTO.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}
	
private ArrayList<BalanceSheetDTO> getYearlyBalanceSheetInfoDisconnectedNonMetered(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		//PreparedStatement ps1=null;
		String area=loggedInUser.getArea_id();
		
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="SELECT TA.*,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE from( " +
					"SELECT TAB1.CUSTOMER_ID,  " +
					"					         FULL_NAME,  " +
					"					         ADDRESS,  " +
					"					         getLedgerBal (TAB1.CUSTOMER_ID, '"+from_date+"') bal_f,  " +
					"					         CUSTOMER_CATEGORY,  " +
					"					         CUSTOMER_CATEGORY_NAME,  " +
					"					         BILLED_AMOUNT,ADJ_Sales,  " +
					"					         COLLECTED_BILLED_AMOUNT, " +
					"					         COLLECTED_SURCHARGE,  " +
					"					         WAIVER,  " +
					"					         NEW_DOUBLE_BURNER_QNT,  " +
					"					         getLedgerBalClosing (TAB1.CUSTOMER_ID, '"+to_date+"') bal_next,  " +
					"					         NEW_DOUBLE_BURNER_QNT_BILLCAL  " +
					"					    FROM (SELECT aa.CUSTOMER_ID CUSTOMER_ID,  " +
					"					                 CUSTOMER_CATEGORY,  " +
					"					                 CUSTOMER_CATEGORY_NAME,  " +
					"					                 BILLED_AMOUNT,ADJ_Sales,  " +
					"					                 COLLECTED_BILLED_AMOUNT,  " +
					"					                 COLLECTED_SURCHARGE,  " +
					"					                 WAIVER  " +
					"					            FROM (SELECT aa.CUSTOMER_ID,  " +
					"					                         CUSTOMER_CATEGORY,DECODE(CUSTOMER_CATEGORY,'01', 'Domestic (PVT.)','02','Domestic (GOVT.)')  " +
					"					                         CUSTOMER_CATEGORY_NAME,  " +
					"					                         BILLED_AMOUNT,ADJ_Sales,  " +
					"					                         COLLECTED_BILLED_AMOUNT,  " +
					"					                         COLLECTED_SURCHARGE  " +
					"					                    FROM (  select cc.CUSTOMER_ID,substr(cc.CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales from(  " +
					"					                    select pp.customer_id,CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales from( " +
					"                                        SELECT CUSTOMER_ID,SUBSTR (CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY, SUM (NVL (TOTAL_AMOUNT, 0)) BILLED_AMOUNT FROM SALES_REPORT " +
					"                                        WHERE TO_NUMBER (billing_year || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+" " +
					"                                        AND SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"                                        GROUP BY CUSTOMER_ID) pp, " +
					"                                        (select CUSTOMER_ID,sum(nvl(BILLED_AMOUNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"                                        AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"                                        group by CUSTOMER_ID) qq " +
					"                                        where pp.customer_id=qq.customer_id(+)) aa,  " +
					"					                    customer_connection cc   " +
					"					                   where cc.customer_id=aa.CUSTOMER_ID(+)  " +
					"					                    AND substr(cc.customer_id,1,2)= '"+area+"'  " +
					"					                    AND ismetered=0 AND status=0) aa,  " +
					"					                         (SELECT CUSTOMER_ID,  " +
					"					                                   SUM (NVL (DEBIT, 0))-SUM (NVL (SURCHARGE, 0))-SUM (NVL (MISCELLANEOUS, 0))  " +
					"					                                      COLLECTED_BILLED_AMOUNT,  " +
					"					                                   SUM (NVL (SURCHARGE, 0))  " +
					"					                                      COLLECTED_SURCHARGE  " +
					"					                              FROM bank_account_ledger  " +
					"					                             WHERE     TRANS_DATE BETWEEN TO_DATE (  " +
					"					                                                                  '"+from_date+"',  " +
					"					                                                                  'dd-mm-yyyy')  " +
					"					                                                           AND TO_DATE (  " +
					"					                                                                  '"+to_date+"',  " +
					"					                                                                  'dd-mm-yyyy')  " +
					"					                                   AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) AND SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"					                         GROUP BY CUSTOMER_ID) bb  " +
					"					                   WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) aa,  " +
					"					                 (  SELECT customer_id,  " +
					"					                           SUM (NVL (TOTAL_COLLECTED_AMOUNT, 0)) WAIVER  " +
					"					                      FROM bill_collection_non_metered  " +
					"					                     WHERE     bank_id = '1'  " +
					"					                           AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"',  " +
					"					                                                                'dd-mm-rrrr')  " +
					"					                                                   AND TO_DATE ('"+to_date+"',  " +
					"					                                                                'dd-mm-rrrr')  " +
					"					                  GROUP BY customer_id) bb  " +
					"					           WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) tab1,  " +
					"					         (  SELECT bqc.CUSTOMER_ID CUSTOMER_ID,  " +
					"					                   MCI.FULL_NAME FULL_NAME,  " +
					"					                   MCI.ADDRESS_LINE1 ADDRESS,  " +
					"					                   bqc.NEW_DOUBLE_BURNER_QNT NEW_DOUBLE_BURNER_QNT,  " +
					"					                   bqc.NEW_DOUBLE_BURNER_QNT_BILLCAL  " +
					"					                      NEW_DOUBLE_BURNER_QNT_BILLCAL  " +
					"					              FROM BURNER_QNT_CHANGE BQC, MVIEW_CUSTOMER_INFO MCI  " +
					"					             WHERE     PID IN  " +
					"					                          (  SELECT MAX (PID)  " +
					"					                               FROM BURNER_QNT_CHANGE  " +
					"					                              WHERE     SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"					                                    AND effective_date <=  " +
					"					                                           TO_DATE ('"+to_date+"', 'dd-MM-YYYY')  " +
					"					                           GROUP BY CUSTOMER_ID)  " +
					"					                   AND MCI.CUSTOMER_ID = BQC.CUSTOMER_ID  " +
					"					          ORDER BY CUSTOMER_ID DESC) tab2  " +
					"					   WHERE TAB1.CUSTOMER_ID = TAB2.CUSTOMER_ID  " +
					"					ORDER BY TAB1.CUSTOMER_ID ) TA, " +
					"					(select customer_id cust,sum(nvl(debit,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"                    where trans_type=666  " +
					"                    AND account_no<>'420420'  " +
					"                    AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  " +
					"                    group by customer_id) TB " +
					"                    WHERE TA.customer_id=TB.cust(+) " +
					"                    AND SUBSTR(TA.CUSTOMER_ID,3,2)='"+customer_category+"' " +
					"                    ORDER BY TA.CUSTOMER_ID " ;

					
			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("BAL_F"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("COLLECTED_SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTED_BILLED_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("bal_next"));
        		balanceSheetDTO.setBurner(resultSet.getDouble("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
        		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("COLLECTED_SURCHARGE"));
        		balanceSheetDTO.setWaiver(resultSet.getDouble("WAIVER"));
        		balanceSheetDTO.setSalesAdjustment(resultSet.getDouble("ADJ_Sales"));
        		balanceSheetDTO.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}
	
	private ArrayList<BalanceSheetDTO> getAreawiseBalanceSheetInfo(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		//PreparedStatement ps1=null;
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="select AREA,SUM (NVL (bal_f, 0)) FIRST_Bal,SUM (NVL (BILLED_AMOUNT, 0)) BILLED_AMOUNT,  " +
					"SUM (NVL (SURCHARGE, 0)) SURCHARGE,SUM (NVL (DEBIT, 0)) COLLECTION_AMOUNT,SUM (NVL (METER_RENT, 0)) METER_RENT,  " +
					"SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL,SUM (NVL (VAT_REBATE_AMOUNT, 0)) VAT_REBATE_AMOUNT,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (bal_n, 0)) LAST_Bal from(  " +
					"SELECT CUSTOMER_ID,getLedgerBal (CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL, " +
					"VAT_REBATE_AMOUNT,getLedgerBalClosing (CUSTOMER_ID, '"+to_date+"') bal_n,AREA FROM( " +
					"SELECT CC.CUSTOMER_ID,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT,SUBSTR (CC.CUSTOMER_ID,1,2) AREA FROM( " +
					"Select tab1.customer_id,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT from( " +
					"select aa.customer_id,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL from( " +
					"select CUSTOMER_ID,sum(nvl(DEBIT,0))-sum(nvl(METER_RENT,0))-sum(nvl(SURCHARGE,0))-sum(nvl(MISCELLANEOUS,0)) DEBIT,sum(nvl(METER_RENT,0)) METER_RENT,sum(nvl(SURCHARGE,0)) SURCHARGE from bank_Account_ledger  " +
					"WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy') AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) group by customer_id) aa, " +
					"(Select CUSTOMER_ID,sum(nvl(TAX_AMOUNT,0)) TAX_AMOUNT,sum(nvl(HHV_NHV_BILL,0)) HHV_NHV_BILL  from bill_collection_metered BCM,summary_margin_pb PB " +
					"WHERE BCM.BILL_ID=PB.BILL_ID " +
					"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy')  group by customer_id) bb " +
					"where aa.CUSTOMER_ID=bb.CUSTOMER_ID(+)) tab1, " +
					"(SELECT CUSTOMER_ID,SUM (TOTAL_AMOUNT) BILLED_AMOUNT,SUM (VAT_REBATE) VAT_REBATE_AMOUNT FROM sales_report "+
                            "WHERE TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN  "+fromYearMonth+" AND "+toYearMonth+" " +
                            "GROUP BY CUSTOMER_ID) tab2 " +
					"WHERE TAB1.CUSTOMER_ID=tab2.CUSTOMER_ID(+)) t1,CUSTOMER_CONNECTION CC " +
					"WHERE CC.CUSTOMER_ID = T1.CUSTOMER_ID(+) " +
					"AND CC.ISMETERED=1) " +
					"union all " +
					"SELECT CUSTOMER_ID,bal_f,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, HHV_NHV_BILL,VAT_REBATE_AMOUNT, bal_n, AREA from( " +
					"SELECT aa.CUSTOMER_ID,getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,DEBIT,null METER_RENT,SURCHARGE,null TAX_AMOUNT,null HHV_NHV_BILL,null VAT_REBATE_AMOUNT,getLedgerBal (aa.CUSTOMER_ID, '"+to_date+"') bal_n, " +
					" SUBSTR (aa.customer_id, 1, 2) AREA  FROM (   " +
					"SELECT cc.CUSTOMER_ID,BILLED_AMOUNT from( " +
					"SELECT CUSTOMER_ID,SUBSTR (CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY, SUM (NVL (TOTAL_AMOUNT, 0)) BILLED_AMOUNT " +
					"                                      FROM SALES_REPORT " +
					"                                     WHERE     TO_NUMBER ( " +
					"                                                     billing_year " +
					"                                                  || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+" " +
					"                                  GROUP BY CUSTOMER_ID)tt,customer_connection cc " +
					"where tt.CUSTOMER_ID(+)=CC.CUSTOMER_ID " +
					"AND ISMETERED=0) aa, " +
					"(SELECT CUSTOMER_ID,SUM(NVL (DEBIT, 0))-SUM(NVL (SURCHARGE, 0)) DEBIT, SUM (NVL (SURCHARGE, 0)) SURCHARGE " +
					"FROM bank_account_ledger WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy') " +
					"AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) " +
					"GROUP BY CUSTOMER_ID) bb " +
					"WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) " +
					") group by AREA " +
					"order by AREA " ;

			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		
        		
        		balanceSheetDTO.setArea_id(resultSet.getString("AREA"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("FIRST_Bal"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("LAST_Bal"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}
	
private ArrayList<BalanceSheetDTO> getCategorywiseBalanceSheetInfo(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		String area=loggedInUser.getArea_id();
		//PreparedStatement ps1=null;
		
		try {
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
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="SELECT TA.CUSTOMER_CATEGORY,CATEGORY_NAME,SUBCATE,bal_first,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE FROM(  " +
					"					SELECT CUSTOMER_CATEGORY,CATEGORY_NAME,substr(CATEGORY_NAME,1,3) SUBCATE,bal_first,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next FROM(   " +
					"					SELECT decode(AA.CUSTOMER_CATEGORY,null,BB.CUSTOMER_CATEGORY,AA.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,DECODE   " +
					"					(BB.CUSTOMER_CATEGORY,'01', 'Domestic Pvt','02','Domestic Govt','03','Commercial Pvt','04','Commercial Govt','05','Industry Pvt','06','Industry Govt','07','Captive Pvt','09','CNG Pvt','11','Power Pvt','12','Power Govt')  " +
					"					CATEGORY_NAME,bal_first,BILLED_AMOUNT,ADJ_Sales,DEBIT-NVL(HHV_NHV_BILL,0) DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next FROM(   " +
					"					select t1.CUSTOMER_CATEGORY CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales,nvl(DEBIT,0)-nvl(ADJUSTMENT_AMOUNT,0) DEBIT,METER_RENT,nvl(SURCHARGE,0)+nvl(ADJUSTMENT_AMOUNT,0) SURCHARGE,nvl(VAT_REBATE_AMOUNT,0)+nvl(WAIVER,0) VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL from( " +
					"					select decode(tab2.CUSTOMER_CATEGORY,null,tab1.CUSTOMER_CATEGORY,tab2.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT from(   " +
					"					select substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (DEBIT, 0)) - SUM (NVL (METER_RENT, 0))- SUM (NVL (SURCHARGE, 0))  - SUM (NVL (MISCELLANEOUS, 0))  DEBIT, SUM (NVL (METER_RENT, 0)) METER_RENT, SUM (NVL (SURCHARGE, 0)) SURCHARGE    " +
					"					from bank_account_ledger  " +
					"					WHERE substr(CUSTOMER_ID,1,2)='"+area+"'  " +
					"					AND TRANS_DATE between to_date('"+from_date+"','dd-mm-yyyy') AND to_date('"+to_date+"','dd-mm-yyyy')  " +
					"					AND TRANS_TYPE in (1,7) AND account_no<>'1'  " +
					"					group by substr(CUSTOMER_ID,3,2)) tab1,(select pp.CUSTOMER_CATEGORY,BILLED_AMOUNT,ADJ_Sales,VAT_REBATE_AMOUNT from( " +
					"                   SELECT SUBSTR (CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY, SUM (NVL (TOTAL_AMOUNT, 0)) BILLED_AMOUNT,SUM (nvl(VAT_REBATE,0)) VAT_REBATE_AMOUNT FROM SALES_REPORT " +
					"                   WHERE TO_NUMBER (billing_year || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+" AND "+toYearMonth+" " +
					"                   AND SUBSTR (customer_id, 1, 2) = '"+area+"'  " +
					"                   GROUP BY substr(CUSTOMER_ID, 3, 2)) pp, " +
					"                   (select substr(CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY,sum(nvl(BILLED_AMOUNT,0)+nvl(HHV_NHV_ADJ_QNT,0)) ADJ_Sales from sales_adjustment where substr(customer_id,1,2)='"+area+"' " +
					"                   AND EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-rrrr') AND TO_DATE ('"+to_date+"','DD-MM-YYYY') " +
					"                   group by substr(CUSTOMER_ID, 3, 2)) qq " +
					"                   where pp.CUSTOMER_CATEGORY=qq.CUSTOMER_CATEGORY(+)) tab2   " +
					"					WHERE tab1.CUSTOMER_CATEGORY=tab2.CUSTOMER_CATEGORY) t1,  " +
					"					(select decode(tab1.CUSTOMER_CATEGORY,null,tab2.CUSTOMER_CATEGORY,tab1.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,TAX_AMOUNT,HHV_NHV_BILL,WAIVER,ADJUSTMENT_AMOUNT from(   " +
					"					select  CUSTOMER_CATEGORY,TAX_AMOUNT,nvl(HHV_NHV_BILL,0)+nvl(HHV_NHV_ADJ_QNT,0) HHV_NHV_BILL,ADJUSTMENT_AMOUNT from(   " +
					"					SELECT substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL,sum(nvl(ADJUSTMENT_AMOUNT,0)) ADJUSTMENT_AMOUNT    " + 
					"					FROM bill_collection_metered BCM,summary_margin_pb PB     " +  
					"					WHERE BCM.BILL_ID = PB.BILL_ID AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')       " +
					"					AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"' GROUP BY substr(CUSTOMER_ID,3,2)) ee,   " +
					"					(select SUBSTR (sa.CUSTOMER_ID, 3, 2) cat,sum(nvl(HHV_NHV_ADJ_QNT,0)) HHV_NHV_ADJ_QNT from sales_adjustment sa,bill_collection_metered bcm   " +
					"					where sa.customer_id=bcm.customer_id   " +
					"					AND sa.bill_id=bcm.bill_id   " +
					"					AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')   " +
					"					AND SUBSTR (sa.CUSTOMER_ID, 1, 2) = '"+area+"' group by SUBSTR (sa.CUSTOMER_ID, 3, 2)) ff   " +
					"					where ee.CUSTOMER_CATEGORY=ff.cat(+)) tab1,  " +
					"					(select substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (collected_bill_amount, 0)) WAIVER from bill_collection_non_metered   " +
					"					where account_no='1'   " +
					"					AND substr(customer_id,1,2)='"+area+"' " +
					"					AND COLLECTION_DATE between to_date('"+from_date+"','dd-mm-yyyy') AND to_date('"+to_date+"','dd-mm-yyyy')  " +
					"					GROUP BY substr(CUSTOMER_ID,3,2)) tab2  " +
					"					where tab1.CUSTOMER_CATEGORY=tab2.CUSTOMER_CATEGORY(+)) t2  " +
					"					where t1.CUSTOMER_CATEGORY=t2.CUSTOMER_CATEGORY(+)) AA,  " +
					"					(select CUSTOMER_CATEGORY,sum(nvl(bal_first,0)) bal_first,sum(nvl(bal_next,0)) bal_next from(   " +
					"					select substr(customer_id,3,2) CUSTOMER_CATEGORY,getLedgerBal(customer_id,'"+from_date+"') bal_first,getLedgerBalClosing (customer_id,'"+to_date+"') bal_next from customer_connection  " +
					"					where substr(customer_id,1,2)='"+area+"')  " +
					"					group by CUSTOMER_CATEGORY) BB  " +
					"					WHERE AA.CUSTOMER_CATEGORY(+)=BB.CUSTOMER_CATEGORY)) TA,  " +
					"					(select substr(customer_id,3,2) CUSTOMER_CATEGORY,sum(nvl(DEBIT,0))-sum(nvl(METER_RENT,0))-sum(nvl(SURCHARGE,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger  " +
					"					where trans_type=666  " +
					"					AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') " +
					"					AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"' " +
					"					group by substr(customer_id,3,2)) TB  " +
					"					WHERE TA.CUSTOMER_CATEGORY=TB.CUSTOMER_CATEGORY(+) " +
					"					ORDER BY CUSTOMER_CATEGORY " ;

					
					
					
					
					
					
					
					
					
					
					/*"SELECT TA.CUSTOMER_CATEGORY,CATEGORY_NAME,SUBCATE,bal_first,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next,SECURITY_ADJ,SEC_METER_RENT,SEC_SURCHARGE FROM( " +
					"SELECT CUSTOMER_CATEGORY,CATEGORY_NAME,substr(CATEGORY_NAME,1,3) SUBCATE,bal_first,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next FROM(  " +
					"SELECT decode(AA.CUSTOMER_CATEGORY,null,BB.CUSTOMER_CATEGORY,AA.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,DECODE  " +
					"(BB.CUSTOMER_CATEGORY,'01', 'Domestic Pvt','02','Domestic Govt','03','Commercial Pvt','04','Commercial Govt','05','Industry Pvt','06','Industry Govt','07','Captive Pvt','09','CNG Pvt','11','Power Pvt','12','Power Govt')  " +
					"CATEGORY_NAME,bal_first,BILLED_AMOUNT,DEBIT-NVL(HHV_NHV_BILL,0) DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL,bal_next FROM(  " +
					"select t1.CUSTOMER_CATEGORY CUSTOMER_CATEGORY,BILLED_AMOUNT,nvl(DEBIT,0)-nvl(ADJUSTMENT_AMOUNT,0) DEBIT,METER_RENT,nvl(SURCHARGE,0)+nvl(ADJUSTMENT_AMOUNT,0) SURCHARGE,nvl(VAT_REBATE_AMOUNT,0)+nvl(WAIVER,0) VAT_REBATE_AMOUNT,TAX_AMOUNT,HHV_NHV_BILL from(  " +
					"select decode(tab2.CUSTOMER_CATEGORY,null,tab1.CUSTOMER_CATEGORY,tab2.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,VAT_REBATE_AMOUNT from(  " +
					"select substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (DEBIT, 0)) - SUM (NVL (METER_RENT, 0))- SUM (NVL (SURCHARGE, 0))  - SUM (NVL (MISCELLANEOUS, 0))  DEBIT, SUM (NVL (METER_RENT, 0)) METER_RENT, SUM (NVL (SURCHARGE, 0)) SURCHARGE   " +
					"from bank_account_ledger  " +
					"WHERE substr(CUSTOMER_ID,1,2)='"+area+"'  " +
					"AND TRANS_DATE between to_date('"+from_date+"','dd-mm-yyyy') AND to_date('"+to_date+"','dd-mm-yyyy')  " +
					"AND TRANS_TYPE in (1,7) AND account_no<>'1'  " +
					" group by substr(CUSTOMER_ID,3,2)) tab1,(SELECT substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY, SUM (nvl(TOTAL_AMOUNT,0)) BILLED_AMOUNT,SUM (nvl(VAT_REBATE,0)) VAT_REBATE_AMOUNT FROM sales_report   " +
					"WHERE SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"'   " +
					"AND TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+" " +
					"GROUP BY substr(CUSTOMER_ID,3,2)) tab2  " +
					"WHERE tab1.CUSTOMER_CATEGORY=tab2.CUSTOMER_CATEGORY) t1,  " +
					"(select decode(tab1.CUSTOMER_CATEGORY,null,tab2.CUSTOMER_CATEGORY,tab1.CUSTOMER_CATEGORY) CUSTOMER_CATEGORY,TAX_AMOUNT,HHV_NHV_BILL,WAIVER,ADJUSTMENT_AMOUNT from(  " +
					"SELECT substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL ,sum(nvl(ADJUSTMENT_AMOUNT,0)) ADJUSTMENT_AMOUNT   " +
					"FROM bill_collection_metered BCM,summary_margin_pb PB    " +
					"WHERE BCM.BILL_ID = PB.BILL_ID    " +
					"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy')    " +
					"AND SUBSTR (CUSTOMER_ID, 1, 2) = '"+area+"'    " +
					"GROUP BY substr(CUSTOMER_ID,3,2)) tab1,  " +
					"(select substr(CUSTOMER_ID,3,2) CUSTOMER_CATEGORY,SUM (NVL (DEBIT, 0)) WAIVER from bank_account_ledger  " +
					"where account_no='1'  " +
					"AND substr(customer_id,1,2)='"+area+"'  " +
					"AND TRANS_DATE between to_date('"+from_date+"','dd-mm-yyyy') AND to_date('"+to_date+"','dd-mm-yyyy')  " +
					"GROUP BY substr(CUSTOMER_ID,3,2)) tab2  " +
					"where tab1.CUSTOMER_CATEGORY=tab2.CUSTOMER_CATEGORY(+)) t2  " +
					"where t1.CUSTOMER_CATEGORY=t2.CUSTOMER_CATEGORY(+)) AA,  " +
					"(select CUSTOMER_CATEGORY,sum(nvl(bal_first,0)) bal_first,sum(nvl(bal_next,0)) bal_next from(  " +
					"select substr(customer_id,3,2) CUSTOMER_CATEGORY,getLedgerBal(customer_id,'"+from_date+"') bal_first,getLedgerBalClosing (customer_id,'"+to_date+"') bal_next from customer_connection  " +
					"where substr(customer_id,1,2)='"+area+"')  " +
					"group by CUSTOMER_CATEGORY) BB  " +
					"WHERE AA.CUSTOMER_CATEGORY(+)=BB.CUSTOMER_CATEGORY)) TA, " +
					"(select substr(customer_id,3,2) CUSTOMER_CATEGORY,sum(nvl(CREDIT,0))-sum(nvl(METER_RENT,0))-sum(nvl(SURCHARGE,0)) SECURITY_ADJ,sum(nvl(METER_RENT,0)) SEC_METER_RENT,sum(nvl(SURCHARGE,0)) SEC_SURCHARGE from bank_account_ledger " +
					"where trans_type=666 " +
					"AND TRANS_DATE BETWEEN TO_DATE('"+from_date+"','DD-MM-YYYY') AND TO_DATE('"+to_date+"','DD-MM-YYYY') "+
					"AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"' "+
					"group by substr(customer_id,3,2)) TB " +
					"WHERE TA.CUSTOMER_CATEGORY=TB.CUSTOMER_CATEGORY(+) " +
					"ORDER BY CUSTOMER_CATEGORY " ;*/

											
			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CATEGORY_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_first"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("DEBIT"));
        		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("SURCHARGE"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("bal_next"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		balanceSheetDTO.setSubCategory(resultSet.getString("SUBCATE"));
        		balanceSheetDTO.setSecurityCollection(resultSet.getDouble("SECURITY_ADJ"));
        		balanceSheetDTO.setSecurityMeterRent(resultSet.getDouble("SEC_METER_RENT"));
        		balanceSheetDTO.setSecuritySurcharge(resultSet.getDouble("SEC_SURCHARGE"));
        		balanceSheetDTO.setSalesAdjustment(resultSet.getDouble("ADJ_Sales"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return balanceSheetList;
	}

private ArrayList<BalanceSheetDTO> getCategorywiseFullBalanceSheetInfo(){
	
	ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
	//PreparedStatement ps1=null;
	
	try {
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
		
		
		 	
        	ResultSet resultSet;
		
		
		
		String disconnection_info_sql="select CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,SUBSTR (CUSTOMER_CATEGORY_NAME, 1, 3) SUBCATE,SUM (NVL (bal_f, 0)) FIRST_Bal,SUM (NVL (BILLED_AMOUNT, 0)) BILLED_AMOUNT,  " +
				"SUM (NVL (SURCHARGE, 0)) SURCHARGE,SUM (NVL (DEBIT, 0)) COLLECTION_AMOUNT,SUM (NVL (METER_RENT, 0)) METER_RENT,  " +
				"SUM (NVL (HHV_NHV_BILL, 0)) HHV_NHV_BILL,SUM (NVL (VAT_REBATE_AMOUNT, 0)) VAT_REBATE_AMOUNT,SUM (NVL (TAX_AMOUNT, 0)) TAX_AMOUNT,SUM (NVL (bal_n, 0)) LAST_Bal from(  " +
				"SELECT CUSTOMER_ID,getLedgerBal (CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL, " +
				"VAT_REBATE_AMOUNT,getLedgerBalClosing (CUSTOMER_ID, '"+to_date+"') bal_n,CUSTOMER_CATEGORY, " +
				"DECODE(CUSTOMER_CATEGORY,'01', 'Domestic (PVT.)','02','Domestic (GOVT.)','03','Commercial Pvt','04','Commercial Govt','05','Industry Pvt','06','Industry Govt','07','Captive Pvt','09','CNG Pvt','11','Power Pvt','12','Power Govt' " +
				") CUSTOMER_CATEGORY_NAME FROM( " +
				"SELECT CC.CUSTOMER_ID,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT,SUBSTR (CC.CUSTOMER_ID,3,2) CUSTOMER_CATEGORY FROM( " +
				"Select tab1.customer_id,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL,VAT_REBATE_AMOUNT from( " +
				"select aa.customer_id,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT,HHV_NHV_BILL from( " +
				"select CUSTOMER_ID,sum(nvl(DEBIT,0))-sum(nvl(METER_RENT,0))-sum(nvl(SURCHARGE,0))-sum(nvl(MISCELLANEOUS,0)) DEBIT,sum(nvl(METER_RENT,0)) METER_RENT,sum(nvl(SURCHARGE,0)) SURCHARGE from bank_Account_ledger  " +
				"WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy') AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) group by customer_id) aa, " +
				"(Select CUSTOMER_ID,sum(nvl(TAX_AMOUNT,0)) TAX_AMOUNT,sum(nvl(HHV_NHV_BILL,0)) HHV_NHV_BILL  from bill_collection_metered BCM,summary_margin_pb PB " +
				"WHERE BCM.BILL_ID=PB.BILL_ID " +
				"AND COLLECTION_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-mm-yyyy') AND TO_DATE ('"+to_date+"', 'dd-mm-yyyy')  group by customer_id) bb " +
				"where aa.CUSTOMER_ID=bb.CUSTOMER_ID(+)) tab1, " +
				"(SELECT CUSTOMER_ID,SUM (TOTAL_AMOUNT) BILLED_AMOUNT,SUM (VAT_REBATE) VAT_REBATE_AMOUNT FROM sales_report "+
                            "WHERE TO_NUMBER (billing_year || LPAD (billING_month, 2, 0)) BETWEEN  "+fromYearMonth+" AND "+toYearMonth+" " +
                            "GROUP BY CUSTOMER_ID) tab2 " +
				"WHERE TAB1.CUSTOMER_ID=tab2.CUSTOMER_ID(+)) t1,CUSTOMER_CONNECTION CC " +
				"WHERE CC.CUSTOMER_ID = T1.CUSTOMER_ID(+) " +
				"AND CC.ISMETERED=1) " +
				"union all " +
				"SELECT CUSTOMER_ID,bal_f,BILLED_AMOUNT,DEBIT,METER_RENT,SURCHARGE,TAX_AMOUNT, HHV_NHV_BILL,VAT_REBATE_AMOUNT, bal_n, CUSTOMER_CATEGORY, " +
				"DECODE(CUSTOMER_CATEGORY,'01', 'Domestic (PVT.)','02','Domestic (GOVT.)','03','Commercial Pvt','04','Commercial Govt','05','Industry Pvt','06','Industry Govt','07','Captive Pvt','09','CNG Pvt','11','Power Pvt','12','Power Govt' " +
				") CUSTOMER_CATEGORY_NAME  from( " +
				"SELECT aa.CUSTOMER_ID,getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f,BILLED_AMOUNT,DEBIT,null METER_RENT,SURCHARGE,null TAX_AMOUNT,null HHV_NHV_BILL,null VAT_REBATE_AMOUNT,getLedgerBalClosing (aa.CUSTOMER_ID, '"+to_date+"') bal_n, " +
				" SUBSTR (aa.customer_id, 3, 2) CUSTOMER_CATEGORY  FROM (   " +
				"SELECT cc.CUSTOMER_ID,BILLED_AMOUNT from( " +
				"SELECT CUSTOMER_ID,SUBSTR (CUSTOMER_ID, 3, 2) CUSTOMER_CATEGORY, SUM (NVL (TOTAL_AMOUNT, 0)) BILLED_AMOUNT " +
					"                                      FROM SALES_REPORT " +
					"                                     WHERE     TO_NUMBER ( " +
					"                                                     billing_year " +
					"                                                  || LPAD (billing_month, 2, 0)) BETWEEN "+fromYearMonth+"   AND  "+toYearMonth+" " +
					"                                  GROUP BY CUSTOMER_ID)tt,customer_connection cc " +
				"where tt.CUSTOMER_ID(+)=CC.CUSTOMER_ID " +
				"AND ISMETERED=0) aa, " +
				"(SELECT CUSTOMER_ID,SUM(NVL (DEBIT, 0))-SUM(NVL (SURCHARGE, 0)) DEBIT, SUM (NVL (SURCHARGE, 0)) SURCHARGE " +
				"FROM bank_account_ledger WHERE TRANS_DATE BETWEEN TO_DATE ('"+from_date+"','dd-mm-yyyy') AND TO_DATE ('"+to_date+"','dd-mm-yyyy') " +
				"AND ACCOUNT_NO not in('1','420420') AND TRANS_TYPE in (1,7) " +
				"GROUP BY CUSTOMER_ID) bb " +
				"WHERE aa.CUSTOMER_ID = bb.CUSTOMER_ID(+)) " +
				") group by CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME " +
				"order by CUSTOMER_CATEGORY " ;

				
		PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
	
    	
    	 resultSet=ps1.executeQuery();
    	
    	
    	while(resultSet.next())
    	{
    		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
    		
    		
    		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
    		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
    		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("FIRST_Bal"));
    		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
    		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
    		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
    		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("SURCHARGE"));
    		balanceSheetDTO.setEndingBalance(resultSet.getDouble("LAST_Bal"));
    		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
    		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
    		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
    		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
    		balanceSheetDTO.setSubCategory(resultSet.getString("SUBCATE"));
    		
    		balanceSheetList.add(balanceSheetDTO);
    	}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return balanceSheetList;
}


	
	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	
	public String getFiscal_year() {
		return fiscal_year;
	}

	public void setFiscal_year(String fiscal_year) {
		this.fiscal_year = fiscal_year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}


}
