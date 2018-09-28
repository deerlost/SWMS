var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true,pldata=[];

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '物料类型', col: 'mat_type'},
			{ th: '物料编码', col: 'mat_code',min:200},
			{ th: '物料描述', col: 'mat_name' ,min:200},
			{ th: '包装物单位', col: 'name_zh' },
			{ th: '包装物类型', col: 'package_type_code', min: 60},
			{ th: '包装物类型描述', col: 'package_type_name', min: 60},
			{ th: '启用序列号 ', col: 'sn_used_name', min: 60},
			{ th: '是否冻结', col: 'is_freeze_name', min: 60},
			{ th: '包装单位', col: 'name_zh', min: 60},
			{ th: '换算关系(物料-包装)', col: 'convers_relation'},
			{ th: 'ERP批次关键字', col: 'erp_batch_key' },
			{ th: 'SN序列号关键字', col: 'sn_serial_key', min: 60},
			{ th: '物料单位', col: 'mat_name_zh', min: 60},
		], 
		data: indexdata.body,
		checkbox: true,
		rownumbers:false,
		selectedrow:{
			args:["dic_mat_package_type_id"],
			isHasindex:true
		},
		absolutelyWidth:true,
		percent:50,//空占位百分比
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
		GetSelectedData(a){
		console.log(a);
			pldata=a;
			}
	   });
}

// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}
function frozen(){
	var obj={};
	var packagelist=[];
	if(pldata.length==0){
		layer.msg("请选择要冻结的数据");
		return false;
	}else{
		$.each(pldata,function(i,item){
			packagelist.push(item.dic_mat_package_type_id)
		})
	}
	obj.dic_mat_package_type_id=packagelist.join(",")
	showloading();
    var url = uriapi + '/conf/mat_package/update_mat_package_type_freeze';
 	ajax(url, 'POST', JSON.stringify(obj), function(data) {
	  setTimeout(function() {
		layer.close(indexloading);}, 50);
		 if(data.head.status == true) {
				actionpager();
		}
	}, function(e) {
		layer.close(indexloading);
	});   
}
function clicksubmit(){
    $(".froen").click(function () {
        layer.confirm("是否冻结选中数据？", {
            btn: ['冻结', '取消'],
            icon: 3
        }, function () {
            frozen();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
}
function getinfo(index){
	return indexdata.body[index];
}
function actionpager(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
    showloading();
    var url = uriapi + '/conf/mat_package/get_mat_package_type_list';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: totalcount,
        condition:kw,
        sort_column:strorder,
		sort_ascend:strsort
    }
    ajax(url, 'post', JSON.stringify(obj), function (data) {
    	$.each(data.body,function(i,item){
    		if(item.is_freeze==0){
    			item.is_freeze_name="否";
    		}else{
    			item.is_freeze_name="是";
    		}
    	})
        indexdata = data;
        if (indexdata.body.length>0) {
        	$(".froen").show();
            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
        }else{
        	$(".froen").hide();
            $("#J_ListPagination").html("");
        }
        dataBind();
        setTimeout(function () { layer.close(indexloading); }, 50);
    }, function (e) {
        layer.close(indexloading);
    });
}
$(function () {
    actionpager();
     clicksubmit();
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            kw=$("input.search").val()
            actionpager(0);
        }
    });
    $("a.search").click(function () {
        kw=$("input.search").val()
        actionpager();
    });
    tableedit($("body"))
});