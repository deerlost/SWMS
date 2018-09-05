package com.inossem.wms.dao.syn;

import com.inossem.wms.model.syn.SynInspectNoticeHead;

public interface SynInspectNoticeHeadMapper {
    int deleteByPrimaryKey(String inspectNoticeCode);

    int insert(SynInspectNoticeHead record);

    int insertSelective(SynInspectNoticeHead record);

    SynInspectNoticeHead selectByPrimaryKey(String inspectNoticeCode);

    int updateByPrimaryKeySelective(SynInspectNoticeHead record);

    int updateByPrimaryKey(SynInspectNoticeHead record);
}