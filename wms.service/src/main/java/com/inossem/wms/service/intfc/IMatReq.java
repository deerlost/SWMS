package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IMatReq {
	JSONArray getReferencePrice(String[] matCodes, String ftyCode, String userId) throws Exception;

	String chechMatForOther(JSONArray ary, String user_id) throws Exception;

	JSONArray getCostCenter(String ftyCode, String costObjCode, String costObjname, String matCode, int costObjType,
			String userId) throws Exception;

	JSONArray getCostObject(String moveType, String ftyCode, String costObjCode, String costObjName, String userId)
			throws Exception;

	String checkWorkReceiptCode(List<Map<String, Object>> map, String userId) throws Exception;

	String submitMatReq(BizMatReqHeadVo bizMatReqHeadVo, List<BizMatReqItemVo> itemList, User createUser,
			User firstApproveUser, User lastApproveUser) throws Exception;

}
