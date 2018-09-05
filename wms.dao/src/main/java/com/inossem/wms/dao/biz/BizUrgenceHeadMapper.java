package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizUrgenceHead;
import com.inossem.wms.model.vo.BizUrgenceReqVo;
import com.inossem.wms.model.vo.BizUrgenceResVo;

public interface BizUrgenceHeadMapper {
    int deleteByPrimaryKey(Integer urgenceId);

    int insert(BizUrgenceHead record);

    int insertSelective(BizUrgenceHead record);

    BizUrgenceHead selectByPrimaryKey(Integer urgenceId);
    
    int updateByPrimaryKeySelective(BizUrgenceHead record);

    int updateByPrimaryKey(BizUrgenceHead record);
    
    List<BizUrgenceResVo> listBizUrgenceHeadOnPaging (BizUrgenceReqVo record) ;
    
    BizUrgenceResVo selectHeadById(Integer urgenceId);
    
    List<BizUrgenceResVo> selectItemById (Integer urgenceId) ;
    
    int deleteHead(BizUrgenceHead record);
    
    List<Map<String,Object>> selectInOrder(Map<String,Object> param);
    
    List<Map<String,Object>> selectOutOrder(Map<String,Object> param);
    
    List<Map<String,Object>> getOutMat(Map<String,Object> param);
    
    int updateById(BizUrgenceHead record);
    
    List<Map<String,Object>> selectUserByIdAndType(Map<String,Object> param);
    
    List<BizUrgenceResVo> listBizUrgenceForApp (BizUrgenceReqVo record) ;
    
    
    List<BizUrgenceResVo> listBizDocUrgenceHeadOnPaging (Map<String,Object> param) ;
    
    List<BizUrgenceResVo> listBizDocUrgenceType(Map<String,Object> param);
    
    List<Map<String, Object>> listBizDocInstockOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocInstockType(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocOutstockOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocOutstockType(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocTaskOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocTaskReqOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocStockZlOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocstockTransportOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocTransportType(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocstockReturnOnPaging(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocReturnType(Map<String, Object> paramMap);
    
    List<Map<String, Object>> listBizDocAllocateOnPaging(Map<String, Object> paramMap);
    
   
    
    
}