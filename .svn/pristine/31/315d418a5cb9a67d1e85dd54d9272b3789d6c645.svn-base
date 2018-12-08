//<<Customer Grid>>: List of Non-Metered Customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.NON_METERED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
    colNames: ['Customer Id', 'Customer Name','Father/Husband Name','Category', 'Area','Mobile','Status','Created On'],
    colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()+10,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Non-Metered Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id,setBurnerInfo);		
		burnerQntChangeForm(clearField);
		burnerQntChangeForm(disableField);
		setActiveTabToBurnerQntHistory(id);
		enableButton("btn_add");
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Burner Qnt. Change History for the selected Customer>>
$("#burnerQnt_change_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BURNER_QNT_CHANGE_SERVICE+'&method='+jsEnum.BURNER_QNT_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
	colNames: ['Customer Id','Customer Name','Old(Double)','New Permanent Disconnection','New Temporary Disconnection','Increase','Reconnection/Temp','Reconnection/Permanent','New(Double)','Effective Date','Remarks'],
			colModel : [ {
				name : 'customer_id',
				index : 'customer_id',
				width : 70,
				align : 'center',
				sorttype : 'string',
				search : false
			}, {
				name : 'customer_name',
				index : 'customer_name',
				width : 200,
				sorttype : 'string',
				search : true
			}, {
				name : 'old_double_burner_qnt',
				index : 'old_double_burner_qnt',
				sorttype : "string",
				width : 50,
				align : 'center',
				search : true
			}, {
				name : 'new_permanent_disconnected_burner_qnt',
				index : 'new_permanent_disconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_temporary_disconnected_burner_qnt',
				index : 'new_temporary_disconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_incrased_burner_qnt',
				index : 'new_incrased_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'

			}, {
				name : 'new_reconnected_burner_qnt',
				index : 'new_reconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			},{
				name : 'new_reconnected_burner_qnt_permanent',
				index : 'new_reconnected_burner_qnt_permanent',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_double_burner_qnt',
				index : 'new_double_burner_qnt',
				sorttype : "string",
				width : 50,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'effective_date',
				index : 'effective_date',
				sorttype : "date",
				formatoptions: { srcformat: "d/m/Y", newformat: "d/m/Y" }
			}, {
				name : 'remarks',
				index : 'remarks',
				sorttype : "string",
				search : true,
				width : 50
			} ],
	// datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#burnerQnt_change_history_this_grid_pager',
   	sortname: 'effective_date,pid',
    sortorder: "asc",
	caption: "Customer's Burner Quantity Change History",
	onSelectRow: function(id){ 
		var rowData = $("#burnerQnt_change_history_this_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchBurnerQntChangeInfo(id);
		
   }
}));
jQuery("#burnerQnt_change_history_this_grid").jqGrid('navGrid','#burnerQnt_change_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToBurnerQntHistory("");}}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
				
			   var ruleArray=[["BURNER_QNT_CHANGE.CUSTOMER_ID"],["eq"],[$("#comm_customer_id").val()]];
			   var oldRules=["customer_id"];
			   var newRules=["BURNER_QNT_CHANGE.customer_id"];			   
			   modifyGridPostData("meterRent_change_history_this_grid",ruleArray,oldRules,newRules);	
		   }
		
		});
gridColumnHeaderAlignment("left","burnerQnt_change_history_this_grid",["remarks"]);

//<<Burner Qnt. Change History for all Customers>>
$("#burnerQnt_change_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BURNER_QNT_CHANGE_SERVICE+'&method='+jsEnum.BURNER_QNT_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
	colNames: ['Customer Id','Customer Name','Old(Double)','New Permanent Disconnection','New Temporary Disconnection','Increase','Reconnection/Temp','Reconnection/Permanent','New(Double)','Effective Date','Remarks'],
	colModel : [ {
		name : 'customer_id',
		index : 'customer_id',
		width : 70,
		align : 'center',
		sorttype : 'string',
		search : false
	}, {
		name : 'customer_name',
		index : 'customer_name',
		width : 200,
		sorttype : 'string',
		search : true
	}, {
		name : 'old_double_burner_qnt',
		index : 'old_double_burner_qnt',
		sorttype : "string",
		width : 50,
		align : 'center',
		search : true
	}, {
		name : 'new_permanent_disconnected_burner_qnt',
		index : 'new_permanent_disconnected_burner_qnt',
		sorttype : "string",
		width : 70,
		align : 'center',
		search : true,
		classes : 'new_qnt'
	}, {
		name : 'new_temporary_disconnected_burner_qnt',
		index : 'new_temporary_disconnected_burner_qnt',
		sorttype : "string",
		width : 70,
		align : 'center',
		search : true,
		classes : 'new_qnt'
	}, {
		name : 'new_incrased_burner_qnt',
		index : 'new_incrased_burner_qnt',
		sorttype : "string",
		width : 70,
		align : 'center',
		search : true,
		classes : 'new_qnt'

	}, {
		name : 'new_reconnected_burner_qnt',
		index : 'new_reconnected_burner_qnt',
		sorttype : "string",
		width : 70,
		align : 'center',
		search : true,
		classes : 'new_qnt'
	}, {
		name : 'new_reconnected_burner_qnt_permanent',
		index : 'new_reconnected_burner_qnt_permanent',
		sorttype : "string",
		width : 70,
		align : 'center',
		search : true,
		classes : 'new_qnt'
	},
	{
		name : 'new_double_burner_qnt',
		index : 'new_double_burner_qnt',
		sorttype : "string",
		width : 50,
		align : 'center',
		search : true,
		classes : 'new_qnt'
	}, {
		name : 'effective_date',
		index : 'effective_date',
		sorttype : "string",
		search : true
	}, {
		name : 'remarks',
		index : 'remarks',
		sorttype : "string",
		search : true,
		width : 50
	} ],
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#burnerQnt_change_history_all_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "All Customers Burner Quantity Change History",
	onSelectRow: function(id){ 		
		var rowData = $("#burnerQnt_change_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchBurnerQntChangeInfo(id);
   }
}));
jQuery("#burnerQnt_change_history_all_grid").jqGrid('navGrid','#burnerQnt_change_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","burnerQnt_change_history_all_grid",["customer_name","remarks"]);



//[START : BASIC INITIALIZATION & OVERRIDE]
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		
			serviceUrl: sBox.NON_METERED_CUSTOMER_LIST,
	    	onSelect:function (){
		
				getCustomerInfo("comm",$("#comm_customer_id").val(),setBurnerInfo);		
				burnerQntChangeForm(clearField);
				burnerQntChangeForm(disableField);
				setActiveTabToBurnerQntHistory($("#comm_customer_id").val());
				enableButton("btn_add");		
			}
	}));
	
	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Burner Quantity Change Information? "+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Burner Quantity Change Information Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteBurnerQntChangeInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});
	

	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "effective_date",
	    trigger    : "effective_date"}));	
	

function setActiveTabToBurnerQntHistory(customer_id){
	//alert("setActiveTabToBurnerQntHistory");
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") 
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadBurnerQntChangeHistory(customerId);
    
}

function reloadBurnerQntChangeHistory(customer_id){
    var ruleArray=[["BQC.CUSTOMER_ID"],["eq"],[customer_id]];
    var postdata=getPostFilter("burnerQnt_change_history_this_grid",ruleArray);
    $("#burnerQnt_change_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("burnerQnt_change_history_this_grid");
}

function fetchBurnerQntChangeInfo(change_id){
	
	burnerQntChangeForm(disableField);	
	
	$.ajax({
		    url: 'getBurnerQntChangeInfo.action',
		    type: 'POST',
		    data: {pId:change_id},
		    cache: false,
		    success: function (response) {
		    	setBurnerQntChagneInfo(response);		    	
		    	
		    	disableButton("btn_add","btn_save");
		    	enableButton("btn_edit","btn_delete");
		    
		    }
		    
		  });	
}


function setBurnerInfo(data){
	$("#old_single_burner_qnt").val(data.connectionInfo.single_burner_qnt);
	$("#old_double_burner_qnt").val(data.connectionInfo.double_burner_qnt);
	$("#double_burner_qnt_hidden").val(data.connectionInfo.double_burner_qnt);
	$("#old_double_burner_qnt_billcal").val(data.connectionInfo.double_burner_qnt_billcal);	
	$("#double_burner_qnt_billcal_hidden").val(data.connectionInfo.double_burner_qnt_billcal);		
	var temp_disconn_half_qnt=(parseFloat(data.connectionInfo.double_burner_qnt_billcal)-parseFloat(data.connectionInfo.double_burner_qnt))*2;
	$("#old_pdisconnected_burner_qnt").val(data.connectionInfo.pemanently_disconnected_burner_qnt);
	var temp_disconn_qnt=parseFloat(data.connectionInfo.temporary_disconnected_burner_qnt)-temp_disconn_half_qnt;
	$("#old_tdisconnected_burner_qnt").val(temp_disconn_qnt);
	$("#old_tdisconnected_half_burner_qnt").val(temp_disconn_half_qnt);
	$("#customer_id").val(data.customer_id);
}

function setBurnerQntChagneInfo(data){
	$("#pid").val(data.pid);
	$("#customer_id").val(data.customer_id);
	$("#old_double_burner_qnt").val(data.old_double_burner_qnt);
	$("#double_burner_qnt_hidden").val(data.old_double_burner_qnt);
	$("#old_double_burner_qnt_billcal").val(data.old_double_burner_qnt_billcal);
	$("#double_burner_qnt_billcal_hidden").val(data.old_double_burner_qnt_billcal);	
	$("#new_permanent_disconnected_burner_qnt").val(data.new_permanent_disconnected_burner_qnt);
	$("#new_permanent_disconnected_cause").val(data.new_permanent_disconnected_cause);
	$("#new_temporary_disconnected_burner_qnt").val(data.new_temporary_disconnected_burner_qnt);
	$("#new_incrased_burner_qnt").val(data.new_incrased_burner_qnt);
	$("#new_reconnected_burner_qnt").val(data.new_reconnected_burner_qnt);
	$("#old_pdisconnected_burner_qnt").val(parseFloat(data.old_pdisconnected_burner_qnt)-parseFloat(data.new_permanent_disconnected_burner_qnt));
	$("#old_tdisconnected_burner_qnt").val(parseFloat(data.old_tdisconnected_burner_qnt)-parseFloat(data.new_temporary_disconnected_burner_qnt)+parseFloat(data.new_reconnected_burner_qnt));
	$("#new_double_burner_qnt").val(data.new_double_burner_qnt);
	$("#new_double_qnt_billcal").val(data.new_double_qnt_billcal);
	$("#effective_date").val(data.effective_date);
	$("#remarks").val(data.remarks);
}

function setDisconnType(id){
	//alert(id);
	var dissconType=$("#disconn_type").val();
	//alert("disconnectionCause"+dissconType);
	var burner_qnt_dissconn=$("#burner_qnt_disconn").val()==""?parseFloat("0"):parseFloat($("#burner_qnt_disconn").val());
	//alert("disconnected burner qnt"+burner_qnt_dissconn);
	if(dissconType=="01")
			{
			$("#new_permanent_disconnected_burner_qnt").val(burner_qnt_dissconn);
			//alert("Permanent disconnection"+$("#new_permanent_disconnected_burner_qnt").val());
			}else
				{
				$("#new_temporary_disconnected_burner_qnt").val(burner_qnt_dissconn);
				//alert("Permanent disconnection"+$("#new_temporary_disconnected_burner_qnt").val());
				}
	
}

function setReconnFrom(id){
	var reconnFrom=$("#reconn_from").val();
	var burner_qnt_reconn=$("#burner_qnt_reconn").val()==""?parseFloat("0"):parseFloat($("#burner_qnt_reconn").val());
	//alert(reconnFrom);
	if(reconnFrom=="01")
	{
	$("#new_reconnected_burner_qnt_permanent").val(burner_qnt_reconn);
	//alert("from permanent reconnect"+$("#new_reconnected_burner_qnt_permanent").val());
	}else
		{
		$("#new_reconnected_burner_qnt").val(burner_qnt_reconn);
		//alert("from Temporary reconnect"+$("#new_reconnected_burner_qnt").val());
		}
}


$("#disconnectionDiv").hide();
$("#incraseDiv").hide();
$("#reconnectionDiv").hide();
$('#isIncraseSelected').click(function() {
    $("#incraseDiv").toggle(this.checked);
    $("#new_incrased_burner_qnt").val("");
    calculateDoubleBurner();
});
$('#isDisconnSelected').click(function() {
    $("#disconnectionDiv").toggle(this.checked);
    $("#disconn_type").val("");
    $("#new_permanent_disconnected_cause").val("");
    $("#burner_qnt_disconn").val("");
    $("#new_permanent_disconnected_burner_qnt").val("");
    $("#new_temporary_disconnected_burner_qnt").val("");
    setDisconnType();
    calculateDoubleBurner();
});
$('#isReconnSelected').click(function() {
    $("#reconnectionDiv").toggle(this.checked);
    $("#reconn_from").val("");
    $("#reconnection_cause").val("");
    $("#burner_qnt_reconn").val("");
    $("#new_reconnected_burner_qnt").val("");
    $("#new_reconnected_burner_qnt_permanent").val("");
    calculateDoubleBurner();
});


function setManagementType(){
	var managementType=$("#management_type").val();
	if(managementType=="01")
		{
		$("#reconnectionDiv").hide();
		$("#disconnectionDiv").hide();
		$("#incraseDiv").show();
		}else if(managementType=="02")
			{
			$("#incraseDiv").hide();
			$("#reconnectionDiv").hide();
			$("#disconnectionDiv").show();	
			}else if(managementType=="03")
				{
				$("#disconnectionDiv").hide();
				$("#incraseDiv").hide();
				$("#reconnectionDiv").show();
				}
}

$('#isTempToPerDiss').change(function(){
	calculateDoubleBurner();
});
function calculateDoubleBurner(id){
	var double_burner_qnt=parseFloat($("#double_burner_qnt_hidden").val());
	//alert("hiiden double qnt"+double_burner_qnt)
	var double_burner_qnt_billcal=parseFloat($("#double_burner_qnt_billcal_hidden").val());
	///alert("hiiden double qnt bill cal"+double_burner_qnt_billcal)
	var permanently_disconnected_burner=$("#new_permanent_disconnected_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#new_permanent_disconnected_burner_qnt").val());
	var temporary_disconnected_burner=$("#new_temporary_disconnected_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#new_temporary_disconnected_burner_qnt").val());
	var increased_burner=$("#new_incrased_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#new_incrased_burner_qnt").val());
	var reconnected_burner=$("#new_reconnected_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#new_reconnected_burner_qnt").val());
	var reconnected_burner_4m_permanentDis=$("#new_reconnected_burner_qnt_permanent").val()==""?parseFloat("0"):parseFloat($("#new_reconnected_burner_qnt_permanent").val());
	var disconnectionType=$("#disconn_type").val();
	var disconnectionCauseTemp=$("#new_permanent_disconnected_cause").val();  // this is actually disconnection cause
	var temp_disconnected_burner=$("#old_tdisconnected_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#old_tdisconnected_burner_qnt").val());
	var reconnCause=$("#reconnection_cause").val();// this is actualyy reconnection cause both for permanent and temporary
	if((reconnected_burner>$("#old_tdisconnected_burner_qnt").val()&&reconnCause!="2")||(reconnected_burner>$("#old_tdisconnected_half_burner_qnt").val()&&reconnCause=="2")||reconnected_burner_4m_permanentDis>$("#old_pdisconnected_burner_qnt").val())
	{
		
			cbColor($("#burner_qnt_reconn"),"e");
			return;
	
	}
	
	var final_dobuble_burner_qnt=double_burner_qnt+increased_burner+reconnected_burner+reconnected_burner_4m_permanentDis-permanently_disconnected_burner-temporary_disconnected_burner;
	var final_dobuble_burner_qnt_billcal=double_burner_qnt_billcal+increased_burner+(reconnCause=="2"?(reconnected_burner*.5):reconnected_burner)+reconnected_burner_4m_permanentDis-permanently_disconnected_burner-(disconnectionType=="02"&&disconnectionCauseTemp=="2"?(temporary_disconnected_burner*.5):temporary_disconnected_burner);
	//alert("double burner qnt"+final_dobuble_burner_qnt);
	//alert("double burner qnt biiCAL"+final_dobuble_burner_qnt_billcal);
	if ($("#isTempToPerDiss").is(':checked')) {
		final_dobuble_burner_qnt=final_dobuble_burner_qnt+permanently_disconnected_burner+temp_disconnected_burner;
		final_dobuble_burner_qnt_billcal=final_dobuble_burner_qnt_billcal+permanently_disconnected_burner-(permanently_disconnected_burner* .5)+temp_disconnected_burner;
		$("#disconn_type").val("03");
	}
	
	$("#new_double_qnt_billcal").val(final_dobuble_burner_qnt_billcal);
	$("#new_double_burner_qnt").val(final_dobuble_burner_qnt);
		
	if ($("#isDisconnSelected").is(':checked')) {
		if ($("#isTempToPerDiss").is(':checked')) {
			$("#remarks").val("From Temporary to Permanent Disconnecte");	
		}else
		{
			$("#remarks").val($("#new_permanent_disconnected_cause :selected").text());	
		}
	
	}

}

function validateAndSaveBurnerQntChange(){
	
	var validate=false;
	
	validate=validateBurnerQntChange();
	if(validate==true){	    
		saveBurnerQntChangeInfo();							
	}	
}


function validateBurnerQntChange(){
	var isValid=false;	
	var field;
	if ($("#isDisconnSelected").is(':checked')) {
		field = ["old_double_burner_qnt","new_double_burner_qnt","effective_date","new_permanent_disconnected_cause","disconn_type"];	
		if ($("#isTempToPerDiss").is(':checked')){
			field = ["old_double_burner_qnt","new_double_burner_qnt","effective_date","new_permanent_disconnected_cause"];
		}
	}else
    {
		field = ["old_double_burner_qnt","new_double_burner_qnt","effective_date"];
		
    }	
	isValid=validateField.apply(this,field);
    if(isValid==true){    	
    	var v2=isPositiveInteger($("#new_double_burner_qnt").val());  
       //var v3=isPositiveInteger($("#new_double_qnt_billcal").val()); 
    	var reconnCause=$("#reconnection_cause").val();
    	var reconnected_burner=$("#new_reconnected_burner_qnt").val()==""?parseFloat("0"):parseFloat($("#new_reconnected_burner_qnt").val());
    	var reconnected_burner_4m_permanentDis=$("#new_reconnected_burner_qnt_permanent").val()==""?parseFloat("0"):parseFloat($("#new_reconnected_burner_qnt_permanent").val());
    	var v4;
    	var v5=reconnected_burner_4m_permanentDis>$("#old_pdisconnected_burner_qnt").val()?false:true;
    	var v6;
    	
    	if((reconnected_burner>$("#old_tdisconnected_burner_qnt").val()&&reconnCause!="2"))
    	{
    		v4=false;
    	}
    	if((reconnected_burner>$("#old_tdisconnected_half_burner_qnt").val()&&reconnCause=="2"))
    	{
    		
    			v6=false;
    	
    	}
    	if(v2==false){
    		isValid=false;
    		alert("Double buner qunatity must be zero or positive number.");
    		return isValid;
    	}
    	if(v4==false||v6==false){
    		isValid=false;
    		alert("No Such Temporary Burner is Available");
    		return isValid;
    	}
    	if(v5==false){
    		isValid=false;
    		alert("No Such Permanent Burner is Available");
    		return isValid;	
    	}
    }
    return isValid;
}


function saveBurnerQntChangeInfo(){
	
	burnerQntChangeForm(enableField);
	burnerQntChangeForm(readOnlyField);
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveBurnerQntChangeInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  burnerQntChangeForm(clearField);
		    	  burnerQntChangeForm(removeReadOnlyField);
		    	  burnerQntChangeForm(disableField);
		    	  disableButton("btn_save","btn_add","btn_delete");
		    	  reloadBurnerQntChangeHistory($("#comm_customer_id").val());
		      }
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
		   
		    }		    
		  });
	
}

function deleteBurnerQntChangeInfo(){
	$.ajax({
	    url: 'deleteBurnerQntChagneInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	//Clean forms data
	    	burnerQntChangeForm(clearField);	    
	    	$dialog.dialog("close");
	    	reloadGrid("burnerQnt_change_history_this_grid");
	    	reloadGrid("burnerQnt_change_history_all_grid");
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}

function cancelButtonPressed(){
	clearRelatedData();
	burnerQntChangeForm(disableField);
	disableButton("btn_add","btn_edit","btn_save","btn_delete");
	resetSelection("customer_grid","burnerQnt_change_history_this_grid","burnerQnt_change_history_all_grid");
}
function addButtonPressed(){
	burnerQntChangeForm(enableField);
	
	disableField("old_single_burner_qnt","old_double_burner_qnt","old_tdisconnected_burner_qnt","old_pdisconnected_burner_qnt","old_tdisconnected_burner_qnt","new_double_burner_qnt");
	enableButton("btn_save");
	disableButton("btn_add");	
}
function editButtonPressed(){	
	burnerQntChangeForm(enableField);
	disableField("old_single_burner_qnt","old_double_burner_qnt","old_tdisconnected_burner_qnt","old_pdisconnected_burner_qnt","old_tdisconnected_burner_qnt","new_double_burner_qnt");
	enableButton("btn_save");
	disableButton("btn_edit");
}
function burnerQntChangeForm(plainFieldMethod){
	var fields=["old_single_burner_qnt","old_double_burner_qnt","new_single_burner_qnt","new_double_burner_qnt","effective_date","remarks","pid","customer_id","old_pdisconnected_burner_qnt","old_tdisconnected_burner_qnt","new_permanent_disconnected_burner_qnt","new_permanent_disconnected_cause","new_temporary_disconnected_burner_qnt","new_incrased_burner_qnt","new_reconnected_burner_qnt","old_double_burner_qnt_billcal","new_double_qnt_billcal","new_reconnected_burner_qnt_permanent","burner_qnt_disconn","burner_qnt_reconn","disconn_type","reconn_from","reconnection_cause","isIncraseSelected","isDisconnSelected","isReconnSelected"];	
	plainFieldMethod.apply(this,fields);	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	burnerQntChangeForm(clearField);
}

