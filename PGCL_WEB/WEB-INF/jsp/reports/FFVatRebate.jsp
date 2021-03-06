<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("vatRebateHome.action");
	setTitle("Income Tax, Vat-Rebate, Freedom Fighter Report");
</script>
<link href="/PGCL_WEB/resources/css/page/meterReading.css" rel="stylesheet" type="text/css" />
<style>
input[type="radio"], input[type="checkbox"]
{
margin-top: -3px !important;
}
.alert{
padding-top: 4px !important;
padding-bottom: 4px !important;
}
.ui-icon, .ui-widget-content .ui-icon {
    cursor: pointer;
}
.sFont{
font-size: 12px;
}
</style>
<div class="meter-reading" style="width: 50%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Vat-Rebate, Income Tax, FF</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="vatRebateInfo.action" style="margin-bottom: 1px;">
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="100%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="vat_rebate" id="vat_rebate" name="report_for" onclick="checkType(this.id)"/> VAT & Rebate&nbsp;&nbsp;&nbsp;
												<input type="radio" value="income_tax" id="income_tax" name="report_for" onclick="checkType(this.id)" /> Income Tax&nbsp;&nbsp;&nbsp;
												<input type="radio" value="individual" id="individual" name="report_for" onclick="checkType(this.id)"/> Individual Income Tax&nbsp;&nbsp;&nbsp;
												<input type="radio" value="freedom" id="freedom" name="report_for" onclick="checkType(this.id)"/> Freedom Fighter&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
						
						<div class="row-fluid" id="individual_div">
		    <div class="w-box-content" style="padding: 10px;" id="content_div">						
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Customer ID</label> 
						<input type="text" name="customer_id" onblur="checkInput(this.id)"  id="comm_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 12%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1" />
				  	</div>
				  
				  	<div class="span6">									    
						<label style="width: 40%">Customer Type</label>
						<input type="text" style="width: 51%;position: relative;left: 0"  id="comm_isMetered_name" disabled="disabled"/>								      
					</div>
				</div>
				<div class="row-fluid">							
					<div class="span6">									    
						<label style="width: 40%">Region/Area</label>
						<select id="comm_area_id"  style="width: 58.5%;" disabled="disabled">
							<option value="" selected="selected">Select Area</option>
							<s:iterator value="%{#application.ACTIVE_AREA}" id="categoryList">
								<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
						</s:iterator>
						</select>									      
				    </div>
					<div class="span6">
						<label style="width: 40%">Category</label>
						<select id="comm_customer_category" style="width: 56%;" disabled="disabled">
							<option value="" selected="selected">Select Category</option>
							<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
								<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
							</s:iterator>
						</select>      
					</div>  
				</div>
				
				<div class="row-fluid">							
					<div class="span12">									    
						<label style="width: 19.5%">Customer Name</label>
						<input type="text" style="width: 76%"  id="comm_full_name" disabled="disabled"/>
					</div>
				</div>
				<div class="row-fluid" id="common_fh_row">							
					<div class="span12">									    
						<label style="width: 19.5%">F/H Name</label>
						<input type="text" style="width: 76%"  id="comm_father_name" disabled="disabled"/>
					</div>
				</div>
				
				<div class="row-fluid" id="common_address_row">							
					<div class="span12">									    
						<label style="width: 19.5%">Address</label>
						<textarea rows="1" style="width: 76%" id="comm_customer_address" disabled="disabled"></textarea>
					</div>
				</div>		
													
					</div>
				</div>
						
						
						<div class="row-fluid" id="from_to_date_div">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel">From Date</label>
								<input type="text" name="from_date" id="from_date" style="width: 54%" value="<s:property value='from_date' />"/>
				  			</div>
				  			<div class="span6" id="toDateSpan">
				    			<label id="toDateLabel">To Date</label>
								<input type="text" name="to_date" id="to_date" style="width: 54%" value="<s:property value='to_date' />"/>
				  			</div>
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">
						   			  <input type="hidden" name="category_name" id="category_name" value="DOMESTIC(PVT)" />
						   			     
						   			 <button class="btn" type="submit">Generate Report</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					</form>																	
				</div>
			</div>
		</div>
	</div>
</div>

  
<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">

$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.CUSTOMER_LIST,
	    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val());getDueMonthList($('#comm_customer_id').val());}
	}));
	
function getDueMonthList(customer_id)
	{
	$.ajax({
    url: "getDuesListByString.action?customer_id="+customer_id,
  	dataType: 'text',		    
    type: 'POST',
    async: true,
    cache: false,
	success: function (response){
    	$("#dues_list").val(response.replace("Dues:", ""));   	
    }    
  });
}

Calendar.setup({
    inputField : "to_date",
    trigger    : "to_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  
  hideElement("individual_div");
  
  function checkType(type){
  if(type=="vat_rebate"){
  hideElement("individual_div");
  }
  if(type=="income_tax"){
   hideElement("individual_div");
  }
  if(type=="freedom"){
   hideElement("individual_div");
  }
  if(type=="individual"){
   showElement("individual_div");
  }
  }

</script>

