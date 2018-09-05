package com.inossem.wms.conf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicAccountPeriod;
import com.inossem.wms.model.enums.EnumBoard;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.vo.DicAccountPeriodVo;
import com.inossem.wms.service.dic.IAccountPeriodService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 账期管理
 * @author 666
 *
 */
@Controller
@RequestMapping("/conf/accountperiod")
public class AccountPeriodController {

	
	private static final Logger logger = LoggerFactory.getLogger(AccountPeriodController.class);


	@Autowired
	private IAccountPeriodService accountPeriodService;
	
	
	
	/**
	 * 15.1.1	账期管理
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_account_period_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getAccountPeriodList(CurrentUser cuser,@RequestBody JSONObject json) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = false;
		List<DicAccountPeriodVo> apList = new ArrayList<DicAccountPeriodVo>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String boardId = json.getString("board_id");
			String corpId = json.getString("corp_id");
			String accountYear = json.getString("account_year");
			String accountMonth = json.getString("account_month");
			if(StringUtils.hasText(boardId)){
				map.put("boardId", boardId);
			}
			if(StringUtils.hasText(corpId)){
				map.put("corpId", corpId);
			}
			if(StringUtils.hasText(accountYear)){
				map.put("accountYear", Integer.parseInt(accountYear));
			}
			if(StringUtils.hasText(accountMonth)){
				map.put("accountMonth", Integer.parseInt(accountMonth));
			}
			pageIndex = json.getInt("page_index");
			pageSize = json.getInt("page_size");
			int totalCount = -1;
			total = 10;
			if (json.containsKey("total")) {
				totalCount = json.getInt("total");
			}
			map.put("paging", true);
			map.put("pageIndex", pageIndex);
			map.put("pageSize", pageSize);
			map.put("totalCount", totalCount);

			apList = accountPeriodService.getAccountPeriods(map);
			if (apList != null && apList.size() > 0) {
				total = apList.get(0).getTotalCount();
				
				for (DicAccountPeriodVo ap : apList) {
					Map<String, Object> zqHashMap = new HashMap<String, Object>();
					zqHashMap.put("corpId", ap.getCorpId());
					zqHashMap.put("boardName", EnumBoard.getNameByValue(ap.getBoardId()));
					zqHashMap.put("periodId", ap.getPeriodId());
					zqHashMap.put("corpName", ap.getCorpName());
					zqHashMap.put("accountYear", ap.getAccountYear());
					zqHashMap.put("accountMonth",ap.getAccountMonth());
					zqHashMap.put("accountBeginDate", UtilString.getShortStringForDate(ap.getAccountBeginDate()));
					zqHashMap.put("accountEndDate", UtilString.getShortStringForDate(ap.getAccountEndDate()));
					zqHashMap.put("accountFactDate", UtilString.getShortStringForDate(ap.getAccountFactDate()));
					zqHashMap.put("createUserName", ap.getCreateUserName());
					zqHashMap.put("createTime", UtilString.getShortStringForDate(ap.getCreateTime()));
					
					returnList.add(zqHashMap);
				}
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("账期列表", e);
		}
		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		
	}

	/**
	 * 16.1.2	批量修改账期
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/update_account_period", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object updateAccountPeriodList(CurrentUser cuser,@RequestBody JSONObject json) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		
		
		boolean status = false;
		List<DicAccountPeriod> apList = new ArrayList<DicAccountPeriod>();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			boolean flag = true;
			JSONArray zqList = json.getJSONArray("account_period_list");
			if(zqList!=null&&!zqList.isEmpty()){
				for(int i=0;i<zqList.size();i++){
					JSONObject zqJsonObject = zqList.getJSONObject(i);
					DicAccountPeriod zPeriod = new DicAccountPeriod();
					zPeriod.setPeriodId(zqJsonObject.getInt("period_id"));
					zPeriod.setAccountBeginDate(UtilString.getDateForString(zqJsonObject.getString("account_begin_date")));
					zPeriod.setAccountEndDate(UtilString.getDateForString(zqJsonObject.getString("account_end_date")));
					zPeriod.setCorpId(zqJsonObject.getInt("corp_id"));
					zPeriod.setBoardId(zqJsonObject.getInt("board_id"));
					zPeriod.setAccountYear(zqJsonObject.getInt("account_year"));
					Integer month = zqJsonObject.getInt("account_month");
					zPeriod.setAccountMonth(month.byteValue());
					zPeriod.setAccountFactDate(UtilString.getDateForString(zqJsonObject.getString("account_fact_date")));
					zPeriod.setIndex(zqJsonObject.getInt("index"));
					if(zPeriod.getAccountBeginDate().after(zPeriod.getAccountEndDate())){
						flag = false;
						body.put("isSuccess", "02");
						body.put("return_str", zPeriod.getIndex());
						errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
						errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
						break;
					}
					
					apList.add(zPeriod);
				}
				if(flag){
					Map<String, Object> returnIndex = validateDate(apList);
					if(returnIndex.containsKey("index")){
						String code = (String) returnIndex.get("code");
						if(code.equals("01")){
							errorCode = EnumErrorCode.ERROR_CODE_MONTH_EXIST.getValue();
							errorString = EnumErrorCode.ERROR_CODE_MONTH_EXIST.getName();
						}else{
							errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
							errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
						}
						body.put("isSuccess", returnIndex.get("code"));
						body.put("return_str", returnIndex.get("index"));
						
					}else{
						DicAccountPeriod apDate = accountPeriodService.validateAccountDate(apList);
						if(apDate!=null){
							body.put("isSuccess", "02");
							body.put("return_str", apDate.getIndex());
							errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
							errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
						}else{
							accountPeriodService.updateAccountPeriods(apList);
						
							status = true;
						}
					}
					
				}
			}else{
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
			}
			
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("更新账期", e);
		}

		

		return UtilResult.getResultToJson(status, errorCode, errorString, body);
		
	}
	
	
	/**
	 * 16.1.2	批量修改账期
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/delete_account_period", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object deleteAccountPeriodList(CurrentUser cuser,@RequestBody JSONObject json) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		
		
		boolean status = true;
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			JSONArray periodIds = json.getJSONArray("period_ids");
			
			if(periodIds!=null&&!periodIds.isEmpty()){
				@SuppressWarnings("unchecked")
				List<Integer> ids = JSONArray.toList(periodIds, new Integer(0), new JsonConfig());
				accountPeriodService.deleteByIds(ids);
				
			}
			
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			status = false;
			logger.error("更新账期", e);
		}

		

		return UtilResult.getResultToJson(status, errorCode, errorString, body);
		
	}
	
	
	/**
	 * 15.1.1	添加账期
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/add_account_period", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object addAccountPeriodList(CurrentUser cuser,@RequestBody JSONObject json) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		String errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
		
		
		boolean status = false;
		List<DicAccountPeriod> apList = new ArrayList<DicAccountPeriod>();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			boolean flag = true;
			JSONArray zqList = json.getJSONArray("account_period_list");
			if(zqList!=null&&!zqList.isEmpty()){
				for(int i=0;i<zqList.size();i++){
					JSONObject zqJsonObject = zqList.getJSONObject(i);
					DicAccountPeriod zPeriod = new DicAccountPeriod();
					zPeriod.setCorpId(zqJsonObject.getInt("corp_id"));
					zPeriod.setBoardId(zqJsonObject.getInt("board_id"));
					zPeriod.setAccountYear(zqJsonObject.getInt("account_year"));
					Integer month = zqJsonObject.getInt("account_month");
					zPeriod.setAccountMonth(month.byteValue());
					zPeriod.setCreateUser(cuser.getUserId());
					zPeriod.setAccountBeginDate(UtilString.getDateForString(zqJsonObject.getString("account_begin_date")));
					zPeriod.setAccountEndDate(UtilString.getDateForString(zqJsonObject.getString("account_end_date")));
					zPeriod.setAccountFactDate(UtilString.getDateForString(zqJsonObject.getString("account_fact_date")));
					zPeriod.setIndex(zqJsonObject.getInt("index"));
					if(zPeriod.getAccountBeginDate().after(zPeriod.getAccountEndDate())){
						flag = false;
						body.put("isSuccess", "02");
						body.put("return_str", zPeriod.getIndex());
						errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
						errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
						break;
					}
					apList.add(zPeriod);
				}
				if(flag){
					Map<String, Object> returnIndex = validateDate(apList);
					if(returnIndex.containsKey("index")){
						String code = (String) returnIndex.get("code");
						if(code.equals("01")){
							errorCode = EnumErrorCode.ERROR_CODE_MONTH_EXIST.getValue();
							errorString = EnumErrorCode.ERROR_CODE_MONTH_EXIST.getName();
						}else{
							errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
							errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
						}
						body.put("isSuccess", returnIndex.get("code"));
						body.put("return_str", returnIndex.get("index"));
						
					}else{
						DicAccountPeriod apDate = accountPeriodService.validateAccountMonth(apList);
						if(apDate!=null){
							body.put("isSuccess", "01");
							body.put("return_str", apDate.getIndex());
						}else{
							DicAccountPeriod apMonth = accountPeriodService.validateAccountDate(apList);
							if(apMonth!=null){
								body.put("isSuccess", "02");
								body.put("return_str", apMonth.getIndex());
								errorCode = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getValue();
								errorString = EnumErrorCode.ERROR_CODE_TIME_BEGIN_END_EXIST.getName();
							}else{
								accountPeriodService.addAccountPeriods(apList);
								status = true;
							}
						}
					}
					
				}
				
				
			}else{
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
			}
			
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			errorString = EnumErrorCode.ERROR_CODE_EXCEPTION.getName();
			logger.error("更新账期", e);
		}

		return UtilResult.getResultToJson(status, errorCode, errorString, body);
		
	}
	
	
	/**
	 * 15.1.1	调拨单列表
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_account_period_list_by_ids", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getAccountPeriodListByIds(CurrentUser cuser,@RequestBody JSONObject json) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = false;
		List<DicAccountPeriodVo> apList = new ArrayList<DicAccountPeriodVo>();
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray periodIds = json.getJSONArray("period_ids");
			
			if(periodIds!=null&&!periodIds.isEmpty()){
				map.put("periodIds", periodIds);
			}
			
			
			

			apList = accountPeriodService.getAccountPeriods(map);
			if (apList != null && apList.size() > 0) {
				
				
				for (DicAccountPeriodVo ap : apList) {
					Map<String, Object> zqHashMap = new HashMap<String, Object>();
					zqHashMap.put("corpId", ap.getCorpId());
					zqHashMap.put("boardName", EnumBoard.getNameByValue(ap.getBoardId()));
					zqHashMap.put("periodId", ap.getPeriodId());
					zqHashMap.put("corpName", ap.getCorpName());
					zqHashMap.put("accountYear", ap.getAccountYear());
					zqHashMap.put("accountMonth",ap.getAccountMonth());
					zqHashMap.put("accountBeginDate", UtilString.getShortStringForDate(ap.getAccountBeginDate()));
					zqHashMap.put("accountEndDate", UtilString.getShortStringForDate(ap.getAccountEndDate()));
					zqHashMap.put("accountFactDate", UtilString.getShortStringForDate(ap.getAccountFactDate()));
					zqHashMap.put("createUserName", ap.getCreateUserName());
					zqHashMap.put("createTime", UtilString.getShortStringForDate(ap.getCreateTime()));
					zqHashMap.put("boardId", ap.getBoardId());
					returnList.add(zqHashMap);
					
				}
			}
			status = true;
			errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		} catch (Exception e) {
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("账期列表", e);
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		
	}
	
	
	/**
	 * 15.1.1	账期基本信息
	 * @param cuser
	 * @return
	 */
	@RequestMapping(value = "/get_base_info", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getBaseInfo(CurrentUser cuser) {

		
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		
		int pageIndex = EnumPage.PAGE_INDEX.getValue();
		int pageSize = EnumPage.PAGE_SIZE.getValue();
		int total = EnumPage.TOTAL_COUNT.getValue();
		boolean status = true;

		JSONArray returnList = new JSONArray();
		try {
			returnList = accountPeriodService.getAllBoardAndCorp();
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("账期列表", e);
		}

		return UtilResult.getResultToJson(status, errorCode, pageIndex, pageSize, total, returnList);
		
	}
	
	
	private Map<String, Object> validateDate(List<DicAccountPeriod> apList){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer index = null;
		Collections.sort(apList);
		HashMap<String, Object> yearMonthMap = new HashMap<String, Object>();
		for(int i=0;i<apList.size();i++){
			DicAccountPeriod ap = apList.get(i);
			Integer corpId = ap.getCorpId();
			Integer boardId = ap.getBoardId();
			Integer year = ap.getAccountYear();
			Byte month = ap.getAccountMonth();
			String only = boardId + "-"+corpId+"-"+year+"-"+month;
			if(yearMonthMap.containsKey(only)){
				index = ap.getIndex();
				returnMap.put("index", index);
				returnMap.put("code", "01");
				break;
			}else{
				yearMonthMap.put(only, only);
				if(i<apList.size()-1){
					DicAccountPeriod apNext = apList.get(i+1);
					if(apNext.getBoardId().compareTo(ap.getBoardId())==0&&apNext.getCorpId().equals(ap.getCorpId())&&apNext.getAccountYear().compareTo(ap.getAccountYear())==0){
						if (apNext.getAccountBeginDate().compareTo(ap.getAccountEndDate())<1) {
							index = apNext.getIndex();
							returnMap.put("index", index);
							returnMap.put("code", "02");
							break;
						}
					}
					
				}
			}
		}
		return returnMap;
	}
	
}
