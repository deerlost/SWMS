package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizAllocateCargoHead;

public interface BizAllocateCargoHeadMapper {
    int deleteByPrimaryKey(Integer allocateCargoId);

    int insert(BizAllocateCargoHead record);

    int insertSelective(BizAllocateCargoHead record);

    BizAllocateCargoHead selectByPrimaryKey(Integer allocateCargoId);
   
    int updateByPrimaryKeySelective(BizAllocateCargoHead record);

    int updateByPrimaryKey(BizAllocateCargoHead record);

   //按条件查询所有配货单
    List<Map<String,Object>> selectCargoonpaging(Map<String,Object> map) throws Exception;
   
    Map<String,Object> getCargoHeadById(Integer allocateCargoId);
    
    /**
     * 根据交货单号获取 配货单 id code type
     * @param map
     * @return
     */
    List<Map<String,Object>> selectOrderCodeAndId(Map<String,Object> map); 
    
    int updateOrderStatus(@Param("allocateCargoId") Integer allocateCargoId,@Param("status") Byte status);
    
    /**
     * 校验是否已经被销售出库使用
     * @param map
     * @return
     */
    List<BizAllocateCargoHead> selectByReferReceiptCode(Map<String,Object> map);
    
    /**
     * 插入销售单信息
     * @param map
     * @return
     */
    int insertBeforeInfo(Map<String,Object> map);
    
    /**
     * 删除销售单信息
     * @param map
     * @return
     */
    int deleteBeforeInfo(Map<String,Object> map);
    
    /**
     * 
     * @param id
     * @return
     */
    Integer deleteAllocateHeadByHeadId(Integer id);
    
    /**
     * 销售出库修改配货单状态
     * @param map
     * @return
     */
    int updateOrderBySale(Map<String,Object> map);
    
    /**
     * 获取待出库配货单
     * @param code
     * @param userId
     * @return
     */
    List<Map<String,Object>> getCargoOrderList(@Param("code") String code,@Param("userId") String userId);
    
   /**
    * 校验单号是否已售未提业务
    * @param code
    * @return
    */
    List<Map<String,Object>> checkOrderCode(@Param("code") String code,@Param("type")Byte type);
}