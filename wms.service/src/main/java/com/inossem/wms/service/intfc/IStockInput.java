package com.inossem.wms.service.intfc;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizPurchaseOrderHead;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.vo.SupplierVo;

public interface IStockInput {

	/**
	 * 从SAP 查询采购订单列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<BizPurchaseOrderHead> getContractListFromSAP(Map<String, Object> map) throws Exception;
	
	/**
	 * 从SAP 查询采购订单详情
	 * @param purchaseOrderCode 采购订单号
	 * @return
	 * @throws Exception
	 */
	BizPurchaseOrderHead getContractInfoFromSAP(String purchaseOrderCode) throws Exception;


	/**
	 * 免检入库过账
	 * @param stockInputHead
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> mjrkOrZcrkPosting(BizStockInputHead stockInputHead) throws Exception;
	
	
	
	/**
	 * 其他入库过账
	 * @param stockInputHead
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> qtrkPosting(BizStockInputHead stockInputHead) throws Exception;
	
	/**
	 * 调拨入库过账
	 * @param stockInputHead
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> dbrkPosting(BizStockInputHead stockInputHead) throws Exception;

	/**
	 * 获取供应商
	 * @param supplier
	 * @return
	 * @throws Exception
	 */
	List<SupplierVo> getSupplierList(SupplierVo supplier) throws Exception;

	Map<String, Object> writeOff(BizStockInputHead stockInputHead,List<BizMaterialDocItem> omDocItemList) throws Exception;
	
}
