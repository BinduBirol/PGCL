$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("",$('#customer_id').val(),loadDuesList);},
}));
function clearRelatedData(){	
	clearField.apply(this,customer_info_field);		
	clearGridData("dues_bill_grid","installment_history_this_grid","installment_grid");
	clearField("agreement_start_month","total_installment","agreement_date","notes");	
	$("#installment_bill_table").find("tr:gt(0)").remove();
}

var billSelectBox="";
var billIds="";
var editable=false;


var installmentData=[],
	grid = $("#installment_grid"),
	delOptions = {
			onclickSubmit: function (rp_ge, rowid) {
            var grid_id = $.jgrid.jqID(this.id)
            rp_ge.processing = true;
            var selRow = $('#'+rowid),   // get the row (<tr> element having id=rowid)
            nextRow = selRow.next(); // get the next row
            var rowData = jQuery("#treegrid").getRowData(rowid);
			if (nextRow.hasClass('ui-subgrid')) {
				// if the next row is a sub-grid one should remove it
				nextRow.remove();
			}
			grid.jqGrid('delRowData', rowid);
            $("#delmod" + grid[0].id).hide();            
            $.jgrid.hideModal("#delmod" + grid_id, {
                    gb: "#gbox_" + grid_id,
                    jqm: rp_ge.jqModal,
                    onClose: rp_ge.onClose
                });
            return true;
        },
        processing: true
    },
    colNames = ['Id','Serial', 'Desc.', 'Due Date','Issue Date', 'Principal', 'Surcharge', 'Meter Rent', 'Total',"Installment Dtl.","Edit","Delete","Status","Generated By"],
    colModel = [
                { name: 'id', index: 'id', key: true, width: 70, sorttype: "int",hidden:true,sortable:false },
                { name: 'serial', index: 'serial', key: true, width: 70, sorttype: "int" ,align:'center',sortable:false},
                { name: 'desc', index: 'desc', width: 100, sorttype: "date",sortable:false },
                { name: 'due_date', index: 'due_date', width: 80 ,sortable:false},
                { name: 'issue_date', index: 'issue_date', width: 80 ,sortable:false},
                { name: 'principal', index: 'principal', width: 100, align: "right", sorttype: "float",sortable:false},
                { name: 'surcharge', index: 'surcharge', width: 100, align: "right", sorttype: "float",sortable:false },
                { name: 'meter_rent', index: 'meter_rent', width: 100, align: "right", sorttype: "float",sortable:false },
                { name: 'total', index: 'total', width: 100,align: "right", sortable: false,sortable:false },                
                { name: 'installment_dtl', index: 'installment_dtl', width: 100 ,hidden:true,sortable:false },
                { 
            		name: 'edit', 
            		width: 60, 
            		align:'center',
            		sortable:false,
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-pencil' style='margin-left:18px;cursor:pointer'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            return ' onClick="editInstallment(\''+rowId+'\')"';
                    }
                },
                { name: 'delete', index: 'delete', width: 60, align: 'center', sortable: false, formatter: 'actions',editable: false,sortable:false,
                        formatoptions: {
                            keys: true, // we want use [Enter] key to save the row and [Esc] to cancel editing.
                            delOptions: delOptions
                        }
                 },
                 { name: 'status', index: 'status', width: 100 ,hidden:true,sortable:false },
                 { name: 'generated_by', index: 'generated_by', width: 100 ,hidden:true,sortable:false }  //by client or by server
            ];

	grid.jqGrid({
	    data: installmentData,
	    datatype: "local",
	    colNames: colNames,
	    colModel: colModel,
	    pager: '#pager',
	    sortname: 'id',
	    sortorder: 'asc',
	    viewrecords: true,
	    rowNum: 10000,
	    height: "100%",
	    caption: "Installments",
	    subGrid: true,
	    footerrow: true,
	    height: $("#right_div").height()-130,
		width: $("#right_div").width(),
	    subGridRowExpanded: function(subgrid_id, row_id) {	
		
		var rowData = grid.getRowData(row_id);	
		var installment_dtl=rowData["installment_dtl"];
    	var status=rowData["status"];
    	var generatedBy=rowData["generated_by"];
			generateSubGridHtml(subgrid_id, row_id,installment_dtl,status,generatedBy);
	    },
	    subGridRowColapsed: function(subgrid_id, row_id) {	

	    	
	    	var tableId="installment_"+row_id;
			var segmentDetailStr="";
			
			 $('#'+tableId+' > tbody > tr').each(function (i, row) {
		        var $row = $(row),		        
		        	$bill = $row.find('select[id*="bill"]'),
		            $principal = $row.find('input[id*="principal"]'),
		            $surcharge = $row.find('input[id*="surcharge"]'),
		            $meter_rent = $row.find('input[id*="meter_rent"]'),
		            $total = $row.find('input[id*="total"]');	
		        
		            //SegmentId @ Bill Id @ Bill Month-Year(Desc) $ Principal @ Surcharge @ Meter Rent @ Total
		        	segmentDetailStr+="1"+"@"+$bill.val()+"@"+$bill.val()+"@"+$principal.val()+"@"+$surcharge.val()+"@"+$meter_rent.val()+"@"+$total.val()+"$";  // $ = Segment SEperator
			  });
			 if(segmentDetailStr.length>0){
				 segmentDetailStr=segmentDetailStr.substring(0,segmentDetailStr.length-1); 
			 }
			 /***/
	    	grid.jqGrid('setRowData', row_id, {installment_dtl: segmentDetailStr });
	    },
	    gridComplete: function () {
	    	removeEditDelelteIcon();
	    	
	    	$("#global_collapse_expand_anchor").click();
	    	
	    	//Or
	    	/*
	    	var rowIds = grid.getDataIDs();
            $.each(rowIds, function (index, rowId) {
               grid.expandSubGridRow(rowId); 
            });
            */ 
	    },
	    loadComplete: function () {	    	
	    	calcuateGridFooterTotal();
            	
        },
        rowattr: function (rd) {
            return {"class": "installmentRow"};           
        }

	});
	grid.jqGrid('navGrid', '#pager', { add: false, edit: false, del: false, search: false, refresh: false });


	
	 var subGridOptions = grid.jqGrid("getGridParam", "subGridOptions"),
     plusIcon = subGridOptions.plusicon,
     minusIcon = subGridOptions.minusicon,
     expandAllTitle = "Expand all subgrids",
     collapseAllTitle = "Collapse all subgrids";
     $("#jqgh_" + grid[0].id + "_subgrid")
        .html('<a id="global_collapse_expand_anchor" style="cursor: pointer;"><span class="ui-icon ' + plusIcon + '" title="' + expandAllTitle + '"></span></a>')
        .click(function () {
            var $spanIcon = $(this).find(">a>span"),
                $body = $(this).closest(".ui-jqgrid-view")
                    .find(">.ui-jqgrid-bdiv>div>.ui-jqgrid-btable>tbody");
            if ($spanIcon.hasClass(plusIcon)) {
                $spanIcon.removeClass(plusIcon)
                    .addClass(minusIcon)
                    .attr("title", collapseAllTitle);
                $body.find(">tr.jqgrow>td.sgcollapsed")
                    .click();
            } else {
                $spanIcon.removeClass(minusIcon)
                    .addClass(plusIcon)
                    .attr("title", expandAllTitle);
                $body.find(">tr.jqgrow>td.sgexpanded")
                    .click();
            }
            
            //For hiding delete, add, edit icons....
            //hideElementByClass("img_add","img_delete","ui-icon.ui-icon-trash");
            //$("td[aria-describedby='installment_grid_edit']" ).hide();
            
        });

     function calcuateGridFooterTotal(){
    	 
    	var principal = grid.jqGrid("getCol", "principal", false, "sum");
    	var surcharge = grid.jqGrid("getCol", "surcharge", false, "sum");
    	var meter_rent = grid.jqGrid("getCol", "meter_rent", false, "sum");
    	var total = grid.jqGrid("getCol", "total", false, "sum");
    	
     	grid.jqGrid("footerData", "set", {due_date: "Total:", principal: principal,surcharge: surcharge,meter_rent: meter_rent,total: total});
     }
function removeEditDelelteIcon(){
	 $( ".ui-pg-div.ui-inline-edit" ).remove();
     $( ".ui-pg-div.ui-inline-save" ).remove();
     $( ".ui-pg-div.ui-inline-cancel" ).remove(); 	
}
function generateSubGridHtml(subgrid_id, rowId,installment_dtl,status,generatedBy){
	//alert(installment_dtl);
	//var installment_dtl=getFieldValueFromSelectedGridRow("list","installment_dtl"),
	tableId="installment_"+rowId;
	var principal="";
                           
	var html=  '   <table id="'+tableId+'" class="hor-minimalist-b" style="float:left;border:1px solid #C0C0C0;">  '  + 
	 '       <thead>  '  + 
	 '       	<tr>  '  + 
	 '           	<th scope="col" width="25%">Monty, Year</th>  '  + 
	 '               <th scope="col"  width="15%" style="text-align:center;">Principal</th>  '  + 
	 '               <th scope="col"  width="15%" style="text-align:center;">Surcharge</th>  '  + 
	 '               <th scope="col"  width="15%" style="text-align:center;">Meter Rent</th>  '  + 
	 '               <th scope="col"  width="20%" style="text-align:center;">Total</th>  '  +
	 '               <th scope="col"  width="10%" style="text-align:center;">Delete</th>  '  +
	 '           </tr>  '  + 
	 '       </thead>  '  + 
	 '       <tbody>  ';

	console.log(installment_dtl);
	
	if(installment_dtl){                
	
		 var segmentArr=installment_dtl.split("$");
		 for(var i=0;i<segmentArr.length;i++){
			 var segmentFieldsArr=segmentArr[i].split("@");
			 var totalRow=i+1;
					 
			 var selectBox=billSelectBox.replace(segmentFieldsArr[1]+"'", segmentFieldsArr[1]+"' selected='selected'");
			 
			 var position=selectBox.indexOf("selected='selected'");
			 position=position+20;			 
			 console.log(selectBox.substring(position));
			 var selectBoxSelectedText=selectBox.substring(position, position+selectBox.substring(position).indexOf("</option>"));
			 
			 html += '  <tr>  '  + 
			 '     <td style="text-align:left;"><labelEx class="segment_label">'+selectBoxSelectedText+'</labelEx>'+selectBox+'</td>  '  + 
			 '     <td style="text-align:center;"><labelEx  class="segment_label">'+segmentFieldsArr[3]+'</labelEx><input class="segment_input" type="text" value="'+segmentFieldsArr[3]+'" id="principal_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  +
			 '     <td style="text-align:center;"><labelEx  class="segment_label">'+segmentFieldsArr[4]+'</labelEx><input class="segment_input" type="text" value="'+segmentFieldsArr[4]+'" id="surcharge_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  +
			 '     <td style="text-align:center;"><labelEx  class="segment_label">'+segmentFieldsArr[5]+'</labelEx><input class="segment_input" type="text" value="'+segmentFieldsArr[5]+'" id="meter_rent_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  +
			 '     <td style="text-align:center;"><labelEx  class="segment_label">'+segmentFieldsArr[6]+'</labelEx><input class="segment_input" type="text" value="'+segmentFieldsArr[6]+'" id="total_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" disabled="disabled"/></td>  '  +
			 '     <td style="text-align:center;"><a href="javascript:void(0);" id="x1" onclick="deleteRow(\'x1\')" class="remCF"><img src="/PGCL_WEB/resources/images/delete_16.png" class="img_delete"/></a></td> '+
			 '  </tr>  ';
			 
		 }
		 
		}
	else{
		html+=getNewRow(rowId);
	} 
	html+=' </tbody>  '  + 
		  '  </table>  '+
		  '   <div style="float:left;padding:10px;"><a href="javascript:void(0);" onclick="addInstallmentDetailRow(\''+tableId+'\',\''+rowId+'\')"><img src="/PGCL_WEB/resources/images/icons/plus.png" class="img_add" /></a></div> '; 
	
	$("#" + subgrid_id).append(html);
	
	//status=="0"
	 if(generatedBy=="CLIENT"){
		$("#"+tableId+" td "+" .segment_input").show();
		$("#"+tableId+" td "+" .segment_label").hide();
	 }
	 else{
		 $("#"+tableId+" td "+" .segment_input").hide();
		 $("#"+tableId+" td "+" .segment_label").show();
		 
		 $(".img_delete").hide();
		 $(".img_add").hide();
		 
		 
	 }
	
	
}




function editInstallment(rowId){
    //show dueTextBox
    //show serialTextBox
    //Show Description Calendar
    var rowData = grid.getRowData(rowId);	
	var serial=rowData["serial"];
	var dueDate=rowData["due_date"];
	var issueDate=rowData["issue_date"];
	var desc=rowData["desc"];
	
	
	var dueDateId="due_date_"+rowId;
	var issueDateId="issue_date_"+rowId;
	var serialId="serial_"+rowId;
	var descId="desc_"+rowId;
	
	
	if(serial.indexOf("<input")==0) return; 
	
     var  textBox_dueDate = "<input type='text' id='"+dueDateId+"'  value='"+dueDate+"' style='width:100px;text-align:center;' />";
     var  textBox_issueDate = "<input type='text' id='"+issueDateId+"'  value='"+issueDate+"' style='width:100px;text-align:center;' />";
     var  textBox_serial = "<input type='text' id='"+serialId+"' value='"+serial+"' style='width:60px;text-align:center;' />";
     var  textBox_desc = "<input type='text' id='"+descId+"' value='"+desc+"' style='width:130px;text-align:left;' />";
      
        grid.jqGrid('setRowData', rowId, { 'due_date': textBox_dueDate });
        grid.jqGrid('setRowData', rowId, { 'issue_date': textBox_issueDate });
        grid.jqGrid('setRowData', rowId, { 'serial': textBox_serial });
        grid.jqGrid('setRowData', rowId, { 'desc': textBox_desc });
        
        Calendar.setup($.extend(true, {}, calOptions,{
    inputField : dueDateId,
    trigger    : dueDateId}));
        Calendar.setup($.extend(true, {}, calOptions,{
            inputField : issueDateId,
            trigger    : issueDateId}));
    
    $('#'+descId).datepicker(monthYearCalOptions);
}
function addInstallmentDetailRow(tableId,rowId){	   
	$("#"+tableId+" tbody").append(getNewRow(rowId));
}

function getNewRow(rowId){
	var totalRow=$("#installment_"+rowId+" > tbody > tr").length+1;
	
	
	return '       	<tr>  '  + 
		  '           	<td>'+billSelectBox+'</td>  '  + 
		  '               <td style="text-align:center;"><input type="text" id="principal_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  + 
		  '               <td style="text-align:center;"><input type="text" id="surcharge_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  + 
		  '               <td style="text-align:center;"><input type="text" id="meter_rent_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" onblur="calculateInstallmentTotal(\''+rowId+'\',\''+rowId+'_'+totalRow+'\')"/></td>  '  + 
		  '               <td style="text-align:center;"><input type="text" id="total_'+rowId+'_'+totalRow+'" style="width:80px;text-align:right;" disabled="disabled"/></td>  '  +
		  '               <td style="text-align:center;"><a href="javascript:void(0);" id="x1" onclick="deleteRow(\'x1\')" class="remCF"><img src="/PGCL_WEB/resources/images/delete_16.png" class="img_delete" /></a></td> '+
		  '           </tr>  ';
}

//
function calculateInstallmentTotal(rowId,suffix){
	
	$("#total_"+suffix).val(parseInt($("#principal_"+suffix).val()==""?0:$("#principal_"+suffix).val())+parseInt($("#surcharge_"+suffix).val()==""?0:$("#surcharge_"+suffix).val())+parseInt($("#meter_rent_"+suffix).val()==""?0:$("#meter_rent_"+suffix).val()));
	
    //run through each row
	var totalPrincipal=0,totalSurcharge=0,totalMeterRent=0,total=0;
	console.log($("#installment_"+rowId+" > tbody > tr").length);
    $('#installment_'+rowId+' > tbody > tr').each(function (i, row) {

        var $row = $(row),
            $principal = $row.find('input[id*="principal"]'),
            $surcharge = $row.find('input[id*="surcharge"]'),
            $meter_rent = $row.find('input[id*="meter_rent"]'),
            $total = $row.find('input[id*="total"]');
        
			
        totalPrincipal+=parseInt($principal.val()==""?0:$principal.val());
        totalSurcharge+=parseInt($surcharge.val()==""?0:$surcharge.val());
        totalMeterRent+=parseInt($meter_rent.val()==""?0:$meter_rent.val());
        total+=parseInt($total.val()==""?0:$total.val());
        

    });
    
 // first change the cell in the visible part of grid
    grid.jqGrid('setCell', rowId, 'principal', totalPrincipal);
    grid.jqGrid('setCell', rowId, 'surcharge', totalSurcharge);
    grid.jqGrid('setCell', rowId, 'meter_rent', totalMeterRent);
    grid.jqGrid('setCell', rowId, 'total', total);

    // now change the internal local data
    grid.jqGrid('getLocalRow', rowId).principal = totalPrincipal;
    grid.jqGrid('getLocalRow', rowId).surcharge = totalSurcharge;
    grid.jqGrid('getLocalRow', rowId).meter_rent = totalMeterRent;
    grid.jqGrid('getLocalRow', rowId).total = total;
    

    calculateInstallmentGridTotal();
}

function calculateInstallmentGridTotal(){
	/*
	var allRows=getAllJqGridRows("installment_grid");
	for(var i=0;i<allRows.length;i++){
		var row=allRows[i];
		//(grid.getRowData(row.id))["principal"]
	}*/
	calcuateGridFooterTotal();
}

function deleteRow(rowId){
   $("#"+rowId).parent().parent().remove();
}

$("#add_installment").click(function () {	   
	var flag=false;
	var rowId =grid.jqGrid('getGridParam','selrow');  
	var rowData = grid.getRowData(rowId);

	var gridRow=grid.getGridRowById(rowId);
	var gridColumn=$("#"+rowId).find(">td.ui-sgcollapsed");
	if(gridColumn.hasClass( "sgexpanded" )){
		grid.collapseSubGridRow(rowId);
		flag=true;
	}
	var newRowId=rowId+"_"+Math.floor((Math.random() * 9999) + 1);
	  var newRow={ id: newRowId, invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" };  
	                 grid.addRowData(newRowId, newRow, "after", rowId); 
	                 grid.expandSubGridRow(newRowId);
	                
	                if(flag==true){
	                	grid.expandSubGridRow(rowId);
	                }
});

/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~*/
//    Save Installments || i) Construct the Installment Strings  //
/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~*/

$("#save_installment").click(function () {	   
	var error=false;
	var allRows=getAllJqGridRows("installment_grid");
	var errorMessage="";
	var tableId="";
	var initCollapse=false;
	var totalSegments=0;
	
	var billIdArr=[],billMonthYearArr=[],billCounterArr=[];
	
	$("#bill option").each(function()
			{
				billIdArr.push($(this).val());
				billMonthYearArr.push($(this).text());
				billCounterArr.push(0);
			});
	
	var $spanIcon = $("#global_collapse_expand_anchor").find(">span");
	
	if ($spanIcon.hasClass(plusIcon)) {
		$("#global_collapse_expand_anchor").click();
		initCollapse=true;
	}
	
	var postString="";
	var segmentDetailStr="";
	
	for(var i=0;i<allRows.length;i++){
		var row=allRows[i];
		var rowId=row.id;
		var status=row.status;
				
		
		if(status==1) continue;
		
		billCounterArr=new Array(billCounterArr.length).fill(0);
		
		
		if($('#serial_'+rowId).is(':visible') && $('#serial_'+rowId).val()=="")
		{  
			errorMessage+="<font style='color:blue;font-weight:bold'>'"+$('#desc_'+rowId).val()+"</font> : Serial is Blank<br/>";
		}
		if($('#due_date_'+rowId).is(':visible') && $('#due_date_'+rowId).val()=="")
		{  
			errorMessage+="<font style='color:blue;font-weight:bold'>"+$('#desc_'+rowId).val()+"</font> : Due date is Blank<br/>";
		}
		if($('#issue_date_'+rowId).is(':visible') && $('#issue_date_'+rowId).val()=="")
		{  
			errorMessage+="<font style='color:blue;font-weight:bold'>"+$('#desc_'+rowId).val()+"</font> : Issue date is Blank<br/>";
		}
		
		postString+=$('#serial_'+rowId).val()+"#"+$('#desc_'+rowId).val()+"#"+$('#due_date_'+rowId).val()+"#"+$('#issue_date_'+rowId).val()+"#"+row.principal+"#"+row.surcharge+"#"+row.meter_rent+"#"+row.total+"#"+row.status+"#";
		
		tableId="installment_"+rowId;
		segmentDetailStr="";
		
		 $('#'+tableId+' > tbody > tr').each(function (i, row) {
	        var $row = $(row),		        
	        	$bill = $row.find('select[id*="bill"]'),
	            $principal = $row.find('input[id*="principal"]'),
	            $surcharge = $row.find('input[id*="surcharge"]'),
	            $meter_rent = $row.find('input[id*="meter_rent"]'),
	            $total = $row.find('input[id*="total"]');	
	        
	        billIdArr.push($(this).val());
			billMonthYearArr.push($(this).text());
			
			index=billIdArr.indexOf($bill.val());
			billCounterArr[index]=billCounterArr[index]+1;
			
	        if($principal.val()=="")
			{  
	        	errorMessage+="<font style='color:blue;font-weight:bold'>"+$('#desc_'+rowId).val()+" </font>, <font style='color:maron;font-weight:bold'>Segment - </font>"+billMonthYearArr[index]+" </font>: Principal is Blank<br/>";
			}
	        if($total.val()=="")
			{  
	        	errorMessage+="<font style='color:blue;font-weight:bold'>"+$('#desc_'+rowId).val()+" </font>, <font style='color:maron;font-weight:bold'>Segment - </font>"+billMonthYearArr[index]+" </font>: Total is Blank<br/>";
			}
	      
	        segmentDetailStr+=$bill.val()+'@'+$principal.val()+"@"+$surcharge.val()+"@"+$meter_rent.val()+"@"+$total.val()+"$";  // $ = Segment Separator
	        
	        
		  });
		 if(segmentDetailStr.length>0){
			 segmentDetailStr=segmentDetailStr.substring(0,segmentDetailStr.length-1); 
		 }
		 postString+=segmentDetailStr;
		 
		 for(var c=0;c<billCounterArr.length;c++){
	        	if(billCounterArr[c]>1){
	        		errorMessage+="<font style='color:blue;font-weight:bold'>"+$('#desc_'+rowId).val()+" </font>, <font style='color:maron;font-weight:bold'> Segment - </font>"+billMonthYearArr[index]+" : "+billMonthYearArr[c]+" occurs more than one.<br/>";
	        	}
	        }
		 postString+="I_SEP"; // I_SEP = Installment SEperator
		 
	}
	

		 
	if(initCollapse==true && errorMessage==""){
		$("#global_collapse_expand_anchor").click();
	}
	
	if(errorMessage!=""){
		var msg="<div style='max-height:300px;overflow:auto;text-align:left;'>"+errorMessage+"</div>";
		showDialog("Data Validation Failed",msg);
	}
	else{
		if(postString.length>0){
			postString=postString.substring(0,postString.length-5);
		}

		
		saveInstallments(postString);
		
	}

});


/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~*/
//    Save Installments || i) Construct the Post Data and do Ajax Post
/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~*/

function saveInstallments(installments){
	
	var form = document.getElementById('agreementForm');
	var formData = new FormData(form);
	
	
	formData.append("customer_id",$("#customer_id").val());
	formData.append("bill_ids",billIds);
	formData.append("installments",installments);
	
	//disableButton("save_installment");
 	$.ajax({
   		  type: 'POST',
   		  url: 'saveInstallments.action',
   		  data: formData,
   		 contentType: false,
         processData: false,
   		  success:function(response){   
 				enableButton("save_installment");
 				$.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, jqDialogOptions);
 				clearRelatedData();
 				
		  },
   		  error:function(){
			  alert("Something went wrong.")
			  enableButton("save_installment");
   		  }
   	});
 	
}

function PrepareInstallments(){
	//var installmentMonthsArr=monthsBetweenTwoMonth($("#installment_start_month").val(),$("#total_installment").val());
	var startMonthYear=$("#agreement_start_month").val();
	startMonthYear=startMonthYear.replace(" ", ", ");	
	
	var endMonthYear=calcuateEndMonthYear(startMonthYear,$("#total_installment").val());	
	
	var installmentMonthsArr=monthsBetweenTwoMonth(startMonthYear,endMonthYear);	
	
	
	var gridData=[];
	for(var i=0;i<installmentMonthsArr.length;i++)
	{
		var data={ id:i+1,
			   serial:i+1,
			   desc:installmentMonthsArr[i],
			   due_date:"",
			   issue_date:"",
			   principal:"",
			   surcharge:"",
			   meter_rent:"",
			   total:"",
			   installment_dtl:"",
			   generated_by:"CLIENT",
			   status:0};
           
		gridData.push(data);
	}	
       
    if(installmentMonthsArr.length>0){
    	 console.log(gridData);
         console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
         reloadGridWithData(gridData,"installment_grid");
         setAllInstallmentToEditMode();
    	$("#save_installment").show();
    	    	
    }
}

function setAllInstallmentToEditMode(){
	
	var allRows=getAllJqGridRows("installment_grid");
	for(var i=0;i<allRows.length;i++){
		var row=allRows[i];
		console.log(row);
		editInstallment(row.id);
	}
}

/*** Calculate Installment Row Total  ***/

function calcuateTotalRow(rowId){
	
	
}

/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~*/
//Load Agreement and Installments and set it to the UI
/*~~~~~------~~~~~~------~~~~~~------~~~~~~------~~~~~~*/
function getBillInstallments(agreementId){
	$.ajax({
	    url: 'getBillInstallments.action',
	    type: 'POST',
	    data: {agreement_id:agreementId},
	    cache: false,
	    success: function (response) {
	    	setAgreement(response.agreement);	    	
	    	setInstallmentBills(response.bills);
	    	setInstallments(response.installments);
	    	$("#total_installment").val(response.installments.length);	    	
	    }
	    
	  });
}

/*~~~~~------~~~~~~-*/
//Set User Agreement
/*~~~~~------~~~~~~-*/

function setAgreement(agreement){
	$("#agreement_start_month").val(agreement.startFrom);
	$("#agreement_date").val(agreement.agreementDate);
	$("#notes").val(agreement.notes);
	
	disableField("agreement_start_month","total_installment","agreement_date","notes");
	hideElement("btn_agreement_next","add_installment","save_installment");	
}

/*~~~~~------~~~~~~------~~~~~~------~~~~~~*/
//Set Installments Data to User Interface
/*~~~~~------~~~~~~------~~~~~~------~~~~~~*/
function setInstallments(installments){
	var gridData=[];
 for(var i=0;i<installments.length;i++){
	  installment=installments[i];
	  var data={ id:installment.installmentId,
			   serial:installment.serial,
			   desc:installment.description,
			   due_date:installment.dueDate,
			   issue_date:installment.issueDate,
			   principal:installment.principal,
			   surcharge:installment.surcharge,
			   meter_rent:installment.meterRent,
			   total:installment.total,
			   installment_dtl:installment.segments,
			   status:installment.status,
			   generated_by:installment.generatedBy
			   };
          
	  gridData.push(data);	  	 
      //setAllInstallmentToEditMode();
 }
 reloadGridWithData(gridData,"installment_grid");
 hideElementByClass("img_add","img_delete","ui-icon.ui-icon-trash");
 $("td[aria-describedby='installment_grid_edit']" ).hide();
}

function setInstallmentBills(bills){
	$("#installment_bill_table").find("tr:gt(0)").remove();
	billSelectBox="<select id='bill' class='segment_input'>";
	for(var i=0;i<bills.length;i++){
		var bill=bills[i];
		
		console.log(bill);
		billSelectBox+="<option value='"+bill.bill_id+"'>"+bill.bill_month_name+" "+bill.bill_year+"</option>";
		var row="<tr>"+
		   			"<td align='center'>"+bill.bill_id+"</td>"+
		   			"<td align='center'>"+bill.bill_month_name+"</td>"+
		   			"<td align='center'>"+bill.bill_year+"</td>"+
		   			"<td align='right' style='padding-right:10px;'>"+bill.payable_amount+"</td>"+
		   			"<td align='right' style='padding-right:10px;'>"+bill.collection_amount+"</td>"+
		   			"<td align='right' style='padding-right:10px;'>"+bill.remaining_amount+"</td>"+
		       "</tr>";
		$("#installment_bill_table").append(row);
		
	}
	billSelectBox+="</select>";
}