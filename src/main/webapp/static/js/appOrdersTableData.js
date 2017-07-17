var rootURI="/";
var local="en_US";

var AppOrdersTable = function () {
	var oTable;	
	var handleTable = function () {		
		var table=$('#orders_table');
		oTable = table.dataTable({
			"lengthChange":false,
        	"filter":true,
        	"sort":false,
        	"info":true,
        	//"scrollX":"100%",
        	"scrollXInner":"100%",
        	"processing":true,                
            // set the initial value
            "displayLength": 10,
            "dom": "t<'row'<'col-md-6'i><'col-md-6'p>>",
            "oLanguage": {
                "sProcessing": loadProperties("dataTable.page.process",local,rootURI),                
                "sZeroRecords":loadProperties("dataTable.page.data.zero",local,rootURI),
                "sEmptyTable": loadProperties("dataTable.page.data.empty",local,rootURI),
                "sInfo": loadProperties("dataTable.page.info",local,rootURI),
                "sInfoEmpty":loadProperties("dataTable.page.info.empty",local,rootURI),
            },            
            "columns": [               
	           { data: "id" ,bVisible:true},	
	           { data: "createdTimeStr",defaultContent:""},
	           { data: "orderTitle",defaultContent:""},	           	          
	           { data: "orderAmount"},
	           { data: "channelName",defaultContent:""},
	           { 'render':function(data,status,row){
                        var tem = row.status;
        				var str = '';
        				if(tem==0){
        					str = loadProperties("dataTable.page.status.process",local,rootURI);
        				}else if(tem==1){
        					str = loadProperties("dataTable.page.status.success",local,rootURI);
        				}
        				else if(tem==2){
        					str = loadProperties("dataTable.page.status.failure",local,rootURI);
        				}			        				
        				return str;
        			}
	           }
			   
	        ],
	        "serverSide": true,
	        "serverMethod": "GET",
	        "ajaxSource": rootURI+"apps/ordersList?rand="+Math.random(),	        

		});	
										
		//搜索表单提交操作
		$("#searchForm").on("submit", function(event) {
			event.preventDefault();
			var jsonData=$(this).serializeJson();
			var jsonDataStr=JSON.stringify(jsonData);			
			oTable.fnFilter(jsonDataStr);
			return false;
		});	
								
        
	};
	
	
	
	//提示信息处理方法（是在页面中指定位置显示提示信息的方式）
	var handleAlerts = function(msg,msgType,position) {         
        Metronic.alert({
            container: position, // alerts parent container(by default placed after the page breadcrumbs)
            place: "prepent", // append or prepent in container 
            type: msgType,  // alert's type (success, danger, warning, info)
            message: msg,  // alert's message
            close: true, // make alert closable
            reset: true, // close all previouse alerts first
            focus: false, // auto scroll to the alert after shown
            closeInSeconds: 10, // auto close after defined seconds, 0 never close
            icon: "warning" // put icon before the message, use the font Awesone icon (fa-[*])
        });        

    };
    
    //initialize datepicker
    var datePicker = function(){
    	$('.date-picker').datepicker({
        rtl: Metronic.isRTL(),
        autoclose: true
        });
     };

    
    return {
        //main function to initiate the module
        init: function (rootPath,local_value) {
        	rootURI=rootPath;
        	local=local_value;        	
        	handleTable(); 
        	datePicker();
        }

    };

}();