package org.pgcl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.struts2.ServletActionContext;
import org.pgcl.dto.CustomerConnectionDTO;
import org.pgcl.dto.DisconnectDTO;
import org.pgcl.dto.MeterTypeDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.dto.UserDTO;
import org.pgcl.enums.DisconnCause;
import org.pgcl.enums.DisconnType;
import org.pgcl.utils.AC;
import org.pgcl.utils.Utils;
import org.pgcl.utils.cache.CacheUtil;
import org.pgcl.utils.connection.ConnectionManager;
import org.pgcl.utils.connection.TransactionManager;

import com.google.gson.Gson;

public class ConnectionService {
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	public ResponseDTO saveConnectionInfo(CustomerConnectionDTO connection)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		String parentConfirmSql="";
		String sql=" MERGE INTO CUSTOMER_CONNECTION USING dual " +
				   " ON (customer_id = ?) " +
				   " WHEN MATCHED THEN " +
				   " UPDATE SET CONNECTION_DATE=to_date(?,'dd-MM-YYYY'),STATUS=? "+
				   " WHEN NOT MATCHED THEN " +
				   " Insert(CUSTOMER_ID, ISMETERED, CONNECTION_TYPE, PARENT_CONNECTION, MIN_LOAD,MAX_LOAD, SINGLE_BURNER_QNT,DOUBLE_BURNER_QNT, CONNECTION_DATE, MINISTRY_ID, VAT_REBATE,HHV_NHV,PAY_WITHIN_WO_SC,PAY_WITHIN_W_SC,STATUS,HAS_SUB_CONNECTION,DOUBLE_BURNER_QNT_BILLCAL,PDISCONNECTED_BURNER_QNT,TDISCONNECTED_BURNER_QNT)" +
				   " Values(?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),?,?,?,?,?,?,?,?,?,?) ";
		
		String burnerInfoSql="INSERT INTO BURNER_QNT_CHANGE ( PID, CUSTOMER_ID,NEW_DOUBLE_BURNER_QNT,NEW_DOUBLE_BURNER_QNT_BILLCAL,EFFECTIVE_DATE, REMARKS,INSERT_BY, INSERT_DATE,OLD_DOUBLE_BURNER_QNT,OLD_DOUBLE_BURNER_QNT_BILLCALL ) VALUES (SQN_CNG_BURNER_QNT.NEXTVAL,?,?,?,to_date(?,'dd-MM-YYYY'),?,?,SYSDATE,?,?)";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,connection.getCustomer_id());
				stmt.setString(2,connection.getConnection_date());
				stmt.setString(3,connection.getStatus_str());
				
				stmt.setString(4,connection.getCustomer_id());
				stmt.setString(5,connection.getIsMetered_str());
				stmt.setString(6,connection.getConnection_type_str());
				stmt.setString(7,connection.getParent_connection());
				stmt.setString(8,connection.getMin_load());
				stmt.setString(9,connection.getMax_load());
				stmt.setInt(10,connection.getSingle_burner_qnt());
				stmt.setInt(11,connection.getDouble_burner_qnt());
				stmt.setString(12,connection.getConnection_date());
				stmt.setString(13,connection.getMinistry_id());
				stmt.setDouble(14,connection.getVat_rebate());
				stmt.setFloat(15, connection.getHhv_nhv());
				stmt.setInt(16, connection.getPay_within_wo_sc());
				stmt.setInt(17, connection.getPay_within_w_sc());
				stmt.setString(18,connection.getStatus_str());
				stmt.setString(19,"N");
				stmt.setFloat(20,Float.valueOf(connection.getDouble_burner_qnt()));
				stmt.setFloat(21,Float.valueOf("0"));
				stmt.setFloat(22,Float.valueOf("0"));
				
				
				stmt.executeUpdate();
				if(connection.getParent_connection()!=null && !connection.getParent_connection().equalsIgnoreCase(""))
				{
					 parentConfirmSql="UPDATE CUSTOMER_CONNECTION SET HAS_SUB_CONNECTION = 'Y' WHERE customer_id ='"+connection.getParent_connection()+"'";
					 stmt = conn.prepareStatement(parentConfirmSql);
					 stmt.executeUpdate();
				}
				if(connection.getIsMetered_str().equals("0"))
				{
					String dayOfmonth=connection.getConnection_date().substring(0,2);
					stmt = conn.prepareStatement(burnerInfoSql);
					stmt.setString(1,connection.getCustomer_id());
					stmt.setInt(2,connection.getDouble_burner_qnt());
					stmt.setInt(3,connection.getDouble_burner_qnt());
					stmt.setString(4,connection.getConnection_date());
					stmt.setString(5,"'Balance Transfer'");
					stmt.setString(6,loggedInUser.getUserName());
					stmt.setInt(7,dayOfmonth.equals("01")?connection.getDouble_burner_qnt():0);
					stmt.setInt(8,dayOfmonth.equals("01")?connection.getDouble_burner_qnt():0);
					stmt.executeUpdate();
				}
					
				transactionManager.commit();
				response.setMessasge("Successfully Saved Connection Information.");
				response.setResponse(true);
				
				String cKey="CUSTOMER_INFO_"+connection.getCustomer_id();
				CacheUtil.clear(cKey);
				
			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;

	}


	

	public static DisconnectDTO getLatestDisconnectInfo(String customer_id,String meter_id)
	{
		DisconnectDTO disconnInfo=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		if(meter_id==null || meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
			   sql="SELECT disconn_info_nonmetered.*,to_char(DISCONNECT_DATE,'dd-MM-YYYY HH24:MI') DISCONNECT_DATE_FORMATTED " +
			   "  FROM disconn_info_nonmetered " +
			   " WHERE customer_id = ? " +
			   "   AND (pid, disconnect_date) IN ( " +
			   "          SELECT pid, disconnect_date " +
			   "            FROM disconn_info_nonmetered " +
			   "           WHERE customer_id = ? " +
			   "             AND disconnect_date IN (SELECT MAX (disconnect_date) " +
			   "                                       FROM disconn_info_nonmetered " +
			   "                                      WHERE customer_id = ?))";

		else
			sql="SELECT d.*,r.*,to_char(DISCONNECT_DATE,'dd-MM-YYYY HH24:MI') DISCONNECT_DATE_FORMATTED " +
			"  FROM disconn_info_metered d, meter_reading r " +
			" WHERE d.customer_id = ? " +
			"   AND (pid, disconnect_date) IN ( " +
			"          SELECT pid, disconnect_date " +
			"            FROM DISCONN_INFO_METERED " +
			"           WHERE customer_id = ? " +
			"             AND pid IN ( " +
			"                         SELECT MAX (pid) " +
			"                           FROM disconn_info_metered " +
			"                          WHERE customer_id = ? " +
			"                                AND meter_id = ?)) " +
			"   AND d.reading_id = r.reading_id ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(meter_id==null || meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
				{
					stmt.setString(1, customer_id);
					stmt.setString(2, customer_id);
					stmt.setString(3, customer_id);
				}
				else
				{
					stmt.setString(1, customer_id);
					stmt.setString(2, customer_id);
					stmt.setString(3, customer_id);
					stmt.setString(4, meter_id);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					disconnInfo=new DisconnectDTO();
					disconnInfo.setPid(r.getString("PID"));
					disconnInfo.setCustomer_id(r.getString("CUSTOMER_ID"));
					disconnInfo.setDisconnect_cause(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")]);
					disconnInfo.setDisconnect_cause_str(String.valueOf(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getId()));
					disconnInfo.setDisconnect_cause_name(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
					disconnInfo.setDisconnect_type(DisconnType.values()[r.getInt("DISCONNECT_TYPE")]);
					disconnInfo.setDisconnect_type_str(String.valueOf(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getId()));
					disconnInfo.setDisconnect_type_name(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());
					disconnInfo.setDisconnect_date(r.getString("DISCONNECT_DATE_FORMATTED"));
					disconnInfo.setRemarks(r.getString("REMARKS"));
					disconnInfo.setInsert_by(r.getString("INSERT_BY"));
					disconnInfo.setInsert_date(r.getString("INSERT_DATE"));
					if(meter_id!=null && !meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
					{
						disconnInfo.setMeter_id(r.getString("METER_ID"));
						disconnInfo.setMeter_reading(r.getString("CURRENT_READING"));
					}
					disconnInfo.toString();
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return disconnInfo;
	}
	
	
}
