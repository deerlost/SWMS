package com.inossem.wms.service.dic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicMoveReasonMapper;
import com.inossem.wms.model.dic.DicMoveReason;
import com.inossem.wms.service.dic.IDicMoveReasonService;

@Transactional
@Service("moveReasonService")
public class DicMoveReasonServiceImpl implements IDicMoveReasonService {

	@Autowired
	private DicMoveReasonMapper dicMoveReasonDao;

	@Override
	public List<DicMoveReason> getMoveTypeByMoveTypeId(int moveTypeId) {
		return dicMoveReasonDao.selectByMoveTypeId(moveTypeId);
	}
}
