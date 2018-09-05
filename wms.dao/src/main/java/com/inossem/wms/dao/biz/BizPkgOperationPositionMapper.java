package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizPkgOperationPosition;
import com.inossem.wms.model.vo.BizPkgOperationPositionVo;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BizPkgOperationPositionMapper {
    int deleteByPrimaryKey(Integer itemPositionId);

    int insert(BizPkgOperationPosition bizPkgOperationPosition);

    int insertSelective(BizPkgOperationPosition bizPkgOperationPosition);

    BizPkgOperationPosition selectByPrimaryKey(Integer itemPositionId);

    int updateByPrimaryKeySelective(BizPkgOperationPosition bizPkgOperationPosition);

    int updateByPrimaryKey(BizPkgOperationPosition bizPkgOperationPosition);

    List<BizPkgOperationPositionVo> selectBizPkgOperationPositionList(@Param("pkgOperationId")Integer pkgOperationId,@Param("pkgOperationRid") Integer pkgOperationRid);

    int deleteBizPkgOperationPosition(@Param("pkgOperationId") Integer pkgOperationId);

    /**
     * 根据托盘code 包code 查询
     * @param map
     * 	palletCode
     * 	packageCode
     * @return
     */
    List<BizPkgOperationPositionVo> selectByPalletCodeOrPackageCode(Map<String, Object> map);
    
    int updatePkgSnById(@Param("pkgOperationId") Integer pkgOperationId);
}
