var p_customer_id="";
var p_month="";
var p_year="";
var p_area_id="";
var p_customer_category="";

$("#bill_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.pgcl.models.BillingService&method=getMeteredBilledCustomerList'+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
   colNames: ['Customer Id', 'Customer Name','Actual Consumption', 'Meter Rent','Total Bill Amount','Status','Approve','Download'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:60,
	                align:'center',
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'full_name',
	                index: 'full_name',
	                width:150,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'actual_consumption',
	                index: 'actual_consumption',
	                width:80,
	                sorttype: "string",
	                align:'right',
	                search: true,
            	},
            	{
	                name: 'meter_rent',
	                index: 'meter_rent',
	                width:80,
	                align:'right',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'total_bill_amount',
	                index: 'total_bill_amount',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},
            	{
	                name: 'status',
	                index: 'status',
	                sorttype: "string",
	                align:'center',
	                width:80,
	                search: true,
            	},
            	{ 
            		name: 'approve',
            		
            		width: 25, 
            		align:'center',
            		formatter:function(cellvalue, options, rowObject){
            		      if(rowObject.status=='Waiting for Approval')
            		    	  return "<span class='ui-icon ui-icon-circle-check' style='margin-left:12px;'></span>"
            		      else
            		    	  return "<span class='ui-icon ui-icon-check' style='margin-left:12px;'></span>";
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            if(rowObject.status=='Waiting for Approval')
                            	return ' onClick="approveBillByBillId('+rowObject.bill_id+')"';
                           	else
                           		return '';
                    }
                },            	
            	{ 
            		name: 'Download', 
            		width: 17, 
            		align:'center',
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-circle-arrow-s' style='margin-left:3px;'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            return ' onClick="window.location=\'downloadMeteredBillp.action?download_type=S&bill_id='+rowObject.bill_id+'\'"';
                    }
                }
        ],
	datatype: 'local',
	height: $("#bill_grid_div").height()-80,
	width: $("#bill_grid_div").width()-2,
   	pager: '#bill_grid_pager',
   	sortname: 'bill_id',
    sortorder: "desc",
	caption: "Metered Customers List(Billed)"
}));

jQuery("#bill_grid").jqGrid('navGrid','#bill_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {reloadBillGrid();}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","bill_grid",["full_name"]);


$('#bill_grid').jqGrid('navGrid','#bill_grid_pager')
    .navButtonAdd('#bill_grid_pager',{
        caption:"<b><font color='blue'>Download Approved Bills</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-print", 
        id: "icon_download_approve_bills",
        onClickButton: function(){
				downloadBill('A');
        }
}).navButtonAdd('#bill_grid_pager',{
        caption:"<b><font color='red'>Download Non-Approved Bills</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-print", 
        id: "icon_download_nonapprove_bills",
        onClickButton: function(){
				downloadBill('NA');
        }
});

function reloadBillGrid(){
	
    var ruleArray=[["bill_month","bill_year"],["eq","eq"],[$("#billing_month").val(),$("#billing_year").val()]];
    var $grid = $('#bill_grid');
    var caption_extra="";
    
	if($("#billing_month").val()=="" || $("#billing_year").val()==""){
		clearGridData("bill_grid");
		$grid.jqGrid('setCaption','Please Select Month, Year');
		return;
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="individual_customer" && $("#customer_id").val()!=""){
		
		ruleArray[0].push("customer_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#customer_id").val());
		caption_extra=". Customer Id - "+$("#customer_id").val();
		p_customer_id=$("#customer_id").val();
	}
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="area_wise"){
		if($("#area_id").val()=="") return;
		
		ruleArray[0].push("area_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		caption_extra=". "+$("#area_id option:selected").text();
		
		p_area_id=$("#area_id").val();
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="category_wise")		{								
	   if($("#area_id").val()=="" || $("#customer_category").val()=="") return;
	   
	    ruleArray[0].push("area_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		
	    ruleArray[0].push("customer_category");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#customer_category").val());
		caption_extra=". "+$("#area_id option:selected").text()+" ["+$("#customer_category option:selected").text()+"] ";
		
		p_area_id=$("#area_id").val();
		p_customer_category=$("#customer_category").val();
		
	}
	
	p_month=$("#billing_month").val();
	p_year=$("#billing_year").val();
	
	var postdata=getPostFilter("bill_grid",ruleArray);
   	$("#bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    		
	reloadGrid("bill_grid");
	
	$grid.jqGrid('setCaption', 'Metered Customers List(Billed) - '+$("#billing_month option:selected").text()+', '+$("#billing_year").val()+caption_extra);
}

function downloadBill(downloadType){
	window.location='downloadMeteredBillp.action?download_type='+downloadType+'&customer_id='+p_customer_id+'&bill_month='+p_month+'&bill_year='+p_year+'&area_id='+p_area_id+'&customer_category='+p_customer_category;
		
}



function valiateFields(){
	 var isValid=true;
	 var bill_for=getRadioCheckedValue("bill_parameter\\.bill_for"); 
	 
	 if(bill_for=="area_wise")
	 	isValid=validateField("area_id");
	 else if(bill_for=="category_wise")
	 	isValid=validateField("area_id","customer_category");
	 else if(bill_for=="individual_customer")
	 	isValid=validateField("customer_id");

	 if(isValid==true)
	 	isValid=validateField("billing_month","billing_year","issue_date");
	 
	 return isValid;
}
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.METERED_CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());
$("#issue_date").val(getCurrentDate());
reloadBillGrid();

function checkType(type){
	if(type=="area_wise")
	{
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	}
	else if(type=="by_category"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	}
	else if(type=="individual"){
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}
}		
function cleanAllFields(){}	
