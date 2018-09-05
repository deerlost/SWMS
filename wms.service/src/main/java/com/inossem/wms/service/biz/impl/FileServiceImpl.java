package com.inossem.wms.service.biz.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.service.biz.IFileService;

@Service("fileService")
public class FileServiceImpl implements IFileService {

	@Autowired
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;

	/**
	 * 添加附件方法
	 */
	@Override
	public int addReceiptAttachment(BizReceiptAttachment receiptAttachment) {
		return bizReceiptAttachmentDao.insertSelective(receiptAttachment);
	}

	/**
	 * 获取相关单据附件列表
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<BizReceiptAttachmentVo> getReceiptAttachments(int receiptId, int receiptType) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		return bizReceiptAttachmentDao.selectByReceipt(map);
	}

	/**
	 * 删除附件
	 */
	@Override
	public int removeReceiptAttachment(int attachment_id, String userId) {
		BizReceiptAttachment receiptAttachment = bizReceiptAttachmentDao.selectByPrimaryKey(attachment_id);
		if (receiptAttachment != null && receiptAttachment.getUserId() != null
				&& receiptAttachment.getUserId().equalsIgnoreCase(userId)) {
			return bizReceiptAttachmentDao.deleteByPrimaryKey(attachment_id);
		}
		return 0;
	}
}
