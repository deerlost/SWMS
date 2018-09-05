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
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.service.biz.IFileService;
import com.inossem.wms.service.dic.IMatMajorDataService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import net.sf.json.JSONObject;

/**
 * 物料主数据
 * 
 * @author 刘宇 2018.02.26
 *
 */
@Controller
@RequestMapping("/conf/mat")
public class MatMajorDataController {
	private static final Logger logger = LoggerFactory.getLogger(MatMajorDataController.class);

	@Autowired
	private IMatMajorDataService matMajorDataService;
	@Autowired
	private IFileService fileService;

	/**
	 * 物料主数据分页查询
	 * 
	 * @param 刘宇
	 *            2018.02.11
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/mat_major_data", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject serachMatMajorData(CurrentUser cUser, @RequestBody JSONObject json) {

		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		String errorString = "失败";
		boolean errorStatus = false;
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		List<Map<String, Object>> matMajorDataMaps = new ArrayList<Map<String, Object>>();
		try {
			String condition = json.getString("condition");// 查询条件
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			if (json.containsKey("total")) {
				total = json.getInt("total");
			}
			boolean sortAscend = json.getBoolean("sort_ascend");
			String sortColumn = json.getString("sort_column");
			matMajorDataMaps = matMajorDataService.listMatMajorDataOnPaging(condition, pageIndex, pageSize, total,
					sortAscend, sortColumn);
			if (matMajorDataMaps.size() > 0) {
				total = Integer.parseInt(matMajorDataMaps.get(0).get("totalCount").toString());
			}
			for (int i = 0; i < matMajorDataMaps.size(); i++) {
				Map<String, Object> map = matMajorDataMaps.get(i);
				List<BizReceiptAttachmentVo> fileList = fileService.getReceiptAttachments(UtilObject.getIntOrZero(map.get("mat_id")),
						(byte)1);
				matMajorDataMaps.get(i).put("fileList", fileList);
			}

			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
			errorStatus = true;
			errorString = "成功";
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
		}

		return UtilResult.getResultToJson(errorStatus, errorCode, errorString, pageIndex, pageSize, total,
				matMajorDataMaps);
	}

	/**
	 * 同步物料
	 * 
	 * @param matCode
	 * @return
	 */
	@RequestMapping(value = "/sync_mat", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object syncMat(String mat_code) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		int count = 0;
		try {
			// count = matMajorDataService.syncSapMaterial(mat_code);
			count = matMajorDataService.syncSapMaterialFromOracle(mat_code);
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}
		JSONObject body = new JSONObject();

		body.put("count", count);

		return UtilResult.getResultToJson(status, errorCode, body);

	}

	@RequestMapping(value = "/update_shelf_by_mat_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateShelfByMatId(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("matId", UtilObject.getStringOrEmpty(json.get("mat_id")));
			map.put("shelfLife", UtilObject.getStringOrEmpty(json.get("shelf_life")));
			map.put("sampName", UtilObject.getStringOrEmpty(json.get("samp_name")));
			map.put("specification", UtilObject.getStringOrEmpty(json.get("specification")));
			map.put("standard", UtilObject.getStringOrEmpty(json.get("standard")));
			matMajorDataService.updateShelfByMatId(map);
		} catch (Exception e) {
			logger.error("", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
			msg = "修改保质期失败！";
		}
		return UtilResult.getResultToJson(status, errorCode, msg, "");
	}

	/**
	* @Description: TODO(添加图片)</br>
	   @Title: addImg </br>
	* @param @param json
	* @param @return    设定文件</br>
	* @return Object    返回类型</br>
	* @throws</br>
	*/
	@RequestMapping(value = "/add_img", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addImg(@RequestBody JSONObject json) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		String msg = "";

		try {
			if (matMajorDataService.addimg(json) > 0) {
				msg = "添加成功";
			} else {
				msg = "添加失败";
			}
		} catch (Exception e) {
			logger.error("-----", e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			status = false;
		}

		return UtilResult.getResultToJson(status, errorCode, msg);
	}
}
