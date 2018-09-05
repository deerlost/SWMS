package com.inossem.wms.service.biz.impl;

import com.inossem.wms.dao.biz.BizBusinessHistoryMapper;
import com.inossem.wms.dao.biz.BizPkgOperationHeadMapper;
import com.inossem.wms.dao.biz.BizPkgOperationItemMapper;
import com.inossem.wms.dao.biz.BizPkgOperationPositionMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportHeadMapper;
import com.inossem.wms.dao.biz.SequenceDAO;
import com.inossem.wms.dao.dic.DicClassTypeMapper;
import com.inossem.wms.dao.dic.DicFactoryMapper;
import com.inossem.wms.dao.dic.DicMaterialMapper;
import com.inossem.wms.dao.dic.DicMaterialReqMatTypeMapper;
import com.inossem.wms.dao.dic.DicPackageTypeMapper;
import com.inossem.wms.dao.dic.DicProductLineMapper;
import com.inossem.wms.dao.dic.DicUnitMapper;
import com.inossem.wms.dao.dic.DicWarehousePalletMapper;
import com.inossem.wms.dao.rel.RelPackageScanCountMapper;
import com.inossem.wms.dao.serial.SerialPackageMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizPkgOperationHead;
import com.inossem.wms.model.biz.BizPkgOperationItem;
import com.inossem.wms.model.biz.BizPkgOperationPosition;
import com.inossem.wms.model.dic.DicFactory;
import com.inossem.wms.model.dic.DicInstallation;
import com.inossem.wms.model.dic.DicMaterial;
import com.inossem.wms.model.dic.DicPackageType;
import com.inossem.wms.model.dic.DicProductLine;
import com.inossem.wms.model.dic.DicSupplier;
import com.inossem.wms.model.dic.DicUnit;
import com.inossem.wms.model.dic.DicWarehousePallet;
import com.inossem.wms.model.enums.*;
import com.inossem.wms.model.rel.RelPackageScanCountKey;
import com.inossem.wms.model.serial.SerialPackage;
import com.inossem.wms.model.vo.BizPkgOperationHeadVo;
import com.inossem.wms.model.vo.DicPackageTypeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IPkgOperationService;
import com.inossem.wms.util.UtilObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 包装单
 * @author sw
 *
 */
@Transactional
@Service("packageService")
public class PkgOperationServiceImpl implements IPkgOperationService {
	
    @Autowired
    private BizPkgOperationHeadMapper pkgHeadDao;
    @Autowired
    private BizPkgOperationItemMapper pkgItemDao;
    @Autowired
    private BizPkgOperationPositionMapper pkgPositionDao;
    @Autowired
    private DicClassTypeMapper classTypeDao;
    @Autowired
    private DicProductLineMapper dicProductLineDao;
    @Autowired
    private DicMaterialMapper dicMaterialDao;
    @Autowired
    private DicPackageTypeMapper dicPackageTypeDao;
    @Autowired
    private DicWarehousePalletMapper dicWarehousePalletDao;
    @Autowired
    private BizStockInputTransportHeadMapper bizStockInputTransportHeadDao;
    @Autowired
    private DicFactoryMapper dicFactoryDao;
    @Autowired
    private DicMaterialReqMatTypeMapper dicMaterialReqMatTypeDao;
    @Autowired
    private SerialPackageMapper serialPackageDao;
    @Autowired
    private ICommonService commonService;
    @Resource
    private SequenceDAO sequenceDao;
    @Autowired
    private RelPackageScanCountMapper packageCountDao;
    @Autowired
    private DicUnitMapper dicUnitDao;
    @Autowired
    private BizBusinessHistoryMapper historyDao;

    
    /**
     * 获取code
     * @param key
     * @return
     */
    public long getNewOrderID(String key) {
        return this.sequenceDao.selectNextVal(key);
    }
    
    /**
     * 查询列表信息
     */
	@Override
    public List<BizPkgOperationHeadVo> selectPkgList(Map<String, Object> param) {
        return this.pkgHeadDao.selectPkgListOnPaging(param);
    }
    
	
	/**
	 * 查询班次 产品线 集合 装置集合
	 */
	@Override
	public Map<String, Object> selectPkgClassLineInstallationList(String userId){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("classTypeList", this.classTypeDao.selectAll());
        result.put("productLineList", this.commonService.selectProductLineListAndDicInstallationList(userId));
        result.put("class_type_id", this.commonService.selectNowClassType());
        return result;
    }

	/**
	 * 保存包装单
	 */
	@Override
    public JSONObject savePkgOperationData(JSONObject jsonData, CurrentUser user) throws Exception {
        boolean isAdd = false;
        BizPkgOperationHead head = null;
        int pkgOperationId = 0;
        if(jsonData.containsKey("pkg_operation_id")) {
            pkgOperationId = UtilObject.getIntOrZero(jsonData.get("pkg_operation_id"));
        }
        //判断是否是新建
        if(pkgOperationId == 0) {
            isAdd = true;
        } else {
            head = this.pkgHeadDao.selectByPrimaryKey(Integer.valueOf(pkgOperationId));
            if(head == null || "".equals(head.getPkgOperationCode()) || head.getIsDelete().byteValue() == 1) {
                isAdd = true;
            }
        }
        
    	// 如果当前操作为修改,则删除详细信息
 		if (!isAdd) {
 			this.pkgItemDao.deleteBizPkgOperationItem(head.getPkgOperationId());
            this.pkgPositionDao.deleteBizPkgOperationPosition(head.getPkgOperationId());
 		}

        
    	/******************************************************
		 * HEAD信息处理
		 ******************************************************/
		@SuppressWarnings("unchecked")
		Map<String, Object> headData = (Map<String, Object>) jsonData;
        head = this.saveHeadData(isAdd, headData, head, user);

		/******************************************************
		 * ITEM信息处理
		 ******************************************************/
        @SuppressWarnings("unchecked")
		List<Map<String,Object>> itemList = (List<Map<String,Object>>)jsonData.get("item_list");

        for(int result = 0; result < itemList.size(); ++result) {
            Map<String, Object> itemDataTemp = (Map<String, Object>)itemList.get(result);
            BizPkgOperationItem item = this.saveItemData(itemDataTemp, head);
            
        	/******************************************************
			 * position信息处理
			 ******************************************************/
			@SuppressWarnings("unchecked")
            List<Map<String,Object>> positionList = (List<Map<String,Object>>)itemDataTemp.get("position_list");
            if(positionList != null) {
                for(int j = 0; j < positionList.size(); ++j) {
                    Map<String,Object> positionDataTemp = (Map<String,Object>)positionList.get(j);
                    this.savePositionData(positionDataTemp, head, item);
                }
            }
        }

        JSONObject json = new JSONObject();
        json.put("pkg_operation_id", head.getPkgOperationId());
        json.put("pkg_operation_code", head.getPkgOperationCode());
        return json;
    }
	
	/**
	 * head信息
	 * @param isAdd
	 * @param headData
	 * @param head
	 * @param user
	 * @return
	 */
    public BizPkgOperationHead saveHeadData(boolean isAdd, Map<String, Object> headData, BizPkgOperationHead head, CurrentUser user) {
        if(isAdd) {
            head = new BizPkgOperationHead();
            long pkgOperationCode = this.getNewOrderID("pkg_operation");
            head.setPkgOperationCode(String.valueOf(pkgOperationCode));
            head.setCreateTime(new Date());
        }
        head.setClassTypeId(UtilObject.getIntegerOrNull(headData.get("class_type_id")));
        head.setProductLine(UtilObject.getIntegerOrNull(headData.get("product_line_id")));
        head.setInstallation(UtilObject.getIntegerOrNull(headData.get("installation_id")));
        head.setReferReceiptCode(UtilObject.getStringOrNull(headData.get("refer_receipt_code")));
        head.setReferReceiptId(0);
        head.setReferReceiptType(EnumReceiptType.STOCK_INPUT_PKG_OPERATION.getValue());
        head.setPackageTeam(UtilObject.getStringOrNull(headData.get("package_team")));
        head.setStatus(UtilObject.getByteOrNull(headData.get("status")));
        head.setIsPallet(UtilObject.getByteOrNull(headData.get("is_pallet")));
        head.setRemark(UtilObject.getStringOrNull(headData.get("remark")));
        head.setProductBatch(UtilObject.getStringOrNull(headData.get("product_batch")));
        head.setDocumentType(UtilObject.getStringOrNull(headData.get("refer_receipt_type")));
        head.setCreateUser(user.getUserId());
        head.setModifyTime(new Date());
        head.setIsDelete(EnumBoolean.FALSE.getValue());
        head.setSynType(EnumSynType.NO_SYN.getValue());
        head.setBeforeOrderCode("");
        head.setBeforeOrderId(0);
        head.setBeforeOrderRid(0);
        head.setBeforeOrderType(null);
        if(isAdd) {
            this.pkgHeadDao.insertSelective(head);
        } else {
            this.pkgHeadDao.updateByPrimaryKeySelective(head);
        }
        return head;
    }
    
    /**
     * itme信息
     * @param itemData
     * @param head
     * @return
     */
    public BizPkgOperationItem saveItemData(Map<String, Object> itemData, BizPkgOperationHead head) {
        BizPkgOperationItem item = new BizPkgOperationItem();
        item.setPkgOperationId(head.getPkgOperationId());
        item.setPkgOperationRid(UtilObject.getIntegerOrNull(itemData.get("pkg_operation_rid")));
        item.setFtyId(UtilObject.getIntegerOrNull(itemData.get("fty_id")));
        item.setBatch(0L);
        item.setMatId(UtilObject.getIntegerOrNull(itemData.get("mat_id")));
        item.setOrderQty(UtilObject.getBigDecimalOrZero(itemData.get("order_qty").equals("--")?0:itemData.get("order_qty")));
        item.setPkgQty(UtilObject.getBigDecimalOrZero(itemData.get("pkg_qty")));
        item.setLocationId(0);
        item.setCreateTime(new Date());
        item.setModifyTime(new Date());
        item.setPackageTypeId(UtilObject.getIntegerOrNull(itemData.get("package_type_id")));
        item.setIsDelete(Byte.valueOf(EnumBoolean.FALSE.getValue()));
        this.pkgItemDao.insertSelective(item);
        return item;
    }
    
    
    /**
     * postion信息
     * @param postionData
     * @param head
     * @param item
     * @return
     */
    public BizPkgOperationPosition savePositionData(Map<String, Object> postionData, BizPkgOperationHead head ,BizPkgOperationItem item)throws Exception {
        BizPkgOperationPosition position = new BizPkgOperationPosition();
        position.setPkgOperationId(head.getPkgOperationId());
        position.setPkgOperationRid(item.getPkgOperationRid());
        position.setPkgOperationPositionId(UtilObject.getIntOrZero(postionData.get("pkg_operation_position_id")));
        position.setPalletId(UtilObject.getIntOrZero(postionData.get("pallet_id")));
        int packageId=UtilObject.getIntOrZero(postionData.get("package_id"));
        position.setPackageId(packageId);
        position.setPackageCode(UtilObject.getStringOrEmpty(postionData.get("package_code")));
        position.setPackageTypeId(item.getPackageTypeId());
        //包装物SN处理
    	if(UtilObject.getStringOrNull(position.getPackageCode()) != null) {
    		//如果包裹id是0就是新增包裹(只有订单完成时保存到数据库)
            if(packageId == 0 && head.getStatus().equals(EnumReceiptStatus.RECEIPT_SUBMIT.getValue())) {
            	if(serialPackageDao.selectByCode(position.getPackageCode()) != null) {
            		throw new WMSException(position.getPackageCode()+"已存在或者已被创建");
            	}
        		SerialPackage pkg = new SerialPackage();
            	pkg.setPackageCode(position.getPackageCode());
            	pkg.setModifyTime(new Date());
            	pkg.setCreateTime(new Date());
            	pkg.setPackageTypeId(item.getPackageTypeId());
            	String sup = position.getPackageCode().substring(2, 4);
            	if(sup.substring(0,1).equals("0")) {
            		pkg.setSupplierId(UtilObject.getIntegerOrNull(sup.replaceAll("0", "")));
            	}else {
            		pkg.setSupplierId(UtilObject.getIntegerOrNull(sup));
            	}
            	pkg.setMatId(item.getMatId());
            	pkg.setCreateUser(head.getCreateUser());
            	pkg.setModifyUser(head.getCreateUser());
            	pkg.setIsDelete(EnumBoolean.FALSE.getValue());
            	pkg.setStatus(EnumBoolean.TRUE.getValue());
            	serialPackageDao.insertSelective(pkg);
            	position.setPackageId(pkg.getPackageId());
            //其它的包装物改SN包装物为激活状态	
            }else {
            	if(serialPackageDao.selectByPrimaryKey(packageId) == null) {
            		throw new WMSException(position.getPackageCode()+"不存在或者已被删除");
            	}
            	SerialPackage pkg = new SerialPackage();
            	pkg.setPackageId(packageId);
            	pkg.setMatId(item.getMatId());
            	pkg.setCreateUser(head.getCreateUser());
            	pkg.setModifyUser(head.getCreateUser());
            	pkg.setStatus(EnumBoolean.TRUE.getValue());
            	serialPackageDao.updateByPrimaryKeySelective(pkg);
            }
    	}
        position.setCreateTime(new Date());
        position.setModifyTime(new Date());
        position.setIsDelete(EnumBoolean.FALSE.getValue());
        this.pkgPositionDao.insertSelective(position);
        return position;
    }
    
    /**
     * 查询物料信息
     */
	@Override
    public List<Map<String, Object>> selectMaterialList(Map<String, Object> param) {
        return this.dicMaterialDao.selectMaterialList(param);
    }
	
	/**
	 * 查询包裹或者托盘信息
	 */
	@Override
    public Map<String, Object> selectPkgOrPalletList(Integer packageTypeId,String keyword,String type) throws Exception{
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("keyword", keyword);
        if(type.equals("pkgtype")) {
        	result.put("type", "pkgtype");
        	DicPackageType dicPackageType = dicPackageTypeDao.selectByPrimaryKey(packageTypeId);
        	String snkey = keyword.substring(0, 2);
        	if(dicPackageType !=null) {
    			if(!dicPackageType.getSnSerialKey().equals(snkey)) {
    				throw new WMSException("包装物SN类型与包装物类型不匹");
    			}
        	} else {
        		throw new WMSException("包装物类型不存在");
        	}
        	boolean flag = false;
        	String sup = keyword.substring(2, 4);
        	List<DicSupplier> supList = dicPackageType.getSupplierList();
        	if(supList!=null&&!supList.isEmpty()) {
        		for(DicSupplier supplier : supList) {
            		String supp = supplier.getSupplierId()+"";
            		if(supp.length()<2) {
            			while(supp.length()<2) {
            				supp = "0"+supp;
            			}
            		}
            		if(supp.equals(sup)) {
            			flag = true;
            			break;
            		}
            	}
            	if(flag == false) {
            		throw new WMSException("包装物SN与包装物的供应商不符");
            	}
        	} else {
        		throw new WMSException("包装物SN对应的供应商不存在");
        	}
        	List<DicPackageTypeVo> list = new ArrayList<DicPackageTypeVo>();
        	List<DicPackageTypeVo> dicPackageTypes = this.dicPackageTypeDao.selectDicPackageTypeList(param);
        	if(dicPackageTypes!=null&&dicPackageTypes.size()>0) {
        		//如果主数据库中有 验证包装物SN是否已经被使用
        		if(dicPackageTypeDao.checkPkgIsUsed(dicPackageTypes.get(0).getPackageId(),"")>0) {
                    throw new WMSException("包裹已被使用");
        		}else {
        			DicPackageTypeVo pack = dicPackageTypes.get(0);
        			if(!pack.getPackageTypeId().equals(packageTypeId)) {
                        throw new WMSException("包装物SN类型与包装物类型不匹配");
        			}else {
        				list.add(pack);
        			}
        		}
        	}else {
        		throw new WMSException("没有查到此包装物SN");
//        		//如果不存在包裹进行校验是否在草稿中
//        		if(dicPackageTypeDao.checkPkgIsUsed(null,keyword)>0){
//                    throw new WMSException("包装物SN已被使用");
//                   //新建(只有包装单完成时保存到数据库)
//                }else{
//                    DicPackageTypeVo dicPackageTypeVo = dicPackageTypeDao.selectDicPackageTypeById(packageTypeId);
//                    dicPackageTypeVo.setPackageCode(keyword);
//                    dicPackageTypeVo.setPackageId(0);
//                    list.add(dicPackageTypeVo);
//                }
        	}
        	result.put("pkgtypeList", list);
        }else if(type.equals("pallet")) {
            result.put("type", "pallet");
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        	List<DicWarehousePallet> dicWarehousePallets = this.dicWarehousePalletDao.selectDicWarehousePalletList(param);
            if(dicWarehousePallets != null && !dicWarehousePallets.isEmpty()) {
            	DicWarehousePallet pallet = dicWarehousePallets.get(0);
            	//判断托盘是否被使用
            	if(dicWarehousePalletDao.checkPalletIsUsed(pallet.getPalletId())>0) {
                    throw new WMSException("托盘已被使用");
            	}else {
                 	Map<String,Object> map = new HashMap<String,Object>();
                 	map.put("pallet_id", pallet.getPalletId());//托盘id
                 	map.put("pallet_code", pallet.getPalletCode());//托盘code
                 	map.put("max_weight", pallet.getMaxWeight());//最大容量
                 	list.add(map);
            	}
            }else {
            	  throw new WMSException("未查到此托盘");
            }
            result.put("palletList", list);
        }
        return result;
    }
	
	/**
	 * 查询详情
	 */
	@Override
    public BizPkgOperationHeadVo selectPkgDetailById(Integer pkgOperationId) {
		BizPkgOperationHeadVo head = this.pkgHeadDao.selectDetailById(pkgOperationId);
		head.setClassTypeList(this.classTypeDao.selectAll());
		head.setProductLineList(this.commonService.selectProductLineListAndDicInstallationList(null));
        return head;
    }
	
	/**
	 * 创建托盘
	 */
	@Override
    public DicWarehousePallet createPkgWarehousePallet(String userId)throws Exception {
		return commonService.newPallet(userId);
    }

	/**
	 * 生产订单信息
	 */
	@Override
	public Map<String, Object> selectOrderViewBySap(String code,String userId) throws Exception{
		Map<String, Object> data = commonService.selectProduceOrderInfo(code);
		if(data == null || data.isEmpty()) {
			throw new WMSException("获取生产订单失败");
		}
		Map<String, Object> result = new HashMap<String,Object>();
		if(data!=null&&!data.isEmpty()) {
			DicFactory factory = dicFactoryDao.selectByCode(UtilObject.getStringOrEmpty(data.get("WERKS")));
			result.put("fty_id", factory.getFtyId());
			result.put("fty_code", factory.getFtyCode());
			result.put("fty_name", factory.getFtyName());
			result.put("product_batch", "");
			result.put("package_team", UtilObject.getStringOrEmpty(data.get("PLGRP")));
			List<DicProductLine> dicProductLines = this.dicProductLineDao.selectDicProductLineList(userId);
			if(dicProductLines!=null&&dicProductLines.size()>0) {
				DicProductLine dicProductLine = dicProductLines.get(0);
				result.put("product_line_id", dicProductLine.getProductLineId());
				List<DicInstallation> dicInstallations= dicProductLine.getInstallations();
				if(dicInstallations!=null&&dicInstallations.size()>0) {
					result.put("installation_id", dicInstallations.get(0).getInstallationId());	
				}
			}
			result.put("class_type_id",this.bizStockInputTransportHeadDao.selectNowClassType());
			result.put("refer_receipt_type",EnumProductOrderType.getNameByValue(UtilObject.getStringOrEmpty(data.get("AUART"))));
			result.put("refer_receipt_code", UtilObject.getStringOrEmpty(data.get("AUFNR")));
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			Map<String,Object> mat = this.dicMaterialDao.getMatUnitByMatCode(UtilObject.getStringOrEmpty(data.get("PLNBEZ").toString().trim()));
			map.put("mat_id", mat.get("mat_id"));
			map.put("mat_code", mat.get("mat_code"));
			map.put("mat_name", mat.get("mat_name"));
			map.put("unit_id", mat.get("unit_id"));
			map.put("unit_code", UtilObject.getStringOrEmpty(data.get("GMEIN").toString().trim()));
			//map.put("unit_en", mat.get("name_en"));
			map.put("unit_name", mat.get("name_zh"));
			map.put("order_qty", UtilObject.getStringOrEmpty(data.get("GAMNG")));
			map.put("package_type_list", this.dicMaterialReqMatTypeDao.selectPackageTypeListByMatId(Integer.parseInt(mat.get("mat_id").toString())));
			itemList.add(map);
			result.put("item_list", itemList);
		}
		return result;
	}

	@Override
	public String deletePkgOperation(Integer id) throws Exception{
		pkgHeadDao.deleteBizPkgOperationHead(id);
		pkgItemDao.deleteBizPkgOperationItem(id);
		pkgPositionDao.updatePkgSnById(id);
		pkgPositionDao.deleteBizPkgOperationPosition(id);
		return "删除成功";
	}

	/**
	 * 记录扫描次数
	 * @param packageId
	 * @return
	 * @throws Exception
	 */
	private void insertScanCount(Integer packageId) throws Exception{
		RelPackageScanCountKey count = packageCountDao.selectByPkgId(packageId);
		if(count!=null) {
			packageCountDao.update(count);
		}else {
			count = new RelPackageScanCountKey();
			count.setCount(1);
			count.setPackageId(packageId);
			packageCountDao.insert(count);
		}
	}
	
	/**
	 * 获取包装物信息
	 */
	@Override
	public Map<String, Object> selectPackegeInfo(String packageCode) throws Exception {
		Map<String, Object> result = new HashMap<String,Object>();
		SerialPackage pkg = serialPackageDao.selectByCode(packageCode);
		this.insertScanCount(pkg.getPackageId());
		Map<String, Object> productInfo = new HashMap<String,Object>();
		productInfo.put("package_id", pkg.getPackageId());
		productInfo.put("package_code", pkg.getPackageCode());
		productInfo.put("company_name", "重庆川维化工有限公司");
		productInfo.put("tel_phone", "+86 010-3111111");
		DicMaterial mat = dicMaterialDao.selectByPrimaryKey(pkg.getMatId()); 
		productInfo.put("mat_id", mat.getMatId());
		productInfo.put("mat_code", mat.getMatCode());
		productInfo.put("mat_name", mat.getMatName());
		DicPackageType packageType = dicPackageTypeDao.selectByPrimaryKey(pkg.getPackageTypeId());
		productInfo.put("package_type_id", packageType.getPackageTypeId());
		productInfo.put("package_type_code", packageType.getPackageTypeCode());
		productInfo.put("package_type_name", packageType.getPackageTypeName());
		DicUnit pkgunit = dicUnitDao.selectByPrimaryKey(packageType.getUnitId());
		productInfo.put("package_type_unit_id",pkgunit.getUnitId() );
		productInfo.put("package_type_unit_code", pkgunit.getUnitCode());
		productInfo.put("package_type_unit_name", pkgunit.getNameZh());
		productInfo.put("package_standard_ch", packageType.getPackageStandardCh());
		productInfo.put("package_standard", packageType.getPackageStandard());
		result.put("product_info", productInfo);
		Map<String, Object> productionInfo = pkgHeadDao.getProductionInfo(pkg.getPackageId());
		if(productionInfo==null) {
			productionInfo = new HashMap<String,Object>();
			productionInfo.put("production_batch", "");
			productionInfo.put("quality_batch", "");
			productionInfo.put("create_time", "");
		}
		result.put("production_info", productionInfo);
		Map<String, Object> expressInfo = historyDao.getPackageExpressInfo(pkg.getPackageId());
		if(expressInfo==null) {
			expressInfo = new HashMap<String,Object>();
			expressInfo.put("receive_name", "");
			expressInfo.put("shipping_type_name", "");
			expressInfo.put("location_name", "");
		}
		expressInfo.put("origin_country","CN-中国");
		expressInfo.put("transport_country","CN-中国");
		result.put("express_info",expressInfo );
		Map<String,Object> labelInfo = new HashMap<String,Object>();
		RelPackageScanCountKey count = packageCountDao.selectByPkgId(pkg.getPackageId());
		labelInfo.put("count",count.getCount());
		labelInfo.put("query_time",count.getQueryTime());
		result.put("label_info", labelInfo);
		return result;
	}


}
