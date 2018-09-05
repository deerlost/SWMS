package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumMatReqStatus;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.dic.IDicReceiverService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 领料单查询数据传输控制类
 * 
 * @author 刘宇 2018.02.01
 *
 */
@Controller
@RequestMapping("/biz/matreqquery")
public class MatReqQueryController {
	private static final Logger logger = LoggerFactory.getLogger(MatReqQueryController.class);
	@Autowired
	private IMatReqService matReqService;

	@Autowired
	private ServerConfig constantConfig;

	@Resource
	private IDicReceiverService dicReceiverService;

	/**
	 * 领料单查询
	 * 
	 * @param 刘宇
	 *            2018.02.06
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_mat_req", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachLLD(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean paging = true;
		JSONArray body = new JSONArray();
		try {
			String matReqFtyId = json.getString("mat_req_fty_id");// 领料工厂
			String matReqMatTypeId = json.getString("mat_req_mat_type_id");// 物料类型
			String receiveFtyId = json.getString("receive_fty_id");// 接收工厂
			// String useDeptName = json.getString("use_dept_name");// 使用部门
			String useDeptCode = json.getString("use_dept_code");// 使用部门code
			String matReqBizTypeId = json.getString("mat_req_biz_type_id");// 业务类型
			String createUser = json.getString("create_user");// 领料申请人 用户id
			String createTimeBegin = json.getString("create_time_begin");// 创建日期开始
			String createTimeEnd = json.getString("create_time_end");// 创建日期开始结束
			String matCode = json.getString("mat_code");// 物料编码
			String locationId = json.getString("location_id");// 库存地点
			String matName = json.getString("mat_name");// 物料描述
			int receiptType = json.getInt("receipt_type");// 领料单类型 receiptType =
															// 33 ,33生产期 34在建期
			int boardId = cUser.getBoardId();// 板块 boardId=2
			String matReqCode = json.getString("mat_req_code");// 领料单code
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}

			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组

			List<Map<String, Object>> bizMatReqHeadMaps = matReqService.listBizMatReqHeadOnPaging(utilMatCodes,
					matReqFtyId, matReqMatTypeId, receiveFtyId, useDeptCode, matReqBizTypeId, createUser,
					createTimeBegin, createTimeEnd, matCode, locationId, matName, matReqCode, receiptType, boardId,
					pageIndex, pageSize, total, paging);
			if (bizMatReqHeadMaps.size() > 0) {
				total = Integer.parseInt(bizMatReqHeadMaps.get(0).get("totalCount").toString());
			}
			for (Map<String, Object> bizMatReqHeadMap : bizMatReqHeadMaps) {
				Integer stats = new Integer((int) bizMatReqHeadMap.get("status"));
				JSONObject bizMatReqHeadJSon = new JSONObject();
				bizMatReqHeadJSon.put("mat_req_id", bizMatReqHeadMap.get("mat_req_id"));// 领料单号
				bizMatReqHeadJSon.put("mat_req_code", bizMatReqHeadMap.get("mat_req_code"));// 领料单编码
				bizMatReqHeadJSon.put("create_time",
						UtilString.getLongStringForDate((Date) bizMatReqHeadMap.get("create_time")));// 创建日期
				bizMatReqHeadJSon.put("user_name", bizMatReqHeadMap.get("user_name"));// 申请人
				bizMatReqHeadJSon.put("receive_materiel_fty", bizMatReqHeadMap.get("receive_materiel_fty"));// 领料工厂
				bizMatReqHeadJSon.put("receive_fty", bizMatReqHeadMap.get("receive_fty"));// 接收工厂
				bizMatReqHeadJSon.put("status_name", EnumMatReqStatus.getNameByValue(stats.byteValue()));// 状态
				bizMatReqHeadJSon.put("mat_req_rid", bizMatReqHeadMap.get("mat_req_rid"));// 单号行号
				bizMatReqHeadJSon.put("mat_code", bizMatReqHeadMap.get("mat_code"));// 物料编码
				bizMatReqHeadJSon.put("mat_name", bizMatReqHeadMap.get("mat_name"));// 物料描述
				bizMatReqHeadJSon.put("name_zh", bizMatReqHeadMap.get("name_zh"));// 计量单位
				bizMatReqHeadJSon.put("demand_qty", bizMatReqHeadMap.get("demand_qty"));// 需求数量
				bizMatReqHeadJSon.put("location_name", bizMatReqHeadMap.get("location_name"));// 库存地点
				bizMatReqHeadJSon.put("take_delivery_qty", bizMatReqHeadMap.get("take_delivery_qty"));// 已出货数量
				bizMatReqHeadJSon.put("not_take_delivery_qty", bizMatReqHeadMap.get("not_take_delivery_qty"));// 未出货数量
				bizMatReqHeadJSon.put("move_type_id", bizMatReqHeadMap.get("move_type_id"));// 移动类型
				bizMatReqHeadJSon.put("move_type_name", bizMatReqHeadMap.get("move_type_name"));// 移动类型描述
				bizMatReqHeadJSon.put("reserve_code", bizMatReqHeadMap.get("reserve_code"));// 预留号
				bizMatReqHeadJSon.put("reserve_rid", bizMatReqHeadMap.get("reserve_rid"));// 预留行项目
				bizMatReqHeadJSon.put("purchase_order_code", bizMatReqHeadMap.get("purchase_order_code"));// 采购订单号
				bizMatReqHeadJSon.put("purchase_order_rid", bizMatReqHeadMap.get("purchase_order_rid"));// 采购订单行号
				bizMatReqHeadJSon.put("use_dept_name", bizMatReqHeadMap.get("use_dept_name"));// 使用部门描述
				bizMatReqHeadJSon.put("mat_type_name", bizMatReqHeadMap.get("mat_type_name"));// 物料类型描述
				bizMatReqHeadJSon.put("biz_type_name", bizMatReqHeadMap.get("biz_type_name"));// 业务类型描述
				bizMatReqHeadJSon.put("cost_obj_name", bizMatReqHeadMap.get("cost_obj_name"));// 成本对象描述
				bizMatReqHeadJSon.put("device_code", bizMatReqHeadMap.get("device_code"));// 设备号
				bizMatReqHeadJSon.put("work_receipt_code", bizMatReqHeadMap.get("work_receipt_code"));// 工单号

				body.add(bizMatReqHeadJSon);
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

	/**
	 * 领料单批量导出
	 * 
	 * @author 刘宇 2018.02.03
	 * @param cUser
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_mat_req", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadLLD(CurrentUser cUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean paging = false;
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ArrayList<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		try {
			String matReqFtyId = request.getParameter("mat_req_fty_id");// 领料工厂
			String locationId = request.getParameter("location_id");// 库存地点
			String matReqMatTypeId = request.getParameter("mat_req_mat_type_id");// 物料类型
			String createUser = request.getParameter("create_user");// 领料申请人 //
																	// 用户id
			String receiveFtyId = request.getParameter("receive_fty_id");// 接收工厂
			// String useDeptName = request.getParameter("use_dept_name");//
			// 使用部门
			String useDeptCode = request.getParameter("use_dept_code");// 使用部门code
			String matReqBizTypeId = request.getParameter("mat_req_biz_type_id");// 业务类型
			String matCode = request.getParameter("mat_code");// 物料编码
			String matName = request.getParameter("mat_name");// 物料描述
			String createTimeBegin = request.getParameter("create_time_begin");// 创建日期开始
			String createTimeEnd = request.getParameter("create_time_end");// 创建日期结束
			int receiptType = Integer.parseInt(request.getParameter("receipt_type"));// 领料单类型
																						// receiptType
																						// =
			// 33 ,33生产期 34在建期
			int boardId = cUser.getBoardId();// 板块 boardId=2
			String matReqCode = request.getParameter("mat_req_code");// 领料单code
			List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组

			list = matReqService.listBizMatReqHeadOnPaging(utilMatCodes, matReqFtyId, matReqMatTypeId, receiveFtyId,
					useDeptCode, matReqBizTypeId, createUser, createTimeBegin, createTimeEnd, matCode, locationId,
					matName, matReqCode, receiptType, boardId, pageIndex, pageSize, total, paging);

			if (list != null && list.size() > 0) {
				for (Map<String, Object> map1 : list) {
					Map<String, Object> mapDown = new HashMap<String, Object>();
					Integer stats = new Integer((int) map1.get("status"));
					mapDown.put("mat_req_id", map1.get("mat_req_id"));// 领料单号
					mapDown.put("create_time", UtilString.getLongStringForDate((Date) map1.get("create_time")));// 创建日期
					mapDown.put("user_name", map1.get("user_name"));// 申请人
					mapDown.put("receive_materiel_fty", map1.get("receive_materiel_fty"));// 领料工厂
					mapDown.put("receive_fty", map1.get("receive_fty"));// 接收工厂
					mapDown.put("status_name", EnumMatReqStatus.getNameByValue(stats.byteValue()));// 状态
					mapDown.put("mat_req_rid", map1.get("mat_req_rid"));// 单号行号
					mapDown.put("mat_code", map1.get("mat_code"));// 物料编码
					mapDown.put("mat_name", map1.get("mat_name"));// 物料描述
					mapDown.put("name_zh", map1.get("name_zh"));// 计量单位
					mapDown.put("demand_qty", map1.get("demand_qty"));// 需求数量
					mapDown.put("location_name", map1.get("location_name"));// 库存地点
					mapDown.put("take_delivery_qty", map1.get("take_delivery_qty"));// 已出货数量
					mapDown.put("not_take_delivery_qty", map1.get("not_take_delivery_qty"));// 未出货数量
					mapDown.put("move_type_id", map1.get("move_type_id"));// 移动类型
					mapDown.put("move_type_name", map1.get("move_type_name"));// 移动类型描述
					mapDown.put("reserve_code", map1.get("reserve_code"));// 预留号
					mapDown.put("reserve_rid", map1.get("reserve_rid"));// 预留行项目
					mapDown.put("purchase_order_code", map1.get("purchase_order_code"));// 采购订单号
					mapDown.put("purchase_order_rid", map1.get("purchase_order_rid"));// 采购订单行号
					mapDown.put("use_dept_name", map1.get("use_dept_name"));// 使用部门描述
					mapDown.put("mat_type_name", map1.get("mat_type_name"));// 物料类型描述
					mapDown.put("biz_type_name", map1.get("biz_type_name"));// 业务类型描述
					mapDown.put("cost_obj_name", map1.get("cost_obj_name"));// 成本对象描述
					mapDown.put("device_code", map1.get("device_code"));// 设备号
					mapDown.put("work_receipt_code", map1.get("work_receipt_code"));// 工单号

					returnList.add(mapDown);
				}
			}
			// 字段对应汉语意思
			Map<String, String> relationMap = new HashMap<String, String>();
			relationMap.put("mat_req_id", "领料单号");
			relationMap.put("create_time", "创建日期");
			relationMap.put("user_name", "申请人");
			relationMap.put("receive_materiel_fty", "领料工厂");
			relationMap.put("receive_fty", "接收工厂");
			relationMap.put("status_name", "状态");
			relationMap.put("mat_req_rid", "单号行号");
			relationMap.put("mat_code", "物料编码");
			relationMap.put("mat_name", "物料描述");
			relationMap.put("name_zh", "计量单位");
			relationMap.put("demand_qty", "需求数量");
			relationMap.put("location_name", "库存地点");
			relationMap.put("take_delivery_qty", "已出库数量");
			relationMap.put("not_take_delivery_qty", "未出库数量");
			relationMap.put("move_type_id", "移动类型");
			relationMap.put("move_type_name", "移动类型描述");
			relationMap.put("reserve_code", "预留号");
			relationMap.put("reserve_rid", "预留行项目");
			relationMap.put("purchase_order_code", "采购订单号");
			relationMap.put("purchase_order_rid", "采购订单行号");
			relationMap.put("use_dept_name", "使用部门");
			relationMap.put("mat_type_name", "物料类型");
			relationMap.put("biz_type_name", "业务类型");
			relationMap.put("cost_obj_name", "成本对象");
			relationMap.put("device_code", "设备号");
			relationMap.put("work_receipt_code", "工单号");
			// 表格排序顺序
			List<String> orderList = new ArrayList<String>();
			orderList.add("领料单号");
			orderList.add("创建日期");
			orderList.add("申请人");
			orderList.add("领料工厂");
			orderList.add("接收工厂");
			orderList.add("状态");
			orderList.add("单号行号");
			orderList.add("物料编码");
			orderList.add("物料描述");
			orderList.add("计量单位");
			orderList.add("需求数量");
			orderList.add("库存地点");
			orderList.add("已出库数量");
			orderList.add("未出库数量");
			orderList.add("移动类型");
			orderList.add("移动类型描述");
			orderList.add("预留号");
			orderList.add("预留行项目");
			orderList.add("采购订单号");
			orderList.add("采购订单行号");
			orderList.add("使用部门");
			orderList.add("物料类型");
			orderList.add("业务类型");
			orderList.add("成本对象");
			orderList.add("设备号");
			orderList.add("工单号");

			String root = constantConfig.getTempFilePath();

			String download_file_name = "领料单查询";// 领料单查询

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, returnList, relationMap, orderList, root);

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
	 * 料单查询下的出库数量查询出库单信息
	 * 
	 * @author 刘宇 2018.02.03
	 * @param cUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/serach_out_put", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachChk(CurrentUser cUser, @RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> chkList = new ArrayList<Map<String, Object>>();
		JSONArray body = new JSONArray();
		try {
			String matReqId = json.getString("mat_req_id");// 领料单号
			String matReqRid = json.getString("mat_req_rid");// 领料单行号

			chkList = matReqService.listOutputForMatReq(matReqId, matReqRid);

			if (chkList != null && chkList.size() > 0) {
				total = chkList.size();
				for (Map<String, Object> map1 : chkList) {
					JSONObject obj = new JSONObject();
					obj.put("stock_output_id", map1.get("stock_output_id")); // 出库单号
					obj.put("stock_output_rid", map1.get("stock_output_rid")); // 行号
					obj.put("output_qty", map1.get("output_qty")); // 出库数量
					obj.put("user_name", map1.get("user_name")); // 创建人
					obj.put("create_time", UtilString.getLongStringForDate((Date) map1.get("create_time"))); // 创建时间
					body.add(obj);
				}
			}
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			logger.error("", e);
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total, body);
	}

}
