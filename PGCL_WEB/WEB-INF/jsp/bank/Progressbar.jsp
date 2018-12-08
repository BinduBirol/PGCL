<div id="myProgress" style="width:90%; background-color: #dd1">
	<div id="myBar" style="width:1%; height:40px; background-color: #4CAF50"></div>
</div>

<script type="text/javascript">
	function move(){
	var elem=document.getElementById("myBar");
	var width=1;
	var id= setInterval(frame,10);
	function frame(){
		if(width>=100){
			clearInterval(id);
			}else{
				width++;
				elem.style.width=width + '%';
			}
		}
	}
</script>