package com.inossem.wms.portable.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.service.dic.IWarehouseVolunmWarringService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

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
	private IWarehouseVolunmWarringService  warehouseVolunmWarringService;
	
	@Autowired
	private ServerConfig constantConfig;
	
	@Autowired
	private IDicWarehouseService dicWarehouseService;
	
	
	@RequestMapping(value = "/get_wearhouse_warring", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getWearhouseWarring(CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String masage = "";
		List<Map<String, Object>> returnMap = null;
//		JSONObject body = new JSONObject();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
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
	
	
	
	
	
}
