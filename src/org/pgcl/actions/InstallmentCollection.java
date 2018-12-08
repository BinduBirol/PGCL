package org.pgcl.actions;

import java.util.ArrayList;

import org.pgcl.dto.InstallmentCollectionDTO;
import org.pgcl.dto.InstallmentCollectionDtlDTO;
import org.pgcl.dto.InstallmentSegmentDTO;
import org.pgcl.dto.ResponseDTO;
import org.pgcl.models.InstallmentCollectionService;
import org.pgcl.models.InstallmentService;

import com.google.gson.Gson;

public class InstallmentCollection extends BaseAction {

	private static final long serialVersionUID = -3918217229482231201L;
	private String installmentId;
	private String collectionDetailStr;
	private InstallmentCollectionDTO collection;
	
	public String getCollectionInfo(){
		InstallmentCollectionService icService=new InstallmentCollectionService();	
		InstallmentService iService=new InstallmentService();
		
		InstallmentCollectionDTO installmentCollection=icService.getInstallmentCollectionInfo(installmentId);
		ArrayList<InstallmentSegmentDTO> segmentList=iService.getInstallmentSegmentList(installmentId);
		ArrayList<InstallmentCollectionDtlDTO> collectionDetailList=icService.getInstallmentCollectionDetailList(installmentId);
		
		Gson gson = new Gson();
		String billSegmentStr=gson.toJson(segmentList==null?"{}":segmentList);
		String collectedSegmentStr=gson.toJson(collectionDetailList==null?"{}":collectionDetailList);
		
		String json="{\"collection\":"+installmentCollection+",\"billSegments\":"+billSegmentStr+",\"collectedSegments\":"+collectedSegmentStr+"}";
		setJsonResponse(json);
        return null;        
	}

	public String saveCollection(){
		
		InstallmentCollectionService icService=new InstallmentCollectionService();
		ResponseDTO response=icService.saveInstallmentCollection(collection, collectionDetailStr);		
		setJsonResponse(response);
		return null;
		
	}
	public String getInstallmentId() {
		return installmentId;
	}
	public void setInstallmentId(String installmentId) {
		this.installmentId = installmentId;
	}

	public String getCollectionDetailStr() {
		return collectionDetailStr;
	}

	public void setCollectionDetailStr(String collectionDetailStr) {
		this.collectionDetailStr = collectionDetailStr;
	}

	public InstallmentCollectionDTO getCollection() {
		return collection;
	}

	public void setCollection(InstallmentCollectionDTO collection) {
		this.collection = collection;
	}
	
	
}
