package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.service.dic.IProductLineService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/conf/product_line")
public class ProductLineController {
	private static final Logger logger = LoggerFactory.getLogger(ProductLineController.class);
	@Autowired
	private IProductLineService productLineService;
	/**
	 * 生产线列表获得
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_dic_product_installation_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDicProductLineList( CurrentUser cUser) {

		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		
		List<DicProductLine> map = new  ArrayList<DicProductLine>();

		try {
			map = productLineService.selectProductInstallationList();
			
		} catch (Exception e) {
			logger.error("生产线列表获得 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}

		return UtilResult.getResultToJson(status, error_code,  map);
	}

	/**
	 * 生产线新增或修改
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/save_dic_product_installation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateProductLine(@RequestBody JSONObject json, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "保存成功";
		try {
			status = productLineService.saveOrUpdateProductLine(json);
		} catch (Exception e) {
			logger.error("生产线新增或修改--", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}
		return UtilResult.getResultToJson(status, error_code, massage,"");
	}
	
	/**
	 * 删除生产线
	 * @param product_line_id
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/delete_product_line", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteProductLine(Integer product_line_id, CurrentUser cUser) {
		int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String massage = "";
		try {
			int i = productLineService.deleteProductLine(product_line_id);
			if(i<=0) {
				status=false;
			}
		} catch (Exception e) {
			logger.error("删除生产线 --", e);
			status = false;
			error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			massage = "操作失败";
		}
		return UtilResult.getResultToJson(status, error_code, massage, "");
		
	}
	
	/**
	 * 生产线
	 * 
	 * @param json
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_dic_product_installation_list1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getDicProductLineList1( CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"Success\",\"page_index\":1,\"page_size\":3,\"total\":20},\"body\":[{\"product_line_id\":\"10\",\"product_line_name\":\"1测试生产线\",\"is_delete\":\"0\",\"installations\":[{\"installation_id\":\"1\",\"installation_name\":\"1测试生产线1装置\",\"is_delete\":\"0\"},{\"installation_id\":\"2\",\"installation_name\":\"1测试生产线2装置\",\"is_delete\":\"0\"},{\"installation_id\":\"3\",\"installation_name\":\"1测试生产线2装置\",\"is_delete\":\"0\"}]},{\"product_line_id\":\"11\",\"product_line_name\":\"2测试生产线\",\"is_delete\":\"0\",\"installations\":[{\"installation_id\":\"1\",\"installation_name\":\"2测试生产线装置1\",\"is_delete\":\"0\"},{\"installation_id\":\"2\",\"installation_name\":\"2测试生产线2装置2\",\"is_delete\":\"0\"},{\"installation_id\":\"3\",\"installation_name\":\"2测试生产线2装置3\",\"is_delete\":\"0\"}]}]}";
		return JSONObject.fromObject(str);
	}

	@RequestMapping(value = "/save_dic_product_installation1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveOrUpdateProductLine1(@RequestBody JSONObject json, CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":5,\"total\":21}}";
		return JSONObject.fromObject(str);
	}

	@RequestMapping(value = "/delete_product_line1", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject deleteProductLine1(Integer product_line_id, CurrentUser cUser) {

		String str = "{\"head\":{\"status\":true,\"error_code\":0,\"msg\":\"成功\",\"page_index\":1,\"page_size\":5,\"total\":21}}";
		return JSONObject.fromObject(str);
	}
}
