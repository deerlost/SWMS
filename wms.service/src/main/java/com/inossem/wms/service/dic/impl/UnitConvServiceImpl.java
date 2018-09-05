package com.inossem.wms.service.dic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicMaterialGroupMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.unit.UnitConvMapper;
import com.inossem.wms.dao.unit.UnitConvVarMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.unit.UnitConv;
import com.inossem.wms.model.unit.UnitConvVar;
import com.inossem.wms.service.dic.IUnitConvService;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("unitConvService")
public class UnitConvServiceImpl implements IUnitConvService {

	private static final Logger logger = LoggerFactory.getLogger(UnitConvServiceImpl.class);
	
	@Autowired
	private UnitConvMapper unitConvDao;
	
	@Autowired
	private UnitConvVarMapper unitConvVarDao;
	
	
	@Autowired
	private DicUnitMapper dicUnitMapper;
	
	@Autowired
	private DicMaterialGroupMapper dicMaterialGroupMapper;
	@Override
	public List<Map<String, Object>> listUnitOnPaging(Map<String, Object> map) {
	
		return unitConvDao.selectUnitConvOnPaging(map);
	}
	
	
	@Override
	public JSONObject  addOrUpdateUnit(String matGroupCode, String unitConvId, JSONArray unitArray,UnitConv unitConv)throws Exception {
		JSONObject body = new JSONObject();
		UnitConv isUpdateOrAddObj = null;
		if (UtilString.isNullOrEmpty(unitConvId) || UtilString.isNullOrEmpty(unitConv.getConstName())|| UtilString.isNullOrEmpty(matGroupCode)) {
			
			body.put("is_success", 1);
			body.put("message", "不可以有空值!");
		} else {
			
			Integer groupId = dicMaterialGroupMapper.selectIdByCode(matGroupCode);
			
			if(groupId==null||groupId==0) {
				body.put("is_success", 1);
				body.put("message", "物料组不存在!");
				return body;
			}
			unitConv.setMatGroupId(groupId);
			
			Map<String, Object> checkMap = new HashMap<String, Object>();
			checkMap.put("matGroupId", groupId);
			checkMap.put("matId", unitConv.getMatId());
			
			isUpdateOrAddObj = unitConvDao.selectObjByGroupAndMat(checkMap);
			if (isUpdateOrAddObj == null) {
				if (UtilString.getIntForString(unitConvId) == 0) {
					
					unitConvDao.insertSelective(unitConv);
					if(unitArray.size()>0){
						for(int i=0;i<unitArray.size();i++) {
							UnitConvVar  unitConvVar = new UnitConvVar();
							unitConvVar.setUnitConvId(unitConv.getUnitConvId());
							JSONObject vr = unitArray.getJSONObject(i);
							unitConvVar.setUnitId(Integer.parseInt(vr.getString("unit_id")));
							unitConvVar.setName(vr.getString("name"));
							unitConvVarDao.insertSelective(unitConvVar);
						}
					}
					
				} else {
					unitConv.setUnitConvId(UtilString.getIntForString(unitConvId));
			
					unitConvDao.updateByPrimaryKeySelective(unitConv);
					if(unitArray.size()>0){
						
						unitConvVarDao.deleteByPrimaryConvId(unitConv.getUnitConvId());
						for(int i=0;i<unitArray.size();i++) {
							UnitConvVar  unitConvVar = new UnitConvVar();
							unitConvVar.setUnitConvId(unitConv.getUnitConvId());
							JSONObject vr = unitArray.getJSONObject(i);
							unitConvVar.setUnitId(Integer.parseInt(vr.getString("unit_id")));
							unitConvVar.setName(vr.getString("name"));
							unitConvVarDao.insertSelective(unitConvVar);
						}
					}else {
						unitConvVarDao.deleteByPrimaryConvId(unitConv.getUnitConvId());
					}
					
				}
				body.put("is_success", 0);
				body.put("message", "保存成功");
			} else {
				if (UtilString.getIntForString(unitConvId) == 0) {
					body.put("is_success", 1);
					body.put("message", "该物料组已经存在!");
				} else {
					if (isUpdateOrAddObj.getUnitConvId().intValue() == UtilString.getIntForString(unitConvId)) {
						unitConv.setUnitConvId(UtilString.getIntForString(unitConvId));
						
						unitConvDao.updateByPrimaryKeySelective(unitConv);
						
						if(unitArray.size()>0){
							
							unitConvVarDao.deleteByPrimaryConvId(unitConv.getUnitConvId());
							for(int i=0;i<unitArray.size();i++) {
								UnitConvVar  unitConvVar = new UnitConvVar();
								unitConvVar.setUnitConvId(unitConv.getUnitConvId());
								JSONObject vr = unitArray.getJSONObject(i);
								unitConvVar.setUnitId(Integer.parseInt(vr.getString("unit_id")));
								unitConvVar.setName(vr.getString("name"));
								unitConvVarDao.insertSelective(unitConvVar);
							}
						}else {
							unitConvVarDao.deleteByPrimaryConvId(unitConv.getUnitConvId());
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
	public JSONObject  deleteOrder(Map<String,Object> map)throws Exception {
		JSONObject body = new JSONObject();
		try {
			unitConvDao.deleteUnitByArray(map);
			unitConvDao.deleteUnitVarByArray(map);
			body.put("is_success", 0);
			body.put("message", "删除成功");
		}catch(Exception e) {
			logger.error("", e);
			body.put("is_success", 0);
			body.put("message", "删除失败");
		}
		
		
		return body;
	}
	
	
	@Override
	public Map<String, Object> getMatByCode(String matCode) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			
			List<Map<String, Object>> matList = unitConvDao.selectMatByCode(matCode);
			if(matList!=null&&matList.size()>0) {
				body = matList.get(0);
			}else {
				body.put("matId", 0);
				body.put("matCode", "");
				body.put("matName", "");
				body.put("matGroupId", 0);
				body.put("matGroupCode", "");
				body.put("unitId", 0);
				body.put("unitCode", "");
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return body;
	}
	
	@Override
	public Map<String, Object>  getUnitConvById(Integer unitConvId,CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			UnitConv dw = null;
			
			List<Map<String, Object>> unitConvVarList = new ArrayList<Map<String, Object>>();

			dw = unitConvDao.selectConvByPrimaryKey(unitConvId);
			if (null != dw) {
				body.put("unit_conv_id", dw.getUnitConvId());
				body.put("mat_group_id", dw.getMatGroupId());
				body.put("mat_group_code", dw.getMatGroupCode());
				body.put("mat_id", dw.getMatId());
				body.put("mat_code", dw.getMatCode());
				body.put("mat_name", dw.getMatName());
				body.put("unit_id", dw.getUnitId());
				body.put("unit_code", dw.getUnitCode());
				
				body.put("const_name", dw.getConstName());
				body.put("const_value", dw.getConstValue());
				unitConvVarList = unitConvVarDao.queryUnitVarByConvId(unitConvId);

				

			} else {
				dw = new UnitConv();
			}

			
		
			
			if (unitConvVarList != null && unitConvVarList.size() > 0) {
				body.put("unit_conv_var_list", unitConvVarList);
			}else {
				List<Map<String, Object>> unitConvVarListNull = new ArrayList<Map<String, Object>>();
				
				body.put("unit_conv_var_list", unitConvVarListNull);
			}

			
		} catch (Exception e) {
			
		}

		return body;

	}
	
	@Override
	public List<DicUnit> selectAllUnit() {
	
		return dicUnitMapper.selectAll();
	}


	@Override
	public DicUnit getUnitByCode(String unitCode) {
		
		return dicUnitMapper.getByCode(unitCode);
	}

}
