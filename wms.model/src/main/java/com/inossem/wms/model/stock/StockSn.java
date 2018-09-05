package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

import com.inossem.wms.model.key.StockSnKey;

public class StockSn extends StockSnKey {

	private String debitOrCredit;

	public String getDebitOrCredit() {
		return debitOrCredit;
	}

	public void setDebitOrCredit(String debitOrCredit) {
		this.debitOrCredit = debitOrCredit;
	}

	private Integer id;
	private Integer matId;
	private BigDecimal qty;

	private Byte freeze;

	private Date createTime;

	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Byte getFreeze() {
		return freeze;
	}

	public void setFreeze(Byte freeze) {
		this.freeze = freeze;
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
}