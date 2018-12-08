package org.pgcl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.pgcl.dto.BurnerQntChangeDTO;
import org.pgcl.dto.MeterRentChangeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.utils.cache.CacheUtil;
import org.pgcl.utils.connection.ConnectionManager;
import org.pgcl.utils.connection.TransactionManager;

public class BurnerQntChangeService {

	public ResponseDTO saveBurnerQntChangeInfo(BurnerQntChangeDTO bqc)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String sqlUpdate="";
		OracleCallableStatement stmt = null;
		int response_code=0;
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		String totalPermanentDisconnBurner= String.valueOf(Integer.valueOf(bqc.getOld_pdisconnected_burner_qnt().equals("")?"0":bqc.getOld_pdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt_permanent().equals("")?"0":bqc.getNew_reconnected_burner_qnt_permanent()));
		String totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(bqc.getOld_tdisconnected_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt())+Integer.valueOf(bqc.getOld_tdisconnected_half_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_half_burner_qnt()));
			if(bqc.getIsTempToPerDiss()!=null && bqc.getIsTempToPerDiss().equals("1")){
				totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(totalTemporaryDisconnBurner)-Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt()));
				bqc.setDisconn_type("03");
			}
	
			try
			{
				
				
				//System.out.println("===>>Procedure : [Save_burner_management_info] START");            
	            stmt = (OracleCallableStatement) conn.prepareCall("{ call SAVE_BURNER_MANGEMENT_INFO(?,?,?,?,?,?,?,?,?,?," +
	            		"																			 ?,?,?,?,?,?,?,?,?,?)  }");
	            //System.out.println("==>>Procedure : [Save_burner_management_info] END");

	            stmt.setString(1, bqc.getCustomer_id()); 
	            stmt.setString(2, bqc.getOld_double_burner_qnt_billcal()); 
	            stmt.setString(3,bqc.getOld_double_burner_qnt()); 
	            stmt.setString(4,bqc.getNew_double_qnt_billcal().equals("")?"0":bqc.getNew_double_qnt_billcal());
	            stmt.setString(5,bqc.getNew_double_burner_qnt().equals("")?"0":bqc.getNew_double_burner_qnt());
	            stmt.setString(6,bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt());
	            stmt.setString(7,bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt());
	            stmt.setString(8,bqc.getDisconn_type());
	            stmt.setString(9,bqc.getNew_permanent_disconnected_cause());
	            stmt.setString(10,bqc.getNew_incrased_burner_qnt().equals("")?"0":bqc.getNew_incrased_burner_qnt());
	            stmt.setString(11,bqc.getNew_reconnected_burner_qnt_permanent().equals("")?"0":bqc.getNew_reconnected_burner_qnt_permanent());
	            stmt.setString(12,bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt());
	            stmt.setString(13,bqc.getReconnection_cause());
	            stmt.setString(14,bqc.getEffective_date());
	            stmt.setString(15,bqc.getRemarks().replaceAll("\\s+",""));
	            stmt.setString(16,bqc.getInsert_by());
	            stmt.setString(17,totalPermanentDisconnBurner);
	            stmt.setString(18,totalTemporaryDisconnBurner);      
	            stmt.registerOutParameter(19, java.sql.Types.INTEGER);
	            stmt.registerOutParameter(20, java.sql.Types.VARCHAR);
	            
	            stmt.executeUpdate();
	            response_code = stmt.getInt(19);
	            response.setMessasge(stmt.getString(20));
	            if(response_code == 1){
	            	response.setResponse(true);
	            }
	            else{
	            	response.setResponse(false);
	            }
				
				String cKey="CUSTOMER_INFO_"+bqc.getCustomer_id();
				CacheUtil.clear(cKey);

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

	 		return response;
	}
	
	public ResponseDTO updateBurnerQntChangeInfo(BurnerQntChangeDTO bqc)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String totalPermanentDisconnBurner= String.valueOf(Integer.valueOf(bqc.getOld_pdisconnected_burner_qnt().equals("")?"0":bqc.getOld_pdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt()));
		String totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(bqc.getOld_tdisconnected_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt()));;
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
						  
		String sqlUpdate1 = " UPDATE BURNER_QNT_CHANGE SET NEW_DOUBLE_BURNER_QNT=?,NEW_PERMANENT_DISCON_QNT=?,DISCONN_CAUSE=?,NEW_TEMPORARY_DISCONN_QNT=?,"
							+ "NEW_INCREASED_QNT=?,NEW_RECONN_QNT=?,"
							+ "NEW_DOUBLE_BURNER_QNT_BILLCAL=?,TOTAL_TDISCONNECTED_BURNER_QNT=?,TOTAL_PDISCONNECTED_BURNER_QNT=?,"
							+ "EFFECTIVE_DATE=TO_DATE(?, 'DD-MM-YYYY'), REMARKS=? "
							+ "WHERE PID=? ";
		String sqlUpdate2=" Update CUSTOMER_CONNECTION set  DOUBLE_BURNER_QNT=?,DOUBLE_BURNER_QNT_BILLCAL=?,PDISCONNECTED_BURNER_QNT=?,TDISCONNECTED_BURNER_QNT=? where customer_id=?";
		
		
		
		PreparedStatement stmt = null;
		try
		{			
			stmt = conn.prepareStatement(sqlUpdate1);
			
			stmt.setString(1,bqc.getNew_double_burner_qnt());
			stmt.setString(2,bqc.getNew_permanent_disconnected_burner_qnt());
			stmt.setString(3,bqc.getNew_permanent_disconnected_cause());
			stmt.setString(4,bqc.getNew_temporary_disconnected_burner_qnt());
			stmt.setString(5,bqc.getNew_incrased_burner_qnt());
			stmt.setString(6,bqc.getNew_reconnected_burner_qnt());
			stmt.setString(7,bqc.getNew_double_burner_billcal_qnt());
			stmt.setString(8,totalTemporaryDisconnBurner);
			stmt.setString(9,totalPermanentDisconnBurner);
			stmt.setString(10,bqc.getEffective_date());
			stmt.setString(11,bqc.getRemarks());
			stmt.setString(11,bqc.getPid());
			stmt.execute();
			
			
			stmt = conn.prepareStatement(sqlUpdate2);
			stmt.setString(1,bqc.getNew_double_burner_qnt());
			stmt.setString(2,bqc.getNew_double_qnt_billcal());
			stmt.setString(3,totalPermanentDisconnBurner);
			stmt.setString(4,totalTemporaryDisconnBurner);
			stmt.setString(5,bqc.getCustomer_id());
			stmt.execute();
			
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Burner Quantity Change Information.");
			response.setResponse(true);
			
			String cKey="CUSTOMER_INFO_"+bqc.getCustomer_id();
			CacheUtil.clear(cKey);
			
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
	 		
	 		return response;
	 	
	}
	
	public ArrayList<BurnerQntChangeDTO> getBurnerQntChangeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		BurnerQntChangeDTO bChangeDTO=null;
		ArrayList<BurnerQntChangeDTO> burnerChangeList=new ArrayList<BurnerQntChangeDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = "SELECT BQC.PID,BQC.CUSTOMER_ID,CPI.FULL_NAME, " +
						"BQC.OLD_DOUBLE_BURNER_QNT,BQC.NEW_DOUBLE_BURNER_QNT,BQC.NEW_PERMANENT_DISCON_QNT,BQC.NEW_TEMPORARY_DISCONN_QNT, " +
						"BQC.NEW_INCREASED_QNT,BQC.NEW_RECONN_QNT_4M_TEMPORARY+BQC.NEW_RECONN_QNT_4M_TEMP_HALF  NEW_RECONN_QNT_4M_TEMPORARY,BQC.NEW_RECONN_QNT_4M_PERMANENT,BQC.DISCONN_CAUSE,BQC.TOTAL_TDISCONNECTED_BURNER_QNT,BQC.TOTAL_PDISCONNECTED_BURNER_QNT, " +
						"EFFECTIVE_DATE,TO_CHAR (EFFECTIVE_DATE,'dd-MM-YYYY') EFFECTIVE_DATE_VIEW,REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME " +
						"FROM BURNER_QNT_CHANGE BQC, CUSTOMER_PERSONAL_INFO CPI,MST_AREA,CUSTOMER " +
						"WHERE BQC.CUSTOMER_ID = CPI.CUSTOMER_ID "+
						" AND CUSTOMER.CUSTOMER_ID=CPI.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
						(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( SELECT BQC.PID,BQC.CUSTOMER_ID,CPI.FULL_NAME, " +
						 "BQC.OLD_DOUBLE_BURNER_QNT,BQC.NEW_DOUBLE_BURNER_QNT,BQC.NEW_PERMANENT_DISCON_QNT,BQC.NEW_TEMPORARY_DISCONN_QNT, " +
						 "BQC.NEW_INCREASED_QNT,BQC.NEW_RECONN_QNT_4M_TEMPORARY+BQC.NEW_RECONN_QNT_4M_TEMP_HALF  NEW_RECONN_QNT_4M_TEMPORARY,BQC.NEW_RECONN_QNT_4M_PERMANENT,BQC.DISCONN_CAUSE,BQC.TOTAL_TDISCONNECTED_BURNER_QNT,BQC.TOTAL_PDISCONNECTED_BURNER_QNT, " +
						"trunc(EFFECTIVE_DATE) EFFECTIVE_DATE,TO_CHAR (EFFECTIVE_DATE,'dd-MM-YYYY') EFFECTIVE_DATE_VIEW,REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME " +
						"FROM BURNER_QNT_CHANGE BQC, CUSTOMER_PERSONAL_INFO CPI,MST_AREA,CUSTOMER " +
						"WHERE BQC.CUSTOMER_ID = CPI.CUSTOMER_ID  "+
						" AND CUSTOMER.CUSTOMER_ID=CPI.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
						(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	    " )tmp1 " +
				  	    " )tmp2   " +
				  	    " Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					bChangeDTO=new BurnerQntChangeDTO();
					
					bChangeDTO.setPid(r.getString("PID"));
					bChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					bChangeDTO.setCustomer_name(r.getString("FULL_NAME"));
					bChangeDTO.setOld_double_burner_qnt(r.getString("OLD_DOUBLE_BURNER_QNT"));
					bChangeDTO.setNew_permanent_disconnected_burner_qnt(r.getString("NEW_PERMANENT_DISCON_QNT"));
					bChangeDTO.setNew_temporary_disconnected_burner_qnt(r.getString("NEW_TEMPORARY_DISCONN_QNT"));
					bChangeDTO.setNew_incrased_burner_qnt(r.getString("NEW_INCREASED_QNT"));
					bChangeDTO.setNew_reconnected_burner_qnt(r.getString("NEW_RECONN_QNT_4M_TEMPORARY"));
					bChangeDTO.setNew_reconnected_burner_qnt_permanent(r.getString("NEW_RECONN_QNT_4M_PERMANENT"));
					bChangeDTO.setNew_double_burner_qnt(r.getString("NEW_DOUBLE_BURNER_QNT"));
					bChangeDTO.setOld_pdisconnected_burner_qnt(r.getString("TOTAL_PDISCONNECTED_BURNER_QNT"));
					bChangeDTO.setOld_tdisconnected_burner_qnt(r.getString("TOTAL_TDISCONNECTED_BURNER_QNT"));
					bChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE_VIEW"));
					bChangeDTO.setRemarks(r.getString("REMARKS"));

					burnerChangeList.add(bChangeDTO);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return burnerChangeList;
	}
	
	public BurnerQntChangeDTO getBurnerQntChangeInfo(String pid)
	{		
		BurnerQntChangeDTO bqChangeDTO=null;
		Connection conn = ConnectionManager.getConnection();
		
		String sql= "SELECT BQC.PID, BQC.CUSTOMER_ID,CPI.FULL_NAME,BQC.OLD_DOUBLE_BURNER_QNT,BQC.NEW_DOUBLE_BURNER_QNT,BQC.NEW_PERMANENT_DISCON_QNT,BQC.NEW_TEMPORARY_DISCONN_QNT,  " +
					"BQC.OLD_DOUBLE_BURNER_QNT_BILLCALL,BQC.NEW_DOUBLE_BURNER_QNT_BILLCAL,BQC.TOTAL_PDISCONNECTED_BURNER_QNT,BQC.TOTAL_TDISCONNECTED_BURNER_QNT,"+
					" BQC.DISCONN_CAUSE,BQC.NEW_INCREASED_QNT,BQC.NEW_RECONN_QNT_4M_TEMPORARY NEW_RECONN_QNT,BQC.REMARKS, BQC.INSERT_BY, TO_CHAR (BQC.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE " +
					" FROM BURNER_QNT_CHANGE BQC,CUSTOMER_PERSONAL_INFO CPI " +
					" WHERE BQC.CUSTOMER_ID=CPI.CUSTOMER_ID  and PID= ?";
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, pid);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					bqChangeDTO=new BurnerQntChangeDTO();
					bqChangeDTO.setPid(r.getString("PID"));
					bqChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					bqChangeDTO.setCustomer_name(r.getString("FULL_NAME"));					
					bqChangeDTO.setOld_double_burner_qnt(r.getString("OLD_DOUBLE_BURNER_QNT"));
					bqChangeDTO.setOld_double_burner_qnt_billcal(r.getString("OLD_DOUBLE_BURNER_QNT_BILLCALL"));
					bqChangeDTO.setNew_permanent_disconnected_burner_qnt(r.getString("NEW_PERMANENT_DISCON_QNT"));
					bqChangeDTO.setNew_permanent_disconnected_cause(r.getString("DISCONN_CAUSE"));
					bqChangeDTO.setNew_temporary_disconnected_burner_qnt(r.getString("NEW_TEMPORARY_DISCONN_QNT"));
					bqChangeDTO.setNew_incrased_burner_qnt(r.getString("NEW_INCREASED_QNT"));
					bqChangeDTO.setNew_reconnected_burner_qnt(r.getString("NEW_RECONN_QNT"));
					bqChangeDTO.setNew_double_burner_qnt(r.getString("NEW_DOUBLE_BURNER_QNT"));
					bqChangeDTO.setNew_double_qnt_billcal(r.getString("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
					bqChangeDTO.setOld_pdisconnected_burner_qnt(r.getString("TOTAL_PDISCONNECTED_BURNER_QNT"));
					bqChangeDTO.setOld_tdisconnected_burner_qnt(r.getString("TOTAL_TDISCONNECTED_BURNER_QNT"));
					bqChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE"));
					bqChangeDTO.setRemarks(r.getString("REMARKS"));
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return bqChangeDTO;	 	
	}
	
	public ResponseDTO deleteBurnerQntChangeInfo(String burnerQntChangeId)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlChangeInfo="Select * from BURNER_QNT_CHANGE  Where PID=?";						  
		String sqlDeleteInfo="Delete BURNER_QNT_CHANGE Where PID=?";
		String sqlUpdateInfo=" Update CUSTOMER_CONNECTION set SINGLE_BURNER_QNT=?, DOUBLE_BURNER_QNT=? Where CUSTOMER_ID=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlChangeInfo);
				stmt.setString(1,burnerQntChangeId);				
				ResultSet r =stmt.executeQuery();
				String old_single_burner="";
				String old_double_burner="";
				String customer_id="";
				
				
				if (r.next())
				{
					old_single_burner=r.getString("OLD_SINGLE_BURNER_QNT");
					old_double_burner=r.getString("OLD_DOUBLE_BURNER_QNT");
					customer_id=r.getString("CUSTOMER_ID");
					
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteInfo);
				stmt.setString(1,burnerQntChangeId);
				stmt.execute();
				
			
				stmt = conn.prepareStatement(sqlUpdateInfo);
				stmt.setString(1,old_single_burner);
				stmt.setString(2,old_double_burner);
				stmt.setString(3,customer_id);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Meter Rent Change Information.");
				response.setResponse(true);				
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

	 		return response;
	 	
	}
}
