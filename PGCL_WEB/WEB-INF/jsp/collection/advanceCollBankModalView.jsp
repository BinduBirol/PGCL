<%@ taglib prefix="s" uri="/struts-tags"%>

	
<script type="text/javascript">
var multiColl_branch_sbox = { sourceElm:"multiColl_bank_id",targetElm :"multiColl_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var multiColl_account_sbox = { sourceElm:"multiColl_branch_id",targetElm :"multiColl_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
</script>
<style type="text/css">
.ui-datepicker-calendar{
display:none;
}
</style>
<form name="multiCollForm" id="multiCollForm">	
<input type="hidden" id="multiColl_customer_id" name="multiColl.customer_id"  />
<div class="content-bg">
<div style="display:none"><jsp:include page="MultiMonthHeader.jsp" /></div>


<div id="customer_grid_div" style="margin-top: 10px;height: 510px;"> 
			<div id="tabbed-nav">
	            <ul>
	                <li><a>Pending Bills</a></li>
	                <li><a>Advanced Payment</a></li>
	            </ul>
            <div>
                <div>
					<!-- Pending billing list goes here  -->
					<jsp:include page="MultiMonthPendingBills.jsp" />
                </div>
                <div>
                    <jsp:include page="MultiMonthAdvancedBills.jsp" />
                </div>                
            </div>
        </div>       
<jsp:include page="MultiMonthTotalAmount.jsp" />
</div>

<jsp:include page="MultiMonthFooter.jsp" />
</div>
<input type="hidden" id="pending_bills_str" name="multiColl.pending_bills_str" value="" />
<input type="hidden" id="advanced_bills_str" name="multiColl.advanced_bills_str" value="" />
</form>

<script type="text/javascript" src="/PGCL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/PGCL_WEB/resources/js/page/dueBillGrid.js"></script>


<script type="text/javascript">
$("#multiColl_bank_id").val($("#bank_id").val());
fetchSelectBox(multiColl_branch_sbox);
$("#multiColl_branch_id").val($("#branch_id").val());
fetchSelectBox(multiColl_account_sbox);
$("#multiColl_account_no").val($("#account_id").val());
$("#multiColl_collection_date").val($("#collection_date").val());
$("#advance_grid").GridUnload();


$("#advance_grid").jqGrid({
    data: [],
    datatype: "local",
    colNames: ['Bill Month',"Amount","Action"],
    colModel: [
        {name:'bill_month',index:'bill_month', width:350,sortable:false},
    	{name: 'bill_amount',index: 'bill_amount',editable: true,align:'right',width:80},
    	{name: 'action',index: 'action',editable: true,align:'center',width:30}
    	],
    pager: '#pager',
    width: 778,
    height: 276,  
    rownumbers: true,    
    footerrow: true
  
	});
$("#advance_grid_pager").hide();
$('#advance_grid').jqGrid('clearGridData');


function addAdvanceCollectionRow(){
 var fromMonthYear=$("#from_date").val();//"September 2013";
 var toMonthYear=$("#to_date").val();//"June 2014";
 var monthArray=monthsBetweenTwoMonth(fromMonthYear,toMonthYear); 
 var monthNames = ["January", "February", "March", "April", "May", "June",
  "July", "August", "September", "October", "November", "December"
];
 var collection_date=$("#collection_date").val();
 var collection_month=collection_date.substr(3,2);
 var collection_year=collection_date.substr(6,4);
 var collection_month_name=monthNames[collection_month-1];
 var collecttion_month_year=collection_month_name+"_"+collection_year;
 for(var i=0;i<monthArray.length;i++)
	{				
		if(collecttion_month_year==monthArray[i].replace(/ /g,"_")){
			continue;
		}
	    if(!($("#advance_"+monthArray[i].replace(/ /g,"_"))).length){
			be = "<input style='height:12px;width:80px;text-align:right;' id='advance_"+monthArray[i].replace(/ /g,"_")+"' onkeyup='calculateTotalBill()'class='advance' type='text' value='"+$("#rate").val()+"'  />"; 
			del="<a style='cursor:pointer;' onclick=\"$('#advance_grid').jqGrid('delRowData',"+i+");calculateTotalBill();\"><img src='/PGCL_WEB/resources/images/delete_16.png' /></a>"	        
			var customer_data = [{"bill_month":monthArray[i].replace(/ /g,", "),"bill_amount":be,"action":del}];
			jQuery("#advance_grid").jqGrid('addRowData',i,customer_data[0],"last");
		} 
	}  
	calculateTotalBill();
}

//$("#multiColl_collection_date").val(getCurrentDate());
/*
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "multiColl_collection_date",
    trigger    : "multiColl_collection_date",
    onSelect   : function() { this.hide(); }
	}));
	*/

	$('#from_date').datepicker(monthYearCalOptions);
	$('#to_date').datepicker(monthYearCalOptions);
	
	
function calculateAdvancePaymentFooterSum(){
	//var bill_amount = jQuery("#pending_grid").jqGrid('getCol', 'bill_amount', false, 'sum');
	var total_advance=0;
		$(".advance").each(function() {
                    total_advance += Number($(this).val());
                });	
	jQuery("#advance_grid").jqGrid('footerData','set', {bill_month: 'Total:  '+$("#advance_grid").getGridParam("reccount"),bill_amount:"<input type='text' id='total_advance' value='"+total_advance+"'  disabled='disabled' style='text-align:right;width:80px;' />"});
}	

function calculateTotalBill(){
	calcualteAdvanceBillTotal();
	calculatePendingBillTotal();
	
	var total_amount=0;
	total_amount = Number($("#total_pending_collection_amount").val())+Number($("#total_pending_surcharge_amount").val())+Number($("#total_advance").val());
	$("#total_amount").html(total_amount)
}

function calcualteAdvanceBillTotal(){
	calculateAdvancePaymentFooterSum();
}

function calculatePendingBillTotal() {
    var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");
    var selectedIDs = grid.getGridParam("selarrrow");
    var total_pending_amount=0;
    var total_surcharge_amount=0
    for (var i = 0; i < selectedIDs.length; i++) {
        total_pending_amount += parseFloat($("#pending_"+selectedIDs[i]).val(),10);
        total_surcharge_amount += parseFloat($("#surcharge_"+selectedIDs[i]).val(),10);
	}           
	$("#total_pending_collection_amount").val(total_pending_amount);   
	$("#total_pending_surcharge_amount").val(total_surcharge_amount);     
}

	
function valiateAndSaveMultiCollection(){	
	var isValidate=validateField("multiColl_bank_id","multiColl_branch_id","multiColl_collection_date");
	
	if(isValidate==false)
		return;
		
	setPendingBillsStr();
	setAdvanceBillsStr();
	
	$("#multiColl_customer_id").val(window.parent.document.getElementById('customer_id').value);
	
	
	
	var form = document.getElementById('multiCollForm');
	var formData = new FormData(form);
	disableButton("btn_save");
	
	
	
  	$.ajax({
    url: "saveBankMultiMonthCollection.action",
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response){ 
    /*    
     enableButton("btn_save");
     //closeModal();
     getTotalCollectionByDateAccount();
	 $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, jqDialogOptions);  
	 alert(response.message);
	 collectionForm(clearField);   
	 */  
	 closeModal();	 
	 $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, jqDialogOptions);
	 //reloadBankCollectionGrid();
	 
    }
    });   
	
	return;	
}

function reloadBankCollectionGrid(){

	$.ajax({
    url: "billCollectionBank.action"
    
    /*type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response){ };*/
	});
}

function setPendingBillsStr(){
		
	$("#pending_bills_str").val("");
	var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");

    var selectedIDs = grid.getGridParam("selarrrow");
    var pendingBills="";
    for (var i = 0; i < selectedIDs.length; i++) {
    
    	//alert($("#bill_month_"+selectedIDs[i]).val());
    	
	    pendingBills+=selectedIDs[i]+"#"+parseFloat($("#pending_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_payable_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_per_coll_"+selectedIDs[i]).val(),10)+"#"+$("#bill_month_"+selectedIDs[i]).val();
	    pendingBills=pendingBills+"@";
	}        
	//console.log(pendingBills);
	$("#pending_bills_str").val(pendingBills);
	
}	

function setAdvanceBillsStr(){
		
	$("#advanced_bills_str").val("");
	var advancedBills="";
	$(".advance").each(function() {
	     var advanceId=$(this).attr("id").replace("advance_", "");
	     var advanceIdArr=advanceId.split("_");
	     var month=Number(monthNames.indexOf(advanceIdArr[0]))+1;
	     var year=advanceIdArr[1];
	     
		 advancedBills+=month+"#"+year+"#"+Number($(this).val());
	     advancedBills=advancedBills+"@";
    })
    //console.log(advancedBills);            
	$("#advanced_bills_str").val(advancedBills);

}	

</script>