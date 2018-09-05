package com.inossem.wms.service.intfc;

import java.util.ArrayList;

import com.inossem.wms.model.log.LogReceiptUser;

public interface IApprove {
	/**
	 * 经办人sap审批
	 * 
	 * @author 刘宇 2018.03.29
	 * @param errorString
	 * @param errorStatus
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	boolean getVerprBySap(ArrayList<LogReceiptUser> list, String userId) throws Exception;
}
