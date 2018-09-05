package com.inossem.wms.service.intfc.sap;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.log.LogReceiptUserMapper;
import com.inossem.wms.model.log.LogReceiptUser;
import com.inossem.wms.service.intfc.IApprove;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("approveImpl")
public class ApproveImpl implements IApprove {
	@Autowired
	private LogReceiptUserMapper LogReceiptUserDao;

	@Override
	public boolean getVerprBySap(ArrayList<LogReceiptUser> list, String userId) throws Exception {
		boolean isSucess = false;
		// ArrayList<LogReceiptUser> logReceiptUserList =
		// LogReceiptUserDao.selectAllLogReceiptUser();
		JSONObject params = new JSONObject();
		JSONObject I_IMPORT = new JSONObject();
		String ZERNAM = userId;
		String ZIMENO = "";
		Date date = new Date();
		String dateStr = UtilString.getLongStringForDate(date);
		String ZDATE = dateStr.substring(0, 10);
		String ZTIME = dateStr.substring(11, 19);
		I_IMPORT.put("ZTYPE", "30");
		I_IMPORT.put("ZERNAM", ZERNAM);
		I_IMPORT.put("ZIMENO", ZIMENO);
		I_IMPORT.put("ZDATE", ZDATE);
		I_IMPORT.put("ZTIME", ZTIME);
		JSONArray I_LIST = new JSONArray();
		for (LogReceiptUser obj : list) {
			if (!UtilString.isNullOrEmpty(obj.getReceiptCode())) {
				JSONObject item = new JSONObject();
				item.put("MBLNR", obj.getMatDocCode());
				item.put("ZTOINDEX", obj.getReceiptCode());
				item.put("ZIN_CHECK1", obj.getFirstUserName());// 入库一级审核人
				item.put("ZIN_CHDATA1", UtilString.getShortStringForDate(obj.getFirstTime()));// 入库一级审核日期
				item.put("ZOUT_CHECK1", obj.getFirstUserName());// 出库一级审核人
				item.put("ZOUT_CHDATA1", UtilString.getShortStringForDate(obj.getFirstTime()));// 出库一级审核日期

				item.put("ZIN_CHECK2", obj.getSecondUserName());// 入库二级审核人
				item.put("ZIN_CHDATA2", UtilString.getShortStringForDate(obj.getSecondTime()));// 入库二级审核日期
				item.put("ZOUT_CHECK2", obj.getSecondUserName());// 出库二级审核人
				item.put("ZOUT_CHDATA2", UtilString.getShortStringForDate(obj.getSecondTime()));// 出库二级审核日期

				item.put("ZPRINCIPAL", obj.getExtraUserName());// 仓库主管审核
				// 最后一个
				item.put("Z_CHECKDATA", UtilString.getShortStringForDate(obj.getExtraTime()));// 仓库主管审核日期

				item.put("BWART", obj.getMoveTypeCode());// 移动类型
				item.put("ZOPERATOR", obj.getCreateUserName());// 业务员 创建人
				I_LIST.add(item);
				LogReceiptUserDao.insertSelective(obj);
			}
		}
		params.put("I_IMPORT", I_IMPORT);
		params.put("I_ZBACK", I_LIST);
		JSONObject returnObj = UtilREST
				.executePostJSONTimeOut(Const.SAP_API_URL + "int_30?sap-client=" + Const.SAP_API_CLIENT, params, 3);

		JSONObject RETURN = returnObj.getJSONObject("RETURN");

		String TYPE = RETURN.getString("TYPE");
		if ("S".equals(TYPE)) {
			isSucess = true;
		} else {
			isSucess = false;
		}

		return isSucess;
	}
}
