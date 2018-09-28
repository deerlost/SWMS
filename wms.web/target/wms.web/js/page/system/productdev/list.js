var indexdata=null,selectdata=null,g1=null,g2=null,area=[],position=[],sn_list=[],noallot=new Array(),package=[],colors=-1,table={select:'select'}

//基础数据加载
function init(){
    showloading();
    var url=uriapi+"/conf/product_line/get_dic_product_installation_list";
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        indexdata=data
         dataBindPosition();
    })
}
//绑定产品线
function dataBindPosition(){
	if(g1==null){
		g1=$("#id_div_grid_position").iGrid({
	        columns: [
	            { th: '产品线', col: 'product_line_name',width:150},
	            { th: '操作', col: 'product_line_id',type:"popup",width:80,
	            	popup:{
	            		display:"修改",
	            		href:"edit.html",
	            		area:"500px,300px",
	            		args:["product_line_id"],
	            		title:"修改",
	            	}
	            },
	           { th: '废弃', col: 'is_used', class:"",width:70, align: 'center',type:"bool",
					 	bool:{//bool型的数据，真假值的设定：可以 BOOL，可以String，可以Char，可以数字。
							truevalue:1,
							falsevalue:0
						}
				},
	            { th: '',type:"delete",tips:"确定删除吗？",align:'center',visible:"isdelete",width:70}, 
	        ],
	        data: null,
	        skin:"tablestyle4",
	        radio:true,
        	isHideRadio:true,
        	mb_YesNoCancelRadio:true,
	        GetRadioData(a,b){
	            colors=b
	            if(b!=-1){
	            	if(a.installations!=undefined&&a.installations.length>0){
						$.each(a.installations,function(i,item){
							if(item.installation_id!=null&&item.installation_id!=undefined){
								item.isdeletes=0
							}else{
								item.isdeletes=1
							}
						})
						g2.showdata(a.installations)
					}else{
						g2.showdata(null)
					}
	            }else{
	            	g2.showdata(null)
	            }
	        },
	        delete:function(a,b){
	        	var index=$("#id_div_grid_position tr.selected").attr("data-index");
		        indexdata.body.splice(b,1);
		        dataBindPosition()
		      	if(index==b){
	        	 g2.showdata(null);
	         }
	        },
	        loadcomplete:function(a){//页面绘制完成，返回所有数据。
	            color()
	        }
	    })
	}else{
			$.each(indexdata.body,function(i,item){
				if(item.product_line_id!=null&&item.product_line_id!=undefined){
					item.isdelete=0;
				}else{
					item.isdelete=1;
				}
	    	})
	        g1.showdata(indexdata.body)
	   }
    color()
}
function getinfodev(index){
	return indexdata.body[$("#id_div_grid_position tr.selected").attr("data-index")].installations[index];
}
function getinfo(index){
	return indexdata.body[index]
}
//修改产品线名字
function setproduct(value,index){
	indexdata.body[index].product_line_name=value;
	dataBindPosition();
}
//修改装置名称
function setdevs(value,index){
	indexdata.body[$("#id_div_grid_position tr.selected").attr("data-index")].installations[index].installation_name=value;
	g2.showdata(indexdata.body[$("#id_div_grid_position tr.selected").attr("data-index")].installations)
}
//变色
function color(){
    if(colors!=-1){
        $("#id_div_grid_position .body tr :radio").eq(colors).prop("checked","true")
    }
    $("#id_div_grid_position .body tr").removeClass("selected")
    $("#id_div_grid_pack .body tr").removeClass("selected")
    $("#id_div_grid_position .body tr input").each(function(i){
        if($("#id_div_grid_position .body tr input").eq(i).is(":checked")){
            $("#id_div_grid_position .body tr").eq($(this).val()).addClass("selected")
        }
    })
    $("#id_div_grid_pack .body tr input").each(function(i){
        if($("#id_div_grid_pack .body tr input").eq(i).is(":checked")){
            $("#id_div_grid_pack .body tr").eq($(this).val()).addClass("selected")
        }
    })
}

//装置表格绑定
function dataBindPack(){
		g2=$("#id_div_grid_pack").iGrid({
	        columns: [
	            { th: '装置', col: 'installation_name',width:150},
	            { th: '操作', col: 'installation_id',type:"popup",width:80,
	            	popup:{
	            		display:"修改",
	            		href:"editdev.html",
	            		area:"500px,300px",
	            		args:["two_id"],
	            		title:"修改",
	            	}
	            },
	           { th: '废弃', col: 'is_used', class:"",width:70, align: 'center',type:"bool",
					bool:{//bool型的数据，真假值的设定：可以 BOOL，可以String，可以Char，可以数字。
							truevalue:1,
							falsevalue:0
						}
				},
	            { th: '',type:"delete",tips:"确定删除吗？",align:'center',visible:"isdeletes",width:70},
	        ],
	        data: null,
	        skin:"tablestyle4",
	        checkbox:true,
	        isHideCheckbox:true,
	        delete:function(a,b){
	        	var index=$("#id_div_grid_position tr.selected").attr("data-index");
	            indexdata.body[index].installations.splice(b,1);
	            g2.showdata(indexdata.body[index].installations)
	        },
	        loadcomplete:function(a){//页面绘制完成，返回所有数据。
	            $("#id_div_grid_pack .body tr :radio").removeAttr("checked")
	            color()
	        },
	        GetSelectedData(a){//选择行后触发
	            color()
	        }
	   })
}

//添加生产线
function production(){
	if($(".product_info").val()==""){
		layer.msg("产品线名称不能为空");
		return false;
	}else{
		indexdata.body.push({
			"product_line_name":$(".product_info").val()
		})
		dataBindPosition();
	}
}
//添加装置
function device(){
	var index=$("#id_div_grid_position tr.selected").attr("data-index");
	if(index==undefined){
		layer.msg("请选择左侧产品线");
		return false;
	}
	if($(".device_info").val()==""){
		layer.msg("装置名称不能为空");
		return false;
	}else{
		if(indexdata.body[index].installations==undefined||indexdata.body[index].installations==null){
			indexdata.body[index].installations=[];
		}
		indexdata.body[index].installations.push({
			"installation_name":$(".device_info").val()
		})
		g2.showdata(indexdata.body[index].installations);
	}
}
//提交
function clicksubmit(){
    $(".btn_submit").click(function () {
        layer.confirm("是否保存？", {
            btn: ['保存', '取消'],
            icon: 3
        }, function () {
            submit(null);
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });    
}
//提交
function submit(){
	var obj={};
	if(indexdata.body.length>0){
		obj.rel_product_line_installation=indexdata.body
	}else{
		layer.msg("暂无产品线&装置信息");
		return false;
	}
    showloading();
	var url = uriapi + "/conf/product_line/save_dic_product_installation";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
				location.href = "list.html";
			}, 3000);	
		}else{
		layer.close(indexloading);
		}	
	}, function(e) {
		layer.close(indexloading);
	});
}


$(function () {
	clicksubmit()
	dataBindPosition();
	dataBindPack()
	init();
    $(".product").click(function(){
        production()
    })
    $(".product_info").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            production();
        }
    });
    $(".device").click(function(){
        device()
    })
    $(".device_info").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            device();
        }
    });
});