package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizInspectNoticeItem;

public interface BizInspectNoticeItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizInspectNoticeItem record);

    int insertSelective(BizInspectNoticeItem record);

    BizInspectNoticeItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizInspectNoticeItem record);

    int updateByPrimaryKey(BizInspectNoticeItem record);
}