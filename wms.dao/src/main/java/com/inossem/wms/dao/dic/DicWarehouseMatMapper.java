package com.inossem.wms.dao.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.dic.DicWarehouseMat;
import com.inossem.wms.model.vo.BizStocktakeQueryMatVo;

public interface DicWarehouseMatMapper {
    int deleteByPrimaryKey(Integer whMatId);

    int insert(DicWarehouseMat record);

    int insertSelective(DicWarehouseMat record);

    DicWarehouseMat selectByPrimaryKey(Integer whMatId);

    int updateByPrimaryKeySelective(DicWarehouseMat record);

    int updateByPrimaryKey(DicWarehouseMat record);
   
    int insertDataForSapMatSync(Integer matId);
    
    /**
	 * @Description: 移动端获取物料信息列表
	 * @param linkMobile 包含工厂编码和库存地点编码信息
	 */
	public List<Map<String, Object>> findMaterielListByFactoryCodeAndInventoryAddressCode(Map<String, Object> map) throws Exception;
	/**
	 * @Description: 根据工厂编号和库存地点代码查询物料信息
	 * @param kcpdQueryVo 包含工厂代码，库存地点编码，及模糊查询条件
	 * @return
	 */
	public List<BizStocktakeQueryMatVo> findMaterielListByFactoryCodeAndlocation(Map<String, Object> map) throws Exception;
}