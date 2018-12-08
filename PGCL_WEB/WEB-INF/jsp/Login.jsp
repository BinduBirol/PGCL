<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
 <title>PGCL :>> User Login <<</title>
	<link href="/PGCL_WEB/resources/css/login.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript" src="/PGCL_WEB/resources/js/template/jquery-latest.js"></script>
	<script type="text/javascript">


	  function getRandomArbitrary(min, max) {
  			return Math.floor(Math.random() * (max - min) + min);
	 }
	$(document).ready(function() {
	  var img=getRandomArbitrary(1,7);
      $("body").css("background", "url('/PGCL_WEB/resources/images/login/"+img+".jpg') no-repeat scroll center top #848484");
      
      $('#password, #userId').keypress(function(e){
      if(e.keyCode==13)
      $('#bLogin').click();
    });
      
  });
	</script>
	<link rel="shortcut icon" type="image/x-icon" href="/PGCL_WEB/resources/images/favicon.ico"/>
	
</head>
<body>
<div id="container">
		<div id="content">
			<div class="top">
				<div>
					<h1>User Login</h1>
					<span class="sub-nav"> </span>
				</div>
			</div>	
			<div class="main">
					<form id="frmAuth" name="frmAuth" method="post" action="checkValidity.action">
					<div id="auth-form">
						  <label for="userId">UserId :</label>
						  <div class="row">
							  <input id="userId" type="text" value="" name="user.userId" />
					 	  </div>
						  <label for="password">Password :</label>
						  <div class="row">
						  	  <input id="password" type="password" value="" name="user.password" />
						  </div>
	
						  <div id="row-button" class="row" style="margin-left: 0px; margin-top: 16px;">
								<a id="bLogin" class="button" style="-moz-user-select: none; cursor: default;" href="#" onclick="document.frmAuth.submit();">
								<span>
								<span>
									<strong>Login</strong>
								</span>
								</span>
								</a>
								<a id="bReset" class="button" style="-moz-user-select: none; cursor: default;">
								<span>
								<span>
									<strong>Reset</strong>
								</span>
								</span>
								</a>
						 </div>
						 
		
						<br/>
						<div class="msg-error" style="clear:both;"><s:label name="err_login" /></div>
						<div style="margin-top:50px;">
						   <img src="/PGCL_WEB/resources/images/banner.png" />
						</div>
						<div style="float:right; margin-top:-240px;margin-right:120px;">
							<img src="/PGCL_WEB/resources/images/userIcon.png" width="120" />
	
						</div>


				</div>
				</form>		
			</div><!-- Main -->

			</div><!-- Content -->
			</div><!-- Container -->

</body>
</html>