<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	navCache("billCollectionHome.action");
	setTitle("Customers Bill Collection Information");
</script>
<script type="text/javascript"
	src="/PGCL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/PGCL_WEB/resources/thirdParty/popupModal/css/ns-window.css"
	rel="stylesheet" />
<link href="/PGCL_WEB/resources/css/page/billCollection.css"
	rel="stylesheet" type="text/css" />
<style>

input[type="radio"],input[type="checkbox"] {
	margin-top: -3px !important;
}

.alert {
	padding-top: 4px !important;
	padding-bottom: 4px !important;
}

.ui-icon,.ui-widget-content .ui-icon {
	cursor: pointer;
}

#ui-datepicker-div {
	z-index: 99999999999 !important;
}
</style>



<div class="">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
					<h4 id="rightSpan_caption">Bill Collection Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px; " id="content_div">
					<form id="billCollectionForm" name="billCollectionForm">
						<div class="row-fluid">
							
							<div class="span6">

								<div class="row-fluid" >
								<br/>
									<div class="span12" style="">
										 <input
											type="text" name="bankCollection.customer_id" id="customer_id"
											style="font-weight:bold; color:  #000866;"  placeholder="Customer ID"/> 						
											
										<span style="background-color:white;cursor: pointer;" onclick="reloadBillGrid();" class="splashy-refresh"></span>
											
											
											<input
											type="hidden" name="" id="customer_id_x" disabled="disabled"
											style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 16%;margin-top: -5px;" />
										<input type="hidden" name="collection_id"
											id="collection_id" />
									</div>
								</div>


								
								
								<div class="row-fluid" id="non_metered_payable_amount">
									<div class="span12 " >
									
									</div>
								</div>

							</div>
							
							<div id="grid_width" class="span6">
								<!-- Start -->
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Bank</label> <select id="bank_id"
											name="bankCollection.bank_id" style="width: 54.5%;"
											>
											
											<s:iterator value="%{#session.BANK_USER}">
												<option selected="selected"  value="<s:property value="bank_id" />">
													<s:property value="bank_name" />
												</option>
											</s:iterator>
											
										</select> 
										
										
										<input type="hidden"
											name="bankCollection.ismeter"
											value="" id="isMetered_str" />
									</div>
								</div>
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Branch</label> 
										<select id="branch_id"
											name="bankCollection.branch_id" style="width: 54.5%;"
											>
										<s:iterator value="%{#session.BANK_USER}">
												<option selected="selected"  value="<s:property value="branch_id" />">
													<s:property value="branch_name" />
												</option>
											</s:iterator>						
										</select>
									</div>
								</div>
								




								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Collection Date</label> <input
											type="text" name="bankCollection.collection_date"
											id="collection_date" style="width: 51%" />
									</div>
								</div>

							</div>
						</div>

						<br/>

						
						<div class=" row-fluid">
							<div class="span6">
								<div id="bill_grid_div">
									<table id="bill_grid"></table>
									<div id="bill_grid_pager"></div>
								</div>
								<br/>
								<div class="row-fluid" id="tax_amount_div">						    
									<div class="span12">
										<label id="tax_label" style="width: 50%">Tax Amount/ Chalan No/ Chalan Date</label>
										<input type="text" name="bankCollection.tax_amount" id="tax_amount" tabindex="5" style="text-align: right;width: 10%;font-weight: bold;color: green;"   value="0" onkeyup="calculateCollection()"/>
										<input type="text" name="bankCollection.tax_no" id="tax_no" tabindex="2" placeholder="Chalan No" style="text-align: right;width: 15%;"/>
										<input type="text" name="bankCollection.tax_date" id="tax_date" placeholder="Chalan Date" tabindex="2" style="text-align: right;width: 15%;"/>
									</div>															
								</div>
								
							</div>

							<div class='span6' id='grid_height'>
							
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Customer ID </label> <input
											type="text" name="bankCollection.customer_id" id="customer_id_r"
											style="font-weight: bold;color: #3b5894; z-index: 2;;width: 40%;margin-top: -4px;"
											value="<s:property value='customer_id' />"
											disabled="disabled" /> <input type="text" name=""
											id="customer_id_x" disabled="disabled"
											style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 16%;margin-top: -5px;" />
										<input type="hidden" name="collection_id"
											id="collection_id" />
									</div>
								</div>
								<!-- 
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Region/Area</label>
										<select id="area_id"  style="width: 54.5%;" disabled="disabled">
											<option value="" selected="selected">Select Area</option>
											<s:iterator value="%{#session.USER_AREA}" >
												<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
										</s:iterator>
										</select>	 
									</div>
								</div>								
								 -->
								 
								 <div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Customer Name</label> <input
											type="text" style="width: 51%"
											name="bill_parameter.issue_date" id="customer_name"
											disabled="disabled" />
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Area Name</label> <input type="text"
											style="width: 51%" name="" id="area_name" disabled="disabled" />
									</div>
								</div>



								
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Address</label>
										<textarea
											style="width: 51%;height: 35px;overflow-y: visible; "
											id="address" readonly="readonly"></textarea>
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Category Name</label> <input
											type="text" style="width: 51%" name="" id="category_name"
											disabled="disabled" />
											 <input type="hidden"
											id="category_id" name="bankCollection.category_id" />
									</div>
								</div>

								<div class="row-fluid" id="burnerDiv" style="display: none;">
									<div class="span12">
										<label style="width: 40%">Double Burner(Qnt)</label>
										<div id="divBurnerQnt" style=""></div>
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Customer Type</label> <input
											type="text"
											style="width: 51%;color: green;font-weight: bold;"
											name="bill_parameter.issue_date" id="customerType"
											disabled="disabled" />
									</div>
								</div>

								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Phone</label> <input
											readonly="readonly" type="text"
											style="width: 51%;height: 18px;overflow-y: visible; "
											id="phone" name="phone" /> <input type="hidden"
											id="phone_old" />
									</div>
								</div>
								
								
								
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Collectable amount</label> <input
											readonly="readonly" type="text"
											style="width: 51%;height: 18px;overflow-y: visible; font-weight: bold; color:red "
											id="net_collected" name="phone" /> 
											
											<input type="hidden" id="bill_ids" name="bankCollection.bill_ids"/>
											<input type="hidden" id="billed_amounts" name="bankCollection.billed_amounts"/>
											<input type="hidden" id="surcharge_amounts" name="bankCollection.surcharge_amounts"/>
											<input type="hidden" id="months" name="bankCollection.months"/>
											<input type="hidden" id="years" name="bankCollection.years"/>
											<input type="hidden" id="payable_amounts" name="bankCollection.payable_amounts"/>															
											
											
									</div>
								</div>
								
								

							</div>

						</div>

						<br />

						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>

						
						<table width="100%">
							<tr>
							<td width="60%" align="left"><p id="resp_msg"><p></td>								
								<td width="60%" align="right">
									<button class='btn btn-beoro-3' type='button' id='btn_next'
										onclick="showShortCuts()">
										<span class='splashy-sprocket_dark'></span>
									</button>
									<button class="btn btn-beoro-3" type="button" id="btn_save"
										onclick="saveCollection()" id="btn_save">
										<span class="splashy-document_letter_okay"></span>Save
										Collection
									</button> <!-- 
							    	<button class="btn btn-beoro-3" type="button" id="btn_parameter"><span class="splashy-help"></span> Help</button>
							    	 --> <%--  <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled">
										<span class="splashy-application_windows_edit"></span> Edit
									 </button> --%>
									
									<button class="btn btn-beoro-3" type="button" id="btn_close"
										onclick="resetFields()">
										<i class="splashy-folder_classic_remove"></i>Reset</button>
								</td>
							</tr>
						</table>
				</div>
				</form>
			</div>
		</div>
	</div>
	
							<div class="">
								<div id="daily_coll_grid_div">
									<table id="daily_coll_grid"></table>
									<div id="daily_coll_grid_pager"></div>
								</div>
							</div>
</div>
</div>


							

<p style="clear: both;margin-top: 5px;clear: both;"></p>


<!--<script type="text/javascript"
	src="/PGCL_WEB/resources/js/page/collection.js"></script>
	-->
<script type="text/javascript"
	src="/PGCL_WEB/resources/js/page/dueBillGrid.js"></script>
<script type="text/javascript">
	var baseBanks = $("#bank_id").html();

	
</script>