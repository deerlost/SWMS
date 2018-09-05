package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IPersonnelService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
public class PersonnelController {

	private static final Logger logger = LoggerFactory.getLogger(PersonnelController.class);

	@Autowired
	IPersonnelService personnelService;

	/**
	 * @Description: TODO(分页)</br>
	 * @Title: getParamMapToPageing </br>
	 * @param @param
	 *            json
	 * @param @return
	 *            设定文件</br>
	 * @return Map<String,Object> 返回类型</br>
	 * @throws</br>
	 */
	private Map<String, Object> getParamMapToPageing(JSONObject json) {
		Map<String, Object> param = new HashMap<String, Object>();

		int pageIndex = json.getInt("page_index");
		int pageSize = json.getInt("page_size");
		int totalCount = EnumPage.TOTAL_COUNT.getValue();
		boolean sortAscend = true;
		String sortColumn = "";

		if (json.containsKey("total")) {
			totalCount = json.getInt("total");
		}
		if (json.containsKey("sort_ascend")) {
			sortAscend = json.getBoolean("sort_ascend");
		}
		if (json.containsKey("sort_column")) {
			sortColumn = json.getString("sort_column");
		}

		param.put("totalCount", totalCount);
		param.put("paging", true);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("totalCount", totalCount);
		return param;
	}

	/**
	 * @Description: TODO(查询员工)</br>
	 * @Title: queryPersonnel </br>
	 * @param @param
	 *            type
	 * @param @return
	 *            设定文件</br>
	 * @return JSONObject 返回类型</br>
	 * @throws</br>
	 */
	@RequestMapping(value = "/conf/personnel/query_person", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject queryPersonnel(@RequestBody JSONObject json) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		int total = 0;
		Long totalCountLong = (long) 0;
		List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = this.getParamMapToPageing(json);
		param.put("type", json.get("type"));
		param.put("keyword", json.get("keyword"));
		try {
			body = personnelService.queryPersonnel(param);
			if(body.size()>0){
				totalCountLong = (Long) body.get(0).get("totalCount");
			}
			
			total = totalCountLong.intValue();
		} catch (Exception e) {
			logger.error("=====================查询员工异常==============", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, UtilObject.getIntOrZero(param.get("pageIndex")),
				UtilObject.getIntOrZero(param.get("pageSize")), total, body);
	}

	/**
	* @Description: TODO(增加员工)</br>
	   @Title: insertPersonnel </br>
	* @param @param json
	* @param @return    设定文件</br>
	* @return JSONObject    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/conf/personnel/insert_person", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject insertPersonnel(@RequestBody JSONObject json) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		try {
			if (personnelService.insertPersonnel(json) == 0) {
				msg = "增加成功";
			} else {
				msg = "增加失败";
			}
		} catch (Exception e) {
			logger.error("=====================增加员工异常==============", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, msg);
	}

	/**
	* @Description: TODO(删除员工)</br>
	   @Title: deletePersonnel </br>
	* @param @param typeId
	* @param @param id
	* @param @return    设定文件</br>
	* @return JSONObject    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/conf/personnel/delete_person", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deletePersonnel(int type, int id) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		try {
			personnelService.deletePersonnel(type, id);
			msg = "删除成功";
		} catch (Exception e) {
			logger.error("=====================删除员工异常==============", e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			msg = "删除失败";
		}
		return UtilResult.getResultToJson(status, error_code, msg);
	}
	
	@RequestMapping(value = "/conf/personnel/query_product_line", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public@ResponseBody   JSONObject queryProductLine(){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>>body = new ArrayList<Map<String,Object>>();
		try {
			List<DicProductLine> list= personnelService.selectAllProductLine();
			for(DicProductLine line : list ){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("product_line_id",line.getProductLineId());
				map.put("product_line_name",	 line.getProductLineName());
				body.add(map);
			}
		} catch (Exception e) {
			logger.error("==============查询产品线======",e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, body);
	}
	
	@RequestMapping(value = "/conf/personnel/query_warehouse", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public@ResponseBody   JSONObject queryWarehouse(){
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		List<Map<String, Object>>body = new ArrayList<Map<String,Object>>();
		try {
			List<DicWarehouse> list = personnelService.selectAllWhIdAndWhCodeAndWhName();
			for(DicWarehouse wh : list ){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("wh_id",wh.getWhId());
				map.put("wh_name",	 wh.getWhName());
				map.put("wh_code", wh.getWhCode());
				body.add(map);
			}
		} catch (Exception e) {
			logger.error("==============查询仓库失败======",e);
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		return UtilResult.getResultToJson(status, error_code, body);
	}
}
