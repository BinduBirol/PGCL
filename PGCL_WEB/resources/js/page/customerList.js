jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_LIST+'&extraFilter=area',
        jsonReader: {
            repeatitems: false,
            id: "customer_id"
        },
        colNames: ['Customer Id', 'Customer Name','Father/Husband Name','Category', 'Area','Phone','Status','Created On'],
        colModel: customerGridColModel,
        caption: jqCaption.LIST_CUSTOMER,
		rowattr: function (rd) {
            if (rd.connection_status === "0") {
                return {"class": "redBackgroundRow"};
            }
            else if (rd.connection_status === "2") {
                return {"class": "newlyAppliedRow"};
            }
        },
        sortname: 'connection_status desc,created_on ',
        sortorder: "desc",
        ondblClickRow: function(rowid) {
        	callAction('viewCustomer.action?customer_id='+rowid);
        }        
    }).navGrid('#gridPager',$.extend(footerButton,{search:true,refresh:true}),{},{},{},{},{});


