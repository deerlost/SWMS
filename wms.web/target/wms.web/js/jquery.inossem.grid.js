/*
基于JQuery-1.11.0.js，Layer.js，bootstrap为Inossem公司制作的插件
将不设版本号
表格用对象实例化，不再用循环自已拼HTML
对公司的业务集中打包控制，以后的业务尽量控制在功能内
*/
(function ($) {
	$.fn.extend({
		"iGrid": function (options) {
			var opts = $.extend({}, defaluts, options);
			this.opts=opts;
			
			//对键盘按键的处理
			this.lockeypress();
			
			//设置一下KeyField
			//this.setKeyField();
			
			this.css({"clear":"both"});
			//当设置了按百分百适应的宽度的时候。clear: both;
			if(opts.absolutelyWidth){
				this.css({"width":"100%","overflow":"auto","transform":"translate(0,0)"});
			}

			//初始化框架和 列头
			this.initframe();
			
			//初始化数据
			this.initgrid();

			return this;
		},
		//初始化框架和 列头
		"initframe":function(){
			var currdiv=this;
			var opts=this.opts;
			
			if(opts.resizehead||this.data("hasheader")==null||this.data("hasheader")==false){
				this.data("divtdwidth",null);
				this.empty();
				var controlID= Math.floor(Math.random()*999999)+100000;
				opts.controlID=controlID;
				
				//建立外部框架
				var arrayTable=[];
				if(opts.fixedcolumn==true&&opts.data!=null){
					arrayTable.push("<table class='wapper wapper"+opts.controlID+"' style='min-width:auto;max-width:100%;width:100%;'><tr>");
					arrayTable.push("<td class='wapperleft'><div class='wapperleft'><div class='head'></div></div></td>");
					arrayTable.push("<td class='wapperright'><div class='wapperright'><div class='head'></div></div></td>");
					arrayTable.push("</tr></table>");
				}else
					arrayTable.push("<table class='wapper wapper"+opts.controlID+"'><tr><td class='wapperright'><div class='wapperright'><div class='head'></div></div></td></tr></table>");
				currdiv.append(arrayTable.join(''));
				
				if(opts.fixedcolumn==true&&opts.data!=null){
					//取出第一次加载的宽度
					opts.firstwidth=$(".wapper"+opts.controlID+"").width();
				}
				
				currdiv.data("controlID",controlID);
				var arrayThLeft=[],arrayThRight=[];
				arrayThLeft.push("<table class='"+opts.skin+" tablebodyhead"+opts.controlID+"'><tr>")
				arrayThRight.push("<table class='"+opts.skin+" tablebodyhead"+opts.controlID+"'><tr>")
				
				var arrayMenu=[];
				arrayMenu.push("<div id='tablemenu' class='tablemenu"+opts.controlID+"'>");
				
				if(opts.star){
					if(opts.fixedcolumn==true&&opts.data!=null)
						arrayThLeft.push("<th style='width:1px' class='border'><div class='star'></div></th>");
					else
						arrayThRight.push("<th style='width:1px' class='border'><div class='star'></div></th>");
				}
				
				if(opts.rownumbers){
					if(opts.fixedcolumn==true&&opts.data!=null)
						arrayThLeft.push("<th style='width:1px' class='border'><div class='num'></div></th>");
					else
						arrayThRight.push("<th style='width:1px' class='border'><div class='num'></div></th>");
				}
					
				if(opts.checkbox){
					var displaynone="";
					if(opts.isHideCheckbox)displaynone="display:none;"
					if(opts.fixedcolumn==true&&opts.data!=null)
						arrayThLeft.push("<th style='"+displaynone+"width:1px' class='border'><div class='chk'><input type='checkbox' /></div></th>");
					else
						arrayThRight.push("<th style='"+displaynone+"width:1px' class='border'><div class='chk'><input type='checkbox' /></div></th>");
				}
				
				if(opts.radio){
					var displaynone="";
					if(opts.isHideRadio)displaynone="display:none;"
					if(opts.fixedcolumn==true&&opts.data!=null)
						arrayThLeft.push("<th style='"+displaynone+"width:1px' class='border'><div class='rdo'></div></th>");
					else
						arrayThRight.push("<th style='"+displaynone+"width:1px' class='border'><div class='rdo'></div></th>");
				}
					
				$.each(opts.columns,function(i,item){
					var createTempTh="";
					var tablestyle="style='";
					
					if(item.width!=undefined)
						tablestyle+=("width:"+item.width+"px;");
					/*if(item.align!=undefined)
						tablestyle+=("text-align:"+item.align+";");*/
					if(item.min!=undefined)
						tablestyle+=("min-width:"+item.min+"px;");
					tablestyle+=("'");
					
					var strissorthtml="";
					if(opts.sortable&&(item.sort==undefined||item.sort==true))
						strissorthtml="<a href='javascript:void(0)' data-value='"+i+"'>"+item.th+"</a>";
					else
						strissorthtml=item.th;
					//根据不同的 类型设置数据。
					if(item.type==undefined){
						var tableresizeandtext="";
						tableresizeandtext=strissorthtml+"<div class='resize'></div>";
						createTempTh+=("<th colnum='"+i+"'><div col='"+i+"' class='col' "+tablestyle+">"+tableresizeandtext+"</div></th>");
					}else{
						var tableresizeandtext=""+item.th+"<div class='resize'></div>";
						var tableresizeandlink=strissorthtml+"<div class='resize'></div>";
						if(item.type=="select"){
							createTempTh=("<th colnum='"+i+"'><div col='"+i+"' class='col colselect' "+tablestyle+">"+tableresizeandtext+"</div></th>");
						}else if(item.type=="number"){//数量可以排序
							createTempTh=("<th colnum='"+i+"' class='showsum'><div col='"+i+"' class='col colnumber' "+tablestyle+">"+tableresizeandlink+"</div></th>");
						}else if(item.type=="link"){//link可以排序
							createTempTh=("<th colnum='"+i+"'><div col='"+i+"' class='col collink' "+tablestyle+">"+tableresizeandlink+"</div></th>");
						}else if(item.type=="light"){//light可以排序
							createTempTh=("<th colnum='"+i+"'><div col='"+i+"' class='col collight' "+tablestyle+">"+tableresizeandlink+"</div></th>");
						}else if(item.type=="popup"){
							createTempTh=("<th colnum='"+i+"'><div col='"+i+"' class='col colpopup' "+tablestyle+">"+tableresizeandlink+"</div></th>");
						}else if(item.type=="tr"){
							//tr类型，是不显示列头的。也不会在 数据上有体现。
						}else{
							createTempTh=("<th colnum='"+i+"'><div col='"+i+"' class='col' "+tablestyle+">"+tableresizeandtext+"</div></th>");
						}
					}
					if(item.fixed!=undefined&&item.fixed==true&&opts.fixedcolumn==true&&opts.data!=null)
						arrayThLeft.push(createTempTh);
					else
						arrayThRight.push(createTempTh);
					if(item.type!=undefined&&item.type=="tr"||item.th==""||item.th=="&nbsp;") return true;
					arrayMenu.push("<div class='tablemenu' colnum='"+i+"'><label><input type='checkbox' value='"+i+"' checked />&nbsp;"+item.th+"</label></div>")
				});
				if(opts.percent!=undefined){
					arrayThRight.push("<th style='width:"+opts.percent+"%;'>&nbsp;</th>");
				}
				arrayThLeft.push("</tr></table>");
				arrayThRight.push("</tr></table>");
				arrayMenu.push("</div>");

				if(opts.fixedcolumn){
					currdiv.find("div.wapperleft div.head").append(arrayThLeft.join(''));
				}
				currdiv.find("div.wapperright div.head").append(arrayThRight.join(''));
				currdiv.append(arrayMenu.join(''));
				currdiv.data("hasheader",true)
				
				//点击  列头（排序功能）
				currdiv.sort();
				
			}else{
				if(currdiv.data("controlID")!=undefined&&currdiv.data("controlID")!=null)
					opts.controlID=currdiv.data("controlID");
			}
			
			//改变表格的宽度（拖拽功能）
			this.changewidth()
			
			//右键选择显示列（设置 显示或隐藏列的功能）
			this.showmenu()
		},
		//改变表格的宽度（拖拽功能）
		"changewidth":function(){
			var currdiv=this;
			var opts=this.opts;
			currdiv.startx=0;
			var divpindex=-1,controlID="",currthwidth=0;
			$(".tablebodyhead"+opts.controlID+" div.resize").mousedown(function(e){
				var currth=$(this).parent().parent();
				currthwidth=currth.width();
				controlID=opts.controlID;
				divpindex=$(".tablebodyhead"+controlID+" div.resize").index($(this));
				$(".resizewidth,.resizeform").remove();
				$("body").append("<div class='resizewidth'></div><div class='resizeform'></div>")
				$(".resizewidth").css({"top":(currdiv.position().top+"px"),"left":(e.pageX+"px")}).height(currdiv.height());
				$(".resizeform").css({"top":(currth.position().top+"px"),"left":(currth.position().left+"px")}).height(currdiv.height()).width(currthwidth);
				currdiv.startx=e.clientX;
				
				$("body").addClass("noselect");
			});
			$(document).mousemove(function(e){
				if($(".resizewidth").length>0){
					$(".resizewidth").css({"left":(e.pageX+"px")});
					if(currdiv.startx>0){
						var currmovewidth=e.pageX-currdiv.startx;
						var newwidth=currthwidth+currmovewidth;
						$(".resizeform").width(newwidth<80?80:newwidth);
					}
				}
			}).mouseup(function(e){
				
				if($(".resizewidth").length>0){
					var endx=e.clientX;
					var diffx=Math.abs(endx-currdiv.startx);
					if(controlID=="") return false;
					var currth=$(".tablebodyhead"+controlID+" th div[col='"+divpindex+"']");
					var newwidth=0;
					if(currdiv.startx>endx){
						if((currth.width()-diffx)<60){
							newwidth=60;
						}else{
							newwidth=currth.width()-diffx;
						}
					}else{
						newwidth=currth.width()+diffx;
					}
					currdiv.find("div[col='"+divpindex+"']").width(newwidth);
					$(".resizewidth,.resizeform").remove();
					
					currdiv.data("divtdwidth",currdiv.getdivsize());
					
					currdiv.setleftwidth();
				}
				$("body").removeClass("noselect");
				controlID="";
				currdiv.startx=0;
			});
		},
		//右键选择显示列（设置 显示或隐藏列的功能）
		"showmenu":function(){
			var currdiv=this;
			var opts=this.opts;
			currdiv.find("th").on("contextmenu",function(e){
				e.preventDefault();
				var menu=$(".tablemenu"+opts.controlID+"");
				menu.css({"left":(e.pageX+'px'),"top":(e.pageY+'px'),"height":"auto"});
				menu.width(165);
			});
			$(".tablemenu"+opts.controlID+"").click(function(e){
				e.stopPropagation();
			});
			$(window).click(function(e){
				$(".tablemenu"+opts.controlID+"").height(0);
			});
			
			$(".tablemenu"+opts.controlID+" :checkbox").change(function(){
				var v=parseInt($(this).val());
				if($(this).is(":checked")){
					currdiv.find("th[colnum='"+v+"'],td[colnum='"+v+"']").show();
				}else{
					currdiv.find("th[colnum='"+v+"'],td[colnum='"+v+"']").hide();
				}
				currdiv.setLeftRightWidth();
			});
		},
		//设置Div的宽度
		"sizediv":function(ws){
			var currdiv=this;
			var opts=this.opts;
			//currdiv.find("th div[col],td div[col]").css({"width":"auto"});
			var tddivwidth=[];
			var isfirst=false;
			if(ws==undefined||ws==null||ws.length==0){
				tddivwidth=[];
				isfirst=true;
			}else
				tddivwidth=ws;
			if(tddivwidth.length==0){
				$.each(opts.columns,function(i,item){
					var divs=currdiv.find("td div[col='"+i+"']");
					var num=0;
					divs.each(function(j){
						var nw=$(this).width();
						if(nw>num)
							num=nw;
					});
					tddivwidth.push(num)
				});
			}
			
			for(i=0;i<tddivwidth.length;i++){
				currdiv.find("div[col='"+i+"']").width(tddivwidth[i]);
			}
			
			if(opts.percent==undefined)
				currdiv.find(".tablestyle4").css({"min-width":"auto","width":"auto"});
			if(isfirst&&$("div.wapperleft table").length==0){
				var diffwidth=currdiv.width()-currdiv.find("div.wapperright table").width();
				if(diffwidth>0){
					var lastdivindex=tddivwidth.length-1;
					currdiv.find("div[col='"+lastdivindex+"']").width(tddivwidth[lastdivindex]+diffwidth);
					tddivwidth[lastdivindex]=tddivwidth[lastdivindex]+diffwidth;
				}
			}
			if(opts.percent!=undefined){
				currdiv.find("th:last-child,tr td:last-child").css({"width":"99%"});
			}
			return tddivwidth;
		},
		//获取Div的宽度
		"getdivsize":function(){
			var currdiv=this;
			var tddivwidth=[];
			currdiv.find("th div[col]").each(function(){
				tddivwidth.push($(this).width());
			});
			return tddivwidth;
		},
		//初始化显示的 数据
		"initgrid":function(){
			var currdiv=this;
			var opts=this.opts;
			
			//整理一下，是否存在分组的列
			this.makeGroup();
			
			//更新数据时，如果已经有数据表格了，那么要先删除。
			currdiv.find(".tablebodydata"+opts.controlID).remove();
			var arrayTdLeft=[],arrayTdRight=[];
			
			arrayTdLeft.push("<table class='tablestyle4 tablebodydata"+opts.controlID+"'>");
			arrayTdRight.push("<table class='tablestyle4 tablebodydata"+opts.controlID+"'>");
			
			opts.tritemdata=[];
			
			if(opts.data==undefined||opts.data==null||opts.data.length==0){
				currdiv.dataIsNull();
				return false;	
			}
			
			if(!currdiv.isNullObject(opts.keyfield)){
				opts.data.sort(currdiv.compare);
			}
			
			//先循环数据，然后根据字段绑定。
			$.each(opts.data,function(i,item){
				//一个数据中私有的变量，做为当前的关键字
				if(opts.primarykey==null){
					opts.primarykey="privatefixedcode";
				}
				
				//如果用这个元素自定义的关键字，那么设定它的数据。
				if(opts.primarykey=="privatefixedcode"){
					var currpageno=(i+1);
					if(opts.pageindex>1){
						currpageno=(opts.pageindex-1)*opts.pagesize+i+1;
					}
					item.privatefixedcode=currpageno.toString();
				}
				
				arrayTdLeft.push("<tr data-index='"+i+"'>");
				arrayTdRight.push("<tr data-index='"+i+"'>");
				
				//如：有星号
				if(opts.star){



					if(Array.isArray(opts.star)){
						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='width:1px'><div class='star'>"+(opts.star[i]==1?"*":"")+"</div></td>");
						else
							arrayTdRight.push("<td style='width:1px'><div class='star'>"+(opts.star[i]==1?"*":"")+"</div></td>");
					}else{
						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='width:1px'><div class='star'>*</div></td>");
						else
							arrayTdRight.push("<td style='width:1px'><div class='star'>*</div></td>");
					}
				}
					
				//如：有序号
				if(opts.rownumbers){
					var currpageno=(i+1);
					if(opts.pageindex>1){
						currpageno=(opts.pageindex-1)*opts.pagesize+i+1;
					}
					if(opts.fixedcolumn)
						arrayTdLeft.push("<td style='width:1px'><div class='num'>"+currpageno+"</div></td>");
					else
						arrayTdRight.push("<td style='width:1px'><div class='num'>"+currpageno+"</div></td>");
				}
				
				//如：有选择框
				
				if(opts.checkbox){
					var displaynone="";
					if(opts.isHideCheckbox)displaynone="display:none;"
					if(Array.isArray(opts.checkbox)){
						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='"+displaynone+"width:1px'><div class='chk'>"+(opts.checkbox[i]==1?"<input type='checkbox' value='"+i+"' />":"")+"</div></td>");
						else
							arrayTdRight.push("<td style='"+displaynone+"width:1px'><div class='chk'>"+(opts.checkbox[i]==1?"<input type='checkbox' value='"+i+"' />":"")+"</div></td>");
					}else{
						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='"+displaynone+"width:1px'><div class='chk'><input type='checkbox' value='"+i+"' /></div></td>");
						else
							arrayTdRight.push("<td style='"+displaynone+"width:1px'><div class='chk'><input type='checkbox' value='"+i+"' /></div></td>");
					}
				}
				
				if(opts.radio){
					var displaynone="";
					if(opts.isHideRadio)displaynone="display:none;"
					if(Array.isArray(opts.radio)){
						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='"+displaynone+"width:1px'><div class='rdo'>"+(opts.radio[i]==1?"<input type='radio' value='"+i+"' name='rdo"+opts.controlID+"'/>":"")+"</div></td>");
						else
							arrayTdRight.push("<td style='"+displaynone+"width:1px'><div class='rdo'>"+(opts.radio[i]==1?"<input type='radio' value='"+i+"' name='rdo"+opts.controlID+"'/>":"")+"</div></td>");
					}else{

						if(opts.fixedcolumn)
							arrayTdLeft.push("<td style='"+displaynone+"width:1px'><div class='rdo'><input type='radio' value='"+i+"' name='rdo"+opts.controlID+"'/></div></td>");
						else
							arrayTdRight.push("<td style='"+displaynone+"width:1px'><div class='rdo'><input type='radio' value='"+i+"' name='rdo"+opts.controlID+"'/></div></td>");
					}
				}
				
				//现在开始利用字段绑定。
				$.each(opts.columns,function(j,columnitem){
					var createTempTd="";
					//当前字段的值
					var currvalue="";//item[columnitem.col]==undefined?"":item[columnitem.col];
					
					if(Array.isArray(columnitem.col)){
						$.each(columnitem.col,function(x,xitem){
							if(item[xitem]!=undefined){
								if(currvalue=="")
									currvalue=item[xitem];
								else
									currvalue+=(opts.valueSplitSign+item[xitem]);
							}
						})
					}else{
						currvalue=item[columnitem.col]==undefined?"":item[columnitem.col];
					}
					
					if(columnitem.type!=undefined&&columnitem.type=="tr"){
						if(currvalue==columnitem.tr.value)
							opts.tritemdata.push(columnitem.class+" "+columnitem.tr.class);
						else
							opts.tritemdata.push(columnitem.class);
					}
					
					if(columnitem.type=="number")
						columnitem.align="right";
					//样式表，如：宽度，文本对齐
					var tablestyle="style='",tablestylenowidth="style='";
					if(columnitem.width!=undefined)
						tablestyle+=("width:"+columnitem.width+"px;");
					if(columnitem.align!=undefined){
						tablestyle+=("text-align:"+columnitem.align+";");
						tablestylenowidth+=("text-align:"+columnitem.align+";");
					}
					if(columnitem.min!=undefined){
						tablestyle+=("min-width:"+columnitem.min+"px;");
						tablestylenowidth+=("min-width:"+columnitem.min+"px;");
					}
					
					if(columnitem.visible!=undefined)
						tablestyle+=(item[columnitem.visible]==0?"visibility:hidden;":"");
					
					tablestyle+=("'");
					tablestylenowidth+=("'");
					
					var classname="";
					if(columnitem.class!=undefined)classname=columnitem.class;
					
					//根据不同的 类型设置数据。
					var idisabled=false;
					if(columnitem.disabled!=undefined){
						if(typeof(columnitem.disabled)=="string")
							idisabled=item[columnitem.disabled];
						else
							idisabled=columnitem.disabled;
					}
					//如果使用了disabled禁用属性
					if(idisabled||opts.disabled){
						if(columnitem.type!=undefined&&columnitem.type=="select"){
							var currselecttext=""
							if(columnitem.disabledtext==undefined||columnitem.disabledtext==''){
								var selectdata=(typeof columnitem.data=="string")?item[columnitem.data]:columnitem.data;
								$.each(selectdata,function(x,xitem){
									if(currvalue==xitem[columnitem.value]){
										if(Array.isArray(columnitem.text)){
											$.each(columnitem.text,function(y,yitem){
												if(xitem[yitem]!=undefined){
													if(currselecttext=="")
														currselecttext=xitem[yitem];
													else
														currselecttext+=(opts.valueSplitSign+xitem[yitem]);
												}
											})
										}else{
											currselecttext=xitem[columnitem.text]==undefined?"":xitem[columnitem.text];
										}
										return false;
									}
								});
							}else{
								if(Array.isArray(columnitem.disabledtext)){
									$.each(columnitem.disabledtext,function(y,yitem){
										if(item[yitem]!=undefined){
											if(currselecttext=="")
												currselecttext=item[yitem];
											else
												currselecttext+=(opts.valueSplitSign+item[yitem]);
										}
									})
								}else{
									currselecttext=item[columnitem.disabledtext]==undefined?"":item[columnitem.disabledtext];
								}
							}
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col colselectdisabled' "+tablestyle+">"+currdiv.formartNullString(currselecttext)+"</div></td>");
						}else{
							//禁用时生效
							if(columnitem.distype==undefined){
								columnitem.distype=null;
							}
							if(columnitem.distype=="number"){
								createTempTd=("<td colnum='"+j+"' class='"+classname+"' style='text-align:right;'><div col='"+j+"' class='col' "+tablestyle+">"+currdiv.formartNullString(currdiv.formatText(currvalue,0))+"</div></td>");
							}else
								createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col' "+tablestyle+">"+currdiv.formartNullString(currvalue)+"</div></td>");
						}
					}
					//如果是无类型数据，按文本输出
					else if(columnitem.type==undefined||columnitem.type==""){
						var tips="";
						if(!currdiv.isNullObject(columnitem.tips)){
							if(item[columnitem.tips]==undefined)
								tips=" source-title='"+columnitem.tips+"'";
							else
								tips=" source-title='"+item[columnitem.tips]+"'";
						}
						var classrich="",richtext="";
						if(!currdiv.isNullObject(columnitem.rich)){
							classrich=" colrichtext";
							richtext="&nbsp;<span class='tiinfo glyphicon glyphicon-info-sign'></span>"
						}
						var multiline="";
						if(columnitem.multi!=undefined&&columnitem.multi==true)
							multiline=" multi";
						createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col"+classrich+multiline+"' "+tablestyle+tips+">"+currdiv.formartNullString(currvalue)+richtext+"</div></td>");
					}
					else{
						//下拉框
						if(columnitem.type=="select"){
							
							var tableoption="",isHasSelected=false;
							//如果是字符串，那么去整体的数据中按字段名去取数据。
							var selectdata=(typeof columnitem.data=="string")?item[columnitem.data]:columnitem.data;
							$.each(selectdata,function(x,xitem){
								var option="";
								var text="";
								
								if(Array.isArray(columnitem.text)){
									$.each(columnitem.text,function(y,yitem){
										if(xitem[yitem]!=undefined){
											if(text=="")
												text=xitem[yitem];
											else
												text+=(opts.valueSplitSign+xitem[yitem]);
										}
									})
								}else{
									text=xitem[columnitem.text]==undefined?"":xitem[columnitem.text];
								}
								
								var value=xitem[columnitem.value];
								var tips=text;
								if(columnitem.tips!=undefined)
									tips=xitem[columnitem.tips];
								if(columnitem.parent!=undefined&&columnitem.parent!=null){
									if(xitem[columnitem.parent]==item[columnitem.parent]){
										if(currvalue==value){
											option="<option selected='selected' value='"+value+"' title='"+tips+"'>"+text+"</option>";
											isHasSelected=true;
										}
										else
											option="<option value='"+value+"' title='"+tips+"'>"+text+"</option>";
									}
								}else{
									if(currvalue==value){
										option="<option selected='selected' value='"+value+"' title='"+tips+"'>"+text+"</option>";
										isHasSelected=true;
									}
									else
										option="<option value='"+value+"' title='"+tips+"'>"+text+"</option>";
								}
								tableoption+=option;
							});
							
							if(!isHasSelected){
								tableoption="<option value=''>请选择</option>"+tableoption;
							}
							
							var tableselect="<select "+tablestylenowidth+" data-index-row='" + i + "' data-index-col='"+j+"' class='form-control'>"+tableoption+"</select>";
							
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col colselect' title=''>"+tableselect+"</div></td>");
						}
						//文本框
						else if(columnitem.type=="text"){
							var tablebtn="",inputstyle="";
							if(columnitem.search!=undefined&&columnitem.search==true){
								tablebtn="<a href='javascript:void(0)' class='btntxt'></a>";
								inputstyle=" txtbtntxt";
							}
							var strmaxlength='';
							if(columnitem.maxlength!=undefined)
								strmaxlength=(" maxlength='"+columnitem.maxlength+"'");
							
							var tableinput="<input type='text' class='form-control txt"+columnitem.col+inputstyle+"' "+tablestylenowidth+" data-index-row='" + i + "'"+strmaxlength+" data-index-col='"+j+"' value='" + currvalue + "' />";
							
							createTempTd=("<td colnum='"+j+"' class='"+classname+"' "+tablestyle+"><div col='"+j+"' class='col coltext' >"+tableinput+tablebtn+"</div></td>");
						}
						//日期类型
						else if(columnitem.type=="date"){
							var tableinput="<input type='text' class='form-control date"+columnitem.col+"' "+tablestylenowidth+" data-index-row='" + i + "' data-index-col='"+j+"' value='" + currvalue + "' readonly data-date-format='"+columnitem.fomart+"'/>";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col coldate' >"+tableinput+"</div></td>");
						}
						//数字类型
						else if(columnitem.type=="number"){
							var columnitemdecimal=columnitem.decimal==undefined?0:columnitem.decimal;
							var coldecimal=item[columnitemdecimal]==undefined?columnitemdecimal:item[columnitemdecimal];

							var fomarttext=currvalue==""?"0.0":currvalue;
							fomarttext=currdiv.formartDecimal_f(fomarttext,coldecimal);
							if(fomarttext==false)
								fomarttext=currvalue;
							fomarttext=fomarttext.toString();
							var fomarttext1="",fomarttext2="";
							if(fomarttext.indexOf('.')>-1){
								fomarttext1=fomarttext.split('.')[0];
								fomarttext2=fomarttext.split('.')[1];
							}else
								fomarttext1=fomarttext;
							fomarttext1=(fomarttext1 || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
							if(fomarttext2=="")
								fomarttext=fomarttext1;
							else
								fomarttext=fomarttext1+"."+fomarttext2;
							classname=classname==""?"":" "+classname;
							createTempTd=("<td colnum='"+j+"' class='showsum"+classname+"'><div col='"+j+"' class='col colnum' "+tablestyle+">"+fomarttext+"&nbsp;</div></td>");
						}
						//强出层
						else if(columnitem.type=="popup"){
							var popup = columnitem.popup;
							var hrefurl="",isNullURL=false;
							if(typeof popup.href=="boolean" && popup.href==false){
								var cvalue=item[popup.column];
								$.each(popup.links,function(x,xitem){
									if(cvalue==xitem.value){
										if(xitem.href=="")
											isNullURL=true;
										else{
											if(xitem.href.indexOf('?')==-1)
												hrefurl=xitem.href+"?index="+i.toString();
											else
												hrefurl=xitem.href+"&index="+i.toString();
										}
										return false;
									}
								})
							}
							else if(popup.href.indexOf('?')==-1)
								hrefurl=popup.href+"?index="+i.toString();
							else
								hrefurl=popup.href+"&index="+i.toString();
							if(popup.args!=undefined){
								$.each(popup.args,function(x,xitem){
									var urlarg=xitem,colarg=xitem;
									if(xitem.indexOf('|')>-1){
										var xitems=xitem.split('|');
										urlarg=xitems[1];
										colarg=xitems[0];
									}
									var tempurl=("&"+urlarg+"="+item[colarg]);
									hrefurl+=tempurl;
								});
							}
							if(popup.random!=undefined&&popup.random==true){
								var r= Math.floor(Math.random()*999999)+100001;
								hrefurl+=("&random="+r);
							}
							var popupdisplay=popup.display;
							if(popup.display==undefined||popup.display==null||popup.display=="")
								popupdisplay=currvalue;
							else{
								if(item[popupdisplay]!=undefined)
									popupdisplay=item[popupdisplay];
							}
							if(columnitem.decimal!=undefined){
								var decimalvalue=item[columnitem.decimal]==undefined?columnitem.decimal:item[columnitem.decimal];
								var formarpopupdisplay=currdiv.formartDecimal_f(popupdisplay,decimalvalue);
								if(formarpopupdisplay!=false)
									popupdisplay=formarpopupdisplay;
							}
							var tableselbtn = "";
							
							var isDecimalNum=false;
							if(currdiv.isDecimal(popupdisplay)){
								popupdisplay=popupdisplay.toString();
								isDecimalNum=true;
								var fomarttext1="",fomarttext2="";
								if(popupdisplay.indexOf('.')>-1){
									fomarttext1=popupdisplay.split('.')[0];
									fomarttext2=popupdisplay.split('.')[1];
								}else
									fomarttext1=popupdisplay;
								fomarttext1=(fomarttext1 || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
								if(fomarttext2=="")
									popupdisplay=fomarttext1;
								else
									popupdisplay=fomarttext1+"."+fomarttext2;	
							}
							
							if(isNullURL)
								tableselbtn = "<span>"+currdiv.formartNullString(popupdisplay)+"</span>";
							else
								tableselbtn = "<a href='javascript:void(0)' data-title='"+popup.title+"' data-href='"+hrefurl+"' data-size='"+popup.area+"' class='btn_table_popup'>"+popupdisplay+"</a>";

							var textright="";
							if(isDecimalNum)
								textright=" style='text-align:right'";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'"+textright+"><div col='"+j+"' class='col colpopup' "+tablestyle+">"+tableselbtn+"</div></td>");
						}
						//链接
						else if(columnitem.type=="link"){
							var tableurl="";
							var boolautocreate=false;
							if(columnitem.link.autocreate==undefined||columnitem.link.autocreate==true)
								boolautocreate=true;
							var isNullURL=false
							if(columnitem.link.href==false){
								var cvalue=item[columnitem.link.column];
								$.each(columnitem.link.links,function(x,xitem){
									if(cvalue==xitem.value){
										if(xitem.href=="")
											isNullURL=true;
										else{
											if(boolautocreate==true){
												if(xitem.href.indexOf('?')==-1)
													tableurl=xitem.href+"?no="+currvalue;
												else
													tableurl=xitem.href+"&no="+currvalue;
											}else
												tableurl=xitem.href
										}
										
										return false;
									}
								})
							}else{
								if(boolautocreate==true){
									if(columnitem.link.href.indexOf('?')==-1)
										tableurl=columnitem.link.href+"?no="+currvalue;
									else
										tableurl=columnitem.link.href+"&no="+currvalue;
								}else
									tableurl=columnitem.link.href;
							}
							
							if(currdiv.GetQueryString("key")!=null&&tableurl.indexOf("key")==-1){
								if(tableurl.indexOf('?')==-1)
									tableurl+=("?key="+currdiv.GetQueryString("key"));
								else
									tableurl+=("&key="+currdiv.GetQueryString("key"));
							}
							if(columnitem.link.args!=undefined){
								$.each(columnitem.link.args,function(x,xitem){
									var urlarg=xitem,colarg=xitem;
									if(xitem.indexOf('|')>-1){
										var xitems=xitem.split('|');
										urlarg=xitems[1];
										colarg=xitems[0];
									}
									var tempurl="";
									if(tableurl.indexOf('?')==-1)
										tempurl=("?"+urlarg+"="+item[colarg])
									else
										tempurl=("&"+urlarg+"="+item[colarg])
									tableurl+=tempurl;
								});
							}
							var tableselbtn = "";
							var displayname=currvalue;
							if(!currdiv.isNullObject(columnitem.link.display)){
								displayname=columnitem.link.display;
							}
							if(isNullURL)
								tableselbtn = "<span>--</span>";
							else
								tableurlbtn = "<a href='"+tableurl+"' "+(columnitem.link.target!=undefined?("target='"+columnitem.link.target+"'"):"")+" >"+displayname+"</a>";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col collink' "+tablestyle+">"+tableurlbtn+"</div></td>");
						}
						//布尔
						else if(columnitem.type=="bool"){
							var ischecked="";
							if(currvalue==columnitem.bool.truevalue){
								ischecked="checked";
							}
							var tablecheckbox = "<input type='checkbox' value='field' "+ischecked+" data-index-row='" + i + "' data-index-col='"+j+"' />";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"' ><div col='"+j+"' class='col colbool' "+tablestyle+">"+tablecheckbox+"</div></td>");
						}
						//高亮
						else if(columnitem.type=="light"){
							var tdclass="";
							var classvalue=item[columnitem.light.coldisplay];
							$.each(columnitem.light.type,function(x,xitem){
								if(xitem.value==classvalue){
									tdclass=xitem.class;
									return false;
								}
							});
							classname=currdiv.isNullObject(classname)?"":" "+classname;
							createTempTd=("<td colnum='"+j+"' class='"+tdclass+""+classname+"' ><div col='"+j+"' class='col collight' "+tablestyle+">"+currvalue+"</div></td>");
						}
						//删除
						else if(columnitem.type=="delete"){
							var tableselbtn = "<a href='javascript:void(0)' class='btn_table_delete' data-index-row='" + i + "' data-tips='"+columnitem.tips+"'>删除</a>";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col coldelete' "+tablestyle+">"+tableselbtn+"</div></td>");
						}
						//按键
						else if(columnitem.type=="button"){
							var buttontext=item[columnitem.button.text]==undefined?columnitem.button.text:item[columnitem.button.text];
							var tablebtn = "<a href='javascript:void(0)' class='table_button' data-index-row='" + i + "' data-index-col='"+j+"'>"+buttontext+"</a>";
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col colbutton' "+tablestyle+">"+tablebtn+"</div></td>");
						}
						//时间戳
						else if(columnitem.type=="timestamp"){
							createTempTd=("<td colnum='"+j+"'><div col='"+j+"' class='col coltimestamp' "+tablestyle+">"+currdiv.formartNullString(currdiv.formartTimestamp(currvalue))+"</div></td>");
						}
						//描述类型
						else if(columnitem.type=="describe"){
							var currtext="--";
							$.each(columnitem.describe,function(i,item){
								if(item.value==currvalue)
									currtext=item.text;
							});
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col coltimestamp' "+tablestyle+">"+currtext+"</div></td>");
						}
						//分组
						else if(columnitem.type=="group"){
							createTempTd=("<td colnum='"+j+"' class='"+classname+"'><div col='"+j+"' class='col colgroup' data-group='"+currvalue+"' "+tablestyle+">"+currvalue+"</div></td>");
						}
					}
					
					if(opts.fixedcolumn&&columnitem.fixed!=undefined&&columnitem.fixed==true)
						arrayTdLeft.push(createTempTd);
					else
						arrayTdRight.push(createTempTd);
				});
				if(opts.percent!=undefined){
					arrayTdRight.push("<td style='width:"+opts.percent+"%'>&nbsp;</td>");
				}
				
				arrayTdLeft.push("</tr>");
				arrayTdRight.push("</tr>");
			});
			
			arrayTdLeft.push("</table>");
			arrayTdRight.push("</table>");
			
			if(opts.fixedhead==false){
				if(opts.fixedcolumn)
					this.find("div.wapperleft").append("<div class='body'>"+arrayTdLeft.join('')+"</div>");
				this.find("div.wapperright").append("<div class='body'>"+arrayTdRight.join('')+"</div>");
			}
			else{
				if(opts.fixedcolumn)
					this.find("div.wapperleft").append("<div class='body'>"+arrayTdLeft.join('')+"</div>");
				this.find("div.wapperright").append("<div class='body' style='overflow-y:auto;height:"+opts.fixedhead+"px'>"+arrayTdRight.join('')+"</div>");
				
				//当设置了按百分之百宽度时
				if(opts.absolutelyWidth){
					this.append("<div class='righrscroll' style='height:"+opts.fixedhead+"px'><div></div></div>");
					this.scroll(function(){
						$("div.righrscroll").css({"right":(-($(this).scrollLeft()))})
					})
				}
			}
			
			// 下拉框模拟
			this.clickselect();
			
			//文本保存
			this.saveinput();
			
			//设置Div的宽度
			var divtdwidth= this.sizediv(this.data("divtdwidth"));
			//每次都要缓存数据。
			this.data("divtdwidth",divtdwidth);
			
			//给文本框加上时间选择框
			this.find(".coldate input").each(function () {
				$(this).datetimepicker({
					language: 'zh-CN',
					minView: 2,
					weekStart: 1,
					todayBtn: 1,
					autoclose: 1,
					todayHighlight: 1,
					startView: 2,
					forceParse: 0
				});
			}).on("changeDate",function(){
				// 选择完时间后，保存在Json对象中。
				var time=$(this).val();
				var currtext=$(this);
				var indexrownum=parseInt(currtext.attr("data-index-row"));
				var indexcolnum=parseInt(currtext.attr("data-index-col"));
				var curroptdata=opts.data[indexrownum];
				var currcol=opts.columns[indexcolnum]; 
				opts.data[indexrownum][currcol.col]=time;
			});
			
			//总数的统计与显示
			this.showSumNumber();
			
			//弹出页
			this.popup();
			
			//CheckBox的 点击
			this.checkboxchange();
			
			//行加样式
			this.traddclass();
			
			//删除数据
			this.delete();
			
			//列表内的Button
			this.button();
			
			//点击行
			this.clickrow();
			
			//数据结束时
			this.loadcomplete();
			
			this.setleftwidth();
			
			this.find("th").each(function(){
				if($(this).attr("style")!=undefined&&$(this).attr("style").indexOf("none")>-1){
					currdiv.find("td[colnum='"+$(this).attr("colnum")+"']").hide();
				}
			})
			
			//this.reSetRowHeight();
			this.showRichTextOrTitie();
			
			//设置行选择的复选框
			this.setCheckBox();
			
			//执行合并表格
			this.executeGroup();
			
			this.radiochange();
			
			//当设置了按百分之百宽度时
			if(opts.absolutelyWidth){
				$(".righrscroll div").height($("div.wapperright div.body table").height()+2);
				//俩个滚动条，互相控制。
				$(".righrscroll").scroll(function(){
					$("div.wapperright div.body").scrollTop($(this).scrollTop());
				});
				$("div.wapperright div.body").scroll(function(){
					var curr=$(this);
					setTimeout(function(){$(".righrscroll").scrollTop(curr.scrollTop());},200);
				});
			}
		},
		"setleftwidth":function(){
			var currdiv=this;
			var opts=this.opts;
			
			this.setLeftRightWidth();
	
			this.find(".tablebodydata"+opts.controlID+" tr").hover(function(){
				var index=($(this).attr("data-index"));
				var currtr= currdiv.find(".tablebodydata"+opts.controlID+" tr[data-index='"+index+"']");
				currtr.addClass("hover");
			},function(){
				var index=($(this).attr("data-index"));
				var currtr= currdiv.find(".tablebodydata"+opts.controlID+" tr[data-index='"+index+"']");
				currtr.removeClass("hover");
			});
		},
		"setLeftRightWidth":function(){
			var currdiv=this;
			var opts=this.opts;
			if(opts.fixedcolumn){
				var firstwidth=opts.firstwidth;
				var secondwidth=$(".wapper"+opts.controlID+"").width();
				currdiv.find("div.wapperright").width(firstwidth-currdiv.find(".wapperleft div.body table").width()-2).css({"overflow":"auto"});//-2
				currdiv.find(".wapperleft").width(currdiv.find(".wapperleft div.body table").width()-0);//+2
				//currdiv.find(".wapper,div.wapperleft").css({"width":"auto"});
			}
		},
		"reSetRowHeight":function(){
			var currdiv=this;
			var opts=this.opts;
			if(opts.fixedcolumn){
				var leftheight=[],rightheight=[];
				currdiv.find(".wapperleft tr:has(td)").each(function(){
					leftheight.push($(this).height())
				});
				currdiv.find(".wapperright tr:has(td)").each(function(){
					rightheight.push($(this).height())
				});
				for(var i=0;i<leftheight.length;i++){
					if(leftheight[i]>rightheight[i]){
						currdiv.find(".wapperright tr:has(td)").eq(i).height(leftheight[i]);
					}else{
						currdiv.find(".wapperleft tr:has(td)").eq(i).height(rightheight[i]);
					}
				}
			}
		},
		//点击列表中的下拉
		"clickselect":function(){
			var currdiv=this;
			var opts=this.opts;
			currdiv.find("div.body div.colselect select").change(function(){
				$(this).find("option[value='']").remove();
				var currval=$(this).val();
				var indexrownum=parseInt($(this).attr("data-index-row"));
				var indexcolnum=parseInt($(this).attr("data-index-col"));
				
				var curroptdata=opts.data[indexrownum];
				var currcol=opts.columns[indexcolnum];
				
				opts.data[indexrownum][currcol.col]=currval;
				
				if(currcol.children!=undefined&&currcol.children!=null){
					opts.data[indexrownum][currcol.children]="";
				}
				
				var selecteddata=null;
				//如果是字符串，那么去整体的数据中按字段名去取数据。
				var selectdata=(typeof currcol.data=="string")?curroptdata[currcol.data]:currcol.data;
				$.each(selectdata,function(i,item){
					if(item[currcol.col]==currval){
						selecteddata=item;
						return false;
					}
				});
				
				currdiv.initgrid();//更新显示的数据
				
				if(opts.cselect!=undefined){
					opts.cselect(currcol.col, indexcolnum, indexrownum, opts.data[indexrownum], selecteddata);
				}
			});			
		},
		// 输入框的保存
		"saveinput":function(){
			var opts=this.opts;
			var currdiv=this;
			var currtxt=this.find(":text");
			
			currtxt.blur(function(){
				var currtext=$(this);
				var indexrownum=parseInt(currtext.attr("data-index-row"));
				var indexcolnum=parseInt(currtext.attr("data-index-col"));
				var curroptdata=opts.data[indexrownum];
				var currcol=opts.columns[indexcolnum]; 
				var currtextval=currtext.val();
				opts.data[indexrownum][currcol.col]=currtextval.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');

				if(currdiv.textCheck(currcol,currtext,currtextval,curroptdata)&&opts.cblur!=undefined)
					opts.cblur(currcol.col, indexcolnum, indexrownum, currtextval, curroptdata, currtext);
			}).focus(function(){
				var currtext=$(this);
				var indexrownum=parseInt(currtext.attr("data-index-row"));
				var indexcolnum=parseInt(currtext.attr("data-index-col"));
				var curroptdata=opts.data[indexrownum];
				var currcol=opts.columns[indexcolnum]; 
				var currtextval=currtext.val();
				opts.data[indexrownum][currcol.col]=currtextval.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');

				if(opts.cfocus!=undefined)
					opts.cfocus(currcol.col, indexcolnum, indexrownum, currtextval, curroptdata, currtext);
			}).keyup(function (e) {
				var currtext=$(this);
				var keycode = e.which;
				if (keycode == 13) {
					var indexrownum=parseInt(currtext.attr("data-index-row"));
					var indexcolnum=parseInt(currtext.attr("data-index-col"));
					var curroptdata=opts.data[indexrownum];
					var currcol=opts.columns[indexcolnum]; 
					var currtextval=currtext.val();
					opts.data[indexrownum][currcol.col]=currtextval.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');

					if(opts.enter!=undefined)
						opts.enter(currcol.col, indexcolnum, indexrownum, currtextval, curroptdata, currtext);
					}
			});
			
		},
		"textCheck":function(currcol,currtext,currtextval,curroptdata){
			var opts=this.opts;
			var currdiv=this;
			var ischecked1=true,ischecked2=true,ischecked3=true;
			//根据正则表达式验证
			
			//是否可以为空，默认不可为空
			var isnullable=true;
			if(currcol.nullable==undefined){
				currcol.nullable=false;
			}else{
				if(typeof currcol.nullable=='string'){
					//当允许为空时
					if(curroptdata[currcol.nullable]){
						//那么空的不判断
						if(currtextval==undefined||currtextval=="")
							isnullable=false;
						else
							isnullable=true;
					}else//不允许为空时，直接就可以过
						isnullable=true;
					
				}else{
					//当允许为空时
					if(currcol.nullable){
						//那么空的不判断
						if(currtextval==undefined||currtextval=="")
							isnullable=false;
						else
							isnullable=true;
					}else//不允许为空时，直接就可以过
						isnullable=true;
				}
			}
			
			if(currcol.verify==undefined)
				currcol.verify=true;
			var isverify=currcol.verify;
			if(typeof currcol.verify=='string'){
				isverify=curroptdata[currcol.verify];
			}
			if(isverify&&isnullable&&currcol.pattern!=undefined&&currcol.pattern.regex!=undefined&&currtextval!=""){
				var patt=new RegExp(currcol.pattern.regex);
				if(!patt.test(currtextval)){
					currtext.focus();
					var tips="格式不正确，验证失败。";
					if(currcol.pattern.tips!=undefined)
						tips=currcol.pattern.tips;
					currdiv.layersMoretips(tips,currtext,true);
					ischecked1=false;
				}
			}
			//根据当触发的条件验证
			if(isverify&&isnullable&&currcol.dowhile!=undefined&&currcol.dowhile.regex!=undefined&&currcol.dowhile.tips!=undefined&&currtextval!=""){
				var regexs=currcol.dowhile.regex.split('~');
				var isChecked=false;
				for(var i=0;i<regexs.length;i++){
					var stringregex=regexs[i].replace(/rowdata\[/g, "curroptdata['");
					stringregex=stringregex.replace(/]/g, "']*1000");
					stringregex=(currtextval*1000).toString()+stringregex;
					if(eval(stringregex)==true)isChecked=true;
				}
				
				if(isChecked){
					currtext.focus();
					var tips="输入有误。";
					if(currcol.dowhile.tips!=undefined)
						tips=currcol.dowhile.tips;
					currdiv.layersMoretips(tips,currtext,true);
					ischecked2=false;
				}
			}
			//根据小数位验证
			if(isverify&&isnullable&&currcol.decimal!=undefined&&currtextval!=""){
				if(currdiv.isDecimal(currtextval) == false) {
					currtext.focus();
					currdiv.layersMoretips("格式为数字。",currtext,true);
					ischecked3=false;
				}
				else {
					var num=0;
					if(typeof(currcol.decimal.regex)=="string")
						num=curroptdata[currcol.decimal.regex];
					else
						num=currcol.decimal.regex;
					var stc="";
					if(num == undefined) {
						num = 3;
						stc = "小数位不能大于3位";
					} else if(num == 0) {
						stc = "只能为整数,不可以是小数";
					} else {
						stc = "小数位不能大于 " + num + " 位";
					};
					if(typeof currtextval!="string")currtextval=currtextval.toString();
					if(currtextval.indexOf(".") > -1 && currtextval.split(".")[1].length > num) {
						currtext.focus();
						currdiv.layersMoretips(stc,currtext,true);
						ischecked3=false;
					}
				}
			}
			if(ischecked1&&ischecked2&&ischecked3)
				return true;
			else
				return false;
		},
		//格式化，小数(参数，原值，小数位数)
		"formartDecimal_f":function(x,y){
			if(y==null||y==0) return x;
			if(x==null)x=0;
			if(x.toString().indexOf('.')==-1){
				return x;
			}
			var f_x = parseFloat(x);
			if (isNaN(f_x)) {
				return false;
			}
			f_x = Math.round(f_x * 1000) / 1000;
			var s_x = f_x.toString();
			var pos_decimal = s_x.indexOf('.');
			if (pos_decimal < 0) {
				pos_decimal = s_x.length;
				s_x += '.';
			}
			while (s_x.length <= pos_decimal + y) {
				s_x += '0';
			}
			return s_x;
		},
		//是否是数字
		"isDecimal": function(v) {
			if (v != null && typeof (v) != "undefined") {
				var str = v.toString();
				if (str != "") {
					if (str.length == 1 && str == "0") {
						return true;
					} else {
						var pattern = '^-?[1-9]\\d*$|^-?0\\.\\d*$|^-?[1-9]\\d*\\.\\d*$';
						var reg = new RegExp(pattern, 'g');
						return reg.test(str);
					}
				}else
					return false;
			}
			return false;
		},
		//总数的统计与显示。
		"showSumNumber":function(){
			var currdiv=this;
			var opts=this.opts;
			var showsum= currdiv.find(".showsum");
			var lastshowsum = { "objClearid": -1 };
			var currhoverevent=null;
			var hasshowtips=false;
			var prevnum=-1;
			showsum.unbind("hover").hover(function(){
				
				var indexcolnum=parseInt($(this).attr("colnum"));
				//console.log(prevnum+"____"+indexcolnum)
				if(prevnum!=indexcolnum){
					$(".sumbg").removeClass("sumbg");
					hasshowtips=false;
				}
				clearTimeout(lastshowsum.objClearid);
				if(hasshowtips==true) return;
				var currcol=opts.columns[indexcolnum];
				var currsumnumber=0.0;
				currhoverevent=currdiv.find("[colnum='"+indexcolnum+"'][class='showsum']");
				currhoverevent.addClass("sumbg")
				$.each(opts.data,function(i,item){
					var itemvalue=item[currcol.col];
					if(itemvalue!=null)
						currsumnumber=countData.add(currsumnumber,itemvalue);
				});
				prevnum=indexcolnum;
				hasshowtips=true;
				
				layer.tips((currhoverevent.eq(0).text() + "：（总）" + ((currdiv.formartDecimal_f(currsumnumber))==false?"0.0":currdiv.formartDecimal_f(currsumnumber))), currhoverevent.eq(0), {
					tips: [2, '#f7b824'],
					tipsMore: true,
					time: 2000
				});
			}, function () {
				lastshowsum.objClearid = setTimeout(function () {$(".sumbg").removeClass("sumbg"); hasshowtips=false;}, 300);
			});
			
		},
		//强出的绑定
		"popup":function(){
			var opts=this.opts;
			var btnedit=this.find(".btn_table_popup");
			btnedit.unbind("click");
			btnedit.click(function (e) {
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
		},
		//Checkbox发生改变的时候
		"checkboxchange":function(){
			var opts=this.opts;
			var currdiv=this;
			currdiv.find(".tablebodydata"+opts.controlID+" :checkbox").change(function(){
				var currcheckbox=$(this);
				if(currcheckbox.val()=="field"){
					var indexrownum=parseInt(currcheckbox.attr("data-index-row"));
					console.log(indexrownum+"_index_");
					var indexcolnum=parseInt(currcheckbox.attr("data-index-col"));
					var curroptdata=opts.data[indexrownum];
					var currcol=opts.columns[indexcolnum]; 
					var currvalue=null;
					if(currcheckbox.is(":checked"))
						currvalue=currcol.bool.truevalue;
					else
						currvalue=currcol.bool.falsevalue;
					var currobject=curroptdata[currcol.col];
					curroptdata[currcol.col]=currvalue;
					
					if(opts.checkBoxChange!=undefined){
						//返回数据行的下标，字段名，值，当前对象。
						opts.checkBoxChange(indexrownum,currcol.col,currvalue,currobject);
					}
				}else{
					//currdiv.setCheckBox();
					var index=currcheckbox.parents("tr").attr("data-index");
					if((opts.isMultiSelected||opts.isMultiCancel)&&index!=opts.clickRowIndex&&opts.clickRowIndex!=-1){
						var temp=-1,min=opts.clickRowIndex,max=index;
						for(var i=min;i<=max;i++){
							currdiv.clickRowEach(i,opts.isMultiSelected);
						}
						for(var i=max;i<=min;i++){
							currdiv.clickRowEach(i,opts.isMultiSelected);
						}

					}else{
						var isChecked=currcheckbox.is(":checked");
						currdiv.setSelectRow(isChecked,currcheckbox.attr("value"));
					}
					opts.clickRowIndex=index;
				}
			});
			currdiv.find(".tablebodyhead"+opts.controlID+" :checkbox").change(function(){
				var currcheckbox=$(this);
				if(currcheckbox.is(":checked")){
					currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").prop("checked", true);
				}else{
					currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").removeAttr("checked");
				}
				//currdiv.setCheckBox();
				if(currcheckbox.is(":checked")){
					currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").prop('checked','checked');
					currdiv.find(".tablebodydata"+opts.controlID+" tr").addClass("selected");
					opts.selectedRowData=[];
					$.each(opts.data,function(i,item){
						opts.selectedRowData.push(item);
					});
					opts.selectedData=[];
					$.each(opts.data,function(i,item){
						opts.selectedData.push(item[opts.primarykey]);
					});
					var seldata=[];
					$.each(opts.data,function(i,item){
						if(opts.selectedData.indexOf(item[opts.primarykey])>-1){
							currdiv.find("tr[data-index='"+i+"']").addClass("selected");
							currdiv.find("tr[data-index='"+i+"'] .chk :checkbox").attr("checked","checked");
							var obj={};
							if(opts.selectedrow!=null){
								if(opts.selectedrow.args!=undefined){
									$.each(opts.selectedrow.args,function(j,jitem){
										obj[jitem]=item[jitem];
									});
								}
								if(opts.selectedrow.isHasindex!=undefined&&opts.selectedrow.isHasindex){
									obj.index=i;
								}
								seldata.push(obj);
							}else{
								seldata.push(item);
							}
						}
					});
					if(opts.GetSelectedData!=undefined)
						opts.GetSelectedData(seldata);
				}
				else{
					currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").removeAttr('checked');
					currdiv.find(".tablebodydata"+opts.controlID+" tr").removeClass("selected");
					opts.selectedRowData=[];
					opts.selectedData=[];
					if(opts.GetSelectedData!=undefined)
						opts.GetSelectedData([]);
				}
				/*currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").each(function(){
					currdiv.setSelectRow(currcheckbox.is(":checked"),$(this).attr("value"));
				});*/
			});
		},
		//radio发生改变的时候
		"radiochange":function(){
			var opts=this.opts;
			var currdiv=this;
			currdiv.find(".tablebodydata"+opts.controlID+" .rdo :radio").change(function(){
				var radiobox=$(this);
				var index=parseInt($(this).attr("value"));
				currdiv.radiovalue=index;
				currdiv.setRadio(index);
			});
		},
		//为tr按照数据来加class
		"traddclass":function(){
			var opts=this.opts;
			if(opts.fixedcolumn){
				var trs = this.find("div.wapperleft div.body table tr");
				$.each(opts.tritemdata,function(i,item){
					trs.eq(i).addClass(item);
				});
			}
			var trs = this.find("div.wapperright div.body table tr");
			$.each(opts.tritemdata,function(i,item){
				trs.eq(i).addClass(item);
			});

		},
		//删除数据
		"delete":function(){
			var currdiv=this;
			var opts=this.opts;
			var btndelete=currdiv.find(".btn_table_delete");
			btndelete.unbind("click").click(function (e) {
				var indexrow = parseInt($(this).attr("data-index-row"));
				var tips = $(this).attr("data-tips");
				layer.confirm(tips, {
					btn: ['确定', '取消'],
					icon: 3
				}, function (index) {
					if(opts.delete!=undefined){
						opts.delete(opts.data[indexrow],indexrow);						
					}else{
						opts.data.splice(indexrow, 1);
						currdiv.initgrid();
					}
					layer.close(index);
				}, function () {

				});
				e.stopPropagation();
			});

		},
		//列表内的Button
		"button":function(){
			var currdiv=this;
			var opts=this.opts;
			currdiv.find(".table_button").click(function(){
				var currbutton=$(this);
				var indexrownum=parseInt(currbutton.attr("data-index-row"));
				var indexcolnum=parseInt(currbutton.attr("data-index-col"));
				var currcol=opts.columns[indexcolnum]; 
				if(opts.clickbutton!=undefined)
					opts.clickbutton(currcol.col,indexrownum,opts.data[indexrownum]);
			})
			
		},
		//自带一个 获得URL参数。
		"GetQueryString":function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r != null) return unescape(r[2]); return null;
		},
		//自带一个 提示的函数。
		"layersMoretips":function(tips,el,t) {
			var index=-1;
			if(t==undefined){
				index=layer.tips(tips, el, {
					tips: [2, '#f7b824']
				});
			}
			else{
				index=layer.tips(tips, el, {
					tips: [2, '#f7b824'],
					tipsMore: true,
					time: 5000
				});
			}
			return index;
		},
		//点击行
		"clickrow":function(){
			var currdiv=this;
			var opts=this.opts;
			
			currdiv.find("input,a,.colselect").click(function(e){
				e.stopPropagation();
			});
			
			currdiv.find(".tablebodydata"+opts.controlID+" tr").click(function(e){
				var index=$(this).attr("data-index");
				var currcheckbox=currdiv.find("tr[data-index='"+index+"'] .chk :checkbox");
				if(currcheckbox.length>0){
					//如果按了功能键， 采取连选中或连取消
					if((opts.isMultiSelected||opts.isMultiCancel)&&index!=opts.clickRowIndex&&opts.clickRowIndex!=-1){
						var temp=-1,min=opts.clickRowIndex,max=index;
						for(var i=min;i<=max;i++){
							currdiv.clickRowEach(i,opts.isMultiSelected);
						}
						for(var i=max;i<=min;i++){
							currdiv.clickRowEach(i,opts.isMultiSelected);
						}

					}else{
						var isChecked=false;
						if(currcheckbox.is(":checked")==true){
							currcheckbox.removeAttr("checked");
							isChecked=false;
						}
						else{
							currcheckbox.prop("checked", true);
							isChecked=true;
						}
						currdiv.setSelectRow(isChecked,currcheckbox.attr("value"));
					}
					opts.clickRowIndex=index;
				}
				var currRadio=currdiv.find("tr[data-index='"+index+"'] .rdo :radio");
				if(currRadio.length>0){
					if(opts.mb_YesNoCancelRadio){
						if(currRadio.is(":checked")){
							currRadio.removeAttr("checked");
							currdiv.setRadio(-1);
						}else{
							currdiv.setRadio(index);
							currRadio.prop("checked", true);
						}
					}else{
						currdiv.setRadio(index);
						currRadio.prop("checked", true);
					}
				}
			});
			
			/*currdiv.find(".tablebodyhead"+opts.controlID+" .chk:has(:checkbox)").click(function(){
				var currcheckbox=$(this).find(":checkbox");
				var isChecked=false;
				if(currcheckbox.is(":checked")){
					currdiv.find(".chk :checkbox").removeAttr("checked");
					isChecked=false;
				}else{
					currdiv.find(".chk :checkbox").prop("checked", true);
					isChecked=true;
				}
				//currdiv.setSelectRow(isChecked,currcheckbox.attr("value"));
				currdiv.find(".tablebodydata"+opts.controlID+" .chk :checkbox").each(function(){
					currdiv.setSelectRow(currcheckbox.is(":checked"),$(this).attr("value"));
				});
			})*/
		},
		"setSelectRow":function(isChecked,row){
			if(row==undefined)return false;
			var currdiv=this;
			var opts=this.opts;//selectedRowData
			var currdata=opts.data[row];
			if(isChecked){
				if(opts.primarykey!=null&&opts.selectedData.indexOf(currdata[opts.primarykey])==-1){
					opts.selectedData.push(currdata[opts.primarykey]);
					opts.selectedRowData.push(currdata);
				}
			}else{
				currdiv.find("tr[data-index='"+row+"']").removeClass("selected");
				if(opts.primarykey!=null&&opts.selectedData.indexOf(currdata[opts.primarykey])>-1){
					var curri=-1;
					$.each(opts.selectedData,function(i,item){
						if(item==currdata[opts.primarykey])curri=i;
					});
					opts.selectedData.splice(curri, 1);
					opts.selectedRowData.splice(curri, 1);
				}
			}

			currdiv.setCheckBox();
			
			var newselectedrow=currdiv.getCheckedData();
			
			//if(opts.GetSelectedData!=undefined)
			//	opts.GetSelectedData(newselectedrow);
		},
		//对点击的行遍历
		"clickRowEach":function(index,isChecked){
			var currdiv=this;
			var opts=this.opts;
			 var currcheckbox=currdiv.find("tr[data-index='"+index+"'] .chk :checkbox");
			currcheckbox.prop("checked", isChecked);
			currdiv.setSelectRow(isChecked,currcheckbox.attr("value"));
		},
		//数据空的时候
		"dataIsNull":function() {
			var currdiv=this;
			var opts=this.opts;
			currdiv.find(".tablebodydata"+opts.controlID).remove();
			var count=(currdiv.find(".tablebodyhead"+opts.controlID).find("th").length) ;
			var strtd="<table class='tablestyle4 tablebodydata"+opts.controlID+"'>"
			strtd+=("<tr class='datanull'><td class='datanull' colspan='" + (count) + "'>"+opts.dataisempty+"</td></tr></table>");
			currdiv.append(strtd);
		},
		//结束
		"loadcomplete":function(){
			var opts=this.opts;
			if(opts.loadcomplete!=undefined)
				opts.loadcomplete(opts.data);
		},
		"formartNullString":function(str) {
			if(str != undefined || str == 0) return str;
			return (str == undefined || str == null || str == "") ? "--" : str;
		},
		//时间戳
		"formartTimestamp":function(t) {
			if (t == undefined || t == null || t == "") return "--";
			var ndate = new Date();
			ndate.setTime(t);
			var m=(ndate.getMonth() + 1);
			var d=ndate.getDate();
			m=(m.toString().length==1)?("0"+m.toString()):m;
			d=(d.toString().length==1)?("0"+d.toString()):d;
			return ndate.getFullYear() + "-" + m + "-" + d;
		},
		//排序
		"sort":function(){
			var currdiv=this;
			var opts=this.opts;
			if(opts.controlID==undefined||opts.controlID==null)
				opts.controlID=currdiv.data("controlID");
			$(".tablebodyhead"+opts.controlID+" th div[col] a").click(function(){
				var index=parseInt($(this).attr("data-value"));
				var currcol = opts.columns[index];
				var ordersort=currdiv.data("ordersort");
				
				if(ordersort==undefined||ordersort==null){
					ordersort={order:currcol.col,sort:'desc'};
				}else{
					ordersort.order=currcol.col;
					if(ordersort.sort=='desc')
						ordersort.sort='asc';
					else
						ordersort.sort='desc';
				}
				currdiv.data("ordersort",ordersort);
				
				$(".tablebodyhead"+opts.controlID+" th div[col] a span").remove();
				if(ordersort.sort=='desc')
					$(this).append("<span class='sort'>&nbsp;∇</span>");
				else
					$(this).append("<span class='sort'>&nbsp;∆</span>");
				if(opts.csort!=undefined)
					opts.csort(ordersort.order,ordersort.sort);
				$(this).blur();
			});
		},
		//加载数据 ;
		"showdata":function(data){
			var currdiv=this;
			var opts=this.opts;
			if(opts.isClearChecked)
				currdiv.clearCheckedData();
			var isReSetKeyField=false;
			if(opts.data==undefined||opts.data==null||opts.data.length==0){
				currdiv.data("hasheader",false);
				isReSetKeyField=true;
			}
			opts.data=data;
			if(isReSetKeyField){
				//this.setKeyField();
			}
			currdiv.initframe();
			currdiv.initgrid();
		},
		//隐藏或显示列数据
		"displaycolumn":function(numbers,isShow){
			var currdiv=this;
			var opts=this.opts;
			if(opts.controlID==undefined||opts.controlID==null)
				opts.controlID=currdiv.data("controlID");
			var m=$(".tablemenu"+opts.controlID);
			var t=$(".wapper"+opts.controlID);
			var columnnumbers=[];
			if(Array.isArray(numbers)){
				columnnumbers=numbers;
			}else{
				columnnumbers.push(numbers);
			}
			$.each(columnnumbers,function(i,item){
				if(isShow){
					m.find(".tablemenu[colnum='"+item+"']").show().find(":checkbox").prop("checked", true);
					t.find("th[colnum='"+item+"'],td[colnum='"+item+"']").show();
				}else{
					m.find(".tablemenu[colnum='"+item+"']").hide().find(":checkbox").removeAttr("checked");
					t.find("th[colnum='"+item+"'],td[colnum='"+item+"']").hide();
				}
			});
		},
		//隐藏或显示行数据
		"displayrow":function(numbers,isShow){
			var currdiv=this;
			var opts=this.opts;
			if(opts.controlID==undefined||opts.controlID==null)
				opts.controlID=currdiv.data("controlID");
			var t=$(".wapper"+opts.controlID);
			var columnnumbers=[];
			if(Array.isArray(numbers)){
				columnnumbers=numbers;
			}else{
				columnnumbers.push(numbers);
			}
			$.each(columnnumbers,function(i,item){
				if(isShow){
					t.find("tr[data-index='"+item+"']").show();
				}else{
					t.find("tr[data-index='"+item+"']").hide();
				}
			});
			if(opts.rownumbers){
				$.each(t.find("td .num"),function(i,item){
					$(this).html(i+1);
				});
			}
		},
		//设置星数据
		"setStardata":function(data){
			this.opts.star=data;
		},
		//设置复选框数据
		"setCheckboxdata":function(data){
			this.opts.checkbox=data;
		},
		//设置复选框数据
		"setRadiodata":function(data){
			this.opts.radio=data;
		},
		//data：Array，重新设置必填项的数据，因为可能根据状态验证的不同。
		"setRequiredData":function(data){
			if(this.opts.required==undefined){
				this.opts.required={};
				this.opts.required.isAll=true;
			}
			this.opts.required.columns=data;
		},
		//验证数据是否正确，可提交数据时使用
		"checkData":function(){
			var currdiv=this;
			var opts=this.opts;
			var isChecked=true;
			if(opts.required!=undefined)opts.required.cols=[];
			//文本框的输入验证
			$.each(opts.columns,function(i,item){
				if(item.type!=undefined&&item.type=="text"&&opts.data!=null){
					$.each(opts.data,function(j,jitem){
						var currtext=currdiv.find(".txt"+item.col).eq(j);
						if(currdiv.textCheck(item,currtext,jitem[item.col],jitem)==false)
							isChecked=false;
					});
				}
				if(opts.required!=undefined){
					$.each(opts.required.columns,function(j,jitem){
						var fieldname=jitem;
						if(jitem.indexOf('|')>-1)
							fieldname=fieldname.split('|')[0];
						if(item.col==fieldname){
							opts.required.cols.push({"name":jitem,"index":i});
						}
					});
				}
				
			});
			
			//必填项的验证
			//opts.required.columns 必填项的字段
			//opts.required.isAll 验证必填项字段的类型，是否是所有的行都要填写， 还是允许只填写一行。
			if(opts.required!=undefined){
				var errordata=[];
				//currdiv.find(".warningbg").removeClass("warningbg");
				$.each(opts.data,function(i,item){
					var ed=[],err=0;
					$.each(opts.required.cols,function(j,jitem){
						var fieldname=jitem.name,fieldvalue=""; 
						//是否有默认值
						if(fieldname.indexOf('|')>-1){
							fieldname=jitem.name.split('|')[0];
							fieldvalue=jitem.name.split('|')[1];
						}
						
						var objisnull=true;
						//如果有默认值，那么等于默认值也是不对的
						if(item[fieldname]==fieldvalue||currdiv.isNullObject(item[fieldname])){
							err++;
							objisnull=false;
						}
						
						ed.push({"x":i,"y":jitem.index,"v":objisnull});
					});
					var rowdata={};
					var iserr=0;
					if(err==0)iserr=1;//说明没有空值
					else if(err==opts.required.cols.length)iserr=0;//说明全是空值
					else iserr=-1;//说明是不全数据
					rowdata.row=ed;
					rowdata.result=iserr;
					errordata.push(rowdata);
				});

				$.each(errordata,function(i,item){
					if(item.result==-1){
						$.each(item.row,function(j,jitem){
							if(jitem.v==false){
								var td=currdiv.find("tr[data-index='"+jitem.x+"'] td[colnum='"+jitem.y+"']")
								//td.addClass("warningbg");
								currdiv.layersMoretips("必填",td,true);
							}
						});
					}
				});
				
				//允许只填写一行时
				if(opts.required.isAll==false){
					var j=0;
					$.each(errordata,function(i,item){
						if(item.result==1){
							j++;
						}
					});
					if(j==0)
						isChecked=false;
				}
				
				//当所有时
				if(opts.required.isAll==true){
					var j=0;
					$.each(errordata,function(i,item){
						if(item.result==1){
							j++;
						}
						if(item.result==0){
							$.each(item.row,function(j,jitem){
								var td=currdiv.find("tr[data-index='"+jitem.x+"'] td[colnum='"+jitem.y+"']")
								//td.addClass("warningbg");
								currdiv.layersMoretips("必填",td,true);
							});
						}
					});
					if(j!=errordata.length)
						isChecked=false;
				}
				
				//当没设置或为空时，可以不填写
				if(opts.required.isAll==undefined||opts.required.isAll==null){
					
				}
			}
			
			return isChecked;
		},
		//取数据
		"resultData":function(){
			var currdiv=this;
			var opts=this.opts;
			
			var newresultrow=[],keyfieldvalue=0;
			$.each(opts.data,function(i,item){
				var curroptdata=item;
				var obj={};
				if(opts.resultrow!=undefined){
					var stringisnull=false;
					if(opts.resultrow.args!=undefined){
						$.each(opts.resultrow.args,function(j,jitem){
							var fieldname=jitem,fieldvalue=jitem;
							//别名的处理
							if(jitem.indexOf('|')>-1){
								fieldname=jitem.split('|')[1];
								fieldvalue=jitem.split('|')[0];
							}
							//这一行数据，是否有空值
							if(currdiv.isNullObject(curroptdata[fieldvalue]))
								stringisnull=true;
							obj[fieldname]=curroptdata[fieldvalue];
						});
					}
					//是否要数据的下标
					if(opts.resultrow.isHasindex!=undefined&&opts.resultrow.isHasindex){
						obj.index=i;
					}
					//如果不要有空值的时候
					//注：如果使用批次这类数据时，单独建立起来一个字段，当给批次赋值时，给这个字段赋值不为空字符串即可解决
					if(opts.resultrow.isUseNullValue==false&&stringisnull==true)
						return true;
					keyfieldvalue++;
					if(!currdiv.isNullObject(opts.keyfield))
						obj[opts.keyfield]=(keyfieldvalue);
					newresultrow.push(obj);
				}else{
					if(!currdiv.isNullObject(opts.keyfield))
						curroptdata[opts.keyfield]=(i+1);
					newresultrow.push(curroptdata);
				}
			});
			
			return newresultrow;
		},
		//一个对象是否为空，如果为空返回True
		"isNullObject":function(obj){
			if(obj==undefined||obj==null||(Array.isArray(obj)&&obj.length==0)||obj=="")
				return true;
			else
				return false;
		},
		// 数据排序时用。
		"compare":function (obj1, obj2) {
			var val1 = obj1[opts.keyfield];
			var val2 = obj2[opts.keyfield];
			if (val1 < val2) {
				return -1;
			} else if (val1 > val2) {
				return 1;
			} else {
				return 0;
			}
		},
		//设置KeyField
		"setKeyField":function(fieldname){
			/*var currdiv=this;
			var opts=this.opts;
			if(fieldname!=undefined){
				opts.keyfield=fieldname;
				return false;
			}
			if(currdiv.isNullObject(opts.keyfield)&&!currdiv.isNullObject(opts.data)){
				var indexname="",ridname="";
				
				for(let fieldname in opts.data[0]){
					//存在display_index
					if(fieldname=="display_index"&&currdiv.isNullObject(indexname)){
						indexname=fieldname;
					}
					//存在_rid结尾
					if(fieldname.lastIndexOf('_rid')==(fieldname.length-4)&&currdiv.isNullObject(ridname)){
						ridname=fieldname;
					}
				}
				if(currdiv.isNullObject(indexname))
					opts.keyfield=indexname;
				else if(currdiv.isNullObject(ridname))
					opts.keyfield=ridname;
				else
					opts.keyfield=null;
			}*/
		},
		//Title的提示显示
		"showRichTextOrTitie":function(){
			var currdiv=this;
			var opts=this.opts;
			//多文本的显示。
			$(".colrichtext .glyphicon").hover(function(){
				var currnode=$(this).parent().parent();
				var indexcolnum=parseInt(currnode.attr("colnum"));
				var indexrownum=parseInt(currnode.parent().attr("data-index"));
				var curroptdata=opts.data[indexrownum];
				var currcol=opts.columns[indexcolnum]; 
				if(curroptdata[currcol.rich]==undefined)
					currdiv.layersMoretips(currcol.rich,$(this));
				else
					currdiv.layersMoretips(curroptdata[currcol.rich],$(this));
			});
			
			//Title显示
			$("[source-title]").hover(function(){
				currdiv.layersMoretips($(this).attr("source-title"),$(this));
			});
		},
		//设置当前页面的页码
		"setPageIndex":function(index){
			index=index==undefined?1:index;
			var opts=this.opts;
			opts.pageindex=index;
		},
		//设置当前每个页面的数据的条数
		"setPageSize":function(index){
			index=index==undefined?1:index;
			var opts=this.opts;
			opts.pagesize=index;
		},
		//设置选择行的数据
		"setSelectedData":function(arraydata){
			var opts=this.opts;
			opts.selectedData=arraydata;
		},
		//根据数据反选CheckBox
		"setCheckBox":function(){
			var currdiv=this;
			var opts=this.opts;
			if(opts.primarykey==null||opts.selectedData.length==0){
				if(opts.GetSelectedData!=undefined)
				opts.GetSelectedData([]);
				return false;
			}
			var seldata=[];
	
			this.find(".wapperright .body table tr").each(function(){
				var i=$(this).attr("data-index");
				if(opts.selectedData.indexOf(opts.data[i][opts.primarykey])>-1){
					currdiv.find("tr[data-index='"+i+"']").addClass("selected");
					currdiv.find("tr[data-index='"+i+"'] .chk :checkbox").attr("checked","checked");
					var obj={};
					if(opts.selectedrow!=null){
						if(opts.selectedrow.args!=undefined){
							$.each(opts.selectedrow.args,function(j,jitem){
								obj[jitem]=opts.data[i][jitem];
							});
						}
						if(opts.selectedrow.isHasindex!=undefined&&opts.selectedrow.isHasindex){
							obj.index=i;
						}
						seldata.push(obj);
					}else{
						seldata.push(opts.data[i]);
					}
				}
			});
			
			if(opts.GetSelectedData!=undefined)
				opts.GetSelectedData(seldata);
		},
		//根据数据反选Radio
		"setRadio":function(i){
			var currdiv=this;
			var opts=this.opts;
			currdiv.find("tr").removeClass("selected");
			currdiv.find("tr[data-index='"+i+"']").addClass("selected");
			
			if(i>-1){
				if(opts.GetRadioData!=undefined)
					opts.GetRadioData(opts.data[i],i);
			}else{
				if(opts.GetRadioData!=undefined)
					opts.GetRadioData([],i);
			}
		},
		//获得Radio所选的数据
		"getSelectedRadio":function(){
			var currdiv=this;
			var opts=this.opts;
			if(!currdiv.isNullObject(currdiv.radiovalue)){
				return opts.data[currdiv.radiovalue];
			}
		},
		//根据数据反选Radio
		"setSelectedRadio":function(a,b){
			var currdiv=this;
			var opts=this.opts;
			if(typeof a!="string"&&b==undefined){
				currdiv.find("tr").removeClass("selected");
				currdiv.find("tr .rdo :radio").removeAttr("checked");
				currdiv.find("tr[data-index='"+a+"']").addClass("selected");
				currdiv.find("tr[data-index='"+a+"'] .rdo :radio").prop("checked", true);
				if(opts.GetRadioData!=undefined)
					opts.GetRadioData(opts.data[a]);
			}else{
				var index=-1;
				$.each(opts.data,function(i,item){
					if(item[a]==b){
						index=i;
						return false;
					}
				});
				if(index>-1){
					currdiv.find("tr").removeClass("selected");
					currdiv.find("tr .rdo :radio").removeAttr("checked");
					currdiv.find("tr[data-index='"+index+"']").addClass("selected");
					currdiv.find("tr[data-index='"+index+"'] .rdo :radio").prop("checked", true);
					if(opts.GetRadioData!=undefined)
						opts.GetRadioData(opts.data[index]);
				}
			}

		},
		//直接取选中的数据。
		"getCheckedData":function(){
			var currdiv=this;
			var opts=this.opts;
			var newselectedrow=[];
			$.each(opts.selectedRowData,function(i,item){
				var obj={};
				if(opts.selectedrow!=null){
					if(opts.selectedrow.args!=undefined){
						$.each(opts.selectedrow.args,function(j,jitem){
							obj[jitem]=item[jitem];
						});
					}
					if(opts.selectedrow.isHasindex!=undefined&&opts.selectedrow.isHasindex){
						obj.index=i;
					}
					newselectedrow.push(obj);
				}else{
					newselectedrow.push(item);
				}
			});
			return newselectedrow;
		},
		//清空选择的数据
		"clearCheckedData":function(){
			var currdiv=this;
			var opts=this.opts;
			opts.selectedData=[];
			opts.selectedRowData=[];
		},
		//整理一下数据，是否有分组的列，暂时只能支持有一列分组
		"makeGroup":function(){
			var currdiv=this;
			var opts=this.opts;
			if(this.isNullObject(opts.data)) return false;
			currdiv.isHasGroup=false;
			currdiv.groupName="";
			currdiv.groupData=[];
			$.each(opts.columns,function(i,item){
				if(item.type=="group"){
					currdiv.isHasGroup=true;
					currdiv.groupName=item.col;
					return false;
				}
			});
			if(currdiv.isHasGroup){
				$.each(opts.data,function(i,item){
					if(currdiv.groupData.indexOf(item[currdiv.groupName])==-1)
						currdiv.groupData.push(item[currdiv.groupName]);
				});
			}
		},
		//执行合并表格
		"executeGroup":function(){
			var currdiv=this;
			var opts=this.opts;
			if(this.isHasGroup){
				$("head").append("<style>table.tablestyle4 tr:nth-child(even){background:#fff;}table.tablestyle4 tr:nth-child(even).hover{background-color:#ddd;}</style>")
				var groupsbg=[];
				$.each(this.groupData,function(i,item){
					groupbg=[];
					currdiv.find("div.colgroup[data-group='"+item+"']").parents("tr").each(function(j,jitem){
						if($(this).attr("data-index")==undefined) return true;
						groupbg.push(parseInt($(this).attr("data-index")));
					});
					groupsbg.push(groupbg);
					var group= currdiv.find("div.colgroup[data-group='"+item+"']");
					var grouplength=group.length;
					currdiv.find("div.colgroup[data-group='"+item+"']:gt(0)").parent().remove();
					group.eq(0).parent().attr("rowspan",grouplength);
					if(i%2==0)
						group.eq(0).height(35*grouplength).css({"line-height":((35*grouplength)+"px"),"background":"#fff"});
					else
						group.eq(0).height(35*grouplength).css({"line-height":((35*grouplength)+"px"),"background":"#f2f2f2"});
					
					//currdiv.find(".chk")
				});
				$.each(groupsbg,function(i,item){
					$.each(item,function(j,jitem){
						if(i%2==1)
							currdiv.find("tr[data-index='"+jitem+"']").css({"background":"#f2f2f2"});
					});
				});
				currdiv.find("td[rowspan]").each(function(){
					console.log($(this).attr("rowspan"));
				});
			}
		},
		//当按下键盘时，对一些功能键做标记
		"lockeypress":function(){
			var currdiv=this;
			var opts=this.opts;
			
			$(window).keydown(function(e){
				if(opts.isUseMultiSelected&&e.which==opts.multiSelectedKeyCode){//按下多选键时
					$("body").addClass("noselect");
					currdiv.opts.isMultiSelected=true;
				}
				if(opts.isUseMultiCancel&&e.which==opts.multiCancelKeyCode){//按下取消多选键时
					$("body").addClass("noselect");
					currdiv.opts.isMultiCancel=true;
				}
			}).keyup(function(e){
				if(opts.isUseMultiSelected&&e.which==opts.multiSelectedKeyCode){//弹起多选键时
					$("body").removeClass("noselect");
					currdiv.opts.isMultiSelected=false;
				}
				if(opts.isUseMultiCancel&&e.which==opts.multiCancelKeyCode){//弹起取消多选键时
					$("body").removeClass("noselect");
					currdiv.opts.isMultiCancel=false;
				}
			});
		},
		//根本类型格式化文本
		"formatText":function(txt,type){
			txt=txt.toString();
			switch(type){
				case 0:{//数字的，加千位符
					var fomarttext1="",fomarttext2="";
					if(txt.indexOf('.')>-1){
						fomarttext1=txt.split('.')[0];
						fomarttext2=txt.split('.')[1];
					}else
						fomarttext1=txt;
					fomarttext1=(fomarttext1 || 0).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
					if(fomarttext2=="")
						txt=fomarttext1;
					else
						txt=fomarttext1+"."+fomarttext2;	
					break;
				}
				case 1:{break;}
				default:{}
			}
			
			return txt;
		}
	});
	
	//默认参数
	var defaluts = {
		rownumbers:true,//是否需要行号
		fixedhead:false,//默认不固定头。
		fixedcolumn:false,//默认不固定列
		resizehead:false,//是否重置列头
		selectedrow:null,//选中的数据列
		isHideCheckbox:false,//是否隐藏Checkbox列，默认不隐藏，作用：可以选择行，但checkbox是否显示。
		isHideRadio:false,//是否隐藏Radio列，默认不隐藏，作用：可以选择行，但Radio是否显示。
		mb_YesNoCancelRadio:false,//是否可以取消Radio的选中，默认不能取消选中，MB_YESNOCANCEL一词选自网络。
		dataisempty:"没有可显示的数据",//数据为空的时候的显示文字
		keyfield:null,//生成HTML前，是否对数据排序
		sortable:true,//是否在列头，可以点击排序
		pageindex:1,//页码
		pagesize:10,//页面的条数
		primarykey:null,//主键字段，如果不设置，给按排一个假的字段名 privatefixedcode
		selectedData:[],//按关键字选中的数据
		selectedRowData:[],//选择的行数据，根据 属性，selectedrow
		isClearChecked:true,//重新加载数据是否清空之前所选的数据，默认是
		absolutelyWidth:false,//百分之百的宽度
		isMultiSelected:false,//是否点击了多选键
		isMultiCancel:false,//是否点击了多条取消键
		clickRowIndex:-1,//多选或多取消时使用
		multiSelectedKeyCode:16,//多选键，默认为16，Shift的AscII值
		multiCancelKeyCode:17,//多取消键，默认为17，Ctrl的AscII值
		isUseMultiSelected:false,//是否启用多选，默认不启用
		isUseMultiCancel:false,//是否启用多取消，默认不启用
		valueSplitSign:'-',//设置Value取值时的中间间隔标记符号，默认为"-"
		disabled:false//如果启用，那么全都是普通文本显示，默认不禁用
	};
})(window.jQuery);