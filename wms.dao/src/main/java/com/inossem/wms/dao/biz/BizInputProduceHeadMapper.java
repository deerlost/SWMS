package com.inossem.wms.dao.biz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inossem.wms.model.biz.BizStockInputHead;
import com.inossem.wms.model.vo.BizPkgOperationHeadVo;

public interface BizInputProduceHeadMapper {

	List<Map<String, Object>> production_input_listOnPagIng(Map<String, Object> map);

	Map<String, Object> production_input_info(String stock_input_id);

	List<Map<String, Object>> selectItemList(String stock_input_id);

	List<Map<String, Object>> selectErpList(Map<String, Object> param);

	List<Map<String, Object>> selectPackageTypeList(String mat_id);
	
	Map<String, Object> selectPackageTypeListByP(Map<String, Object> param);

	List<Map<String, Object>> selectLocationList(Map<String, Object> param);

	List<Map<String,Object>> selectPkgList(Map<String, Object> param);

	List<Map<String, Object>> selectProduceLineList();

	Map<String, Object> getInputHeadMap(Map<String, Object> map);

	List<Map<String, Object>> getItemMap(Map<String, Object> map);

	List<Map<String, Object>> getPackageOperationlist(Map<String, Object> param);

	List<Map<String, Object>> getPackageList(Map<String, Object> map);

	Map<String,Object>  getUnitIdByCode(String string);

	Map<String, Object> getMatInfoByCode(String mat_code);

	List<Map<String, Object>> selectByStockTransportId(int stock_input_id);

	List<String> selectItemByInputId(Integer stockInputId);

	int selectPkgOrperationNum(String string);

	List<Map<String, Object>> getPkgPosition(String string);

	Map<String, Object> getErpBatchByBatch(Map<String,Object> map);

	List<String> selectItemIdByInputId(Integer stockInputId);

	String selectPackageId(String name);

	List<Map<String, Object>> selectPackageTypeListNoSn(String mat_id);

	//List<Map<String, Object>> selectAllErpList();

	List<Map<String, Object>> selectPackageTypeListNoSn();

	//List<Map<String, Object>> selectAllPackageTypeList();

	List<Map<String, Object>> selectFactoryList();

	Map<String, Object> select_mat_info(String mat_code);

	void updateStatusById(Integer pkgOperationId);

	void updateStatusByInputId(String stock_input_id);

	String selectbatchMaterial(Map<String, Object> param);

	String getMesCodeByMesLocationId(String mes_location_id);

	List<Map<String, Object>> getMesMatList(String item_id);

	List<String>  selectTranspotStatus(String stock_input_id);

	List<String> getPackageId(String id);

	void updateItemStatus(String stock_input_id);


    List<Map<String,Object>> selectFtyLineLocation(@Param("ftyCode") String ftyCode,@Param("productLineName") String productLineName);

	List<String> getPackageStanderd(String id);
    
}