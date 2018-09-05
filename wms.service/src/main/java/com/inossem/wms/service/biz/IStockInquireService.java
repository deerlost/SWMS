package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import net.sf.json.JSONObject;

public interface IStockInquireService {

    List<Map<String, Object>> queryInputStockOutAndInOnPaging(Map<String, Object> param);

    List<Map<String, Object>> queryTransStockOutAndInOnPaging(Map<String, Object> param);

    List<Map<String, Object>> queryTransStockCwOutAndInOnPaging(Map<String, Object> param);

    List<Map<String, Object>> queryOutputStockOutAndInOnPaging(Map<String, Object> param);

    List<Map<String, Object>> queryStockDaysOnPaging(Map<String, Object> param);

    List<Map<String, Object>> queryStockOutAndInOnPaging(Map<String, Object> param) throws Exception;

    public List<Map<String, Object>> getNewMaps(JSONObject re, List<Map<String, Object>> stockDaysMaps)
            throws Exception;

    Map<String, Object> queryPrice(Map<String, Object> param);

    Map<String, Object> queryLocPrice(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> queryLocPriceforCity(Map<String, Object> param) throws Exception;

    Map<String, Object> queryLocPricePortable(Map<String, Object> param) throws Exception;

    public List<Map<String, Object>> downloadLocPrice(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> queryFailMES(Map<String, Object> param) throws Exception;

    JSONObject queryBaseInfo(CurrentUser cUser) throws Exception;
}
