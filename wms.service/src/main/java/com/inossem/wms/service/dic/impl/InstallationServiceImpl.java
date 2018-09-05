package com.inossem.wms.service.dic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicInstallationMapper;
import com.inossem.wms.model.dic.DicInstallation;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.service.dic.IInstallationService;
import com.inossem.wms.util.UtilObject;

import net.sf.json.JSONObject;
@Transactional
@Service("installationService")
public class InstallationServiceImpl implements IInstallationService{
   @Autowired
   private DicInstallationMapper dicInstallationMapper;
	@Override
	public List<DicInstallation> getDicInstallationList(Map<String, Object> parameter) {
	
		return dicInstallationMapper.selectInstallationOnpaging(parameter);
	}
	@Override
	public boolean saveOrUpdateProductLine(JSONObject json) {
		 boolean isAdd=true;
		 DicInstallation dicInstallation=new DicInstallation();
		if(json.containsKey("installation_id")) {
			dicInstallation.setInstallationId(UtilObject.getIntegerOrNull(json.get("installation_id")));		
			isAdd=false;
		}
		dicInstallation.setInstallationName(UtilObject.getStringOrEmpty(json.get("installation_name")));
		dicInstallation.setIsDelete(EnumBoolean.FALSE.getValue());
		if(isAdd) {
			dicInstallationMapper.insertSelective(dicInstallation);
		}else {
			dicInstallationMapper.updateByPrimaryKeySelective(dicInstallation);
		}
		return true;
	}
	@Override
	public int deleteInstallation(Integer installationId) {
		
		return dicInstallationMapper.updateIsDeleteByKey(installationId);
	}

}
