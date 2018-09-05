package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;

public interface BizReceiptAttachmentMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(BizReceiptAttachment record);

	int insertSelective(BizReceiptAttachment record);

	BizReceiptAttachment selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(BizReceiptAttachment record);

	int updateByPrimaryKey(BizReceiptAttachment record);

	List<BizReceiptAttachmentVo> selectByReceipt(Map<String, Object> map);

	int deleteByReceiptId(int receiptId);

	int deleteLogicalByReceiptId(int receiptId);

	int deleteByUUID(Map<String, Object> map);
}