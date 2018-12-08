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
import org.pgcl.dto.DefaulterDto;
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
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class DefaulterCustomerList extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<DefaulterDto> defaulterList=new ArrayList<DefaulterDto>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
	    private  String criteria_type;
	    private  String month_number;
	    private  String customer_type;

	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		
		
		
		String fileName="DefaulterCustomerList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,60,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			DefaulterDto defaulterDto = new DefaulterDto();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
//************************************ PGCL Logo *******************************************************
			
			
			/*Image img = Image.getInstance("H:/workspaceNew/PGCL/PGCL_WEB/resources/images/logo.png");
            //img.scaleToFit(10f, 200f);
            //img.scalePercent(200f);
			img.scaleAbsolute(65, 79);
            img.setAbsolutePosition(100f, 510f);
            
            document.add(img);*/
			
            //************************************ PGCL Logo *******************************************************
            
			
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
			
			
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("DEFAULTER CUSTOMER LIST",ReportUtil.f11B));
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
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("As On "+Month.values()[Integer.valueOf(bill_month)-1]+", "+bill_year,ReportUtil.f11B));
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
			
			String symbol="";
			if(criteria_type.equals("lt")){
				symbol="less than";
			}else if(criteria_type.equals("gt")){
				symbol="greater than";
			}else if(criteria_type.equals("eq")){
				symbol="equal to";
			}else if(criteria_type.equals("gteq")){
				symbol="greater than equal to";
			}else if(criteria_type.equals("lteq")){
				symbol="less than equal to";
			}
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Due "+symbol+" "+month_number+" Months",ReportUtil.f11B));
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
			
			defaulterList =getDefaulterInformation();
			
			int totalRecordsPerCategory=0;
			int total_burner=0;
			float subTotal_amount=0;
			int subTotal_month=0;
			float grandTotal_amount=0;
			int grandTotal_month=0;
			int grandTotalCustomer=0;

			int expireListSize=defaulterList.size();
			String previousCustomerCategoryName=new String("");
			
			for(int i=0;i<expireListSize;i++)
			{
				defaulterDto = defaulterList.get(i);
				String currentCustomerCategoryName=defaulterDto.getCategory_name();
				
				if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
				{	
				
				if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
				{
					
					if(i>0)
					{
						pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(2);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
													

						pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(3);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(taka_format.format(subTotal_amount),ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(1);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(String.valueOf(subTotal_month),ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(1);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(1);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						document.add(ptable);
					    
						grandTotalCustomer=grandTotalCustomer+totalRecordsPerCategory;
						totalRecordsPerCategory=0;
						subTotal_amount=0;
                        subTotal_month=0;
						
					}
					
				}
				ptable = new PdfPTable(8);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{20,60,60,30,60,30,20,20});
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
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
								
				
				pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Address",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Contact No.",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Due Month",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Amount(Tk)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Total Month",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Status",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
				}
				
				
				
				pcell = new PdfPCell(new Paragraph(defaulterDto.getCustomer_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(defaulterDto.getFull_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(defaulterDto.getAddress(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
			
				pcell = new PdfPCell(new Paragraph(defaulterDto.getContact_no(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(defaulterDto.getDue_month(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				subTotal_amount=subTotal_amount+defaulterDto.getAmount();
				grandTotal_amount=grandTotal_amount+defaulterDto.getAmount();
				pcell = new PdfPCell(new Paragraph(taka_format.format(defaulterDto.getAmount()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				subTotal_month=subTotal_month+defaulterDto.getTotal_month();
				grandTotal_month=grandTotal_month+defaulterDto.getTotal_month();
				pcell = new PdfPCell(new Paragraph(String.valueOf(defaulterDto.getTotal_month()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(defaulterDto.getStatus(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
			
				previousCustomerCategoryName=defaulterDto.getCategory_name();
				totalRecordsPerCategory++;
				
				
			}
			/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
			pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
										

			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subTotal_amount),ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(subTotal_month),ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			grandTotalCustomer=grandTotalCustomer+totalRecordsPerCategory;
			
		
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			//// Grand total starts here
			pcell=new PdfPCell(new Paragraph("Grand Total: "+String.valueOf(grandTotalCustomer),ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
										

			pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(grandTotal_amount),ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(grandTotal_month),ReportUtil.f11B));
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
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	private ArrayList<DefaulterDto> getDefaulterInformation()
	{
	ArrayList<DefaulterDto> defaulterInfoList=new ArrayList<DefaulterDto>();

		
		try {
			
			String monyear=String.valueOf(bill_year)+""+"0"+String.valueOf(bill_month);
			int month_year=Integer.parseInt(monyear);
			String wClause="";
			String criteriaWclause="";
			String defaulterListSql="";
			if(report_for.equals("area_wise"))
			{
				wClause=" area_id="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause=" area_id="+area+" And category_id="+customer_category;
			}
			
			
			if(criteria_type.equals("lt"))
			{
				criteriaWclause=" And total_month<"+month_number;
			}else if(criteria_type.equals("gt"))
			{
				criteriaWclause=" And total_month>"+month_number;
			}else if(criteria_type.equals("eq"))
			{
				criteriaWclause=" And total_month="+month_number;
			}else if(criteria_type.equals("gteq"))
			{
				criteriaWclause=" And total_month>="+month_number;
			}else if(criteria_type.equals("lteq"))
			{
				criteriaWclause=" And total_month<="+month_number;
			}
			
			if(customer_type.equals("metered"))
			{
				 defaulterListSql="select * from  " +
						"( " +
						"SELECT billMiter.CUSTOMER_ID,mci.FULL_NAME,ADDRESS_LINE1 ADDRESS,mci.PHONE||','||mci.MOBILE CONTACT_NO,CATEGORY_ID,CATEGORY_NAME,mci.AREA_ID, " +
						"         LISTAGG ( " +
						"               TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') " +
						"            || ' ' " +
						"            || SUBSTR (BILL_YEAR, 3), " +
						"            ',') " +
						"         WITHIN GROUP (ORDER BY BILL_YEAR asc,BILL_MONTH asc) " +
						"            AS DUEMONTH, " +
						"         (SELECT SUM (BILLED_AMOUNT) " +
						"            FROM BILL_METERED " +
						"           WHERE CUSTOMER_ID = billMiter.CUSTOMER_ID AND STATUS = 1 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <="+month_year+") " +
						"            AS TOTAL_AMOUNT, " +
						"         (SELECT COUNT (BILL_MONTH) " +
						"            FROM BILL_METERED " +
						"           WHERE CUSTOMER_ID = billMiter.CUSTOMER_ID AND STATUS = 1 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <="+month_year+") " +
						"            AS TOTAL_MONTH, " +
						"            decode(CONNECTION_STATUS,'1','ACTIVE','INACTIVE') STATUS " +
						"    FROM BILL_METERED billMiter,MVIEW_CUSTOMER_INFO mci " +
						"   WHERE billMiter.CUSTOMER_ID=MCI.CUSTOMER_ID " +
						"   AND billMiter.STATUS = 1 " +
						"GROUP BY billMiter.CUSTOMER_ID, " +
						"mci.FULL_NAME,mci.PHONE||','||mci.MOBILE,CONNECTION_STATUS,CATEGORY_ID,CATEGORY_NAME,mci.AREA_ID,ADDRESS_LINE1 " +
						") tabl1 " +
						"where "+wClause+criteriaWclause+
						"AND TOTAL_AMOUNT is not null "+
						"order by customer_id " ;
			}else
			{
				 defaulterListSql="SELECT * " +
						 "    FROM (  SELECT billNonMiter.CUSTOMER_ID, " +
						 "                   mci.FULL_NAME, " +
						 "                   mci.ADDRESS, " +
						 "                   mci.PHONE||','||mci.MOBILE CONTACT_NO, " +
						 "                   CATEGORY_ID, " +
						 "                   CATEGORY_NAME, " +
						 "                   mci.AREA_ID, " +
						 "                   LISTAGG ( " +
						 "                         TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') " +
						 "                      || ' ' " +
						 "                      || SUBSTR (BILL_YEAR, 3), " +
						 "                      ',') " +
						 "                   WITHIN GROUP (ORDER BY BILL_YEAR ASC, BILL_MONTH ASC) " +
						 "                      AS DUEMONTH, " +
						 "                   (SELECT SUM ( " +
						 "                                NVL(BILLED_AMOUNT,0) " +
						 "                              - NVL (COLLECTED_BILLED_AMOUNT, 0)) " +
						 "                      FROM BILL_NON_METERED " +
						 "                     WHERE CUSTOMER_ID = billNonMiter.CUSTOMER_ID AND STATUS = 1 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= "+month_year+") " +
						 "                      AS TOTAL_AMOUNT, " +
						 "                   (SELECT COUNT (BILL_MONTH) " +
						 "                      FROM BILL_NON_METERED " +
						 "                     WHERE CUSTOMER_ID = billNonMiter.CUSTOMER_ID AND STATUS = 1 AND BILL_YEAR || LPAD (BILL_MONTH, 2, 0) <= "+month_year+") " +
						 "                      AS TOTAL_MONTH, " +
						 "                   DECODE (burner, '0','INACTIVE', 'ACTIVE') STATUS " +
						 "              FROM BILL_NON_METERED billNonMiter, VIEW_CUSTOMER_active mci " +
						 "             WHERE     billNonMiter.CUSTOMER_ID = MCI.CUSTOMER_ID " +
						 "                   AND billNonMiter.STATUS = 1 " +
						 "          GROUP BY billNonMiter.CUSTOMER_ID, " +
						 "                   mci.FULL_NAME, " +
						 "                   mci.PHONE||','||mci.MOBILE, " +
						 "                   burner, " +
						 "                   CATEGORY_ID, " +
						 "                   CATEGORY_NAME, " +
						 "                   mci.AREA_ID, " +
						 "                   mci.ADDRESS) tabl1 " +
						 "   WHERE "+wClause+criteriaWclause+
						 "ORDER BY customer_id " ;

			}
			
			





			
			PreparedStatement ps1=conn.prepareStatement(defaulterListSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		DefaulterDto defaulterDto=new DefaulterDto();
        		defaulterDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		defaulterDto.setFull_name(resultSet.getString("FULL_NAME"));
        		defaulterDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
        		defaulterDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		defaulterDto.setAddress(resultSet.getString("ADDRESS"));
        		defaulterDto.setContact_no(resultSet.getString("CONTACT_NO"));      		
        		defaulterDto.setDue_month(resultSet.getString("DUEMONTH"));
        		defaulterDto.setAmount(resultSet.getFloat("TOTAL_AMOUNT"));
        		defaulterDto.setTotal_month(resultSet.getInt("TOTAL_MONTH"));
        		defaulterDto.setStatus(resultSet.getString("STATUS"));
        		
      
        		
        		
   
        		
        		defaulterInfoList.add(defaulterDto);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return defaulterInfoList;
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


	public String getCriteria_type() {
		return criteria_type;
	}


	public void setCriteria_type(String criteria_type) {
		this.criteria_type = criteria_type;
	}


	public String getMonth_number() {
		return month_number;
	}


	public void setMonth_number(String month_number) {
		this.month_number = month_number;
	}


	public String getCustomer_type() {
		return customer_type;
	}


	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	
	


	
  }


