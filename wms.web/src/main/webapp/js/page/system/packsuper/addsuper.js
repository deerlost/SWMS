var indexdata = null,
	pardata = null,
	cjinfo=[];
	g1=null;
	superobj=null
function dataBind() {
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
            	{ th: '供应商编码', col: 'supplier_code', sort:false },
                { th: '供应商描述', col: 'supplier_name', sort:false},
                { th: '城市', col: 'city_name', sort:false},
            ],
            data: null,
            radio: true,
            percent:30,
            skin:"tablestyle4",//皮肤
			GetRadioData:function(a,b){//选择行后触发
				superobj=a
			},
        })
  }else{
        g1.showdata(indexdata.body)
    }
}
function setsuper(){
	if(superobj!=null){
		parent.setsuperinfo(superobj);
		$(".btn_iframe_close_window").click();
	}else{
		layer.msg("请选择供应商信息")
		return false;
	}
}
function getsuper(){
	if($(".cgdh").val()==""){
			layer.msg("请输入供应商描述");
			return false
		}
	var obj={};
	obj.keyword=$(".cgdh").val()
	var url = uriapi+"/conf/supplier_package/get_supplier_list";	
	showloading();
	ajax(url, 'POST',JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			indexdata=data;
			dataBind();
		}
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function setwlinfo(){
	if(cjinfo.length==0){
		layer.msg("请至少选择一条物料信息");
		return false;
	}else{
		parent.setmateriel(cjinfo);
		$(".btn_iframe_close_window").click();
	}
}
$(function() {
	dataBind()
	$("input.cgdh").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            getsuper()
        }
    });
    $("a.btn-search").click(function () {
       getsuper()
    });
    $(".btnsubmit").click(function(){
    	setsuper()
    })
});