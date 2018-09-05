package com.inossem.wms.service.dic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.dao.dic.DicCorpMapper;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicBoard;
import com.inossem.wms.model.dic.DicCorp;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.service.dic.IDicCorpService;
import com.inossem.wms.util.UtilString;

@Transactional
@Service("dicCorpServiceImpl")
public class DicCorpServiceImpl implements IDicCorpService {
	// private static Logger logger =
	// LoggerFactory.getLogger(DicCorpServiceImpl.class);

	@Autowired
	private DicCorpMapper dicCorpDao;

	@Autowired
	private DicBoardMapper dicBoardMapper;
	/**
	 * 查询所有公司
	 * 
	 * @return
	 */
	@Override
	public List<DicCorp> getAllCorpList() {
		return dicCorpDao.selectAllCorpList();
	}

	/**
	 * 保存公司
	 * 
	 * @param isAdd
	 * @param corp
	 * @return
	 */
	@Override
	public int saveCorp(boolean isAdd, DicCorp corp) {
		if (isAdd) {
			return dicCorpDao.insertSelective(corp);
		} else {
			return dicCorpDao.updateByPrimaryKey(corp);
		}
	}

	/**
	 * 根据公司代码查找公司
	 * 
	 * @param Bukrs
	 * @return
	 */
	@Override
	public DicCorp getCorpById(int id) {
		return dicCorpDao.selectByPrimaryKey(id);
	}

	/**
	 * 根据公司代码查找公司
	 * 
	 * @param Bukrs
	 * @return
	 */
	@Override
	public DicCorp getCorpByCode(String corpCode) {
		return dicCorpDao.selectByCode(corpCode);
	}

	@Override
	public List<DicCorp> listAllCorpIdAndCorpNameOfCorp() {

		return dicCorpDao.selectAllCorpIdAndCorpNameOfCorpList();
	}

	@Override
	public List<Map<String, Object>> listCorpOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("condition", condition);// 查询条件
		map.put("pageSize", pageSize);
		map.put("pageIndex", pageIndex);
		map.put("paging", true);
		map.put("sortAscend", sortAscend);
		map.put("sortColumn", sortColumn);
		
	  List<DicBoard> boardList=	dicBoardMapper.selectAllBoard();
	  List<Map<String, Object>> result=  dicCorpDao.selectCorpOnPaging(map);
	  for (Map<String, Object> mapIn : result) {
		  mapIn.put("board_list", boardList);
	   }
		return result; 
	}

	@Override
	public Map<String, Object> addOrUpdateCorp(int errorCode, String errorString, boolean errorStatus, String corpId,
			String corpCode, String corpName, String boardId) throws Exception {

		DicCorp isUpdateOrAddObj = null;
		if (UtilString.isNullOrEmpty(corpId) || UtilString.isNullOrEmpty(corpCode) || UtilString.isNullOrEmpty(corpName)
				|| UtilString.isNullOrEmpty(boardId)) {
			errorStatus = false;
			errorString = "不可以有空值";
		} else {
			isUpdateOrAddObj = getCorpByCode(corpCode);
			if (isUpdateOrAddObj == null) {
				if (UtilString.getIntForString(corpId) == 0) {

					DicCorp obj = new DicCorp();
					obj.setCorpCode(corpCode); // 公司编码
					obj.setCorpName(corpName); // 公司描述
					obj.setCityId(0); // 城市id
					obj.setBoardId(UtilString.getIntForString(boardId)); // 板块id
					dicCorpDao.insertSelective(obj);
				} else {
					DicCorp obj = new DicCorp();
					obj.setCorpId(UtilString.getIntForString(corpId));// 公司id
					obj.setCorpCode(corpCode); // 公司编码
					obj.setCorpName(corpName); // 公司描述
					obj.setCityId(0); // 城市id
					obj.setBoardId(UtilString.getIntForString(boardId)); // 板块id
					dicCorpDao.updateByPrimaryKeySelective(obj);
					errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
				}
			} else {
				if (UtilString.getIntForString(corpId) == 0) {
					errorStatus = false;
					errorString = "已经存在该公司代码";
				} else {
					if (isUpdateOrAddObj.getCorpId().intValue() == UtilString.getIntForString(corpId)) {
						DicCorp obj = new DicCorp();
						obj.setCorpId(UtilString.getIntForString(corpId));// 公司id
						obj.setCorpCode(corpCode); // 公司编码
						obj.setCorpName(corpName); // 公司描述
						obj.setCityId(0); // 城市id
						obj.setBoardId(UtilString.getIntForString(boardId)); // 板块id
						dicCorpDao.updateByPrimaryKeySelective(obj);
						errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
					} else {
						errorStatus = false;
						errorString = "已经存在该公司代码";
					}
				}
			}
		}

		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("errorCode", errorCode);
		errorMap.put("errorStatus", errorStatus);
		errorMap.put("errorString", errorString);
		return errorMap;
	}

	@Override
	public Map<String, Object> getCorpById(Integer corpId, CurrentUser user) {

		Map<String, Object> body = new HashMap<String, Object>();
		try {
			DicCorp dw = null;

			dw = dicCorpDao.selectByPrimaryKey(corpId);
			if (null == dw) {
				dw = new DicCorp();
			}

			body.put("corp_id", dw.getCorpId());
			body.put("corp_code", dw.getCorpCode());
			body.put("corp_name", dw.getCorpName());
			body.put("board_id", dw.getBoardId());

		} catch (Exception e) {

		}

		return body;

	}

}
