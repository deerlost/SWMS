package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.vo.BizStockTaskReqHeadVo;

public interface BizStockTaskReqHeadMapper {
	int deleteByPrimaryKey(Integer stockTaskReqId);

	int insert(BizStockTaskReqHead record);

	int insertSelective(BizStockTaskReqHead record);

	BizStockTaskReqHead selectByPrimaryKey(Integer stockTaskReqId);

	int updateByPrimaryKeySelective(BizStockTaskReqHead record);

	int updateByPrimaryKey(BizStockTaskReqHead record);

	List<BizStockTaskReqHeadVo> getBizStockTaskReqHeadListOnPaging(BizStockTaskReqHeadVo paramVo);
	
	//川维 上架下架列表
	List<Map<String, Object>> getBizStockTaskReqHeadListForCWOnPaging(Map<String, Object> map);

	int deleteByMatDocIdIfItemIsNull(Integer matDocId);

	BizStockTaskReqHead selectByBizStockTaskReqHead(BizStockTaskReqHead record);
	
	Byte selectMinStatusByReferIdAndType(Map<String, Object> map);
	
	List<BizStockTaskReqHead> getBizStockTaskReqHeadListByReferReipt(Map<String,Object> param);
	
	//川维退库冲销生成下架作业请求
	//int insertReqHeadForWriteoff (Map<String,Object> param);
	
	int updateDeleteByReferReceiptIdAndType(Map<String, Object> map);

	Integer countTaskByParam(Map<String, Object> map);
	
}