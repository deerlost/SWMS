package com.inossem.wms.model.biz;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.batch.BatchMaterialSpec;

/**
 * 盘点保存用
 * @author wlg
 *
 */
public class BizStocktakeItemVo {
	private Integer itemId;	    //自增ID
	private Integer stocktakeId;	    //盘点凭证号
	private Integer whId;	//仓库号
	private Integer areaId;	//存储区
	private String areaCode;	//存储区
	private Integer positionId;	//仓位
	private String positionCode;	//仓位
	private Integer matId;	//物料编码
//	private String matName;	//物料描述
	
	private Long batch;	    //批号
	private BigDecimal stockQty;	//库存数量
	private BigDecimal stocktakeQty;	//盘点数量
	private Integer unitId;	//计量单位
	private String specStock;	//特殊库存标识	"E 现有订单,K 寄售（供应商）,O 供应商分包库存,Q 项目库存"[K|Q]
	private String specStockCode;	//特殊库存编码
	private String specStockName;	//特殊库存描述
	private String takeDeliveryDate; // 收货日期=入库时间
	private Date actualStocktakeTime;	    //实际盘点日期
	private String stocktakeUser;	//盘点人
	private String remark;	//备注
	private String status;	//状态：1-草稿，2-未完成，3-已完成，4-已提交，6-重盘
	private BigDecimal lastQty;	//上次盘点数量
	private Byte requestSource ; // 盘断pda或者Web端来源
	
	private String prefix; // 存储区 空格 仓位的前缀【按照-拆分，去掉最后一位】
	private String suffix;      // 仓位的后缀【按照-拆分，取最后一位】
	private byte isDelete;
	private String createTime;
	private String modifyTime;
	
	
	//---------------以下为自定义字段---------------------------------------------
	
	//-----------------批量添加新增的物料时，构造批次信息对象------------------------------------------------------------
	private BatchMaterial batchMaterial;                // 新增的批次对象
	private List<BatchMaterialSpec> batchMaterialSpecList; // 新增的批次特性对象
	
//	
//	private String sobkzmc; // 特殊库存标识名称
//	private String zsatemc; // 状态名称
//	private String idatuxs;	// 实际盘点日期显示
//	
//	private String pdrs; // 盘点人姓名 count(cname) group by lgtyp
//	private Integer cws; // 仓位数，count(仓位lgpla) group by lgtyp
//	private Integer wlpls; // 物料品类数，count(distinct 物料编码  matnr)  group by lgtyp
//	private BigDecimal ypds;//已盘点数 count(menge) group by lgtyp
	
	private static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private BizStocktakeItemNewVo itemNew; // 库存盘点-新加盘点物料明细表 1对1

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

	public Date getActualStocktakeTime() {
		return actualStocktakeTime;
	}

	public void setActualStocktakeTime(Date actualStocktakeTime) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLastQty() {
		return lastQty;
	}

	public void setLastQty(BigDecimal lastQty) {
		this.lastQty = lastQty;
	}

	public Byte getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(Byte requestSource) {
		this.requestSource = requestSource;
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

	public BizStocktakeItemNewVo getItemNew() {
		return itemNew;
	}

	public void setItemNew(BizStocktakeItemNewVo itemNew) {
		this.itemNew = itemNew;
	}

	public BatchMaterial getBatchMaterial() {
		return batchMaterial;
	}

	public void setBatchMaterial(BatchMaterial batchMaterial) {
		this.batchMaterial = batchMaterial;
	}

	public List<BatchMaterialSpec> getBatchMaterialSpecList() {
		return batchMaterialSpecList;
	}

	public void setBatchMaterialSpecList(List<BatchMaterialSpec> batchMaterialSpecList) {
		this.batchMaterialSpecList = batchMaterialSpecList;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
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

	
	
	
//	private BigDecimal winNum;	//盘赢数量
//	private BigDecimal lossNum;	//盘亏数量
//	
//	//-----------------由于数据库设计的字段名称不统一，为了返回数据名称一样，重命名字段------------------------------------------
//	private String mseht;   //特殊库存描述
//	private String edatu;	//入库时间
//	private String ztskc;	//特殊库存代码
//	private String ztsms;	//特殊库存描述
//	
//	//-----------------批量添加新增的物料时，构造批次信息对象------------------------------------------------------------
//	private MCHA mcha;                // 新增的批次对象
//	private ArrayList<MCH1> mch1List; // 新增的批次特性对象
//	
//	//-----------------盘点结果确认需要的页面变量--------------------------------------------------------------------
//	private BigDecimal price;  // 库存单价：调用SAP接口获取移动平均价
//	
//	private Boolean hasPdr = false; // 盘点人是否存在
//
//	private String werks; // 工厂,用于批次特性接收参数
//	
//	private Byte andec;
	
	
	
}
