package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizInspectNoticeHead;

public interface BizInspectNoticeHeadMapper {
    int deleteByPrimaryKey(Integer inspectNoticeId);

    int insert(BizInspectNoticeHead record);

    int insertSelective(BizInspectNoticeHead record);

    BizInspectNoticeHead selectByPrimaryKey(Integer inspectNoticeId);

    int updateByPrimaryKeySelective(BizInspectNoticeHead record);

    int updateByPrimaryKey(BizInspectNoticeHead record);
}