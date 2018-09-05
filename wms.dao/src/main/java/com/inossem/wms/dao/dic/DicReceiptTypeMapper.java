package com.inossem.wms.dao.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicReceiptType;

public interface DicReceiptTypeMapper {
    int deleteByPrimaryKey(Byte receiptType);

    int insert(DicReceiptType record);

    int insertSelective(DicReceiptType record);

    DicReceiptType selectByPrimaryKey(Byte receiptType);

    int updateByPrimaryKeySelective(DicReceiptType record);

    int updateByPrimaryKey(DicReceiptType record);

    List<DicReceiptType> selectDicReceiptTypeList();
}