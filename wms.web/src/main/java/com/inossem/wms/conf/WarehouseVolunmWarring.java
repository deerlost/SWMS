package com.inossem.wms.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.service.dic.IWarehouseVolunmWarringService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/warehouse_volunm_warring")
public class WarehouseVolunmWarring {
	private static final Logger logger = LoggerFactory.getLogger(WarehouseVolunmWarring.class);

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IStockQueryService stockQueryService;

	@Autowired
	private IWarehouseVolunmWarringService warehouseVolunmWarringService;

	@Autowired
	private ServerConfig constantConfig;

	@Autowired
	private IDicWarehouseService dicWarehouseService;

	// 根据板块得到公司、工厂、库存地点联动
	@RequestMapping(value = "/base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object baseInfo( CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;

		JSONObject body = new JSONObject();
		try {
			Calendar date = Calendar.getInstance();
		    String year = String.valueOf(date.get(Calendar.YEAR));
			int defaultCorpId = cUser.getCorpId();
			List<DicBoard> bList = new ArrayList<DicBoard>();
			bList = commonService.getBordList();
			body.put("year",year);
			body.put("deaultBoardId", cUser.getBoardId());
			body.put("defaultCorpId", defaultCorpId);
			body.put("boardList", bList);			

		} catch (Exception e) {
			logger.error("图表查询 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		JSONObject  obj = UtilResult.getResultToJson(status, error_code, body);
		UtilJSONConvert.setNullToEmpty(obj);
		return obj;

	}
	
	@RequestMapping(value = "/get_wearhouse_warring", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWearhouseWarring(CurrentUser cUser,Integer wh_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String masage = "";
		List<Map<String, Object>> returnMap = null;
//		JSONObject body = new JSONObject();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("whId", UtilObject.getIntegerOrNull(wh_id));
			returnMap = warehouseVolunmWarringService.selectWareHouseWarringForPortal(param);
			body=this.formatHead(returnMap);
//			body.put("head", head);
//			body.put("returnMap", returnMap);
		} catch (Exception e) {
			logger.error("容量预警容量分析 --", e);
			status = false;
			masage = "查询容量预警容量分析失败";
		}
		JSONObject obj = UtilResult.getResultToJson(status, error_code, masage, body);
		return obj;
	}
	
	private Map<String, Object> formatHead(List<Map<String, Object>> returnMap) {
		int two=0;
		int one=0;
		int min=0;
		int normal=0;
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String, Object> twoMap = new HashMap<String, Object>();
		Map<String, Object> oneMap = new HashMap<String, Object>();
		Map<String, Object> minMap = new HashMap<String, Object>();
		Map<String, Object> normalMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> twoList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> oneList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> minList=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> normalList=new ArrayList<Map<String, Object>>();
		
		for(Map<String, Object> map:returnMap) {
			BigDecimal reminding_qty_one=new BigDecimal("".equals(map.get("reminding_qty_one").toString())?"0":map.get("reminding_qty_one").toString());
			BigDecimal reminding_qty_two=new BigDecimal("".equals(map.get("reminding_qty_two").toString())?"0":map.get("reminding_qty_two").toString());
			BigDecimal volume_min=new BigDecimal("".equals(map.get("volume_min").toString())?"0":map.get("volume_min").toString());
			BigDecimal volume_max=new BigDecimal("".equals(map.get("volume_max").toString())?"0":map.get("volume_max").toString());
			BigDecimal qty=new BigDecimal(map.get("qty").toString());
			List<Map<String,Object>> lis = new ArrayList<Map<String,Object>>();
			Map<String,Object> dat = new HashMap<String,Object>();
			dat.put("info_class", "最大容量");
			dat.put("unit_code", "KG");
			dat.put("qty", volume_max);
			lis.add(dat);
			dat = new HashMap<String,Object>();
			dat.put("info_class", "二级预警容量");
			dat.put("unit_code", "KG");
			dat.put("qty", reminding_qty_two);
			lis.add(dat);
			dat = new HashMap<String,Object>();
			dat.put("info_class", "一级预警容量");
			dat.put("unit_code", "KG");
			dat.put("qty", reminding_qty_one);
			lis.add(dat);
			dat = new HashMap<String,Object>();
			dat.put("info_class", "最小预警容量");
			dat.put("unit_code", "KG");
			dat.put("qty", volume_min);
			lis.add(dat);
			dat = new HashMap<String,Object>();
			dat.put("info_class", "当前容量");
			dat.put("unit_code", "KG");
			dat.put("qty", qty);
			lis.add(dat);
			map.put("qty_info", lis);
			map.put("persont",map.get("persont"));
			if(qty.compareTo(reminding_qty_two)==1) {
				two++;
				twoList.add(map);
			}
			if(qty.compareTo(reminding_qty_one)==1) {
				one++;
				oneList.add(map);
			}
			if(qty.compareTo(volume_min)==-1) {
				min++;
				minList.add(map);
			}
			if(qty.compareTo(reminding_qty_two)==-1) {
				normal++;
				normalList.add(map);
			}
		}
		twoMap.put("num", two);
		oneMap.put("num", one);
		minMap.put("num", min);
		normalMap.put("num", normal);
		
		twoMap.put("data", twoList);
		oneMap.put("data", oneList);
		minMap.put("data", minList);
		normalMap.put("data", normalList);
		
		result.put("twoMap", twoMap);
		result.put("oneMap", oneMap);
		result.put("minMap", minMap);
		result.put("normalMap", normalMap);
		return result;
	}

	@RequestMapping(value = "/get_wearhouse_warring_by_area", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWearhouseWarring(CurrentUser cUser,int area_id) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String masage = "";
		List<Map<String, Object>> returnMap = null;
		JSONObject body = new JSONObject();
		try {
			returnMap = warehouseVolunmWarringService.selectWareHouseWarringByAreaId(area_id);
			body.put("returnMap", returnMap);
		} catch (Exception e) {
			logger.error("容量预警容量分析 --", e);
			status = false;
			masage = "查询容量预警容量分析失败";
		}
		JSONObject obj = UtilResult.getResultToJson(status, error_code, masage, body);
		return obj;
	}
	
	
	/**
	 * 导出Excel
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download_wearhouse_warring", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public void downloadJobStatisList(CurrentUser cuser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		BufferedOutputStream out = null;
		InputStream bis = null;
		File fileInServer = null;

		List<Map<String, Object>> zylList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			String ftyId = request.getParameter("fty_id");
			String whId = request.getParameter("wh_id");
			if (StringUtils.hasText(ftyId)) {
				map.put("ftyId", ftyId);
			}
			if (StringUtils.hasText(whId)) {
				map.put("whId", whId);
			}
			zylList = warehouseVolunmWarringService.selectWareHouseWarring(map);

			// 下载
			Map<String, String> relationMap = new HashMap<String, String>();

			relationMap.put("wh_code", "仓库号");
			relationMap.put("area_code", "仓储区");
			relationMap.put("area_name", "仓储区名称");
			relationMap.put("volume_min", "最小容量");
			relationMap.put("volume_max", "最大容量");
			relationMap.put("reminding_qty_one", "一级预警容量");
			relationMap.put("reminding_qty_two", "二级预警容量");
			relationMap.put("qty", "当前库存数量");
			relationMap.put("persont", "使用率");

			List<String> orderList = new ArrayList<String>();
			orderList.add("仓库号");
			orderList.add("仓储区");
			orderList.add("仓储区名称");
			orderList.add("最小容量");
			orderList.add("最大容量");
			orderList.add("一级预警容量");
			orderList.add("二级预警容量");
			orderList.add("当前库存数量");
			orderList.add("使用率");
			String root = constantConfig.getTempFilePath();

			String download_file_name = "quantityOfWork";// 作业量

			String filePath = UtilExcel.getExcelRopertUrl(download_file_name, zylList, relationMap, orderList, root);

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

}
