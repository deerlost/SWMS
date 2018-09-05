package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizUrgenceResVo;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 仓库
 * 
 * @author 刘宇 2018.03.01
 *
 */

@Transactional
@Service("dicWarehouseService")
public class DicWarehouseServiceImpl implements IDicWarehouseService {
	@Autowired
	private DicWarehouseMapper dicWarehouseDao;
	
	@Autowired
	private DicWarehouseAreaMapper areaDao;
	
	
	@Override
	public List<DicWarehouse> listWhIdAndWhCodeAndWhName() throws Exception {

		return dicWarehouseDao.selectAllWhIdAndWhCodeAndWhName();
	}

	

	@Override
	public List<Map<String, Object>> queryWarehouseList(Map<String, Object> paramMap) {

		return dicWarehouseDao.listWarehouseOnPaging(paramMap);
	}
	
	@Override
	public
	List<Map<String, Object>> queryLoationList(Map<String, Object> paramMap){

		return dicWarehouseDao.listLocationOnPaging(paramMap);
	}
	@Override
	public JSONObject addOrUpdateWarehouse(String whId, JSONArray locationArray,DicWarehouse dw) throws Exception {
		JSONObject body = new JSONObject();
		DicWarehouse isUpdateOrAddObj = null;
		if (UtilString.isNullOrEmpty(whId) || UtilString.isNullOrEmpty(dw.getWhCode()) || UtilString.isNullOrEmpty(dw.getWhName())) {
			
			body.put("is_success", 1);
			body.put("message", "不可以有空值!");
		} else {
			isUpdateOrAddObj = dicWarehouseDao.selectByPrimaryCode(dw.getWhCode());
			if (isUpdateOrAddObj == null) {
				if (UtilString.getIntForString(whId) == 0) {
					dicWarehouseDao.insertSelective(dw);
					if(locationArray.size()>0){
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("locationArray", locationArray);
						paramMap.put("whId", dw.getWhId());
						dicWarehouseDao.updateLocationByArray(paramMap);
					}
					
				} else {
					dw.setWhId(UtilString.getIntForString(whId));
			
					dicWarehouseDao.updateByPrimaryKeySelective(dw);
					if(locationArray.size()>0){
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("locationArray", locationArray);
						paramMap.put("whId", dw.getWhId());
						dicWarehouseDao.updateLocationByWhId(dw.getWhId());
						dicWarehouseDao.updateLocationByArray(paramMap);
					}else {
						dicWarehouseDao.updateLocationByWhId( dw.getWhId());
					}
					
				}
				body.put("is_success", 0);
				body.put("message", "保存成功");
			} else {
				if (UtilString.getIntForString(whId) == 0) {
					body.put("is_success", 1);
					body.put("message", "已经存在该仓库代码!");
				} else {
					if (isUpdateOrAddObj.getWhId().intValue() == UtilString.getIntForString(whId)) {
						dw.setWhId(UtilString.getIntForString(whId));
						
						dicWarehouseDao.updateByPrimaryKeySelective(dw);
						
						if(locationArray.size()>0){
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("locationArray", locationArray);
							paramMap.put("whId", dw.getWhId());
							dicWarehouseDao.updateLocationByWhId(dw.getWhId());
							dicWarehouseDao.updateLocationByArray(paramMap);
						}else {
							dicWarehouseDao.updateLocationByWhId( dw.getWhId());
						}
						
						body.put("is_success", 0);
						body.put("message", "保存成功");
						
					} else {
						body.put("is_success", 1);
						body.put("message", "已经存在该仓库代码!");
					}
				}
			}
		}

		
		return body;
	}
	
	
	@Override
	public Map<String, Object> getWarehouseById(Integer whId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicWarehouse dw = null;
			
			List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();
			

			dw = dicWarehouseDao.selectByPrimaryKey(whId);
			if (null != dw) {
			
				locationList = dicWarehouseDao.queryLocationByWhId(whId);

				

			} else {
				dw = new DicWarehouse();
			}

			body.put("wh_id", dw.getWhId());
			body.put("wh_code", dw.getWhCode());
			body.put("wh_name", dw.getWhName());
			body.put("address", dw.getAddress());
		
			
			if (locationList != null && locationList.size() > 0) {
				body.put("location_list", locationList);
			}else {
				List<Map<String, Object>> locationListNull = new ArrayList<Map<String, Object>>();
				
				body.put("location_list", locationListNull);
			}

			
		} catch (Exception e) {
			
		}

		return body;

	}
	
	@Override
	public List<Map<String, Object>> queryWarehouseAreaList(Map<String, Object> paramMap) {

		return areaDao.listAreaOnPaging(paramMap);
	}
	
	
	
	@Override
	public JSONObject addOrUpdateWarehouseAreas(String areaId,DicWarehouseArea dw) throws Exception {
		JSONObject body = new JSONObject();
		DicWarehouseArea isUpdateOrAddObj = null;
		if (UtilString.isNullOrEmpty(areaId) || UtilString.isNullOrEmpty(dw.getAreaCode()) || UtilString.isNullOrEmpty(dw.getAreaName())) {
			
			body.put("is_success", 1);
			body.put("message", "不可以有空值!");
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areaCode", dw.getAreaCode());
			map.put("whId", dw.getWhId());
			isUpdateOrAddObj = areaDao.selectByPrimaryCode(map);
			if (isUpdateOrAddObj == null) {
				if (UtilString.getIntForString(areaId) == 0) {
					areaDao.insertSelective(dw);
					
					
				} else {
					dw.setAreaId(UtilString.getIntForString(areaId));
			
					areaDao.updateByPrimaryKeySelective(dw);
					
				}
				body.put("is_success", 0);
				body.put("message", "保存成功");
			} else {
				if (UtilString.getIntForString(areaId) == 0) {
					body.put("is_success", 1);
					body.put("message", "已经存在该存储区代码!");
				} else {
					if (isUpdateOrAddObj.getAreaId().intValue() == UtilString.getIntForString(areaId)) {
						dw.setAreaId(UtilString.getIntForString(areaId));
						
						areaDao.updateByPrimaryKeySelective(dw);
						
						body.put("is_success", 0);
						body.put("message", "保存成功");
						
					} else {
						body.put("is_success", 1);
						body.put("message", "已经存在该已经存在该存储区代码代码!");
					}
				}
			}
		}

		
		return body;
	}
	
	

	@Override
	public Map<String, Object> getWarehouseAreaById(Integer areaId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicWarehouseArea dw = null;
			
			dw = areaDao.selectByPrimaryKey(areaId);
			if (null == dw) {
				dw = new DicWarehouseArea();
			} 

			body.put("area_id", dw.getAreaId());
			body.put("area_code", dw.getAreaCode());
			body.put("area_name", dw.getAreaName());
			body.put("wh_id", dw.getWhId());
			body.put("check_method", dw.getCheckMethod());
			body.put("type_input", dw.getTypeInput());
			body.put("type_output", dw.getTypeOutput());
			body.put("mix", dw.getMix());

			
		} catch (Exception e) {
			
		}

		return body;

	}
	
	@Override
	public List<Map<String, Object>> queryWarehouseList() {
		return dicWarehouseDao.listWarehouse();
	}
	
}
