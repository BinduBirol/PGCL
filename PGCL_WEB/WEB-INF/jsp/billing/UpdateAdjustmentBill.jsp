<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("updateAdjustmentHome.action");
	setTitle("Update Adjustment");
</script>

<div style="width: 40%;height: 98%;float: left;" id="left_div">
	<div class="row-fluid">
		<div style="width: 100%;float: left;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Update Adjusted Bill</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input type="text" name="supplyOff.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 29%;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 29%;"/>
						  	</div>
						</div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveSalesAdjustment()">Adjustment Sales</button>
						</div> 
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
    	onSelect:function (){getCustomerInfo("",$('#customer_id').val());loadUnpaidBillList();},
}));



function updateSalesAdjustment(){
	$.ajax({
		    url: 'updateSalesAdjustmentInfo.action',
		    type: 'POST',
		    data: {customer_id:$("#customer_id").val()},
		    cache: false,
		    success: function (response) {
		    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
		    }
		    
		  });	
}
</script>