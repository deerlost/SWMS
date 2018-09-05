package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizMaterialDocHeadMapper;
import com.inossem.wms.dao.biz.BizMaterialDocItemMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputHeadMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.syn.SynMaterialDocHeadMapper;
import com.inossem.wms.dao.syn.SynMaterialDocItemMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizStockInputItem;
import com.inossem.wms.model.biz.BizStockOutputHead;
import com.inossem.wms.model.biz.BizStockOutputItem;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.enums.EnumBoolean;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumStockOutputStatus;
import com.inossem.wms.model.syn.SynMaterialDocHead;
import com.inossem.wms.model.syn.SynMaterialDocItem;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IStockCheckService;
import com.inossem.wms.service.intfc.IStockCheck;
import com.inossem.wms.util.UtilObject;

/**
 * 库存对账service
 * @author 高海涛
 * @date 2018年2月26日
 */
@Service
public class StockCheckServiceImpl implements IStockCheckService {

	@Resource
	private DicStockLocationMapper locationDao;
	@Resource
	private IStockCheck stockCheckImpl;
	@Resource
	private SynMaterialDocHeadMapper synMDocHeadDao;
	@Resource
	private SynMaterialDocItemMapper synMDocItemDao;
	@Resource
	private SequenceDAO sequenceDao;
	@Resource
	private BizStockOutputHeadMapper outPutHeadDao;
	@Resource
	private BizStockOutputItemMapper outPutItemDao;
	@Resource
	private IDictionaryService dictionaryService;
	@Resource
	private BizStockInputHeadMapper stockInputHeadDao;
	@Resource
	private BizStockInputItemMapper stockInputItemDao;
	@Resource
	private BizMaterialDocHeadMapper bizMaterialHeadDocDao;
	@Resource
	private BizMaterialDocItemMapper bizMaterialItemDocDao;
	
	
	/**
	 * 同步数据 并且设置好是否为WMS系统中没有的凭证 和 是否已自动配对
	 */
	@Override
	@Transactional
	public boolean syncSapMatDoc(String ftyId,String locationId,String sDate,String eDate,String userId) throws Exception {
		boolean ret = false;
		
		DicStockLocation locationObject = locationDao.selectByPrimaryKey(Integer.parseInt(locationId)); 
		
		if(locationObject!=null){
			if(locationObject.getCreateTime()!=null){
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(sDate);
				if(locationObject.getCreateTime().after(date)){
					sDate = dateFormat.format(locationObject.getCreateTime());
				}
			}
		}
		
		ret = stockCheckImpl.syncSapMatDoc(ftyId, locationId, sDate, eDate, userId);
		
		return ret;
	}
	
	
	/**
	 * 查询出所有需要操作的凭证列表
	 * @date 2017年10月16日
	 * @author 高海涛
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getMatItemList(String ftyId,String locationId,String sDate,String eDate,String status1,String status2,String matId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ftyId",ftyId);
		map.put("locationId",locationId);
		map.put("sDate",sDate);
		map.put("eDate",eDate);
		List<String> isNew = new ArrayList<String>();
		if(status1.equals("1")){
			isNew.add("1");
		}
		if(status2.equals("1")){
			isNew.add("2");
		}
		map.put("isNewList", isNew);
		map.put("matId", matId);
		map.put("isNeutralize", "0");
		return synMDocItemDao.selectMatDocInfoByParams(map);
	}
	
	/**
	 * 查询已匹配好不需要任何操作的凭证列表
	 * @date 2017年10月16日
	 * @author 高海涛
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getMatItemNeutralizeList(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isNew","1");
		map.put("isNeutralize","1");
		return synMDocItemDao.selectMatDocInfoByParams(map);
	}
	
	
	/**
	 * 获取出库新主键
	 */
	private long getNewOrderID(String type) {
		return sequenceDao.selectNextVal(type);
	}
	
	
	/**
	 * 保存出库主数据
	 * @date 2017年10月17日
	 * @author 高海涛
	 * @param map
	 * @return
	 */
	private int saveToOutputHead(SynMaterialDocItem matDocItem,CurrentUser cUser){
		BizStockOutputHead head = new BizStockOutputHead();
		
		head.setStockOutputCode(String.valueOf(this.getNewOrderID("stock_output")));// 新出库单号
		head.setReceiptType(EnumReceiptType.STOCK_OUTPUT_OTHER.getValue()); // 出库单类型 
		head.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_UNFINISH.getValue());
		head.setIsDelete(EnumBoolean.FALSE.getValue());
		head.setCheckAccount(EnumBoolean.TRUE.getValue());
		head.setCreateUser(cUser.getUserId());
		head.setCreateTime(new Date());
		head.setCorpId(cUser.getCorpId()); //公司ID
		
		head.setReferReceiptCode(matDocItem.getSaleOrderCode()); //前置单据ID

		head.setReceiveCode(matDocItem.getCustomerCode()); //接收人编码
		head.setReceiveName(matDocItem.getCustomerName()); //接收人名称
		
		head.setModifyUser(cUser.getUserId()); // 最后修改人
		head.setModifyTime(new Date()); // 最后修改时间
		head.setRequestSource(EnumBoolean.FALSE.getValue());
		
		outPutHeadDao.insertSelective(head);
		
		return head.getStockOutputId();

	}
	/**
	 * 保存出库明细
	 * @date 2017年10月17日
	 * @author 高海涛
	 * @param map
	 * @param zdjbh
	 * @param mblnr
	 */
	private void saveToOutputItem(SynMaterialDocHead matDocHead,SynMaterialDocItem matDocItem,int stockOutputId,CurrentUser cUser){
		BizStockOutputItem item = new BizStockOutputItem();
		
		item.setStockOutputId(stockOutputId);
		item.setStockOutputRid(matDocItem.getMatDocRid());
		item.setFtyId(matDocItem.getFtyId());// 工厂
		item.setLocationId(matDocItem.getLocationId()); // 库存地点
		item.setMatId(matDocItem.getMatId());   // 物料编码
		item.setUnitId(matDocItem.getUnitId()); // 计量单位
		
		Map<Integer, DicUnit> unitMap = dictionaryService.getUnitIdMap();
		DicUnit dicUnit = unitMap.get(matDocItem.getUnitId());
		item.setDecimalPlace(dicUnit.getDecimalPlace());  // 浮点型
		item.setSendQty(new BigDecimal("0"));
		item.setStockQty(matDocItem.getQty());  //订单数量
		item.setOutputQty(new BigDecimal("0"));
		item.setMoveTypeId(matDocItem.getMoveTypeId());// 移动类型
		item.setMatDocId(matDocItem.getMatDocId());
		item.setMatDocRid(matDocItem.getMatDocRid());
		item.setMatDocYear(matDocHead.getMatDocYear());
		
		item.setReferReceiptCode(matDocItem.getSaleOrderCode());
		item.setReserveId(matDocItem.getReserveId());
		item.setReserveRid(matDocItem.getReserveRid());

		item.setIsDelete(EnumBoolean.FALSE.getValue());
		item.setWriteOff(EnumBoolean.FALSE.getValue());
		
		item.setCreateUser(cUser.getUserId());
		item.setCreateTime(new Date());
		item.setModifyUser(cUser.getUserId());
		item.setModifyTime(new Date());
		
		outPutItemDao.insertSelective(item);

	}

	/**
	 * 保存入库主数据
	 * @date 2017年10月17日
	 * @author 高海涛
	 * @param map
	 * @return
	 */
	private int saveToInputHead(SynMaterialDocHead matDocHead,SynMaterialDocItem matDocItem,CurrentUser cUser){
		
		BizStockInputHead stockInputHead = new BizStockInputHead();
		
		stockInputHead.setStockInputCode(String.valueOf(sequenceDao.selectNextVal("stock_input")));
		stockInputHead.setReceiptType(EnumReceiptType.STOCK_INPUT_OTHER.getValue());
		stockInputHead.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_UNFINISH.getValue());
		stockInputHead.setDocTime(matDocHead.getMatDocTime());
		stockInputHead.setPostingDate(matDocHead.getPostingTime());
		stockInputHead.setCreateUser(cUser.getUserId());
		stockInputHead.setSupplierCode(matDocItem.getSupplierCode());
		stockInputHead.setSupplierName(matDocItem.getSupplierName());
		stockInputHead.setFtyId(matDocItem.getFtyId());
		stockInputHead.setLocationId(matDocItem.getLocationId());
		stockInputHead.setMoveTypeId(matDocItem.getMoveTypeId());
		stockInputHead.setSpecStock(matDocItem.getSpecStockCode());
		
		stockInputHead.setModifyTime(new Date()); // 最后修改时间
		stockInputHead.setCreateTime(new Date());
		stockInputHead.setCheckAccount(EnumBoolean.TRUE.getValue());
		stockInputHead.setIsDelete(EnumBoolean.FALSE.getValue());
		stockInputHeadDao.insertSelective(stockInputHead);
		
		return stockInputHead.getStockInputId();
	}
	/**
	 * 保存入库明细
	 * @date 2017年10月17日
	 * @author 高海涛
	 * @param map
	 * @param zdjbh
	 * @param mblnr
	 */
	private void saveToInputItem(SynMaterialDocItem matDocItem,int stockInputId,CurrentUser cUser){
		BizStockInputItem item = new BizStockInputItem();
		item.setStockInputId(stockInputId);
		item.setStockInputRid(matDocItem.getMatDocRid());
		item.setMoveTypeId(matDocItem.getMoveTypeId());
		item.setMatId(matDocItem.getMatId());
		item.setFtyId(matDocItem.getFtyId());
		item.setLocationId(matDocItem.getLocationId());
		item.setSpecStock(matDocItem.getSpecStock());
		item.setSpecStockCode(matDocItem.getSpecStockCode());
		item.setSpecStockName(matDocItem.getSpecStockName());
		item.setSupplierCode(matDocItem.getSupplierCode());
		item.setSupplierName(matDocItem.getSupplierName());
		item.setCustomerCode(matDocItem.getCustomerCode());
		item.setCustomerName(matDocItem.getCustomerName());
		item.setSaleOrderCode(matDocItem.getSaleOrderCode());
		item.setSaleOrderPrjectCode(matDocItem.getSaleOrderPrjectCode());
		item.setSaleOrderDeliveredPlan(matDocItem.getSaleOrderDeliveredPlan());
		
		item.setReferReceiptCode(matDocItem.getSaleOrderCode());
		item.setQty(matDocItem.getQty());
		item.setUnitId(matDocItem.getUnitId());
		item.setMatDocId(matDocItem.getMatDocId());
		item.setMatDocRid(matDocItem.getMatDocRid());
		item.setReserveId(matDocItem.getReserveId());
		item.setReserveRid(matDocItem.getReserveRid());
		item.setStatus(EnumStockOutputStatus.STOCK_OUTPUT_UNFINISH.getValue());
		item.setIsDelete(EnumBoolean.FALSE.getValue());
		item.setCreateTime(new Date());
		item.setModifyTime(new Date());
		
		stockInputItemDao.insertSelective(item);
	}
	
	/**
	 * 处理凭证保存到表单
	 * @date 2017年10月17日
	 * @author 高海涛
	 * @param mapList
	 * @return
	 */
	@Override
	@Transactional
	public int saveOrder(List<Map<String,Object>> matDocList,CurrentUser cUser) throws Exception{
		Map<String,Integer> matDocHeadMap = new HashMap<String,Integer>();
		int count = 0;
		for(Map<String,Object> matDoc : matDocList){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("matDocId", matDoc.get("mat_doc_id"));
			param.put("matDocRid", matDoc.get("mat_doc_rid"));
			SynMaterialDocHead matDocHead = synMDocHeadDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(matDoc.get("mat_doc_id")));
			SynMaterialDocItem matDocItem = synMDocItemDao.selectByPrimaryKey(param);
			int matDocId = matDocItem.getMatDocId();
			
			if("H".equals(matDocItem.getDebitCredit())){
				
				if (!matDocHeadMap.containsKey(""+matDocId+matDocItem.getDebitCredit())) {
					int stockOutputId = this.saveToOutputHead(matDocItem,cUser);
					matDocHeadMap.put(""+matDocId+matDocItem.getDebitCredit(), stockOutputId);
				}
				
				this.saveToOutputItem(matDocHead,matDocItem,matDocHeadMap.get(matDocId+matDocItem.getDebitCredit()),cUser);
				
				SynMaterialDocItem record = new SynMaterialDocItem();	
				record.setMatDocId(matDocItem.getMatDocId());
				record.setMatDocRid(matDocItem.getMatDocRid());
				record.setIsNew((byte)2);
				
				count = count + synMDocItemDao.updateByPrimaryKeySelective(record);
				
			}else if("S".equals(matDocItem.getDebitCredit())){
				
				if (!matDocHeadMap.containsKey(""+matDocId+matDocItem.getDebitCredit())) {
					int stockInputId = this.saveToInputHead(matDocHead,matDocItem,cUser);
					matDocHeadMap.put(""+matDocId+matDocItem.getDebitCredit(), stockInputId);
				}
				
				this.saveToInputItem(matDocItem,matDocHeadMap.get(matDocId+matDocItem.getDebitCredit()),cUser);
				
				SynMaterialDocItem record = new SynMaterialDocItem();	
				record.setMatDocId(matDocItem.getMatDocId());
				record.setMatDocRid(matDocItem.getMatDocRid());
				record.setIsNew((byte)2);
				count = count + synMDocItemDao.updateByPrimaryKeySelective(record);
			}
			
		}
		return count;
	}
	
	/**
	 * 批量保存以对冲凭证
	 * @date 2017年10月18日
	 * @author 高海涛
	 * @param list
	 */
	@Override
	@Transactional
	public int saveToWmsMatDoc(List<Map<String,Object>> matDocList) throws Exception{
		Map<Integer,Integer> matDocHeadMap = new HashMap<Integer,Integer>();
		int count = 0;
		
		for(Map<String,Object> matDoc : matDocList){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("matDocId", matDoc.get("mat_doc_id"));
			param.put("matDocRid", matDoc.get("mat_doc_rid"));
			SynMaterialDocHead matDocHead = synMDocHeadDao.selectByPrimaryKey(UtilObject.getIntegerOrNull(matDoc.get("mat_doc_id")));
			SynMaterialDocItem matDocItem = synMDocItemDao.selectByPrimaryKey(param);
			int matDocId = matDocItem.getMatDocId();
			
				
			if (!matDocHeadMap.containsKey(matDocId)) {
				int id = this.saveMatDocHead(matDocHead);
				matDocHeadMap.put(matDocId, id);
			}
			
			this.saveMatDocItem(matDocHead,matDocItem,matDocHeadMap.get(matDocId));
			
			SynMaterialDocItem record = new SynMaterialDocItem();	
			record.setMatDocId(matDocItem.getMatDocId());
			record.setMatDocRid(matDocItem.getMatDocRid());
			record.setIsNew((byte)2);
			
			count = count + synMDocItemDao.updateByPrimaryKeySelective(record);
				
		}
		return count;
	}
	
	/**
	 * 保存物料凭证抬头
	 * 
	 * @date 2017年9月30日
	 * @author 高海涛
	 * @param sapReturnData
	 * @return
	 */
	private int saveMatDocHead(SynMaterialDocHead matDocHead){
		BizMaterialDocHead mDocHead = new BizMaterialDocHead();

		mDocHead.setMatDocCode(matDocHead.getMatDocCode());// 物料凭证号
		mDocHead.setMatDocType(EnumReceiptType.TYPE_SAP.getValue());
		mDocHead.setDocTime(matDocHead.getMatDocTime());	// 凭证日期
		mDocHead.setPostingDate(matDocHead.getPostingTime());

		mDocHead.setCreateUser("SAP");

		bizMaterialHeadDocDao.insertSelective(mDocHead);
		
		return mDocHead.getMatDocId();
	}
	/**
	 * 保存物料明细
	 * 
	 * @date 2017年9月30日
	 * @author 高海涛
	 * @param e_mseg
	 * @param postion
	 * @return
	 */
	private void saveMatDocItem(SynMaterialDocHead matDocHead,SynMaterialDocItem matDocItem,int matDocId) {
		// 保存凭证详细表数据
		BizMaterialDocItem item = new BizMaterialDocItem();
		
		item.setMatDocId(matDocId);
		item.setMatDocRid(matDocItem.getMatDocRid());	// 物料凭证行项目
		item.setMatDocYear(matDocHead.getMatDocYear());
		item.setBatch(new Long(0));
		item.setMoveTypeId(matDocItem.getMoveTypeId()); // 移动类型
		item.setMoveTypeCode(dictionaryService.getMoveTypeCodeByMoveTypeId(matDocItem.getMoveTypeId()));
		item.setMatId(matDocItem.getMatId());// 物料编码
		item.setFtyId(matDocItem.getFtyId()); // 工厂
		item.setLocationId(matDocItem.getLocationId()); // 库存地点

		item.setSpecStock(matDocItem.getSpecStock());
		item.setSpecStockCode(matDocItem.getSpecStockCode());
		item.setSpecStockName(matDocItem.getSpecStockName());
		
		item.setSupplierCode(matDocItem.getSupplierCode()); // 供应商编码
		item.setSupplierName(matDocItem.getSupplierName()); // 供应商名称
		
		item.setCustomerCode(matDocItem.getCustomerCode()); // 客户编码
		item.setCustomerName(matDocItem.getCustomerName()); // 客户名名称

		item.setSaleOrderCode(matDocItem.getSaleOrderCode()); // 销售订单号
		item.setSaleOrderProjectCode(matDocItem.getSaleOrderPrjectCode()); // 销售订单项目编号
		item.setSaleOrderDeliveredPlan(matDocItem.getSaleOrderDeliveredPlan()); // 销售订单交货计

		item.setQty(matDocItem.getQty());
		item.setUnitId(matDocItem.getUnitId());// 基本计量单位

		item.setCreateTime(new Date());
		item.setDebitCredit(matDocItem.getDebitCredit());// 借贷标识 "H：贷方 数量减 S：借方 数量加 "

		String insmk = matDocItem.getStockStatus();// 库存状态

		// 设置库存状态
		if ("".equals(insmk)) {
			// 非限制库存
			item.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
		} else if ("X".equals(insmk)) {
			// 质量检验库存
			item.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_QUALITY_INSPECTION.getValue());
		} else if ("S".equals(insmk)) {
			// 冻结库存
			item.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_FREEZE.getValue());
		} else if ("T".equals(insmk)) {
			// 在途库存
			item.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_ON_THE_WAY.getValue());
		}

		bizMaterialItemDocDao.insertSelective(item);
	}
	
	
	/**
	 * 获取库存
	 * @date 2017年10月19日
	 * @author 高海涛
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getStorageList(String ftyId,String locationId,String userId) throws Exception{
		return stockCheckImpl.getStorageList(ftyId, locationId, userId);
	}
	
}
