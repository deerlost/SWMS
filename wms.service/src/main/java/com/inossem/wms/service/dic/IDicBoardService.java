package com.inossem.wms.service.dic;

import java.util.List;

import com.inossem.wms.model.dic.DicBoard;

/**
 * 板块
 * 
 * @author 刘宇 2018.03.02
 *
 */
public interface IDicBoardService {
	/**
	 * 查询所有板块id和描述
	 * 
	 * @author 刘宇 2018.03.02
	 * @return
	 * @throws Exception
	 */
	List<DicBoard> listBoard() throws Exception;
}
