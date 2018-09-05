package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

public class StockSnHistory {
	private Integer id;

	private Integer snId;

	private Integer matId;

	private Integer stockId;

	private BigDecimal qty;

	private Date createTime;

	private Date modifyTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSnId() {
		return snId;
	}

	public void setSnId(Integer snId) {
		this.snId = snId;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

	public StockSnHistory(StockSn stockSn) {
		this.id = stockSn.getId();

		this.snId = stockSn.getSnId();

		this.matId = stockSn.getMatId();

		this.stockId = stockSn.getStockId();

		this.qty = stockSn.getQty();

		this.createTime = stockSn.getCreateTime();

		// this.modifyTime = stockSn.getModifyTime();
	}
}