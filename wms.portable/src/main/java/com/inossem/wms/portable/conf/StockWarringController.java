package com.inossem.wms.portable.conf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockQueryService;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/warring")
public class StockWarringController {
	private static final Logger logger = LoggerFactory.getLogger(StockWarringController.class);
	@Autowired
	private IStockQueryService stockQueryService;
	@Autowired
	private ServerConfig constantConfig;
	@Autowired
	private ICommonService commonService;
	 
	
	// 显示图表信息
	@RequestMapping(value = "/to_coming/pda_product_picture", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject pdaProductPicture(CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = true;
		String userId = cUser.getUserId();
		JSONObject ftyAry = new JSONObject();
		try {
			ftyAry = stockQueryService.pdaProductPicture();
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString=EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			errorStatus=false;
			logger.error("临期库存图表信息查询失败", e);
			errorCode=EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString="临期库存图表信息查询失败";
		}

		return ftyAry;
	}
	
	

	@RequestMapping(value = "/select_persont_by_product", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectPersontByProduct(Integer product_line_id) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = true;
		List<Map<String,Object>> body=new ArrayList<Map<String,Object>>();
		try {
			body=stockQueryService.selectPersontByProduct(product_line_id);
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		}catch (Exception e) {
			errorStatus=false;
			logger.error("详细信息查询失败", e);
			errorString="详细信息查询失败";
		}
		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, body);
	}
	
	
	
	

	@RequestMapping(value = "/select_first_view", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectFirstView(CurrentUser cUser ) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		Map<String,Object> list=new HashMap<String,Object>();
		try {
			list=stockQueryService.selectFirstView();//  lq_qty  lq_qty_money  cq_qty  cq_qty_money
			
			
				BigDecimal qty=	(BigDecimal) list.get("lq_qty");	
				qty.setScale(3, BigDecimal.ROUND_HALF_UP);
				list.put("lq_qty", qty)	; 
				
				BigDecimal lq_qty_money=	(BigDecimal) list.get("lq_qty_money");	
				lq_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
				list.put("lq_qty_money", lq_qty_money)	;
				
				BigDecimal cq_qty=	(BigDecimal) list.get("cq_qty");	
				cq_qty.setScale(3, BigDecimal.ROUND_HALF_UP);
				list.put("cq_qty", cq_qty)	; 
				
				BigDecimal cq_qty_money=	(BigDecimal) list.get("cq_qty_money");	
				cq_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
				list.put("cq_qty_money", cq_qty_money)	; 
			

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString,list);
	}
	
	@RequestMapping(value = "/select_first_report", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectFirstReport(CurrentUser cUser) {
		JSONObject body = new JSONObject();
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		Map<String,Object> list=new HashMap<String,Object>();
		try {
			
			
			list=stockQueryService.selectFirstReport();//yj_qty_money   lq_qty_money  zc_qty_money
			
			BigDecimal yj_qty_money=	(BigDecimal) list.get("yj_qty_money");	
			yj_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
			list.put("yj_qty_money", yj_qty_money)	;
			
			BigDecimal lq_qty_money=	(BigDecimal) list.get("lq_qty_money");	
			lq_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
			list.put("lq_qty_money", lq_qty_money)	; 
			
			BigDecimal zc_qty_money=	(BigDecimal) list.get("zc_qty_money");	
			zc_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
			list.put("zc_qty_money", zc_qty_money)	; 

			JSONArray chartInfoAry = new JSONArray();
			
			JSONObject chartObject1 = new JSONObject();
			chartObject1.put("name", "正常库存");
			chartObject1.put("type", "0");
			chartObject1.put("dependence", "0");
			chartInfoAry.add(chartObject1);

			JSONObject chartObject2 = new JSONObject();
			chartObject2.put("name", "临期库存");
			chartObject2.put("type", "0");
			chartObject2.put("dependence", "0");
			chartInfoAry.add(chartObject2);

			JSONObject chartObject3 = new JSONObject();
			chartObject3.put("name", "预警库存");
			chartObject3.put("type", "0");
			chartObject3.put("dependence", "0");
			chartInfoAry.add(chartObject3);

			
			JSONArray xAxisAry = new JSONArray();
			List<BigDecimal> leftList = new ArrayList<BigDecimal>();
//			List<BigDecimal> rightList1 = new ArrayList<BigDecimal>();
//			List<BigDecimal> rightList2 = new ArrayList<BigDecimal>();

			BigDecimal tenThousand = new BigDecimal(10000);

			
				leftList.add(UtilObject.getBigDecimalOrZero(list.get("zc_qty_money")));
				leftList.add(UtilObject.getBigDecimalOrZero(list.get("lq_qty_money")));
				leftList.add(UtilObject.getBigDecimalOrZero(list.get("yj_qty_money")));
				
				JSONArray xAry = new JSONArray();
				JSONObject obj = new JSONObject();
				JSONObject innerObj1 = new JSONObject();
				
					innerObj1.put("yAxisValue",
							list.get("zc_qty_money"));
					xAry.add(innerObj1);
					JSONObject innerObj2 = new JSONObject();
					innerObj2.put("yAxisValue",
							list.get("lq_qty_money"));
					xAry.add(innerObj2);
					JSONObject innerObj3 = new JSONObject();
					innerObj3.put("yAxisValue",
							list.get("yj_qty_money"));
					xAry.add(innerObj3);
					
				
				obj.put("xAxisName",chartInfoAry);
				obj.put("yAxisValueList", xAry);
				
				xAxisAry.add(obj);
			
			BigDecimal leftMax = BigDecimal.ZERO;
//			BigDecimal rightMax1 = BigDecimal.ZERO;
//			BigDecimal rightMax2 = BigDecimal.ZERO;
			for (int i = 0; i < leftList.size(); i++) {
				if (leftMax.compareTo(leftList.get(i)) == -1) {
					leftMax = leftList.get(i);
				}
			}
//			for (int i = 0; i < rightList1.size(); i++) {
//				if (rightMax1.compareTo(rightList1.get(i)) == -1) {
//					rightMax1 = rightList1.get(i);
//				}
//			}
//			for (int i = 0; i < rightList2.size(); i++) {
//				if (rightMax2.compareTo(rightList2.get(i)) == -1) {
//					rightMax2 = rightList2.get(i);
//				}
//			}
         
        	   body.put("leftAxisMaxValue", leftMax);   
        	   body.put("leftAxisUnit", "万元");        
			
			body.put("title", "临期库存预警");
			body.put("chartInfoList", chartInfoAry);
			body.put("xAxisList", xAxisAry);

			
			
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString,body);
	}
	
	
	@RequestMapping(value = "/select_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectDetail(CurrentUser cUser, Integer search_type ) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try {
			list=stockQueryService.selectDetail(search_type);//lq_qty_money  lq_qty
				for (int i = 0; i < list.size(); i++) {
					
					BigDecimal lq_qty_money=	(BigDecimal) list.get(i).get("lq_qty_money");	
					lq_qty_money.setScale(3, BigDecimal.ROUND_HALF_UP);
					list.get(i).put("lq_qty_money", lq_qty_money)	; 
					
					BigDecimal lq_qty=	(BigDecimal) list.get(i).get("lq_qty");	
					lq_qty.setScale(3, BigDecimal.ROUND_HALF_UP);
					list.get(i).put("lq_qty", lq_qty)	;
					
				}
			
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString,list);
	}
	
	@RequestMapping(value = "/select_detail_mat", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject selectDetailMat(CurrentUser cUser, Integer wh_id,Integer search_type ) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
		boolean errorStatus = false;
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("whId", wh_id);
		map.put("searchType", search_type);
		try {
			list=stockQueryService.selectDetailMat(map);

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		} catch (Exception e) {
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString,list);
	}
	



}
