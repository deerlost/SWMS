package com.inossem.wms.service.biz;

import java.util.List;

import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;

public interface IFileService {
	int addReceiptAttachment(BizReceiptAttachment receiptAttachment) throws Exception;

	List<BizReceiptAttachmentVo> getReceiptAttachments(int receiptId, int receiptType) throws Exception;

	int removeReceiptAttachment(int attachment_id, String userId) throws Exception;
}
