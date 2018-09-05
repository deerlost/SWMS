package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumLocationStatus;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.IDeliveryNoticeService;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DeliveryNoticeController {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryNoticeController.class);

	@Autowired
	private IDeliveryNoticeService deliveryNoticeService;

	

	@Autowired
	private IUserService userService;

	/**
	 * 查询合同列表
	 * 
	 * @param zhtbh
	 * @param ebeln
	 * @return
	 */
	@RequestMapping(value = "/biz/dhys/cjshtzd/contractList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getContractList(String zhtbh, String ebeln) {
		logger.info("根据送货单号获取验收单方法");
		logger.info("传进参数-----" + zhtbh);
		logger.info("传进参数-----" + ebeln);
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		ArrayList<HashMap<String, Object>> contractList = new ArrayList<HashMap<String, Object>>();
		if (StringUtils.hasText(ebeln) || StringUtils.hasText(zhtbh)) {
			JSONObject findObj = new JSONObject();
			findObj.put("ebeln", ebeln);
			findObj.put("zhtbh", zhtbh);
			try {
				contractList = deliveryNoticeService.getContract(findObj);
				status = true;
			} catch (Exception e) {
				logger.error("",e);
				errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			}
		} else {
			errorCode = EnumErrorCode.ERROR_CODE_NO_PARAM.getValue();
		}

		
		return UtilResult.getResultToJson(status, errorCode, contractList);
	}

	/**
	 * 获取合同信息（详情）
	 * 
	 * @param htbh
	 * @return
	 */
	@RequestMapping(value = "/biz/dhys/cjshtzd/contract/{ebeln}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getContract(@PathVariable("ebeln") String ebeln, CurrentUser cUser) {
		logger.info("获取合同信息");
		logger.info("传进参数-----" + ebeln);
		HashMap<String, Object> contract = new HashMap<String, Object>();
		boolean status = false;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			contract = deliveryNoticeService.getContractInfo(ebeln);

			if (contract.containsKey("shdiList")) {

				List<RelUserStockLocationVo> locationList = userService.listLocForUser(cUser.getUserId());
				List<String> fty_locationCode = new ArrayList<String>();
				Map<String, Object> locationIdStatusMap = new HashMap<String, Object>();

				for (RelUserStockLocationVo innerObj : locationList) {
					String key = innerObj.getFtyCode() + "-" + innerObj.getLocationCode();
					fty_locationCode.add(key);
					locationIdStatusMap.put(key, innerObj.getStatus());
				}

				ArrayList<HashMap<String, Object>> contractItemList = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> contractItemListAlList = (ArrayList<HashMap<String, Object>>) contract
						.get("shdiList");
				if (contractItemListAlList != null) {
					for (HashMap<String, Object> map : contractItemListAlList) {
						String werksString = (String) map.get("werks");
						String lgortString = (String) map.get("lgort");
						String key = werksString + "-" + lgortString;
						Byte locationStatus = UtilObject.getByteOrNull(locationIdStatusMap.get(key));

						if (fty_locationCode.contains(key) && locationStatus != null
								&& (locationStatus.equals(EnumLocationStatus.LOCATION_STATUS_ON_LINE.getValue())
										|| locationStatus.equals(
												EnumLocationStatus.LOCATION_STATUS_ON_LINE_VIRTUAL.getValue()))) {
							contractItemList.add(map);
						}
					}
				}
				contract.put("shdiList", contractItemList);

			}
			status = true;
		} catch (Exception e) {
			logger.error("",e);
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
		}
		
		return UtilResult.getResultToJson(status, errorCode, contract);
	}

}
