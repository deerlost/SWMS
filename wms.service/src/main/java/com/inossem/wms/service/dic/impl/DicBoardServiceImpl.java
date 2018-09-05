package com.inossem.wms.service.dic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.service.dic.IDicBoardService;

/**
 * 板块
 * 
 * @author 刘宇 2018.03.02
 *
 */
@Transactional
@Service("dicBoardService")
public class DicBoardServiceImpl implements IDicBoardService {
	@Autowired
	private DicBoardMapper dicBoardDao;

	@Override
	public List<DicBoard> listBoard() throws Exception {

		return dicBoardDao.selectAllBoard();
	}

}
