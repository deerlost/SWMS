package com.inossem.wms.dao.syn;

import com.inossem.wms.model.syn.SynInspectNoticeItem;

public interface SynInspectNoticeItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(SynInspectNoticeItem record);

    int insertSelective(SynInspectNoticeItem record);

    SynInspectNoticeItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(SynInspectNoticeItem record);

    int updateByPrimaryKey(SynInspectNoticeItem record);
}