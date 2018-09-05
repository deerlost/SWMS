package com.inossem.wms.service.dic;

import java.util.List;
import java.util.Map;

import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.dic.DicCorp;

public interface IDicCorpService {

	/**
	 * 查询所有公司
	 * 
	 * @return
	 */
	List<DicCorp> getAllCorpList();

	/**
	 * 保存公司
	 * 
	 * @param isAdd
	 * @param t001Info
	 * @return
	 */
	int saveCorp(boolean isAdd, DicCorp corpInfo);

	/**
	 * 根据公司代码查找公司
	 * 
	 * @param Bukrs
	 * @return
	 */
	DicCorp getCorpById(int id);

	/**
	 * 根据公司代码查找公司
	 * 
	 * @param Bukrs
	 * @return
	 */
	DicCorp getCorpByCode(String code);

	/**
	 * 查询所有公司id和公司描述
	 * 
	 * @author 刘宇 2018.02.28
	 * @return
	 */
	List<DicCorp> listAllCorpIdAndCorpNameOfCorp();

	/**
	 * 公司分页查询
	 * 
	 * @author 刘宇 2018.03.01
	 * @param condition
	 * @param pageIndex
	 * @param pageSize
	 * @param total
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> listCorpOnPaging(String condition, int pageIndex, int pageSize, int total,
			boolean sortAscend, String sortColumn) throws Exception;

	/**
	 * 添加和修改公司
	 * 
	 * @author 刘宇 2018.03.02
	 * @param errorString
	 * @param errorStatus
	 * @param corpId
	 * @param corpCode
	 * @param corpName
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> addOrUpdateCorp(int errorCode,String errorString, boolean errorStatus, String corpId, String corpCode,
			String corpName, String boardId) throws Exception;

	Map<String, Object> getCorpById(Integer corpId, CurrentUser user);

}
