package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizStockOutputReturnItem;
import com.inossem.wms.model.vo.BizStockOutputReturnItemVo;

public interface BizStockOutputReturnItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizStockOutputReturnItem record);

    int insertSelective(BizStockOutputReturnItem record);

    BizStockOutputReturnItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizStockOutputReturnItem record);

    int updateByPrimaryKey(BizStockOutputReturnItem record);
    
    int updateForMatDocIdAndMatDocRid (BizStockOutputReturnItem record);
    
    
    List<BizStockOutputReturnItemVo> selectSaleOrReserveReturnItem(Integer returnId);
    
    List<BizStockOutputReturnItemVo> selectSaleReturnItems(Integer returnId);
    
    List<BizStockOutputReturnItem> selectItemsByReturnId(Integer returnId);
    
    int deleteItemsByReturnId(Integer returnId);
    
    BizStockOutputReturnItem selectItemByReturnIdAndReturnRid(Map<String, Object> map);
    
    // 查询退库单库存地点是否是唯一
  	int selectCountByLocation( Integer returnId);
  	
  	int updateMesDocCodeByReturnId(Map<String, Object> map);//更新ERP凭证和MES凭证
  	
  	//删除退库单明细
  	int updateItemsToDelete( Integer returnId);
  	
    
}