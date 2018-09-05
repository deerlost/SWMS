package com.inossem.wms.dao.biz;

import com.inossem.wms.model.biz.BizDeliveryNoticeItem;

public interface BizDeliveryNoticeItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(BizDeliveryNoticeItem record);

    int insertSelective(BizDeliveryNoticeItem record);

    BizDeliveryNoticeItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(BizDeliveryNoticeItem record);

    int updateByPrimaryKey(BizDeliveryNoticeItem record);
}