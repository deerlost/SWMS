package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizDeliveryNoticeHead;

public interface BizDeliveryNoticeHeadMapper {
    int deleteByPrimaryKey(Integer deliveryNoticeId);

    int insert(BizDeliveryNoticeHead record);

    int insertSelective(BizDeliveryNoticeHead record);

    BizDeliveryNoticeHead selectByPrimaryKey(Integer deliveryNoticeId);

    int updateByPrimaryKeySelective(BizDeliveryNoticeHead record);

    int updateByPrimaryKey(BizDeliveryNoticeHead record);
}