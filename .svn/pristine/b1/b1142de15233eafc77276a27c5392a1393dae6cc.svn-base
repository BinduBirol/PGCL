function checkType(type){
	
	if(type=="area_wise")
	{
	 resetSelectBoxSelectedValue("customer_ministry");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 disableField("area_id");
	 hideElement("ministry_div");
	}
	else if(type=="by_category"){
	 resetSelectBoxSelectedValue("customer_ministry");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 disableField("area_id");
	 hideElement("ministry_div");
	}	
	else if(type="by_ministry"){
		showElement("ministry_div");
		resetSelectBoxSelectedValue("area_id");
		resetSelectBoxSelectedValue("customer_ministry");
		enableField("customer_ministry","area_id");
		autoSelect("customer_ministry","area_id");
		
	}
	Console.log(type);
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
  


function fetchMinistryName()
{

	$("#ministry_name").val($( "#customer_ministry option:selected" ).text());
}
