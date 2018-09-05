package com.inossem.wms.service.biz;

import java.util.Map;

public interface IInAndOutService {

	/**
	 * 
	 * @param boardId
	 * @param ftyId
	 * @param whId
	 * @param type  1 金额  2 数量
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOverview(String boardId, String corpId, String whId, int type ,String date) throws Exception;
	
	public Map<String, Object> getInfo(String boardId, String corpId, String whId, int type,Integer mounth ,String date)throws Exception;
	
	public Map<String, Object> getMatDetial(String boardId, String corpId, String whId, int type ,String date) throws Exception;
	
	public Map<String, Object> getWhDetial(String boardId, String corpId, String whId, int type ,String date) throws Exception;
	
}
