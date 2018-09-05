package com.inossem.wms.service.dic.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.model.dic.DicClassType;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.service.dic.IClassTypeService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;
@Transactional
@Service("classTypeService")
public class ClassTypeServiceImpl implements IClassTypeService{
    @Autowired
    private DicClassTypeMapper dicClassTypeMapper;
	@Override
	public List<Map<String,Object>> getDicClassTypeList(Map<String,Object> parameter) {
		
		return dicClassTypeMapper.selectClassTypeOnpaging(parameter);
	}
	@Override
	public boolean saveOrUpdateClassType(JSONObject json) throws ParseException {
		DicClassType dicClassType=new DicClassType();
		boolean isAdd=true;
		int i=-1;
		if(json.containsKey("class_type_id")) {
			dicClassType.setClassTypeId(UtilObject.getIntegerOrNull(json.get("class_type_id")));
			isAdd=false;
		}
		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
		String startTime=UtilObject.getStringOrEmpty(json.get("start_time"));
		String endTime=UtilObject.getStringOrEmpty(json.get("end_time"));		
		dicClassType.setClassTypeName(UtilObject.getStringOrEmpty(json.get("class_type_name")));
		dicClassType.setStartTime(sdf.parse(startTime));
		dicClassType.setEndTime(sdf.parse(endTime));
		dicClassType.setIsDelete(EnumBoolean.FALSE.getValue());
		if(isAdd) {
			i=dicClassTypeMapper.insertSelective(dicClassType);
		}else {
			i=dicClassTypeMapper.updateByPrimaryKeySelective(dicClassType);
		}	
		if(i<1) {
			return false;
		}
		return true;
	}
	@Override
	public int updateIsDeleteByKey(Integer classTypeId) {
		
		return dicClassTypeMapper.updateIsDeleteByKey(classTypeId);
	}
	
	
	

}
