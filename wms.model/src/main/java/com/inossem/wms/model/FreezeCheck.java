package com.inossem.wms.model;

import java.util.List;

import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.vo.CheckLockFacLocVo;

public class FreezeCheck {

	public boolean lockedInput; // 入库用 false->没有被锁定；true被锁定
	public boolean lockedOutput; // 出库用 false->没有被锁定；true被锁定

	// 整理后的消息，直接返回给前端页面用
	// 仓位被冻结:库存地点xxxx下XXXX仓位已被冻结，请检查。
	// 库存地点被冻结：库存地点xxxx已被冻结，请检查。
	public String resultMsgInput;

	// 整理后的消息，直接返回给前端页面用
	// 仓位被冻结:库存地点xxxx下XXXX仓位已被冻结，请检查。
	// 库存地点被冻结：库存地点xxxx已被冻结，请检查。
	public String resultMsgOutput;

	public List<CheckLockFacLocVo> lockedInputDicStockLocation; // 入库被锁定的库存地点
	public List<DicWarehousePosition> lockedInputDicWarehousePosition; // 入库被锁定的仓位
	public List<CheckLockFacLocVo> lockedOutputDicStockLocation; // 出库被锁定的库存地点
	public List<DicWarehousePosition> lockedOutputDicWarehousePosition; // 出库被锁定的仓位

	public FreezeCheck() {
		resultMsgInput = "";
		resultMsgOutput = "";
		lockedInput = false;
		lockedOutput = false;
	}

	public boolean isLockedInput() {
		return lockedInput;
	}

	public void setLockedInput(boolean lockedInput) {
		this.lockedInput = lockedInput;
	}

	public boolean isLockedOutput() {
		return lockedOutput;
	}

	public void setLockedOutput(boolean lockedOutput) {
		this.lockedOutput = lockedOutput;
	}

	public String getResultMsgInput() {
		return resultMsgInput;
	}

	public void setResultMsgInput(String resultMsgInput) {
		this.resultMsgInput = resultMsgInput;
	}

	public String getResultMsgOutput() {
		return resultMsgOutput;
	}

	public void setResultMsgOutput(String resultMsgOutput) {
		this.resultMsgOutput = resultMsgOutput;
	}

	public List<CheckLockFacLocVo> getLockedInputDicStockLocation() {
		return lockedInputDicStockLocation;
	}

	public void setLockedInputDicStockLocation(List<CheckLockFacLocVo> lockedInputDicStockLocation) {
		this.lockedInputDicStockLocation = lockedInputDicStockLocation;
	}

	public List<DicWarehousePosition> getLockedInputDicWarehousePosition() {
		return lockedInputDicWarehousePosition;
	}

	public void setLockedInputDicWarehousePosition(List<DicWarehousePosition> lockedInputDicWarehousePosition) {
		this.lockedInputDicWarehousePosition = lockedInputDicWarehousePosition;
	}

	public List<CheckLockFacLocVo> getLockedOutputDicStockLocation() {
		return lockedOutputDicStockLocation;
	}

	public void setLockedOutputDicStockLocation(List<CheckLockFacLocVo> lockedOutputDicStockLocation) {
		this.lockedOutputDicStockLocation = lockedOutputDicStockLocation;
	}

	public List<DicWarehousePosition> getLockedOutputDicWarehousePosition() {
		return lockedOutputDicWarehousePosition;
	}

	public void setLockedOutputDicWarehousePosition(List<DicWarehousePosition> lockedOutputDicWarehousePosition) {
		this.lockedOutputDicWarehousePosition = lockedOutputDicWarehousePosition;
	}
	
	
}