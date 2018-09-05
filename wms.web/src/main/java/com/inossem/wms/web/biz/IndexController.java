package com.inossem.wms.web.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.auth.Resources;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.biz.IIndexService;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/biz/auth")
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private IIndexService indexService;
	

	private String getSql(List<String> locationList) {
		String sql = "";
		if (locationList != null) {
			for (int i = 0; i < locationList.size(); i++) {
				String[] strs = locationList.get(i).split("-");
				String innerSql = " ( t.fty_id = '" + strs[0] + "' and t.location_id = '" + strs[1] + "' ) ";
				if (i == 0) {
				} else {
					innerSql = " or " + innerSql;

				}
				sql += innerSql;
			}
			sql = "( " + sql + " ) ";
		}
		return sql;
	}

	private String getString(Object str) {
		if (str == null)
			return "";
		return (String) str;
	}
	
	private long getCount(Object str) {
		long count = 0;
		if (str != null) {
			count = (Long) str;
//			if (count > 99) {
//				count = 99;
//			}
		}
		return count;
	}
	
	
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getCountNum(CurrentUser cUser) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject body = new JSONObject();
		
		ArrayList<HashMap<String, Object>> cNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> unCNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<Resources> resourcesShortCut = new ArrayList<Resources>();
		ArrayList<HashMap<String, Object>> rcNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> runCNumList = new ArrayList<HashMap<String, Object>>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<Integer> role = cUser.getRoleCode();

			map.put("userId", cUser.getUserId());
			map.put("roles", role);
			map.put("todolist", 1);
			map.put("checked", 1);
			ArrayList<Resources> resources = indexService.getCurrentUserShowResouses(map);

			HashMap<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("userId", "'" + cUser.getUserId() + "'");
			countMap.put("corpId", "'" + cUser.getCorpId() + "'");
			String sql = this.getSql(cUser.getLocationList());
			countMap.put("sql", sql);
			for (Resources r : resources) {
				countMap.put("type" + r.getResourcesId(), r.getResourcesId() + "");
			}

			cNumList = indexService.getCompletedNum(countMap);
			unCNumList = indexService.getUnCompletedNum(countMap);

			for (Resources r : resources) {
				for (HashMap<String, Object> innerObj : cNumList) {
					String type = (String) innerObj.get("type");

					if (r.getResourcesId() != null && r.getResourcesId() == Integer.parseInt(type)) {
						HashMap<String, Object> num = new HashMap<String, Object>();
						num.put("type", r.getResourcesId());
						num.put("num", this.getCount(innerObj.get("countNum")));
						num.put("type_name", r.getResourcesName());
						num.put("url", this.getString(r.getResourcesUrl()));
						rcNumList.add(num);
						// if (rcNumList.size() < 7) {
						// rcNumList.add(num);
						// }

					}
				}

			}
			for (Resources r : resources) {
				
				for (HashMap<String, Object> innerObj : unCNumList) {
					String type = (String) innerObj.get("type");

					if (r.getResourcesId() != null && r.getResourcesId() == Integer.parseInt(type)) {
						HashMap<String, Object> num = new HashMap<String, Object>();
						num.put("type", r.getResourcesId());
						num.put("num", this.getCount(innerObj.get("countNum")));
						num.put("type_name", r.getResourcesName());
						num.put("url", this.getString(r.getResourcesUrl()));
						runCNumList.add(num);
						// if (runCNumList.size() < 7) {
						// runCNumList.add(num);
						// }
					}
				}

			}

			HashMap<String, Object> shortCutmap = new HashMap<String, Object>();

			shortCutmap.put("userId", cUser.getUserId());
			shortCutmap.put("roles", role);
			shortCutmap.put("shortcut", 1);
			shortCutmap.put("checked", 1);
			resourcesShortCut = indexService.getCurrentUserShowResouses(shortCutmap);

			
			body.put("cNumList", rcNumList);
			body.put("unCNumList", runCNumList);
			body.put("resourcesShortCut", resourcesShortCut);
			
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取主页信息", e);
		}
		
		return UtilResult.getResultToJson(status, errorCode, body);
	}
	
	//创优新增 - 数据统计 时间限制
	@RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody Object getDataStatistical(CurrentUser cUser , String start_date, String end_date) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		JSONObject body = new JSONObject();
		
		ArrayList<HashMap<String, Object>> cNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> unCNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<Resources> resourcesShortCut = new ArrayList<Resources>();
		ArrayList<HashMap<String, Object>> rcNumList = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> runCNumList = new ArrayList<HashMap<String, Object>>();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<Integer> role = cUser.getRoleCode();

			map.put("userId", cUser.getUserId());
			map.put("roles", role);
			map.put("todolist", 1);
			map.put("checked", 1);
			ArrayList<Resources> resources = indexService.getCurrentUserShowResouses(map);

			HashMap<String, Object> countMap = new HashMap<String, Object>();
			countMap.put("userId", "'" + cUser.getUserId() + "'");
			countMap.put("corpId", "'" + cUser.getCorpId() + "'");
			String sql = this.getSql(cUser.getLocationList());
			countMap.put("sql", sql);
			for (Resources r : resources) {
				countMap.put("type" + r.getResourcesId(), r.getResourcesId() + "");
			}

			countMap.put("timeFr", start_date);
			countMap.put("timeTo", end_date);

			cNumList = indexService.getCompletedNum(countMap);

			for (Resources r : resources) {
				for (HashMap<String, Object> innerObj : cNumList) {
					String type = (String) innerObj.get("type");

					if (r.getResourcesId() != null && r.getResourcesId() == Integer.parseInt(type)) {
						HashMap<String, Object> num = new HashMap<String, Object>();
						num.put("type", r.getResourcesId());
						num.put("num", this.getCount(innerObj.get("countNum")));
						num.put("type_name", r.getResourcesName());
						num.put("url", this.getString(r.getResourcesUrl()));
						rcNumList.add(num);

					}
				}

			}

			body.put("cNumList", rcNumList);
			
		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取主页信息", e);
		}
		
		return UtilResult.getResultToJson(status, errorCode, body);
	}

	/**
	 * 获取显示的快捷方式
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_short_cut_path", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getShortCutPath(CurrentUser cUser) {
		ArrayList<Resources> resources = new ArrayList<Resources>();
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<Integer> role = cUser.getRoleCode();

			map.put("userId", cUser.getUserId());
			map.put("roles", role);
			map.put("shortcut", 1);
			map.put("checked", 1);
			resources = indexService.getCurrentUserShowResouses(map);

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取显示的快捷方式", e);
		}
		return UtilResult.getResultToJson(status, errorCode, resources);

	}

	/**
	 * 格式化返回结果
	 * @throws Exception 
	 */
	private ArrayList<HashMap<String, Object>> formatReturnList(ArrayList<Resources> resources) throws Exception{
		HashMap<String, ArrayList<Resources>> parentChildMap = new HashMap<String, ArrayList<Resources>>();
		HashMap<String, Object> parentMap = new HashMap<String, Object>();
		for (Resources r : resources) {
			String keyString = UtilString.getStringOrEmptyForObject(r.getParentId());
			if (r.getParentId() > 0 && parentMap.containsKey(keyString)) {
				ArrayList<Resources> chils = parentChildMap.get(keyString);
				chils.add(r);
				parentChildMap.put(keyString, chils);
			} else if (r.getParentId() > 0 && !parentMap.containsKey(keyString)) {
				ArrayList<Resources> chils = new ArrayList<Resources>();
				chils.add(r);
				parentChildMap.put(keyString, chils);
				Resources parentResource = indexService.getResourceById(r.getParentId());
				parentMap.put(keyString, parentResource.getResourcesName());
			}
		}

		Set<Entry<String, Object>> entries = parentMap.entrySet();

		ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		
		for (Entry<String, Object> entry : entries) {
			HashMap<String, Object> resource = new HashMap<String, Object>();

			resource.put("name", parentMap.get(entry.getKey()));

			resource.put("childList", parentChildMap.get(entry.getKey()));

			returnList.add(resource);
		}
		return returnList;
		
	}
	/**
	 * 获取代办分类列表
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_to_do_check_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getToDoCheckList(CurrentUser cUser) {
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		boolean status = true;
		ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();

		try {
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<Integer> role = cUser.getRoleCode();

			map.put("userId", cUser.getUserId());
			map.put("roles", role);
			map.put("todolist", 1);
			// 获取资源
			ArrayList<Resources> resources = indexService.getCurrentUserShowResouses(map);

			returnList = this.formatReturnList(resources);

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取代办分类列表", e);
		}
		return UtilResult.getResultToJson(status, errorCode, returnList);
	}

	/**
	 * 获取快捷方式列表
	 * @param cUser
	 * @return
	 */
	@RequestMapping(value = "/get_short_cut_check_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject getShortCutCheckList(CurrentUser cUser) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
		ArrayList<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();
		
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();

			List<Integer> role = cUser.getRoleCode();

			map.put("userId", cUser.getUserId());
			map.put("roles", role);
			map.put("shortcut", 1);
			// 获取资源
			ArrayList<Resources> resources = indexService.getCurrentUserShowResouses(map);

			returnList = this.formatReturnList(resources);

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("获取快捷方式列表", e);
		}
		return UtilResult.getResultToJson(status, errorCode, returnList);

	}

	/**
	 * 保存要显示的代办
	 * @param cuser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/save_to_do_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveToDoList(CurrentUser cuser, @RequestBody JSONObject json) {
		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String msg = "";

		try {
			JSONArray checkArray = json.getJSONArray("checked_list");
			ArrayList<HashMap<String, Object>> checkedList = new ArrayList<HashMap<String, Object>>();
			if (checkArray != null && !checkArray.isEmpty() && checkArray.size() <= 8) {
				for (int i = 0; i < checkArray.size(); i++) {
					Integer resourcesId = checkArray.getInt(i);
					HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
					itemHashMap.put("userId", cuser.getUserId());
					itemHashMap.put("type", 1);
					itemHashMap.put("resourcesId", resourcesId);
					checkedList.add(itemHashMap);
				}
				indexService.insertCheckResources(checkedList, cuser.getUserId(), (byte) 1);
			} else {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				msg = "勾选数量大于8个";
			}

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("saveToDoList", e);
		}
		return UtilResult.getResultToJson(status, errorCode, msg);
	}

	/**
	 * 保存要显示的快捷方式
	 * @param cuser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/save_short_cut_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public @ResponseBody JSONObject saveShortCutList(CurrentUser cuser, @RequestBody JSONObject json) {

		boolean status = true;
		int errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();

		String msg = "";

		try {
			JSONArray checkArray = json.getJSONArray("checked_list");
			ArrayList<HashMap<String, Object>> checkedList = new ArrayList<HashMap<String, Object>>();
			if (checkArray != null && !checkArray.isEmpty() && checkArray.size() <= 4) {
				for (int i = 0; i < checkArray.size(); i++) {
					Integer resourcesId = checkArray.getInt(i);
					HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
					itemHashMap.put("userId", cuser.getUserId());
					itemHashMap.put("type", 2);
					itemHashMap.put("resourcesId", resourcesId);
					checkedList.add(itemHashMap);
				}
				indexService.insertCheckResources(checkedList, cuser.getUserId(), (byte) 2);
			} else {
				status = false;
				errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
				msg = "勾选数量大于4个";
			}

		} catch (Exception e) {
			status = false;
			errorCode = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
			logger.error("saveShortCutList", e);
		}
		return UtilResult.getResultToJson(status, errorCode, msg);

	}

}
