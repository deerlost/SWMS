package com.inossem.wms.model.dic;

import java.util.Date;

public class DicAccountPeriod implements Comparable<DicAccountPeriod>{
	
	private Integer index;
	
    public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	private Integer periodId;

    private Integer corpId;

    private Integer accountYear;

    private Byte accountMonth;

    private Date accountBeginDate;

    private Date accountEndDate;

    private Date accountFactDate;

    private Integer boardId;

    private Byte isDelete;

    private String createUser;

    private Date createTime;

    private Date modifyTime;

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Integer getCorpId() {
        return corpId;
    }

    public void setCorpId(Integer corpId) {
        this.corpId = corpId;
    }

    public Integer getAccountYear() {
        return accountYear;
    }

    public void setAccountYear(Integer accountYear) {
        this.accountYear = accountYear;
    }

    public Byte getAccountMonth() {
        return accountMonth;
    }

    public void setAccountMonth(Byte accountMonth) {
        this.accountMonth = accountMonth;
    }

    public Date getAccountBeginDate() {
        return accountBeginDate;
    }

    public void setAccountBeginDate(Date accountBeginDate) {
        this.accountBeginDate = accountBeginDate;
    }

    public Date getAccountEndDate() {
        return accountEndDate;
    }

    public void setAccountEndDate(Date accountEndDate) {
        this.accountEndDate = accountEndDate;
    }

    public Date getAccountFactDate() {
        return accountFactDate;
    }

    public void setAccountFactDate(Date accountFactDate) {
        this.accountFactDate = accountFactDate;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

	@Override
	public int compareTo(DicAccountPeriod o) {
		int i = 0;
		if(this.boardId.compareTo(o.getBoardId())==1){
			i=1;
		}else if(this.boardId.compareTo(o.getBoardId())==-1){
			i=-1;
		}else{
			if(this.corpId.compareTo(o.getCorpId())>0){
				i=1;
			}else if(this.corpId.compareTo(o.getCorpId())<0){
				i=-1;
			}else{
				if(this.accountYear.compareTo(o.getAccountYear())==1){
					i=1;
				}else if(this.accountYear.compareTo(o.getAccountYear())==-1){
					i=-1;
				}else{
					
						if(this.accountBeginDate.after(o.getAccountBeginDate())){
							i=1;
						}else if(this.accountBeginDate.before(o.getAccountBeginDate())){
							i=-1;
						}else{
							i=0;
						}
					
				}
			}
		}
		
		return i;
	}
}