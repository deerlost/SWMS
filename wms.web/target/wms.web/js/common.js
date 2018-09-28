// JavaScript Document
var menudata = null,
	indexloading = -1,
	loginuserdata = null,
	files = [];

//基础配置
var urlPrdfix = '/wms/web';

var httphead = location.origin == undefined ? ("http://" + location.host) : location.origin;

var uriapi = httphead + "/wms/web";

var httpApi = uriapi;
//基础配置 end

$(function() {
	var oHead = document.getElementsByTagName('HEAD').item(0);   
	var oScript= document.createElement("script");   
	oScript.type = "text/javascript";   
	oScript.src=uriapi+"/js/md5.js";   
	oHead.appendChild( oScript); 
	var cookieuser = $.cookie("X-Auth-Token");
	if(cookieuser == undefined || cookieuser == null)
		cookieuser = "";
	else {
		if(cookieuser.indexOf(':') > -1)
			cookieuser = cookieuser.split(':')[0];
	}
	try {
		loginuserdata = JSON.parse(localStorage.getItem(cookieuser + "loginuserdata"));
	} catch(err) {
		loginuserdata = null;
		localStorage.removeItem(cookieuser + "loginuserdata");
	}

	CreateMenuModule();

	if(loginuserdata == null) {
		var authurl = uriapi + "/auth/get_menu";
		ajax(authurl, "GET", null, function(data) {
			loginuserdata = data;
			DrawPageBase();
			localStorage.setItem(cookieuser + "loginuserdata", JSON.stringify(data));
			$(".disusername").html(loginuserdata.body.user_name);
		}, function(e) {

		});
	} else {
		DrawPageBase();
	}

	//日期统一加类，直接绑定
	//bindDatePicker();

	//iframe点取消
	$(".btn_iframe_close_window").click(function() {
		parent.layer.close(parseInt(parent.$(".layui-layer-iframe").attr("times")));
	});

	$("label :checkbox").change(function() {
		if($(this).is(":checked"))
			$(this).parent().addClass("on");
		else
			$(this).parent().removeClass("on");
	});
	$("label :checkbox").change();

	$(document.body).on("click", "table.tablestyle1 tr,table.tablestyle3 tr", function() {
		var curr = $(this),
			chk = curr.find(":checkbox"),
			rdo = curr.find(":radio");
		if(chk.length == 1) {
			var IsAll = curr.find("th").length > 0;
			if(IsAll == true) {
				/*var chk1=chk.eq(0);
				 if(chk1.is(":checked")==true)
				 curr.parents("table").find(":checkbox").prop("checked", true);
				 else
				 curr.parents("table").removeAttr("checked");*/
			} else {
				var chk1 = chk.eq(0);
				if(chk1.is(":checked") == true)
					chk1.removeAttr("checked");
				else
					chk1.prop("checked", true);
			}
			var tb = $(this).parents("table");
			if(tb.find("td :checkbox").length == tb.find("td :checkbox:checked").length)
				tb.find("th :checkbox").prop("checked", true);
			else
				tb.find("th :checkbox").removeAttr("checked");
		}
		if(rdo.length == 1) {
			var rdo1 = rdo.eq(0);
			if(rdo1.is(":checked") == true) {

			} else
				rdo1.prop("checked", true);
		}
	});

	$(document.body).on("click", "table.tablestyle1 tr th:first:has(:checkbox),table.tablestyle3 tr th:first:has(:checkbox)", function() {
		var curr = $(this),
			chk = curr.find(":checkbox"),
			currtable = curr.parents("table");
		var chk1 = chk.eq(0);
		if(chk1.is(":checked") == true) {
			chk1.removeAttr("checked");
			currtable.find("td :checkbox").removeAttr("checked");
		} else {
			chk1.prop("checked", true);
			currtable.find("td :checkbox").prop("checked", true);
		}
	});

	$(document.body).on("click", "table tr :checkbox", function(e) {
		if($(this).parent("th").length > 0) {
			var currtable = $(this).parents("table");
			if($(this).is(":checked") == true) {
				currtable.find("td :checkbox").prop("checked", true);
			} else {
				currtable.find("td :checkbox").removeAttr("checked");
			}
		} else {
			var tb = $(this).parents("table");
			if(tb.find("td :checkbox").length == tb.find("td :checkbox:checked").length)
				tb.find("th :checkbox").prop("checked", true);
			else
				tb.find("th :checkbox").removeAttr("checked");
		}
		e.stopPropagation();
	});
	$(document.body).on("click", "table tr :radio", function(e) {
		e.stopPropagation();
	});

	$(document.body).on("click", "a", function(e) {
		$(this).blur();
	});

	$("input,textarea").blur(function(){
		var str=$(this)
		str.val(str.val().trim())
	})
			
	var indexloadingpwd=-1;
	$(document.body).on("click", "#btn_a_change_pwd", function(e) {
				indexloadingpwd=layer.open({
					type: 1,
					title: '修改密码',
					shadeClose: false,
					shade: 0.8,
					area: ['400px', '300px'],
					content: $('#div_change_pwd')
				});
			});
	
	$(document.body).on("click", ".btn_a_change_pwd_submit", function(e) {
				var isChecked=true;
				var inputs=$("#div_change_pwd input");
				if(!FormDataCheck.isUserName(inputs.eq(0).val())){
					layersMoretips('请正确填写密码格式',inputs.eq(0));
					isChecked=false;
					return false;
				}
				if(inputs.eq(0).val()==''||inputs.eq(0).val().length<3){layersMoretips('必填项，最少3位',inputs.eq(0));isChecked=false;return false;}
				if(isChecked==false)return false;
				showloading();
				var url = uriapi + "/auth/change_password";
				var submitdata={"password": hex_md5(inputs.eq(0).val())};
				ajax(url, "POST", JSON.stringify(submitdata), function(data) {
					if(data.head.status){
						layer.close(indexloading);
						layer.close(indexloadingpwd);
						layer.msg("修改成功");
					}else{
						layer.close(indexloading);
						//layer.msg("修改失败");
					}
					inputs.val("");
				}, function(e) {
					layer.close(indexloading);
				});
	});
	$(document.body).on("click", ".btn_a_change_pwd_cancel", function(e) {
		layer.close(indexloadingpwd)
	})
});

//表单数据验证
var FormDataCheck = {
	isNull: function(v) {
		var p = /^.+$/;
		return !p.test(v);
	},
	checkStrLen: function(s, x, y) {
		//s为要验证的字符,x为最小长度,y为最大长度
		eval("var p = /^.{" + x + "," + y + "}$/");
		return p.test(s);
	},
	isNumber: function(s, x, y) {
		//s为要验证的字符,x为最小长度,y为最大长度
		eval("var p = /^\\d{" + x + "," + y + "}$/");
		return p.test(s);
	},
	isExcel: function(v) {
		var reg = /\.(xls|xlsx)$/i;
		return reg.test(v);
	},
	isPicture: function(v) {
		var reg = /\.(jpg|png|jpeg|gif)$/i;
		return reg.test(v);
	},
	isMp4: function(v) {
		var reg = /\.(mp4|flv)$/i;
		return reg.test(v);
	},
	isAllowFile: function(v) {
		var reg = /\.(gif|jpg|jpeg|png|rar|zip|doc|docx|pdf|xlsx|xls|ppt|mp4|txt)$/i;
		return reg.test(v);
	},
	isEmail: function(v) {
		var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return reg.test(v);
	},
	isUserName: function(v) {
		var reg = /^[a-zA-Z]\w{3,11}$/;
		return reg.test(v);
	},
	isUserRealName: function(v) {
		var reg = /^[\u4e00-\u9fa5]{2,10}$/;
		return reg.test(v);
	},
	isCellPhone: function(v) {
		var reg = /^1[345678]\d{9}$/;
		return reg.test(v);
	},
	isChinese: function(v) {
		var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
		return reg.test(v);
	},
	isPassword: function(v) {
		var reg = /^[a-zA-Z0-9_\.\-\!\@\#\$\%]{3,18}$/;
		return reg.test(v);
	},
	isDecimal: function(v) {
		if(v != null && typeof(v) != "undefined") {
			var str = v.toString();
			if(str != "") {
				if(str.length == 1 && str == "0") {
					return true;
				} else {
					var pattern = '^-?[1-9]\\d*$|^-?0\\.\\d*$|^-?[1-9]\\d*\\.\\d*$';
					var reg = new RegExp(pattern, 'g');
					return reg.test(str);
				}
			} else
				return false;
		}
		return false;
	}
}

//面包屑，传数组对象(href,name)，先data，后obj;
var BreadcrumbNavigation = {
	bnhtml: "",
	data: function(args) {
		var htmltxt = "";
		for(var i = 0; i < args.length; i++) {
			var curr = args[i];
			if(StringIsNull(curr.href) == false) {
				htmltxt += ("<span>" + curr.name + "</span>");
			} else {
				htmltxt += ("<a href='" + curr.href + "'>" + curr.name + "</a>");
			}
			if(args[i + 1] != undefined && args[i + 1] != null) {
				htmltxt += ("<span>&nbsp;&nbsp;&nbsp;&rsaquo;&nbsp;&nbsp;&nbsp;</span>");
			}
		}
		bnhtml = htmltxt;
	},
	show: function(obj) {
		obj.html(bnhtml);
	}
}

//基于JS原型数组的删除
Array.prototype.del = function(n) {　
	if(n < 0)
		return this;　
	else
		return this.slice(0, n).concat(this.slice(n + 1, this.length));
}

//字符串，是否为空
function StringIsNull(str) {
	if(str == undefined || str == null || str == "")
		return false;
	else
		return true;
}

//计算加，减，乘，除，取余
var countData={
	add:function(num1,num2){
		var r1,r2,m,n;
		try{r1=num1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=num2.toString().split(".")[1].length}catch(e){r2=0}
		m = Math.pow(10,Math.max(r1,r2));
		n = (r1>=r2)?r1:r2;
		return ((num1*m + num2*m)/m).toFixed(n);
	},
	sub:function (num1,num2){
		var r1,r2,m,n;
		try{r1=num1.toString().split(".")[1].length}catch(e){r1=0}
		try{r2=num2.toString().split(".")[1].length}catch(e){r2=0}
		n = (r1>=r2)?r1:r2;
		m = Math.pow(10,Math.max(r1,r2));
		return ((num1*m - num2*m)/m).toFixed(n);
	},
	mul:function(num1,num2){
		var m = 0;
		try{m+=num1.toString().split(".")[1].length}catch(e){}
		try{m+=num2.toString().split(".")[1].length}catch(e){}
		return (Number(num1.toString().replace(".",""))*Number(num2.toString().replace(".","")))/Math.pow(10,m)
	},
	div:function (arg1,arg2){
	   var t1=0,t2=0,t3=0,r1,r2,r3;
	   try{t1=arg1.toString().split(".")[1].length}catch(e){}
	   try{t2=arg2.toString().split(".")[1].length}catch(e){}
	   r1=Number(arg1.toString().replace(".",""));
	   r2=Number(arg2.toString().replace(".",""));
	   var arg3=(r1/r2).toString()
	   try{t3=arg3.toString().split(".")[1].length}catch(e){}
	   r3=Number(arg3.toString().replace(".",""));
	   return r3*Math.pow(10,t2-t1)*Math.pow(10,0-t3);
	},
	mod:function (arg1,arg2){
		var t1=0,t2=0,r1,r2;
		try{t1=arg1.toString().split(".")[1].length}catch(e){}
		try{t2=arg2.toString().split(".")[1].length}catch(e){}
		r1=Number(arg1.toString().replace(".",""));
		r2=Number(arg2.toString().replace(".",""));
		return (r1*Math.pow(10,t2-t1))%r2;
	}
}

//去前后空格
if (!String.prototype.trim) {
	String.prototype.trim = function () {
		return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
	};
}

//转数字千位符
function toThousands(num) {
	return (num || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
}

var CApp = {
	initBase: function(name, data) {
		var nodes = [
			["span", "div", "th", "td", "b", "h1", "h2", "h3", "h4", "h5"],
			["input", "textarea", "select"]
		];
		var appblock = $("[c-app='" + name + "']");
		var appmodels = appblock.find("[c-model]");
		appmodels.each(function(i, item) {
			var curr = $(this);
			var nodeName = $(this)[0].nodeName.toLowerCase();
			if(nodes[0].indexOf(nodeName) > -1) {
				curr.html(formartNullString(data[curr.attr("c-model")]));
			}
			if(nodes[1].indexOf(nodeName) > -1) {
				curr.val(data[curr.attr("c-model")]);
				curr.change(function(){
					data[curr.attr("c-model")]=curr.val();
				});
			}

		});
	},
	initTable: function(name, data) {
		var appblock = $("[c-app='" + name + "']");
		var apptr = appblock.find("tr:eq(1)");
		appblock.find("tr:gt(0)").remove();
		$.each(data, function(i, item) {
			var appclone = apptr.clone();

			var models = appclone.find("[c-model]");
			models.each(function() {
				var curr = $(this);
				if(curr.attr("c-model") == "indexnumber") {
					curr.html(i + 1);
				} else
					curr.html(item[curr.attr("c-model")]);
			});

			var appchildren = appclone.find("*");
			appchildren.each(function() {
				var currnode = $(this);
				for(var j = 0; j < currnode[0].attributes.length; j++) {
					for(var o in item) {
						eval("var re = /{{" + o.toString() + "}}/;");
						currnode[0].attributes[j].value = currnode[0].attributes[j].value.replace(re, item[o]);
					}
				}
			});

			appblock.append(appclone);
		});
	}
}

///校验时间，控制前后时间的设置
///StartDateClass 前一个时间的类名（如：开始时间）
///EndDateClass 后一个时间的类名（如：结束时间）
function checkDateDifference(StartDateClass, EndDateClass) {
	var startdate = $('.' + StartDateClass);
	var endtdate = $('.' + EndDateClass);
	//开始时间决定着结束时间不能早于开始时间
	startdate.datetimepicker().on('changeDate', function(ev) {
		endtdate.datetimepicker('setStartDate', ev.date.getFullYear() + '-' + (ev.date.getMonth() + 1) + '-' + (ev.date.getDate()-1));
	});

	//结束时间决定着开始时间不能晚于结束时间
	endtdate.datetimepicker().on('changeDate', function(ev) {
		startdate.datetimepicker('setEndDate', ev.date.getFullYear() + '-' + (ev.date.getMonth() + 1) + '-' + (ev.date.getDate()));
	});
}

function CreateMenuModule() {
	var template = $("div[c-template='menu']");
	$.get(uriapi + "/html/common/menu.html", function(result) {
		if(template.length > 0) {
			template.before(result);
			template.remove();
		}
		DrawPageBase();
	});
}

var baseLoadCount=0;
function DrawPageBase(){
	baseLoadCount++;
	if(baseLoadCount>=2){
		setDrawMenu();

		var userlogindate = new Date();
		var userlocalStorage = "menudata" + userlogindate.getFullYear() + "-" + userlogindate.getMonth() + "-" + userlogindate.getDate() + "-" + userlogindate.getHours();
		if(loginuserdata!=null)$(".disusername").html(loginuserdata.body.user_name);

		$(window).resize(function () {
			setScroll();
		});

		//时间
		var currdate = new Date();
		$(".currdate").html(currdate.getFullYear() + "年" + (currdate.getMonth() + 1) + "月" + currdate.getDate() + "日");

		//右上角的，个人信息菜单
		var objusermenu = null;
		$(".username").hover(function () {
			$(".menuuser").show();
		}, function () {
			objusermenu = setTimeout(function () { $(".menuuser").hide(); }, 300);
		});
		$(".menuuser").hover(function () {
			clearTimeout(objusermenu);
		}, function () {
			objusermenu = setTimeout(function () { $(".menuuser").hide(); }, 300);
		});

		$("#btn_a_clear").click(function () {
			$.cookie("X-Auth-Token", null, { path: '/' });
			localStorage.clear();
			if(location.href.indexOf("yitaigroup")>-1){
				showloading();
				var url="http://home.yitaigroup.com:80/AGLogout";
				if($("#frm").length==0)
					$("body").append("<iframe id='frm' height='0' width='0' src='"+url+"' style='display:none;' ></iframe>");
				$("#frm").load(function(){
					location.href=uriapi+"/login.html";
				});
			}else{
				setTimeout(function(){location.href=uriapi+"/login.html";},1000);
			}

		});

		$(".logo").click(function(){location.href=uriapi+"/index.html";});$(".logo").click(function(){location.href=uriapi+"/index.html";});

		var ishidenav=true;
		if($.cookie("navhide")!=null){
			ishidenav=$.cookie("navhide")=="1"?true:false;
		}
		if(ishidenav==false){
			$("body").addClass("hidenav");
		}else{
			$("body").removeClass("hidenav");
		}
		$(".nav").hover(function(){
			$(".nav .navbtn").show();
		},function(){
			$(".nav .navbtn").hide();
		});

		$(".nav .navbtn,.navshow .navbtn").click(function(){
			if($("body").hasClass("hidenav")){
				$("body").removeClass("hidenav");
				$.cookie("navhide","1");
			}
			else{
				$("body").addClass("hidenav");
				$.cookie("navhide","0");
			}
		});
	}
}

//设置滚动条
function setScroll() {
    var navscoll = $("#navscoll");
    if (navscoll.length == 0) return true;
    var hnavscoll = $("#hnavscoll");
    var hnavscoll_div = $("#hnavscoll div");
    var navscoll_div = $("#navscoll div");
    hnavscoll_div.height($("#navscollbox ul").height());
    if (hnavscoll_div.height() <= hnavscoll.height()) {
        $("#navscollbg,#hnavscoll").hide();
    } else {
        $("#navscollbg,#hnavscoll").show();

        var scollheight = hnavscoll.height() / hnavscoll_div.height() * hnavscoll.height();
        navscoll_div.height(scollheight);
        navscoll.css({ "height": "100%" });
        navscoll.height(navscoll.height() - scollheight);

        var aa = true, scrolla, scrollb;
        $("#navscollbox").scroll(function () {
            var scollvalue = $("#navscollbox ul").height() - $(this).height();
            navscoll_div.css({ "top": (($(this).scrollTop() / scollvalue * 100) + "%") });
        });
        hnavscoll.hover(function () {
            hnavscoll.scrollTop($("#navscollbox").scrollTop());
            hnavscoll.scroll(function () {
                $("#navscollbox").scrollTop($(this).scrollTop());
            });
        }, function () {
        });
    }
}

var compare = function (obj1, obj2) {
    var val1 = obj1.display_index;
    var val2 = obj2.display_index;
    if (val1 < val2) {
        return -1;
    } else if (val1 > val2) {
        return 1;
    } else {
        return 0;
    }
}

function setDrawMenu() {
    setFristMenu();
    setSecondMenu();

    var menukey = getCurrMenuItemID();
	if(menukey==-1&&$.cookie("lastmenuid")!=null){
		menukey=$.cookie("lastmenuid");
	}
    var ids = getMenuItemIDs(menukey);
    $.cookie("lastmenuid",menukey);
    if (ids.length == 0&&(location.href.indexOf("/web/index.html")==-1)) {
        $("a.frist[data-id='" + menukey + "']").parent().addClass("on");
    } else if (ids.length == 1) {
        if (menukey == ids[0]) {
            $("a.frist[data-id='" + ids[0] + "']").parent().addClass("on");
        }
        else {
            $("a.frist[data-id='" + ids[0] + "']").click();
            $("a.second").removeClass("on");
            $("a.second[data-id='" + menukey + "']").addClass("on");
        }

    } else if (ids.length == 2) {
        $("a.frist[data-id='" + ids[1] + "']").click();
        $("a.second[data-id='" + ids[0] + "']").click();
        $("a.second").removeClass("on");
        $("a.third[data-id='" + menukey + "']").addClass("on");
    } else {

    }
}

function setFristMenu() {
	var classnames=[{id:1,icon:"receipts"},{id:2,icon:"work"},{id:3,icon:"query"},{id:4,icon:"analysis"},{id:5,icon:"logs"},{id:6,icon:"system"}];
	var fstmenuata=[];
	$.each(loginuserdata.body.resources, function (i, item) {
		if(item.resources_url.indexOf("?key=")>-1){
			item.resources_url=item.resources_url.split("?key=")[0];
		}
		if (item.enabled == true && item.parent_id == 0) {
			fstmenuata.push(item)
		}
	});
	fstmenuata.sort(compare);
    $.each(fstmenuata, function (i, item) {
		//item.class="";
        var href = item.resources_url == "" ? "javascript:void(0)": (urlPrdfix + item.resources_url);
        var isHasChildren = hasChildren(item.resources_id);
		var classname="";
		$.each(classnames,function(j,jitem){
			if(item.resources_id==jitem.id)
				classname=" ico-"+jitem.icon;
		});
        $("ul.fisrt").append("<li class='frist " + (isHasChildren ? "ico ico-arrow" : "") + "'><a class='frist ico" + classname + "' data-id='" + item.resources_id + "' data-vid='" + item.menu_id + "' href='" + href + "'>" + item.resources_name + "</a></li>");
    });
    setScroll();
}

function setSecondMenu() {
    $("a.frist").click(function () {
        var curr = $(this);
        if (curr.siblings("ul.second").length > 0) {
            curr.siblings("ul.second").remove();
            curr.parent().removeClass("arrowtop");
        } else {
            $("ul.fisrt>li").removeClass("on");
            $("ul.second").remove();
            curr.parent().addClass("on arrowtop");
            var id = curr.attr("data-id");
            curr.after("<ul class='second'></ul>");
			var sndmenuata=[];
			$.each(loginuserdata.body.resources, function (i, item) {
				if (item.enabled == true && item.parent_id == id) {
					sndmenuata.push(item)
				}
			});
			sndmenuata.sort(compare);
            $.each(sndmenuata, function (i, item) {
                var href = item.resources_url == "" ? "javascript:void(0)" : (urlPrdfix + item.resources_url);
                var isHasChildren = hasChildren(item.resources_id);
                $("ul.second").append("<li><a class='second " + (isHasChildren ? "ico ico-arrow" : "") + "' data-id='" + item.resources_id + "' data-vid='" + item.menu_id + "' href='" + href + "'>" + item.resources_name + "</a></li>");
            });
            setScroll();
            setThirdMenu();
        }
    });
}

function setThirdMenu() {
    $("a.second").click(function () {
        var curr = $(this);
        if (curr.siblings("ul.third").length > 0) {
            curr.siblings("ul.third").remove();
            curr.removeClass("arrowtop");
        } else {
            $("a.second").removeClass("on");
            $("ul.third").remove();
            curr.addClass("on arrowtop");
            var id = curr.attr("data-id");
            curr.after("<ul class='third'></ul>");
			var trdmenuata=[];
			$.each(loginuserdata.body.resources, function (i, item) {
				if (item.enabled == true && item.parent_id == id) {
					trdmenuata.push(item)
				}
			});
			trdmenuata.sort(compare);
            $.each(trdmenuata, function (i, item) {
                var href = item.resources_url == "" ? "javascript:void(0)" : (urlPrdfix + item.resources_url);
                $("ul.third").append("<li><a class='third' data-id='" + item.resources_id + "' data-vid='" + item.menu_id + "' href='" + href + "'>" + item.resources_name + "</a></li>");
            });
            setScroll();
            $("a.third").click(function () {
                $("a.second").removeClass("on");
                var curr = $(this);
                curr.addClass("on");
            });
        }
    });
}

//菜单，是否有子元素
function hasChildren(id) {
    var isHas = false;
    $.each(loginuserdata.body.resources, function (i, item) {
        if (item.parent_id == id) {
            isHas = true;
            return false;
        }
    });
    return isHas;
}

//菜单，根据子节点，查询
function getCurrMenuItemID(){
	var newid=-1;
	$.each(loginuserdata.body.resources, function (i, item) {
        if (item.resources_url!=""&&location.href.indexOf(item.resources_url)>-1) {
			newid=item.resources_id;
        }
    });
	return newid;
}
function getMenuItemIDs(id) {
    var items = new Array();
    $.each(loginuserdata.body.resources, function (i, item) {
        if (item.resources_id == id) {

            if (item.parent_id == 0) {
                return false;
            }
            items.push(item.parent_id);
            id = item.parent_id;
        }

    });
    $.each(loginuserdata.body.resources, function (i, item) {
        if (item.resources_id == id) {

            if (item.parent_id == 0) {
                return false;
            }
            items.push(item.parent_id);
        }

    });
    return items;
}

//扫描枪
function scan(){
	var keys=[];
	$(this).keypress(function(e){
		keys.push({"key":e.key,"time": e.timeStamp});

		var keysinput=""
		for(var i=0;i<keys.length-1;i++){
			keysinput+=keys[i].key;
			if(keysinput.indexOf("http:")!=-1){
				$("#scan").focus()
				break
			}
		}

		if(e.which==13){
			var strkeys="",time=keys[0].time
			for(var i=0;i<keys.length-1;i++){
				if(keys[i].time-time<1000){
					strkeys+=keys[i].key;
				}else{
					time=keys[i].time
					strkeys=keys[i].key
				}
			}
			if(strkeys.length>11){
				if(strkeys.indexOf("SN=")!=-1){
					strkeys=strkeys.slice(strkeys.indexOf("SN=")+3)
				}
				if(strkeys.indexOf("INS-11")!=-1){
					strkeys=strkeys.slice(strkeys.indexOf("INS-11")+6)
				}
				$("#scan").val(strkeys);
			}
			$(".search").click()
			keys=[];
		}
	})
}

//离开页面提示
function closeTip(){
	window.onbeforeunload = function(event) {
		return ""
	};
}

//取消离开页面提示
function closeTipCancel(){
	window.onbeforeunload = null;
}
///Table中的删除
///table 操作的Table
function tabledelete(table) {
	var btndelete = table.find(".btn_table_delete");
	btndelete.unbind("click");
	btndelete.click(function(e) {
		var id = $(this).attr("data-id");
		var interfaces = $(this).attr("data-interfaces");
		var text = "确定" + $(this).text() + "吗？";
		layer.confirm(text, {
			btn: ['确定', '取消'],
			icon: 3
		}, function() {
			//每个有删除页面，都要单独有function_delete(id)函数
			function_delete(id, interfaces);
			layer.close(parseInt($(".layui-layer-shade").attr("times")));
		}, function() {

		});
		e.stopPropagation();
	});
}

///Table中的编辑或查看
///table 操作的Table
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

function tableLiveEdit(table) {
	$(table).on("click", ".btn_table_edit", function(e) {
		var title = $(this).attr("data-title");
		var href = $(this).attr("my-href");
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

function bindDatePicker(panel) {
	var txtdate = null;

	if(panel == undefined) {
		txtdate = $('.txtdatepicker');
	} else {
		txtdate = panel.find('.txtdatepicker');
	}

	txtdate.each(function() {
		var pos = $(this).attr("data-pickerPosition") == undefined ? "" : $(this).attr("data-pickerPosition");
		$(this).datetimepicker({
			language: 'zh-CN',
			minView: 2,
			weekStart: 1,
			todayBtn: 1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			pickerPosition: pos,
		});
	});
}

///格式化三位小数
///x 数字
function formartDecimal_f(x) {
	if(x == null) x = 0;
	var f_x = parseFloat(x);
	if(isNaN(f_x)) {
		//alert('function:changeTwoDecimal->parameter error');
		return false;
	}
	f_x = Math.round(f_x * 1000) / 1000;
	var s_x = f_x.toString();
	var pos_decimal = s_x.indexOf('.');
	if(pos_decimal < 0) {
		pos_decimal = s_x.length;
		s_x += '.';
	}
	while(s_x.length <= pos_decimal + 3) {
		s_x += '0';
	}
	return s_x;
}

///格式化金额
///金额
function outputmoney(number) {
	number = number.replace(/\,/g, "");
	if(isNaN(number) || number == "") return "";
	number = Math.round(number * 100) / 100;
	if(number < 0)
		return '-' + outputdollars(Math.floor(Math.abs(number) - 0) + '') + outputcents(Math.abs(number) - 0);
	else
		return outputdollars(Math.floor(number - 0) + '') + outputcents(number - 0);
}

function outputdollars(number) {
	if(number.length <= 3)
		return(number == '' ? '0' : number);
	else {
		var mod = number.length % 3;
		var output = (mod == 0 ? '' : (number.substring(0, mod)));
		for(i = 0; i < Math.floor(number.length / 3); i++) {
			if((mod == 0) && (i == 0))
				output += number.substring(mod + 3 * i, mod + 3 * i + 3);
			else
				output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
		}
		return(output);
	}
}

function outputcents(amount) {
	amount = Math.round(((amount) - Math.floor(amount)) * 100);
	return(amount < 10 ? '.0' + amount : '.' + amount);
}
///格式化金额  end

///空字符串处理
///字符串
function formartNullString(str) {
	return(str == null || str == "") ? "--" : str;
}

function showloading() {
	$("input").blur()
	indexloading = layer.load(3, {
		shade: [0.4, '#000']
	});
}

///格式化文件名
function formartFileName(name) {
	var name1 = name.split(".")[0];
	var name2 = name.split(".")[1];
	if(name1.length > 18) {
		name = name1.substr(0, 5) + "..." + name1.substr(name1.length - 7) + "." + name2;
	}
	return name;
}

///格式化文件大小
function formartFileSize(size) {
	size = size / 1024 * 10;
	size = Math.floor(size);
	size = size / 10;
	return size;
}

///物料编码
function formartCodeNo(str) {
	if(str.length > 10) {
		str = str.substr(0, 10) + "<br/>" + str.substr(10, str.length - 10);
	}
	return str;
}
var lastshowsum = {
	"classname": "",
	"ojblayerid": -1,
	"objClearid": -1
};

function tableshowsum() {
	$(".showsum1,.showsum2,.showsum3,.showsum4").unbind("hover");
	$(".showsum1,.showsum2,.showsum3,.showsum4").hover(function() {
		var curr = $(this);
		var showsum = "";
		if(curr.hasClass("showsum1")) {
			showsum = ".showsum1";
		} else if(curr.hasClass("showsum2")) {
			showsum = ".showsum2";
		} else if(curr.hasClass("showsum3")) {
			showsum = ".showsum3";
		} else if(curr.hasClass("showsum4")) {
			showsum = ".showsum4";
		}
		$(showsum).addClass("sum");
		if(showsum == lastshowsum.classname) {
			clearTimeout(lastshowsum.objClearid);
		} else {
			var sum = 0.0;
			$(showsum).each(function() {
				var i = $(this).attr("data-val");
				if(i != undefined)
					sum += parseFloat(i);
			});
			lastshowsum.ojblayerid = layersMoretips(($(showsum).eq(0).html() + "：（总）" + formartDecimal_f(sum)), $(showsum).eq(0));
		}
		lastshowsum.classname = showsum;
	}, function() {
		$(".showsum1,.showsum2,.showsum3,.showsum4").removeClass("sum");
		var ojblayerid = lastshowsum.ojblayerid;
		lastshowsum.objClearid = setTimeout(function() {
			layer.close(ojblayerid);
			lastshowsum.classname = "";
		}, 300);
	});
}

///取URL的值
///值名
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

///那些判断是否是数字的都用这个做
///把Input传进来
function CheckisDecimal(currinput, num) {
	if(FormDataCheck.isDecimal(currinput.val()) == false) {
		layer.tips("格式为数字", currinput, {
			tips: [2, '#f7b824'],
			tipsMore: true,
			time: 5000
		});
		return false;
	} else {
		var stc;
		if(num == undefined) {
			num = 3;
			stc = "小数位不能大于3位";
		} else if(num == 0) {
			stc = "只能为整数,不可以是小数";
		} else {
			stc = "小数位不能大于 " + num + " 位";
		};
		if(currinput.val().indexOf(".") > -1 && currinput.val().split(".")[1].length > num) {
			layer.tips(stc, currinput, {
				tips: [2, '#f7b824'],
				tipsMore: true,
				time: 5000
			});
			return false;
		} else {
			return true;
		}

	}
}

///对比数量，订单数量不能小于（已收货数量+到货数量）
///indexitem index
///arraylist 数组
///currinput 输入的到货数量
function contrastQuantity(indexitem, arraylist, currinput, tips) {
	tips = tips == undefined ? "订单数量不能小于（已收货数量+到货数量）" : tips;
	var ordersum = arraylist[indexitem].zddsl * 1000;
	var Receiving = arraylist[indexitem].zrksl * 1000;
	var AOGsum = currinput.val() * 1000;
	if(ordersum < (Receiving + AOGsum)) {
		layersMoretips(tips, currinput);
		return -1;
	} else {
		return AOGsum / 1000;
	}
}

function dataBindFile(statevalue, arraylist) {
	$(".filebox").empty();
	$.each(arraylist.file, function(i, item) {
		var staticname = "";
		if(item.static == 1) staticname = "上传完成";
		var strtext = "";
		strtext += ("<div class='file ico ico-file col-lg-4 col-xs-6'>");
		strtext += ("<div class='fileer'>" + item.by + "</div>");
		strtext += ("<div class='filename'><span title='" + item.filename + "'>" + formartFileName(item.filename) + "</span>  (" + item.filesize + "kb)</div>");
		strtext += ("<div class='clearfx'></div>");
		if(statevalue == 2)
			strtext += ("<div class='fileer'><a href='javascript:void(0)' data-id='" + item.id + "'>删除</a></div>");
		else
			strtext += ("<div class='fileer'></div>");
		strtext += ("<div class='filename'>" + staticname + "</div>");
		strtext += ("</div>");
		$(".filebox").append(strtext);
	});
	deletefile();
}

///直接调用关闭按键
function iframe_close_window() {
	$(".btn_iframe_close_window").click();
}

function setspandmap(value) {
	$(".mapadress span:last").html(value);
}

///绘制翻页
///翻页的数据对象
///当前面的索引0开始
///每页多少条
function drawpager(obj, pageindex, count) {
	try {
		if(obj.total > count) {
			$(".btnlinum").remove();
			$(".btnlistart,.btnliend").removeClass("disabled");
			$(".pagerbox").hide().data("index", pageindex); //show
			var j = Math.ceil(obj.total / count);
			for(i = 0; i < j; i++) {
				if(i == pageindex)
					$(".btnliend").before("<li class='btnlinum active'><a href='javascript:void(0);'>" + (i + 1) + "</a></li>");
				else
					$(".btnliend").before("<li class='btnlinum'><a href='javascript:actionpager(" + i + ");'>" + (i + 1) + "</a></li>");
			}
			if(pageindex == 0) $(".btnlistart").addClass("disabled");
			if(pageindex == (j - 1)) $(".btnliend").addClass("disabled");

			$(".btnlistart:not([class*='disabled']) a").click(function() {
				pageindex = $(".pagerbox").data("index");
				if(pageindex == 0) return;
				actionpager(pageindex - 1);
			});
			$(".btnliend:not([class*='disabled']) a").click(function() {
				pageindex = $(".pagerbox").data("index");
				if(pageindex == (j - 1)) return;
				actionpager(pageindex + 1);
			});
		}
	} catch(e) {}

}

///给Table空时，加个显示
///table 要操作的Table
function dataIsNull(table) {
	if(table.find("tr").length == 1) table.append("<tr class='datanull'><td class='datanull' colspan='" + (table.find("th").length) + "'>没有可显示的数据</td></tr>");
}

///ajax请求
///URL
///TYPE，get,post,put,delete
///data 数据
///successcallback 成功回调数据
///errorcallback 失败回调数据
///isalert，其它值或不赋值时，显示msg，不赋值错误的时候Alert提示，赋值true时对错都提示。所以想要提示这个加个ture就行了
function ajax(url, type, data, successcallback, errorcallback,isalert) {
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
			var isError = false;
			if(data.head != undefined) {
				//isError = true;
//				if(isalert==undefined){//其它值或不赋值时，显示msg，
//					var nomsg=["success","Success","SUCCESS","成功"]
//					if(data.head.msg!=""&&nomsg.indexOf(data.head.msg)==-1)
//						layer.msg(data.head.msg);
//				}
				
				if(isalert==undefined){//不赋值错误的时候Alert提示
					if(!data.head.status){
						isError=true;
						layer.alert(data.head.msg, {
							icon: 5,
							title: "操作失败",
							end: function(index, layero) {
								layer.close(index);
								errorcallback(data);
							}
						});
					}
				}
				
				if(isalert===true){//赋值true时对错都提示。所以想要提示这个加个ture就行了	
					isError=true;
					if(data.head.status){
						layer.alert(data.head.msg, {
							title: "信息",
							end: function(index, layero) {
								layer.close(index);
								successcallback(data);
							}
						});
					}else{
						layer.alert(data.head.msg, {
							icon: 5,
							title: "操作失败",
							end: function(index, layero) {
								layer.close(index);
								errorcallback(data);
							}
						});
					}
				}
			}
			if(isError == false)
				successcallback(data);
		},
		error: function(xhr, textStatus, errorThrown) {
			console.log("error:" + xhr.status + "__" + textStatus);
			if(xhr.status == 401) {
				layer.alert("请重新登录", {
					icon: 5,
					title: "操作失败",
					end: function(index, layero) {
						location.href = uriapi + "/login.html?url=" + escape(location.href);
						layer.close(index);
					}
				});
			}
			if(xhr.status == 403) {
				if(location.href.indexOf("index.html") == -1)
					location.href = uriapi + "/index.html";
			} else if(xhr.status == 500) {
				layer.alert("数据错误", {
					icon: 5,
					title: "操作失败"
				});
			} else if(xhr.status == 404) {
				layer.alert("404", {
					icon: 5,
					title: "操作失败"
				});
			} else {
				layer.alert("数据错误，请尝试重试登录", {
					icon: 5,
					title: "操作失败"
				});
			}
			errorcallback(textStatus)
		}
	});
}
///多个Tips直接调用
///tips
///el element
function layersMoretips(tips, el) {
	var index = layer.tips(tips, el, {
		tips: [2, '#f7b824'],
		tipsMore: true,
		time: 5000
	});
	return index;
}

///创建Select(列表用).selZCLFS
///data 数据
///selectedvalue 如果需要选中给值，不需要给-1
///index 没有给null,index是组件数据时为了使用方便
function createselect(data, selectedvalue, index) {
	var options = "";
	if(data == null) {
		return "<select class='form-control selZCLFS' data-index='" + index + "'><option value=''></option></select>";
	}
	$.each(data.body, function(i, item) {
		if(selectedvalue == item.key)
			options += "<option selected value='" + item.key + "'>" + item.value + "</option>";
		else
			options += "<option value='" + item.key + "'>" + item.value + "</option>";
	});
	if(index == null)
		return "<select class='form-control selZCLFS'>" + options + "</select>";
	else
		return "<select class='form-control selZCLFS' data-index='" + index + "'>" + options + "</select>";
}

function GetCurrData() {
	var ndate = new Date;
	var m = (ndate.getMonth() + 1);
	var d = ndate.getDate();
	m = (m.toString().length == 1) ? ("0" + m.toString()) : m;
	d = (d.toString().length == 1) ? ("0" + d.toString()) : d;
	return ndate.getFullYear() + "-" + m + "-" + d;
}

function formartTimestamp(t) {
	if(t == undefined || t == null || t == "") return "--";
	var d = new Date();
	d.setTime(t);
	return d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
}

///获取文件
function GetFilesByReceipt(receipt_id, receipt_type, IsDelete) {
	var url = uriapi + "/biz/file/attachmentlist";

	var obj = {
		"receipt_id": receipt_id,
		"receipt_type": receipt_type
	};
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
		$("[data-file-type='" + receipt_type + "']").remove();
		$.each(data.body, function(i, item) {
			var strtext = "";
			strtext += ("<div class='file ico ico-file col-lg-4 col-xs-6' data-file='" + item.attachment_id + "' data-file-type='" + receipt_type + "'>");
			strtext += ("<div class='fileer'>" + item.user_name + "</div>");
			strtext += ("<div class='filename'><a href='javascript:void(0)' class='adownload' data-id='" + item.attachment_id + "' title='点击下载：" + item.file_name + "'>" + formartFileName(item.file_name) + "</a>  (" + formartFileSize(item.size) + "kb)</div>");
			strtext += ("<div class='clearfx'></div>");
			if(IsDelete == true) {
				if(loginuserdata != null && loginuserdata.body.user_id == item.user_id)
					strtext += ("<div class='fileer'><a href='javascript:void(0)' class='btn_table_delete' data-id='file," + item.attachment_id + "'>删除</a></div>");
				else
					strtext += ("<div class='fileer'></div>");
			} else
				strtext += ("<div class='fileer'></div>");

			strtext += ("<div class='filename'>" + "" + "</div>"); //上传完成
			strtext += ("</div>");
			$(".filebox").prepend(strtext);
		});

		tabledelete($(".filebox"));
		$(".adownload").click(function() {
			var filehtml = uriapi + "/html/common/filedownload.html?id=" + $(this).attr("data-id") + "&actionurl=" + loginuserdata.body.fileServer;
			window.open(filehtml, "name1", "width=500,height=200,toolbar=no,scrollbars=no,menubar=no,location=no,resizable=no,screenX=400,screenY=300");
		});

	}, function(e) {

		layer.msg("数据请求失败");
	});
}
//文件描绘
function GetFilesByReceiptId(IsDelete, files) {
	$(".filebox").empty();
	$.each(files, function(i, item) {
		var strtext = "";
		strtext += ("<div class='file ico ico-file col-lg-4 col-xs-6' data-file='" + item.uuid + "'>");
		strtext += ("<div class='fileer'>" + item.user_name + "</div>");
		strtext += ("<div class='filename'><a href='javascript:void(0)' class='adownload' data-id='" + item.uuid + "' title='点击下载：" + item.name + "'>" + formartFileName(item.name) + "</a>  (" + formartFileSize(item.size) + "kb)</div>");
		strtext += ("<div class='clearfx'></div>");
		if(IsDelete == true) {
			if(loginuserdata != null && loginuserdata.body.user_id == item.user_id)
				strtext += ("<div class='fileer'><a href='javascript:void(0)' class='btn_table_delete' data-id='file,"+item.uuid+',' + item.interfaces + "'>删除</a></div>");
			else
				strtext += ("<div class='fileer'></div>");
		} else
			strtext += ("<div class='fileer'></div>");

		strtext += ("<div class='filename'>" + "" + "</div>"); //上传完成
		strtext += ("</div>");
		$(".filebox").prepend(strtext);
	});

	tabledelete($(".filebox"));
	$(".adownload").click(function() {
		var filehtml = uriapi + "/html/common/filedownload.html?id=" + $(this).attr("data-id") + "&actionurl=" + loginuserdata.body.fileServer;
		window.open(filehtml, "name1", "width=500,height=200,toolbar=no,scrollbars=no,menubar=no,location=no,resizable=no,screenX=400,screenY=300");
	});
}

function DeleteFileByID(fileid) {
	var url = uriapi + "/biz/file/removeattachment";
	showloading();
	var obj = {
		"attachment_id": fileid
	};
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		if(data == false) {
			layer.msg("删除失败。");
		} else {
			$("[data-file='" + fileid + "']").remove();
		}
	}, function(e) {
		layer.close(indexloading);
		layer.msg("数据请求失败");
	});
}
//走接口删除
function DeleteFilext(fileid, receiptId, receiptType) {
	var url = uriapi + "/biz/jjcrk/removeFile";
	showloading();
	var obj = {
		"uuid": fileid,
		"receiptId": receiptId,
		"receiptType": receiptType
	};
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		if(data.body.code == 1) {
			layer.msg(data.body.msg);
			$("[data-file='" + fileid + "']").remove();
		} else {
			layer.msg("删除失败");
		}
	}, function(e) {
		layer.close(indexloading);
		layer.msg("数据请求失败");
	});
}

//添加文件
function pushfile(obj,receiptId,receiptType,files,isdelete){
	if(obj!=undefined){
		obj.receiptId=receiptId;
		obj.receiptType=receiptType;
		files.push(obj);
	}
	drawfiles(files,isdelete);
}

// 画文件列表
function drawfiles(files,isdelete){
	$(".filebox").empty();
	$.each(files, function(i, item) {
		item.can_delete = true;
		if(item.user_id == undefined) {
			item.user_id = loginuserdata.body.user_id;
			item.user_name = loginuserdata.body.user_name;
		}
		if(item.interfaces == undefined) {
			item.interfaces = false;
		}
		if(isdelete==false || item.user_id!=loginuserdata.body.user_id){
			item.can_delete = false;
		}

		var strtext = "";
		strtext += ("<div class='file ico ico-file col-lg-4 col-xs-6' data-file='" + item.file_id + "'>");
		strtext += ("<div class='fileer'>" + item.user_name + "</div>");
		strtext += ("<div class='filename'><a href='"+(location.origin+":8100/wms/file/file/download?file_id=" + item.file_id)+"' target='_blank' data-id='" + item.file_id + "' title='点击下载：" + item.file_id + "'>" + formartFileName(item.file_name) + "</a>  (" + formartFileSize(item.file_size) + "kb)</div>");
		strtext += ("<div class='clearfx'></div>");
		if(item.can_delete == true) {
			strtext += ("<div class='fileer'><a href='javascript:void(0)' class='btn_delete_file' data-id='"+item.file_id+',' + item.interfaces + "'>删除</a></div>");
		} else
			strtext += ("<div class='fileer'></div>");

		strtext += ("<div class='filename'>" + "" + "</div>"); //上传完成
		strtext += ("</div>");
		$(".filebox").prepend(strtext);
	});
	var btndelete=$(".btn_delete_file");
	btndelete.unbind("click");
	btndelete.click(function (e) {
		var id = $(this).attr("data-id");
		var text = "确定删除吗？";
		layer.confirm(text, {
			btn: ['确定', '取消'],
			icon: 3
		}, function () {
			var fileid = id.split(',')[0];
			var interfaces=id.split(',')[1];
			if(interfaces == "true") {
				DeleteFilext(fileid, GetQueryString("no"), indexdata.body.mst.zdjlx);
			} else {
				DeleteFile(fileid);
			}
			layer.close(parseInt($(".layui-layer-shade").attr("times")));
		}, function () {

		});
		e.stopPropagation();
	});
}

//删除文件
function DeleteFile(fileid) {
	$.each(files, function(i, item) {
		if(fileid == item.file_id) {
			files.splice(i, 1);
			return false;
		}
	})
	drawfiles(files);
}

//原来用Key做关键字存信息的，现在用这个
function GetURLKey(){
	return location.href.split('.html')[0].split("/web/")[1].replace(/\//gm,'');
}

//cookie
(function(factory) {
	if(typeof define === "function" && define.amd) {
		define(["jquery"], factory)
	} else {
		if(typeof exports === "object") {
			factory(require("jquery"))
		} else {
			factory(jQuery)
		}
	}
}(function($) {
	var pluses = /\+/g;

	function encode(s) {
		return config.raw ? s : encodeURIComponent(s)
	}

	function decode(s) {
		return config.raw ? s : decodeURIComponent(s)
	}

	function stringifyCookieValue(value) {
		return encode(config.json ? JSON.stringify(value) : String(value))
	}

	function parseCookieValue(s) {
		if(s.indexOf('"') === 0) {
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, "\\")
		}
		try {
			s = decodeURIComponent(s.replace(pluses, " "));
			return config.json ? JSON.parse(s) : s
		} catch(e) {}
	}

	function read(s, converter) {
		var value = config.raw ? s : parseCookieValue(s);
		return $.isFunction(converter) ? converter(value) : value
	}
	var config = $.cookie = function(key, value, options) {
		if(value !== undefined && !$.isFunction(value)) {
			options = $.extend({}, config.defaults, options);
			if(typeof options.expires === "number") {
				var days = options.expires,
					t = options.expires = new Date();
				t.setTime(+t + days * 86400000)
			}
			return(document.cookie = [encode(key), "=", stringifyCookieValue(value), options.expires ? "; expires=" + options.expires.toUTCString() : "", options.path ? "; path=" + options.path : "", options.domain ? "; domain=" + options.domain : "", options.secure ? "; secure" : ""].join(""))
		}
		var result = key ? undefined : {};
		var cookies = document.cookie ? document.cookie.split("; ") : [];
		for(var i = 0, l = cookies.length; i < l; i++) {
			var parts = cookies[i].split("=");
			var name = decode(parts.shift());
			var cookie = parts.join("=");
			if(key && key === name) {
				result = read(cookie, value);
				break
			}
			if(!key && (cookie = read(cookie)) !== undefined) {
				result[name] = cookie
			}
		}
		return result
	};
	config.defaults = {};
	$.removeCookie = function(key, options) {
		if($.cookie(key) === undefined) {
			return false
		}
		$.cookie(key, "", $.extend({}, options, {
			expires: -1
		}));
		return !$.cookie(key)
	}
}));