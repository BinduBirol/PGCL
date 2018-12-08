var uBank_id,uBranch_id,uAccount_no;

function updateBankAccountBalance(){
	uBank_id=$("#bank_id").val();
	uBranch_id=$("#branch_id").val();
	uAccount_no=$("#account_id").val();
	
	if(uBank_id=="" || uBranch_id=="" || uAccount_no==""){
		alert("Please select a bank account information for balance update.");
		return;
	}
	var formData = new FormData($('form')[0]);
	$.ajax({
	    url: 'bankBalanceUpdate.action',
	    type: 'POST',
	    data: formData,
	    success: function (response) {
	    	if(response.status=="OK"){
	  			//enableButton("btn_save");
	  		}
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
	    }			    
	  });
}
