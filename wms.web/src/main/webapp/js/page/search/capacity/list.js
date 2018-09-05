var indexdata=null,basedata=null,index=0,types="normal_map",newdata=[];
//读取数据
//板块
function getdata(){
    var url=uriapi+"/conf/warehouse_volunm_warring/base_info"
    showloading();
    ajax(url,"get",null,function(data){
    	layer.close(indexloading)
        basedata=data.body;
        $.each(basedata.board_list,function(i,item){
        	$("#board").append("<option value='" + item.board_id + "'data-index='" + i + "'>" +item.board_name + "</option>");
        })
        $("#board").val(basedata.deault_board_id);
        getcomp();
    })
}
//公司
function getcomp(){
  $("#comp option:gt(0)").remove();
  $("#ware option:gt(0)").remove();
	var index=parseInt($("#board option:selected").attr("data-index"));
	  if(index!=-1){
		$.each(basedata.board_list[index].corp_list, function(i, item) {
			$("#comp").append("<option value='" + item.corp_id + "'data-index='" + i + "'>" +item.corp_name + "</option>");
		})
		$("#comp").val(basedata.default_corp_id);
		getarea();
	}
}
//仓库号
function getarea(){
  $("#ware option:gt(0)").remove();
	var index=parseInt($("#board option:selected").attr("data-index"));
	var index1=parseInt($("#comp option:selected").attr("data-index"));
	  if(index!=-1&&index1!=-1){
		$.each(basedata.board_list[index].corp_list[index1].warehouse_list, function(i, item) {
			$("#ware").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_code+"-"+item.wh_name + "</option>");
		})
	}
}
//加载图表数据
function btnsearch(){
    var url=uriapi+"/conf/warehouse_volunm_warring/get_wearhouse_warring?wh_id="+$("#ware").val();
    showloading()
    ajax(url,"get",null,function(data){
        layer.close(indexloading)
        if(data.head.status==true){
        	indexdata=data
			$(".overview1").html(data.body.two_map.num)
			$(".overview2").html(data.body.one_map.num)
			$(".overview3").html(data.body.min_map.num)
			$(".overview4").html(data.body.normal_map.num)
			arrangdata()
			dataBindTable()
        }
    })
}
function drawdata(data){
	var myChart = echarts.init(document.getElementById('Chart'));
	myChart.clear();
	if(data[0].istrue==false){
		return false
	}else{
		var option = {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    series: [
	        {
	            name: '业务指标',
	            type: 'gauge',
	            detail: {
	            	formatter:data[0].value+"%",
	            },
	            data: data,
	            axisLine: {
	                lineStyle: {
	                    width: 10 // 这个是修改宽度的属性
	                },
	            },
	        }
	    ],
	};
	myChart.setOption(option,true);
	}
}
function arrangdata(){
	var draw=[]
	var obj={}
	var datainfo={};
	if(indexdata.body[types].data[index]!=undefined||indexdata.body[types].data[index]!=null){
		datainfo=indexdata.body[types].data[index]
		newdata=datainfo.qty_info
		obj.value=datainfo.persont
		obj.name="使用率"
		obj.istrue=true;
	}else{
		obj.istrue=false;
		newdata=[];
	}
	draw.push(obj)
	drawdata(draw)
	dataBindTable2()
}
//绑定表格
function dataBindTable(){
	if(indexdata.body[types].data.length==0){
		arrangdata()
	}
	$.each(indexdata.body[types].data,function(i,item){
			item.persontinfo=item.persont+"%"
		})
		$("#id_div_grid1").iGrid({
			columns: [
				{ th: '仓库号', col: 'wh_name', sort:false,width:180},
				{ th: '仓储区', col: 'area_name', sort:false},
				{ th: '使用率(%)', col: 'persont', sort:false}
			],
			data: indexdata.body[types].data,
			skin:"tablestyle4",
			rownumbers:false,
			isHideRadio:true,
			 resizehead:true,
			radio:true,
			GetRadioData:function(a){//选择行后触发
				$.each(indexdata.body[types].data,function(i,item){
					if(a.wh_id==item.wh_id&&a.area_id==item.area_id){
						index=i;
						return false;
					}
				})
				arrangdata()
			},
			loadcomplete:function(a){//页面绘制完成，返回所有数据。
				$(".body [data-index='0']").addClass("selected")
				$(".body input").eq(0).attr("checked","checked")
				arrangdata()
			}
		});

}

//绑定表格2
function dataBindTable2(){
	$("#id_div_grid2").iGrid({
		columns: [
			{ th: '分类', col: 'info_class', sort:false,width:180},
			{ th: '计量单位', col: 'unit_code', sort:false},
			{ th: '数量', col: 'qty', sort:false},
		],
		data:newdata,
		skin:"tablestyle4",
		rownumbers:false,
	});
}
//导出excel
function formDownload(){
    $("form [name='board_id']").val($("#board").val());
    $("form [name='fty_id']").val($("#fty").val());
    $("form [name='ware_id']").val($("#ware").val());
    $("#download").attr("action", uriapi + "/conf/warehouse_volunm_warring/download_wearhouse_warring");
    $("#download").submit();
}

$(function(){
    getdata()
    btnsearch()
    $(".btn_search").click(function(){
        btnsearch()
    })
   	$(".yj_info").click(function(){
        types =$(this).attr("data-type");
        index=0;
        dataBindTable()
    })
   	$("#board").change(function(){
   		getcomp()
   	})
   	$("#comp").change(function(){
   		getarea()
   	})
})



