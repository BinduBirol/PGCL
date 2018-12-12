package org.pgcl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;
import org.apache.struts2.ServletActionContext;
import org.pgcl.actions.BaseAction;
import org.pgcl.dto.AccountDTO;
import org.pgcl.dto.AddressDTO;
import org.pgcl.dto.BankDTO;
import org.pgcl.dto.BranchDTO;
import org.pgcl.dto.CustomerCategoryDTO;
import org.pgcl.dto.CustomerConnectionDTO;
import org.pgcl.dto.CustomerDTO;
import org.pgcl.dto.CustomerPersonalDTO;
import org.pgcl.dto.GasPurchaseDTO;
import org.pgcl.dto.MeterReadingDTO;
import org.pgcl.dto.NonMeterReportDTO;
import org.pgcl.dto.TariffDTO;
import org.pgcl.dto.TransactionDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.enums.Area;
import org.pgcl.enums.Month;
import org.pgcl.reports.ReportFormat;
import org.pgcl.reports.ReportUtil;
import org.pgcl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class CollectionBankStatement extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
	AccountDTO accountInfo=new AccountDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	    private  String bank_id;
	    private  String branch_id;
	    private  String account_no;
	    private  String collection_month;
	    private  String collection_year;
	    private  String collection_date;
	    private  String report_for; 
	    private  String area="01";
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public String execute() throws Exception
	{
				
		String fileName="Collection_Statement.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
//************************************ PGCL Logo *******************************************************
			
			
			/*Image img = Image.getInstance("H:/workspaceNew/PGCL/PGCL_WEB/resources/images/logo.png");
			img.scaleAbsolute(65, 79);
            img.setAbsolutePosition(100f, 510f);
            
            document.add(img);*/
			
            //************************************ PGCL Logo *******************************************************
            
			
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
			
			
			
			
			if(report_for.equals("date_wise")){
				generatePdfDate_wise(document);
			}else if(report_for.equals("month_wise")){
				generatePdfMonth_wise(document);
			}else if(report_for.equals("month_wiseDetails")){
				generatePdfMonth_wiseDetails(document);
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

	
	
	
	private void generatePdfDate_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("DATE WISE BANK STATEMENT [ACCOUNT NO. {"+account_no+"} COLLECTION DATED FOR{"+collection_date+"}]",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		double forwardBalanceDate=getForwardBalanceDatewise();
		
		accountInfo=getAccountInfo();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAc_opening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		
		
		
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalanceDate),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(7);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,10,30,13,14,14,14});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////
		
	
		
		
		
		
		
		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(6);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalanceDate),ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		/////////Date/////////
		
		
		pcell = new PdfPCell(new Paragraph("Dated : ",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(collection_date,ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(6);
		pdfPTable.addCell(pcell);
		
		transactionList=getTransactionListDateWise();
		int listSize=transactionList.size();
		double balance=forwardBalanceDate;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;
		double grandTotalDebit=0.0;
		double grandTotalCredit=0.0;
		
		String monthYear="";

		for(int i=0;i<listSize;i++)
		{
		
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getCustomer_id(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			monthYear=transactionList.get(i).getBillMonth().substring(0,2);
			
			if(monthYear.equals("Co")){
				
				pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBillMonth().substring(11),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
			}else if(monthYear.equals("By")){
				
				pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBillMonth().substring(8),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
			}else{
				pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBillMonth(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
			}
			
			
			
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	
	/////////////////////Month wise//////////////////////////
	
	private void generatePdfMonth_wise(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION STATEMENT ACCOUNT NO. {"+account_no+"} FOR THE MONTH "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		accountInfo=getAccountInfo();
		double forwardBalance=getForwardBalanceMonthwise();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAc_opening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(5);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{15,40,15,15,15});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Dated",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////
		
		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		transactionList=getCollectlistByMonth();
		int listSize=transactionList.size();
		double balance=forwardBalance;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;

		for(int i=0;i<listSize;i++)
		{
		
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getTrans_date().toString(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);			
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	
	//////////////////////////Details Collection///////////////////////////////
	
	private void generatePdfMonth_wiseDetails(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION STATEMENT ACCOUNT NO. {"+account_no+"} FOR THE MONTH "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		accountInfo=getAccountInfo();
		double forwardBalance=getForwardBalanceMonthwise();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getOpening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(7);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,10,30,10,15,15,15});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Month of Bill",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////
		
		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(6);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		transactionList=getMonthlyDetailsTransactionList();
		int listSize=transactionList.size();
		double balance=forwardBalance;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;
		double daywiseSubTotalDebit=0.0;
		double daywiseSubTotalCredit=0.0;
		double daywiseSubTotalBalance=0.0;
		String previousDate=new String("");

		for(int i=0;i<listSize;i++)
		{
			String currentDate=transactionList.get(i).getTrans_date();
			
			if(!currentDate.equals(previousDate)){
				
				if(i>0){
					pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(4);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalDebit),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalCredit),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalBalance),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					daywiseSubTotalDebit=0.0;
					daywiseSubTotalCredit=0.0;
					daywiseSubTotalBalance=0.0;
				}
				
				pcell = new PdfPCell(new Paragraph(transactionList.get(i).getTrans_date(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pdfPTable.addCell(pcell);
				
				
			}
		
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getCustomer_id(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBillMonth(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			if(currentDate.equals(previousDate)){
				daywiseSubTotalDebit=daywiseSubTotalDebit+transactionList.get(i).getDebit();
				daywiseSubTotalCredit=daywiseSubTotalCredit+transactionList.get(i).getCredit();
				daywiseSubTotalBalance=balance;
			}else if(!currentDate.equals(previousDate)){
				daywiseSubTotalDebit=daywiseSubTotalDebit+transactionList.get(i).getDebit();
				daywiseSubTotalCredit=daywiseSubTotalCredit+transactionList.get(i).getCredit();
			}
			
			
			
			previousDate=transactionList.get(i).getTrans_date();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(4);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalDebit),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalCredit),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalBalance),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
			}
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	////////////////////////End of Details Collection/////////////////////////////
	

	private Double getForwardBalanceMonthwise()
	{
		int closingMonth=Integer.valueOf(collection_month);
		int closingYear=Integer.valueOf(collection_year);
		
		int tempMonth=0;
		int tempYear=0;
		double openingBalance=0.0; 
		
		
		try {
			if(closingMonth==1){
				tempMonth=12;
				tempYear=closingYear-1;
			}else if(closingMonth>1){
				tempMonth=closingMonth-1;
				tempYear=closingYear;
			}
			
			String account_info_sql= "select get_opening_balance ('"+bank_id+"','"+branch_id+"','"+account_no+"','"+tempMonth+"','"+tempYear+"') BALANCE from dual " ;

			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalance=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return openingBalance;
	}
	
	private Double getForwardBalanceDatewise()
	{
		double openingBalanceDatewise=0.0; 
		
//		int closingDate=Integer.valueOf(collection_date);
//		int closingMonth=Integer.valueOf(collection_month);
//		int closingYear=Integer.valueOf(collection_year);
//		int tempDate;
//		int tempMonth;
//		int tempYear;
		
		try {
			
//			if(closingDate==1 && closingMonth==1){
//				tempDate=31;
//				tempMonth=12;
//				tempYear=closingYear-1;
//			}else if(closingDate>1){
//				tempDate=closingDate-1;
//				tempMonth=closingMonth;
//				tempYear=closingYear;
//			}
		
			String account_info_sql="SELECT BALANCE "+
					"  FROM BANK_ACCOUNT_LEDGER "+
					" WHERE TRANS_ID IN "+
					"          (SELECT MAX (TRANS_ID) TRANS_ID "+
					"             FROM BANK_ACCOUNT_LEDGER "+
					"            WHERE     TRANS_DATE IN "+
					"                         (SELECT MAX (TRANS_DATE) "+
					"                            FROM BANK_ACCOUNT_LEDGER "+
					"                           WHERE TRANS_DATE <to_date('"+collection_date+"','dd/mm/yyyy') "+
					"                                 AND branch_id = '"+branch_id+"' "+
					"                                 AND ACCOUNT_NO = '"+account_no+"' "+
					"                                 AND Status = 1) "+
					"                  AND branch_id = '"+branch_id+"' "+
					"                  AND ACCOUNT_NO = '"+account_no+"' "+
					"                  AND Status = 1) ";




			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalanceDatewise=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return openingBalanceDatewise;
	}
	
	
	private AccountDTO getAccountInfo()
	 {
	           AccountDTO accountInfo=new AccountDTO();
	           BankDTO bankInfo=new BankDTO();
	           BranchDTO branchInfo=new BranchDTO();
	  
	  try {
	  
	   
	   String account_info_sql="SELECT MCI.ACCOUNT_NO,mci.ACCOUNT_NAME,to_char(mci.AC_OPENING_DATE) AC_OPENING_DATE,mbi.BANK_NAME,BRANCH_NAME,MBRI.ADDRESS,MBRI.PHONE,MBRI.FAX,MBRI.EMAIL " +
	     "  FROM MST_ACCOUNT_INFO mci, MST_BANK_INFO mbi, MST_BRANCH_INFO mbri " +
	     " WHERE     mci.bank_id = mbi.bank_id " +
	     "       AND mci.branch_id = mbri.branch_id " +
	     "       AND MCI.BANK_ID =? " +
	     "       AND MCI.branch_id =? " +
	     "       AND MCI.account_no =? " ;

	     
	   
	   PreparedStatement ps1=conn.prepareStatement(account_info_sql);
	   ps1.setString(1, bank_id);
	   ps1.setString(2, branch_id);
	   ps1.setString(3, account_no);
	  
	         
	         ResultSet resultSet=ps1.executeQuery();
	         
	         
	         while(resultSet.next())
	         {
	          
	          
	          accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
	          branchInfo.setAddress(resultSet.getString("ADDRESS"));
	          branchInfo.setPhone(resultSet.getString("PHONE"));
	          branchInfo.setFax(resultSet.getString("FAX"));
	          branchInfo.setEmail(resultSet.getString("EMAIL"));
	          accountInfo.setBranch_name(resultSet.getString("BRANCH_NAME"));
	          accountInfo.setBranch(branchInfo);
	          accountInfo.setAccount_name(resultSet.getString("ACCOUNT_NAME"));
	          accountInfo.setAccount_no(resultSet.getString("ACCOUNT_NO")) ; 
	          accountInfo.setAc_opening_date(resultSet.getString("AC_OPENING_DATE"));
	   
	          
	          
	          
	         }
	  } catch (Exception e) {
			e.printStackTrace();
		}
	  return accountInfo;
	 }
	
	
	
/*	private AccountDTO getAccountInfo()
	{
           AccountDTO accountInfo=new AccountDTO();
           BankDTO bankInfo=new BankDTO();
           BranchDTO branchInfo=new BranchDTO();
		
		try {
		
			
			String account_info_sql="SELECT MCI.ACCOUNT_NO,mci.ACCOUNT_NAME,mci.AC_OPENING_DATE,mbi.BANK_NAME,BRANCH_NAME,MBRI.ADDRESS,MBRI.PHONE,MBRI.FAX,MBRI.EMAIL " +
				     "  FROM MST_ACCOUNT_INFO mci, MST_BANK_INFO mbi, MST_BRANCH_INFO mbri " +
				     " WHERE     mci.bank_id = mbi.bank_id " +
				     "       AND mci.branch_id = mbri.branch_id " +
				     "       AND MCI.BANK_ID =? " +
				     "       AND MCI.branch_id =? " +
				     "       AND MCI.account_no =? " ;



			
			ps1=conn.prepareStatement(account_info_sql);
			ps1.setString(1, bank_id);
			ps1.setString(2, branch_id);
			ps1.setString(3, account_no);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
        		branchInfo.setAddress(resultSet.getString("ADDRESS"));
        		branchInfo.setPhone(resultSet.getString("PHONE"));
        		branchInfo.setFax(resultSet.getString("FAX"));
        		branchInfo.setEmail(resultSet.getString("EMAIL"));
        		accountInfo.setBranch_name(resultSet.getString("BRANCH_NAME"));
        		accountInfo.setBranch(branchInfo);
        		accountInfo.setAccount_name(resultSet.getString("ACCOUNT_NAME"));
        		accountInfo.setAccount_no(resultSet.getString("ACCOUNT_NO")) ; 
        		accountInfo.setOpening_balance(resultSet.getString("OPENING_BALANCE"));
        		accountInfo.setAc_opening_date(resultSet.getString("OPENING_DATE"));
   
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountInfo;
	}
	
	*/

	private ArrayList<TransactionDTO> getTransactionListDateWise()
	{
	ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
	CustomerDTO customer=new CustomerDTO();
	CustomerPersonalDTO cusPersonalInfo=new CustomerPersonalDTO();
	 
	
	String area=loggedInUser.getArea_id();
		
		try {
			
			
			String wClause="";
			
			if(account_no.equals("2916-01-0000300") || account_no.equals("01787685899-0501")){
				wClause=" AND TRANS_TYPE <> 2 AND ACCOUNT_NO = '"+account_no+"' AND substr(CUSTOMER_ID,1,2)='"+area+"' ";
			}else{
				wClause=" AND TRANS_TYPE <> 2 AND ACCOUNT_NO = '"+account_no+"' ";
			}
		
			
			
			String transaction_sql="SELECT TRANS_DATE, " +
				     "         tbl.CUSTOMER_ID, " +
				     "         DECODE (tbl.CUSTOMER_ID, NULL, PARTICULARS, FULL_NAME) PARTICULARS, " +
				     "         PARTICULARS BILL_MONTH, " +
				     "         DEBIT, " +
				     "         CREDIT, " +
				     "         INSERTED_ON " +
				     "    FROM (SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
				     "                 TRANS_ID, " +
				     "                 CUSTOMER_ID, " +
				     "                 PARTICULARS, " +
				     "                 DEBIT, " +
				     "                 CREDIT, " +
				     "                 INSERTED_ON " +
				     "            FROM BANK_ACCOUNT_LEDGER " +
				     "           WHERE     TO_CHAR (TRANS_DATE, 'dd-MM-yyyy') = '"+collection_date+"' " +wClause+
				     "          UNION ALL " +
				     "          SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
				     "                 TRANS_ID, " +
				     "                 CUSTOMER_ID, " +
				     "                 PARTICULARS, " +
				     "                 DEBIT, " +
				     "                 CREDIT, " +
				     "                 INSERTED_ON " +
				     "            FROM BANK_ACCOUNT_LEDGER " +
				     "           WHERE     TO_CHAR (TRANS_DATE, 'dd-MM-yyyy') = '"+collection_date+"' " +
				     "                 AND ACCOUNT_NO = '"+account_no+"' " +
				     "                 AND TRANS_TYPE = 2) tbl " +
				     "         LEFT OUTER JOIN MVIEW_CUSTOMER_INFO mcf " +
				     "            ON tbl.CUSTOMER_ID = mcf.CUSTOMER_ID " +
				     " ORDER BY INSERTED_ON,TRANS_DATE asc,TRANS_ID asc " ;
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setBillMonth(resultSet.getString("BILL_MONTH"));
        		transactionDto1.setParticulars(resultSet.getString("PARTICULARS"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto1.setCredit(resultSet.getDouble("CREDIT"));
        		
        		
        		
        		
        		
   
        		
        		transactionList.add(transactionDto1);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return transactionList;
	}
	
	private ArrayList<TransactionDTO> getCollectlistByMonth(){
		ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
		
		
		String area=loggedInUser.getArea_id();
		
		try {
			
			
			
		
			
			
			String transaction_sql="SELECT TRANS_DATE, " +
					"         PERTICULARS, " +
					"         DEBIT, " +
					"         CREDIT " +
					"    FROM (  " +
					"    SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"    SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Received Amount From Customer As Security Deposit', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                 CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 0" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'By  Bank Gas Bill Collection', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 1" +
					//"					AND SUBSTR(CUSTOMER_ID,1,2)='"+area+"'  "+
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                      PARTICULARS PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 2" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"           SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Receive Amount from Bank (Refund,Interest etc)', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 3" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"           SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'AMOUNT RECEIVE FROM BANK FOR STD ACCOUNT', " +
					"                           'AMOUNT TRANSFER TO STD ACCOUNT') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 4" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Interest Income', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 5" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE,PERTICULARS" +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Installment Collection', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 7" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE,PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                   PERTICULARS, " +
					"                   SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT " +
					"              FROM (SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
					"                             TRANS_ID, " +
					"                             DECODE (CREDIT, " +
					"                                     0, 'Security Adjustment', " +
					"                                     'Security Refund') " +
					"                                PERTICULARS, " +
					"                             DEBIT, " +
					"                             CREDIT " +
					"                        FROM BANK_ACCOUNT_LEDGER " +
					"                       WHERE     TO_CHAR (TRANS_DATE, 'MM') ="+collection_month+" " +
					"                             AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                             AND ACCOUNT_NO ='"+account_no+"' " +
					"                             AND TRANS_TYPE = 666 " +
					"                    ORDER BY TRANS_DATE ASC, TRANS_ID ASC) " +
					"                    GROUP BY TRANS_DATE, PERTICULARS) " +
					"		   ORDER BY TRANS_DATE " ;




			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto=new TransactionDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto.setParticulars(resultSet.getString("PERTICULARS"));
        		transactionDto.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto.setCredit(resultSet.getDouble("CREDIT"));       		
   
        		
        		transactionList.add(transactionDto);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionList;
	}
	

	private ArrayList<TransactionDTO> getMonthlyDetailsTransactionList()
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	
	String area=loggedInUser.getArea_id();
	String wClause="";
	if(account_no.equals("01787685899-0501") || account_no.equals("01787685899-0501")){
		wClause=" AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
				"                 AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " ;
				/*+
				"                 and substr(CUSTOMER_ID,1,2)='"+area+"' " ;*/
	}else{
		wClause=" AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
				"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " ;
	}
		
		try {
			
			String transaction_sql="SELECT TRANS_DATE, " +
					"         tbl.CUSTOMER_ID, " +
					"         DECODE (tbl.CUSTOMER_ID, NULL, PARTICULARS, FULL_NAME) PARTICULARS, " +
					"         DECODE (tbl.CUSTOMER_ID, " +
					"                 NULL, '', " +
					"                 REPLACE (PARTICULARS, 'Collection, ')) " +
					"            BILL_MONTH, " +
					"         DEBIT, " +
					"         CREDIT " +
					"    FROM (SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
					"                 TRANS_ID, " +
					"                 CUSTOMER_ID, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 CREDIT " +
					"            FROM BANK_ACCOUNT_LEDGER " +
					"           WHERE     ACCOUNT_NO =  '"+account_no+"' " +
					"                 AND TRANS_TYPE <> 2 " +wClause+
					"          UNION ALL " +
					"          SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
					"                 TRANS_ID, " +
					"                 CUSTOMER_ID, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 CREDIT " +
					"            FROM BANK_ACCOUNT_LEDGER " +
					"           WHERE  ACCOUNT_NO =  '"+account_no+"' " +
					"                 AND TRANS_TYPE = 2 " +wClause+
					"                 ) tbl " +
					"         LEFT OUTER JOIN MVIEW_CUSTOMER_INFO mcf " +
					"            ON tbl.CUSTOMER_ID = mcf.CUSTOMER_ID " +
					"ORDER BY TRANS_DATE ASC, TRANS_ID ASC " ;

					
					
					
					
					
					
					
					
//					" SELECT TRANS_DATE, " +
//					"        tbl.CUSTOMER_ID, " +
//					"        decode(tbl.CUSTOMER_ID,null,PARTICULARS,FULL_NAME) PARTICULARS, " +
//					"        decode(tbl.CUSTOMER_ID,null,'',REPLACE(PARTICULARS,'Collection, ') ) BILL_MONTH, " +
//					"        DEBIT, " +
//					"        CREDIT " +
//					"    FROM (  SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
//					"                   CUSTOMER_ID, " +
//					"                   PARTICULARS,                  " +
//					"                   DEBIT, " +
//					"                   CREDIT " +
//					"              FROM BANK_ACCOUNT_LEDGER " +
//					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//					"                   AND ACCOUNT_NO =  '"+account_no+"' " +
//					"                   AND TRANS_TYPE <> 2 " +
//					"           " +
//					"          UNION ALL " +
//					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
//					"                   CUSTOMER_ID, " +
//					"                   PARTICULARS, " +
//					"                   DEBIT, " +
//					"                   CREDIT " +
//					"              FROM BANK_ACCOUNT_LEDGER " +
//					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//					"                   AND ACCOUNT_NO =  '"+account_no+"' " +
//					"                   AND TRANS_TYPE = 2 " +
//					"         )tbl LEFT OUTER JOIN MVIEW_CUSTOMER_INFO mcf " +
//					"         ON tbl.CUSTOMER_ID = mcf.CUSTOMER_ID  " +
//					"          " +
//					" ORDER BY TRANS_DATE asc,TRANS_ID asc " ;


			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setBillMonth(resultSet.getString("BILL_MONTH"));
        		transactionDto1.setParticulars(resultSet.getString("PARTICULARS"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto1.setCredit(resultSet.getDouble("CREDIT"));
        		
        		
        		
        		
        		
   
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	
	
	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getCollection_month() {
		return collection_month;
	}

	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}

	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}

	public String getCollection_date() {
		return collection_date;
	}

	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
  }


