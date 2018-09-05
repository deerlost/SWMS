package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizMatReqItem;
import com.inossem.wms.model.vo.BizMatReqItemVo;

public interface BizMatReqItemMapper {
	int deleteByPrimaryKey(Integer itemId);

	int insert(BizMatReqItem record);

	int insertSelective(BizMatReqItem record);

	BizMatReqItem selectByPrimaryKey(Integer itemId);

	int updateByPrimaryKeySelective(BizMatReqItem record);

	int updateByPrimaryKey(BizMatReqItem record);

	List<BizMatReqItemVo> selectBizMatReqItemVo(int matReqId);

	int deleteByMatReqId(int matReqId);

	int updateTakeDeliveryQty(Map<String, Object> map);

	BizMatReqItem selectByIdAndRid(BizMatReqItem record);

	int deleteLogicalByMatReqId(int matReqId);

	int updateForLastTask(BizMatReqItemVo record);

	int updateReserve(BizMatReqItemVo record);

	int updatePurchaseOrder(BizMatReqItemVo record);
}