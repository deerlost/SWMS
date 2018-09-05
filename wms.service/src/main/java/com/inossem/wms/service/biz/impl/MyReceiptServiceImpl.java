package com.inossem.wms.service.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.auth.UserMapper;
import com.inossem.wms.dao.biz.BizMatReqHeadMapper;
import com.inossem.wms.dao.biz.BizMatReqItemMapper;
import com.inossem.wms.dao.wf.FlowableMapper;
import com.inossem.wms.dao.wf.WfReceiptUserMapper;
import com.inossem.wms.model.biz.BizMatReqHead;
import com.inossem.wms.model.dic.DicMoveType;
import com.inossem.wms.model.dic.DicReceiptType;
import com.inossem.wms.model.enums.EnumMatReqStatus;
import com.inossem.wms.model.enums.EnumReceiptApproveStatus;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.ApproveUserMatReqVo;
import com.inossem.wms.model.vo.BizMatReqHeadVo;
import com.inossem.wms.model.vo.BizMatReqItemVo;
import com.inossem.wms.model.wf.WfReceiptUser;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IDictionaryService;
import com.inossem.wms.service.biz.IMatReqService;
import com.inossem.wms.service.biz.IMyReceiptService;
import com.inossem.wms.service.intfc.IMatReq;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("myReceiptService")
public class MyReceiptServiceImpl implements IMyReceiptService {

	@Autowired
	private FlowableMapper flowableDao;

	@Autowired
	private WfReceiptUserMapper wfReceiptUserDao;

	@Autowired
	private IMatReq matReq;

	@Autowired
	private IMatReqService matReqService;

	@Autowired
	private BizMatReqHeadMapper bizMatReqHeadDao;

	@Autowired
	private IDictionaryService dictionaryService;

	@Autowired
	private BizMatReqItemMapper bizMatReqItemDao;

	@Autowired
	private UserMapper userDao;

	/*@Autowired
	private TaskService taskService;*/

	/*@Autowired
	private RuntimeService runtimeService;*/

	@Autowired
	private ICommonService commonService;

	/**
	 * 根据userId获取非领料单的所有单据类型
	 */
	@Override
	public List<DicReceiptType> getApproveTaskType(String userId) {
		return flowableDao.selectApproveTaskTypeByUserID(userId);
	}

	/**
	 * 非领料单未审批任务
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<ApproveUserMatReqVo> getApproveTasks(Map<String, Object> map) {
		return flowableDao.selectApproveTasksByUserIDOnPaging(map);
	}

	/**
	 * 非领料单已审批任务
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<ApproveUserMatReqVo> getApprovedTasks(Map<String, Object> map) {
		return flowableDao.selectApprovedTasksByUserIDOnPaging(map);
	}

	/**
	 * 领料单未审批任务
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<ApproveUserMatReqVo> getMatReqApproveTasks(Map<String, Object> map) {
		return flowableDao.selectMatReqApproveTasksByUserIDOnPaging(map);
	}

	/**
	 * 领料单已审批任务
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public List<ApproveUserMatReqVo> getMatReqApprovedTasks(Map<String, Object> map) {
		return flowableDao.selectMatReqApprovedTasksByUserIDOnPaging(map);
	}

	@Override
	// 最后审批人审批
	public String lastTaskApprove(int receiptId, String processInstanceId, String approvePerson, String deptCode,
			String deptName, JSONArray itemList, String comment, String url) throws Exception {
		// 审批通过,则需要添加 使用部门 移动类型 成本对象
		BizMatReqHeadVo bizMatReqHead = matReqService.getMatReqHead(receiptId,
				EnumReceiptType.MAT_REQ_PRODUCTION.getValue());

		// 使用部门添加
		bizMatReqHead.setDeptCode(deptCode);
		bizMatReqHead.setDeptName(deptName);
		// 保存使用部门 内部订单
		bizMatReqHeadDao.updateForLastTask(bizMatReqHead);
		List<BizMatReqItemVo> list = matReqService.getMatReqItemList(receiptId);

		List<Map<String, Object>> mapListForCheckWorkReceiptCode = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapForCheckWorkReceiptCode;
		if (list.size() == itemList.size()) {
			// 最后审批添加 移动类型 成本对象
			for (Object object : itemList) {
				JSONObject itemObj = (JSONObject) object;
				mapForCheckWorkReceiptCode = new HashMap<String, Object>();
				int moveTypeId = itemObj.getInt("move_type_id"); // '移动类型描述',
				DicMoveType dicMoveType = dictionaryService.getMoveTypeIdMap().get(moveTypeId);
				// String moveType =
				// itemObj.getString("move_type");// '移动类型',
				// String moveTypeName =
				// itemObj.getString("move_type_name"); // '移动类型描述',
				// String specStock =
				// itemObj.getString("spec_stock"); // '特殊库存标识',
				String costObjCode = itemObj.getString("cost_obj_code");// 成本对象编码
				String costObjName = itemObj.getString("cost_obj_name");// 成本对象描述
				int receiptRid = itemObj.getInt("receipt_rid");// 领料单行号

				BizMatReqItemVo itemVo = list.stream()
						.filter(itemTmp -> itemTmp.getMatReqId() == receiptId && itemTmp.getMatReqRid() == receiptRid)
						.findFirst().get();
				itemVo.setMoveTypeId(moveTypeId);
				itemVo.setMoveTypeCode(dicMoveType.getMoveTypeCode());
				itemVo.setMoveTypeName(dicMoveType.getMoveTypeName());
				itemVo.setSpecStock(dicMoveType.getSpecStock());
				itemVo.setCostObjCode(costObjCode);
				itemVo.setCostObjName(costObjName);
				if (itemObj.containsKey("general_ledger_subject")) {
					itemVo.setGeneralLedgerSubject(itemObj.getString("general_ledger_subject"));
				}
				bizMatReqItemDao.updateForLastTask(itemVo);

				mapForCheckWorkReceiptCode.put("mat_req_rid", String.valueOf(itemVo.getMatReqRid()));
				mapForCheckWorkReceiptCode.put("mat_code", itemVo.getMatCode());
				mapForCheckWorkReceiptCode.put("mat_req_fty_code",
						dictionaryService.getFtyCodeByFtyId(bizMatReqHead.getMatReqFtyId()));
				mapForCheckWorkReceiptCode.put("work_receipt_code", itemVo.getWorkReceiptCode());
				mapListForCheckWorkReceiptCode.add(mapForCheckWorkReceiptCode);
			}

			// 审批通过
			int result = this.taskApprove(receiptId, processInstanceId, approvePerson, true, comment, url);
			// 肯定是最后一个审批人,没有判断返回值,直接修改的状态
			bizMatReqHead.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_APPROVED.getValue());
			bizMatReqHeadDao.updateStatus(bizMatReqHead);

			return null;
		} else {
			return "物料数量不一致";
		}
	}

	@Override
	public int taskApprove(int receiptId, String processInstanceId, String approvePerson, boolean approve,
			String comment, String url) {
		BizMatReqHead bizMatReqHead = bizMatReqHeadDao.selectByPrimaryKey(receiptId);
		// 没有该领料单单
		if (bizMatReqHead == null) {
			return -1;
		}

		// RuntimeService runtimeService = null;

		// ProcessEngine processEngine =
		// ProcessEngines.getDefaultProcessEngine();

		// runtimeService = processEngine.getRuntimeService();
		// TaskService taskService = processEngine.getTaskService();

		// 获取当前任务
		/*Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskCandidateUser(approvePerson)
				.singleResult();*/
		if (true) {
			throw new NullPointerException("没有这个任务");
		} else {
			// logger.info(String.format("task id : %s,task ProcessInstanceId ：
			// %s", task.getId(),
			// task.getProcessInstanceId()));

			//String eid = task.getExecutionId();

			//runtimeService.setVariable(eid, "approve", approve);

			String commentID = null;
			if (comment != null && comment.trim().length() > 0) {
				// 添加批注信息, comment为批注内容
				//Comment cmt = taskService.addComment(task.getId(), processInstanceId, comment);
				/*if (cmt != null) {
					commentID = cmt.getId();
				}*/
			}
			// 任务完成
		//taskService.complete(task.getId());

			WfReceiptUser wfReceiptUser = new WfReceiptUser();
			wfReceiptUser.setUserId(approvePerson);
			wfReceiptUser.setReceiptType(EnumReceiptType.MAT_REQ_PRODUCTION.getValue());
			wfReceiptUser.setReceiptId(receiptId);
			//wfReceiptUser.setTaskId(task.getId());
			// 修改任务ID
			if (commentID != null) {
				wfReceiptUser.setCommentId(commentID);
			}
			if (approve) {
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_APPROVE.getValue());
			} else {
				wfReceiptUser.setApprove(EnumReceiptApproveStatus.RECEIPT_STATUS_REJECT.getValue());
			}
			wfReceiptUser.setApproveTime(new Date());
			wfReceiptUserDao.insertSelective(wfReceiptUser);

			// 返回值
			int ret;

			BizMatReqHead bizMatReqHeadTmp = new BizMatReqHead();
			bizMatReqHeadTmp.setMatReqId(receiptId);
			if (approve) {
				/*ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
						.singleResult();*/
				/*if (pi == null) {
					bizMatReqHeadTmp.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_APPROVED.getValue());
					bizMatReqHeadDao.updateStatus(bizMatReqHeadTmp);
					ret = EnumMatReqStatus.MAT_REQ_STATUS_APPROVED.getValue();
					this.pushSetTodoDone(bizMatReqHead.getMatReqCode(), bizMatReqHead.getPiid());
				} else {
					ret = EnumMatReqStatus.MAT_REQ_STATUS_DRAFT.getValue();
					this.pushSetTodoDone(bizMatReqHead.getMatReqCode(), bizMatReqHead.getPiid());
					this.pushSendTodo(bizMatReqHead.getMatReqCode(), bizMatReqHead.getPiid(), url);
				}*/
			} else {
				bizMatReqHeadTmp.setStatus(EnumMatReqStatus.MAT_REQ_STATUS_REJECT.getValue());
				bizMatReqHeadDao.updateStatus(bizMatReqHeadTmp);
				ret = EnumMatReqStatus.MAT_REQ_STATUS_REJECT.getValue();
				this.pushSetTodoDone(bizMatReqHead.getMatReqCode(), bizMatReqHead.getPiid());
			}
			return 0;
		}
	}

	private void pushSendTodo(String matReqCode, String piid, String url) {

		List<String> spEmployerNumberlist = flowableDao.selectApproveUserByPiid(piid);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("modelId", matReqCode); // 领料单号
		param.put("modelName", "领料单审批");// 模块名
		param.put("subject", "领料单审批"); // 标题
		param.put("link", url);// 路径
		param.put("type", 1); // 固定 1
		JSONArray jsonList = new JSONArray(); // 人员信息 json
		for (String user_id : spEmployerNumberlist) {
			JSONObject js = new JSONObject();
			js.put("PersonNo", user_id);
			jsonList.add(js);
		}

		param.put("targets", jsonList.toString());
		commonService.oaPushSendTodo(param);
	}

	private void pushSetTodoDone(String matReqCode, String piid) {

		List<String> spEmployerNumberlist = flowableDao.selectApproveUserByPiid(piid);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("modelId", matReqCode); // 领料单号
		param.put("modelName", "领料单审批");// 模块名
		param.put("subject", "领料单审批"); // 标题
		param.put("link", "");// 路径
		param.put("type", 1); // 固定 1
		JSONArray jsonList = new JSONArray(); // 人员信息 json
		for (String user_id : spEmployerNumberlist) {
			JSONObject js = new JSONObject();
			js.put("PersonNo", user_id);
			jsonList.add(js);
		}

		param.put("targets", jsonList.toString());
		commonService.oaPushSetTodoDone(param);
	}
}
