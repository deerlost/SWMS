package com.inossem.wms.dao.wf;

import com.inossem.wms.model.wf.WfReceipt;
import com.inossem.wms.model.wf.WfReceiptKey;

public interface WfReceiptMapper {
    int deleteByPrimaryKey(WfReceiptKey key);

    int insert(WfReceipt record);

    int insertSelective(WfReceipt record);

    WfReceipt selectByPrimaryKey(WfReceiptKey key);

    int updateByPrimaryKeySelective(WfReceipt record);

    int updateByPrimaryKey(WfReceipt record);
}