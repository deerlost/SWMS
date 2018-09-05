package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.biz.ITurnoverService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/turnover")
public class TurnoverController {
	private static final Logger logger = LoggerFactory.getLogger(TurnoverController.class);

	@Autowired
	private ITurnoverService turnoverService;

	@Autowired
	private ServerConfig constantConfig;

	@Autowired
	private IStockQueryService stockQueryService;

	@Autowired
	private IDicWarehouseService dicWarehouseService;

	@RequestMapping(value = "/report", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject report(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		JSONObject body = new JSONObject();
		try {
			String boardId = json.getString("board_id");// 板块id
			String ftyId = UtilObject.getStringOrEmpty(json.get("fty_id"));// 工厂id
			String locationId = null;
			if (json.containsKey("location_id")) {
				locationId = UtilObject.getStringOrEmpty(json.get("location_id"));// 库存地点id
			}
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}

			String matGroupId = UtilObject.getStringOrEmpty(json.get("mat_group_id"));// 物料组编码

			String timeFr = json.getString("time_fr");// 过账日期起
			String timeTo = json.getString("time_to");// 过账日期止

			JSONArray resultAry = turnoverService.getReportDetail(boardId, ftyId, whId, matGroupId, timeFr, timeTo);

			JSONArray chartInfoAry = new JSONArray();
			// 平均存货金额,期初库存金额,本期入库金额,本期出库金额,期末库存金额,存货周转天数,存货周转次数
			JSONObject chartObject1 = new JSONObject();
			chartObject1.put("name", "平均存货金额");
			chartObject1.put("type", "0");
			chartObject1.put("dependence", "0");
			chartInfoAry.add(chartObject1);

			JSONObject chartObject2 = new JSONObject();
			chartObject2.put("name", "期初库存金额");
			chartObject2.put("type", "0");
			chartObject2.put("dependence", "0");
			chartInfoAry.add(chartObject2);

			JSONObject chartObject3 = new JSONObject();
			chartObject3.put("name", "本期入库金额");
			chartObject3.put("type", "0");
			chartObject3.put("dependence", "0");
			chartInfoAry.add(chartObject3);

			JSONObject chartObject4 = new JSONObject();
			chartObject4.put("name", "本期出库金额");
			chartObject4.put("type", "0");
			chartObject4.put("dependence", "0");
			chartInfoAry.add(chartObject4);

			JSONObject chartObject5 = new JSONObject();
			chartObject5.put("name", "期末库存金额");
			chartObject5.put("type", "0");
			chartObject5.put("dependence", "0");
			chartInfoAry.add(chartObject5);

			JSONObject chartObject6 = new JSONObject();
			chartObject6.put("name", "存货周转天数");
			chartObject6.put("type", "1");
			chartObject6.put("dependence", "1");
			chartInfoAry.add(chartObject6);

			JSONObject chartObject7 = new JSONObject();
			chartObject7.put("name", "存货周转次数");
			chartObject7.put("type", "1");
			chartObject7.put("dependence", "2");
			chartInfoAry.add(chartObject7);

			// 平均存货金额,期初库存金额,本期入库金额,本期出库金额,期末库存金额,存货周转天数,存货周转次数
			JSONArray xAxisAry = new JSONArray();
			List<BigDecimal> leftList = new ArrayList<BigDecimal>();
			List<BigDecimal> rightList1 = new ArrayList<BigDecimal>();
			List<BigDecimal> rightList2 = new ArrayList<BigDecimal>();

			BigDecimal tenThousand = new BigDecimal(10000);

			for (Object object : resultAry) {
				JSONObject xAxisObj = (JSONObject) object;
				leftList.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("finalMoney")));
				leftList.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("outnputMoney")));
				leftList.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("inputMoney")));
				leftList.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("beginMoney")));
				leftList.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("averageMoney")));
				rightList1.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("days")));// 1天数
				rightList2.add(UtilObject.getBigDecimalOrZero(xAxisObj.get("times")));// 2次数
				JSONArray xAry = new JSONArray();
				JSONObject obj = new JSONObject();
				JSONObject innerObj1 = new JSONObject();
				innerObj1.put("yAxisValue",
						UtilJSON.getBigDecimalFromJSON("averageMoney", xAxisObj).divide(tenThousand).toString());
				xAry.add(innerObj1);
				JSONObject innerObj2 = new JSONObject();
				innerObj2.put("yAxisValue",
						UtilJSON.getBigDecimalFromJSON("beginMoney", xAxisObj).divide(tenThousand).toString());
				xAry.add(innerObj2);
				JSONObject innerObj3 = new JSONObject();
				innerObj3.put("yAxisValue",
						UtilJSON.getBigDecimalFromJSON("inputMoney", xAxisObj).divide(tenThousand).toString());
				xAry.add(innerObj3);
				JSONObject innerObj4 = new JSONObject();
				innerObj4.put("yAxisValue",
						UtilJSON.getBigDecimalFromJSON("outnputMoney", xAxisObj).divide(tenThousand).toString());
				xAry.add(innerObj4);
				JSONObject innerObj5 = new JSONObject();
				innerObj5.put("yAxisValue",
						UtilJSON.getBigDecimalFromJSON("finalMoney", xAxisObj).divide(tenThousand).toString());
				xAry.add(innerObj5);
				JSONObject innerObj6 = new JSONObject();
				innerObj6.put("yAxisValue", xAxisObj.get("days"));
				xAry.add(innerObj6);
				JSONObject innerObj7 = new JSONObject();
				innerObj7.put("yAxisValue", xAxisObj.get("times"));
				xAry.add(innerObj7);
				obj.put("xAxisName", xAxisObj.get("mat_group_name"));
				obj.put("yAxisValueList", xAry);
				xAxisAry.add(obj);
			}
			BigDecimal leftMax = BigDecimal.ZERO;
			BigDecimal rightMax1 = BigDecimal.ZERO;
			BigDecimal rightMax2 = BigDecimal.ZERO;
			for (int i = 0; i < leftList.size(); i++) {
				if (leftMax.compareTo(leftList.get(i)) == -1) {
					leftMax = leftList.get(i);
				}
			}
			for (int i = 0; i < rightList1.size(); i++) {
				if (rightMax1.compareTo(rightList1.get(i)) == -1) {
					rightMax1 = rightList1.get(i);
				}
			}
			for (int i = 0; i < rightList2.size(); i++) {
				if (rightMax2.compareTo(rightList2.get(i)) == -1) {
					rightMax2 = rightList2.get(i);
				}
			}

			body.put("leftAxisMaxValue", leftMax.divide(tenThousand).toString());
			body.put("rightAxisMaxValue1", rightMax1.toString());// 天数
			body.put("rightAxisMaxValue2", rightMax2.toString());// 次数
			body.put("leftAxisUnit", "万元");
			body.put("rightAxisUnit1", "天");
			body.put("rightAxisUnit2", "次");
			body.put("title", "存货周转率");
			body.put("chartInfoList", chartInfoAry);
			body.put("xAxisList", xAxisAry);

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, "", body);
	}

	// 根据板块得到公司、工厂、库存地点联动
	@RequestMapping(value = "/select_info_data", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object selectInfoData(String board_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		JSONObject body = new JSONObject();
		try {
			JSONArray allBody = new JSONArray();

			int defaultCorpId = cUser.getCorpId();
			int defaultFtyId = cUser.getFtyId();
			String defaultLocationId = "";

			List<String> locationList = cUser.getLocationList();
			if (locationList != null && locationList.size() > 0) {
				String[] lgortStr = locationList.get(0).split("-");
				defaultLocationId = lgortStr[1];
			} else {
				defaultLocationId = "";
			}

			allBody = turnoverService.selectInfoData(Integer.parseInt(board_id));

			body.put("deaultBoardId", cUser.getBoardId());
			body.put("defaultCorpId", defaultCorpId);
			body.put("defaultFtyId", defaultFtyId);
			body.put("defaultLocationId", defaultLocationId);
			// 计算当前时间的上一个月的月初日期和月末日期
			Calendar calendar1 = Calendar.getInstance();
			calendar1.add(Calendar.MONTH, -1);
			calendar1.set(Calendar.DAY_OF_MONTH, 1);
			body.put("timeFr", calendar1.getTime());

			Calendar calendar2 = Calendar.getInstance();
			calendar2.set(Calendar.DAY_OF_MONTH, 0);
			body.put("timeTo", calendar2.getTime());

			body.put("all", allBody);

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, body);

	}

	// 获取物料组描述下拉
	@RequestMapping(value = "/select_mat_group", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object selectMatGroup(int fty_id, String wh_id) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ftyId", fty_id);
			map.put("whId", UtilObject.getIntegerOrNull(wh_id));

			resultList = turnoverService.selectMatGroup(map);
		} catch (Exception e) {
			logger.error("", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}

		return UtilResult.getResultToJson(status, error_code, resultList);
	}

	@RequestMapping(value = "/download_turnover", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadTurnover(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			String boardId = request.getParameter("board_id");// 板块id
			String ftyId = UtilObject.getStringOrEmpty(request.getParameter("fty_id"));// 工厂id
			String whId = UtilObject.getStringOrEmpty(request.getParameter("wh_id"));// 库存地点id
			String matGroupId = UtilObject.getStringOrEmpty(request.getParameter("mat_group_id"));// 物料组编码

			String timeFr = request.getParameter("time_fr");// 过账日期起
			String timeTo = request.getParameter("time_to");// 过账日期止
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("boardId", boardId);
			param.put("ftyId", ftyId);
			param.put("whId", whId);
			param.put("matGroupId", matGroupId);
			param.put("timeFr", timeFr);
			param.put("timeTo", timeTo);
			list = turnoverService.downloadTurnover(param);

			// g.mat_group_code,g.mat_group_name,db.board_name, cp.corp_name,
			// l.location_name,dt.move_type_code, m.mat_code,m.mat_name,
			// i.mat_doc_code,i.mat_doc_rid,rt.receipt_type_name,
			// u.name_zh,i.output_qty
			// qty,price.price,DATE_FORMAT(i.create_time,'%Y-%m-%d')
			// create_time,wu.user_name,(i.output_qty*price.price) money

			// 物料组 物料组描述 板块 工厂名称 库存地点 移动类型 物料编码 物料描述 特殊库存代码 物料凭证 物料凭证行项目 仓储单据类型
			// 计量单位 数量 金额 创建时间 过账日期 创建人 金额

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();
			relationMap.put("mat_group_code", "物料组");
			relationMap.put("mat_group_name", "物料组描述");
			relationMap.put("board_name", "板块");
			relationMap.put("corp_name", "工厂名称");//
			relationMap.put("wh_name", "仓库号");
			relationMap.put("move_type_code", "移动类型");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("mat_doc_code", "物料凭证");
			relationMap.put("mat_doc_rid", "物料凭证行项目");
			relationMap.put("receipt_type_name", "仓储单据类型");
			relationMap.put("name_zh", "计量单位");
			relationMap.put("qty", "数量");
			relationMap.put("price", "单价");
			relationMap.put("create_time", "创建时间");
			relationMap.put("posting_date", "过账日期");
			relationMap.put("user_name", "创建人");
			relationMap.put("money", "金额");

			List<String> orderList = new ArrayList<String>();
			orderList.add("物料组");
			orderList.add("物料组描述");
			orderList.add("板块");
			orderList.add("工厂名称");
			orderList.add("仓库号");
			orderList.add("移动类型");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("物料凭证");
			orderList.add("物料凭证行项目");
			orderList.add("仓储单据类型");
			orderList.add("计量单位");
			orderList.add("数量");
			orderList.add("单价");
			orderList.add("创建时间");
			orderList.add("过账日期");
			orderList.add("创建人");
			orderList.add("金额");

			String root = constantConfig.getTempFilePath();

			String download_file_name = "turnOver";// 存货周转率

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, list, relationMap, orderList, root);

			// filePath=UtilExcel.getExcelRopertUrl("overstock1", list,
			// relationMap, orderList, root);
			fileInServer = new File(filePath);
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(fileInServer));

			// 转码，免得文件名中文乱码
			String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
			// 设置文件下载头
			response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			while ((len = bis.read()) != -1) {
				out.write(len);
				out.flush();
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 查询板块工厂库存地点
	 * 
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_base_info1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getBaseInfo(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			returnMap = stockQueryService.getBaseInfo(cUser);
			returnMap.put("fty_id", cUser.getLocationList().get(0).split("-")[0]);
			returnMap.put("location_id", cUser.getLocationList().get(0).split("-")[1]);

			List<Map<String, Object>> wareHouseList = dicWarehouseService.queryWarehouseList();
			returnMap.put("ware_house_list", wareHouseList);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DATE, 1);
			returnMap.put("time_fr", sdf.format(cal.getTime()));
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			returnMap.put("time_to", sdf.format(cal.getTime()));
		} catch (Exception e) {
			logger.error("板块工厂库存地点查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code, returnMap);
	}

	// 获取物料组描述下拉
	@RequestMapping(value = "/over_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object overView(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}
			String date = json.getString("date");// 日期
			int type = json.getInt("type");
			returnMap = turnoverService.getOverview(boardId, corpId, whId, date, type);

		} catch (WMSException e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		return UtilResult.getResultToJson(status, error_code, error_string, returnMap);
	}

	@RequestMapping(value = "/avg_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object avgView(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}
			String date = json.getString("date");// 日期
			int type = json.getInt("type");
			returnMap = turnoverService.getAvgview(boardId, corpId, whId, date, type, 4);

		} catch (WMSException e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		return UtilResult.getResultToJson(status, error_code, error_string, returnMap);
	}

	@RequestMapping(value = "/mat_group_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object matGroupView(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}
		//	String matGroupId = UtilObject.getStringOrEmpty(json.get("mat_group_id"));
			String date = json.getString("date");// 日期
			int type = json.getInt("type");
			returnList = turnoverService.getWhAndMatGroupView(boardId, corpId, whId, date, type, null, false);
			if(returnList!=null&&returnList.size()>6){
				returnList = returnList.subList(0, 6);
			}
		} catch (WMSException e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		return UtilResult.getResultToJson(status, error_code, error_string, returnList);
	}

	@RequestMapping(value = "/mat_view", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object matView(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String error_string = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		boolean status = true;

		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");// 板块id
			String corpId = json.getString("corp_id");// 板块id
			String whId = null;
			if (json.containsKey("wh_id")) {
				whId = UtilObject.getStringOrEmpty(json.get("wh_id"));
			}
			String date = json.getString("date");// 日期
			int type = json.getInt("type");
			int showType = json.getInt("show_type");
			String matGroupId = UtilObject.getStringOrEmpty(json.get("mat_group_id"));
			returnMap = turnoverService.getMatView(boardId, corpId, whId, date, type, 3, matGroupId, showType);
		} catch (WMSException e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = e.getErrorCode();
			error_string = e.getMessage();
		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			error_string = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
		}

		return UtilResult.getResultToJson(status, error_code, error_string, returnMap);
	}
}
