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

import org.apache.poi.ss.formula.ptg.PowerPtg;
import org.apache.struts2.ServletActionContext;
import org.pgcl.actions.BaseAction;
import org.pgcl.dto.AddressDTO;
import org.pgcl.dto.CustomerCategoryDTO;
import org.pgcl.dto.CustomerConnectionDTO;
import org.pgcl.dto.CustomerDTO;
import org.pgcl.dto.CustomerPersonalDTO;
import org.pgcl.dto.GasPurchaseDTO;
import org.pgcl.dto.MeterReadingDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.TariffDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.enums.Area;
import org.pgcl.enums.Month;
import org.pgcl.reports.ReportFormat;
import org.pgcl.reports.ReportUtil;
import org.pgcl.utils.cache.CacheUtil;
import org.pgcl.utils.connection.ConnectionManager;
import org.pgcl.utils.connection.TransactionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class VariousMarginReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<TariffDTO> tarrifMargin=new ArrayList<TariffDTO>();
	GasPurchaseDTO monthlyGasPurchase=new GasPurchaseDTO();
	GasPurchaseDTO monthlyGasPurchaseNew= new GasPurchaseDTO();
	public  ServletContext servlet;
	HashMap<String, Double> monthlySalesInfo=new HashMap<String, Double>();
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area="01";
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
		private PdfPCell pcell;
		private PdfPTable ptable;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		static DecimalFormat margin_format = new DecimalFormat("#############0.0000000000000000000");
		static DecimalFormat tarif_format = new DecimalFormat("#0.0000");
		
		
		
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		

	public String execute() throws Exception
	{
				
		String fileName="Various_Margin_Report.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		//DecimalFormat consumption_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.00000000");
		//DecimalFormat factor_format=new DecimalFormat("##########0.000");
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			/*Image img = Image.getInstance("H:/workspaceNew/PGCL/PGCL_WEB/resources/images/logo.png");
            //img.scaleToFit(10f, 200f);
            //img.scalePercent(200f);
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
			
			
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("SATEMENT SHOWING THE CALCULATION OF VARIOUS MARGIN FOR THE MONTH "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
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
			
			monthlySalesInfo=getMonthlySalesInformation();
			monthlyGasPurchase=getMonthlyGasPurchaseInformation();
			tarrifMargin=getTarrifMargin();
			
			
			
			double total_bgfcl_scm=0.0;
			double total_bgfcl_vat=0.0;
			double total_bgfcl_sd=0.0;
			double total_bgfcl_pdf=0.0;
			double total_bgfcl_bapex=0.0;
			double total_bgfcl_wellhead=0.0;
			double total_bgfcl_dwelhead=0.0;
			double total_bgfcl_transmission=0.0;
			double total_bgfcl_gdfund=0.0;
			double total_bgfcl_Avalue=0.0;
			double total_bgfcl_distribution=0.0;
			
			double total_sgfl_scm=0.0; 
			double total_sgfl_vat=0.0;
			double total_sgfl_sd=0.0;
			double total_sgfl_pdf=0.0;
			double total_sgfl_bapex=0.0;
			double total_sgfl_wellhead=0.0;
			double total_sgfl_dwelhead=0.0;
			double total_sgfl_transmission=0.0;
			double total_sgfl_gdfund=0.0;
			double total_sgfl_Avalue=0.0;
			double total_sgfl_distribution=0.0;
			
			double grand_total_scm=0.0; 
			double grand_total_vat=0.0;
			double grand_total_sd=0.0;
			double grand_total_pdf=0.0;
			double grand_total_bapex=0.0;
			double grand_total_wellhead=0.0;
			double grand_total_dwelhead=0.0;
			double grand_total_transmission=0.0;
			double grand_total_gdfund=0.0;
			double grand_total_Avalue=0.0;
			double grand_total_distribution=0.0;
			
			double total_bill_vat=0.0;
			double total_bill_sd=0.0;
			double total_taka=0.0;
			
			PdfPTable variousMargintable = new PdfPTable(15);
			variousMargintable.setWidthPercentage(100);
			variousMargintable.setWidths(new float[]{2,6,7,4,7,7,7,7,7,7,7,8,8,8,8});		
			
			pcell=new PdfPCell(new Paragraph("SL No",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Consumption SCM",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("End User Price SCM",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("VAT",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("SD",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("PB's Margin @ 45% On End User Price",font3));
			pcell.setColspan(8);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Taka",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("GOB's Margin @ 55% on end user price",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("PDF Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("BAPEX Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Wellhead Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Dif. Wellhead Margin for BAPEX",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Transmission Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Development Fund",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Asset Value of Gas",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Distribution Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			//--------Power----------//////////////////////////
			
			double powerTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getPrice()));
			double powerTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getVat()));
			double powerTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getWellhead()));
			double powerTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getSd()));
			double powerTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getPdf()));
			double powerTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getBapex()));
			double powerTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getDwellhead()));
			double powerTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getAvalue()));
			double powerTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getTrasmission()));
			double powerTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getDistribution()));
			double powerTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(0).getGdfund()));
			
			
			double capPowerTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getPrice()));
			double capPowerTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getVat()));
			double capPowerTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getWellhead()));
			double capPowerTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getSd()));
			double capPowerTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getPdf()));
			double capPowerTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getBapex()));
			double capPowerTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getDwellhead()));
			double capPowerTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getAvalue()));
			double capPowerTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getTrasmission()));
			double capPowerTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getDistribution()));
			double capPowerTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(2).getGdfund()));
			
			
			double cngTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getPrice()));
			double cngTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getVat()));
			double cngTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getWellhead()));
			double cngTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getSd()));
			double cngTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getPdf()));
			double cngTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getBapex()));
			double cngTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getDwellhead()));
			double cngTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getAvalue()));
			double cngTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getTrasmission()));
			double cngTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getDistribution()));
			double cngTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(1).getGdfund()));
			
			double indTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getPrice()));
			double indTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getVat()));
			double indTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getWellhead()));
			double indTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getSd()));
			double indTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getPdf()));
			double indTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getBapex()));
			double indTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getDwellhead()));
			double indTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getAvalue()));
			double indTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getTrasmission()));
			double indTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getDistribution()));
			double indTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(3).getGdfund()));
			
			double comTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getPrice()));
			double comTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getVat()));
			double comTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getWellhead()));
			double comTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getSd()));
			double comTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getPdf()));
			double comTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getBapex()));
			double comTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getDwellhead()));
			double comTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getAvalue()));
			double comTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getTrasmission()));
			double comTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getDistribution()));
			double comTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(4).getGdfund()));
			
			double domTarifPrice=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getPrice()));
			double domTarifVat=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getVat()));
			double domTarifWellhead=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getWellhead()));
			double domTarifSD=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getSd()));
			double domTarifPdf=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getPdf()));
			double domTarifBapex=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getBapex()));
			double domTarifDwelhead=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getDwellhead()));
			double domTarifAvalue=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getAvalue()));
			double domTarifTransmission=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getTrasmission()));
			double domTarifDistribution=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getDistribution()));
			double domTarifGdfund=Double.valueOf(tarif_format.format(tarrifMargin.get(5).getGdfund()));
			
			
			
			
			
			pcell = new PdfPCell(new Paragraph("01",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Power",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifVat),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifSD),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifPdf),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifBapex),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifWellhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifDwelhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifTransmission),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifGdfund),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifAvalue),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifDistribution),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(powerTarifPrice),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			
			double sys_gain_pow=(monthlySalesInfo.get("pow_gvt")+monthlySalesInfo.get("pow_pvt"))-monthlyGasPurchase.getTotal_power();
			double distribution_pow=(monthlyGasPurchase.getBgfcl_power()*powerTarifDistribution)+sys_gain_pow*powerTarifPrice;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_pow),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*powerTarifPrice),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*powerTarifPrice),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*powerTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Captive Power-------////////////////
			
			pcell = new PdfPCell(new Paragraph("02",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cap. Power",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifVat),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifSD),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifPdf),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifBapex),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifWellhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifDwelhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifTransmission),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifGdfund),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifAvalue),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifDistribution),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(capPowerTarifPrice),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_cap=(monthlySalesInfo.get("cap_gvt")+monthlySalesInfo.get("cap_pvt"))-monthlyGasPurchase.getTotal_captive();
			double distribution_cap=(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifDistribution)+sys_gain_cap*capPowerTarifPrice;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_cap),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*capPowerTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*capPowerTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----CNG-----------------////////////////
			
			pcell = new PdfPCell(new Paragraph("03",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("CNG",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifVat),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifSD),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifPdf),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifBapex),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifWellhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifDwelhead),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifTransmission),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifGdfund),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifAvalue),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifDistribution),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(cngTarifPrice),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			
			double sys_gain_cng=(monthlySalesInfo.get("cng_gvt")+monthlySalesInfo.get("cng_pvt"))-monthlyGasPurchase.getTotal_cng();
			double distribution_cng=(monthlyGasPurchase.getBgfcl_cng()*cngTarifDistribution)+sys_gain_cng*cngTarifPrice;
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_cng),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*cngTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
		
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*cngTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*cngTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Industrial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("04",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Industrial",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(indTarifPrice),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_ind=(monthlySalesInfo.get("ind_gvt")+monthlySalesInfo.get("ind_pvt"))-monthlyGasPurchase.getTotal_industrial();
			double distribution_ind=(monthlyGasPurchase.getBgfcl_industrial()*indTarifDistribution)+sys_gain_ind*indTarifPrice;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_ind),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*indTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*indTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*indTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Commercial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("05",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commercial",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(comTarifPrice),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			
			double sys_gain_comm=(monthlySalesInfo.get("comm_gvt")+monthlySalesInfo.get("comm_pvt"))-monthlyGasPurchase.getTotal_commercial();
			double distribution_comm=(monthlyGasPurchase.getBgfcl_commercial()*comTarifDistribution)+sys_gain_comm*comTarifPrice;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_comm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*comTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*comTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*comTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Domestic------------////////////////
			
			pcell = new PdfPCell(new Paragraph("06",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Domestic",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifPrice),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(domTarifPrice),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_dommestic=(monthlySalesInfo.get("dom_m_gvt")+monthlySalesInfo.get("dom_m_pvt")+monthlySalesInfo.get("dom_nm_gvt")+monthlySalesInfo.get("dom_nm_pvt"))-monthlyGasPurchase.getTotal_domestic();
			double distribution_dommestic=(monthlyGasPurchase.getBgfcl_Domestic()*domTarifDistribution)+sys_gain_dommestic*domTarifPrice;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_dommestic),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*domTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*domTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifVat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifSD),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifPdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifBapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifWellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifDwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifTransmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifGdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifAvalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifDistribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*domTarifPrice),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total BGFCL---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_scm= monthlyGasPurchase.getBgfcl_power()+monthlyGasPurchase.getBgfcl_captive()+monthlyGasPurchase.getBgfcl_cng()+monthlyGasPurchase.getBgfcl_industrial()+monthlyGasPurchase.getBgfcl_commercial()+monthlyGasPurchase.getBgfcl_Domestic();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_vat=(monthlyGasPurchase.getBgfcl_power()*powerTarifVat)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifVat)	+(monthlyGasPurchase.getBgfcl_cng()*cngTarifVat)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifVat)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifVat)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifVat);			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_bgfcl_sd=(monthlyGasPurchase.getBgfcl_power()*powerTarifSD)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifSD)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifSD)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifSD)	+(monthlyGasPurchase.getBgfcl_commercial()*comTarifSD)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifSD);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_bgfcl_pdf=(monthlyGasPurchase.getBgfcl_power()*powerTarifPdf)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifPdf)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifPdf)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifPdf)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifPdf)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifPdf);
			
			
			System.out.println("BGFCL PDF : "+consumption_format.format(total_bgfcl_pdf));
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_bapex=(monthlyGasPurchase.getBgfcl_power()*powerTarifBapex)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifBapex)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifBapex)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifBapex)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifBapex)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifBapex);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_wellhead=(monthlyGasPurchase.getBgfcl_power()*powerTarifWellhead)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifWellhead)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifWellhead)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifWellhead)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifWellhead)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifWellhead);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_dwelhead=(monthlyGasPurchase.getBgfcl_power()*powerTarifDwelhead)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifDwelhead)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifDwelhead)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifDwelhead)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifDwelhead)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifDwelhead);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_transmission=(monthlyGasPurchase.getBgfcl_power()*powerTarifTransmission)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifTransmission)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifTransmission)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifTransmission)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifTransmission)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifTransmission);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_gdfund=(monthlyGasPurchase.getBgfcl_power()*powerTarifGdfund)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifGdfund)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifGdfund)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifGdfund)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifGdfund)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifGdfund);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_Avalue=(monthlyGasPurchase.getBgfcl_power()*powerTarifAvalue)+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifAvalue)+(monthlyGasPurchase.getBgfcl_cng()*cngTarifAvalue)+(monthlyGasPurchase.getBgfcl_industrial()*indTarifAvalue)+(monthlyGasPurchase.getBgfcl_commercial()*comTarifAvalue)+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifAvalue);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_distribution=distribution_pow+distribution_cap+distribution_cng+distribution_comm+distribution_ind+distribution_dommestic;
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_taka = (monthlyGasPurchase.getBgfcl_power()*powerTarifPrice)+(monthlyGasPurchase.getSgfl_power()*powerTarifPrice)+(monthlyGasPurchase.getIoc_power()*powerTarifPrice)
					+(monthlyGasPurchase.getBgfcl_captive()*capPowerTarifPrice)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifPrice)+(monthlyGasPurchase.getIoc_captive()*capPowerTarifPrice)
					+(monthlyGasPurchase.getBgfcl_cng()*cngTarifPrice)+(monthlyGasPurchase.getSgfl_cng()*cngTarifPrice)+(monthlyGasPurchase.getIoc_cng()*cngTarifPrice)
					+(monthlyGasPurchase.getBgfcl_industrial()*indTarifPrice)+(monthlyGasPurchase.getSgfl_industrial()*indTarifPrice)+(monthlyGasPurchase.getIoc_industrial()*indTarifPrice)
					+(monthlyGasPurchase.getBgfcl_commercial()*comTarifPrice)+(monthlyGasPurchase.getSgfl_commercial()*comTarifPrice)+(monthlyGasPurchase.getIoc_commercial()*comTarifPrice)
					+(monthlyGasPurchase.getBgfcl_Domestic()*domTarifPrice)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifPrice)+(monthlyGasPurchase.getIoc_Domestic()*domTarifPrice);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_taka),ReportUtil.f9B));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			insertMarginDate("MARGIN_BGFCL",total_bgfcl_vat,total_bgfcl_sd,total_bgfcl_pdf,total_bgfcl_bapex,total_bgfcl_wellhead,total_bgfcl_dwelhead,total_bgfcl_transmission,total_bgfcl_gdfund,total_bgfcl_Avalue,total_bgfcl_distribution);
			
			
			/*---------------------------------------------------------------------------For Testing Purpose Code of PDF Margin-------------------------------------------------------------------------*/
			
			String powerConsump=margin_format.format(monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getBgfcl_power_gvt());
			String cngConsump=margin_format.format(monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getBgfcl_cng_gvt());
			String captiveConsum=margin_format.format(monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getBgfcl_captive_gvt());
			String indusConsump=margin_format.format(monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getBgfcl_industry_gvt());
			String commConsump=margin_format.format(monthlyGasPurchase.getBgfcl_comm_pvt()+monthlyGasPurchase.getBgfcl_comm_gvt());
			String domesConsump=margin_format.format(monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt());
		
			double powerTarif=powerTarifPdf;
			double cngTarif=cngTarifPdf;
			double captiveTarif=capPowerTarifPdf;
			double indusTarif=indTarifPdf;
			double commTarif=comTarifPdf;
			double domesTarif=domTarifPdf;
			
			
			 
			double abs = Double.valueOf(consumption_format.format(powerTarifPdf));
			
			String tpowerConsump=margin_format.format((monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getBgfcl_power_gvt())*powerTarifPdf);
			String tcngConsump=margin_format.format((monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getBgfcl_cng_gvt())*cngTarifPdf);
			String tcaptiveConsum=margin_format.format((monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getBgfcl_captive_gvt())*capPowerTarifPdf);
			String tindusConsump=margin_format.format((monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getBgfcl_industry_gvt())*indTarifPdf);
			String tcommConsump=margin_format.format((monthlyGasPurchase.getBgfcl_comm_pvt()+monthlyGasPurchase.getBgfcl_comm_gvt())*comTarifPdf);
			String tdomesConsump=margin_format.format((monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt())*domTarifPdf);
			
			
			double power=Double.parseDouble(powerConsump);
			double cng=Double.parseDouble(cngConsump);
			
			
			/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
			
			double total_bgfcl_scm1=0.0;
			double total_bgfcl_vat1=0.0;
			double total_bgfcl_sd1=0.0;
			double total_bgfcl_pdf1=0.0;
			double total_bgfcl_bapex1=0.0;
			double total_bgfcl_wellhead1=0.0;
			double total_bgfcl_dwelhead1=0.0;
			double total_bgfcl_transmission1=0.0;
			double total_bgfcl_gdfund1=0.0;
			double total_bgfcl_Avalue1=0.0;
			double total_bgfcl_distribution1=0.0;
			
			double total_sgfl_scm1=0.0; 
			double total_sgfl_vat1=0.0;
			double total_sgfl_sd1=0.0;
			double total_sgfl_pdf1=0.0;
			double total_sgfl_bapex1=0.0;
			double total_sgfl_wellhead1=0.0;
			double total_sgfl_dwelhead1=0.0;
			double total_sgfl_transmission1=0.0;
			double total_sgfl_gdfund1=0.0;
			double total_sgfl_Avalue1=0.0;
			double total_sgfl_distribution1=0.0;
			
			
			
			monthlyGasPurchaseNew=getMonthlyGasPurchaseInformationNew();
			
			total_bgfcl_scm1=monthlyGasPurchaseNew.getBgfcl_power_gvt()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()+monthlyGasPurchaseNew.getBgfcl_industry_gvt()
					+monthlyGasPurchaseNew.getBgfcl_comm_gvt()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt();
			
			total_bgfcl_vat1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifVat+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifVat+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifVat+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifVat+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifVat+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifVat;
			
			total_bgfcl_sd1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifSD+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifSD+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifSD+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifSD+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifSD+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifSD;
			
			total_bgfcl_pdf1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifPdf+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifPdf+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifPdf+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifPdf+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifPdf+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifPdf;
					
					
			total_bgfcl_bapex1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifBapex+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifBapex+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifBapex+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifBapex+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifBapex+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifBapex;
			
			
			total_bgfcl_wellhead1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifWellhead+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifWellhead+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifWellhead+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifWellhead+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifWellhead+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifWellhead;
			
			
			total_bgfcl_dwelhead1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifDwelhead+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifDwelhead+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifDwelhead+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifDwelhead+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifDwelhead+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifDwelhead;
			
			
			total_bgfcl_transmission1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifTransmission+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifTransmission+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifTransmission+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifTransmission+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifTransmission+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifTransmission;
			
			total_bgfcl_gdfund1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifGdfund+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifGdfund+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifGdfund+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifGdfund+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifGdfund+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifGdfund;
			
			total_bgfcl_Avalue1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifAvalue+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifAvalue+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifAvalue+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifAvalue+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifAvalue+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifAvalue;
					
			
			total_bgfcl_distribution1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*powerTarifDistribution+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*cngTarifDistribution+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*capPowerTarifDistribution+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*indTarifDistribution+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*comTarifDistribution+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*domTarifDistribution;
			
			
			
			total_sgfl_scm1=monthlyGasPurchaseNew.getSgfl_power_gvt()+monthlyGasPurchaseNew.getSgfl_captive_gvt()+monthlyGasPurchaseNew.getSgfl_cng_gvt()+monthlyGasPurchaseNew.getSgfl_industry_gvt()
					+monthlyGasPurchaseNew.getSgfl_comm_gvt()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt();
			
			total_sgfl_vat1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifVat+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifVat+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifVat+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifVat+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifVat+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifVat;
			
			total_sgfl_sd1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifSD+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifSD+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifSD+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifSD+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifSD+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifSD;
			
			total_sgfl_pdf1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifPdf+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifPdf+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifPdf+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifPdf+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifPdf+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifPdf;
					
					
			total_sgfl_bapex1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifBapex+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifBapex+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifBapex+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifBapex+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifBapex+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifBapex;
			
			
			total_sgfl_wellhead1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifWellhead+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifWellhead+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifWellhead+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifWellhead+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifWellhead+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifWellhead;
			
			
			
			total_sgfl_dwelhead1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifDwelhead+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifDwelhead+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifDwelhead+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifDwelhead+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifDwelhead+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifDwelhead;
			
			
			total_sgfl_transmission1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifTransmission+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifTransmission+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifTransmission+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifTransmission+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifTransmission+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifTransmission;
			
			total_sgfl_gdfund1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifGdfund+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifGdfund+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifGdfund+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifGdfund+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifGdfund+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifGdfund;
			
			total_sgfl_Avalue1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifAvalue+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifAvalue+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifAvalue+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifAvalue+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifAvalue+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifAvalue;
					
			
			total_sgfl_distribution1=monthlyGasPurchaseNew.getSgfl_power_gvt()*powerTarifDistribution+monthlyGasPurchaseNew.getSgfl_cng_gvt()*cngTarifDistribution+monthlyGasPurchaseNew.getSgfl_captive_gvt()*capPowerTarifDistribution+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*indTarifDistribution+monthlyGasPurchaseNew.getSgfl_comm_gvt()*comTarifDistribution+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*domTarifDistribution;
			
			
			insertMarginDateNew("MARGIN_BGFCL_NEW", total_bgfcl_vat1, total_bgfcl_sd1, total_bgfcl_pdf1, total_bgfcl_bapex1, total_bgfcl_wellhead1, total_bgfcl_dwelhead1, total_bgfcl_transmission1, total_bgfcl_gdfund1, total_bgfcl_Avalue1, total_bgfcl_distribution1);
			
			
			
			insertMarginDateNew("MARGIN_SGFL_NEW",total_sgfl_vat1,total_sgfl_sd1,total_sgfl_pdf1,total_sgfl_bapex1,total_sgfl_wellhead1,total_sgfl_dwelhead1,total_sgfl_transmission1,total_sgfl_gdfund1,total_sgfl_Avalue1,total_sgfl_distribution1);
			
			
			
			
			
			/*---------------------------------------------------------------------------End of Test------------------------------------------------------------------------------------------------------------------*/
		
			//////////////////------Total SGFL----------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total SGFL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_scm= monthlyGasPurchase.getSgfl_power()+monthlyGasPurchase.getSgfl_captive()+monthlyGasPurchase.getSgfl_cng()
			+monthlyGasPurchase.getSgfl_industrial()+monthlyGasPurchase.getSgfl_commercial()+monthlyGasPurchase.getSgfl_Domestic();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_vat=(monthlyGasPurchase.getSgfl_power()*powerTarifVat)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifVat)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifVat)+(monthlyGasPurchase.getSgfl_industrial()*indTarifVat)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifVat)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifVat);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_sgfl_sd=(monthlyGasPurchase.getSgfl_power()*powerTarifSD)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifSD)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifSD)+(monthlyGasPurchase.getSgfl_industrial()*indTarifSD)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifSD)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifSD);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_sgfl_pdf=(monthlyGasPurchase.getSgfl_power()*powerTarifPdf)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifPdf)+(monthlyGasPurchase.getSgfl_cng()*cngTarifPdf)+(monthlyGasPurchase.getSgfl_industrial()*indTarifPdf)+(monthlyGasPurchase.getSgfl_commercial()*comTarifPdf)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifPdf);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			System.out.println("Total SGFL : "+consumption_format.format(total_sgfl_pdf));
			
			total_sgfl_bapex=(monthlyGasPurchase.getSgfl_power()*powerTarifBapex)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifBapex)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifBapex)+(monthlyGasPurchase.getSgfl_industrial()*indTarifBapex)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifBapex)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifBapex);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_wellhead=(monthlyGasPurchase.getSgfl_power()*powerTarifWellhead)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifWellhead)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifWellhead)+(monthlyGasPurchase.getSgfl_industrial()*indTarifWellhead)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifWellhead)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifWellhead);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_dwelhead=(monthlyGasPurchase.getSgfl_power()*powerTarifDwelhead)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifDwelhead)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifDwelhead)+(monthlyGasPurchase.getSgfl_industrial()*indTarifDwelhead)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifDwelhead)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifDwelhead);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_transmission=(monthlyGasPurchase.getSgfl_power()*powerTarifTransmission)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifTransmission)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifTransmission)+(monthlyGasPurchase.getSgfl_industrial()*indTarifTransmission)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifTransmission)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifTransmission);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_gdfund=(monthlyGasPurchase.getSgfl_power()*powerTarifGdfund)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifGdfund)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifGdfund)+(monthlyGasPurchase.getSgfl_industrial()*indTarifGdfund)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifGdfund)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifGdfund);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_Avalue=(monthlyGasPurchase.getSgfl_power()*powerTarifAvalue)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifAvalue)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifAvalue)+(monthlyGasPurchase.getSgfl_industrial()*indTarifAvalue)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifAvalue)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifAvalue);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_distribution=(monthlyGasPurchase.getSgfl_power()*powerTarifDistribution)+(monthlyGasPurchase.getSgfl_captive()*capPowerTarifDistribution)
					+(monthlyGasPurchase.getSgfl_cng()*cngTarifDistribution)+(monthlyGasPurchase.getSgfl_industrial()*indTarifDistribution)
					+(monthlyGasPurchase.getSgfl_commercial()*comTarifDistribution)+(monthlyGasPurchase.getSgfl_Domestic()*domTarifDistribution);
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			insertMarginDate("MARGIN_SGFL",total_sgfl_vat,total_sgfl_sd,total_sgfl_pdf,total_sgfl_bapex,total_sgfl_wellhead,total_sgfl_dwelhead,total_sgfl_transmission,total_sgfl_gdfund,total_sgfl_Avalue,total_sgfl_distribution);
			//////////////////------Grand Total---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Grand Total=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_scm=total_bgfcl_scm+total_sgfl_scm;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_vat=total_bgfcl_vat+total_sgfl_vat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			grand_total_sd=total_bgfcl_sd+total_sgfl_sd;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			grand_total_pdf=total_bgfcl_pdf+total_sgfl_pdf;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_bapex=total_bgfcl_bapex+total_sgfl_bapex;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_wellhead=total_bgfcl_wellhead+total_sgfl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_dwelhead=total_bgfcl_dwelhead+total_sgfl_dwelhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_transmission=total_bgfcl_transmission+total_sgfl_transmission;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_gdfund=total_bgfcl_gdfund+total_sgfl_gdfund;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_Avalue=total_bgfcl_Avalue+total_sgfl_Avalue;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_distribution=total_bgfcl_distribution+total_sgfl_distribution;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total Bill of BGFCL/SGFL-//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total Bill of BGFCL/SGFL=",font2));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bill_vat = total_bgfcl_vat+total_bgfcl_sd+total_bgfcl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bill_vat),ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bill_sd=total_sgfl_vat+total_sgfl_sd+total_sgfl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bill_sd),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(9);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			document.add(variousMargintable);
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	public void insertMarginDate(String tableName,double total_bgfcl_vat,double total_bgfcl_sd,double total_bgfcl_pdf,double total_bgfcl_bapex,double total_bgfcl_wellhead,double total_bgfcl_dwelhead,double total_bgfcl_transmission,double total_bgfcl_gdfund,double total_bgfcl_Avalue,double total_bgfcl_distribution){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
		String deleteString="Delete  "+tableName+" where MONTH=? AND YEAR=?";
		String sqlInsert=" Insert into "+tableName+"(MONTH,YEAR,VAT,SD,PDF,BAPEX,WELLHEAD,DWELLHED,TRANSMISSION,GD_FUND,ASSET_VALUE,DISTRIBUTION_MARGIN) " +
				 		  " Values(?,?,?,?,?,?,?,?,?,?,?,?)";
		

		
		

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(deleteString);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.execute();

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.setDouble(3,total_bgfcl_vat);
				stmt.setDouble(4,total_bgfcl_sd);
				stmt.setDouble(5,total_bgfcl_pdf);
				stmt.setDouble(6,total_bgfcl_bapex);
				stmt.setDouble(7,total_bgfcl_wellhead);	
				stmt.setDouble(8,total_bgfcl_dwelhead);		
				stmt.setDouble(9,total_bgfcl_transmission);
				stmt.setDouble(10,total_bgfcl_gdfund);
				stmt.setDouble(11,total_bgfcl_Avalue);
				stmt.setDouble(12,total_bgfcl_distribution);
				stmt.execute();
				
			
				
				transactionManager.commit();
				
			
				
			

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return;
	}
	
	/*----------------------------------------------------------------------------------------------Should be comment out---------------------------------------------------------------------------------*/
	
	public void insertMarginDateNew(String tableName,double total_bgfcl_vat,double total_bgfcl_sd,double total_bgfcl_pdf,double total_bgfcl_bapex,double total_bgfcl_wellhead,double total_bgfcl_dwelhead,double total_bgfcl_transmission,double total_bgfcl_gdfund,double total_bgfcl_Avalue,double total_bgfcl_distribution){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
		String deleteString="Delete  "+tableName+" where MONTH=? AND YEAR=?";
		String sqlInsert=" Insert into "+tableName+"(MONTH,YEAR,VAT,SD,PDF,BAPEX,WELLHEAD,DWELLHED,TRANSMISSION,GD_FUND,ASSET_VALUE,DISTRIBUTION_MARGIN) " +
				 		  " Values(?,?,?,?,?,?,?,?,?,?,?,?)";
		

		
		

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(deleteString);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.execute();

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.setDouble(3,total_bgfcl_vat);
				stmt.setDouble(4,total_bgfcl_sd);
				stmt.setDouble(5,total_bgfcl_pdf);
				stmt.setDouble(6,total_bgfcl_bapex);
				stmt.setDouble(7,total_bgfcl_wellhead);	
				stmt.setDouble(8,total_bgfcl_dwelhead);		
				stmt.setDouble(9,total_bgfcl_transmission);
				stmt.setDouble(10,total_bgfcl_gdfund);
				stmt.setDouble(11,total_bgfcl_Avalue);
				stmt.setDouble(12,total_bgfcl_distribution);
				stmt.execute();
				
			
				
				transactionManager.commit();
				
			
				
			

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return;
	}
	
	
	private GasPurchaseDTO getMonthlyGasPurchaseInformationNew()
	{
		GasPurchaseDTO gasPurchase = new GasPurchaseDTO();
	
		
	
		
		try {
			
			
			
 			String monthly_gas_purchase="select bgfcl.MONTH, bgfcl.YEAR,TOTAL_BGFCL, TOTAL_SGFL, TOTAL_IOC, TOTAL_GTCL,  " +
 					"                    bgfcl.POWER, bgfcl.CAPTIVE, bgfcl.CNG, bgfcl.INDUSTRY, bgfcl.COMMERCIAL,bgfcl.DOMESTIC,bgfcl.FERTILIZER, bgfcl.TEA, " +
 					"                    sgfl.POWER POWER_SGFL, sgfl.CAPTIVE CAPTIVE_SGFL, sgfl.CNG CNG_SGFL, sgfl.INDUSTRY INDUSTRY_SGFL, sgfl.COMMERCIAL COMMERCIAL_SGFL, " +
 					"                    sgfl.DOMESTIC DOMESTIC_SGFL,sgfl.FERTILIZER FERTILIZER_SGFL, sgfl.TEA TEA_SGFL                     " +
 					"                    from GAS_PURCHASE_SUMMARY gps,TOTAL_BGFCL_RATIO bgfcl,TOTAL_SGFL_RATIO sgfl  " +
 					"                    where gps.pid=bgfcl.pid  " +
 					"                    and gps.pid=sgfl.pid  " +
 					"                     AND bgfcl.MONTH="+bill_month+
 					"                     AND bgfcl.YEAR="+bill_year+" " ;






			
 			PreparedStatement ps1=conn.prepareStatement(monthly_gas_purchase);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		        		
        		gasPurchase.setMonth(resultSet.getString("MONTH"));
        		gasPurchase.setYear(resultSet.getString("YEAR"));
        		gasPurchase.setTotal_bgfcl(resultSet.getDouble("TOTAL_BGFCL"));
        		gasPurchase.setTotal_sgfl(resultSet.getDouble("TOTAL_SGFL"));
        		gasPurchase.setTotal_ioc(resultSet.getDouble("TOTAL_IOC"));
        		gasPurchase.setTotal_gtcl(resultSet.getDouble("TOTAL_GTCL"));
        		
        		gasPurchase.setBgfcl_power_gvt(resultSet.getDouble("POWER"));
        		gasPurchase.setBgfcl_captive_gvt(resultSet.getDouble("CAPTIVE"));
        		gasPurchase.setBgfcl_cng_gvt(resultSet.getDouble("CNG"));
        		gasPurchase.setBgfcl_industry_gvt(resultSet.getDouble("INDUSTRY"));
        		gasPurchase.setBgfcl_comm_gvt(resultSet.getDouble("COMMERCIAL"));
        		gasPurchase.setBgfcl_dom_meter_gvt(resultSet.getDouble("DOMESTIC"));
        		gasPurchase.setBgfcl_fertilizer_gvt(resultSet.getDouble("FERTILIZER"));
        		gasPurchase.setBgfcl_tea_gvt(resultSet.getDouble("TEA"));
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("POWER_SGFL"));
        		gasPurchase.setSgfl_captive_gvt(resultSet.getDouble("CAPTIVE_SGFL"));
        		gasPurchase.setSgfl_cng_gvt(resultSet.getDouble("CNG_SGFL"));
        		gasPurchase.setSgfl_industry_gvt(resultSet.getDouble("INDUSTRY_SGFL"));
        		gasPurchase.setSgfl_comm_gvt(resultSet.getDouble("COMMERCIAL_SGFL"));
        		gasPurchase.setSgfl_dom_meter_gvt(resultSet.getDouble("DOMESTIC_SGFL"));
        		gasPurchase.setSgfl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_SGFL"));
        		gasPurchase.setSgfl_tea_gvt(resultSet.getDouble("TEA_SGFL"));
        		
        		
           		
        	}
      
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gasPurchase;
	}
	
	
	
	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	
	
	private ArrayList<TariffDTO> getTarrifMargin()
	{
		
		ArrayList<TariffDTO> tariffMarginList=new ArrayList<TariffDTO>();
	
		//PreparedStatement ps1=null;
		try {
			
			
			//// area,month,year change kora hoiche initialization e
			String margin_calculation_sql="select MT.*,SUBSTR (CATEGORY_NAME, 0, INSTR (CATEGORY_NAME, '(') - 1) CATEGORY_NAME from MST_TARIFF MT,MST_CUSTOMER_CATEGORY mcc " +
					"where " +
					"MT.CUSTOMER_CATEGORY_ID=MCC.CATEGORY_ID " +
					"and  " +
					"Effective_From<=to_date('01-"+bill_month+"-"+bill_year+"','dd-MM-YYYY HH24:MI:SS') " +
					"And (Effective_To is Null or Effective_To>=to_date('01-"+bill_month+"-"+bill_year+"','dd-MM-YYYY HH24:MI:SS')) " +
					"and CUSTOMER_CATEGORY_ID in(01,03,05,07,09,11) " +
					"and meter_status=1 " +
					"order by CUSTOMER_CATEGORY_ID desc " ;





			
			PreparedStatement ps1=conn.prepareStatement(margin_calculation_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TariffDTO tariffMargin=new TariffDTO();
        		
        		tariffMargin.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		tariffMargin.setPrice(resultSet.getFloat("PRICE"));
        		tariffMargin.setPb(resultSet.getFloat("PB"));
        		tariffMargin.setVat(resultSet.getFloat("VAT"));
        		tariffMargin.setSd(resultSet.getFloat("SD"));
        		tariffMargin.setPdf(resultSet.getFloat("PDF"));
        		tariffMargin.setBapex(resultSet.getFloat("BAPEX"));
        		tariffMargin.setWellhead(resultSet.getFloat("WELLHEAD"));
        		tariffMargin.setDwellhead(resultSet.getFloat("DWELLHEAD"));
        		tariffMargin.setTrasmission(resultSet.getFloat("TRNSMISSION"));
        		tariffMargin.setGdfund(resultSet.getFloat("GDFUND"));
        		tariffMargin.setDistribution(resultSet.getFloat("DISTRIBUTION"));
        		tariffMargin.setAvalue(resultSet.getFloat("AVALUE"));    		
        		tariffMarginList.add(tariffMargin);
        		
        	}
            
        	
        	
        	
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tariffMarginList;
	}
	@SuppressWarnings("unused")
	private GasPurchaseDTO getMonthlyGasPurchaseInformation()
	{
		GasPurchaseDTO gasPurchase = new GasPurchaseDTO();
		//PreparedStatement ps1=null;
		
	
		
		try {
			
			
			
 			String monthly_gas_purchase="select bgfcl.MONTH, bgfcl.YEAR,TOTAL_BGFCL, TOTAL_SGFL, TOTAL_IOC, TOTAL_GTCL, " +
					"bgfcl.PW_GVT, bgfcl.PW_PVT, bgfcl.CAP_GVT,  " +
					"bgfcl.CAP_PVT, bgfcl.CNG_GVT, bgfcl.CNG_PVT,  " +
					"bgfcl.IND_GVT, bgfcl.IND_PVT, bgfcl.COMM_GVT,  " +
					"bgfcl.COMM_PVT, bgfcl.DOM_M_GVT, bgfcl.DOM_M_PVT,  " +
					"bgfcl.DOM_NM_GVT, bgfcl.DOM_NM_PVT, bgfcl.FERTILIZER_GVT,  " +
					"bgfcl.FERTILIZER_PVT, bgfcl.TEA_GVT, bgfcl.TEA_PVT, " +
					"ioc.PW_GVT PW_GVT_IOC, ioc.PW_PVT PW_PVT_IOC, ioc.CAP_GVT CAP_GVT_IOC,  " +
					"ioc.CAP_PVT CAP_PVT_IOC, ioc.CNG_GVT CNG_GVT_IOC, ioc.CNG_PVT CNG_PVT_IOC,  " +
					"ioc.IND_GVT IND_GVT_IOC, ioc.IND_PVT IND_PVT_IOC, ioc.COMM_GVT COMM_GVT_IOC,   " +
					"ioc.COMM_PVT COMM_PVT_IOC, ioc.DOM_M_GVT DOM_M_GVT_IOC, ioc.DOM_M_PVT DOM_M_PVT_IOC,  " +
					"ioc.DOM_NM_GVT DOM_NM_GVT_IOC, ioc.DOM_NM_PVT DOM_NM_PVT_IOC, ioc.FERTILIZER_GVT FERTILIZER_GVT_IOC,  " +
					"ioc.FERTILIZER_PVT FERTILIZER_PVT_IOC, ioc.TEA_GVT TEA_GVT_IOC, ioc.TEA_PVT TEA_PVT_IOC, " +
					"sgfl.PW_GVT PW_GVT_sgfl, sgfl.PW_PVT PW_PVT_sgfl, sgfl.CAP_GVT CAP_GVT_sgfl,  " +
					"sgfl.CAP_PVT CAP_PVT_sgfl, sgfl.CNG_GVT CNG_GVT_sgfl, sgfl.CNG_PVT CNG_PVT_sgfl,  " +
					"sgfl.IND_GVT IND_GVT_sgfl, sgfl.IND_PVT IND_PVT_sgfl, sgfl.COMM_GVT COMM_GVT_sgfl,   " +
					"sgfl.COMM_PVT COMM_PVT_sgfl, sgfl.DOM_M_GVT DOM_M_GVT_sgfl, sgfl.DOM_M_PVT DOM_M_PVT_sgfl,  " +
					"sgfl.DOM_NM_GVT DOM_NM_GVT_sgfl, sgfl.DOM_NM_PVT DOM_NM_PVT_sgfl, sgfl.FERTILIZER_GVT FERTILIZER_GVT_sgfl,  " +
					"sgfl.FERTILIZER_PVT FERTILIZER_PVT_sgfl, sgfl.TEA_GVT TEA_GVT_sgfl, sgfl.TEA_PVT TEA_PVT_sgfl " +
					"from GAS_PURCHASE_SUMMARY gps,GAS_PURCHASE_BGFCL bgfcl,GAS_PURCHASE_IOC ioc,GAS_PURCHASE_SGFL sgfl " +
					"where gps.pid=bgfcl.pid " +
					"and gps.pid=ioc.pid " +
					"and gps.pid=sgfl.pid "+
					" AND bgfcl.MONTH="+bill_month+
					" AND bgfcl.YEAR="+bill_year;






			
 			PreparedStatement ps1=conn.prepareStatement(monthly_gas_purchase);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		Double powper_bgfcl=0.0;
        		Double cap_bgfcl=0.0;
        		Double cng_bgfcl=0.0;
        		Double ind_bgfcl=0.0;
        		Double comm_bgfcl=0.0;
        		Double domestic_bgfcl=0.0;
        		
        		Double powper_ioc=0.0;
        		Double cap_ioc=0.0;
        		Double cng_ioc=0.0;
        		Double ind_ioc=0.0;
        		Double comm_ioc=0.0;
        		Double domestic_ioc=0.0;
        		
        		Double powper_sgfl=0.0;
        		Double cap_sgfl=0.0;
        		Double cng_sgfl=0.0;
        		Double ind_sgfl=0.0;
        		Double comm_sgfl=0.0;
        		Double domestic_sgfl=0.0;
        		
        		gasPurchase.setMonth(resultSet.getString("MONTH"));
        		gasPurchase.setYear(resultSet.getString("YEAR"));
        		gasPurchase.setTotal_bgfcl(resultSet.getDouble("TOTAL_BGFCL"));
        		gasPurchase.setTotal_sgfl(resultSet.getDouble("TOTAL_SGFL"));
        		gasPurchase.setTotal_ioc(resultSet.getDouble("TOTAL_IOC"));
        		gasPurchase.setTotal_gtcl(resultSet.getDouble("TOTAL_GTCL"));
        		gasPurchase.setBgfcl_power_gvt(resultSet.getDouble("PW_GVT"));
        		gasPurchase.setBgfcl_power_pvt(resultSet.getDouble("PW_PVT"));
        		gasPurchase.setBgfcl_captive_gvt(resultSet.getDouble("CAP_GVT"));
        		gasPurchase.setBgfcl_captive_pvt(resultSet.getDouble("CAP_PVT"));
        		gasPurchase.setBgfcl_cng_gvt(resultSet.getDouble("CNG_GVT"));
        		gasPurchase.setBgfcl_cng_pvt(resultSet.getDouble("CNG_PVT"));
        		gasPurchase.setBgfcl_industry_gvt(resultSet.getDouble("IND_GVT"));
        		gasPurchase.setBgfcl_industry_pvt(resultSet.getDouble("IND_PVT"));
        		gasPurchase.setBgfcl_comm_gvt(resultSet.getDouble("COMM_GVT"));
        		gasPurchase.setBgfcl_comm_pvt(resultSet.getDouble("COMM_PVT"));
        		gasPurchase.setBgfcl_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT"));
        		gasPurchase.setBgfcl_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT"));
        		gasPurchase.setBgfcl_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT"));
        		gasPurchase.setBgfcl_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT"));
        		gasPurchase.setBgfcl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT"));
        		gasPurchase.setBgfcl_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT"));
        		gasPurchase.setBgfcl_tea_gvt(resultSet.getDouble("TEA_GVT"));
        		gasPurchase.setBgfcl_tea_pvt(resultSet.getDouble("TEA_PVT"));
        		powper_bgfcl=resultSet.getDouble("PW_GVT")+resultSet.getDouble("PW_PVT");
        		gasPurchase.setBgfcl_power(powper_bgfcl);
        		cap_bgfcl=resultSet.getDouble("CAP_GVT")+resultSet.getDouble("CAP_PVT");
        		gasPurchase.setBgfcl_captive(cap_bgfcl);
        		cng_bgfcl=resultSet.getDouble("CNG_GVT")+resultSet.getDouble("CNG_PVT");
        		gasPurchase.setBgfcl_cng(cng_bgfcl);
        		ind_bgfcl=resultSet.getDouble("IND_GVT")+resultSet.getDouble("IND_PVT");
        		gasPurchase.setBgfcl_industrial(ind_bgfcl);
        		comm_bgfcl=resultSet.getDouble("COMM_GVT")+resultSet.getDouble("COMM_PVT");
        		gasPurchase.setBgfcl_commercial(comm_bgfcl);
        		domestic_bgfcl=resultSet.getDouble("DOM_M_GVT")+resultSet.getDouble("DOM_M_PVT")+resultSet.getDouble("DOM_NM_GVT")+resultSet.getDouble("DOM_NM_PVT");
        		gasPurchase.setBgfcl_Domestic(domestic_bgfcl);
        		gasPurchase.setBgfcl_fertilizer(resultSet.getDouble("FERTILIZER_GVT")+resultSet.getDouble("FERTILIZER_PVT"));
        		gasPurchase.setBgfcl_tea(resultSet.getDouble("TEA_GVT")+resultSet.getDouble("TEA_GVT"));
        		
        		
           		gasPurchase.setIoc_power_gvt(resultSet.getDouble("PW_GVT_IOC"));
        		gasPurchase.setIoc_power_pvt(resultSet.getDouble("PW_PVT_IOC"));
        		gasPurchase.setIoc_captive_gvt(resultSet.getDouble("CAP_GVT_IOC"));
        		gasPurchase.setIoc_captive_pvt(resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_cng_gvt(resultSet.getDouble("CNG_GVT_IOC"));
        		gasPurchase.setIoc_cng_pvt(resultSet.getDouble("CNG_PVT_IOC"));
        		gasPurchase.setIoc_industry_gvt(resultSet.getDouble("IND_GVT_IOC"));
        		gasPurchase.setIoc_industry_pvt(resultSet.getDouble("IND_PVT_IOC"));
        		gasPurchase.setIoc_comm_gvt(resultSet.getDouble("COMM_GVT_IOC"));
        		gasPurchase.setIoc_comm_pvt(resultSet.getDouble("COMM_PVT_IOC"));
        		gasPurchase.setIoc_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT_IOC"));
        		gasPurchase.setIoc_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT_IOC"));
        		gasPurchase.setIoc_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT_IOC"));
        		gasPurchase.setIoc_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT_IOC"));
        		gasPurchase.setIoc_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT_IOC"));
        		gasPurchase.setIoc_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT_IOC"));
        		gasPurchase.setIoc_tea_gvt(resultSet.getDouble("TEA_GVT_IOC"));
        		gasPurchase.setIoc_tea_pvt(resultSet.getDouble("TEA_PVT_IOC"));
        		gasPurchase.setIoc_tea_gvt(resultSet.getDouble("TEA_GVT"));
        		gasPurchase.setIoc_tea_pvt(resultSet.getDouble("TEA_PVT"));
        		powper_ioc=resultSet.getDouble("PW_GVT_IOC")+resultSet.getDouble("PW_PVT_IOC");
        		gasPurchase.setIoc_power(powper_ioc);
        		cap_ioc=resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC");
        		gasPurchase.setIoc_captive(cap_ioc);
        		cng_ioc=resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC");
        		gasPurchase.setIoc_cng(cng_bgfcl);
        		ind_ioc=resultSet.getDouble("IND_GVT_IOC")+resultSet.getDouble("IND_GVT_IOC");
        		gasPurchase.setIoc_industrial(ind_ioc);
        		comm_ioc=resultSet.getDouble("COMM_GVT_IOC")+resultSet.getDouble("COMM_PVT_IOC");
        		gasPurchase.setIoc_commercial(comm_ioc);
        		domestic_ioc=resultSet.getDouble("DOM_M_GVT_IOC")+resultSet.getDouble("DOM_M_PVT_IOC")+resultSet.getDouble("DOM_NM_GVT_IOC")+resultSet.getDouble("DOM_NM_PVT_IOC");
        		gasPurchase.setIoc_Domestic(domestic_ioc);
        		gasPurchase.setIoc_fertilizer(resultSet.getDouble("FERTILIZER_GVT_IOC")+resultSet.getDouble("FERTILIZER_PVT_IOC"));
        		gasPurchase.setIoc_tea(resultSet.getDouble("TEA_GVT_IOC")+resultSet.getDouble("TEA_GVT_IOC"));
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("PW_GVT_SGFL"));
        		gasPurchase.setSgfl_power_pvt(resultSet.getDouble("PW_PVT_SGFL"));
        		System.out.println("Pow_Sgcl_gvt="+resultSet.getDouble("PW_GVT_SGFL"));
        		System.out.println("Pow_Sgcl_pvt="+resultSet.getDouble("PW_PVT_SGFL"));
        		gasPurchase.setSgfl_captive_gvt(resultSet.getDouble("CAP_GVT_SGFL"));
        		gasPurchase.setSgfl_captive_pvt(resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_cng_gvt(resultSet.getDouble("CNG_GVT_SGFL"));
        		gasPurchase.setSgfl_cng_pvt(resultSet.getDouble("CNG_PVT_SGFL"));
        		gasPurchase.setSgfl_industry_gvt(resultSet.getDouble("IND_GVT_SGFL"));
        		gasPurchase.setSgfl_industry_pvt(resultSet.getDouble("IND_PVT_SGFL"));
        		gasPurchase.setSgfl_comm_gvt(resultSet.getDouble("COMM_GVT_SGFL"));
        		gasPurchase.setSgfl_comm_pvt(resultSet.getDouble("COMM_PVT_SGFL"));
        		gasPurchase.setSgfl_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT_SGFL"));
        		gasPurchase.setSgfl_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT_SGFL"));
        		gasPurchase.setSgfl_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT_SGFL"));
        		gasPurchase.setSgfl_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT_SGFL"));
        		gasPurchase.setSgfl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT_SGFL"));
        		gasPurchase.setSgfl_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT_SGFL"));
        		gasPurchase.setSgfl_tea_gvt(resultSet.getDouble("TEA_GVT_SGFL"));
        		gasPurchase.setSgfl_tea_pvt(resultSet.getDouble("TEA_PVT_SGFL"));
        		
        		powper_sgfl=resultSet.getDouble("PW_GVT_SGFL")+resultSet.getDouble("PW_PVT_SGFL");
        		gasPurchase.setSgfl_power(powper_sgfl);   
        		cap_sgfl=resultSet.getDouble("CAP_GVT_SGFL")+resultSet.getDouble("CAP_PVT_SGFL");
        		gasPurchase.setSgfl_captive(cap_sgfl);
        		cng_sgfl=resultSet.getDouble("CNG_GVT_SGFL")+resultSet.getDouble("CNG_PVT_SGFL");
        		gasPurchase.setSgfl_cng(cng_sgfl);
        		ind_sgfl=resultSet.getDouble("IND_GVT_SGFL")+resultSet.getDouble("IND_PVT_SGFL");
        		gasPurchase.setSgfl_industrial(ind_sgfl);
        		comm_sgfl=resultSet.getDouble("COMM_GVT_SGFL")+resultSet.getDouble("COMM_PVT_SGFL");
        		gasPurchase.setSgfl_commercial(comm_sgfl);
        		domestic_sgfl=resultSet.getDouble("DOM_M_GVT_SGFL")+resultSet.getDouble("DOM_M_PVT_SGFL")+resultSet.getDouble("DOM_NM_GVT_SGFL")+resultSet.getDouble("DOM_NM_PVT_SGFL");
        		gasPurchase.setSgfl_Domestic(domestic_sgfl);
        		gasPurchase.setSgfl_fertilizer(resultSet.getDouble("FERTILIZER_GVT_SGFL")+resultSet.getDouble("FERTILIZER_PVT_SGFL"));
        		gasPurchase.setSgfl_tea(resultSet.getDouble("TEA_GVT_SGFL")+resultSet.getDouble("TEA_GVT_SGFL"));
        	
        		gasPurchase.setTotal_power(powper_bgfcl+powper_ioc+powper_sgfl);
        		gasPurchase.setTotal_captive(cap_bgfcl+cap_ioc+cap_sgfl);
        		gasPurchase.setTotal_cng(cng_bgfcl+cng_ioc+cng_sgfl);
        		gasPurchase.setTotal_industrial(ind_bgfcl+ind_ioc+ind_sgfl);
        		gasPurchase.setTotal_commercial(comm_bgfcl+comm_ioc+comm_sgfl);
        		gasPurchase.setTotal_domestic(domestic_bgfcl+domestic_ioc+domestic_sgfl);
        		
        		
        		System.out.println("Power BGFCL Total : "+consumption_format.format(powper_bgfcl));
        		System.out.println("Captive BGFCL Total : "+consumption_format.format(cap_bgfcl));
        		System.out.println("CNG BGFCL Total : "+consumption_format.format(cng_bgfcl));
        		System.out.println("Indus BGFCL Total : "+consumption_format.format(ind_bgfcl));
        		System.out.println("Commercial BGFCL Total : "+consumption_format.format(comm_bgfcl));
        		System.out.println("Domestic BGFCL Total : "+consumption_format.format(domestic_bgfcl));
        		System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        		System.out.println("Power SGFL Total : "+consumption_format.format(powper_sgfl));
        		System.out.println("Captive SGFL Total : "+consumption_format.format(cap_sgfl));
        		System.out.println("CNG SGFL Total : "+consumption_format.format(cng_sgfl));
        		System.out.println("Industial SGFL Total : "+consumption_format.format(ind_sgfl));
        		System.out.println("Commercial SGFL Total : "+consumption_format.format(comm_sgfl));
        		System.out.println("Domestic SGFL Total : "+consumption_format.format(domestic_sgfl));
        		
        		System.out.println("Total Power : "+consumption_format.format(powper_bgfcl+powper_ioc+powper_sgfl));
        		System.out.println("Total Captive : "+consumption_format.format(cap_bgfcl+cap_ioc+cap_sgfl));
        		System.out.println("Total CNG : "+consumption_format.format(cng_bgfcl+cng_ioc+cng_sgfl));
        		System.out.println("Total Indus : "+consumption_format.format(ind_bgfcl+ind_ioc+ind_sgfl));
        		System.out.println("Total Comm : "+consumption_format.format(comm_bgfcl+comm_ioc+comm_sgfl));
        		System.out.println("Total Domestic : "+consumption_format.format(domestic_bgfcl+domestic_ioc+domestic_sgfl));
        	}
      
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gasPurchase;
	}
	
	private HashMap<String, Double> getMonthlySalesInformation()
	{
		HashMap<String, Double> monthlySalesInfo = new HashMap<String, Double>();
		monthlySalesInfo.put("dom_m_pvt",0.0);
		monthlySalesInfo.put("dom_m_gvt",0.0);
		monthlySalesInfo.put("dom_nm_gvt", 0.0);
		monthlySalesInfo.put("dom_nm_pvt", 0.0);
		monthlySalesInfo.put("comm_pvt",0.0);
		monthlySalesInfo.put("comm_gvt",0.0);
		monthlySalesInfo.put("ind_pvt",0.0);
		monthlySalesInfo.put("ind_gvt",0.0);
		monthlySalesInfo.put("cap_pvt",0.0);
		monthlySalesInfo.put("cap_gvt",0.0);
		monthlySalesInfo.put("cng_pvt",0.0);
		monthlySalesInfo.put("cng_gvt",0.0);
		monthlySalesInfo.put("pow_pvt",0.0);
		monthlySalesInfo.put("pow_gvt",0.0);
		monthlySalesInfo.put("total_sales_pgcl",0.0);
		Double total_sales_pgcl=0.0;
		
		//PreparedStatement ps1=null;
	
		
		try {
			
			
			
			String monthly_sales_metered="select SUM(BILLED_CONSUMPTION) BILLED_CONSUMPTION,CUSTOMER_CATEGORY from bill_metered " +
					" where BILL_MONTH="+bill_month+
					" AND BILL_YEAR="+bill_year+
					" group by CUSTOMER_CATEGORY " +
					" order by CUSTOMER_CATEGORY";
			String monthly_sales_non_metered="select SUM(TOTAL_CONSUMPTION) BILLED_CONSUMPTION,CUSTOMER_CATEGORY from bill_non_metered " +
					" where BILL_MONTH="+bill_month+
					" AND BILL_YEAR="+bill_year+
					" group by CUSTOMER_CATEGORY " +
					" order by CUSTOMER_CATEGORY";




			
			PreparedStatement ps1=conn.prepareStatement(monthly_sales_metered);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		if(resultSet.getString("CUSTOMER_CATEGORY").equals("01"))
        		{
        			monthlySalesInfo.put("dom_m_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        			
        		}else if(resultSet.getString("CUSTOMER_CATEGORY").equals("02"))
        		{
        			monthlySalesInfo.put("dom_m_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("03"))
        		{
        			monthlySalesInfo.put("comm_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("04"))
        		{
        			monthlySalesInfo.put("comm_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("05"))
        		{
        			monthlySalesInfo.put("ind_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("06"))
        		{
        			monthlySalesInfo.put("ind_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("07"))
        		{
        			monthlySalesInfo.put("cap_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("08"))
        		{
        			monthlySalesInfo.put("cap_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("09"))
        		{
        			monthlySalesInfo.put("cng_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("10"))
        		{
        			monthlySalesInfo.put("cng_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("11"))
        		{
        			monthlySalesInfo.put("pow_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("12"))
        		{
        			monthlySalesInfo.put("pow_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        	}
             ps1=conn.prepareStatement(monthly_sales_non_metered);
        	 resultSet=ps1.executeQuery();
     	
        	while(resultSet.next())
        	{
        		if(resultSet.getString("CUSTOMER_CATEGORY").equals("01"))
        		{
        			monthlySalesInfo.put("dom_nm_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}else if(resultSet.getString("CUSTOMER_CATEGORY").equals("02"))
        		{
        			monthlySalesInfo.put("dom_nm_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_pgcl=total_sales_pgcl+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
         			
        	}
        	
        	
        	monthlySalesInfo.put("total_sales_pgcl",total_sales_pgcl);
        	
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthlySalesInfo;
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
	
	


	
  }


