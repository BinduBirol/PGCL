<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="/PGCL_WEB/resources/js/template/jquery-latest.js"></script>

<s:if test="ipgResponse.txnStatus == 'success">
Success
</s:if>
<s:else>
Failed
</s:else>

	

