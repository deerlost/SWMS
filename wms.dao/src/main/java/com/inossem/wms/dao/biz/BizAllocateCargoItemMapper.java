package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizAllocateCargoItem;
import com.inossem.wms.model.vo.BizAllocateCargoItemVo;

public interface BizAllocateCargoItemMapper {
	int deleteByPrimaryKey(Integer itemId);

	int insert(BizAllocateCargoItem record);

	int insertSelective(BizAllocateCargoItem record);

	BizAllocateCargoItem selectByPrimaryKey(Integer itemId);

	int updateByPrimaryKeySelective(BizAllocateCargoItem record);

	int updateByPrimaryKey(BizAllocateCargoItem record);

	// 配货单详细查询
	List<Map<String, Object>> selectCargoItem1(Integer allocate_cargo_id);

	// 根据配货单id删除配货详细
	int updateByAllocateCargoId(Map<String, Object> map);

	// 根据交货单号获得已配货数量
	Map<String,Object> getCountCargoed(Map<String, Object> condition);

	List<Map<String, Object>> selectByCargoId(Integer allocate_cargo_id);

	List<BizAllocateCargoItemVo> selectByReferReceiptCode(Map<String, Object> param);
	
	Integer deleteAllocateItemByHeadId(Integer id);
}