package com.inossem.wms.service.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.vo.BizBusinessHistoryVo;

public interface IBusinessHistoryService {
	
	List<BizBusinessHistoryVo> selectHistoryList(Map<String,Object> param);

	List<Map<String, Object>> selectInAndOut(Map<String, Object> map);

}
