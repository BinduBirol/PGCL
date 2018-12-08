var row_num=($("#contentPanel").height()/27).toFixed();
row_num=parseInt(row_num);
var grid_default={
	    datatype: 'json',
	    mtype: 'GET',
		rownumbers:true,
		altRows:true,
		altclass:'altRowClass',
		cmTemplate: {editable: true, editrules: {edithidden: true}}, //This rule is necessary to show a hidden column in add,edit,view mode
        multiselect: false,
        pager: '#gridPager',
        rowNum: row_num,
        height: $("#contentPanel").height()-jsVar.GH_DEDUCT,
        width: $("#contentPanel").width()-jsVar.GW_DEDUCT,
        rowList: jsVar.PAGER_COMBO,
        viewrecords: true,
        gridview: true,
        rowattr: function (rd) {
            if (rd.status === "0") {
                return {"class": "redBackgroundRow"};
            }
        },
        ondblClickRow: function(rowid) {
          	 jQuery(this).jqGrid('viewGridRow', rowid,
                       {width:jsVar.CRUD_WINDOW_WIDTH,closeOnEscape:true});
          }
	 };
jQuery.extend(jQuery.jgrid.defaults, grid_default);

