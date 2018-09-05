package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.httpclient.util.DateUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.inossem.wms.config.BatchMaterialConfig;
import com.inossem.wms.constant.Const;
import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.batch.BatchMaterialMapper;
import com.inossem.wms.dao.batch.BatchMaterialSpecMapper;
import com.inossem.wms.dao.biz.BizFailMesMapper;
import com.inossem.wms.dao.biz.BizMaterialDocHeadMapper;
import com.inossem.wms.dao.biz.BizMaterialDocItemMapper;
import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqHeadMapper;
import com.inossem.wms.dao.biz.BizStockTaskReqItemMapper;
import com.inossem.wms.dao.biz.BufFactoryMatPriceMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicAccountPeriodMapper;
import com.inossem.wms.dao.dic.DicBatchSpecMapper;
import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.dao.dic.DicCityMapper;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicCorpMapper;
import com.inossem.wms.dao.dic.DicDeliveryDriverMapper;
import com.inossem.wms.dao.dic.DicDeliveryVehicleMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicMoveReasonMapper;
import com.inossem.wms.dao.dic.DicMoveTypeMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicPrinterMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.dic.DicStockLocationMapper;
import com.inossem.wms.dao.dic.DicSupplierMapper;
import com.inossem.wms.dao.dic.DicWarehouseAreaMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.dao.dic.DicWarehousePalletMapper;
import com.inossem.wms.dao.dic.DicWarehousePositionMapper;
import com.inossem.wms.dao.rel.RelMatErpbatchMapper;
import com.inossem.wms.dao.stock.StockBatchBeginMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.dao.stock.StockOccupancyMapper;
import com.inossem.wms.dao.stock.StockPositionHistoryMapper;
import com.inossem.wms.dao.stock.StockPositionMapper;
import com.inossem.wms.dao.stock.StockSnHistoryMapper;
import com.inossem.wms.dao.stock.StockSnMapper;
import com.inossem.wms.dao.wf.FlowableMapper;
import com.inossem.wms.dao.wf.WfReceiptHistoryMapper;
import com.inossem.wms.dao.wf.WfReceiptMapper;
import com.inossem.wms.dao.wf.WfReceiptUserMapper;
import com.inossem.wms.exception.SAPDebitCreditException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.FreezeCheck;
import com.inossem.wms.model.auth.User;
import com.inossem.wms.model.batch.BatchMaterial;
import com.inossem.wms.model.biz.BizMaterialDocHead;
import com.inossem.wms.model.biz.BizMaterialDocItem;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizStockTaskItemCw;
import com.inossem.wms.model.biz.BizStockTaskPositionCw;
import com.inossem.wms.model.biz.BizStockTaskReqHead;
import com.inossem.wms.model.biz.BizStockTaskReqItem;
import com.inossem.wms.model.biz.BufFactoryMatPrice;
import com.inossem.wms.model.dic.DicBatchSpec;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.dic.DicCity;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.dic.DicMoveReason;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicPrinter;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.dic.DicStockLocation;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.dic.DicWarehouse;
import com.inossem.wms.model.dic.DicWarehouseArea;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.dic.DicWarehousePosition;
import com.inossem.wms.model.enums.EnumBoard;
import com.inossem.wms.model.enums.EnumDebitCredit;
import com.inossem.wms.model.enums.EnumFixedBatchSpec;
import com.inossem.wms.model.enums.EnumFreeze;
import com.inossem.wms.model.enums.EnumFtyType;
import com.inossem.wms.model.enums.EnumIsUrgent;
import com.inossem.wms.model.enums.EnumMatStoreType;
import com.inossem.wms.model.enums.EnumOperatorRoles;
import com.inossem.wms.model.enums.EnumOperatorTypes;
import com.inossem.wms.model.enums.EnumPrinterType;
import com.inossem.wms.model.enums.EnumReceiptApproveStatus;
import com.inossem.wms.model.enums.EnumReceiptStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.enums.EnumShippingType;
import com.inossem.wms.model.enums.EnumStockStatus;
import com.inossem.wms.model.enums.EnumUrgentSynStatus;
import com.inossem.wms.model.key.BatchMaterialSpecKey;
import com.inossem.wms.model.key.StockBatchKey;
import com.inossem.wms.model.key.StockPositionKey;
import com.inossem.wms.model.key.StockSnKey;
import com.inossem.wms.model.stock.StockBatch;
import com.inossem.wms.model.stock.StockBatchBegin;
import com.inossem.wms.model.stock.StockOccupancy;
import com.inossem.wms.model.stock.StockPosition;
import com.inossem.wms.model.stock.StockPositionHistory;
import com.inossem.wms.model.stock.StockSn;
import com.inossem.wms.model.stock.StockSnHistory;
import com.inossem.wms.model.vo.ApproveUserMatReqVo;
import com.inossem.wms.model.vo.ApproveUserVo;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BufFactoryMatVo;
import com.inossem.wms.model.vo.CheckLockFacLocVo;
import com.inossem.wms.model.vo.DicAccountPeriodVo;
import com.inossem.wms.model.vo.DicFactoryVo;
import com.inossem.wms.model.vo.DicMoveTypeVo;
import com.inossem.wms.model.vo.RelUserStockLocationVo;
import com.inossem.wms.model.vo.SupplierVo;
import com.inossem.wms.model.wf.WfReceipt;
import com.inossem.wms.model.wf.WfReceiptHistory;
import com.inossem.wms.model.wf.WfReceiptKey;
import com.inossem.wms.model.wf.WfReceiptUser;
import com.inossem.wms.service.auth.IUserService;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.intfc.IStockInput;
import com.inossem.wms.util.UtilConst;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilMethod;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilOracleConnection;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilREST;
import com.inossem.wms.util.UtilString;
import com.inossem.wms.util.UtilTimestamp;
import com.inossem.wms.wsdl.oapush.ISysNotifyTodoWebServiceService;
import com.inossem.wms.wsdl.oapush.NotifyTodoAppResult;
import com.inossem.wms.wsdl.oapush.NotifyTodoRemoveContext;
import com.inossem.wms.wsdl.oapush.NotifyTodoSendContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("commonService")
public class CommonServiceImpl implements ICommonService {

	private static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Resource
	private DicBoardMapper dicBoardMapper;

	@Resource
	private UserMapper userDao;

	@Resource
	private IUserService userService;

	@Autowired
	private DicMoveTypeMapper moveTypeDao;

	@Autowired
	private DicMoveReasonMapper moveReasonDao;

	@Autowired
	private IStockInput stockInputSap;

	/*@Autowired
	private RuntimeService runtimeService;*/

	@Autowired
	private DicAccountPeriodMapper dicAccountPeriodDao;

	@Autowired
	private DicWarehousePositionMapper whpDao;

	@Autowired
	private DicFactoryMapper ftyDao;

	@Autowired
	private BizMaterialDocHeadMapper bizMaterialHeadDocDao;

	@Autowired
	private BizMaterialDocItemMapper bizMaterialItemDocDao;

	@Autowired
	private StockBatchMapper stockBatchDao;

	@Autowired
	private StockPositionMapper stockPositionDao;

	@Autowired
	private StockPositionHistoryMapper stockPositionHistoryDao;

	@Autowired
	private StockSnMapper stockSnDao;

	@Autowired
	private StockSnHistoryMapper stockSnHistoryDao;

	@Autowired
	private DicStockLocationMapper dicStockLocationDao;

	@Autowired
	private SequenceDAO sequenceDao;

	@Autowired
	private FlowableMapper flowableDao;

	@Autowired
	private WfReceiptUserMapper wfReceiptUserDao;

	/*@Autowired
	private TaskService taskService;
*/
	@Autowired
	private BatchMaterialSpecMapper batchMaterialSpecDao;

	@Autowired
	private BatchMaterialMapper batchMaterialDao;

	@Autowired
	private DicBatchSpecMapper batchSpecDao;

	@Autowired
	private WfReceiptMapper wfReceiptDao;

	@Autowired
	private WfReceiptHistoryMapper wfReceiptHistoryDao;

	@Autowired
	private BizStockTaskReqItemMapper stockTaskReqItemDao;

	@Autowired
	private BizStockTaskReqHeadMapper stockTaskReqHeadDao;

	@Autowired
	private DicWarehousePositionMapper warehousePositionDap;

	@Autowired
	private DicWarehouseAreaMapper warehouseArea;

	@Autowired
	private DicWarehouseMapper warehouseMapper;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private BufFactoryMatPriceMapper bufFactoryMatPriceDao;

	@Autowired
	private DicPackageTypeMapper packageTypeDao;

	@Autowired
	private DicWarehousePalletMapper warehousePalletDao;

	@Autowired
	private DicClassTypeMapper classTypeDao;

	@Autowired
	private StockOccupancyMapper stockOccupancyDao;

	@Autowired
	private BizStockInputTransportHeadMapper bizStockInputHeadDao;

	@Autowired
	private DicProductLineMapper dicProductLineDao;

	@Autowired
	private RelMatErpbatchMapper relMatErpbatchDao;

	@Autowired
	private DicMaterialMapper materialDao;

	@Autowired
	private StockBatchBeginMapper stockBatchBeginDao;

	@Autowired
	private DicCityMapper dicCityDao;

	@Autowired
	private DicSupplierMapper supplierDao;

	@Autowired
	private RelMatErpbatchMapper matErpbatchDao;

	@Autowired
	private DicDeliveryDriverMapper dicDeliveryDriverMapper;

	@Autowired
	private DicDeliveryVehicleMapper dicDeliveryVehicleMapper;

	@Autowired
	private DicProductLineMapper dicProductLineMapper;

	@Autowired
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;
	
	@Autowired
	private BizFailMesMapper failMesDao;
	
    @Autowired
    private DicPrinterMapper printerDao;
    
    @Autowired
    private DicBoardMapper boardDao;
    
    @Autowired
    private DicCorpMapper corpDao;
    
    
  
	@Override
	public String getPostDate(Integer ftyId) throws Exception {
		Date date = new Date();
		String postDate = UtilString.getShortStringForDate(date);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("postDate", date);
		map.put("ftyId", ftyId);
		List<DicAccountPeriodVo> apList = dicAccountPeriodDao.selectPostDateByFtyId(map);
		if (apList != null && apList.size() > 0) {
			postDate = UtilString.getShortStringForDate(apList.get(0).getAccountFactDate());
		}
		return postDate;
	}

	@Override
	public List<DicMoveType> getMoveTypeByReceiptType(Byte receiptType) throws Exception {

		if (receiptType == null) {
			return moveTypeDao.selectAll();
		} else {
			return moveTypeDao.selectByReceiptType(receiptType);
		}
	}

	@Override
	public List<DicMoveReason> getReasonByMoveTypeId(Integer moveTypeId) throws Exception {
		return moveReasonDao.selectByMoveTypeId(moveTypeId);
	}

	@Override
	public List<SupplierVo> getSupplierList(SupplierVo supplier) throws Exception {
		return stockInputSap.getSupplierList(supplier);
	}

	/**
	 * 在出入库转储时，传入的List结构判断库存地点和仓位状态, 默认仓位不进行校验，但是所属的库存地点需要校验
	 * 
	 * @param listParam
	 *            ListParam 每一个元素对应一个库存地点或者仓位。存储结构如下 库存地点级别check时结构如下
	 *            Map:"location_id"->"12321" 库存地点ID 仓位级别的check时结构如下
	 *            Map:"position_id"->"2131" 仓位ID
	 * 
	 * @return FreezeCheck 入库时： 判断 FreezeCheck.getResultMsgInput()是否为空字符串
	 *         如果为空代表没有被锁定的.如果非空,返回结构如下. 库存地点0021已被冻结，请检查。
	 *         库存地点0004下仓位01-0003在盘点中，已被盘点冻结，请检查。
	 *         库存地点0021下仓位11111在仓位主数据配置中已设置入库冻结，请检查。 出库时： 判断
	 *         FreezeCheck.getResultMsgOutput()是否为空字符串 如果为空代表没有被锁定的.如果非空,返回结构如下.
	 *         库存地点0021已被冻结，请检查。 库存地点0004下仓位01-0003在盘点中，已被盘点冻结，请检查。
	 *         库存地点0021下仓位11111在仓位主数据配置中已设置出库冻结，请检查。
	 * 
	 */
	@Override
	public FreezeCheck checkWhPosAndStockLoc(List<Map<String, String>> listParam) {
		FreezeCheck ret = new FreezeCheck();
		String msgInput = "";
		String msgOutput = "";

		List<CheckLockFacLocVo> lockedInputDicStockLocation = new ArrayList<CheckLockFacLocVo>(); // 入库被锁定的库存地点
		List<DicWarehousePosition> lockedInputDicWarehousePosition = new ArrayList<DicWarehousePosition>(); // 入库被锁定的仓位
		List<CheckLockFacLocVo> lockedOutputDicStockLocation = new ArrayList<CheckLockFacLocVo>(); // 出库被锁定的库存地点
		List<DicWarehousePosition> lockedOutputDicWarehousePosition = new ArrayList<DicWarehousePosition>(); // 出库被锁定的仓位

		boolean lockedInput = false; // 入库用 false->没有被锁定；true被锁定
		boolean lockedOutput = false; // 出库用 false->没有被锁定；true被锁定

		// 默认仓位不会进入本check函数
		// String defaultCcq = UtilProperties.getInstance().getDefaultCCQ(); //
		// '存储区'
		// String defaultCw = UtilProperties.getInstance().getDefaultCW(); //
		// '仓位'

		try {
			// 1 在库存地点级别检查
			// 1.1 检索出所有的库存地点
			List<CheckLockFacLocVo> tList = ftyDao.selectAllFactoryAndLocationList();

			// 1.2 将全部的库存地点的主键放入到set中
			Set<String> setAll = new HashSet<String>();
			for (Map<String, String> map : listParam) {
				setAll.add(map.get("location_id"));
			}

			// 1.3 临时变量
			String werksAndLgort = null;
			String enabled = null;
			String freeze_output = null;
			String freeze_input = null;
			CheckLockFacLocVo dicStockLocation = null;

			// 1.4 传入的库存地点判断是否被锁定
			Iterator<String> it = setAll.iterator();
			while (it.hasNext()) {
				werksAndLgort = it.next();
				for (CheckLockFacLocVo bflv : tList) {
					if (werksAndLgort.equals(bflv.getLocationId().toString())) {

						enabled = bflv.getEnabled().toString();
						freeze_output = bflv.getFreezeOutput().toString();
						freeze_input = bflv.getFreezeInput().toString();
						// 判断出库是否可用
						if ("0".equals(enabled) || "0".equals(freeze_output)) {
							msgOutput += "库存地点" + bflv.getLocationCode() + "已被冻结，请检查。\r\n";
							lockedOutput = true;
							dicStockLocation = new CheckLockFacLocVo();
							dicStockLocation.setLocationCode(bflv.getLocationCode());
							dicStockLocation.setFtyCode(bflv.getFtyCode());
							dicStockLocation.setLocationName(bflv.getLocationName());
							lockedOutputDicStockLocation.add(dicStockLocation);
						}
						// 判断入库是否可用
						if ("0".equals(enabled) || "0".equals(freeze_input)) {
							msgInput += "库存地点" + bflv.getLocationCode() + "已被冻结，请检查。\r\n";
							lockedInput = true;
							dicStockLocation = new CheckLockFacLocVo();
							dicStockLocation.setLocationCode(bflv.getLocationCode());
							dicStockLocation.setFtyCode(bflv.getFtyCode());
							dicStockLocation.setLocationName(bflv.getLocationName());
							lockedOutputDicStockLocation.add(dicStockLocation);
						}
					}
				}
			}

			// 2 仓位级别判断
			// 2.1 检查仓位是否被冻结
			Set<String> setCw = new HashSet<String>();
			for (Map<String, String> map : listParam) {
				// 仓位ID不为空
				if (map.get("position_id") != null && StringUtils.hasLength(map.get("position_id"))) {
					setCw.add(map.get("position_id"));
				}
			}

			// 遍历仓位
			it = setCw.iterator();
			while (it.hasNext()) {
				Integer positionId = Integer.parseInt(it.next());
				// 取得仓位
				DicWarehousePosition dicWarehousePosition = whpDao.selectByPrimaryKey(positionId);
				DicStockLocation dsl = null;

				// 如果仓位检索到了，说明数据合法
				if (dicWarehousePosition != null) {
					// 仓位盘点锁定
					if (EnumFreeze.FREEZE.getValue().equals(dicWarehousePosition.getFreezeStocktake())) {
						dsl = dicStockLocationDao.selectLocationCodeByPositionId(positionId);

						msgInput += "库存地点" + dsl.getLocationName() + "(" + dsl.getLocationCode() + ")" + "下仓位"
								+ dicWarehousePosition.getPositionCode() + "在盘点中，已被盘点冻结，请检查。\r\n";

						lockedInput = true;
						lockedOutput = true;
						lockedInputDicWarehousePosition.add(dicWarehousePosition);
						lockedOutputDicWarehousePosition.add(dicWarehousePosition);
					}
					// 入库冻结标识
					if (EnumFreeze.FREEZE.getValue().equals(dicWarehousePosition.getFreezeInput())) {
						// 本次循环如果已经取得则不需要重新检索
						if (dsl == null) {
							dsl = dicStockLocationDao.selectLocationCodeByPositionId(positionId);
						}
						msgInput += "库存地点" + dsl.getLocationName() + "(" + dsl.getLocationCode() + ")" + "下仓位"
								+ dicWarehousePosition.getPositionCode() + "在仓位主数据配置中已设置入库冻结，请检查。\r\n";
						lockedInput = true;
						lockedInputDicWarehousePosition.add(dicWarehousePosition);
					}

					// 出库冻结标识
					if (EnumFreeze.FREEZE.getValue().equals(dicWarehousePosition.getFreezeOutput())) {
						if (dsl == null) {
							dsl = dicStockLocationDao.selectLocationCodeByPositionId(positionId);
						}

						msgOutput += "库存地点" + dsl.getLocationName() + "(" + dsl.getLocationCode() + ")" + "下仓位"
								+ dicWarehousePosition.getPositionCode() + "在仓位主数据配置中已设置出库冻结，请检查。\r\n";
						lockedOutput = true;
						lockedOutputDicWarehousePosition.add(dicWarehousePosition);
					}

				} else { // 如果仓位没检索到了，说明数据不合法

					msgInput += "仓位ID:" + positionId + "没有被检索到，数据异常。\r\n";
					msgOutput += "仓位ID:" + positionId + "没有被检索到，数据异常。\r\n";

					lockedInput = true;
					lockedOutput = true;
					lockedInputDicWarehousePosition.add(dicWarehousePosition);
					lockedOutputDicWarehousePosition.add(dicWarehousePosition);
				}
			}

			// 3 设置结果
			ret.setLockedOutput(lockedOutput);
			ret.setLockedInput(lockedInput);
			ret.setResultMsgInput(msgInput);
			ret.setResultMsgOutput(msgOutput);
			ret.setLockedInputDicStockLocation(lockedInputDicStockLocation);
			ret.setLockedInputDicWarehousePosition(lockedInputDicWarehousePosition);
			ret.setLockedOutputDicStockLocation(lockedOutputDicStockLocation);
			ret.setLockedOutputDicWarehousePosition(lockedOutputDicWarehousePosition);
		} catch (Exception e) {
			ret.setLockedOutput(lockedOutput);
			ret.setLockedInput(lockedInput);
			ret.setResultMsgInput(msgInput);
			ret.setResultMsgOutput(msgOutput);
			ret.setLockedInputDicStockLocation(lockedInputDicStockLocation);
			ret.setLockedInputDicWarehousePosition(lockedInputDicWarehousePosition);
			ret.setLockedOutputDicStockLocation(lockedOutputDicStockLocation);
			ret.setLockedOutputDicWarehousePosition(lockedOutputDicWarehousePosition);
			return ret;
		}
		return ret;
	}

	@Override
	public List<RelUserStockLocationVo> getStockLocationForUser(String userId) {
		List<RelUserStockLocationVo> list = dicStockLocationDao.selectLocationForUser(userId);
		return list;
	}

	/**
	 * 修改批次库存通用方法
	 * 
	 * @param 创建人
	 * @param 物料凭证抬头
	 * @param 物料凭证明细
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean modifyStockBatchByMaterialDoc(String userId, BizMaterialDocHead bizMaterialDocHead,
			List<BizMaterialDocItem> list) throws Exception {
		boolean ret = false;

		bizMaterialHeadDocDao.insertSelective(bizMaterialDocHead);
		for (BizMaterialDocItem bizMaterialDocItem : list) {
			bizMaterialDocItem.setMatDocId(bizMaterialDocHead.getMatDocId());
			bizMaterialItemDocDao.insertSelective(bizMaterialDocItem);
			// 查询批次库存信息
			StockBatchKey key = new StockBatchKey();
			key.setMatId(bizMaterialDocItem.getMatId()); // '物料编号',
			key.setLocationId(bizMaterialDocItem.getLocationId());// '库存地点',
			key.setBatch(bizMaterialDocItem.getBatch()); // '批号',
			key.setStatus(bizMaterialDocItem.getStatus());
			StockBatch stockBatch = stockBatchDao.selectByUniqueKey(key);
			if (stockBatch == null) {
				// 在该工厂-库存地点不存在该批次的物料
				stockBatch = new StockBatch();
				stockBatch.setMatId(bizMaterialDocItem.getMatId()); // '物料编号',
				stockBatch.setLocationId(bizMaterialDocItem.getLocationId());// '库存地点',
				stockBatch.setBatch(bizMaterialDocItem.getBatch()); // '批号',
				// stockBatch.set(UtilString.getShortStringForDate(new
				// Date()));// '创建日期',
				stockBatch.setCreateUser(userId); // '创建人',

				// mchb.setCumlm(new BigDecimal(0)); // '在途库存',
				// mchb.setCinsm(new BigDecimal(0));// '质量检验库存 ',
				// mchb.setCspem(new BigDecimal(0)); // '冻结的库存',
				stockBatch.setSpecStock(bizMaterialDocItem.getSpecStock());// 特殊库存标识
				stockBatch.setSpecStockCode(bizMaterialDocItem.getSpecStockCode());// 特殊库存代码
				stockBatch.setSpecStockName(bizMaterialDocItem.getSpecStockName());// 特殊库存描述
				stockBatch.setStatus(bizMaterialDocItem.getStatus());
				// mchb.setStatus(Const.MCHB_STATUS_UNRESTRICTED);

				if ("H".equalsIgnoreCase(bizMaterialDocItem.getDebitCredit())) {
					stockBatch.setQty(bizMaterialDocItem.getQty().negate());// '非限制库存
																			// ',
				} else if ("S".equalsIgnoreCase(bizMaterialDocItem.getDebitCredit())) {
					stockBatch.setQty(bizMaterialDocItem.getQty());// '非限制库存
																	// ',
				} else {
					throw new SAPDebitCreditException();
				}

				stockBatchDao.insertSelective(stockBatch);
			} else {
				StockBatch stockBatchTmp = new StockBatch();
				stockBatchTmp.setMatId(bizMaterialDocItem.getMatId()); // '物料编号',
				stockBatchTmp.setLocationId(bizMaterialDocItem.getLocationId());// '库存地点',
				stockBatchTmp.setBatch(bizMaterialDocItem.getBatch()); // '批号',
				stockBatchTmp.setStatus(bizMaterialDocItem.getStatus());
				if ("H".equalsIgnoreCase(bizMaterialDocItem.getDebitCredit())) {
					stockBatchTmp.setQty(stockBatch.getQty().subtract(bizMaterialDocItem.getQty()));
				} else if ("S".equalsIgnoreCase(bizMaterialDocItem.getDebitCredit())) {
					stockBatchTmp.setQty(stockBatch.getQty().add(bizMaterialDocItem.getQty()));
				} else {
					throw new SAPDebitCreditException();
				}
				if (BigDecimal.ZERO.compareTo(stockBatchTmp.getQty()) == 0) {
					stockBatchDao.deleteByByUniqueKey(key);
				} else {
					stockBatchDao.updateQty(stockBatchTmp);
				}
			}
		}
		return ret;
	}

	/**
	 * 修改仓位库存SN库存通用方法
	 * 
	 * @param 郝顺平
	 * @param 物料凭证抬头
	 * @param 物料凭证明细
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean modifyStockPositionAndSn(StockPosition param, List<StockSn> list) throws Exception {
		boolean ret = false;

		StockPosition stockPosition;
		if (param.getId() != null && param.getId() > 0) {
			stockPosition = stockPositionDao.selectByPrimaryKey(param.getId());
		} else {
			// 根据唯一键查询仓位库存是否存在
			StockPositionKey key = new StockPositionKey();
			key.setPositionId(param.getPositionId());// 仓位
			// key.setPalletId(param.getPalletId());// 托盘
			key.setPackageId(param.getPackageId());// 包
			key.setMatId(param.getMatId()); // 物料id,
			key.setLocationId(param.getLocationId());// 库存地点id
			key.setBatch(param.getBatch()); // 批号id
			key.setStatus(param.getStatus());
			stockPosition = stockPositionDao.selectByUniqueKey(key);
		}
		// 存储仓位库存id,以备sn库存使用
		int stockId;
		if (stockPosition == null) {
			// 如果不存在,直接插入
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty().negate());
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty());
			} else {
				throw new SAPDebitCreditException();
			}

			// 插入仓位库存并取出新添加的仓位库存id
			stockPositionDao.insertSelective(param);
			stockId = param.getId();

			// 仓位库存不存在,意味着sn库存也不存在,直接insert
			for (StockSn stockSn : list) {
				stockSn.setStockId(stockId);
				stockSnDao.insertSelective(stockSn);
			}
		} else {
			// 如果存在,直接取出仓位库存id
			stockId = stockPosition.getId();

			// 创建个临时变量,设置id和修改后数量
			StockPosition stockPositionTmp = new StockPosition();
			stockPositionTmp.setId(stockId);

			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 仓位库存历史
				StockPositionHistory history = new StockPositionHistory(stockPosition);
				history.setQty(param.getQty());
				stockPositionHistoryDao.insertSelective(history);

				// 贷 H 减
				stockPositionTmp.setQty(stockPosition.getQty().subtract(param.getQty()));
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 借 S 加
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
			} else {
				throw new SAPDebitCreditException();
			}

			if (BigDecimal.ZERO.compareTo(stockPositionTmp.getQty()) == 0) {
				// 如果是0直接删除
				stockPositionDao.deleteByPrimaryKey(stockId);
				stockSnDao.deleteByStockId(stockId);
			} else {
				// 如果不是0,直接修改,修改的数量是已经经过计算的
				stockPositionDao.updateByStockQty(stockPositionTmp);
				for (StockSn snParam : list) {
					snParam.setStockId(stockId);
					StockSn stockSn;
					if (snParam.getId() != null && snParam.getId() > 0) {
						stockSn = stockSnDao.selectByPrimaryKey(snParam.getId());
					} else {
						StockSnKey stockSnKey = new StockSnKey();
						stockSnKey.setSnId(snParam.getSnId());
						stockSnKey.setStockId(snParam.getStockId());
						stockSn = stockSnDao.selectByUniqueKey(stockSnKey);
					}

					if (stockSn == null) {
						// 如果不存在,直接插入
						if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(snParam.getDebitOrCredit())) {
							// sn库存历史
							StockSnHistory history = new StockSnHistory(snParam);
							history.setQty(snParam.getQty());
							history.setId(0);
							stockSnHistoryDao.insertSelective(history);

							snParam.setQty(snParam.getQty().negate());
						} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(snParam.getDebitOrCredit())) {
							snParam.setQty(snParam.getQty());
						} else {
							throw new SAPDebitCreditException();
						}
						stockSnDao.insertSelective(snParam);
					} else {
						StockSn stockSnTmp = new StockSn();
						stockSnTmp.setId(stockSn.getId());

						if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(snParam.getDebitOrCredit())) {
							// sn库存历史
							StockSnHistory history = new StockSnHistory(snParam);
							history.setQty(snParam.getQty());
							history.setId(0);
							stockSnHistoryDao.insertSelective(history);

							// 贷 H 减
							stockSnTmp.setQty(stockSn.getQty().subtract(snParam.getQty()));
						} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(snParam.getDebitOrCredit())) {
							// 借 S 加
							stockSnTmp.setQty(stockSn.getQty().add(snParam.getQty()));
						} else {
							throw new SAPDebitCreditException();
						}

						if (BigDecimal.ZERO.compareTo(stockSnTmp.getQty()) == 0) {
							// 如果是0直接删除
							stockSnDao.deleteByPrimaryKey(stockSn.getId());
						} else {
							stockSnDao.updateByStockQty(stockSnTmp);
						}
					}
				}
			}
		}

		return ret;
	}

	@Override
	public String getNextReceiptCode(String seqName) throws Exception {
		return UtilString.getStringOrEmptyForObject(sequenceDao.selectNextVal(seqName));
	}

	@Override
	public Long getBatch() throws Exception {
		return sequenceDao.selectNextVal(EnumSequence.BATCH.getValue());
	}

	/**
	 * 发送新代办
	 * 
	 * @date 2017年10月24日
	 * @author 高海涛
	 */
	public int oaPushSendTodo(Map<String, Object> param) {
		try {
			NotifyTodoSendContext context = new NotifyTodoSendContext();
			context.setAppName("WMS");
			context.setModelId(UtilString.getStringOrEmptyForObject(param.get("modelId")));
			context.setModelName(UtilString.getStringOrEmptyForObject(param.get("modelName")));
			context.setSubject(UtilString.getStringOrEmptyForObject(param.get("subject")));
			context.setLink(UtilString.getStringOrEmptyForObject(param.get("link")));
			context.setType(UtilObject.getIntOrZero(param.get("type")));
			context.setTargets(param.get("targets").toString());
			context.setKey("");
			context.setParam1("");
			context.setParam2("");
			context.setCreateTime(UtilString.getLongStringForDate(new Date()));

			// ISysNotifyTodoWebService service =
			// (ISysNotifyTodoWebService)callService(PropertiesUtil.getInstance().getOa_wsdl_url());
			ISysNotifyTodoWebServiceService service = new ISysNotifyTodoWebServiceService();
			// NotifyTodoAppResult result = service.sendTodo(context);
			// ISysNotifyTodoWebService sysNotifyTodoWebService =
			// service.getISysNotifyTodoWebServicePort();
			// BindingProvider bindingProvider = (BindingProvider)
			// sysNotifyTodoWebService;
			// Map<String, Object> requestContext =
			// bindingProvider.getRequestContext();
			// requestContext.put("com.sun.xml.internal.ws.connection.timeout",
			// 3 * 1000);// 建立连接的超时时间为3秒
			// requestContext.put("com.sun.xml.internal.ws.request.timeout", 3 *
			// 1000);// 指定请求的响应超时时间为3秒
			// NotifyTodoAppResult result =
			// sysNotifyTodoWebService.sendTodo(context);
			NotifyTodoAppResult result = service.getISysNotifyTodoWebServicePort().sendTodo(context);
			return result.getReturnState();
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return 0;
	}

	@Override
	public int addReceiptUser(int receiptId, int receiptType, String userId) {
		WfReceiptUser receiptApprove = new WfReceiptUser();
		receiptApprove.setReceiptId(receiptId);
		receiptApprove.setReceiptType((byte) receiptType);
		receiptApprove.setUserId(userId);
		receiptApprove.setRoleId(0);
		return wfReceiptUserDao.insert(receiptApprove);
	}

	@Override
	public int addReceiptUser(int receiptId, int receiptType, String userId, int roleId) {
		WfReceiptUser receiptApprove = new WfReceiptUser();
		receiptApprove.setReceiptId(receiptId);
		receiptApprove.setReceiptType((byte) receiptType);
		receiptApprove.setUserId(userId);
		receiptApprove.setRoleId(roleId);
		return wfReceiptUserDao.insertSelective(receiptApprove);
	}

	@Override
	public int removeReceiptUser(int receiptId, int receiptType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		return wfReceiptUserDao.deleteByReceiptIdAndReceiptType(map);
	}

	@Override
	public JSONArray getReceiptUserForMatReq(int matReqId, int receiptType) {
		JSONArray approveUserAry = new JSONArray();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", matReqId);
		map.put("receiptType", receiptType);
		List<ApproveUserMatReqVo> list = flowableDao.selectCandidateUsersByReceiptIDAndType(map);

		JSONObject approveUserJson;

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 判断角色是否已经被审批,如果已经审批,针对该角色未审批的人不再显示
		HashSet<String> nodeSet = new HashSet<String>();
		for (ApproveUserMatReqVo approveUserMatReqVo : list) {
			String nodeName = approveUserMatReqVo.getNodeName();
			// 如果角色已经审批,则在set中存储该角色,用来下次循环中校验
			if (approveUserMatReqVo.getApprove() != null && approveUserMatReqVo.getApprove() > 0) {
				if (!nodeSet.contains(nodeName)) {
					nodeSet.add(nodeName);
				}
			}
		}
		String nodeName = "";
		StringBuffer receiptId = new StringBuffer();
		StringBuffer corpName = new StringBuffer();
		StringBuffer orgName = new StringBuffer();
		StringBuffer userName = new StringBuffer();

		for (ApproveUserMatReqVo approveUserMatReqVo : list) {
			// 校验该角色是否已经审批
			if (nodeSet.contains(approveUserMatReqVo.getNodeName())) {
				// 如果审批,该审批人未审批,则该审批人信息不显示
				if (approveUserMatReqVo.getApprove() == null || approveUserMatReqVo.getApprove() <= 0) {
					continue;
				}
				approveUserJson = new JSONObject();
				approveUserJson.put("node_name",
						UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getNodeName()));
				approveUserJson.put("receipt_id",
						UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getReceiptId()));
				approveUserJson.put("corp_name",
						UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getCorpName()));
				approveUserJson.put("org_name", UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getOrgName()));
				approveUserJson.put("user_name",
						UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getUserName()));
				approveUserJson.put("approve_name", approveUserMatReqVo.getApproveName());

				approveUserJson.put("approve_time",
						UtilString.getLongStringForDate(approveUserMatReqVo.getApproveTime()));

				approveUserJson.put("comment", approveUserMatReqVo.getComment());
				approveUserAry.add(approveUserJson);
			} else {
				// 角色未审批,合并所有审批人
				nodeName = UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getNodeName());
				receiptId.append(UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getReceiptId()))
						.append("<br/>");
				corpName.append(UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getCorpName()))
						.append("<br/>");
				orgName.append(UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getOrgName())).append("<br/>");
				userName.append(UtilString.getStringOrEmptyForObject(approveUserMatReqVo.getUserName()))
						.append("<br/>");
			}
		}

		if (receiptId.length() > 0) {
			approveUserJson = new JSONObject();
			approveUserJson.put("node_name", nodeName);
			approveUserJson.put("receipt_id", receiptId.substring(0, receiptId.length() - 5));
			approveUserJson.put("corp_name", corpName.substring(0, corpName.length() - 5));
			approveUserJson.put("org_name", orgName.substring(0, orgName.length() - 5));
			approveUserJson.put("user_name", userName.substring(0, userName.length() - 5));
			approveUserJson.put("approve_name", "");
			approveUserJson.put("approve_time", "");
			approveUserJson.put("comment", "");
			approveUserAry.add(approveUserJson);
		}
		return approveUserAry;
	}

	@Override
	public List<ApproveUserVo> getReceiptUser(int receiptId, int receiptType) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		return flowableDao.selectByReceiptIDAndReceiptType(map);
	}

	/**
	 * 默认审批功能,返回审批成功后 单据是否完成
	 */
	@Override
	public boolean taskDefaultApprove(int receiptId, byte receiptType, String processInstanceId, String approvePerson) {

		// 获取当前任务
		/*Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(approvePerson)
				.singleResult();*/
		if (true) {
			throw new NullPointerException("没有这个任务");
		} else {

		/*	String eid = task.getExecutionId();*/

			int rejectCount;

			/*if (runtimeService.hasVariable(eid, "rejectCount")) {
				rejectCount = Integer.parseInt(runtimeService.getVariable(eid, "rejectCount").toString());
			} else {
				rejectCount = 0;
			}*/

			//runtimeService.setVariable(eid, "rejectCount", rejectCount);

			// 任务完成
			//taskService.complete(task.getId());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receiptId", receiptId);
			map.put("receiptType", receiptType);
			map.put("userId", approvePerson);
			WfReceiptUser wfReceiptUser = wfReceiptUserDao.selectByReceiptIdAndReceiptTypeAndUserId(map);
			if (wfReceiptUser == null) {
				wfReceiptUser = new WfReceiptUser();
				wfReceiptUser.setUserId(approvePerson);
				wfReceiptUser.setReceiptType(receiptType);
				wfReceiptUser.setReceiptId(receiptId);
				//wfReceiptUser.setTaskId(task.getId());
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_APPROVE.getValue());
				wfReceiptUser.setApproveTime(new Date());
				wfReceiptUserDao.insertSelective(wfReceiptUser);
			} else {
				//wfReceiptUser.setTaskId(task.getId());
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_APPROVE.getValue());
				wfReceiptUser.setApproveTime(new Date());
				wfReceiptUserDao.updateApproveMsg(wfReceiptUser);
			}

			/*ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
					.singleResult();*/

			// 这个默认审批不改状态
			// if (pi == null) {
			// return EnumMatReqStatus.RECEIPT_APPROVE_COMPLETE;
			// } else {
			// return Const.RECEIPT_APPROVE_INCOMPLETE;
			// }

			return false;
		}
	}

	@Override
	public List<Map<String, String>> getOperatorRoles() throws Exception {

		List<Map<String, String>> rolesList = EnumOperatorRoles.toList();

		return rolesList;
	}

	@Override
	public List<Map<String, String>> getOperatorTypes() throws Exception {

		List<Map<String, String>> typeList = EnumOperatorTypes.toList();

		return typeList;
	}

	@Override
	public List<User> getOperatorUsers(User user) throws Exception {
		ArrayList<User> userList = new ArrayList<User>();
		userList = userDao.selectJBRUser(user);
		return userList;
	}

	/**
	 * 代办完成
	 * 
	 * @date 2017年10月24日
	 * @author 高海涛
	 */
	public int oaPushSetTodoDone(Map<String, Object> param) {
		try {

			NotifyTodoRemoveContext context = new NotifyTodoRemoveContext();
			context.setAppName("WMS");
			context.setModelId(UtilString.getStringOrEmptyForObject(param.get("modelId")));
			context.setModelName(UtilString.getStringOrEmptyForObject(param.get("modelName")));
			context.setOptType(1);
			context.setTargets(param.get("targets").toString());

			ISysNotifyTodoWebServiceService service = new ISysNotifyTodoWebServiceService();
			// ISysNotifyTodoWebService sysNotifyTodoWebService =
			// service.getISysNotifyTodoWebServicePort();
			// BindingProvider bindingProvider = (BindingProvider)
			// sysNotifyTodoWebService;
			// Map<String, Object> requestContext =
			// bindingProvider.getRequestContext();
			// requestContext.put("com.sun.xml.internal.ws.connection.timeout",
			// 3 * 1000);// 建立连接的超时时间为3秒
			// requestContext.put("com.sun.xml.internal.ws.request.timeout", 3 *
			// 1000);// 指定请求的响应超时时间为3秒
			// NotifyTodoAppResult result =
			// sysNotifyTodoWebService.setTodoDone(context);
			NotifyTodoAppResult result = service.getISysNotifyTodoWebServicePort().setTodoDone(context);
			return result.getReturnState();

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(UtilString.STRING_EMPTY, e);
		}

		return 0;
	}

	@Override
	public Map<String, Object> getBatchSpecList(Long batch, Integer ftyId, Integer matId) throws Exception {
		// 查询批次特性值
		Map<String, Object> batchSpecMap = new HashMap<String, Object>();
		List<DicBatchSpec> batchSpecList = new ArrayList<DicBatchSpec>();
		BatchMaterial batchMaterial = null;
		if (batch != null && ftyId != null && matId != null) {
			BatchMaterialSpecKey find = new BatchMaterialSpecKey();
			find.setMatId(matId);
			find.setFtyId(ftyId);
			find.setBatch(batch);

			batchSpecList = batchMaterialSpecDao.selectBatchSpecByUniqueKey(find);
			if (batchSpecList == null || batchSpecList.size() == 0) {
				batchSpecList = batchSpecDao.selectByMatId(matId);
			}
			BatchMaterial key = new BatchMaterial();
			key.setBatch(batch);
			key.setMatId(matId);
			key.setFtyId(ftyId);
			batchMaterial = batchMaterialDao.selectByUniqueKey(key);
		} else if (matId != null) {
			batchSpecList = batchSpecDao.selectByMatId(matId);

		}
		if (batchMaterial == null) {
			batchMaterial = new BatchMaterial();
		}
		if (batchSpecList != null && batchSpecList.size() > 0) {
			for (DicBatchSpec bs : batchSpecList) {
				if (!StringUtils.hasText(bs.getInfo())) {
					bs.setInfo("[]");
				}
			}
		}
		List<DicBatchSpec> batchMaterialSpecList = this.getFixedBatchMaterialSpec(batchMaterial, batchSpecList);
		batchSpecMap.put("batchSpecList", batchSpecList);
		batchSpecMap.put("batchMaterialSpecList", batchMaterialSpecList);
		return batchSpecMap;

	}

	@Override
	public List<DicBatchSpec> getFixedBatchMaterialSpec(BatchMaterial batchMaterial, List<DicBatchSpec> batchSpecList)
			throws Exception {

		List<String> batchSpecCodeList = new ArrayList<String>();
		List<BatchMaterialConfig> bmcList = UtilProperties.getInstance().getBatchMaterialConfigList();

		List<DicBatchSpec> returnList = new ArrayList<DicBatchSpec>();
		for (DicBatchSpec item : batchSpecList) {
			if (StringUtils.hasText(item.getBatchSpecCode()))
				batchSpecCodeList.add(item.getBatchSpecCode());
		}
		for (BatchMaterialConfig item : bmcList) {

			if ((!StringUtils.hasText(item.getBatchSpecCode())) || (StringUtils.hasText(item.getBatchSpecCode())
					&& !batchSpecCodeList.contains(item.getBatchSpecCode()))) {
				DicBatchSpec bs = new DicBatchSpec();
				bs.setBatchSpecCode(item.getColumnName());
				bs.setBatchSpecName(EnumFixedBatchSpec.getNameByValue(item.getColumnName()));
				if (item.isEdit()) {
					bs.setEdit((byte) 1);
				} else {
					bs.setEdit((byte) 0);
				}
				bs.setInfo("[]");
				bs.setRequired((byte) 0);
				// 批次信息属性名
				String fieldName = UtilJSONConvert.humpName(item.getColumnName());
				String value = "";
				Object valueObj = UtilMethod.getValue(batchMaterial, fieldName);
				if (BatchMaterial.class.getDeclaredField(fieldName).getGenericType().toString()
						.equals("class java.util.Date")) {
					value = UtilString.getShortStringForDate((Date) valueObj);
				} else {
					value = UtilString.getStringOrEmptyForObject(valueObj);
				}
				bs.setBatchSpecValue(value);
				bs.setBatchSpecType(EnumFixedBatchSpec.getTypeByValue(item.getColumnName()));
				bs.setDisplayIndex(item.getIndex());
				returnList.add(bs);
			}
		}
		return returnList;

	}

	/**
	 * 初始化审批流程
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String initDefaultWf(int receiptId, byte receiptType, String receiptCode, String createUserId)
			throws Exception {

		String piid = "";

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		List<WfReceiptUser> receiptUserList = wfReceiptUserDao.selectByReceiptIdAndReceiptType(map);

		// 没有审批人员
		if (receiptUserList == null || receiptUserList.size() == 0) {
			return piid;
		}

		// lambda表达式取出审批人ID列表
		List<String> approvePersonnels = receiptUserList.stream().map(ra -> ra.getUserId())
				.collect(Collectors.toList());

		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put("approvePersons", approvePersonnels);

		// 启动验收流程
		ProcessInstance processInstance = null;//runtimeService.startProcessInstanceByKey("defaultApprove", variables);
		piid = processInstance.getId();

		// 获取任务
		for (String approvePerson : approvePersonnels) {
			/*Task task = taskService.createTaskQuery().processInstanceId(piid).taskAssignee(approvePerson)
					.singleResult();*/
			map.put("userId", approvePerson);
			WfReceiptUser receiptUser = wfReceiptUserDao.selectByReceiptIdAndReceiptTypeAndUserId(map);
			// 修改任务ID
			//receiptUser.setTaskId(task.getId());
			wfReceiptUserDao.updateApproveMsg(receiptUser);
		}

		// 修改审批流程ID到审批历史表WfHistory
		WfReceiptKey key = new WfReceiptKey();
		key.setReceiptId(receiptId);
		key.setReceiptType(receiptType);
		WfReceipt wfReceipt = wfReceiptDao.selectByPrimaryKey(key);

		if (wfReceipt != null) {
			// 已经存在,移到历史记录中
			WfReceiptHistory wfReceiptHistory = new WfReceiptHistory();
			wfReceiptHistory.setReceiptId(wfReceipt.getReceiptId());
			wfReceiptHistory.setReceiptType(wfReceipt.getReceiptType());
			wfReceiptHistory.setReceiptCode(wfReceipt.getReceiptCode());
			wfReceiptHistory.setPiid(wfReceipt.getPiid());
			wfReceiptHistory.setReceiptUserId(wfReceipt.getReceiptUserId());
			wfReceiptHistory.setCreateTime(wfReceipt.getCreateTime());
			wfReceiptHistory.setModifyTime(wfReceipt.getModifyTime());
			// 添加历史,删除原数据
			wfReceiptHistoryDao.insertSelective(wfReceiptHistory);

			wfReceiptDao.deleteByPrimaryKey(key);
		}

		// 新建原数据
		wfReceipt = new WfReceipt();
		wfReceipt.setReceiptId(receiptId);
		wfReceipt.setReceiptType(receiptType);
		wfReceipt.setReceiptCode(receiptCode);
		wfReceipt.setPiid(piid);
		wfReceipt.setReceiptUserId(createUserId);
		wfReceiptDao.insertSelective(wfReceipt);

		return piid;

	}

	/**
	 * 初始化角色审批流程
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @return
	 * @throws Exception
	 */
	@Override
	public String initRoleWf(int receiptId, byte receiptType, String receiptCode, String createUserId)
			throws Exception {

		String piid = "";

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		List<WfReceiptUser> receiptUserList = wfReceiptUserDao.selectByReceiptIdAndReceiptType(map);

		// 没有审批人员
		if (receiptUserList == null || receiptUserList.size() == 0) {
			return piid;
		}

		// lambda表达式取出审批人ID列表
		List<String> approvePersonnels = receiptUserList.stream().map(ra -> ra.getUserId())
				.collect(Collectors.toList());

		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put("approvePersons", approvePersonnels);

		// 启动验收流程
		ProcessInstance processInstance = null;//runtimeService.startProcessInstanceByKey("roleApprove", variables);
		piid = processInstance.getId();

		// 获取任务
		for (String approvePerson : approvePersonnels) {
			/*Task task = taskService.createTaskQuery().processInstanceId(piid).taskAssignee(approvePerson)
					.singleResult();*/
			map.put("userId", approvePerson);
			WfReceiptUser receiptUser = wfReceiptUserDao.selectByReceiptIdAndReceiptTypeAndUserId(map);
			// 修改任务ID
			//receiptUser.setTaskId(task.getId());
			wfReceiptUserDao.updateApproveMsg(receiptUser);
		}

		// 修改审批流程ID到审批历史表WfHistory
		WfReceiptKey key = new WfReceiptKey();
		key.setReceiptId(receiptId);
		key.setReceiptType(receiptType);
		WfReceipt wfReceipt = wfReceiptDao.selectByPrimaryKey(key);

		if (wfReceipt != null) {
			// 已经存在,移到历史记录中
			WfReceiptHistory wfReceiptHistory = new WfReceiptHistory();
			wfReceiptHistory.setReceiptId(wfReceipt.getReceiptId());
			wfReceiptHistory.setReceiptType(wfReceipt.getReceiptType());
			wfReceiptHistory.setReceiptCode(wfReceipt.getReceiptCode());
			wfReceiptHistory.setPiid(wfReceipt.getPiid());
			wfReceiptHistory.setReceiptUserId(wfReceipt.getReceiptUserId());
			wfReceiptHistory.setCreateTime(wfReceipt.getCreateTime());
			wfReceiptHistory.setModifyTime(wfReceipt.getModifyTime());
			// 添加历史,删除原数据
			wfReceiptHistoryDao.insertSelective(wfReceiptHistory);

			wfReceiptDao.deleteByPrimaryKey(key);
		}

		// 新建原数据
		wfReceipt = new WfReceipt();
		wfReceipt.setReceiptId(receiptId);
		wfReceipt.setReceiptType(receiptType);
		wfReceipt.setReceiptCode(receiptCode);
		wfReceipt.setPiid(piid);
		wfReceipt.setReceiptUserId(createUserId);
		wfReceiptDao.insertSelective(wfReceipt);

		return piid;

	}

	/**
	 * by wang.ligang 指定角色初始化角色审批流程，
	 * 
	 * @param receiptId
	 * @param receiptType
	 * @param receiptCode
	 * @param createUserId
	 * @param role
	 * @return
	 * @throws Exception
	 */
	@Override
	public String initStocktakeWf(int receiptId, byte receiptType, String receiptCode, String createUserId)
			throws Exception {

		String piid = "";

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("receiptId", receiptId);
		map.put("receiptType", receiptType);
		List<WfReceiptUser> receiptUserList = wfReceiptUserDao.selectByReceiptIdAndReceiptType(map);

		// 没有审批人员
		if (receiptUserList == null || receiptUserList.size() == 0) {
			return piid;
		}

		// lambda表达式取出审批人ID列表
		List<String> approvePersonnels = receiptUserList.stream().map(ra -> ra.getUserId())
				.collect(Collectors.toList());

		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put("approvePersons", approvePersonnels);

		// 启动验收流程
		ProcessInstance processInstance = null;//runtimeService.startProcessInstanceByKey("kcpdApprove", variables);
		piid = processInstance.getId();

		// 获取任务
		for (String approvePerson : approvePersonnels) {
			/*Task task = taskService.createTaskQuery().processInstanceId(piid).taskAssignee(approvePerson)
					.singleResult();*/
			map.put("userId", approvePerson);
			WfReceiptUser receiptUser = wfReceiptUserDao.selectByReceiptIdAndReceiptTypeAndUserId(map);
			// 修改任务ID
			//receiptUser.setTaskId(task.getId());
			wfReceiptUserDao.updateApproveMsg(receiptUser);
		}

		// 修改审批流程ID到审批历史表WfHistory
		WfReceiptKey key = new WfReceiptKey();
		key.setReceiptId(receiptId);
		key.setReceiptType(receiptType);
		WfReceipt wfReceipt = wfReceiptDao.selectByPrimaryKey(key);

		if (wfReceipt != null) {
			// 已经存在,移到历史记录中
			WfReceiptHistory wfReceiptHistory = new WfReceiptHistory();
			wfReceiptHistory.setReceiptId(wfReceipt.getReceiptId());
			wfReceiptHistory.setReceiptType(wfReceipt.getReceiptType());
			wfReceiptHistory.setReceiptCode(wfReceipt.getReceiptCode());
			wfReceiptHistory.setPiid(wfReceipt.getPiid());
			wfReceiptHistory.setReceiptUserId(wfReceipt.getReceiptUserId());
			wfReceiptHistory.setCreateTime(wfReceipt.getCreateTime());
			wfReceiptHistory.setModifyTime(wfReceipt.getModifyTime());
			// 添加历史,删除原数据
			wfReceiptHistoryDao.insertSelective(wfReceiptHistory);

			wfReceiptDao.deleteByPrimaryKey(key);
		}

		// 新建原数据
		wfReceipt = new WfReceipt();
		wfReceipt.setReceiptId(receiptId);
		wfReceipt.setReceiptType(receiptType);
		wfReceipt.setReceiptCode(receiptCode);
		wfReceipt.setPiid(piid);
		wfReceipt.setReceiptUserId(createUserId);
		wfReceiptDao.insertSelective(wfReceipt);

		return piid;

	}

	@Override
	public JSONArray listFtyLocationForUser(String userId, String ftyType) throws Exception {
		boolean flag = false;
		JSONArray ftyAry = new JSONArray();
		List<RelUserStockLocationVo> locationList = userService.listLocForUser(userId);
		Map<Integer, JSONObject> map = new LinkedHashMap<Integer, JSONObject>();
		for (RelUserStockLocationVo location : locationList) {
			flag = true;

			if (!"".equals(ftyType) && ftyType != null) {
				if (!ftyType.equals(location.getFtyType())) {
					flag = false;
				}
			}

			if (flag) {
				if (!map.containsKey(location.getFtyId())) {

					JSONObject ftyObj = new JSONObject();
					ftyObj.put("fty_id", UtilString.getStringOrEmptyForObject(location.getFtyId()));
					ftyObj.put("fty_code", UtilString.getStringOrEmptyForObject(location.getFtyCode()));
					ftyObj.put("fty_name", UtilString.getStringOrEmptyForObject(location.getFtyName()));
					ftyObj.put("board_id", UtilString.getStringOrEmptyForObject(location.getBoardId()));
					ftyObj.put("fty_type",UtilString.getStringOrEmptyForObject(location.getFtyType()));
					ftyObj.put("location_ary", new JSONArray());
					map.put(location.getFtyId(), ftyObj);

				}

				JSONObject ftyObj = map.get(location.getFtyId());
				JSONArray locationAry = ftyObj.getJSONArray("location_ary");

				JSONObject locObj = new JSONObject();
				locObj.put("loc_id", UtilString.getStringOrEmptyForObject(location.getLocationId()));
				locObj.put("loc_code", UtilString.getStringOrEmptyForObject(location.getLocationCode()));
				locObj.put("loc_name", UtilString.getStringOrEmptyForObject(location.getLocationName()));
				locObj.put("loc_index", UtilString.getStringOrEmptyForObject(location.getLocationIndex()));
				locObj.put("wh_id", UtilString.getStringOrEmptyForObject(location.getWhId()));
				locObj.put("wh_code", UtilString.getStringOrEmptyForObject(location.getWhCode()));
				locObj.put("wh_name", UtilString.getStringOrEmptyForObject(location.getWhName()));
				locationAry.add(locObj);
				ftyObj.put("location_ary", locationAry);
			}
		}
		for (Integer ftyId : map.keySet()) {
			ftyAry.add(map.get(ftyId));
		}
		return ftyAry;
	}

	@Override
	public JSONArray listFtyLocationForBoardId(int boardId) throws Exception {
		JSONArray ftyAry = new JSONArray();
		List<RelUserStockLocationVo> locationList = userService.listLocForBoardId(boardId);
		HashMap<Integer, JSONObject> map = new LinkedHashMap<Integer, JSONObject>();
		for (RelUserStockLocationVo location : locationList) {
			if (!map.containsKey(location.getFtyId())) {
				JSONObject ftyObj = new JSONObject();
				ftyObj.put("fty_id", UtilString.getStringOrEmptyForObject(location.getFtyId()));
				ftyObj.put("fty_code", UtilString.getStringOrEmptyForObject(location.getFtyCode()));
				ftyObj.put("fty_name", UtilString.getStringOrEmptyForObject(location.getFtyName()));
				ftyObj.put("location_ary", new JSONArray());
				map.put(location.getFtyId(), ftyObj);

			}

			JSONObject ftyObj = map.get(location.getFtyId());
			JSONArray locationAry = ftyObj.getJSONArray("location_ary");

			JSONObject locObj = new JSONObject();
			locObj.put("loc_id", UtilString.getStringOrEmptyForObject(location.getLocationId()));
			locObj.put("loc_code", UtilString.getStringOrEmptyForObject(location.getLocationCode()));
			locObj.put("loc_name", UtilString.getStringOrEmptyForObject(location.getLocationName()));
			locObj.put("loc_index", UtilString.getStringOrEmptyForObject(location.getLocationIndex()));
			locObj.put("wh_id", UtilString.getStringOrEmptyForObject(location.getWhId()));
			locObj.put("wh_code", UtilString.getStringOrEmptyForObject(location.getWhCode()));
			locObj.put("wh_name", UtilString.getStringOrEmptyForObject(location.getWhName()));
			locationAry.add(locObj);
			ftyObj.put("location_ary", locationAry);

		}
		for (Integer ftyId : map.keySet()) {
			ftyAry.add(map.get(ftyId));
		}
		return ftyAry;
	}

	@Override
	public JSONArray listLocationForUser(String userId, String ftyType) throws Exception {
		JSONArray locAry = new JSONArray();
		List<RelUserStockLocationVo> locationList = userService.listLocForUser(userId);

		boolean flag = false;

		for (RelUserStockLocationVo location : locationList) {
			flag = true;

			if (!"".equals(ftyType) && ftyType != null) {
				if (!ftyType.equals(location.getFtyType())) {
					flag = false;
				}
			}

			if (flag) {
				JSONObject locObj = new JSONObject();
				locObj.put("loc_id", UtilString.getStringOrEmptyForObject(location.getLocationId()));
				locObj.put("loc_code", UtilString.getStringOrEmptyForObject(location.getLocationCode()));
				locObj.put("loc_name", UtilString.getStringOrEmptyForObject(location.getLocationName()));
				locObj.put("fty_id", UtilString.getStringOrEmptyForObject(location.getFtyId()));
				locObj.put("fty_code", UtilString.getStringOrEmptyForObject(location.getFtyCode()));
				locObj.put("fty_name", UtilString.getStringOrEmptyForObject(location.getFtyName()));
				locObj.put("loc_index", UtilString.getStringOrEmptyForObject(location.getLocationIndex()));
				locObj.put("wh_id", UtilString.getStringOrEmptyForObject(location.getWhId()));
				locObj.put("wh_code", UtilString.getStringOrEmptyForObject(location.getWhCode()));
				locObj.put("wh_name", UtilString.getStringOrEmptyForObject(location.getWhName()));
				locObj.put("status", UtilString.getStringOrEmptyForObject(location.getStatus()));
				locAry.add(locObj);
			}
		}
		return locAry;
	}

	@Override
	public List<DicMoveType> listMovTypeAndReason(Byte receiptType) throws Exception {
		List<DicMoveType> returnList = new ArrayList<DicMoveType>();
		List<DicMoveTypeVo> moveTypeVoList = moveTypeDao.selectVoByReceiptType(receiptType);
		HashMap<Integer, DicMoveType> moveMap = new HashMap<Integer, DicMoveType>();
		for (DicMoveTypeVo innerObj : moveTypeVoList) {

			if (!moveMap.containsKey(innerObj.getMoveTypeId())) {
				DicMoveType moveType = new DicMoveType();
				moveType.setMoveTypeCode(innerObj.getMoveTypeCode());
				moveType.setMoveTypeId(innerObj.getMoveTypeId());
				moveType.setMoveTypeName(innerObj.getMoveTypeName());
				List<DicMoveReason> reasonList = new ArrayList<DicMoveReason>();
				moveType.setReasonList(reasonList);
				moveMap.put(innerObj.getMoveTypeId(), moveType);
			}

		}

		for (Integer moveTypeId : moveMap.keySet()) {
			returnList.add(moveMap.get(moveTypeId));
		}
		return returnList;
	}

	// @Override
	// public int deleteBatchMaterialByWerksMatBatch(Integer ftyId, Integer
	// matId, Long batch) throws Exception {
	// BatchMaterial upBatchMaterial = new BatchMaterial();
	// upBatchMaterial.setBatch(batch);
	// upBatchMaterial.setMatId(matId);
	// upBatchMaterial.setFtyId(ftyId);
	// upBatchMaterial.setIsDelete((byte) 1);
	// return batchMaterialDao.updateByPrimaryKeySelective(upBatchMaterial);
	// }
	//
	// @Override
	// public int deleteByFtyIdAndMatIdAndBatch(Integer ftyId, Integer matId,
	// Long batch) throws Exception {
	// Map<String, Object> param = new HashMap<String, Object>();
	// param.put("ftyId", ftyId);
	// param.put("matId", matId);
	// param.put("batch", batch);
	// return batchMaterialSpecDao.deleteByFtyIdAndMatIdAndBatch(param);
	// }

	@Override
	public void updateStockTaskReq(Long batch, Integer matId, Integer ftyId, Integer locationId, BigDecimal updateQty,
			Integer areaId, Integer positionId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("batch", batch);
		map.put("matId", matId);
		map.put("ftyId", ftyId);
		map.put("locationId", locationId);

		BizStockTaskReqItem reqItem = stockTaskReqItemDao.selectByUniqueKey(map);
		DicWarehouseArea areaObj = warehouseArea.selectByPrimaryKey(areaId);
		DicWarehousePosition positionObj = warehousePositionDap.selectByPrimaryKey(positionId);
		if (reqItem != null && areaObj != null && positionObj != null) {

			// 如果从默认存储区发出

			if (UtilProperties.getInstance().getDefaultCCQ().equals(areaObj.getAreaCode())
					&& UtilProperties.getInstance().getDefaultCW().equals(positionObj.getPositionCode())) {

				BizStockTaskReqItem updateItem = new BizStockTaskReqItem();
				updateItem.setItemId(reqItem.getItemId());
				updateItem.setQty(reqItem.getQty().subtract(updateQty));

				BigDecimal sjsl = reqItem.getStockTaskQty();
				// 已上架数量为0 上架总数量为0
				if (BigDecimal.ZERO.compareTo(updateItem.getQty()) == 0 && BigDecimal.ZERO.compareTo(sjsl) == 0) {

					updateItem.setIsDelete((byte) 1);

				} else if (sjsl.compareTo(updateItem.getQty()) == 0) {
					updateItem.setStatus((byte) 1);// 已完成

				}
				stockTaskReqItemDao.updateByPrimaryKeySelective(updateItem);
			} else if (areaId == null && positionId == null) {
				BizStockTaskReqItem updateItem = new BizStockTaskReqItem();
				updateItem.setItemId(reqItem.getItemId());
				updateItem.setIsDelete((byte) 1);
				stockTaskReqItemDao.updateByPrimaryKeySelective(updateItem);

			}
			Byte isComplete = stockTaskReqItemDao.selectMinStatus(reqItem.getStockTaskReqId());
			if (isComplete == null) {
				BizStockTaskReqHead updateHead = new BizStockTaskReqHead();
				updateHead.setStockTaskReqId(reqItem.getStockTaskReqId());
				updateHead.setIsDelete((byte) 1);
				stockTaskReqHeadDao.updateByPrimaryKeySelective(updateHead);

			} else if (isComplete.compareTo(EnumReceiptStatus.RECEIPT_FINISH.getValue()) == 0) {
				BizStockTaskReqHead updateHead = new BizStockTaskReqHead();
				updateHead.setStockTaskReqId(reqItem.getStockTaskReqId());
				updateHead.setStatus(EnumReceiptStatus.RECEIPT_FINISH.getValue());
				stockTaskReqHeadDao.updateByPrimaryKeySelective(updateHead);
			}
		}

	}

	@Override
	public void bufferPrice() {
		List<BufFactoryMatVo> list = stockBatchDao.getFtyCodeMatCode();

		// 根据工厂BufFactoryMatVo::getFtyCode分组
		// 获得物料的set
		Map<String, Set<String>> map = list.stream().collect(Collectors.groupingBy(BufFactoryMatVo::getFtyCode,
				Collectors.mapping(BufFactoryMatVo::getMatCode, Collectors.toSet())));
		List<String> matCodelist = new ArrayList<String>();
		// 遍历每个工厂的所有物料,分300个一组调用接口,并更新数据库
		for (String ftyCode : map.keySet()) {
			Set<String> set = map.get(ftyCode);

			for (String matCode : set) {
				if (!UtilString.isNullOrEmpty(matCode)) {
					matCodelist.add(matCode);
				}
				if (matCodelist.size() >= 500) {
					syncPriceForMatCodeAry(matCodelist, ftyCode);
					matCodelist.clear();
				}
			}

			if (matCodelist.size() > 0) {
				syncPriceForMatCodeAry(matCodelist, ftyCode);
				matCodelist.clear();
			}
		}
	}

	@Override
	public void bufferERPBatch() throws Exception {
		List<String> list = materialDao.selectMatCode();

		List<String> matCodelist = new ArrayList<String>();

		for (String matCode : list) {
			if (!UtilString.isNullOrEmpty(matCode)) {
				matCodelist.add(matCode);
			}
			if (matCodelist.size() >= 500) {
				synErpBatch(matCodelist);
				matCodelist.clear();
			}
		}

		if (matCodelist.size() > 0) {
			synErpBatch(matCodelist);
			matCodelist.clear();
		}

	}

	/**
	 * 根据一组的物料编码和一个工厂编码查询参考价格
	 * 
	 * @param matCodeList
	 * @param ftyCode
	 */
	private void syncPriceForMatCodeAry(List<String> matCodeList, String ftyCode) {
		try {
			// 获取id
			int ftyId = dictionaryService.getFtyIdByFtyCode(ftyCode);
			Map<String, Integer> map = dictionaryService.getMatMapByCodeList(matCodeList);

			// list转ary
			// String[] matCodeAry = new String[matCodeList.size()];
			// matCodeList.toArray(matCodeAry);
			// 调用接口查询价格
			// JSONArray ary = matReqImpl.getReferencePrice(matCodeAry, ftyCode,
			// "wms");
			JSONArray ary = getMatPriceFromOracle(matCodeList, ftyCode);
			ArrayList<BufFactoryMatPrice> list = new ArrayList<BufFactoryMatPrice>();
			if (ary.size() > 0) {
				for (Object obj : ary) {
					JSONObject json = (JSONObject) obj;
					String matCode = json.getString("MAT_CODE");
					BigDecimal price = UtilJSON.getBigDecimalFromJSON("REFER_PRICE", json);
					BufFactoryMatPrice tmp = new BufFactoryMatPrice();
					tmp.setFtyId(ftyId);
					tmp.setMatId(map.get(matCode));
					tmp.setPrice(price);
					list.add(tmp);
				}

				bufFactoryMatPriceDao.insertOrUpdatePrices(list);
				list.clear();
			}
		} catch (Exception e) {
			logger.error(UtilString.STRING_EMPTY, e);
		}
	}

	@Override
	public List<User> getUsers(User user) throws Exception {
		return userDao.selectUser(user);
	}

	@Override
	public JSONObject listBoardFtyAndLocationForUser(String userId) throws Exception {
		JSONObject body = new JSONObject();
		JSONArray locationAry = listFtyLocationForUser(userId, "");
		List<Integer> boardIdList = new ArrayList<Integer>();
		for (Object object : locationAry) {
			JSONObject obj = (JSONObject) object;
			int boardId = obj.getInt("board_id");
			if (!boardIdList.contains(boardId)) {
				boardIdList.add(boardId);
			}
		}

		String ftyId = locationAry.getJSONObject(0).getString("fty_id");
		String ftyCode = locationAry.getJSONObject(0).getString("fty_code");
		String ftyName = locationAry.getJSONObject(0).getString("fty_name");
		String locationId = locationAry.getJSONObject(0).getJSONArray("location_ary").getJSONObject(0)
				.getString("loc_id");
		String locationCode = locationAry.getJSONObject(0).getJSONArray("location_ary").getJSONObject(0)
				.getString("loc_code");
		String locationName = locationAry.getJSONObject(0).getJSONArray("location_ary").getJSONObject(0)
				.getString("loc_name");
		Integer boardId = locationAry.getJSONObject(0).getInt("board_id");
		String boardName = EnumBoard.getNameByValue(boardId);

		// List<Map<String, Object>> boradList = EnumBoard.toBoardList();
		// 2018-4-18改成只能查看其权限所在板块 不显示所有板块 boardId boardName
		List<Map<String, Object>> boradList = new ArrayList<Map<String, Object>>();
		for (Integer id : boardIdList) {
			DicBoard borad = dicBoardMapper.selectByPrimaryKey(id);
			if (borad != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("boardId", borad.getBoardId());
				map.put("boardName", borad.getBoardName());
				boradList.add(map);
			}
		}

		for (Map<String, Object> map : boradList) {
			String boadrId = UtilString.getStringOrEmptyForObject(map.get("boardId"));
			map.put("fty_ary", new JSONArray());
			for (int i = 0; i < locationAry.size(); i++) {
				JSONObject ftyObj = locationAry.getJSONObject(i);
				String innerBoardId = ftyObj.getString("board_id");
				if (boadrId.equals(innerBoardId)) {
					JSONArray fty_ary = (JSONArray) map.get("fty_ary");
					fty_ary.add(ftyObj);
					map.put("fty_ary", fty_ary);
				}
			}
		}
		body.put("fty_id", ftyId);
		body.put("fty_code", ftyCode);
		body.put("fty_name", ftyName);
		body.put("location_id", locationId);
		body.put("location_name", locationName);
		body.put("location_code", locationCode);
		body.put("boardId", boardId);
		body.put("boardName", boardName);
		body.put("board_ary", boradList);
		body.put("begin_time", UtilString.getShortStringForDate(UtilConst.getFirstDay()));
		body.put("end_time", UtilString.getShortStringForDate(UtilConst.getLastDay()));
		return body;
	}

	@Override
	public List<DicWarehouse> selectAllWarehouse() {

		return warehouseMapper.selectAll();
	}

	@Override
	public boolean lastRoleTask(int receipt_id, String processInstanceId, String approve_person) {

		// 获取当前任务
		/*Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(approve_person)
				.singleResult();*/
		if (true) {
			return false;
		} else {
			try {
				// 完成会签的次数
				/*int nrOfCompletedInstances = (Integer) (//runtimeService.getVariable(task.getExecutionId(),
						"nrOfCompletedInstances"));

				// 总循环次数
				int nrOfInstances = (Integer) (runtimeService.getVariable(task.getExecutionId(), "nrOfInstances"));
*/
				/*if (nrOfInstances == nrOfCompletedInstances + 1) {
					return true;
				} else {
					return false;
				}*/return false;
			} catch (Exception e) {
				return false;
			}
		}

	}

	@Override
	public int taskRoleApprove1(int receiptId, byte receiptType, String processInstanceId, String approvePerson) {

		// 获取当前任务
		/*Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(approvePerson)
				.singleResult();*/
		if (true) {
			throw new NullPointerException("没有这个任务");
		} else {
		/*	logger.info(String.format("task id : %s,task ProcessInstanceId ： %s", task.getId(),
					task.getProcessInstanceId()));

			String eid = task.getExecutionId();*/

			int rejectCount;
/*
			if (runtimeService.hasVariable(eid, "rejectCount")) {
				rejectCount = Integer.parseInt(runtimeService.getVariable(eid, "rejectCount").toString());
			} else {
				rejectCount = 0;
			}

			runtimeService.setVariable(eid, "rejectCount", rejectCount);*/

			// 任务完成
			//taskService.complete(task.getId());

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("receipt_id", receiptId);
			map.put("receiptType", receiptType);
			map.put("user_id", approvePerson);
			WfReceiptUser wfReceiptUser = wfReceiptUserDao.selectByReceiptIdAndReceiptTypeAndUserId(map);
			if (wfReceiptUser == null) {
				wfReceiptUser = new WfReceiptUser();
				wfReceiptUser.setUserId(approvePerson);
				wfReceiptUser.setReceiptType(receiptType);
				wfReceiptUser.setReceiptId(receiptId);
				//wfReceiptUser.setTaskId(task.getId());
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_APPROVE.getValue());
				wfReceiptUser.setApproveTime(new Date());
				wfReceiptUserDao.insertSelective(wfReceiptUser);
			} else {
				//wfReceiptUser.setTaskId(task.getId());
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_APPROVE.getValue());
				wfReceiptUser.setApproveTime(new Date());
				wfReceiptUserDao.updateApproveMsg(wfReceiptUser);
			}

			ProcessInstance pi = null;//runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
					//.singleResult();

			// 这个默认审批不改状态
			if (pi == null) {
				return Const.RECEIPT_APPROVE_COMPLETE;
			} else {
				return Const.RECEIPT_APPROVE_INCOMPLETE;
			}
		}
	}

	/**
	 * 通过单据id和单据类型查找物料凭证 刘宇 2018.04.18
	 */
	@Override
	public List<Map<String, Object>> listBizMaterialDocHeadByRefereceiptIdAndMatDocType(int referReceiptId,
			int matDocType) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("referReceiptId", referReceiptId);
		param.put("matDocType", matDocType);
		return bizMaterialHeadDocDao.selectByRefereceiptIdAndMatDocType(param);
	}

	@Override
	public List<DicPackageType> listPackageTypeByMatId(Integer matId) throws Exception {

		return packageTypeDao.selectByMatId(matId);
	}

	/**
	 * 
	 * @param map
	 *            matId locationId batch status createUserId debitCredit (H减 S加)
	 *            qty isUrgent stockTypeId isWriteOff
	 * @throws Exception
	 */
	@Override
	public void modifyStockBatch(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("matId") && map.containsKey("locationId") && map.containsKey("batch")
				&& map.containsKey("status") && map.containsKey("createUserId") && map.containsKey("debitCredit")
				&& map.containsKey("qty"))) {
			throw new WMSException("更新批次库存参数异常");
		}

		Integer matId = UtilObject.getIntegerOrNull(map.get("matId"));
		Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
		Long batch = UtilObject.getLongOrNull(map.get("batch"));
		batch = batch == null ? 0L : batch;
		Byte status = UtilObject.getByteOrNull(map.get("status"));
		String createUserId = UtilObject.getStringOrNull(map.get("createUserId"));
		String debitCredit = UtilObject.getStringOrNull(map.get("debitCredit"));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		Byte isUrgent = UtilObject.getByteOrNull(map.get("isUrgent"));
		Byte stockTypeId = UtilObject.getByteOrNull(map.get("stockTypeId"));
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		// 查询批次库存信息
		StockBatchKey key = new StockBatchKey();
		key.setMatId(matId); // '物料编号',
		key.setLocationId(locationId);// '库存地点',
		key.setBatch(batch); // '批号',
		key.setStatus(status);
		if (map.containsKey("isUrgent")) {
			key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
			key.setIsUrgent(isUrgent);
		}
		// 库存类型
		key.setStockTypeId(stockTypeId);
		StockBatch stockBatch = stockBatchDao.selectByUniqueKey(key);
		if (stockBatch == null) {
			// 在该工厂-库存地点不存在该批次的物料
			stockBatch = new StockBatch();
			stockBatch.setIsUrgent(isUrgent);
			stockBatch.setMatId(matId); // '物料编号',
			stockBatch.setLocationId(locationId);// '库存地点',
			stockBatch.setBatch(batch); // '批号',
			stockBatch.setCreateUser(createUserId); // '创建人',

			// stockBatch.setSpecStock(bizMaterialDocItem.getSpecStock());//
			// 特殊库存标识
			// stockBatch.setSpecStockCode(bizMaterialDocItem.getSpecStockCode());//
			// 特殊库存代码
			// stockBatch.setSpecStockName(bizMaterialDocItem.getSpecStockName());//
			// 特殊库存描述
			stockBatch.setStatus(status);
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty.negate());// '非限制库存
				throw new WMSException("负库存异常");
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty);// '非限制库存
			} else {
				throw new SAPDebitCreditException();
			}

			stockBatch.setStockTypeId(stockTypeId);
			stockBatchDao.insertSelective(stockBatch);
		} else {
			StockBatch stockBatchTmp = new StockBatch();
			stockBatchTmp.setMatId(matId); // '物料编号',
			stockBatchTmp.setLocationId(locationId);// '库存地点',
			stockBatchTmp.setBatch(batch); // '批号',
			stockBatchTmp.setStatus(status);
			stockBatchTmp.setIsUrgent(isUrgent);
			stockBatchTmp.setStockTypeId(stockTypeId);
			if (map.containsKey("isUrgent")) {
				stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());

			}
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().subtract(qty));
				if(stockBatchTmp.getQty().compareTo(BigDecimal.ZERO)==-1){
					throw new WMSException("负库存异常");
				}
				
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().add(qty));
			} else {
				throw new SAPDebitCreditException();
			}
			if (BigDecimal.ZERO.compareTo(stockBatchTmp.getQty()) == 0) {
				stockBatchDao.deleteByByUniqueKey(key);
			} else {
				stockBatchDao.updateQty(stockBatchTmp);
			}
		}
		// // 修改冻结库存
		// if (!map.containsKey("isWriteOff") && "H".equals(debitCredit)) {
		// StockOccupancy stockOccupancy = new StockOccupancy();
		// stockOccupancy.setBatch(batch);
		// Map<Integer, DicStockLocationVo> loMap =
		// dictionaryService.getLocationIdMap();
		// DicStockLocationVo lo = loMap.get(locationId);
		// stockOccupancy.setFtyId(lo.getFtyId());
		// stockOccupancy.setLocationId(locationId);
		// stockOccupancy.setMatId(matId);
		// stockOccupancy.setQty(qty);
		// stockOccupancy.setStockTypeId(stockTypeId);
		// stockOccupancy.setIsUrgent(isUrgent);
		// this.modifyStockOccupancy(stockOccupancy, debitCredit);
		// }

	}

	@Override
	public void modifyStockBatchCanMinus(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("matId") && map.containsKey("locationId") && map.containsKey("batch")
				&& map.containsKey("status") && map.containsKey("createUserId") && map.containsKey("debitCredit")
				&& map.containsKey("qty"))) {
			throw new WMSException("更新批次库存参数异常");
		}

		Integer matId = UtilObject.getIntegerOrNull(map.get("matId"));
		Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
		Long batch = UtilObject.getLongOrNull(map.get("batch"));
		batch = batch == null ? 0L : batch;
		Byte status = UtilObject.getByteOrNull(map.get("status"));
		String createUserId = UtilObject.getStringOrNull(map.get("createUserId"));
		String debitCredit = UtilObject.getStringOrNull(map.get("debitCredit"));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		Byte isUrgent = UtilObject.getByteOrNull(map.get("isUrgent"));
		Byte stockTypeId = UtilObject.getByteOrNull(map.get("stockTypeId"));
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		// 查询批次库存信息
		StockBatchKey key = new StockBatchKey();
		key.setMatId(matId); // '物料编号',
		key.setLocationId(locationId);// '库存地点',
		key.setBatch(batch); // '批号',
		key.setStatus(status);
		if (map.containsKey("isUrgent")) {
			key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
			key.setIsUrgent(isUrgent);
		}
		// 库存类型
		key.setStockTypeId(stockTypeId);
		StockBatch stockBatch = stockBatchDao.selectByUniqueKey(key);
		if (stockBatch == null) {
			// 在该工厂-库存地点不存在该批次的物料
			stockBatch = new StockBatch();
			stockBatch.setIsUrgent(isUrgent);
			stockBatch.setMatId(matId); // '物料编号',
			stockBatch.setLocationId(locationId);// '库存地点',
			stockBatch.setBatch(batch); // '批号',
			stockBatch.setCreateUser(createUserId); // '创建人',

			
			stockBatch.setStatus(status);
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty.negate());// '非限制库存
				
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty);// '非限制库存
			} else {
				throw new SAPDebitCreditException();
			}

			stockBatch.setStockTypeId(stockTypeId);
			stockBatchDao.insertSelective(stockBatch);
		} else {
			StockBatch stockBatchTmp = new StockBatch();
			stockBatchTmp.setMatId(matId); // '物料编号',
			stockBatchTmp.setLocationId(locationId);// '库存地点',
			stockBatchTmp.setBatch(batch); // '批号',
			stockBatchTmp.setStatus(status);
			stockBatchTmp.setIsUrgent(isUrgent);
			stockBatchTmp.setStockTypeId(stockTypeId);
			if (map.containsKey("isUrgent")) {
				stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());

			}
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().subtract(qty));
				
				
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().add(qty));
			} else {
				throw new SAPDebitCreditException();
			}
			if (BigDecimal.ZERO.compareTo(stockBatchTmp.getQty()) == 0) {
				stockBatchDao.deleteByByUniqueKey(key);
			} else {
				stockBatchDao.updateQty(stockBatchTmp);
			}
		}
		

	}
	
	/**
	 * 
	 * @param map
	 *            matId locationId batch status createUserId debitCredit (H减 S加)
	 *            qty isUrgent stockTypeId isWriteOff
	 * @throws Exception
	 */
	@Override
	public void modifyStockBatchOnSyn(Map<String, Object> map) throws Exception {

		if (!(map.containsKey("matId") && map.containsKey("locationId") && map.containsKey("batch")
				&& map.containsKey("status") && map.containsKey("createUserId") && map.containsKey("debitCredit")
				&& map.containsKey("qty"))) {
			throw new WMSException("更新批次库存参数异常");
		}

		Integer matId = UtilObject.getIntegerOrNull(map.get("matId"));
		Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
		Long batch = UtilObject.getLongOrNull(map.get("batch"));
		batch = batch == null ? 0L : batch;
		Byte status = UtilObject.getByteOrNull(map.get("status"));
		String createUserId = UtilObject.getStringOrNull(map.get("createUserId"));
		String debitCredit = UtilObject.getStringOrNull(map.get("debitCredit"));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		Byte isUrgent = UtilObject.getByteOrNull(map.get("isUrgent"));
		Byte stockTypeId = UtilObject.getByteOrNull(map.get("stockTypeId"));
		if (stockTypeId == null) {
			stockTypeId = 1;
		}

		// 在该工厂-库存地点不存在该批次的物料
		StockBatch param = new StockBatch();
		param.setIsUrgent(isUrgent);
		param.setMatId(matId); // '物料编号',
		param.setLocationId(locationId);// '库存地点',
		param.setBatch(batch); // '批号',
		param.setCreateUser(createUserId); // '创建人',
		param.setStatus(status);
		param.setQty(qty);
		param.setStockTypeId(stockTypeId);

		StockBatch stockBatchTmp = new StockBatch();
		stockBatchTmp.setMatId(matId); // '物料编号',
		stockBatchTmp.setLocationId(locationId);// '库存地点',
		stockBatchTmp.setBatch(batch); // '批号',
		stockBatchTmp.setStatus(status);
		stockBatchTmp.setIsUrgent(isUrgent);
		stockBatchTmp.setStockTypeId(stockTypeId);

		// 查询批次库存信息
		StockBatchKey key = new StockBatchKey();
		key.setMatId(matId); // '物料编号',
		key.setLocationId(locationId);// '库存地点',
		key.setBatch(batch); // '批号',
		key.setStatus(status);
		if (map.containsKey("isUrgent")) {
			key.setIsUrgent(isUrgent);
		}
		// 库存类型
		key.setStockTypeId(stockTypeId);
		key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
		StockBatch stockBatch = stockBatchDao.selectByUniqueKey(key);
		if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(debitCredit)) {

			if (stockBatch == null) {
				key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
				StockBatch stockBatch1 = stockBatchDao.selectByUniqueKey(key);
				if (stockBatch1 == null) {
					// 插入仓位库存并取出新添加的仓位库存id
					param.setQty(param.getQty().negate());
					param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
					stockBatchDao.insertSelective(param);
				} else {
					stockBatch1.setQty(stockBatch1.getQty().subtract(param.getQty()));
					if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == 0) {
						// 紧急
						stockBatchDao.deleteByByUniqueKey(key);
					} else if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == 1) {

						stockBatchTmp.setQty(stockBatch1.getQty());
						stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
						stockBatchDao.updateQty(stockBatchTmp);
					} else if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == -1) {
						stockBatchTmp.setQty(stockBatch1.getQty());
						stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
						stockBatchDao.updateQty(stockBatchTmp);
					}
				}

			} else {
				stockBatch.setQty(stockBatch.getQty().subtract(param.getQty()));
				if (stockBatch.getQty().compareTo(BigDecimal.ZERO) == 0) {
					// 如果是0直接删除

					// 紧急
					stockBatchDao.deleteByByUniqueKey(key);

				} else if (stockBatch.getQty().compareTo(BigDecimal.ZERO) == 1) {
					stockBatchTmp.setQty(stockBatch.getQty());
					stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
					stockBatchDao.updateQty(stockBatchTmp);
				} else if (stockBatch.getQty().compareTo(BigDecimal.ZERO) == -1) {
					// 不够减 查询 状态 1的 扣

					key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
					StockBatch stockBatch1 = stockBatchDao.selectByUniqueKey(key);
					if (stockBatch1 == null) {

						stockBatchTmp.setQty(stockBatch.getQty());
						stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
						stockBatchDao.updateQty(stockBatchTmp);

					} else {
						stockBatch1.setQty(stockBatch1.getQty().add(stockBatch.getQty()));
						// 删除 状态2
						key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
						stockBatchDao.deleteByByUniqueKey(key);

						if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == 0) {
							key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
							stockBatchDao.deleteByByUniqueKey(key);
						} else if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == 1) {
							stockBatchTmp.setQty(stockBatch1.getQty());
							stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
							stockBatchDao.updateQty(stockBatchTmp);
						} else if (stockBatch1.getQty().compareTo(BigDecimal.ZERO) == -1) {
							stockBatchTmp.setQty(stockBatch1.getQty());
							stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
							stockBatchDao.updateQty(stockBatchTmp);
						}

					}
				}
			}
		} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(debitCredit)) {
			if (stockBatch == null) {
				param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
				stockBatchDao.insertSelective(param);
			} else {
				stockBatchTmp.setQty(stockBatch.getQty().add(param.getQty()));
				stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
				if (stockBatchTmp.getQty().compareTo(BigDecimal.ZERO) == 0) {
					stockBatchDao.deleteByByUniqueKey(key);
				} else {
					stockBatchDao.updateQty(stockBatchTmp);
				}

			}

		} else {
			throw new SAPDebitCreditException();
		}

	}

	@Override
	public boolean modifyStockPosition(StockPosition param) throws Exception {

		boolean ret = false;
		Byte stockTypeId = param.getStockTypeId();
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		StockPosition stockPosition;
		if (param.getId() != null && param.getId() > 0) {
			if (param.getIsUrgent() != null) {
				// 紧急查询
				stockPosition = stockPositionDao.selectByPrimaryKeyOnUrgent(param.getId());
			} else {
				stockPosition = stockPositionDao.selectByPrimaryKey(param.getId());
			}

		} else {
			// 根据唯一键查询仓位库存是否存在
			StockPositionKey key = new StockPositionKey();
			key.setPositionId(param.getPositionId());// 仓位
			// key.setPalletId(param.getPalletId());// 托盘
			key.setPackageId(param.getPackageId());// 包
			key.setMatId(param.getMatId()); // 物料id,
			key.setLocationId(param.getLocationId());// 库存地点id
			key.setBatch(param.getBatch()); // 批号id
			key.setStatus(param.getStatus());
			// 紧急查询
			key.setIsUrgent(param.getIsUrgent());
			if (param.getIsUrgent() != null) {
				key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
			}
			key.setStockTypeId(stockTypeId);
			stockPosition = stockPositionDao.selectByUniqueKey(key);
		}
		// 存储仓位库存id,以备sn库存使用
		int stockId;
		if (stockPosition == null) {
			// 如果不存在,直接插入
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty().negate());
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty());
			} else {
				throw new SAPDebitCreditException();
			}

			// 插入仓位库存并取出新添加的仓位库存id
			stockPositionDao.insertSelective(param);
			stockId = param.getId();

		} else {
			// 如果存在,直接取出仓位库存id
			stockId = stockPosition.getId();

			// 创建个临时变量,设置id和修改后数量
			StockPosition stockPositionTmp = new StockPosition();
			stockPositionTmp.setId(stockId);
			stockPositionTmp.setIsUrgent(param.getIsUrgent());
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 仓位库存历史
				StockPositionHistory history = new StockPositionHistory(stockPosition);
				history.setQty(param.getQty());

				stockPositionHistoryDao.insertSelective(history);

				// 贷 H 减
				stockPositionTmp.setQty(stockPosition.getQty().subtract(param.getQty()));
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 借 S 加
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
			} else {
				throw new SAPDebitCreditException();
			}

			if (BigDecimal.ZERO.compareTo(stockPositionTmp.getQty()) == 0) {
				// 如果是0直接删除
				if (param.getIsUrgent() != null) {
					// 紧急
					stockPositionDao.deleteByPrimaryKeyUrgent(stockId);
				} else {
					stockPositionDao.deleteByPrimaryKey(stockId);
				}

			} else {
				// 如果不是0,直接修改,修改的数量是已经经过计算的
				stockPositionDao.updateByStockQty(stockPositionTmp);

			}
		}

		return ret;

	}

	@Override
	public boolean modifyStockPositionNotNull(StockPosition param) throws Exception {

		boolean ret = false;
		Byte stockTypeId = param.getStockTypeId();
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		StockPosition stockPosition;

		// 根据唯一键查询仓位库存是否存在
		StockPositionKey key = new StockPositionKey();
		key.setPositionId(param.getPositionId());// 仓位
		// key.setPalletId(param.getPalletId());// 托盘
		key.setPackageId(param.getPackageId());// 包
		key.setMatId(param.getMatId()); // 物料id,
		key.setLocationId(param.getLocationId());// 库存地点id
		key.setBatch(param.getBatch()); // 批号id
		key.setStatus(param.getStatus());
		// 紧急查询
		key.setIsUrgent(param.getIsUrgent());
		if (param.getIsUrgent() != null) {
			key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
		}
		key.setStockTypeId(stockTypeId);
		stockPosition = stockPositionDao.selectByUniqueKey(key);

		DicWarehousePosition positionObj = warehousePositionDap.selectByPrimaryKey(param.getPositionId());
		Byte isDefault = positionObj.getIsDefault();
		// 存储仓位库存id,以备sn库存使用
		int stockId;
		if (stockPosition == null) {
			// 如果不存在,直接插入
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty().negate());
				if(isDefault==0){
					throw new WMSException("库存不足");
				}
				
			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				param.setQty(param.getQty());
			} else {
				throw new SAPDebitCreditException();
			}

			// 插入仓位库存并取出新添加的仓位库存id
			stockPositionDao.insertSelective(param);
			stockId = param.getId();

		} else {
			// 如果存在,直接取出仓位库存id
			stockId = stockPosition.getId();

			// 创建个临时变量,设置id和修改后数量
			StockPosition stockPositionTmp = new StockPosition();
			stockPositionTmp.setId(stockId);
			stockPositionTmp.setIsUrgent(param.getIsUrgent());
			if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 仓位库存历史
				StockPositionHistory history = new StockPositionHistory(stockPosition);
				history.setQty(param.getQty());

				stockPositionHistoryDao.insertSelective(history);

				// 贷 H 减
				stockPositionTmp.setQty(stockPosition.getQty().subtract(param.getQty()));

			} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
				// 借 S 加
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
			} else {
				throw new SAPDebitCreditException();
			}

			if (BigDecimal.ZERO.compareTo(stockPositionTmp.getQty()) == 0) {
				// 如果是0直接删除
				if (param.getIsUrgent() != null) {
					// 紧急
					stockPositionDao.deleteByPrimaryKeyUrgent(stockId);
				} else {
					stockPositionDao.deleteByPrimaryKey(stockId);
				}

			} else if (BigDecimal.ZERO.compareTo(stockPositionTmp.getQty()) > 0) {
				if(isDefault==0){
					throw new WMSException("库存不足");
				}
				stockPositionDao.updateByStockQty(stockPositionTmp);
			} else {
				// 如果不是0,直接修改,修改的数量是已经经过计算的
				stockPositionDao.updateByStockQty(stockPositionTmp);

			}
		}

		return ret;

	}

	@Override
	public boolean modifyStockPositionOnSyn(StockPosition param) throws Exception {

		boolean ret = false;
		Byte stockTypeId = param.getStockTypeId();
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		StockPosition stockPosition;

		// 根据唯一键查询仓位库存是否存在
		StockPositionKey key = new StockPositionKey();
		key.setPositionId(param.getPositionId());// 仓位
		// key.setPalletId(param.getPalletId());// 托盘
		key.setPackageId(param.getPackageId());// 包
		key.setMatId(param.getMatId()); // 物料id,
		key.setLocationId(param.getLocationId());// 库存地点id
		key.setBatch(param.getBatch()); // 批号id
		key.setStatus(param.getStatus());
		// 紧急查询
		key.setIsUrgent(param.getIsUrgent());
		key.setStockTypeId(stockTypeId);
		key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
		stockPosition = stockPositionDao.selectByUniqueKey(key);
		if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {

			if (stockPosition == null) {
				key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
				StockPosition stockPosition1 = stockPositionDao.selectByUniqueKey(key);
				if (stockPosition1 == null) {
					// 插入仓位库存并取出新添加的仓位库存id
					param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
					param.setQty(param.getQty().negate());
					stockPositionDao.insertSelective(param);
				} else {
					stockPosition1.setQty(stockPosition1.getQty().subtract(param.getQty()));
					if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 0) {
						// 紧急
						stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition1.getId());
					} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 1) {
						StockPosition stockPositionTmp = new StockPosition();
						stockPositionTmp.setId(stockPosition1.getId());
						stockPositionTmp.setIsUrgent(param.getIsUrgent());
						stockPositionTmp.setQty(stockPosition1.getQty());

						stockPositionDao.updateByStockQty(stockPositionTmp);
					} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == -1) {
						StockPosition stockPositionTmp = new StockPosition();
						stockPositionTmp.setId(stockPosition1.getId());
						stockPositionTmp.setIsUrgent(param.getIsUrgent());
						stockPositionTmp.setQty(stockPosition1.getQty());
						stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
						stockPositionDao.updateByStockQty(stockPositionTmp);
					}
				}

			} else {
				stockPosition.setQty(stockPosition.getQty().subtract(param.getQty()));
				if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == 0) {
					// 如果是0直接删除

					// 紧急
					stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());

				} else if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == 1) {
					StockPosition stockPositionTmp = new StockPosition();
					stockPositionTmp.setId(stockPosition.getId());
					stockPositionTmp.setIsUrgent(param.getIsUrgent());
					stockPositionTmp.setQty(stockPosition.getQty());
					stockPositionDao.updateByStockQty(stockPositionTmp);
				} else if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == -1) {
					// 不够减 查询 状态 1的 扣

					key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
					StockPosition stockPosition1 = stockPositionDao.selectByUniqueKey(key);
					if (stockPosition1 == null) {
						// 插入负值
						StockPosition stockPositionTmp = new StockPosition();
						stockPositionTmp.setId(stockPosition.getId());
						stockPositionTmp.setIsUrgent(param.getIsUrgent());
						stockPositionTmp.setQty(stockPosition.getQty());
						stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
						stockPositionDao.updateByStockQty(stockPositionTmp);
					} else {
						stockPosition1.setQty(stockPosition1.getQty().add(stockPosition.getQty()));
						// 删除 状态2
						stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());

						if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 0) {
							stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition1.getId());
						} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 1) {
							StockPosition stockPositionTmp = new StockPosition();
							stockPositionTmp.setId(stockPosition1.getId());
							stockPositionTmp.setIsUrgent(param.getIsUrgent());
							stockPositionTmp.setQty(stockPosition1.getQty());

							stockPositionDao.updateByStockQty(stockPositionTmp);
						} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == -1) {
							StockPosition stockPositionTmp = new StockPosition();
							stockPositionTmp.setId(stockPosition1.getId());
							stockPositionTmp.setIsUrgent(param.getIsUrgent());
							stockPositionTmp.setQty(stockPosition1.getQty());
							stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
							stockPositionDao.updateByStockQty(stockPositionTmp);
						}

					}
				}
			}
		} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
			if (stockPosition == null) {
				param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
				stockPositionDao.insertSelective(param);
			} else {
				StockPosition stockPositionTmp = new StockPosition();
				stockPositionTmp.setId(stockPosition.getId());
				stockPositionTmp.setIsUrgent(param.getIsUrgent());
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
				if (stockPositionTmp.getQty().compareTo(BigDecimal.ZERO) == 0) {
					stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());
				} else {
					stockPositionDao.updateByStockQty(stockPositionTmp);
				}

			}

		} else {
			throw new SAPDebitCreditException();
		}

		return ret;

	}
	
	@Override
	public boolean modifyStockPositionOnSynByDelete(StockPosition param) throws Exception {

		boolean ret = false;
		Byte stockTypeId = param.getStockTypeId();
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		StockPosition stockPosition;

		// 根据唯一键查询仓位库存是否存在
		StockPositionKey key = new StockPositionKey();
		key.setPositionId(param.getPositionId());// 仓位
		// key.setPalletId(param.getPalletId());// 托盘
		key.setPackageId(param.getPackageId());// 包
		key.setMatId(param.getMatId()); // 物料id,
		key.setLocationId(param.getLocationId());// 库存地点id
		key.setBatch(param.getBatch()); // 批号id
		key.setStatus(param.getStatus());
		// 紧急查询
		key.setIsUrgent(param.getIsUrgent());
		key.setStockTypeId(stockTypeId);
		key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_FINISH.getValue());
		stockPosition = stockPositionDao.selectByUniqueKey(key);
		if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
//
//			if (stockPosition == null) {
//				key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
//				StockPosition stockPosition1 = stockPositionDao.selectByUniqueKey(key);
//				if (stockPosition1 == null) {
//					throw new WMSException("负库存异常");
//					// 插入仓位库存并取出新添加的仓位库存id
////					param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
////					param.setQty(param.getQty().negate());
////					stockPositionDao.insertSelective(param);
//				} else {
//					stockPosition1.setQty(stockPosition1.getQty().subtract(param.getQty()));
//					if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 0) {
//						// 紧急
//						stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition1.getId());
//					} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 1) {
//						StockPosition stockPositionTmp = new StockPosition();
//						stockPositionTmp.setId(stockPosition1.getId());
//						stockPositionTmp.setIsUrgent(param.getIsUrgent());
//						stockPositionTmp.setQty(stockPosition1.getQty());
//
//						stockPositionDao.updateByStockQty(stockPositionTmp);
//					} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == -1) {
////						StockPosition stockPositionTmp = new StockPosition();
////						stockPositionTmp.setId(stockPosition1.getId());
////						stockPositionTmp.setIsUrgent(param.getIsUrgent());
////						stockPositionTmp.setQty(stockPosition1.getQty());
////						stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
////						stockPositionDao.updateByStockQty(stockPositionTmp);
//						throw new WMSException("负库存异常");
//					}
//				}
//
//			} else {
//				stockPosition.setQty(stockPosition.getQty().subtract(param.getQty()));
//				if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == 0) {
//					// 如果是0直接删除
//
//					// 紧急
//					stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());
//
//				} else if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == 1) {
//					StockPosition stockPositionTmp = new StockPosition();
//					stockPositionTmp.setId(stockPosition.getId());
//					stockPositionTmp.setIsUrgent(param.getIsUrgent());
//					stockPositionTmp.setQty(stockPosition.getQty());
//					stockPositionDao.updateByStockQty(stockPositionTmp);
//				} else if (stockPosition.getQty().compareTo(BigDecimal.ZERO) == -1) {
//					// 不够减 查询 状态 1的 扣
//
//					key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
//					StockPosition stockPosition1 = stockPositionDao.selectByUniqueKey(key);
//					if (stockPosition1 == null) {
////						// 插入负值
////						StockPosition stockPositionTmp = new StockPosition();
////						stockPositionTmp.setId(stockPosition.getId());
////						stockPositionTmp.setIsUrgent(param.getIsUrgent());
////						stockPositionTmp.setQty(stockPosition.getQty());
////						stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
////						stockPositionDao.updateByStockQty(stockPositionTmp);
//						throw new WMSException("负库存异常");
//					} else {
//						stockPosition1.setQty(stockPosition1.getQty().add(stockPosition.getQty()));
//						// 删除 状态2
//						stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());
//
//						if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 0) {
//							stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition1.getId());
//						} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == 1) {
//							StockPosition stockPositionTmp = new StockPosition();
//							stockPositionTmp.setId(stockPosition1.getId());
//							stockPositionTmp.setIsUrgent(param.getIsUrgent());
//							stockPositionTmp.setQty(stockPosition1.getQty());
//
//							stockPositionDao.updateByStockQty(stockPositionTmp);
//						} else if (stockPosition1.getQty().compareTo(BigDecimal.ZERO) == -1) {
//							StockPosition stockPositionTmp = new StockPosition();
//							stockPositionTmp.setId(stockPosition1.getId());
//							stockPositionTmp.setIsUrgent(param.getIsUrgent());
//							stockPositionTmp.setQty(stockPosition1.getQty());
//							stockPositionTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
//							stockPositionDao.updateByStockQty(stockPositionTmp);
//						}
//
//					}
//				}
//			}
//		
			throw new WMSException("库存异常");
		} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(param.getDebitOrCredit())) {
			if (stockPosition == null) {
				param.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
				stockPositionDao.insertSelective(param);
			
			} else {
				StockPosition stockPositionTmp = new StockPosition();
				stockPositionTmp.setId(stockPosition.getId());
				stockPositionTmp.setIsUrgent(param.getIsUrgent());
				stockPositionTmp.setQty(stockPosition.getQty().add(param.getQty()));
				if (stockPositionTmp.getQty().compareTo(BigDecimal.ZERO) == 0) {
					stockPositionDao.deleteByPrimaryKeyUrgent(stockPosition.getId());
				} else {
					stockPositionDao.updateByStockQty(stockPositionTmp);
				}

			}

		} else {
			throw new SAPDebitCreditException();
		}

		return ret;

	}

	@Override
	public void updateStockPositionByTask(List<BizStockTaskItemCw> taskItemList,
			List<BizStockTaskPositionCw> positionList, Byte stockTypeId, boolean isUrgent) throws Exception {

		if (positionList != null && positionList.size() > 0) {

			Map<String, BizStockTaskItemCw> taskItemMap = new HashMap<String, BizStockTaskItemCw>();
			if (taskItemList != null && taskItemList.size() > 0) {
				for (BizStockTaskItemCw item : taskItemList) {
					String key = item.getStockTaskId() + "-" + item.getStockTaskRid();
					taskItemMap.put(key, item);
				}
			} else {
				throw new WMSException("缺少行项目");
			}

			for (BizStockTaskPositionCw pItem : positionList) {
				// 原仓位

				String key = pItem.getStockTaskId() + "-" + pItem.getStockTaskRid();

				BizStockTaskItemCw stockTaskItem = taskItemMap.get(key);
				Byte receiptType = stockTaskItem.getReceiptType();
				StockPosition stockPosition = new StockPosition();

				stockPosition.setWhId(pItem.getWhId());
				// 上架 组盘上架
				if (receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())
						|| receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
					stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
					stockPosition.setAreaId(pItem.getSourceAreaId());
					stockPosition.setPositionId(pItem.getSourcePositionId());
				} else if (receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
					// 下架
					stockPosition.setAreaId(pItem.getTargetAreaId());
					stockPosition.setPositionId(pItem.getTargetPositionId());
					stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				} else {
					throw new WMSException("单据类型出错");
				}

				stockPosition.setMatId(stockTaskItem.getMatId());
				stockPosition.setFtyId(stockTaskItem.getFtyId());
				stockPosition.setLocationId(stockTaskItem.getLocationId());
				stockPosition.setBatch(stockTaskItem.getBatch());

				stockPosition.setInputDate(new Date());
				stockPosition.setQty(pItem.getQty());
				stockPosition.setUnitId(stockTaskItem.getUnitId());
				stockPosition.setInputDate(new Date());
				if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
					// 启用包
					if (pItem.getPackageId() == null) {
						throw new WMSException("包装类型出错");
					}
					stockPosition.setPackageId(pItem.getPackageId());
					if (pItem.getPalletId() == null) {
						stockPosition.setPalletId(0);
					} else {
						stockPosition.setPalletId(pItem.getPalletId());
					}

				} else {
					// 不启用包
					stockPosition.setPackageId(0);
					stockPosition.setPalletId(0);
				}
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPosition.setStockTypeId(stockTypeId);
				if (isUrgent) {
					stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				}
				this.modifyStockPosition(stockPosition);

			}

		} else {
			throw new WMSException("缺少数据");
		}

	}

	@Override
	public void updateStockPositionByTaskReq(Byte receiptType, List<BizStockTaskReqItem> taskReqItemList,
			List<BizStockTaskPositionCw> positionList, Byte stockTypeId, boolean isUrgent) throws Exception {

		if (positionList != null && positionList.size() > 0) {

			Map<String, BizStockTaskReqItem> taskItemMap = new HashMap<String, BizStockTaskReqItem>();
			if (taskReqItemList != null && taskReqItemList.size() > 0) {
				for (BizStockTaskReqItem item : taskReqItemList) {
					String key = item.getReferReceiptId() + "-" + item.getReferReceiptRid() + "-"
							+ item.getReferReceiptType();
					taskItemMap.put(key, item);
				}
			} else {
				throw new WMSException("缺少行项目");
			}

			for (BizStockTaskPositionCw pItem : positionList) {
				// 原仓位

				String key = pItem.getReferReceiptId() + "-" + pItem.getReferReceiptRid() + "-"
						+ pItem.getReferReceiptType();

				BizStockTaskReqItem stockTaskItem = taskItemMap.get(key);

				StockPosition stockPosition = new StockPosition();

				stockPosition.setWhId(pItem.getWhId());
				// 上架 组盘上架
				if (receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())
						|| receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
					stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
					stockPosition.setAreaId(pItem.getTargetAreaId());
					stockPosition.setPositionId(pItem.getTargetPositionId());
				} else if (receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
					// 下架
					stockPosition.setAreaId(pItem.getSourceAreaId());
					stockPosition.setPositionId(pItem.getSourcePositionId());
					stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
				} else {
					throw new WMSException("单据类型出错");
				}

				stockPosition.setMatId(stockTaskItem.getMatId());
				stockPosition.setFtyId(stockTaskItem.getFtyId());
				stockPosition.setLocationId(stockTaskItem.getLocationId());
				stockPosition.setBatch(stockTaskItem.getBatch());

				stockPosition.setInputDate(new Date());
				stockPosition.setQty(pItem.getQty());
				stockPosition.setUnitId(stockTaskItem.getUnitId());
				stockPosition.setInputDate(new Date());
				if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
					// 启用包
					if (pItem.getPackageId() == null) {
						throw new WMSException("包装类型出错");
					}
					stockPosition.setPackageId(pItem.getPackageId());
					if (pItem.getPalletId() == null) {
						stockPosition.setPalletId(0);
					} else {
						stockPosition.setPalletId(pItem.getPalletId());
					}

				} else {
					// 不启用包
					stockPosition.setPackageId(0);
					stockPosition.setPalletId(0);
				}
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPosition.setStockTypeId(stockTypeId);
				if (isUrgent) {
					stockPosition.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				}

				this.modifyStockPosition(stockPosition);

			}

		} else {
			throw new WMSException("缺少数据");
		}

	}

	@Override
	public DicWarehousePallet newPallet(String userId) throws Exception {
		DicWarehousePallet pallet = new DicWarehousePallet();
		String palletCode = this.getNextReceiptCode("warehouse_pallet");
		while (palletCode.length() < 5) { // 不够五位数字，自动补0
			palletCode = "0" + palletCode;
		}
		String date = DateUtil.formatDate(new Date(), "yyMMdd");
		pallet.setPalletCode("T" + date + palletCode);
		pallet.setMaxWeight(Const.PALLET_MAX_WEIGHT);
		warehousePalletDao.insertPalletSelective(pallet);
		//以下是发送打印请求
		try {
			JSONObject json = new JSONObject();
			DicPrinter printer = printerDao.getPrinterByUserAndType(userId,EnumPrinterType.LABEL_PRINTER.getValue());
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("type", "L1");
			map.put("content", pallet.getPalletCode());
			map.put("print_name", printer.getPrinterName());
			data.add(map);
			json.put("print_data", data);
			String url = UtilProperties.getInstance().getPrintUrl() + "/wms/print/cwLabelZpl";
			UtilREST.executePostJSONTimeOut(url, json, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pallet;
	}

	@Override
	public List<Map<String, Object>> getClassTypeList() throws Exception {

		return classTypeDao.selectAll();
	}

	@Override
	public List<Map<String, Object>> getStockGroupByCondition(Map<String, Object> map) throws Exception {

		return stockPositionDao.selectGroupByFourKeys(map);
	}

	@Override
	public List<Map<String, Object>> getMatListByBatch(Map<String, Object> map) throws Exception {

		return stockBatchDao.selectMatListByBatch(map);
	}

	@Override
	public List<Map<String, Object>> getMatListByPosition(Map<String, Object> map) throws Exception {

		return stockBatchDao.selectMatListByPosition(map);
	}

	@Override
	public Map<String, Object> selectProduceOrderInfo(String purchase_order_code) {

		Connection conn = null;

		String driver = "oracle.jdbc.driver.OracleDriver"; // 驱动

		String url = "jdbc:oracle:thin:@//10.122.1.175:1521/cwedw"; // 连接字符串

		String username = "CWDBA"; // 用户名

		String password = "cwedw"; // 密码
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
		if (conn == null) {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);

				// conn.setRequestProperty("accept", "*/*");
				//
				// conn.setRequestProperty("connection", "Keep-Alive");
				// conn.setRequestProperty("user-agent", "Mozilla/4.0
				// (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

				PreparedStatement pstmt;
				// List<Map<String,Object>> records = new
				// ArrayList<Map<String,Object>>();
				while (purchase_order_code.length() < 12) { // 不够三位数字，自动补0
					purchase_order_code = "0" + purchase_order_code;
				}
				pstmt = conn.prepareStatement("SELECT\r\n" + "	AUFK.AUFNR,\r\n" + "	AUFK.AUART,\r\n"
						+ "	AUFK.KTEXT,\r\n" + "	AUFK.WERKS,\r\n" + "	AUFK.BUKRS,\r\n" + "	AFKO.AUFNR,\r\n"
						+ "	AFKO.GAMNG,\r\n" + "	AFKO.GMEIN,\r\n" + "	AFKO.PLNBEZ,\r\n" + "	AFKO.PLGRP,\r\n"
						+ "	AFKO.DISPO,\r\n" + "	AFKO.FEVOR\r\n" + "FROM\r\n" + "	AUFK,\r\n" + "	AFKO\r\n"
						+ "WHERE\r\n" + "	AUFK.AUFNR = AFKO.AUFNR\r\n" + "and AUFK.AUFNR='" + purchase_order_code
						+ "'");
				// 建立一个结果集，用来保存查询出来的结果
				ResultSet rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int fieldCount = rsmd.getColumnCount();

				while (rs.next()) {
					for (int i = 1; i <= fieldCount; i++) {
						String fieldName = rsmd.getColumnName(i);
						if(fieldName.equals("PLNBEZ")) {
							String dbMatCode = UtilObject.getStringOrEmpty(rs.getObject(i)).trim();
							if(dbMatCode.substring(0, 2).equals("00")) {
								dbMatCode = dbMatCode.substring(2,dbMatCode.length());
							}
							valueMap.put(fieldName, dbMatCode);
						}else{
							valueMap.put(fieldName, UtilObject.getStringOrEmpty(rs.getObject(i)).trim());
						}
					}
				}
				rs.close();
				pstmt.close();
				conn.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valueMap;
	}
	
	@Override
	public List<Map<String, Object>> selectProduceOrderInfo2(String purchase_order_code) {

		Connection conn = null;

		String driver = "oracle.jdbc.driver.OracleDriver"; // 驱动

		String url = "jdbc:oracle:thin:@//10.122.1.175:1521/cwedw"; // 连接字符串

		String username = "CWDBA"; // 用户名

		String password = "cwedw"; // 密码
		List<Map<String, Object>> valueMap = new ArrayList<Map<String, Object>>();
		if (conn == null) {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);

				// conn.setRequestProperty("accept", "*/*");
				//
				// conn.setRequestProperty("connection", "Keep-Alive");
				// conn.setRequestProperty("user-agent", "Mozilla/4.0
				// (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

				PreparedStatement pstmt;
				// List<Map<String,Object>> records = new
				// ArrayList<Map<String,Object>>();
				while (purchase_order_code.length() < 12) { // 不够三位数字，自动补0
					purchase_order_code = "0" + purchase_order_code;
				}
				pstmt = conn.prepareStatement("SELECT * FROM RESB WHERE KZKUP='X' AND AUFNR like'"+"%"+purchase_order_code+"%"+"'");
				// 建立一个结果集，用来保存查询出来的结果
				ResultSet rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int fieldCount = rsmd.getColumnCount();

				while (rs.next()) {
					Map<String, Object> map=new HashMap<String, Object>();
					for (int i = 1; i <= fieldCount; i++) {
						String fieldName = rsmd.getColumnName(i);	
						if(fieldName.equals("MATNR")) {
							String dbMatCode = UtilObject.getStringOrEmpty(rs.getObject(i)).trim();
							if(dbMatCode.substring(0, 2).equals("00")) {
								dbMatCode = dbMatCode.substring(2,dbMatCode.length());
							}
							map.put(fieldName, dbMatCode);
						}else {
							map.put(fieldName, UtilObject.getStringOrEmpty(rs.getObject(i)).trim());
						}							
					}
					valueMap.add(map);
				}
				rs.close();
				pstmt.close();
				conn.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return valueMap;
	}
	
	

	@Override
	public Integer selectNowClassType() {
		Integer classTypeId = this.bizStockInputHeadDao.selectNowClassType();
		if (classTypeId == null) {
			classTypeId = 0;
		}
		return classTypeId;
	}

	@Override
	public List<DicProductLine> selectProductLineListAndDicInstallationList(String userId) {
		return this.dicProductLineDao.selectDicProductLineList(userId);
	}

	@Override
	public List<Map<String, Object>> getTaskMatListByPosition(Map<String, Object> map) throws Exception {
		return stockBatchDao.selectTaskMatListByPositionParam(map);
	}

	@Override
	public void modifyStockOccupancy(StockOccupancy stockOccupancy, String type) throws WMSException {
		if (stockOccupancy == null) {
			throw new WMSException("缺少数据");
		} else {
			BigDecimal qty = stockOccupancy.getQty();

			StockOccupancy stockOccupancyTmp = stockOccupancyDao.selectByUniqueKey(stockOccupancy);

			if (stockOccupancyTmp == null) {
				if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equals(type)) {
					stockOccupancy.setQty(stockOccupancy.getQty().negate());
				}
				stockOccupancyDao.insertSelective(stockOccupancy);
			} else {
				stockOccupancyTmp.setIsUrgent(stockOccupancy.getIsUrgent());
				if (EnumDebitCredit.CREDIT_H_SUBTRACT.getName().equals(type)) {
					stockOccupancyTmp.setQty(stockOccupancyTmp.getQty().subtract(qty));
				} else if (EnumDebitCredit.DEBIT_S_ADD.getName().equalsIgnoreCase(type)) {
					stockOccupancyTmp.setQty(stockOccupancyTmp.getQty().add(qty));
				} else {
					throw new WMSException("缺少数据");
				}
				if (BigDecimal.ZERO.compareTo(stockOccupancyTmp.getQty()) == 0) {
					stockOccupancyDao.deleteByByUniqueKey(stockOccupancyTmp);
				} else {
					stockOccupancyDao.updateQty(stockOccupancyTmp);
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> getErpBatchList(Integer matId, Integer ftyId) {
		return relMatErpbatchDao.selectErpBatchListByMatId(matId, ftyId);
	}

	@Override
	public List<Map<String, Object>> getDataFromOracle(String sqlStr) throws Exception {

		Connection conn = UtilOracleConnection.getConnection();
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		if (conn != null) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {

				pstmt = conn.prepareStatement(sqlStr);

				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int fieldCount = rsmd.getColumnCount();

				while (rs.next()) {
					Map<String, Object> valueMap = new LinkedHashMap<String, Object>();
					for (int i = 1; i <= fieldCount; i++) {
						String fieldName = rsmd.getColumnName(i);
						valueMap.put(fieldName, rs.getObject(i));
					}
					returnList.add(valueMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		}
		return returnList;

	}

	@Override
	public void insertStockBatchBegin() throws Exception {
		List<StockBatch> stockBatchs = this.stockBatchDao.selectBatchListToBegin();
		for (StockBatch batch : stockBatchs) {
			StockBatchBegin begin = new StockBatchBegin();
			begin.setCreateTime(new Date());
			begin.setLocationId(batch.getLocationId());
			begin.setMatId(batch.getMatId());
			begin.setQty(batch.getQty());
			begin.setStatus(batch.getStatus());
			begin.setErpBatch(batch.getErpBatch());
			begin.setStockTypeId(batch.getStockTypeId());
			this.stockBatchBeginDao.insertSelective(begin);
		}
	}

	@Override
	public JSONArray getMatPriceFromOracle(List<String> matCodeList, String ftyCode) throws Exception {
		JSONArray returnAry = new JSONArray();
		DicFactoryVo ftyVo = dictionaryService.getFtyCodeMap().get(ftyCode);
		String BWTAR = "ZZCP";
		if(UtilObject.getByteOrNull(ftyVo.getFtyType()).equals(EnumFtyType.PRODUCT.getValue())){
			BWTAR = "ZZSC";
		}
		if (matCodeList != null && matCodeList.size() > 0) {

			StringBuffer sqlbuf = new StringBuffer(
					"select SUBSTR(MATNR, 3, 18) mat_code,STPRS/PEINH refer_price from MBEW where MATNR in (");
			for (String matCode : matCodeList) {
				int strLength = matCode.length();

				if (strLength < 18) {
					while (strLength < 18) {
						StringBuffer sb = new StringBuffer();
						sb.append("0").append(matCode);
						matCode = sb.toString();
						strLength = matCode.length();
					}
				}

				sqlbuf.append("'").append(matCode).append("',");
			}
			sqlbuf.deleteCharAt(sqlbuf.length() - 1);
			sqlbuf.append(")");
			sqlbuf.append(" and BWKEY = '").append(ftyCode).append("'");
			sqlbuf.append(" and BWTAR = '").append(BWTAR).append("'");
			sqlbuf.append(" and VPRSV = 'S'");
			List<Map<String, Object>> priceList = getDataFromOracle(sqlbuf.toString());
			returnAry = JSONArray.fromObject(priceList);
		}

		return returnAry;
	}

	@Override
	public List<DicSupplier> synSupplier(String keyword) throws Exception {
		List<DicSupplier> result = new ArrayList<DicSupplier>();
		String sqlStr = "select LIFNR,NAME1,ORT01 from LFA1 where ";
		if (StringUtils.hasText(keyword)) {
			sqlStr = sqlStr + "( LIFNR LIKE '%" + keyword.trim() + "%'";
			sqlStr = sqlStr + " OR";
			sqlStr = sqlStr + " NAME1 LIKE '%" + keyword.trim() + "%' )";
		} else {
			String dateStr = UtilTimestamp.formartDate(new Date(), "yyyyMMdd");
			sqlStr = sqlStr + "ERDAT = '" + dateStr + "'";
		}
		List<Map<String, Object>> spMapList = this.getDataFromOracle(sqlStr);
		if (spMapList != null && spMapList.size() > 0) {
			for (Map<String, Object> map : spMapList) {
				DicSupplier sp = new DicSupplier();
				sp.setSupplierCode(UtilObject.getStringOrEmpty(map.get("LIFNR")));
				sp.setSupplierName(UtilObject.getStringOrEmpty(map.get("NAME1")));
				String cityName = UtilObject.getStringOrEmpty(map.get("ORT01"));
				sp.setCityName(cityName);
				result.add(sp);
			}
		}
		return result;
	}

	@Override
	public int synErpBatch(List<String> matCodeList) throws Exception {

		int count = 0;
		String sqlStr = "select SUBSTR(MATNR, 3, 18) MATNR, CHARG ,WERKS from MCHA where ";
		if (matCodeList != null && matCodeList.size() > 0) {
			sqlStr = sqlStr + "MATNR IN (";
			for (String matCode : matCodeList) {
				sqlStr += "'" + "00" + matCode + "',";
			}
			sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
			sqlStr = sqlStr + ")";
		} else {
			String dateStr = UtilTimestamp.formartDate(new Date(), "yyyyMMdd");
			sqlStr = sqlStr + "ERSDA = '" + dateStr + "'";
		}
		List<Map<String, Object>> erpData = this.getDataFromOracle(sqlStr);
		Map<String, Integer> map = dictionaryService.getMatMapByCodeList(matCodeList);
		for (Map<String, Object> innerMap : erpData) {
			Integer matId = map.get(innerMap.get("MATNR"));
			String erpBatch = UtilObject.getStringOrEmpty(innerMap.get("CHARG"));
			innerMap.put("matId", matId);
			innerMap.put("erpBatch", erpBatch);
			Integer ftyId = dictionaryService.getFtyIdByFtyCode(UtilObject.getStringOrEmpty(innerMap.get("WERKS")));
			innerMap.put("ftyId", ftyId);
		}
		if (erpData != null && erpData.size() > 0) {
			count = matErpbatchDao.insertList(erpData);
		}
		return count;
	}

	@Override
	public void synStockPositionOnUrgent(List<BizStockTaskItemCw> taskItemList,
			List<BizStockTaskPositionCw> positionList, Byte stockTypeId) throws Exception {

		if (positionList != null && positionList.size() > 0) {

			Map<String, BizStockTaskItemCw> taskItemMap = new HashMap<String, BizStockTaskItemCw>();
			if (taskItemList != null && taskItemList.size() > 0) {
				for (BizStockTaskItemCw item : taskItemList) {
					String key = item.getStockTaskId() + "-" + item.getStockTaskRid();
					taskItemMap.put(key, item);
				}
			} else {
				throw new WMSException("缺少行项目");
			}

			for (BizStockTaskPositionCw pItem : positionList) {
				// 原仓位

				String key = pItem.getStockTaskId() + "-" + pItem.getStockTaskRid();

				BizStockTaskItemCw stockTaskItem = taskItemMap.get(key);
				Byte receiptType = stockTaskItem.getReceiptType();
				StockPosition stockPosition = new StockPosition();
				StockPosition stockPositionUrgent = new StockPosition();
				stockPosition.setWhId(pItem.getWhId());
				stockPositionUrgent.setWhId(pItem.getWhId());
				// 上架 组盘上架
				if (receiptType.equals(EnumReceiptType.STOCK_TASK_LISTING.getValue())
						|| receiptType.equals(EnumReceiptType.STOCK_TASK_GROUP.getValue())) {
					stockPosition.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
					stockPosition.setAreaId(pItem.getTargetAreaId());
					stockPosition.setPositionId(pItem.getTargetPositionId());
					stockPositionUrgent.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					stockPositionUrgent.setAreaId(pItem.getTargetAreaId());
					stockPositionUrgent.setPositionId(pItem.getTargetPositionId());
				} else if (receiptType.equals(EnumReceiptType.STOCK_TASK_REMOVAL.getValue())) {
					// 下架
					stockPosition.setAreaId(pItem.getSourceAreaId());
					stockPosition.setPositionId(pItem.getSourcePositionId());
					stockPosition.setDebitOrCredit(EnumDebitCredit.CREDIT_H_SUBTRACT.getName());
					stockPositionUrgent.setAreaId(pItem.getSourceAreaId());
					stockPositionUrgent.setPositionId(pItem.getSourcePositionId());
					stockPositionUrgent.setDebitOrCredit(EnumDebitCredit.DEBIT_S_ADD.getName());
				} else {
					throw new WMSException("单据类型出错");
				}

				stockPosition.setMatId(stockTaskItem.getMatId());
				stockPosition.setFtyId(stockTaskItem.getFtyId());
				stockPosition.setLocationId(stockTaskItem.getLocationId());
				stockPosition.setBatch(stockTaskItem.getBatch());

				stockPosition.setInputDate(new Date());
				stockPosition.setQty(pItem.getQty());
				stockPosition.setUnitId(stockTaskItem.getUnitId());
				stockPosition.setInputDate(new Date());

				stockPositionUrgent.setMatId(stockTaskItem.getMatId());
				stockPositionUrgent.setFtyId(stockTaskItem.getFtyId());
				stockPositionUrgent.setLocationId(stockTaskItem.getLocationId());
				stockPositionUrgent.setBatch(stockTaskItem.getBatch());

				stockPositionUrgent.setInputDate(new Date());
				stockPositionUrgent.setQty(pItem.getQty());
				stockPositionUrgent.setUnitId(stockTaskItem.getUnitId());
				stockPositionUrgent.setInputDate(new Date());
				if (stockTaskItem.getMatStoreType().equals(EnumMatStoreType.USEING.getValue())) {
					// 启用包
					if (pItem.getPackageId() == null) {
						throw new WMSException("包装类型出错");
					}
					stockPosition.setPackageId(pItem.getPackageId());
					stockPositionUrgent.setPackageId(pItem.getPackageId());
					if (pItem.getPalletId() == null) {
						stockPosition.setPalletId(0);
						stockPositionUrgent.setPalletId(0);
					} else {
						stockPosition.setPalletId(pItem.getPalletId());
						stockPositionUrgent.setPalletId(pItem.getPalletId());
					}

				} else {
					// 不启用包
					stockPosition.setPackageId(0);
					stockPosition.setPalletId(0);
					stockPositionUrgent.setPackageId(0);
					stockPositionUrgent.setPalletId(0);
				}
				stockPosition.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPosition.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPosition.setStockTypeId(stockTypeId);

				stockPositionUrgent.setStatus(EnumStockStatus.STOCK_BATCH_STATUS_UNRESTRICTED.getValue());
				stockPositionUrgent.setPackageTypeId(stockTaskItem.getPackageTypeId());
				stockPositionUrgent.setStockTypeId(stockTypeId);
				stockPositionUrgent.setIsUrgent(EnumIsUrgent.URGENT.getValue());
				this.modifyStockPosition(stockPosition);
				this.modifyStockPositionOnSyn(stockPositionUrgent);

			}

		} else {
			throw new WMSException("缺少数据");
		}

	}

	@Override
	public List<Map<String, Object>> getSynStockFromOracle(Integer ftyId, Integer locationId) throws Exception {
		String ftyCode = dictionaryService.getFtyCodeByFtyId(ftyId);
		String locationCode = dictionaryService.getLocCodeByLocId(locationId);

		String sqlStr = "select b.WERKS , b.LGORT , substr(b.MATNR,length(b.MATNR)-15,16) MATNR , b.CHARG , b.CLABS ,w.VERPR  from MCHB b  INNER JOIN MBEW w on b.WERKS = w.BWKEY and b.MATNR = w.MATNR and w.VPRSV = 'S' ";
		if (ftyId > 0) {
			sqlStr = sqlStr + " AND " + "b.WERKS = '" + ftyCode + "'";
		}
		if (locationId > 0) {
			sqlStr = sqlStr + " AND " + "b.LGORT = '" + locationCode + "'";
		}

		List<Map<String, Object>> list = this.getDataFromOracle(sqlStr);

		return list;
	}

	@Override
	public List<Map<String, String>> getShippingTypeList() throws Exception {
		List<Map<String, String>> shippingTypeList = new ArrayList<Map<String, String>>();
		shippingTypeList = EnumShippingType.toList();
		return shippingTypeList;
	}

	// 根据用户查询车辆信息
	@Override
	public List<Map<String, Object>> selectVehicleByProductId(String userId) {
		Map<String, Object> mapProduct = dicProductLineMapper.getRelUserProductLine(userId);
		return dicDeliveryVehicleMapper
				.selectVehicleByProductId(UtilObject.getIntegerOrNull(mapProduct.get("product_line_id")));
	}

	// 根据用户查询司机信息
	@Override
	public List<Map<String, Object>> selectDriverByProductlineId(String userId) {
		Map<String, Object> mapProduct = dicProductLineMapper.getRelUserProductLine(userId);
		return dicDeliveryDriverMapper
				.selectDriverByProductlineId(UtilObject.getIntegerOrNull(mapProduct.get("product_line_id")));
	}

	@Override
	public void saveFileVoList(Integer referReceitpId, Byte referReceiptType, String createUser,
			List<BizReceiptAttachmentVo> fileList) throws Exception {
		if (fileList != null && !fileList.isEmpty()) {
			for (BizReceiptAttachmentVo object : fileList) {
				BizReceiptAttachment Attobject = new BizReceiptAttachment();
				Attobject.setReceiptId(referReceitpId);
				Attobject.setReceiptType(referReceiptType);
				Attobject.setUserId(createUser);
				Attobject.setFileId(object.getFileId());
				Attobject.setFileName(object.getFileName());
				Attobject.setSize(object.getFileSize());
				Attobject.setExt(object.getExt());
				bizReceiptAttachmentDao.insertSelective(Attobject);
			}
		}

	}

	@Override
	public void saveFileAry(Integer referReceitpId, Byte referReceiptType, String createUser, JSONArray fileList)
			throws Exception {
		if (fileList != null && !fileList.isEmpty()) {
			for (int i = 0; i < fileList.size(); i++) {
				JSONObject o = fileList.getJSONObject(i);
				BizReceiptAttachment object = new BizReceiptAttachment();
				object.setReceiptId(referReceitpId);
				object.setReceiptType(referReceiptType);
				object.setUserId(createUser);
				object.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", o));
				object.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", o));
				object.setSize(o.getInt("file_size"));
				String ext = UtilString.STRING_EMPTY;
				int extIndex = object.getFileName().lastIndexOf('.');
				if (object.getFileName().length() - extIndex < 10) {
					ext = object.getFileName().substring(extIndex + 1);
				}

				object.setExt(ext);
				bizReceiptAttachmentDao.insertSelective(object);
			}
		}

	}

	@Override
	public void saveFileList(Integer referReceitpId, Byte referReceiptType, String createUser,
			List<BizReceiptAttachment> fileList) throws Exception {
		if (fileList != null && !fileList.isEmpty()) {
			for (BizReceiptAttachment object : fileList) {
				object.setReceiptId(referReceitpId);
				object.setReceiptType(referReceiptType);
				object.setUserId(createUser);
				object.setSize(object.getFileSize());
				String ext = UtilString.STRING_EMPTY;
				int extIndex = UtilString.getStringOrEmptyForObject(object.getFileName()).lastIndexOf('.');
				if (object.getFileName().length() - extIndex < 10) {
					ext = object.getFileName().substring(extIndex + 1);
				}
				object.setExt(ext);
				bizReceiptAttachmentDao.insertSelective(object);
			}
		}

	}

	@Override
	public void deleteDateByDays(Integer days) {
		failMesDao.deleteDateByDays(days);
	}

	@Override
	public void modifyStockBatchCanNull(Map<String, Object> map) throws Exception {


		if (!(map.containsKey("matId") && map.containsKey("locationId") && map.containsKey("batch")
				&& map.containsKey("status") && map.containsKey("createUserId") && map.containsKey("debitCredit")
				&& map.containsKey("qty"))) {
			throw new WMSException("更新批次库存参数异常");
		}

		Integer matId = UtilObject.getIntegerOrNull(map.get("matId"));
		Integer locationId = UtilObject.getIntegerOrNull(map.get("locationId"));
		Long batch = UtilObject.getLongOrNull(map.get("batch"));
		batch = batch == null ? 0L : batch;
		Byte status = UtilObject.getByteOrNull(map.get("status"));
		String createUserId = UtilObject.getStringOrNull(map.get("createUserId"));
		String debitCredit = UtilObject.getStringOrNull(map.get("debitCredit"));
		BigDecimal qty = UtilObject.getBigDecimalOrZero(map.get("qty"));
		Byte isUrgent = UtilObject.getByteOrNull(map.get("isUrgent"));
		Byte stockTypeId = UtilObject.getByteOrNull(map.get("stockTypeId"));
		if (stockTypeId == null) {
			stockTypeId = 1;
		}
		// 查询批次库存信息
		StockBatchKey key = new StockBatchKey();
		key.setMatId(matId); // '物料编号',
		key.setLocationId(locationId);// '库存地点',
		key.setBatch(batch); // '批号',
		key.setStatus(status);
		if (map.containsKey("isUrgent")) {
			key.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());
			key.setIsUrgent(isUrgent);
		}
		// 库存类型
		key.setStockTypeId(stockTypeId);
		StockBatch stockBatch = stockBatchDao.selectByUniqueKey(key);
		if (stockBatch == null) {
			// 在该工厂-库存地点不存在该批次的物料
			stockBatch = new StockBatch();
			stockBatch.setIsUrgent(isUrgent);
			stockBatch.setMatId(matId); // '物料编号',
			stockBatch.setLocationId(locationId);// '库存地点',
			stockBatch.setBatch(batch); // '批号',
			stockBatch.setCreateUser(createUserId); // '创建人',

			// stockBatch.setSpecStock(bizMaterialDocItem.getSpecStock());//
			// 特殊库存标识
			// stockBatch.setSpecStockCode(bizMaterialDocItem.getSpecStockCode());//
			// 特殊库存代码
			// stockBatch.setSpecStockName(bizMaterialDocItem.getSpecStockName());//
			// 特殊库存描述
			stockBatch.setStatus(status);
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty.negate());// '非限制库存
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatch.setQty(qty);// '非限制库存
			} else {
				throw new SAPDebitCreditException();
			}

			stockBatch.setStockTypeId(stockTypeId);
			stockBatchDao.insertSelective(stockBatch);
		} else {
			StockBatch stockBatchTmp = new StockBatch();
			stockBatchTmp.setMatId(matId); // '物料编号',
			stockBatchTmp.setLocationId(locationId);// '库存地点',
			stockBatchTmp.setBatch(batch); // '批号',
			stockBatchTmp.setStatus(status);
			stockBatchTmp.setIsUrgent(isUrgent);
			stockBatchTmp.setStockTypeId(stockTypeId);
			if (map.containsKey("isUrgent")) {
				stockBatchTmp.setUrgentSynStatus(EnumUrgentSynStatus.SYN_UNFINISH.getValue());

			}
			if ("H".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().subtract(qty));
			} else if ("S".equalsIgnoreCase(debitCredit)) {
				stockBatchTmp.setQty(stockBatch.getQty().add(qty));
			} else {
				throw new SAPDebitCreditException();
			}
			if (BigDecimal.ZERO.compareTo(stockBatchTmp.getQty()) == 0) {
				stockBatchDao.deleteByByUniqueKey(key);
			} else {
				stockBatchDao.updateQty(stockBatchTmp);
			}
		}
		
	}

	@Override
	public List<DicBoard> getBordList() {
		List<DicBoard> boardList = new ArrayList<DicBoard>();
		boardList = boardDao.selectAllBoard();
		List<DicCorp> corpList = corpDao.selectAllCorpList();
		List<DicWarehouse> wList = warehouseMapper.selectAllWarehouseListWithCoryId();
		if(boardList!=null){
			for(DicBoard b:boardList){
				if(corpList!=null){
					for(DicCorp c:corpList){
						if(b.getBoardId().equals(c.getBoardId())){
							List<DicCorp> innerCList = b.getCorpList();
							if(innerCList == null){
								innerCList = new ArrayList<DicCorp>();
							}
							innerCList.add(c);
							if(wList!=null){
								for(DicWarehouse w:wList){
									List<DicWarehouse> warehouseList = c.getWarehouseList();
									if(warehouseList==null){
										warehouseList = new ArrayList<DicWarehouse>();
									}
									warehouseList.add(w);
									c.setWarehouseList(warehouseList);
								}
							}
							b.setCorpList(innerCList);
						}
					}
				}
			}
		}
		
 		return boardList;
	}



}
