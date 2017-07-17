var rootURI="/";
var local="en_US";
var isAdmin=false;

var OperatorTable = function () {
	var oTable;
	var selected=[];
	var handleTable = function () {		
		var table=$('#operator_table');
		oTable = table.dataTable({
			"lengthChange":false,
        	"filter":true,
        	"sort":false,
        	"info":true,
        	"scrollX":"100%",
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
               
	           { data: "jobId" ,bVisible:false},	
	           { data: "code",defaultContent:""},
	           { data: "clientName",defaultContent:""},
	           { data: "createdBy"},	           
	           { data: "createdTimeStr",defaultContent:""},	           
	           { data: "doctor",defaultContent:"" },
	           { data: "assignTimeStr",defaultContent:"" ,bVisible:false},
	           { data: "reportTimeStr",defaultContent:"",bVisible:false},
	           { data: "doneTimeStr",defaultContent:"" ,bVisible:false },
	           { data: "closeTimeStr",defaultContent:""},	           
	           { 'render':function(data,status,row){
	                                var tem = row.type;
			        				var str = '';
			        				if(tem==1){
			        					str = 'Data Uploaded';
			        				}else if(tem==2){
			        					str = 'Data Assigned';
			        				}
			        				else if(tem==3){
			        					str = 'Data Downloaded';
			        				}
			        				else if(tem==4){
			        					str = 'Report Uploaded';
			        				}
			        				else if(tem==5){
			        					str = 'Report Assigned';
			        				}
			        				else if(tem==6){
			        					str = 'Report Downloaded';
			        				}
			        				else if(tem==7){
			        					str = 'Report Ready';
			        				}
			        				else{
			        					str = 'Data Uploaded';
			        				}
			        				return str;
			        			}
			           }
			   
	        ],
	        "serverSide": true,
	        "serverMethod": "GET",
	        "ajaxSource": rootURI+"report/operator/list?rand="+Math.random(),
	        "fnDrawCallback":function(oSetting){
	        	selected=[];
	        }

		});	
										
		//搜索表单提交操作
		$("#searchBtn").on("click", function(event) {
			event.preventDefault();
			var jsonData=$('#searchForm').serializeJson();
			var jsonDataStr=JSON.stringify(jsonData);			
			oTable.fnFilter(jsonDataStr);
			return false;
		});	
		
		//禁用规则
		$('#exportExcel').on('click', function (e) {
			$('#searchForm').submit();
        }); 
		
		/* handle show/hide columns*/
        var tableColumnToggler = $('#column_toggler');		
		$('input[type="checkbox"]', tableColumnToggler).change(function () {
		    /* Get the DataTables object again - this is not a recreation, just a get of the object */
		    var iCol = parseInt($(this).attr("data-column"));
		    var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
		    oTable.fnSetColumnVis(iCol, (bVis ? false : true));
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
        init: function (rootPath,local_value,isadmin) {
        	rootURI=rootPath;
        	local=local_value;
        	isAdmin=isadmin;
        	handleTable(); 
        	datePicker();
        }

    };

}();