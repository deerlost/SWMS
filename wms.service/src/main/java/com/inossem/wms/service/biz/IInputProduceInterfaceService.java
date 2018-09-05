package com.inossem.wms.service.biz;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizStockInputHead;

import net.sf.json.JSONObject;

public interface IInputProduceInterfaceService {
	BizStockInputHead insertProduction(CurrentUser user, JSONObject json) throws Exception;
}
