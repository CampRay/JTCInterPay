//jquery插件把表单序列化成json格式的数据start 
(function($){
    $.fn.serializeJson=function(){
        var serializeObj={};
        var array=this.serializeArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value;
            }
        });
        return serializeObj;
    };
})(jQuery);

var rootURI="/";
var ManagersTable = function () {
	var oTable;
	var oLogTable;
	var selected = [];
	var handleTable = function () {				
		var viewTable = function(ids){			
			var logTable=$('#managerlog_table');
			oLogTable = logTable.dataTable({
				"lengthChange":false,
		    	"filter":false,
		    	"sort":false,
		    	"info":true,
		    	"bRetrieve": true,
		    	"processing":true,
		    	"bDestroy":true,
		    	"scrollX":"100%",
	           	"scrollXInner":"100%",
		        // set the initial value
		        "displayLength": 3,
		        "dom": "t<'row'<'col-md-6'i><'col-md-6'p>>",
		        "columns": [
		 	           { title: "ID",   data: "id" },
		 	           { title: "Admin Name",   data: "adminId" },
		 	           { title: "Content",  data: "content"},
		 	           { title: "Level", data: "level"},
		 	           { title: "Create Time", data: "createdTimeStr" },
		 	        ],
     	        "serverSide": true,
     	        "serverMethod": "GET",
     	        "ajaxSource": rootURI+"managerlog/managerslogList/"+ids+"?rand="+Math.random()
			});	
		};
	
		var table=$('#adminusers_table');
		 oTable = table.dataTable({
			"lengthChange":false,
        	"filter":true,
        	"sort":false,
        	"info":true,
        	"processing":true,
        	"scrollX":"100%",
           	"scrollXInner":"100%",
            "displayLength": 10,
            "dom": "t<'row'<'col-md-6'i><'col-md-6'p>>",
            "columnDefs": [{                    
                    'targets': 0,   
                    'render':function(data,type,row){
                    	return '<div class="checker"><span><input type="checkbox" class="checkboxes"/></span></div>';
                    },
                    //'defaultContent':'<div class="checker"><span><input type="checkbox" class="checkboxes" value="1"/></span></div>'                    
                },
                {                	
                	'targets':-1,
                	'data':null,//定义列名
                	'render':function(data,type,row){
                    	//return '<div class="actions"><a class="btn btn-default btn-sm" data-toggle="modal"  href="#view_log" id="openrluesviewmodal">view</a></div>';
                		return '<div class="actions"><a  class="btn btn-sm dark" data-toggle="modal"  href="#view_log" id="openrluesviewmodal">view</a></div>';
                    },
                    'class':'center'
                }
            ],
            "columns": [
               {"orderable": false },
	           { title: "ID",   data: "adminId"  },
	           { title: "Email",   data: "email" },
	           { title: "Role Name",    data: "roleName" },
	           { title: "Status",  
	 	        'render':function(data,status,row){
	        				var tem = row.status;
	        				var str = '';
	        				if(tem==1){
	        					str = 'Active';
	        				}else if(tem==0){
	        					str = 'Inactive';
	        				}
	        				return str;
	        			}
	           },

	           { title: "Created By", data: "createdBy",defaultContent:"" ,"bVisible":false},
	           { title: "Created Time", data: "createdTimeStr",defaultContent:"", "bVisible":false},
	           { title: "Updated By",  data: "updatedBy",defaultContent:"" ,"bVisible":false},
	           { title: "Updated Time",    data: "updatedTimeStr" ,defaultContent:"","bVisible":false},  
	           { title: "Action" ,"class":"center"},
	        ],
	        "serverSide": true,
	        "serverMethod": "GET",
	        "ajaxSource": rootURI+"manager/managersList?rand="+Math.random(),
	        "fnDrawCallback":function(oSetting){
	        	selected=[];
	        }
		});		
		 
		//打开删除对话框前判断是否已选择要删除的行
			$("#openDeleteadminsModal").on("click",function(event){
					if(selected.length==0){
						handleAlerts("Please select the rows which you want to delete.","warning","");				
						return false;
					}
				});
			$("#openActiveadminsModal").on("click",function(event){
				if(selected.length==0){
					handleAlerts("Please select the rows which you want to Active.","warning","");				
					return false;
				}
			});
			$("#openDeactiveadminsModal").on("click",function(event){
				if(selected.length==0){
					handleAlerts("Please select the rows which you want to deactive.","warning","");				
					return false;
				}
			});
		//删除操作
		$('#deleteBtn').on('click', function (e) {
			$.ajax( {
             "dataType": 'json', 
             "type": "DELETE", 
             "url": rootURI+"manager/managers/"+selected.join(), 
             "success": function(data,status){
            	 if(status == "success"){					
					 if(data.status){
						 selected=[];						 
		            	 oTable.api().draw();
		            	 oTable.$('th span').removeClass();
		            	 handleAlerts("delete the adminusers successfully.","success","");
					 }
					 else{
						 handleAlerts("Failed to delete the adminusers. " +data.info,"danger","");
					 }
				}             	 
             },
             "error":function(XMLHttpRequest, textStatus, errorThrown){
            	 alert(errorThrown);
             }
           });
        });  
		
		//激活规则
		$('#activateBtn').on('click', function (e) {
			$.ajax( {
             "dataType": 'json', 
             "type": "POST", 
             "url": rootURI+"manager/activateusers/"+selected.join(), 
             "success": function(data,status){
            	 if(status == "success"){					
					 if(data.status){
						 selected=[];						 
		            	oTable.api().draw();
		            	oTable.$('th span').removeClass();
		            	 handleAlerts("activateBtn the rules successfully.","success","");
					 }
					 else{
						 alert(data.info);
					 }
				}             	 
             },
             "error":function(XMLHttpRequest, textStatus, errorThrown){
            	 alert(errorThrown);
             }
           });
        }); 
		//禁用规则
		$('#deactivateBtn').on('click', function (e) {
			$.ajax( {
             "dataType": 'json', 
             "type": "POST", 
             "url": rootURI+"manager/deactivateusers/"+selected.join(), 
             "success": function(data,status){
            	 if(status == "success"){					
					 if(data.status){
						 selected=[];						 
		            	 oTable.api().draw();
		            	 oTable.$('th span').removeClass();
		            	 handleAlerts("deactivateBtn the rules successfully.","success","");
					 }
					 else{
						 alert(data.info);
					 }
				}             	 
             },
             "error":function(XMLHttpRequest, textStatus, errorThrown){
            	 alert(errorThrown);
             }
           });
        }); 
		//搜索表单提交操作
		$("#searchForm").on("submit", function(event) {
			event.preventDefault();
			var jsonData=$(this).serializeJson();
			var jsonDataStr=JSON.stringify(jsonData);			
			oTable.fnFilter(jsonDataStr);
			return false;
		});	
		$("#openEditRightModal").on("click",function(event){
			if(selected.length>1){
				handleAlerts("Only one row can be edited.","warning","");
				event.stopPropagation();
			}else if(selected.length==0)
			{
				handleAlerts("Please choose one row.","warning","");
				event.stopPropagation();
			}
			else{
				var data = oTable.api().row($("tr input:checked").parents('tr')).data();
	            var adminId = data.adminId;
	            var email =data.email;
	            var createby=data.createdBy;
	            var creatime=data.createdTimeStr;
	            $("#editUsersForm input[name='adminId']").val(adminId);
	            $("#editUsersForm input[name='email']").val(email);
	            $("#editUsersForm input[name='createdBy']").val(createby);
	            $("#editUsersForm input[name='createdTimeStr']").val(creatime);
			}
		});
				           
		//全选
		
		$(".group-checkable").on('change',function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            selected=[];
            if(checked){            	
	            var api=oTable.api();            
	            jQuery(set).each(function () {            	
	            	var data = api.row($(this).parents('tr')).data();
	            	var ids=data.adminId;
	                var index = $.inArray(ids, selected);
	                selected.push( ids );
                    $(this).attr("checked", true);
                    $(this).parents('tr').addClass("active");
                    $(this).parents('span').addClass("checked");
	            });
            }
            else{
            	jQuery(set).removeAttr("checked");
            	jQuery(set).parents('tr').removeClass("active");
            	jQuery(set).parents('span').removeClass("checked");
            }
            jQuery.uniform.update(set);
        });
        
        
        table.on('click', 'tbody tr a',function(){
            var data = oTable.api().row($(this).parents('tr')).data();
           var ids=data.adminId;
           if(oLogTable!=null){
        	   oLogTable.fnDestroy();
        	   viewTable(ids); 
           }else{
        	   viewTable(ids);
           }
           });
        
        //单选
        table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");            
            var data = oTable.api().row($(this).parents('tr')).data();
            var id = data.adminId;
            var index = $.inArray(id, selected);     
            if ( index === -1 ) {
                selected.push( id );
                $(this).parents('span').addClass("checked");
                $(this).attr("checked","checked");
            } else {
                selected.splice( index, 1 );
                $(this).parents('span').removeClass("checked");
                $(this).removeAttr("checked");
            }
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
  //添加操作
	var AddUsers = function(){
		
		$.ajax( {
         "dataType": 'json', 
         "type":'POST', 
         "url": rootURI+"manager/addUsers", 
         "data": $('#addUsersForm').serialize(),
         "success": function(resp,status){
        	 if(status == "success"){  
        		 if(resp.status){						 
	            	 oTable.api().draw();
	            	 handleAlerts("Added the data successfully.","success","");		            	 
				 }
				 else{
					 handleAlerts("Failed to add the data."+resp.info+"the name or email exist","danger","");						 
				 }
			}             	 
         },
         "error":function(XMLHttpRequest, textStatus, errorThrown){
        	 alert(errorThrown);
         }
       });
		$('#add_users').modal('hide');
    };
    
    var AddUsersValidation = function() {
        var form = $('#addUsersForm');
        var errorDiv = $('.alert-danger', form);            
        form.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",  // validate all fields including form hidden input                
            rules: {
             adminId: {
            	required: true,
            	minlength:4,
                		},
             password: {
        		
        		required: true,
        		minlength:6,
        		maxlength:12,
        	
    				},
        	 email: {
        		
        		required: true,
        		email:true,
        		
    				}

            },
           invalidHandler: function (event, validator) { //display error alert on form submit              
                errorDiv.show();                    
            },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

            success: function (label) {
                label
                    .closest('.form-group').removeClass('has-error'); // set success class to the control group
            },
            onfocusout:function(element){
            	$(element).valid();
            },
            submitHandler: function (form) { 
            	errorDiv.hide();
            	AddUsers();
            }
        });
    };
    
	//编辑表单提交操作
	var EditUsers= function() {
	  $.ajax( {
         "dataType": 'json', 
         "type": "POST", 
         "url": rootURI+"manager/editUsers", 
         "data" :$('#editUsersForm').serializeJson(),
         "success": function(resp,status){
        	 if(status == "success"){  
        		 if(resp.status){
					 selected=[];
	            	 oTable.api().draw();
	            	 handleAlerts("Edited the data successfully.","success","");
				 }
				 else{
					 handleAlerts("Failed to add the data."+resp.info+"the email is exist","danger","");
				 }
			}             	 
         },
         "error":function(XMLHttpRequest, textStatus, errorThrown){
        	 alert(errorThrown);
         }
       });
	  $('#edit_users').modal('hide');
	};
		
            
	var EditUsersValidation = function() {
		var form = $('#editUsersForm');
		var errorDiv = $('.alert-danger', form);            
		form.validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block help-block-error', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			ignore: "",  // validate all fields including form hidden input                
			rules: {
				adminId: {
					required: true,
					minlength:4,
            			},
            	password: {
            		minlength:6,
            		maxlength:12,
					},
				email: {
					required: true,
					email:true,
				}

        },
       invalidHandler: function (event, validator) { //display error alert on form submit              
            errorDiv.show();                    
        },

        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        },
        onfocusout:function(element){
        	$(element).valid();
        },
        submitHandler: function (form) { 
        	errorDiv.hide();
        	EditUsers();
        }
    });
};
    

    return {
        //main function to initiate the module
        init: function (rootPath) {
        	rootURI=rootPath;
        	handleTable();  
        	AddUsersValidation();
        	EditUsersValidation();        	
        }

    };

}();
