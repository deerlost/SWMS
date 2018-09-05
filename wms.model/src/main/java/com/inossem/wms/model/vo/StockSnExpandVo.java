package com.inossem.wms.model.vo;

import com.inossem.wms.model.stock.StockSn;

public class StockSnExpandVo extends StockSn {

	   private Integer targetAreaId;

	    private Integer targetPositionId;
	    
	    private Integer targetPalletId;
		private Integer targetPackageId;
		public Integer getTargetAreaId() {
			return targetAreaId;
		}
		public void setTargetAreaId(Integer targetAreaId) {
			this.targetAreaId = targetAreaId;
		}
		public Integer getTargetPositionId() {
			return targetPositionId;
		}
		public void setTargetPositionId(Integer targetPositionId) {
			this.targetPositionId = targetPositionId;
		}
		public Integer getTargetPalletId() {
			return targetPalletId;
		}
		public void setTargetPalletId(Integer targetPalletId) {
			this.targetPalletId = targetPalletId;
		}
		public Integer getTargetPackageId() {
			return targetPackageId;
		}
		public void setTargetPackageId(Integer targetPackageId) {
			this.targetPackageId = targetPackageId;
		}
}
