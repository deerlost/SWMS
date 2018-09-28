var pardata=null,files=[];
$(function(){
	var index=GetQueryString('index');
	pardata=parent.setdatainfo(index);
	if(pardata.file_list!=null&&pardata.file_list!=undefined&&pardata.file_list.length>0){
		files=pardata.file_list;
		pardata.files=pardata.file_list;
		uploadresult()
	}
	tableedit($("body"));
	accessory();
	$(".btn_submit").click(function(){
		subission()
	})
	
})
function accessory() {
	var filehtml = location.origin+"/wms/web/html/common/addfile1.html?no="+pardata.mat_id+"&type=1&actionurl="+loginuserdata.body.file_server;
	$(".addfile").attr("data-href", filehtml);
}

function uploadresult(obj) {
	if(obj!=undefined){
		pushfile(obj,files);
	}else{
		pushfile(null,files);
	}
}
function pushfile(obj,files){
	if(obj!=null){
		files=[];
		files.push(obj);
		pardata.files=files
	}
	drawfiles1(files);
}
// 画文件列表
function drawfiles1(files){
	if(files.length>0){
		$(".btn_submit").show();
	}else{
		$(".btn_submit").hide();
	}
	$(".filebox").empty();
	$.each(files, function(i, item) {
		var strtext = "";
		strtext += ("<div class='file imginfo' data-file='" + item.file_id + "'>");
		strtext += ("<div class='filename'><a href='"+loginuserdata.body.file_server+"download?file_id="+item.file_id+ "' target='_blank'><img id='img' class='max' src='"+loginuserdata.body.file_server+"download?file_id="+item.file_id+ "'></a></div>");
		strtext += ("<div class='clearfx'></div>");
		strtext += ("<div class='filenam'>" + "" + "</div>"); //上传完成
		strtext += ("</div>");
		$(".filebox").prepend(strtext);
	});
}

/**
 * 提交
 * @returns
 */
function subission(){
	showloading();
	var obj={
		"mat_id":pardata.mat_id,
		"file_list":pardata.files
	}
    var url = uriapi + "/conf/mat/add_img"
    ajax(url, "POST", JSON.stringify(obj), function(data) {
        if(data.head.status == true) {
            layer.close(indexloading);
            parent.location.href = "wlList.html";
            $(".btn_iframe_close_window").click(); 
        } else {
            layer.close(indexloading);
        }

    }, function(e) {
        layer.close(indexloading);
    },true);
}
