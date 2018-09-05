var menudata = null,indexloading = -1,loginuserdata = null,files = [];

//基础配置

//var urlPrdfix = '/wms/portable';
var httphead = location.origin == undefined ? ("http://" + location.host) : location.origin;
//var httphead = "http://192.168.2.63:8080"
var uriapi = httphead + "/wms/portable";
var httpApi = uriapi;


//基础功能
function showloading() {
    indexloading = layer.load(3, {
        shade: [0.4, '#000']
    });
}
function tableedit(table) {
	var btnedit = table.find(".btn_table_edit");
	btnedit.unbind("click");
	btnedit.click(function(e) {
		var title = $(this).attr("data-title");
		var href = $(this).attr("data-href");
		var size = $(this).attr("data-size");
		var size_x = size.split(",")[0];
		var size_y = size.split(",")[1];
		layer.open({
			type: 2,
			title: title,
			shadeClose: false,
			shade: 0.8,
			area: [size_x, size_y],
			content: href
		});
		e.stopPropagation();
	});
}
//值名
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r != null) return unescape(r[2]);
    return null;
}


//对象转url
function parseParam(param, key){
    var paramStr="";
    if(param instanceof String||param instanceof Number||param instanceof Boolean){
        paramStr+="&"+key+"="+encodeURIComponent(param);
    }else{
        $.each(param,function(i){
            var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
            paramStr+='&'+parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};

//url转对象
function parseQueryString() {
    var url=window.location.href
    var reg_url = /^[^\?]+\?([\w\W]+)$/,
        reg_para = /([^&=]+)=([\w\W]*?)(&|$|#)/g,
        arr_url = reg_url.exec(url),
        ret = {};
    if (arr_url && arr_url[1]) {
        var str_para = arr_url[1], result;
        while ((result = reg_para.exec(str_para)) != null) {
            ret[result[1]] = result[2];
        }
    }
    return ret;
}

function dataIsNull(table) {
    if(table.find("tr").length == 1) table.append("<tr class='datanull'><td class='datanull' colspan='" + (table.find("th").length) + "'>没有可显示的数据</td></tr>");
}

function ajax(url, type, data, successcallback, errorcallback) {
    errorcallback = successcallback || function() {};
    $.ajax({
        type: type,
        url: url,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        data: data,
        contentType: "application/json",
        success: function(data) {
            successcallback(data);
        },
        error: function(xhr, textStatus, errorThrown) {
            errorcallback(textStatus)
        }
    });
}