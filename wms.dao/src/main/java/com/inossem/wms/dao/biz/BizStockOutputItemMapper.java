package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizStockOutputItem;
import com.inossem.wms.model.vo.BizStockOutputItemVo;

public interface BizStockOutputItemMapper {

	BizStockOutputItem selectByPrimaryKey(Map<String,Object> param);
	
	List<BizStockOutputItemVo> selectItemByOrderId(Map<String,Object> param);
	
    int insertSelective(BizStockOutputItem record);
    
    int updateByPrimaryKeySelective(BizStockOutputItem record);
    
    int deleteItemByOrderId(Map<String,Object> param);
    
    int updateByOutPutId(Map<String,Object> sapReturn);
    
    /**
     * 销售退库 配货查销售出库单行项目
     * @param param
     * @return
     */
    List<Map<String, Object>> getOutputItemList(Map<String,Object> param);
    
	List<Map<String, Object>> queryOutputStockOutAndInOnPaging(Map<String, Object> param);
	//出库金额
	List<Map<String, Object>>	getOutputAmount(Map<String, Object> param);
	
	List<Map<String, Object>>	getAllOutput(Map<String, Object> param);
}