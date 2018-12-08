<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("meterInformationHome.action");
	setTitle("Customer's Meter Information");
</script>

<div id="customer_meter_div" style="height: 60%;width: 99%;">
	<div class="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 57%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		<div id="available_meter_grid_div" style="height: 40%;">
			<jsp:include page="../common/MeterGrid.jsp" />
		</div>
	</div>

	<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
		<div class="row-fluid">
			<jsp:include page="MeterInfo.jsp" />
		</div>
	</div>
</div>

<div id="customer_grid_div" style="height: 38%;width: 99%;">
 	<table id="customer_grid"></table>
	<div id="customer_grid_pager" ></div>
</div>

<script type="text/javascript" src="/PGCL_WEB/resources/js/page/meterListGrid.js"></script> 
<script type="text/javascript" src="/PGCL_WEB/resources/js/page/meterInformation.js"></script>
<script>
clearField.apply(this,fields_with_evc);
getCustomerInfo("comm",$("#comm_customer_id").val());
loadMeters($("#comm_customer_id").val(),preOperationWrapper);
enableButton("btn_add");
</script>


