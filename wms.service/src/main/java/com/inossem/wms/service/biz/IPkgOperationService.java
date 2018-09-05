package com.inossem.wms.service.biz;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.vo.BizPkgOperationHeadVo;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

public interface IPkgOperationService {
	
    JSONObject savePkgOperationData(JSONObject jsonData, CurrentUser user) throws Exception;

    List<BizPkgOperationHeadVo> selectPkgList(Map<String, Object> param);

    List<Map<String, Object>> selectMaterialList(Map<String, Object> param);

    Map<String, Object> selectPkgOrPalletList(Integer packageTypeId,String keyword,String type)throws Exception;

    BizPkgOperationHeadVo selectPkgDetailById(Integer pkgOperationId);

    DicWarehousePallet createPkgWarehousePallet(String userId)throws Exception;
    
    Map<String, Object> selectPkgClassLineInstallationList(String userId) ;
    
    Map<String,Object> selectOrderViewBySap(String code,String userId) throws Exception;
    
    String deletePkgOperation(Integer id)throws Exception;
    
    Map<String,Object> selectPackegeInfo(String packageCode)throws Exception;;
    
}
