package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicInstallation;

import net.sf.json.JSONObject;

public interface IInstallationService {
   List<DicInstallation>	getDicInstallationList(Map<String,Object> parameter);
   
   boolean  saveOrUpdateProductLine(JSONObject json);
   
    int   deleteInstallation(Integer installationId);
}
