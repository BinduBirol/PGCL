<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("bankBalanceUpdateHome.action");
	setTitle("Bank Book Balance Update");
</script>

<div class="row-fluid" style="width: 55%; padding-top: 20px;margin-left: 40px;padding-bottom: 10px; margin-bottom: 40px;">
<form action="bankbookupdatebalance.action">	
<div class="row-fluid">			
	<div class="row-fluid">
		<div class="span12">
			<label style="width: 40%">Bank</label>
			<select id="bank_id" name="bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
				<option value="" selected="selected">Select Bank</option>
			<s:iterator value="%{#session.USER_BANK_LIST}">
				<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
			</s:iterator>
			</select>
		</div>
	</div>	
	<div class="row-fluid">
		<div class="span12">
			<label style="width: 40%">Branch</label>
			<select id="branch_id" name="branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
				<option value="" selected="selected">Select Branch</option>
			</select>  
		</div>
	</div>	
	<div class="row-fluid">
		<div class="span12">
			<label style="width: 40%">Account</label>
				<select id="account_id" name="account_no"  onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
					<option value="" selected="selected">Select Account</option>
				</select> 
			</div>
		</div>
	</div>
	<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
		<div id="aDiv" style="height: 0px;"></div>
		</div>
						
	<div class="formSep sepH_b" style="width:90%;padding-top: 20px;margin-bottom: 0px;padding-bottom: 5px;">		
		<table width="100%">
			<tr>
				<td style="width: 70%" align="right">
					<button type="button" class="btn btn-primary" onclick="updateBankAccountBalance();" ><span class="splashy-document_letter_okay"/>
								    Update</button>	
					<button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
				</td>
			</tr>
		</table>								    
	</div>
	<!--  <div id="myProgress" style="width:90%; background-color: #dd1">
		<div id="myBar" style="width:1%; height:40px; background-color: #4CAF50">
		</div>
	</div> -->
	</form>
	<div style="width: 60%;text-align: center;float: left;padding-top:20px;margin-left: 5px;display: none;" id="stat_div">
		<table>
			<tr>
				<td style="text-align: left;padding-left: 10px;padding-bottom: 20px;background-color: #40ACDD;color: white;"  id="loading_div"></td>
			</tr>
		</table>
	</div>
</div>

<div id="detailDiv">

</div>

<!--<script type="text/javascript" src="/PGCL_WEB/resources/js/page/bankbookBalanceUpdate.js"></script>-->

<script type="text/javascript">

function updateBankAccountBalance(){


	//var formData = new FormData($('form')[0]);
	var postData={"bank_id":$("#bank_id").val(),"branch_id":$("#branch_id").val(),"account_no":$("#account_id").val()};
	
	$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Balance is updating for you.</font>");
		
	
	$.ajax({
	    url: 'bankBalanceUpdate.action',
	    type: 'POST',
	    data: postData,
	    success: function (response) {
	    	if(response.status=="OK"){
	  			//enableButton("btn_save");
	  			//move();
	  		}
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    			   
	    }			    
	  });
	  
}

function move(){
	var elem=document.getElementById("myBar");
	var width=1;
	var id= setInterval(frame,10);
	function frame(){
		if(width>=100){
			clearInterval(id);
			}else{
				width++;
				elem.style.width=width + '%';
			}
		}
	}
</script>


