var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="",kw="",strsort=true,pldata=[];

function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
        	{ th: '客户名称', col: 'receive_name' },
        	{ th: '交货单号', col: 'refer_receipt_code'},
        	{ th: '订单类型', col: 'order_type_name' },
        	{ th: '交货单行号', col: 'refer_receipt_rid'},
        	{ th: '物料编码', col: 'mat_code'},
        	{ th: '物料描述', col: 'mat_name'},
            { th: '出库数量', col: 'output_qty'},
            { th: '单位', col: 'unit_code'},
            { th: '库存地点', col: 'loc_name_code', sort:false},
            { th: '创建人', col: 'create_user' },
            { th: '创建日期', col: 'create_time' },
            { th: '状态', col: 'status_name'},
            { th: '出库单号',col:'stock_output_code', type:"link",
            	link:{
                    href:false,
                    autocreate:false,
                    args:["stock_output_id|id"],
                    column:"receipt_type",
                    links:[
                    	{value:22,href:"../../../html/storage/output/sell/detail.html"},
                    	{value:25,href:"../../../html/storage/output/production/detail.html"},
                    	{value:26,href:"../../../html/storage/specificword/selloutput/detail.html"},
                    	{value:27,href:"../../../html/storage/specificword/selloutput/detail.html"},
                    	{value:28,href:"../../../html/storage/specificrecord/output/detail.html"}
                    ]
            	}
            },
            { th: '出库类型',col:'receipt_type_name'}
        ],
        data: indexdata.body,
       	checkbox: true,
		rownumbers:false,
        percent:30,//空占位百分比
        skin:"tablestyle4",//皮肤
        csort:function(a,b){
		if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
            actionpager();
        },
        GetSelectedData:function(a){
			pldata=a;
			
        }
        
    });
}
function closeticket(){
	if(pldata.length==0){
		layer.msg("请选择要冲销的数据");
	}else{
		var obj={};
		var ares=[];
		$.each(pldata,function(i,item){
			ares.push(item.stock_output_id)
		})
		obj.ids=ares
		obj.status=10;
		var url=uriapi+"/conf/output/all/update_order_status";
	    showloading();
	    ajax(url,"POST",JSON.stringify(obj),function(data){
	    	if(data.head.status==true){
	    		location.href="list.html"
	    	}else{
	    		layer.close(indexloading);	
	    	}
	    },function(e) {
	        layer.close(indexloading);
	    })
  }
}
function openticket(){
	if(pldata.length==0){
		layer.msg("请选择要开票的数据");
	}else{
		var obj={};
		var ares=[];
		$.each(pldata,function(i,item){
			ares.push(item.stock_output_id)
		})
		obj.ids=ares
		obj.status=8;
		var url=uriapi+"/conf/output/all/update_order_status";
	    showloading();
	    ajax(url,"POST",JSON.stringify(obj),function(data){
	    	if(data.head.status==true){
	    		location.href="list.html"
	    	}else{
	    		layer.close(indexloading);	
	    	}
	    },function(e) {
	        layer.close(indexloading);
	    })
  }
}
function clicksubmit(){
    //开票
    $(".ticket").click(function() {
        layer.confirm("是否开票？", {
            btn: ['开票', '取消'],
            icon: 3
        }, function() {
           	openticket();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    }); 
    //冲销
    $(".ticketclose").click(function() {
        layer.confirm("是否冲销？", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function() {
           	closeticket();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    }); 
}
function files() {
    var status="";
    $(".chkbox input:checked").each(function(i,item){
    	status = $(this).val()+","+status;
    })
	$("[name='stock_output_code']").val($(".stock_output_code").val());//关键字
	$("[name='refer_receipt_code']").val($(".refer_receipt_code").val());//状态集合
	$("[name='preorder_id']").val($(".preorder_id").val());//关键字
	$("[name='receive_name']").val($(".receive_name").val());//状态集合
	$("[name='user_name']").val($(".user_name").val());//关键字
	$("[name='mat_name']").val($(".mat_name").val());//状态集合
	$("[name='status']").val(status.substring(0,status.length-1));//状态集合
	$("#download").attr("action", uriapi + "/conf/output/all/download_output_data");
	$("#download").submit();
}
// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
		is_number: true,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}

function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	actionpager()
}

function actionpager(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
    var status_list=[]
    $(".chkbox input:checked").each(function(i,item){
        status_list.push($(this).val())
    })
    if(status_list.length>0){
	   	showloading();
	    var url = uriapi + '/conf/output/all/get_order_list';
	    var obj={
	        page_index: pageindex||1,
	        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
	        total: totalcount,
	        status:status_list,
	        stock_output_code:$(".stock_output_code").val(),
	        refer_receipt_code:$(".refer_receipt_code").val(),
	        preorder_id:$(".preorder_id").val(),
	        receive_name:$(".receive_name").val(),
	        user_name:$(".user_name").val(),
	        mat_name:$(".mat_name").val(),
	        receipt_type_list:[22,25,26,27,28],
	        sort_column:strorder,
			sort_ascend:strsort
	    }
	    ajax(url, 'post', JSON.stringify(obj), function (data) {
		        $.each(data.body,function(i,item){
		        	item.loc_name_code=item.location_code+"-"+item.location_name;
		        })
	        	indexdata = data;
	        if (indexdata.body.length>0) {
	        	$(".ticket").show();
	        	$(".print").show();
	            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
	        }else{
	            $("#J_ListPagination").html("");
	        }
	        dataBind();
	        setTimeout(function () { layer.close(indexloading); }, 50);
	    }, function (e) {
	        layer.close(indexloading);
	        layer.alert("数据请求失败");
	    });
    }else{
    	$(".ticket").hide();
	    $(".print").hide();
    	indexdata.body=[];
    	dataBind();
    }
}
$(function () {
	$(".btn_search").click(function(){
		actionpager(0)
	})
	clicksubmit();
    actionpager();
    $(".chkbox input").change(function(){
    	actionpager();
    })
    $(".print").click(function(){
    	files();
    })
    tableedit($("body"))
});