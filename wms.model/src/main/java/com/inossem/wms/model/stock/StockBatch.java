package com.inossem.wms.model.stock;

import java.math.BigDecimal;
import java.util.Date;

import com.inossem.wms.model.key.StockBatchKey;

public class StockBatch extends StockBatchKey {
	private Integer id;

	private String createUser;

	private BigDecimal qty;

	private String specStock;

	private String specStockCode;

	private String specStockName;

	private Date createTime;

	private Date modifyTime;
	
	private String erpBatch;

	public String getErpBatch() {
		return erpBatch;
	}

	public void setErpBatch(String erpBatch) {
		this.erpBatch = erpBatch;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock == null ? null : specStock.trim();
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode == null ? null : specStockCode.trim();
	}

	public String getSpecStockName() {
		return specStockName;
	}

	public void setSpecStockName(String specStockName) {
		this.specStockName = specStockName == null ? null : specStockName.trim();
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