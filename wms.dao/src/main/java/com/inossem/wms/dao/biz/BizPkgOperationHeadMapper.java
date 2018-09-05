package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizPkgOperationHead;
import com.inossem.wms.model.vo.BizPkgOperationHeadVo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BizPkgOperationHeadMapper {
    int deleteByPrimaryKey(Integer pkgOperationId);

    int insert(BizPkgOperationHead head);

    int insertSelective(BizPkgOperationHead head);

    BizPkgOperationHead selectByPrimaryKey(Integer pkgOperationId);

    BizPkgOperationHeadVo selectDetailById(Integer pkgOperationId);

    int updateByPrimaryKeySelective(BizPkgOperationHead head);

    int updateByPrimaryKey(BizPkgOperationHead head);

    List<BizPkgOperationHeadVo> selectPkgListOnPaging(Map<String, Object> param);
    
    int deleteBizPkgOperationHead(@Param("pkgOperationId")Integer pkgOperationId);
    
    Map<String,Object> getProductionInfo(@Param("packageId")Integer packageId);
}
