<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("ownershipChange.action");
	setTitle("Ownership Change");
</script>
<form id="ownershipForm" name="ownershipForm">
<div class="row-fluid">
	<div style="width: 48%;height: 98%;float: left;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Old Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Id</label>	
								<input type="text" id="customer_id" name="customer.customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 12%;margin-top: -4px;"  />
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Name</label>	
								<input type="text" style="width: 71%"  id="old_full_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">F/H Name</label>	
								<input type="text" style="width: 71%"  id="old_father_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mother Name</label>	
								<input type="text" style="width: 71%"  id="old_mother_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Gender</label>	
								<select id="old_gender"  style="width: 73.5%;" disabled="disabled">
			                        <option value="" selected="selected">Select Gender</option>
			                            <option value="Male" >Male</option>
			                            <option value="Female" >Female</option>
			                            <option value="Others" >Others</option>
			                    </select>								
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mobile</label>	
								<input type="text" style="width: 71%"  id="old_mobile" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Phone</label>	
								<input type="text" style="width: 71%"  id="old_phone" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Fax</label>	
								<input type="text" style="width: 71%"  id="old_fax" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">National Id</label>	
								<input type="text" style="width: 71%"  id="old_national_id" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Passport</label>	
								<input type="text" style="width: 71%"  id="old_passport_no" disabled="disabled"/>									
								
						  	</div>	
						 </div>						 
				</div>
		 </div>
		</div>
	</div>
	
	
	<div style="width: 48%;height: 98%;float: left;margin-left: 1%;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">New Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%"></label>	
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Name</label>	
								<input type="text" style="width: 71%"  id="full_name" name="personal.full_name"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">F/H Name</label>	
								<input type="text" style="width: 71%"  id="father_name" name="personal.father_name"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mother Name</label>	
								<input type="text" style="width: 71%"  id="mother_name" name="personal.mother_name"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Gender</label>	
								<select id="gender"  style="width: 73.5%;" name="personal.gender">
			                        <option value="" selected="selected">Select Gender</option>
			                            <option value="Male" >Male</option>
			                            <option value="Female" >Female</option>
			                            <option value="Others" >Others</option>
			                    </select>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mobile</label>	
								<input type="text" style="width: 71%"  id="mobile" name="personal.mobile"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Phone</label>	
								<input type="text" style="width: 71%"  id="phone" name="personal.phone"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Fax</label>	
								<input type="text" style="width: 71%"  id="fax" name="personal.fax"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">National Id</label>	
								<input type="text" style="width: 71%"  id="national_id" name="personal.national_id"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Passport</label>	
								<input type="text" style="width: 71%"  id="passport_no" name="personal.passport_no"/>									
								
						  	</div>	
						 </div>
				</div>
		 </div>
		</div>
	</div>

</div>
</form>


<div class="row-fluid" style="text-align: center;padding-top: 10px;">
	<button id="btn_edit" class="btn btn-beoro-3" onclick="validateAndSaveChangeOwnershipInfo()" type="button">
	<span class="splashy-application_windows_edit"></span>
	Change Ownership
	</button>
</div>

<div id="customer_grid_div" style="height: 39%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>All Customer</a></li>
                <li><a>Ownership Change History (<font color="blue" style="font-weight: bold;">For this customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="ownership_change_history_grid"></table>
					<div id="ownership_change_history_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>
<script type="text/javascript" src="/PGCL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/PGCL_WEB/resources/js/page/ownershipChange.js"></script>