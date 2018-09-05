package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizPkgOperationItem;
import com.inossem.wms.model.vo.BizPkgOperationItemVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BizPkgOperationItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizPkgOperationItem bizPkgOperationItem);

    int insertSelective(BizPkgOperationItem bizPkgOperationItem);

    BizPkgOperationItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizPkgOperationItem bizPkgOperationItem);

    int updateByPrimaryKey(BizPkgOperationItem bizPkgOperationItem);

    List<BizPkgOperationItemVo> selectBizPkgOperationItemList(@Param("pkgOperationId") String pkgOperationId);

    int deleteBizPkgOperationItem(@Param("pkgOperationId") Integer pkgOperationId);
}
