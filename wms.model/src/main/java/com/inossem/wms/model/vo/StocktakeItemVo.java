package com.inossem.wms.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点行项目
 * @author wang.ligang
 *
 */
public class StocktakeItemVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer itemId;

    private Integer stocktakeId; // 盘点凭证id

    private Integer whId; // 仓库号

    private Integer areaId; // 存储区

    private Integer positionId;

    private String prefix; // 存储区 空格 仓位的前缀【按照-拆分，去掉最后一位】

    private String suffix; // 仓位的后缀【按照-拆分，取最后一位】

    private Integer matId; 

    private Long batch; 

    private BigDecimal stockQty; // 库存数量

    private BigDecimal stocktakeQty; // 盘点数量

    private Integer unitId;

    private String specStock; // 特殊库存标识	"E 现有订单,K 寄售（供应商）,O 供应商分包库存,Q 项目库存"

    private String specStockCode; // 特殊库存编码

    private String specStockName; // 特殊库存描述
 
    private String takeDeliveryDate; // 收货日期

    private String actualStocktakeTime; // 实际盘点日期

    private String stocktakeUser; // 盘点人

    private String remark;

    private Byte status; // 状态：2-未完成，3-已完成，4-已提交，6-重盘

    private BigDecimal lastQty; // 上次盘点数量

//    private Byte isDelete;

    private String createTime;

    private String modifyTime;
    
    
    //---------------以下为自定义字段---------------------------------------------

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStocktakeId() {
		return stocktakeId;
	}

	public void setStocktakeId(Integer stocktakeId) {
		this.stocktakeId = stocktakeId;
	}

	public Integer getWhId() {
		return whId;
	}

	public void setWhId(Integer whId) {
		this.whId = whId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Integer getMatId() {
		return matId;
	}

	public void setMatId(Integer matId) {
		this.matId = matId;
	}

	public Long getBatch() {
		return batch;
	}

	public void setBatch(Long batch) {
		this.batch = batch;
	}

	public BigDecimal getStockQty() {
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
	}

	public BigDecimal getStocktakeQty() {
		return stocktakeQty;
	}

	public void setStocktakeQty(BigDecimal stocktakeQty) {
		this.stocktakeQty = stocktakeQty;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getSpecStock() {
		return specStock;
	}

	public void setSpecStock(String specStock) {
		this.specStock = specStock;
	}

	public String getSpecStockCode() {
		return specStockCode;
	}

	public void setSpecStockCode(String specStockCode) {
		this.specStockCode = specStockCode;
	}

	public String getSpecStockName() {
		return specStockName;
	}

	public void setSpecStockName(String specStockName) {
		this.specStockName = specStockName;
	}

	public String getTakeDeliveryDate() {
		return takeDeliveryDate;
	}

	public void setTakeDeliveryDate(String takeDeliveryDate) {
		this.takeDeliveryDate = takeDeliveryDate;
	}

	public String getActualStocktakeTime() {
		return actualStocktakeTime;
	}

	public void setActualStocktakeTime(String actualStocktakeTime) {
		this.actualStocktakeTime = actualStocktakeTime;
	}

	public String getStocktakeUser() {
		return stocktakeUser;
	}

	public void setStocktakeUser(String stocktakeUser) {
		this.stocktakeUser = stocktakeUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getLastQty() {
		return lastQty;
	}

	public void setLastQty(BigDecimal lastQty) {
		this.lastQty = lastQty;
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
    
    
}
