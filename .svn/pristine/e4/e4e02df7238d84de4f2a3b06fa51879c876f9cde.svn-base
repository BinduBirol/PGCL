package org.pgcl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.pgcl.dto.ClearnessDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.ipg.IpgResponse;
import org.pgcl.dto.ipg.PaymentMethod;
import org.pgcl.utils.cache.CacheUtil;
import org.pgcl.utils.connection.ConnectionManager;
import org.pgcl.utils.connection.TransactionManager;

public class IpgService {

	public static List<PaymentMethod> getPaymentMethods()
	{
		String cKey="PAYMENT_METHODS";
		ArrayList<PaymentMethod> paymentMethodList=CacheUtil.getListFromCache(cKey,PaymentMethod.class);
		if(paymentMethodList!=null)
			return paymentMethodList;
		else
			paymentMethodList=new ArrayList<PaymentMethod>();

		
		PaymentMethod method=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
				  sql=" Select * from IPG_METHODS order by id ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);				
				r = stmt.executeQuery();
				while (r.next())
				{
					method=new PaymentMethod();
					method.setId(r.getString("ID"));
					method.setName(r.getString("NAME"));
					method.setImagUrl("/PGCL_WEB/resources/images/ipg/"+r.getString("IMAGE_URL"));
					paymentMethodList.add(method);
				}
				CacheUtil.setListToCache(cKey,paymentMethodList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return paymentMethodList;
	}
	
	public static PaymentMethod getPaymentMethod(String id)
	{
		String cKey="PAYMENT_METHOD_"+id;
		PaymentMethod paymentMethod= (PaymentMethod)CacheUtil.getObjFromCache(cKey);
		if(paymentMethod!=null)
			return paymentMethod;


		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
				  sql=" Select * from IPG_METHODS where id =? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);	
				stmt.setString(1, id);
				r = stmt.executeQuery();
				while (r.next())
				{
					paymentMethod=new PaymentMethod();
					paymentMethod.setId(r.getString("ID"));
					paymentMethod.setName(r.getString("NAME"));
					paymentMethod.setImagUrl(r.getString("IMAGE_URL"));
				}
				CacheUtil.setObjToCache(cKey,paymentMethod);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return paymentMethod;
	}
	
	public ResponseDTO initiateTransaction(String transId, String customerId, String paymentMethod, Double totalAmount, List<ClearnessDTO>  selectedBills)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		PreparedStatement mstStmt = null;
		PreparedStatement dtlStmt = null;		
		// VAliation goes here....
		
		
		

		String sqlIpgMst="Insert into IPG_MST(TRANSACTION_ID, CUSTOMER_ID, PAYMENT_METHOD, TOTAL_AMOUNT, STATUS) " +
				          " Values(?,?,?,?,?)";		
		String sqlIpgDtl="Insert into IPG_DTL( TRANSACTION_ID, CUSTOMER_ID, BILL_ID, BILL_AMOUNT, SURCHARGE_AMOUNT, TOTAL_AMOUNT) " +
		          " Values(?,?,?,?,?,?)";
		
	
			try
			{
			
				mstStmt = conn.prepareStatement(sqlIpgMst);
				mstStmt.setString(1,transId);
				mstStmt.setString(2, customerId);
				mstStmt.setString(3, paymentMethod);
				mstStmt.setDouble(4,totalAmount);
				mstStmt.setFloat(5,201);
				
				mstStmt.execute();
				
				for(ClearnessDTO bill : selectedBills){
				dtlStmt = conn.prepareStatement(sqlIpgDtl);				
				dtlStmt.setString(1,transId);
				dtlStmt.setString(2, customerId);
				dtlStmt.setString(3, bill.getBillId());
				dtlStmt.setDouble(4,bill.getDueAmount());
				dtlStmt.setDouble(5,0);
				dtlStmt.setDouble(6, 100);
				
				dtlStmt.addBatch();
				}
				
				dtlStmt.executeBatch();
				

				
				       
				
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
	 		finally{try{mstStmt.close();dtlStmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}mstStmt = null;dtlStmt = null;conn = null;}

	 		return response;
	 	
	}
	
	public ResponseDTO saveResponse(IpgResponse ipgResponse){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		PreparedStatement mstStmt = null;
		PreparedStatement dtlStmt = null;
		PreparedStatement responseStmt = null;
		

		
		String sqlResponse = "Insert into IPG_MST(TRANSACTION_ID, IPG_TRANSACTION_ID, STATUS,ERROR_MESSAGE, CARD_NO, CARD_NAME) " +
		          " Values(?,?,?,?,,??)";

		String sqlIpgMst="Update IPG_MST set status=? Where Trans_Id=? ";
		
		// Update bill collection information here......
		
	
			try
			{
			
				
				responseStmt = conn.prepareStatement(sqlResponse);
				responseStmt.setString(1,ipgResponse.getTransId());
				responseStmt.setString(2, ipgResponse.getIpgTrxId());
				responseStmt.setString(3, ipgResponse.getTxnStatus());
				responseStmt.setString(4,ipgResponse.getError_msg());
				responseStmt.setString(5,ipgResponse.getCard_no());
				responseStmt.setString(6,ipgResponse.getCardName());
				responseStmt.execute();
				
				
				
				mstStmt = conn.prepareStatement(sqlIpgMst);
				
				mstStmt.setInt(1, ipgResponse.getTxnStatus().equals("success")?200:444);
				mstStmt.setString(2,ipgResponse.getIpgTrxId());
				
				mstStmt.executeUpdate();
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
	 		finally{try{mstStmt.close();dtlStmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}mstStmt = null;dtlStmt = null;conn = null;}
		
		
			return response;
	}

}
