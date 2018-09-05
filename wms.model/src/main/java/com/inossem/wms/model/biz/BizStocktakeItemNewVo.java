package com.inossem.wms.model.biz;

import java.util.Date;
import java.util.List;

import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.StocktakeBatch;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.vo.StocktakeInputBatchVo;

public class BizStocktakeItemNewVo {
	private Integer id;	    //自增ID
	private Integer itemId;	    
	private String purchaseOrderCode; // 采购订单编号

    private String contractCode; // 合同编号

    private String supplierCode; // 供应商代码

    private String supplierName; // 供应商名称

    private String demandDept;	// 需求部门
    private Integer corpId; // 采购订单公司代码

    private String inputTime; // 入库时间

    private Date productionTime; // 生产日期
    private Date validityTime; // 有效期

    private String remark;
    private byte isDelete;
    
	private String createTime;
	private String modifyTime;
    
//    StocktakeBatch[] batchSpecList;
    /**
	 * 批次特性
	 */
	private StocktakeInputBatchVo[] batchSpecList;

	/**
	 * 批次信息
	 */
	private StocktakeInputBatchVo[] batchMaterialSpecList;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}

	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDemandDept() {
		return demandDept;
	}

	public void setDemandDept(String demandDept) {
		this.demandDept = demandDept;
	}

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	public Date getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(Date productionTime) {
		this.productionTime = productionTime;
	}

	public Date getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Date validityTime) {
		this.validityTime = validityTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public StocktakeInputBatchVo[] getBatchSpecList() {
		return batchSpecList;
	}

	public void setBatchSpecList(StocktakeInputBatchVo[] batchSpecList) {
		this.batchSpecList = batchSpecList;
	}

	public byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public StocktakeInputBatchVo[] getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(StocktakeInputBatchVo[] batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}
    

}
