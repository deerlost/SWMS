package com.inossem.wms.dao.wf;

import com.inossem.wms.model.wf.WfReceiptHistory;
import com.inossem.wms.model.wf.WfReceiptHistoryKey;

public interface WfReceiptHistoryMapper {
    int deleteByPrimaryKey(WfReceiptHistoryKey key);

    int insert(WfReceiptHistory record);

    int insertSelective(WfReceiptHistory record);

    WfReceiptHistory selectByPrimaryKey(WfReceiptHistoryKey key);

    int updateByPrimaryKeySelective(WfReceiptHistory record);

    int updateByPrimaryKey(WfReceiptHistory record);
}