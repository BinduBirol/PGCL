package org.pgcl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;

import org.pgcl.dto.ConnectionLedgerDTO;
import org.pgcl.dto.CustomerLedgerDTO;
import org.pgcl.dto.DepositLedgerDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.utils.connection.ConnectionManager;

public class LedgerService {

	public ArrayList<CustomerLedgerDTO> getCustomerLedger(String customer_id)
	{
		CustomerLedgerDTO entry=null;
		ArrayList<CustomerLedgerDTO> ledger=new ArrayList<CustomerLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql=" Select TRANS_ID,to_char(TRANS_DATE,'DD-MON-RRRR') TRANS_DATE_F1,PARTICULARS,DEBIT,CREDIT,BALANCE,STATUS" +
				   " FROM CUSTOMER_LEDGER Where Customer_Id = ? And STATUS=1 Order By TRANS_DATE,TRANS_ID, INSERTED_ON Asc ";
		   
		
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,customer_id);
				r = stmt.executeQuery();
				while (r.next())
				{
					entry=new CustomerLedgerDTO();
					entry.setEntry_type(r.getString("TRANS_ID"));
					entry.setIssue_paid_date(r.getString("TRANS_DATE_F1"));
					entry.setParticulars(r.getString("PARTICULARS"));
					entry.setDebit_amount(r.getString("DEBIT"));
					entry.setCredit_amount(r.getString("CREDIT"));
					entry.setBalance_amount(r.getDouble("BALANCE"));
					entry.setStatus(r.getString("STATUS"));
					ledger.add(entry);
				}
				
				double balance=0d;
				for(int i=0;i<ledger.size();i++)
				{
					if(i==0) 
						balance=Double.valueOf(ledger.get(i).getBalance_amount());
					
					//System.out.println("Balance : "+balance+", Debit : "+ledger.get(i).getDebit_amount()+", Credit : "+ledger.get(i).getCredit_amount());
					balance=balance+Double.valueOf(ledger.get(i).getDebit_amount()==null?"0":ledger.get(i).getDebit_amount())-Double.valueOf(ledger.get(i).getCredit_amount()==null?"0":ledger.get(i).getCredit_amount());
					//System.out.print(" ===>> New Balance : "+balance);
					ledger.get(i).setBalance_amount(balance);
				}
				
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return ledger;
	}
	
	public ArrayList<DepositLedgerDTO> getDepositLedger(String customer_id)
	{
		DepositLedgerDTO entry=null;
		ArrayList<DepositLedgerDTO> ledger=new ArrayList<DepositLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql=" Select TRANS_ID,to_char(TRANS_DATE,'DD-MON-RRRR') TRANS_DATE_VIEW,TRANS_DATE,DESCRIPTION,SECURITY_AMOUNT,DEBIT,CREDIT,BALANCE,STATUS" +
				   " From CUSTOMER_SECURITY_LEDGER Where Customer_Id = ? Order By TRANS_DATE,INSERTED_ON Asc ";
		   
		   float balance=0;
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,customer_id);
				r = stmt.executeQuery();
				while (r.next())
				{
					entry=new DepositLedgerDTO();
					entry.setTrans_id(r.getString("TRANS_ID"));
					entry.setDeposit_date(r.getString("TRANS_DATE_VIEW"));
					entry.setDescription(r.getString("DESCRIPTION"));
					entry.setSecurity_amount(r.getString("SECURITY_AMOUNT"));
					entry.setDebit_amount(r.getString("DEBIT"));
					entry.setCredit_amount(r.getString("CREDIT"));
					balance=balance+(r.getString("DEBIT")==null?Float.valueOf("0"):Float.valueOf(r.getString("DEBIT")))-(r.getString("CREDIT")==null?Float.valueOf("0"):Float.valueOf(r.getString("CREDIT")));
					entry.setBalance_amount(String.valueOf(balance));
					entry.setStatus(r.getString("STATUS"));
					ledger.add(entry);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return ledger;
	}
	public ArrayList<ConnectionLedgerDTO> getConnectionLedger(String customer_id)
	{
		ArrayList<ConnectionLedgerDTO> ledger=new ArrayList<ConnectionLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		ConnectionLedgerDTO connection;
		try {
   			
   			System.out.println("===>>Procedure : [GET_CONNECTION_LEDGER] START");  
   			stmt = (OracleCallableStatement) conn.prepareCall("{ call GET_CONNECTION_LEDGER(?,?)  }");
   		    System.out.println("==>>Procedure : END");
          
            stmt.setString(1, customer_id);           
            stmt.registerOutParameter(2, OracleTypes.CURSOR);            
            stmt.execute();
            ResultSet rs = ((OracleCallableStatement)stmt).getCursor(2);
            
            while (rs.next()) {
            	connection=new ConnectionLedgerDTO();
            	connection.setEvent_date(rs.getString("Event_Date_View"));
            	connection.setDescription(rs.getString("Description"));
            	try{
            		connection.setMin_load(rs.getString("Min_Load"));
                	connection.setMax_load(rs.getString("Max_Load"));
            	}
            	catch(Exception ex){
            		connection.setSingle_burner(rs.getString("Single_Burner_Qnt"));
            		connection.setDouble_burner(rs.getString("Double_Burner_Qnt"));
            	}
            	ledger.add(connection);
    	      }
		} 
		catch (Exception e){e.printStackTrace();
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return ledger;
		
	}
	
	public ResponseDTO processCustomerLedgerBalance(String customer_id)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		try {
   			
   			//System.out.println("===>>Procedure : [PROCESS_BALANCE_CUST_ACCOUNT] START");  
   			stmt = (OracleCallableStatement) conn.prepareCall("{ call PROCESS_BALANCE_CUST_ACCOUNT(?,?,?,?)  }");
   		    //System.out.println("==>>Procedure : END");
          
            stmt.setString(1, customer_id);
            stmt.setString(2, "");           
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);
            stmt.registerOutParameter(4, java.sql.Types.VARCHAR);      
            stmt.executeUpdate();
            response_code = stmt.getInt(3);
            response.setMessasge(stmt.getString(4));
            
            if(response_code == 1){
            	response.setResponse(true);
            }
            else{            	
            	response.setResponse(false);
            }
		} 
		catch (Exception e){e.printStackTrace();
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return response;
		
	}
	
}
