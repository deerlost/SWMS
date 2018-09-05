package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.biz.BizReceiptAttachmentMapper;
import com.inossem.wms.dao.biz.BizStockInputHeadMapper;
import com.inossem.wms.dao.biz.BizUrgenceHeadMapper;
import com.inossem.wms.dao.biz.BizUrgenceItemMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.biz.BizReceiptAttachment;
import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.biz.BizUrgenceHead;
import com.inossem.wms.model.biz.BizUrgenceItem;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.enums.EnumSequence;
import com.inossem.wms.model.vo.BizReceiptAttachmentVo;
import com.inossem.wms.model.vo.BizUrgenceReqVo;
import com.inossem.wms.model.vo.BizUrgenceResVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IUrgenceService;
import com.inossem.wms.util.UtilJSON;
import com.inossem.wms.util.UtilProperties;
import com.inossem.wms.util.UtilString;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@Service("urgenceInAndOutStockService")
public class UrgenceServiceImpl implements IUrgenceService {

	private static Logger logger = LoggerFactory.getLogger(UrgenceServiceImpl.class);

	@Autowired
	private BizUrgenceHeadMapper bizUrgenceHeadMapper;

	@Autowired
	private BizUrgenceItemMapper bizUrgenceItemMapper;

	@Autowired
	private BizStockInputHeadMapper bizStockInputHeadMapper;

	@Autowired
	ICommonService commonService;

	@Autowired
	private BizReceiptAttachmentMapper bizReceiptAttachmentDao;

	@Override
	public List<BizUrgenceResVo> listBizUrgenceHead(BizUrgenceReqVo paramVo) {

		return bizUrgenceHeadMapper.listBizUrgenceHeadOnPaging(paramVo);
	}

	@Override
	public List<BizUrgenceResVo> listBizUrgenceForApp(BizUrgenceReqVo paramVo) {

		return bizUrgenceHeadMapper.listBizUrgenceForApp(paramVo);
	}

	@Override
	public Map<String, Object> getUrgenceById(Integer urgenceId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			BizUrgenceResVo bizUrgenceHead = null;
			List<BizUrgenceResVo> bizUrgenceItemList = new ArrayList<BizUrgenceResVo>();
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			List<BizReceiptAttachmentVo> fileList = new ArrayList<BizReceiptAttachmentVo>();

			bizUrgenceHead = bizUrgenceHeadMapper.selectHeadById(urgenceId);
			if (null != bizUrgenceHead) {
				// userList =
				// commonService.getReceiptApprove(Long.parseLong(jjdh.getZdjbh()),Integer.parseInt(jjdh.getZdjlx()));
				bizUrgenceItemList = bizUrgenceHeadMapper.selectItemById(urgenceId);

				if (bizUrgenceHead.getReceiptType() == 101) {
					if (bizUrgenceItemList != null && bizUrgenceItemList.size() > 0) {
						for (BizUrgenceResVo svo : bizUrgenceItemList) {
							if (3 == svo.getStatus()) {
								BizStockInputHead head = bizStockInputHeadMapper
										.selectByInputCodeUrgence(svo.getRelateReceiptCode());
								svo.setItemReceiptType(head.getReceiptType());
							}
						}
					}
				}

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("receiptId", bizUrgenceHead.getUrgenceId());
				param.put("receiptType", bizUrgenceHead.getReceiptType());
				userList = bizUrgenceHeadMapper.selectUserByIdAndType(param);

				fileList = bizReceiptAttachmentDao.selectByReceipt(param);

			} else {
				bizUrgenceHead = new BizUrgenceResVo();
			}

			body.put("urgence_id", bizUrgenceHead.getUrgenceId());
			body.put("urgence_code", bizUrgenceHead.getUrgenceCode());
			body.put("user_name", bizUrgenceHead.getUserName());
			body.put("receipt_type", bizUrgenceHead.getReceiptType());
			body.put("receipt_type_name", bizUrgenceHead.getReceiptTypeName());
			body.put("create_time", bizUrgenceHead.getCreateTime());
			body.put("fty_id", bizUrgenceHead.getFtyId());
			body.put("fty_name", bizUrgenceHead.getFtyName());
			body.put("fty_code", bizUrgenceHead.getFtyCode());
			body.put("location_id", bizUrgenceHead.getLocationId());
			body.put("location_name", bizUrgenceHead.getLocationName());
			body.put("location_code", bizUrgenceHead.getLocationCode());
			body.put("demand_dept", bizUrgenceHead.getDemandDept());
			body.put("demand_person", bizUrgenceHead.getDemandPerson());
			body.put("remark", bizUrgenceHead.getRemark());
			body.put("item_list", bizUrgenceItemList);
			if (bizUrgenceItemList != null && bizUrgenceItemList.size() > 0) {
				body.put("status_code", bizUrgenceItemList.get(0).getStatus());
				body.put("status_name", bizUrgenceItemList.get(0).getStatusName());
			} else {
				body.put("status_code", "1");
				body.put("status_name", "草稿");
			}

			if (userList != null && userList.size() > 0) {
				body.put("user_list", userList);
			} else {
				List<Map<String, Object>> userListNull = new ArrayList<Map<String, Object>>();

				body.put("user_list", userListNull);
			}

			if (fileList != null && fileList.size() > 0) {

				 for(BizReceiptAttachmentVo fileVo:fileList) {
				 fileVo.setFileUrl(
						 UtilProperties.getInstance().getFileServerUrl()+"download?file_id="+fileVo.getFileId());

				 }

				body.put("file_list", fileList);
			} else {
				List<BizReceiptAttachmentVo> fileListNull = new ArrayList<BizReceiptAttachmentVo>();
				body.put("file_list", fileListNull);
			}
		} catch (Exception e) {
			logger.error("获取紧急出入库 - ", e);
		}

		return body;

	}

	/**
	 * 暂存单据
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param mapColl
	 * @param user
	 * @return
	 */
	public JSONObject saveOrder(JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		boolean isAdd = false; // 新增or修改

		try {
			// 主数据处理

			// Map<String,Object> mst = (Map<String,Object>)mapColl.get("mst");
			// JSONObject mst = json.getJSONObject("head");
			if (!json.containsKey("urgence_id")) {
				isAdd = true;
			}
			BizUrgenceHead bizUrgenceHead = saveToMstData(isAdd, json, user.getUserId());

			// 修改与新增处理
			if (!isAdd) {
				commonService.removeReceiptUser(bizUrgenceHead.getUrgenceId(), bizUrgenceHead.getReceiptType());
				bizUrgenceItemMapper.deleteByUrgenceId(bizUrgenceHead.getUrgenceId());
			}

			// 经办人处理

			JSONArray userList = json.getJSONArray("user_list");
			for (int i = 0; i < userList.size(); i++) {

				JSONObject juser = userList.getJSONObject(i);
				commonService.addReceiptUser(bizUrgenceHead.getUrgenceId(), bizUrgenceHead.getReceiptType(),
						juser.getString("user_id"), juser.getInt("role_id"));

			}

			JSONArray fileList = json.getJSONArray("file_list");

			if (fileList != null && fileList.size() > 0) {
				bizReceiptAttachmentDao.deleteByReceiptId(bizUrgenceHead.getUrgenceId());

				for (int i = 0; i < fileList.size(); i++) {

					JSONObject jfile = fileList.getJSONObject(i);

					BizReceiptAttachment bizReceiptAttachment = new BizReceiptAttachment();
					bizReceiptAttachment.setReceiptId(bizUrgenceHead.getUrgenceId());
					bizReceiptAttachment.setReceiptType(bizUrgenceHead.getReceiptType());
					bizReceiptAttachment.setUserId(user.getUserId());
					bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", jfile));
					bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", jfile));
					bizReceiptAttachment.setSize(jfile.getInt("file_size"));
					String ext = UtilString.STRING_EMPTY;
					int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
					if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
						ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
					}

					bizReceiptAttachment.setExt(ext);

					bizReceiptAttachmentDao.insertSelective(bizReceiptAttachment);

				}
			}
			// 明细处理

			JSONArray dtlList = json.getJSONArray("item_list");
			for (int i = 0; i < dtlList.size(); i++) {
				JSONObject dtl = dtlList.getJSONObject(i);
				dtl.put("urgence_id", bizUrgenceHead.getUrgenceId());
				saveToDtlData(dtl, user.getUserId(), 1);
			}

			body.put("is_success", 0);
			body.put("message", bizUrgenceHead.getUrgenceCode());

		} catch (Exception e) {
			logger.error("暂存紧急出入库 - ", e);

			body.put("is_success", 1);
			body.put("message", "保存失败!");
		}

		return body;
	}

	/**
	 * 暂存单据
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param mapColl
	 * @param user
	 * @return
	 */
	public JSONObject subOrder(JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();
		boolean isAdd = false; // 新增or修改

		try {
			// 主数据处理
			if (!json.containsKey("urgence_id")) {
				isAdd = true;
			}
			BizUrgenceHead bizUrgenceHead = saveToMstData(isAdd, json, user.getUserId());

			// 修改与新增处理
			if (!isAdd) {
				commonService.removeReceiptUser(bizUrgenceHead.getUrgenceId(), bizUrgenceHead.getReceiptType());
				bizUrgenceItemMapper.deleteByUrgenceId(bizUrgenceHead.getUrgenceId());
			}

			JSONArray userList = json.getJSONArray("user_list");
			for (int i = 0; i < userList.size(); i++) {

				JSONObject juser = userList.getJSONObject(i);
				commonService.addReceiptUser(bizUrgenceHead.getUrgenceId(), bizUrgenceHead.getReceiptType(),
						juser.getString("user_id"), juser.getInt("role_id"));

			}

			JSONArray fileList = json.getJSONArray("file_list");

			if (fileList != null && fileList.size() > 0) {
				bizReceiptAttachmentDao.deleteByReceiptId(bizUrgenceHead.getUrgenceId());
				for (int i = 0; i < fileList.size(); i++) {

					JSONObject jfile = fileList.getJSONObject(i);

					BizReceiptAttachment bizReceiptAttachment = new BizReceiptAttachment();
					bizReceiptAttachment.setReceiptId(bizUrgenceHead.getUrgenceId());
					bizReceiptAttachment.setReceiptType(bizUrgenceHead.getReceiptType());
					bizReceiptAttachment.setUserId(user.getUserId());
					bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", jfile));
					bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", jfile));
					bizReceiptAttachment.setSize(jfile.getInt("file_size"));
					String ext = UtilString.STRING_EMPTY;
					int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
					if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
						ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
					}

					bizReceiptAttachment.setExt(ext);

					bizReceiptAttachmentDao.insertSelective(bizReceiptAttachment);

				}
			}

			// 明细处理
			JSONArray dtlList = json.getJSONArray("item_list");
			for (int i = 0; i < dtlList.size(); i++) {
				JSONObject dtl = dtlList.getJSONObject(i);
				dtl.put("urgence_id", bizUrgenceHead.getUrgenceId());
				saveToDtlData(dtl, user.getUserId(), 2);
			}
			commonService.initRoleWf(bizUrgenceHead.getUrgenceId(), bizUrgenceHead.getReceiptType(),
					bizUrgenceHead.getUrgenceCode(), user.getUserId());
			body.put("is_success", 0);
			body.put("message", bizUrgenceHead.getUrgenceCode());

		} catch (Exception e) {
			logger.error("暂存紧急出入库 - ", e);

			body.put("is_success", 1);
			body.put("message", "保存失败!");
		}

		return body;
	}

	/**
	 * 暂存单据
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @param mapColl
	 * @param user
	 * @return
	 */
	public JSONObject subOrderApp(JSONObject json, CurrentUser user) {

		JSONObject body = new JSONObject();

		try {
			// 主数据处理

			String urgenceId = json.getString("urgence_id");
			String receiptType = json.getString("receipt_type");
			String status = json.getString("status");

			if (json.containsKey("file_list")) {
				JSONArray fileList = json.getJSONArray("file_list");

				if (fileList != null && fileList.size() > 0) {
					bizReceiptAttachmentDao.deleteByReceiptId(Integer.parseInt(urgenceId));
					for (int i = 0; i < fileList.size(); i++) {

						JSONObject jfile = fileList.getJSONObject(i);

						BizReceiptAttachment bizReceiptAttachment = new BizReceiptAttachment();
						bizReceiptAttachment.setReceiptId(Integer.parseInt(urgenceId));
						bizReceiptAttachment.setReceiptType((byte) Integer.parseInt(receiptType));
						bizReceiptAttachment.setUserId(user.getUserId());
						bizReceiptAttachment.setFileId(UtilJSON.getStringOrEmptyFromJSON("file_id", jfile));
						bizReceiptAttachment.setFileName(UtilJSON.getStringOrEmptyFromJSON("file_name", jfile));
						bizReceiptAttachment.setSize(Integer.parseInt(jfile.getString("file_size")));
						String ext = UtilString.STRING_EMPTY;
						int extIndex = bizReceiptAttachment.getFileName().lastIndexOf('.');
						if (bizReceiptAttachment.getFileName().length() - extIndex < 10) {
							ext = bizReceiptAttachment.getFileName().substring(extIndex + 1);
						}

						bizReceiptAttachment.setExt(ext);

						bizReceiptAttachmentDao.insertSelective(bizReceiptAttachment);

					}
				}

			}

			if (json.containsKey("user_list")) {
				if ("1".equals(status)) {

					JSONArray userList = json.getJSONArray("user_list");
					if (userList != null && userList.size() > 0) {

						commonService.removeReceiptUser(Integer.parseInt(urgenceId), Integer.parseInt(receiptType));
						for (int i = 0; i < userList.size(); i++) {

							JSONObject juser = userList.getJSONObject(i);
							commonService.addReceiptUser(Integer.parseInt(urgenceId), Integer.parseInt(receiptType),
									juser.getString("user_id"), Integer.parseInt(juser.getString("role_id")));

						}
					}

				}
			}

			body.put("is_success", 0);
			body.put("message", "提交成功");

		} catch (Exception e) {
			logger.error("提交紧急出入库 - ", e);

			body.put("is_success", 1);
			body.put("message", "提交失败!");
		}

		return body;
	}

	@Override
	public JSONObject deleteByUUID(Map<String, Object> params) {
		String re = "";
		JSONObject body = new JSONObject();

		// re =
		// RESTUtil.executeGetStringTimeOut(PropertiesUtil.getInstance().getFileServerUrl()+"remove?file_id="+
		// param.get("uuid"), 30);
		try {
			int i = bizReceiptAttachmentDao.deleteByUUID(params);
			body.put("is_success", 0);
			body.put("message", i + "个文件删除成功");
		} catch (Exception e) {
			logger.error("删除文件- ", e);
			body.put("is_success", 1);
			body.put("message", "删除文件失败!");
		}
		return body;
	}

	/**
	 * 保存抬头数据
	 * 
	 * @date 2017年11月21日
	 * @author sangjl
	 * @throws Exception
	 */
	private BizUrgenceHead saveToMstData(boolean isAdd, JSONObject mst, String userId) throws Exception {
		BizUrgenceHead head = new BizUrgenceHead();

		head.setReceiptType((byte) mst.getInt("receipt_type"));
		head.setFtyId(mst.getInt("fty_id"));
		head.setLocationId(mst.getInt("location_id"));
		head.setDemandDept(mst.getString("demand_dept"));
		head.setDemandPerson(mst.getString("demand_person"));
		head.setRemark(mst.getString("remark"));
		head.setModifyUser(userId);
		if (mst.getString("create_time") != null && !"".equals(mst.getString("create_time"))) {
			head.setCreateTime(UtilString.getDateForString(mst.getString("create_time")));
		} else {
			head.setCreateTime(new Date());
		}

		Integer id = 0;
		if (isAdd) {
			head.setCreateUser(userId);
			String urgenceCode = commonService.getNextReceiptCode(EnumSequence.URGENCE.getValue());
			head.setUrgenceCode(urgenceCode);
			bizUrgenceHeadMapper.insertSelective(head);
			id = head.getUrgenceId();
		} else {
			id = mst.getInt("urgence_id");
			head.setUrgenceId(id);
			head.setModifyTime(new Date());
			bizUrgenceHeadMapper.updateById(head);
		}

		return bizUrgenceHeadMapper.selectByPrimaryKey(id);
	}

	/**
	 * 保存明细数据
	 * 
	 * @date 2017年11月21日
	 * @author 桑九龙
	 */
	private int saveToDtlData(JSONObject dtl, String userId, int state) {
		BizUrgenceItem bizUrgenceItem = new BizUrgenceItem();

		bizUrgenceItem.setUrgenceId(dtl.getInt("urgence_id"));
		bizUrgenceItem.setUrgenceRid(dtl.getInt("urgence_rid"));
		bizUrgenceItem.setMatId(dtl.getInt("mat_id"));
		bizUrgenceItem.setMatName(dtl.getString("mat_name"));
		bizUrgenceItem.setUnitId(dtl.getInt("unit_id"));
		bizUrgenceItem.setUnitCode(dtl.getString("unit_code"));
		bizUrgenceItem.setQty(getBigDecimal(dtl.getString("qty")));

		bizUrgenceItem.setAreaId(dtl.getInt("area_id"));
		bizUrgenceItem.setAreaCode(dtl.getString("area_code"));
		bizUrgenceItem.setPositionId(dtl.getInt("position_id"));
		bizUrgenceItem.setPositionCode(dtl.getString("position_code"));
		bizUrgenceItem.setStatus((byte) state);

		bizUrgenceItem.setCreateUser(userId);
		bizUrgenceItem.setModifyUser(userId);

		return bizUrgenceItemMapper.insertSelective(bizUrgenceItem);
	}

	/**
	 * 删除单据(假删除)
	 * 
	 * @author sangjl
	 * @param zdjbh
	 * @param user
	 */
	public JSONObject deleteOrder(Integer urgenceId, CurrentUser user) {
		JSONObject body = new JSONObject();
		try {

			BizUrgenceItem item = new BizUrgenceItem();
			item.setUrgenceId(urgenceId);
			item.setIsDelete((byte) 1);
			item.setModifyUser(user.getUserId());

			bizUrgenceItemMapper.deleteItem(item);

			BizUrgenceHead head = new BizUrgenceHead();
			head.setUrgenceId(urgenceId);
			head.setIsDelete((byte) 1);
			head.setModifyUser(user.getUserId());

			bizUrgenceHeadMapper.deleteHead(head);

			head = bizUrgenceHeadMapper.selectByPrimaryKey(urgenceId);

			commonService.removeReceiptUser(urgenceId, head.getReceiptType());

			bizReceiptAttachmentDao.deleteByReceiptId(urgenceId);

			body.put("is_success", 0);
			body.put("message", head.getUrgenceCode());

		} catch (Exception e) {
			logger.error("删除紧急出入库 - ", e);

			body.put("is_success", 1);
			body.put("message", "删除失败!");

		}

		return body;
	}

	/**
	 * 处理单据
	 * 
	 * @date 2017年11月21日
	 * @author 桑九龙
	 * @param mapColl
	 * @param user
	 * @return
	 */
	public JSONObject postingOrder(JSONObject json, CurrentUser user) {
		JSONObject body = new JSONObject();
		try {

			JSONArray list = json.getJSONArray("item_list");
			int count = 0;
			int urgenceId = 0;
			for (int i = 0; i < list.size(); i++) {
				JSONObject jobj = list.getJSONObject(i);
				BizUrgenceItem item = new BizUrgenceItem();
				if (urgenceId <= 0) {
					urgenceId = jobj.getInt("urgence_id");
				}
				item.setUrgenceId(jobj.getInt("urgence_id"));
				item.setUrgenceRid(jobj.getInt("urgence_rid"));
				item.setOperationDate(UtilString.getDateForString(jobj.getString("operation_date")));
				item.setOperator(user.getUserId());
				item.setRelateReceiptCode(jobj.getString("relate_receipt_code"));
				item.setRelateReceiptId(jobj.getInt("relate_receipt_id"));
				item.setStatus((byte) 3);
				item.setModifyUser(user.getUserId());

				count += bizUrgenceItemMapper.updateByUrgenceId(item);
			}
			BizUrgenceResVo bizUrgenceHead = bizUrgenceHeadMapper.selectHeadById(urgenceId);
			//启动审批
			commonService.initRoleWf(urgenceId, bizUrgenceHead.getReceiptType(), bizUrgenceHead.getUrgenceCode(),
					user.getUserId());
			body.put("is_success", 0);
			body.put("message", count);
		} catch (Exception e) {
			logger.error("处理紧急出入库 - ", e);

			body.put("is_success", 1);
			body.put("message", "删除失败!");

		}

		return body;
	}

	/**
	 * 获取关联单号
	 * 
	 * @date 2017年11月21日
	 * @author 桑九龙
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getInnerOrder(Map<String, Object> paramMap) {

		List<Map<String, Object>> list = null;

		if (paramMap.get("receiptType").equals("101")) {
			list = bizUrgenceHeadMapper.selectInOrder(paramMap);
		} else if (paramMap.get("receiptType").equals("102")) {
			list = bizUrgenceHeadMapper.selectOutOrder(paramMap);
		}

		return list;
	}

	public List<Map<String, Object>> getOutMat(Map<String, Object> paramMap) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> tmplist = bizUrgenceHeadMapper.getOutMat(paramMap);
		if (tmplist != null && tmplist.size() > 0) {
			return tmplist;
		} else {
			return list;
		}
	}

	public BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigDecimal.");
			}
		}
		return ret;
	}

	@Override
	public List<BizUrgenceResVo> listBizDocUrgenceHead(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocUrgenceHeadOnPaging(paramMap);
	}

	@Override
	public List<Integer> listBizDocUrgenceType(Map<String, Object> param) {

		List<BizUrgenceResVo> list = bizUrgenceHeadMapper.listBizDocUrgenceType(param);
		List<Integer> listI = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (BizUrgenceResVo vo : list) {
				listI.add(Integer.parseInt(vo.getReceiptType().toString()));
			}
		}

		return listI;
	}

	@Override
	public List<Map<String, Object>> getDocinstockList(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocInstockOnPaging(paramMap);
	}

	@Override
	public List<Integer> listBizDocInstockType(Map<String, Object> param) {

		List<Map<String, Object>> list = bizUrgenceHeadMapper.listBizDocInstockType(param);
		List<Integer> listI = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (Map map : list) {
				listI.add(Integer.parseInt(map.get("receiptType").toString()));
			}
		}
		return listI;
	}

	@Override
	public List<Map<String, Object>> getDocOutstockList(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocOutstockOnPaging(paramMap);
	}

	@Override
	public List<Integer> listBizDocOutstockType(Map<String, Object> param) {

		List<Map<String, Object>> list = bizUrgenceHeadMapper.listBizDocOutstockType(param);
		List<Integer> listI = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (Map map : list) {
				listI.add(Integer.parseInt(map.get("receiptType").toString()));
			}
		}
		return listI;
	}

	@Override
	public List<Map<String, Object>> getDocStockTaskList(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocTaskOnPaging(paramMap);
	}

	@Override
	public List<Map<String, Object>> getDocStockTaskReqList(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocTaskReqOnPaging(paramMap);
	}

	@Override
	public List<Map<String, Object>> getDocStockZlList(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocStockZlOnPaging(paramMap);
	}

	@Override
	public List<Map<String, Object>> listBizDocstockTransportOnPaging(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocstockTransportOnPaging(paramMap);
	}

	@Override
	public List<Integer> listBizDocTransportType(Map<String, Object> param) {

		List<Map<String, Object>> list = bizUrgenceHeadMapper.listBizDocTransportType(param);
		List<Integer> listI = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (Map map : list) {
				listI.add(Integer.parseInt(map.get("receiptType").toString()));
			}
		}
		return listI;
	}

	@Override
	public List<Map<String, Object>> listBizDocstockReturnOnPaging(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocstockReturnOnPaging(paramMap);
	}

	@Override
	public List<Integer> listBizDocReturnType(Map<String, Object> param) {

		List<Map<String, Object>> list = bizUrgenceHeadMapper.listBizDocReturnType(param);
		List<Integer> listI = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (Map map : list) {
				listI.add(Integer.parseInt(map.get("receiptType").toString()));
			}
		}
		return listI;
	}

	@Override
	public List<Map<String, Object>> listBizDocAllocateOnPaging(Map<String, Object> paramMap) {

		return bizUrgenceHeadMapper.listBizDocAllocateOnPaging(paramMap);
	}

}
