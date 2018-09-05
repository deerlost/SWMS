package com.inossem.wms.model.dic;

import java.util.List;

public class DicBoard {
	List<DicCorp> corpList;
	
    public List<DicCorp> getCorpList() {
		return corpList;
	}

	public void setCorpList(List<DicCorp> corpList) {
		this.corpList = corpList;
	}

	private Integer boardId;

    private String boardName;

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName == null ? null : boardName.trim();
    }
}