package com.inossem.wms.model.vo;

import com.inossem.wms.model.dic.DicFactory;

public class DicFactoryVo extends DicFactory {
	private String corpName;
	private int boardId;

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
}
