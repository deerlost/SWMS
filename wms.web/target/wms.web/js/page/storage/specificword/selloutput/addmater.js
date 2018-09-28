var indexdata = null,
	pardata = null,
	cjinfo=[];
	g1=null;
function dataBind() {
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
            	{ th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false},
                { th: '单位', col: 'unit_name', sort:false },
            ],
            data: null,
            checkbox: true,
            percent:30,
            skin:"tablestyle4",//皮肤
            GetSelectedData(a) {
				cjinfo = a;
				console.log(a)
			},
        })
  }else{
        g1.showdata(indexdata.body)
    }
}
function getwlinfo(){
	var obj={};
	obj.keyword=$(".wlinfo").val();
	obj.fty_id=pardata.fty_id;
	var url = uriapi + "/biz/output/urgent/get_mat_list_fty";
	showloading();
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
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
	pardata=parent.getransfer();
	dataBind();
	if(pardata.fty_id==""){
		layer.msg("请选择工厂");
		setTimeout(function() {
              $(".btn_iframe_close_window").click();
         }, 1000);
		return false;
	}	
	$(".btnsubmit").click(function(){
		setwlinfo()
	})
	 $("input.wlinfo").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            getwlinfo();
        }
    });
    $("a.btn-search").click(function () {
        getwlinfo();
    });
});