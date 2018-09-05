package com.inossem.wms.web.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.inossem.wms.config.ServerConfig;
import com.inossem.wms.exception.InterfaceCallException;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.model.auth.CurrentUser;
import com.inossem.wms.model.enums.EnumErrorCode;
import com.inossem.wms.model.enums.EnumPage;
import com.inossem.wms.model.enums.EnumReceiptType;
import com.inossem.wms.model.vo.MatCodeVo;
import com.inossem.wms.service.biz.ICommonService;
import com.inossem.wms.service.biz.IStockInquireService;
import com.inossem.wms.service.dic.IDicWarehouseService;
import com.inossem.wms.util.UtilDateTime;
import com.inossem.wms.util.UtilExcel;
import com.inossem.wms.util.UtilJSONConvert;
import com.inossem.wms.util.UtilObject;
import com.inossem.wms.util.UtilResult;
import com.inossem.wms.util.UtilString;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Package com.inossem.wms.web.biz
 * @ClassName：StockInquireController
 * @Description : 查询
 * @anthor：王洋
 * @date：2018年6月11日 上午10:57:35 @版本：V1.0
 */
@Controller
public class StockInquireController {

    private final static Logger logger = LoggerFactory.getLogger(StockInquireController.class);

    @Autowired
    private ICommonService commonService;
    @Autowired
    private IStockInquireService stockInquireSerivce;
    @Autowired
    private ServerConfig constantConfig;
    @Autowired
    private IDicWarehouseService dicWarehouseService;

    /**
     * @param @param  cUser
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Title: getBaseInfoForUser </br>
     * @Description: TODO(这里用一句话描述这个方法的作用)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/baseinfo_byuser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject getBaseInfoForUser(CurrentUser cUser) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;

        JSONArray fty_list = new JSONArray();
        JSONObject body = new JSONObject();
        try {
            fty_list = commonService.listFtyLocationForUser(cUser.getUserId(), null);

            body.put("fty_list", fty_list);

        } catch (Exception e) {
            logger.error("===================获取工厂及仓库=======", e);
            error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
            status = false;
            // TODO: handle exception
        }

        return UtilResult.getResultToJson(status, error_code, body);
    }

    /**
     * @param @param  json
     * @param @return 设定文件</br>
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object> 返回类型</br>
     * @Description: TODO(分页)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: getParamMapToPageing </br>
     * @throws</br>
     */
    private Map<String, Object> getParamMapToPageing(JSONObject json) {
        Map<String, Object> param = new HashMap<String, Object>();

        int pageIndex = json.getInt("page_index");
        int pageSize = json.getInt("page_size");
        int totalCount = EnumPage.TOTAL_COUNT.getValue();
        boolean sortAscend = true;
        String sortColumn = "";

        if (json.containsKey("total")) {
            totalCount = json.getInt("total");
        }
        if (json.containsKey("sort_ascend")) {
            sortAscend = json.getBoolean("sort_ascend");
        }
        if (json.containsKey("sort_column")) {
            sortColumn = json.getString("sort_column");
        }

        param.put("totalCount", totalCount);
        param.put("paging", true);
        param.put("pageIndex", pageIndex);
        param.put("pageSize", pageSize);
        param.put("totalCount", totalCount);
        return param;
    }

    /**
     * @param @param  json
     * @param @param  cUser
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Description: TODO(出入库查询)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: QueryStockOutAndIn </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/serach_outandin_stock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryStockOutAndIn(@RequestBody JSONObject json, CurrentUser cUser) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        int total = 0;
        String msg = "";

        List<Map<String, Object>> stockList = new ArrayList<Map<String, Object>>();
        Long totalCountLong = (long) 0;

        String createTimeBegin = UtilObject.getStringOrNull(json.get("create_time_begin"));
        String createTimeEnd = UtilObject.getStringOrNull(json.get("create_time_end"));

        SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
        Map<String, Object> param = this.getParamMapToPageing(json);

        try {
            if (StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeBegin));// 创建日期起
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));// 创建日期止
            } else if (StringUtils.hasText(createTimeBegin) && !StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeBegin));
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeBegin), 1));
            } else if (!StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeEnd));
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));
            }
            param.put("userName", UtilObject.getStringOrNull(json.get("create_user")));// 创建人
            param.put("moveTypeCode", UtilObject.getStringOrNull(json.get("move_type_code")));// 移动类型
            param.put("ftyOutputId", UtilObject.getIntegerOrNull(json.get("fty_output_id")));// 工厂id
            param.put("locationOutputId", UtilObject.getIntegerOrNull(json.get("location_output_id")));// 库存地点id
            param.put("ftyInputId", UtilObject.getIntegerOrNull(json.get("fty_input_id")));// 工厂id
            param.put("locationInputId", UtilObject.getIntegerOrNull(json.get("location_input_id")));// 库存地点id
            param.put("matName", UtilObject.getStringOrNull(json.get("mat_name")));// 物料描述
            param.put("productBatch", UtilObject.getStringOrNull(json.get("product_batch")));// 生产批次
            param.put("matDocCode", UtilObject.getStringOrNull(json.get("mat_doc_code")));// 物料凭证
            param.put("erpBatch", UtilObject.getStringOrNull(json.get("erp_batch")));// 物料凭证
            String matCode = UtilObject.getStringOrNull(json.get("mat_code"));
            param.put("matCode", UtilObject.getStringOrNull(matCode));// 物料编码（判断用）
            List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
            param.put("utilMatCodes", utilMatCodes);// 物料编码集合
            param.put("referReceiptCode", UtilObject.getStringOrNull(json.get("stock_code")));// 单据编号
            param.put("biz_types", json.get("biz_types"));// 单据编号
            if (json.has("wh_input_id")) {
                param.put("whInputId", json.get("wh_input_id"));
            }
            if (json.has("wh_output_id")) {
                param.put("whOutputId", json.get("wh_output_id"));
            }
            JSONArray bizTypes = json.getJSONArray("biz_types");

            for (int i = 0; i < bizTypes.size(); i++) {
                int type = bizTypes.getInt(i);
                if (type == EnumReceiptType.STOCK_INPUT.getValue()) {
                    param.put("input", "input");
                }
                if (type == EnumReceiptType.STOCK_TRANSPORT.getValue()) {
                    param.put("trans", "trans");
                }
                if (type == EnumReceiptType.STOCK_OUTPUT.getValue()) {
                    param.put("output", "output");
                }
            }

            stockList = stockInquireSerivce.queryStockOutAndInOnPaging(param);
            if (stockList.size() > 0) {
                totalCountLong = (Long) stockList.get(0).get("totalCount");
            } else {
                totalCountLong = (long) 0;

            }
            total += totalCountLong.intValue();

            Collections.sort(stockList, new MapComparatorDesc());
            for (Map<String, Object> stock : stockList) {
                Integer biz_type = UtilObject.getIntegerOrNull(stock.get("biz_type"));

                JSONObject stockOutAndInMapsJSon = new JSONObject();
                stockOutAndInMapsJSon.put("biz_type_name", stock.get("biz_type"));// 业务类型
                stockOutAndInMapsJSon.put("mat_id", stock.get("mat_id"));// 物料id
                stockOutAndInMapsJSon.put("mat_code", stock.get("mat_code"));// 物料编码
                stockOutAndInMapsJSon.put("mat_name", stock.get("mat_name"));// 物料描述
                stockOutAndInMapsJSon.put("fty_output_name", stock.get("fty_output_name"));// 发出工厂描述
                stockOutAndInMapsJSon.put("fty_output_code", stock.get("fty_output_code"));// 发出工厂编码
                stockOutAndInMapsJSon.put("location_output_name", stock.get("location_output_name"));// 发出库存地点描述
                stockOutAndInMapsJSon.put("location_output_code", stock.get("location_output_code"));// 发出库存地点编码

                stockOutAndInMapsJSon.put("fty_input_name", stock.get("fty_input_name"));// 接收
                // 工厂描述
                stockOutAndInMapsJSon.put("fty_input_code", stock.get("fty_input_code"));// 接收工厂编码
                stockOutAndInMapsJSon.put("location_input_name", stock.get("location_input_name"));// 接收库存地点描述
                stockOutAndInMapsJSon.put("location_input_code", stock.get("location_input_code"));// 接收
                // 库存地点编码
                stockOutAndInMapsJSon.put("product_batch", stock.get("product_batch"));
                stockOutAndInMapsJSon.put("move_type_code", stock.get("move_type_code"));// 移动类型编码
                stockOutAndInMapsJSon.put("batch", stock.get("batch"));// 批次号
                stockOutAndInMapsJSon.put("erp_batch", stock.get("erp_batch"));// erp批次号
                stockOutAndInMapsJSon.put("mat_doc_code", stock.get("mat_doc_code"));// 物料凭证
                stockOutAndInMapsJSon.put("mat_doc_rid", stock.get("mat_doc_rid"));// 物料凭证行项目
                stockOutAndInMapsJSon.put("mat_doc_type_name",
                        EnumReceiptType.getNameByValue(UtilObject.getByteOrNull(stock.get("receipt_type"))));//
                // 仓储单据类型
                stockOutAndInMapsJSon.put("receipt_type", UtilObject.getByteOrNull(stock.get("receipt_type")));
                stockOutAndInMapsJSon.put("refer_receipt_id", stock.get("refer_receipt_id"));// 单据号id
                stockOutAndInMapsJSon.put("refer_receipt_code", stock.get("refer_receipt_code"));// 单据号
                stockOutAndInMapsJSon.put("refer_receipt_rid", stock.get("refer_receipt_rid"));// 单据行号
                stockOutAndInMapsJSon.put("name_zh", stock.get("name_zh"));// 计量单位
                stockOutAndInMapsJSon.put("qty", stock.get("qty"));// 数量
                stockOutAndInMapsJSon.put("kg_qty", stock.get("kg_qty"));// 批次号
                stockOutAndInMapsJSon.put("status", stock.get("status_name"));// 状态
                stockOutAndInMapsJSon.put("create_time", stock.get("create_time")
                        /*
                         * String.valueOf(stock.get("create_time")).trim().substring(0,
                         * 10)
                         */);
                stockOutAndInMapsJSon.put("mes_doc_code", stock.get("mes_doc_code"));
                stockOutAndInMapsJSon.put("mat_off_code", stock.get("mat_off_code"));
                stockOutAndInMapsJSon.put("mes_off_code", stock.get("mes_off_code"));
                stockOutAndInMapsJSon.put("user_name", stock.get("user_name"));// 创建人
                stockOutAndInMapsJSon.put("driver", stock.get("driver"));//
                stockOutAndInMapsJSon.put("vehicle", stock.get("vehicle"));//
                // Date date = (Date) stock.get("doc_time");
                // String d =UtilString.getShortStringForDate(date);
                stockOutAndInMapsJSon.put("doc_time", stock.get("doc_time"));
                stockOutAndInMapsJSon.put("posting_date", stock.get("posting_date"));
                stockOutAndInMapsJSon.put("headremark", stock.get("headremark"));
                stockOutAndInMapsJSon.put("itemremark", stock.get("itemremark"));

                body.add(stockOutAndInMapsJSon);
            }

        } catch (WMSException e) {
            error_code = e.getErrorCode();
            status = false;
            msg = e.getMessage();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("===========出入库查询异常===========", e);
            error_code = EnumErrorCode.ERROR_CODE_EXCEPTION.getValue();
            status = false;
        }

        return UtilResult.getResultToJson(status, error_code, msg, UtilObject.getIntOrZero(param.get("pageIndex")),
                UtilObject.getIntOrZero(param.get("pageSize")), total, body);
    }

    /**
     * @Description :按创建时间降序
     * @Package com.inossem.wms.web.biz
     * @ClassName：MapComparatorDesc
     * @anthor：王洋
     * @date：2018年5月31日 下午2:54:11 @版本：V1.0
     */
    static class MapComparatorDesc implements Comparator<Map<String, Object>> {

        @Override
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            // TODO Auto-generated method stub

            String s1 = UtilObject.getStringOrNull(o1.get("create_time")).trim().replaceAll("-", "");
            String s2 = UtilObject.getStringOrNull(o2.get("create_time")).trim().replaceAll("-", "");
            Integer date1 = Integer.valueOf(s1);
            Integer date2 = Integer.valueOf(s2);
            if (date2 != null) {
                return date2.compareTo(date1);
            }
            return 0;

        }

    }

    /**
     * @param @param  cUser
     * @param @param  json
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Description: TODO(库龄查询)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: serachStockDays </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/serach_days_stock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject serachStockDays(CurrentUser cUser, @RequestBody JSONObject json) {

        int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
        String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
        boolean errorStatus = false;
        int total = 0;
        Long totalsun = (long) 0;
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        JSONArray body = new JSONArray();
        Map<String, Object> param = this.getParamMapToPageing(json);
        try {
            param.put("dateBegin", UtilObject.getStringOrNull(json.get("date_begin")));// 库龄开始（天）
            param.put("dateEnd", UtilObject.getStringOrNull(json.get("date_end")));// 库龄结束（天）
            param.put("checkDate", UtilObject.getStringOrNull(json.get("check_date"))); // 查询日期
            String ftyId = UtilObject.getStringOrNull(json.get("fty_id"));
            param.put("ftyId", ftyId);// 工厂id
            param.put("locationId", UtilObject.getIntegerOrNull(json.get("location_id")));// 库存地点id
            //
            if (json.has("wh_id")) {
                param.put("whId", UtilObject.getIntegerOrNull(json.get("wh_id")));
            }
            List<Map<String, Object>> stockDaysMaps = stockInquireSerivce.queryStockDaysOnPaging(param);

            if (stockDaysMaps != null && stockDaysMaps.size() > 0) {
                totalsun = Long.parseLong(stockDaysMaps.get(0).get("totalCount").toString());
                total = totalsun.intValue();

                //if (UtilString.isNullOrEmpty(ftyId) == false) {
                Map<String, Object> map = new HashMap<String, Object>();
                Map<String, Object> mapres = new HashMap<String, Object>();
                for (Map<String, Object> m : stockDaysMaps) {
                    map.put("ftyId", UtilObject.getIntegerOrNull(m.get("fty_id")));
                    map.put("matId", UtilObject.getIntegerOrNull(m.get("mat_id")));

                    mapres = stockInquireSerivce.queryPrice(map);
                    if (mapres != null && mapres.size() > 0) {
                        BigDecimal price = UtilObject.getBigDecimalOrZero(mapres.get("price"));
                        m.put("price", price);
                    } else {
                        m.put("price", "");
                    }

                    mapList.add(m);
                }
                //}
            }

            for (Map<String, Object> stockDaysMap : mapList) {
                JSONObject stockDaysMapsJSon = new JSONObject();

                BigDecimal qty = UtilObject.getBigDecimalOrZero(stockDaysMap.get("qty"));
                BigDecimal price = UtilObject.getBigDecimalOrZero(stockDaysMap.get("price"));
                stockDaysMapsJSon.put("check_date", stockDaysMap.get("check_date"));// 查询日期
                stockDaysMapsJSon.put("mat_id", stockDaysMap.get("mat_id"));// 物料id
                stockDaysMapsJSon.put("mat_code", stockDaysMap.get("mat_code"));// 物料编码
                stockDaysMapsJSon.put("mat_name", stockDaysMap.get("mat_name"));// 物料描述
                stockDaysMapsJSon.put("name_zh", stockDaysMap.get("name_zh"));// 计量单位
                stockDaysMapsJSon.put("fty_code", stockDaysMap.get("fty_code"));// 工厂编码
                stockDaysMapsJSon.put("fty_name", stockDaysMap.get("fty_name"));// 工厂描述
                stockDaysMapsJSon.put("location_code", stockDaysMap.get("location_code"));// 库存地点编码
                stockDaysMapsJSon.put("location_name", stockDaysMap.get("location_name"));// 库存地点描述
                stockDaysMapsJSon.put("product_batch", stockDaysMap.get("product_batch"));// 批次号
                stockDaysMapsJSon.put("input_date", stockDaysMap.get("in_date"));// 入库时间
                stockDaysMapsJSon.put("stcok_days", stockDaysMap.get("stcok_days"));// 库龄（天）
                stockDaysMapsJSon.put("qty", stockDaysMap.get("qty"));// 库存数量
                stockDaysMapsJSon.put("move_average_sum", price);// 移动平均价
                stockDaysMapsJSon.put("stock_sum", price.multiply(qty));// 库存金额
                body.add(stockDaysMapsJSon);

            }
            errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
            errorStatus = true;
            errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
        } catch (InterfaceCallException e) {
            errorCode = e.getErrorCode();
            errorString = e.getMessage();
            errorStatus = false;
            // logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }

        return UtilResult.getResultToJson(errorStatus, errorCode, UtilObject.getIntOrZero(param.get("pageIndex")),
                UtilObject.getIntOrZero(param.get("pageSize")), total, body);
    }

    /**
     * @param @param  request
     * @param @param  response
     * @param @throws IOException 设定文件</br>
     * @return void 返回类型</br>
     * @Description: TODO(库龄批量导出)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: downloadOutAndInStock </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/download_stock_days", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void downloadStockDays(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedOutputStream out = null;
        InputStream bis = null;
        File fileInServer = null;
        int pageIndex = EnumPage.PAGE_INDEX.getValue();
        int pageSize = EnumPage.PAGE_SIZE.getValue();
        int total = EnumPage.TOTAL_COUNT.getValue();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> param = new HashMap<String, Object>();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        try {
            param.put("pageIndex", pageIndex);
            param.put("pageSize", pageSize);
            param.put("totalCount", total);
            param.put("dateBegin", request.getParameter("date_begin"));// 库龄开始（天）
            param.put("dateEnd", request.getParameter("date_end"));// 库龄结束（天）
            param.put("checkDate", request.getParameter("check_date")); // 查询日期
            String ftyId = request.getParameter("fty_id");
            param.put("ftyId", ftyId);// 工厂id
            param.put("locationId", request.getParameter("location_id"));// 库存地点id
            param.put("whId", request.getParameter("wh_id"));
            List<Map<String, Object>> stockDaysMaps = stockInquireSerivce.queryStockDaysOnPaging(param);

            if (stockDaysMaps != null && stockDaysMaps.size() > 0) {
                // total =
                // Integer.parseInt(stockDaysMaps.get(0).get("totalCount").toString());
                if (UtilString.isNullOrEmpty(ftyId) == false) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> mapres = new HashMap<String, Object>();
                    for (Map<String, Object> m : stockDaysMaps) {
                        map.put("ftyId", ftyId);
                        map.put("matId", UtilObject.getIntegerOrNull(m.get("mat_id")));

                        mapres = stockInquireSerivce.queryPrice(map);
                        BigDecimal price = UtilObject.getBigDecimalOrZero(mapres.get("price"));
                        m.put("price", price);
                        mapList.add(m);
                    }
                }
            }

            for (Map<String, Object> stockDaysMap : mapList) {
                JSONObject stockDaysMapsJSon = new JSONObject();

                BigDecimal qty = UtilObject.getBigDecimalOrZero(stockDaysMap.get("qty"));
                BigDecimal price = UtilObject.getBigDecimalOrZero(stockDaysMap.get("price"));
                stockDaysMapsJSon.put("check_date", stockDaysMap.get("check_date"));// 查询日期
                stockDaysMapsJSon.put("mat_id", stockDaysMap.get("mat_id"));// 物料id
                stockDaysMapsJSon.put("mat_code", stockDaysMap.get("mat_code"));// 物料编码
                stockDaysMapsJSon.put("mat_name", stockDaysMap.get("mat_name"));// 物料描述
                stockDaysMapsJSon.put("name_zh", stockDaysMap.get("name_zh"));// 计量单位
                stockDaysMapsJSon.put("fty_code", stockDaysMap.get("fty_code"));// 工厂编码
                stockDaysMapsJSon.put("fty_name", stockDaysMap.get("fty_name"));// 工厂描述
                stockDaysMapsJSon.put("location_code", stockDaysMap.get("location_code"));// 库存地点编码
                stockDaysMapsJSon.put("location_name", stockDaysMap.get("location_name"));// 库存地点描述
                stockDaysMapsJSon.put("batch", stockDaysMap.get("batch"));// 批次号
                stockDaysMapsJSon.put("input_date", stockDaysMap.get("in_date"));// 入库时间
                stockDaysMapsJSon.put("stcok_days", stockDaysMap.get("stcok_days"));// 库龄（天）
                stockDaysMapsJSon.put("qty", stockDaysMap.get("qty"));// 库存数量
                stockDaysMapsJSon.put("move_average_sum", price);// 移动平均价
                stockDaysMapsJSon.put("stock_sum", price.multiply(qty));// 库存金额
                list.add(stockDaysMapsJSon);

            }
            // 下载
            Map<String, String> relationMap = new HashMap<String, String>();

            relationMap.put("check_date", "查询日期");
            relationMap.put("mat_code", "物料编码");
            relationMap.put("mat_name", "物料描述");
            relationMap.put("name_zh", "计量单位");
            relationMap.put("fty_code", "工厂");
            relationMap.put("location_code", "库存地点");
            relationMap.put("product_batch", "生产批次");
            relationMap.put("input_date", "入库时间");
            relationMap.put("stcok_days", "库龄(天)");
            relationMap.put("qty", "库存数量");
            relationMap.put("move_average_sum", "移动平均价");
            relationMap.put("stock_sum", "库存金额");

            List<String> orderList = new ArrayList<String>();
            orderList.add("查询日期");
            orderList.add("物料编码");
            orderList.add("物料描述");
            orderList.add("计量单位");
            orderList.add("工厂");
            orderList.add("库存地点");
            orderList.add("生产批次");
            orderList.add("入库时间");
            orderList.add("库龄(天)");
            orderList.add("库存数量");
            orderList.add("移动平均价");
            orderList.add("库存金额");

            String root = constantConfig.getTempFilePath();

            String download_file_name = "overstock";// 库存积压

            String filePath = UtilExcel.getExcelRopertUrl(download_file_name, list, relationMap, orderList, root);

            fileInServer = new File(filePath);
            // 获取输入流
            bis = new BufferedInputStream(new FileInputStream(fileInServer));

            // 转码，免得文件名中文乱码
            String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
            // 设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
        } catch (Exception e) {
            logger.error("--------------------------库龄导出-----------------", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    /**
     * @param @param  request
     * @param @param  response
     * @param @throws IOException 设定文件</br>
     * @return void 返回类型</br>
     * @Description: TODO(出入库批量导出)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: downloadOutAndInStock </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/download_outandin_stock", method = RequestMethod.GET, produces = "multipart/form-data")
    public void downloadOutAndInStock(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int pageIndex = EnumPage.PAGE_INDEX.getValue();
        int pageSize = EnumPage.PAGE_SIZE.getValue();
        int total = EnumPage.TOTAL_COUNT.getValue();
        BufferedOutputStream out = null;
        InputStream bis = null;
        File fileInServer = null;

        List<Map<String, Object>> stockList = new ArrayList<Map<String, Object>>();

        String createTimeBegin = request.getParameter("create_time_begin");
        String createTimeEnd = request.getParameter("create_time_end");

        SimpleDateFormat clsFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> param = new HashMap<String, Object>();

        try {
            param.put("pageIndex", pageIndex);
            param.put("pageSize", pageSize);
            param.put("totalCount", total);
            if (StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeBegin));// 创建日期起
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));// 创建日期止
            } else if (StringUtils.hasText(createTimeBegin) && !StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeBegin));
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeBegin), 1));
            } else if (!StringUtils.hasText(createTimeBegin) && StringUtils.hasText(createTimeEnd)) {
                param.put("createTimeBegin", clsFormat.parse(createTimeEnd));
                param.put("createTimeEnd", UtilDateTime.getDate(clsFormat.parse(createTimeEnd), 1));
            }
            param.put("userName", request.getParameter("create_user"));// 创建人
            param.put("moveTypeCode", request.getParameter("move_type_code"));// 移动类型
            param.put("ftyOutputId", request.getParameter("fty_output_id"));// 工厂id
            param.put("locationOutputId", request.getParameter("location_output_id"));// 库存地点id
            param.put("ftyInputId", request.getParameter("fty_input_id"));// 工厂id
            param.put("locationInputId", request.getParameter("location_input_id"));// 库存地点id
            param.put("matName", request.getParameter("mat_name"));// 物料描述
            param.put("productBatch", request.getParameter("product_batch"));// 生产批次
            param.put("matDocCode", request.getParameter("mat_doc_code"));// 物料凭证
            param.put("whInputId", request.getParameter("wh_input_id"));
            param.put("whOutputId", request.getParameter("wh_output_id"));
            String matCode = request.getParameter("mat_code");
            param.put("matCode", UtilObject.getStringOrNull(matCode));// 物料编码（判断用）
            String erp_batch = request.getParameter("erp_batch");
            param.put("erpBatch", UtilObject.getStringOrNull(erp_batch));// 物料凭证

            List<MatCodeVo> utilMatCodes = UtilString.cutOutMatCode(matCode);// 物料编码类，begin区间开始的物料编码、end区间结束的物料编码、strings单个或者多个物料编码数组
            param.put("utilMatCodes", utilMatCodes);// 物料编码集合
            param.put("referReceiptCode", request.getParameter("stock_code"));// 单据编号

            String bizTypes = request.getParameter("biz_types");
            String[] bizType = new String[3];
            int num = 0;
            for (String type : bizTypes.split(",")) {
                if (type == null && type.equals("")) {
                    continue;
                }
                bizType[num] = type;
                num++;
            }
            JSONArray bizArray = JSONArray.fromObject(bizType);
            param.put("biz_types", bizArray);
            for (int i = 0; i < bizType.length; i++) {
                int type = -1;
                type = (null == bizType[i] ? -1 : Integer.parseInt(bizType[i]));
                if (type == EnumReceiptType.STOCK_INPUT.getValue()) {
                    param.put("input", "input");
                }
                if (type == EnumReceiptType.STOCK_TRANSPORT.getValue()) {
                    param.put("trans", "trans");
                }
                if (type == EnumReceiptType.STOCK_OUTPUT.getValue()) {
                    param.put("output", "output");
                }
            }

            stockList = stockInquireSerivce.queryStockOutAndInOnPaging(param);

            Collections.sort(stockList, new MapComparatorDesc());
            for (Map<String, Object> stock : stockList) {
                Integer biz_type = UtilObject.getIntegerOrNull(stock.get("biz_type"));

                JSONObject stockOutAndInMapsJSon = new JSONObject();
                stockOutAndInMapsJSon.put("biz_type_name", stock.get("biz_type"));// 业务类型
                stockOutAndInMapsJSon.put("mat_id", stock.get("mat_id"));// 物料id
                stockOutAndInMapsJSon.put("mat_code", stock.get("mat_code"));// 物料编码
                stockOutAndInMapsJSon.put("mat_name", stock.get("mat_name"));// 物料描述
                stockOutAndInMapsJSon.put("fty_output_name", stock.get("fty_output_name"));// 发出工厂描述
                stockOutAndInMapsJSon.put("fty_output_code", stock.get("fty_output_code"));// 发出工厂编码
                stockOutAndInMapsJSon.put("location_output_name", stock.get("location_output_name"));// 发出库存地点描述
                stockOutAndInMapsJSon.put("location_output_code", stock.get("location_output_code"));// 发出库存地点编码

                stockOutAndInMapsJSon.put("fty_input_name", stock.get("fty_input_name"));// 接收
                // 工厂描述
                stockOutAndInMapsJSon.put("fty_input_code", stock.get("fty_input_code"));// 接收工厂编码
                stockOutAndInMapsJSon.put("location_input_name", stock.get("location_input_name"));// 接收库存地点描述
                stockOutAndInMapsJSon.put("location_input_code", stock.get("location_input_code"));// 接收
                // 库存地点编码
                stockOutAndInMapsJSon.put("product_batch", stock.get("product_batch"));
                stockOutAndInMapsJSon.put("move_type_code", stock.get("move_type_code"));// 移动类型编码
                stockOutAndInMapsJSon.put("batch", stock.get("batch"));// 批次号
                stockOutAndInMapsJSon.put("erp_batch", stock.get("erp_batch"));// erp批次号
                stockOutAndInMapsJSon.put("mat_doc_code", stock.get("mat_doc_code"));// 物料凭证
                stockOutAndInMapsJSon.put("mat_doc_rid", stock.get("mat_doc_rid"));// 物料凭证行项目
                stockOutAndInMapsJSon.put("mat_doc_type_name",
                        EnumReceiptType.getNameByValue(UtilObject.getByteOrNull(stock.get("receipt_type"))));//
                // 仓储单据类型
                stockOutAndInMapsJSon.put("receipt_type", UtilObject.getByteOrNull(stock.get("receipt_type")));
                stockOutAndInMapsJSon.put("refer_receipt_id", stock.get("refer_receipt_id"));// 单据号id
                stockOutAndInMapsJSon.put("refer_receipt_code", stock.get("refer_receipt_code"));// 单据号
                stockOutAndInMapsJSon.put("refer_receipt_rid", stock.get("refer_receipt_rid"));// 单据行号
                stockOutAndInMapsJSon.put("name_zh", stock.get("name_zh"));// 计量单位
                stockOutAndInMapsJSon.put("qty", stock.get("qty"));// 数量
                stockOutAndInMapsJSon.put("kg_qty", stock.get("kg_qty"));// 批次号
                stockOutAndInMapsJSon.put("status", stock.get("status_name"));// 状态
                stockOutAndInMapsJSon.put("create_time", stock.get("create_time")
                        /*
                         * String.valueOf(stock.get("create_time")).trim().substring(0,
                         * 10)
                         */);
                stockOutAndInMapsJSon.put("mes_doc_code", stock.get("mes_doc_code"));
                stockOutAndInMapsJSon.put("mat_off_code", stock.get("mat_off_code"));
                stockOutAndInMapsJSon.put("mes_off_code", stock.get("mes_off_code"));
                stockOutAndInMapsJSon.put("user_name", stock.get("user_name"));// 创建人
                stockOutAndInMapsJSon.put("driver", stock.get("driver"));//
                stockOutAndInMapsJSon.put("vehicle", stock.get("vehicle"));//
                // Date date = (Date) stock.get("doc_time");
                // String d =UtilString.getShortStringForDate(date);
                stockOutAndInMapsJSon.put("doc_time", stock.get("doc_time"));
                stockOutAndInMapsJSon.put("posting_date", stock.get("posting_date"));
                stockOutAndInMapsJSon.put("headremark", stock.get("headremark"));
                stockOutAndInMapsJSon.put("itemremark", stock.get("itemremark"));

                list.add(stockOutAndInMapsJSon);
            }

            // 下载
            Map<String, String> relationMap = new HashMap<String, String>();

            relationMap.put("biz_type_name", "业务类型");
            relationMap.put("mat_code", "物料编码");
            relationMap.put("mat_name", "物料描述");
            relationMap.put("fty_output_name", "发出工厂");
            relationMap.put("location_output_name", "发出库存地点");
            relationMap.put("fty_input_name", "接收工厂");
            relationMap.put("location_input_name", "接收库存地点");
            relationMap.put("product_batch", "生产批次");
            relationMap.put("erp_batch", "ERP批次");
            relationMap.put("mat_doc_type_name", "仓储单据类型");
            relationMap.put("refer_receipt_code", "单据号");
            relationMap.put("refer_receipt_rid", "单据行号");
            relationMap.put("qty", "数量");
            relationMap.put("name_zh", "计量单位");
            relationMap.put("kg_qty", "公斤数量");
            relationMap.put("status", "状态");
            relationMap.put("create_time", "创建日期");
            relationMap.put("posting_date", "过账日期");
            relationMap.put("mat_doc_code", "正向ERP凭证");
            relationMap.put("mes_doc_code", "正向MES凭证");
            relationMap.put("mat_off_code", "逆向ERP凭证");
            relationMap.put("mes_off_code", "逆向MES凭证");
            relationMap.put("user_name", "创建人");
            relationMap.put("driver", "司机");
            relationMap.put("vehicle", "车辆");
            relationMap.put("headremark", "抬头备注");
            relationMap.put("itemremark", "行项目备注");

            List<String> orderList = new ArrayList<String>();
            orderList.add("业务类型");
            orderList.add("物料编码");
            orderList.add("物料描述");
            orderList.add("发出工厂");
            orderList.add("发出库存地点");
            orderList.add("接收工厂");
            orderList.add("接收库存地点");
            orderList.add("生产批次");
            orderList.add("ERP批次");
            orderList.add("仓储单据类型");
            orderList.add("单据号");
            orderList.add("单据行号");
            orderList.add("数量");
            orderList.add("计量单位");
            orderList.add("公斤数量");
            orderList.add("状态");
            orderList.add("创建日期");
            orderList.add("过账日期");
            orderList.add("正向ERP凭证");
            orderList.add("正向MES凭证");
            orderList.add("逆向ERP凭证");
            orderList.add("逆向MES凭证");
            orderList.add("创建人");
            orderList.add("司机");
            orderList.add("车辆");
            orderList.add("抬头备注");
            orderList.add("行项目备注");

            String root = constantConfig.getTempFilePath();

            String download_file_name = "outandinstock";//

            String filePath = UtilExcel.getExcelRopertUrl(download_file_name, list, relationMap, orderList, root);

            fileInServer = new File(filePath);
            // 获取输入流
            bis = new BufferedInputStream(new FileInputStream(fileInServer));

            // 转码，免得文件名中文乱码
            String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
            // 设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("---------------出入库导出----------", e);
        }

    }

    /**
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Description: TODO(库存结构分析web)</   br>
     * @Title: queryLocPrice </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/query_loc_price", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryLocPrice(@RequestBody JSONObject json) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        String msg = "";
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        //统计年度
        if (json.containsKey("year")) {
            if (null != json.get("year") && !json.get("year").equals("")) {
                params.put("beginDate", String.format("%s-%s", json.getString("year"), "01-01"));
                params.put("endDate", String.format("%s-%s", json.getString("year"), "12-31"));
            } else {
            }
        }
        //查询某个城市的库存情况
        if (json.containsKey("city_id")) {
            if (null == json.get("city_id") || json.get("city_id").equals("")) {
                params.put("cityId", "");
            } else {
                params.put("cityId", json.getInt("city_id"));
            }
        }
        if (json.containsKey("board_id")) {
            if (null == json.get("board_id") || json.get("board_id").equals("")) {
            } else {
                params.put("boardId", json.getInt("board_id"));
            }
        }
        if (json.containsKey("corp_id")) {
            if (null == json.get("corp_id") || json.get("corp_id").equals("")) {
            } else {
                params.put("corpId", json.getInt("corp_id"));
            }
        }
        if (json.containsKey("wh_id")) {
            if (null == json.get("wh_id") || json.get("wh_id").equals("")) {
            } else {
                params.put("whId", json.getInt("wh_id"));
            }
        }
        if (json.containsKey("type")) {
            if (null == json.get("type") || json.get("type").equals("")) {
                params.put("type", "order by wh_price desc");
            } else {
                params.put("type", String.format("order by %s desc", json.getString("type")));
            }
        } else {
            params.put("type", "order by wh_price desc");
        }
        try {
            body = stockInquireSerivce.queryLocPrice(params);
        } catch (WMSException e) {
            error_code = e.getErrorCode();
            msg = e.getMessage();
            status = false;
        }catch (Exception e) {
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
            logger.error("--------------=============-库存结构分析查询失败==============", e);
        }

        return UtilResult.getResultToJson(status, error_code,msg, body);
    }

    @RequestMapping(value = "/biz/stquery/query_for_city/{city_id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    JSONObject queryLocPriceForCity(@PathVariable int city_id) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();


        try {
            body = stockInquireSerivce.queryLocPrice(params);
        } catch (Exception e) {
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
            logger.error("--------------=============-库存结构分析查询失败==============", e);
        }

        return UtilResult.getResultToJson(status, error_code, body);
    }


    /**
     * @param @param  request
     * @param @param  response
     * @param @throws IOException 设定文件</br>
     * @return void 返回类型</br>order by %s desc
     * @Description: TODO(库存结构分析导出)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: downloadLocPrice </br>
     * @throws</br>
     */
    @RequestMapping(value = "/biz/stquery/download_locprice", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public void downloadLocPrice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String whId = request.getParameter("wh_id");
        //   String locationId = request.getParameter("location_id");
        List<Map<String, Object>> stockBatchMaps = new ArrayList<Map<String, Object>>();
        Map<String, Object> params = new HashMap<String, Object>();
        BufferedOutputStream out = null;
        InputStream bis = null;
        File fileInServer = null;
        if (null != whId && whId != "") {
            params.put("whId", whId.trim());
        }
      /*  if (null != locationId && locationId != "") {
            params.put("locationId", locationId.trim());
        }*/
        try {
            stockBatchMaps = stockInquireSerivce.downloadLocPrice(params);

            // 字段对应汉语意思
            Map<String, String> relationMap = new HashMap<String, String>();
            relationMap.put("mat_group_code", "物料组");
            relationMap.put("mat_code", "物料编码");
            relationMap.put("mat_name", "物料描述");
            relationMap.put("package_type_code", "包装类型");
            relationMap.put("package_standard_ch", "包装规格");
            relationMap.put("stock_qty", "库存数量");
            relationMap.put("erp_batch", "ERP批次");
            relationMap.put("product_batch", "生产批次");
            relationMap.put("quality_batch", "质检批次");
            relationMap.put("batch", "批次号");
            relationMap.put("fty_name", "工厂描述");
            relationMap.put("location_name", "库存地点描述");
            relationMap.put("unit_name", "计量单位");
            relationMap.put("input_date", "入库时间");
            relationMap.put("stock_type_name", "库存类型");

            // 表格排序顺序
            List<String> orderList = new ArrayList<String>();
            orderList.add("物料组");
            orderList.add("物料编码");
            orderList.add("物料描述");
            orderList.add("包装类型");
            orderList.add("包装规格");
            orderList.add("库存数量");
            orderList.add("计量单位");
            orderList.add("工厂描述");
            orderList.add("库存地点描述");
            orderList.add("ERP批次");
            orderList.add("生产批次");
            orderList.add("质检批次");
            orderList.add("批次号");
            orderList.add("入库时间");
            orderList.add("库存类型");

            String root = constantConfig.getTempFilePath();

            String download_file_name = "locationprice";// 批次库存查询

            String filePath = UtilExcel.getExcelRopertUrl(download_file_name, stockBatchMaps, relationMap, orderList,
                    root);

            fileInServer = new File(filePath);
            // 获取输入流
            bis = new BufferedInputStream(new FileInputStream(fileInServer));

            // 转码，免得文件名中文乱码
            String fileNameForUTF8 = URLEncoder.encode(download_file_name + ".xls", "UTF-8");
            // 设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileNameForUTF8);

            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
        } catch (Exception e) {
            logger.error("==============", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * @param @param  json
     * @param @return 设定文件</br>
     * @return JSONObject 返回类型</br>
     * @Description: TODO(mes历史记录查询)</                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               br>
     * @Title: queryFailMES </br>
     * @throws</br>
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/biz/emsquery/query_fail_mes", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryFailMES(@RequestBody JSONObject json) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        Map<String, Object> param = this.getParamMapToPageing(json);
        List<Map<String, Object>> body = new ArrayList<Map<String, Object>>();
        int total = 0;
        String msg = "";
        param.put("referReceiptCode", UtilObject.getStringOrEmpty(json.get("refer_receipt_code")).trim());
        param.put("createUser", UtilObject.getStringOrEmpty(json.get("create_user"))
                .trim());
        param.put("createTime", UtilObject.getStringOrEmpty(json.get("create_time")).trim());
        try {
            body = stockInquireSerivce.queryFailMES(param);
            total = Integer.parseInt(body.get(0).get("totalCount").toString());
        } catch (WMSException e) {
            error_code = e.getErrorCode();
            status = false;
            msg = e.getMessage();
        } catch (Exception e) {
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
            logger.error("--------------=============-查询失败==============", e);
        }

        return UtilResult.getResultToJson(status, error_code, msg, UtilObject.getIntOrZero(param.get("pageIndex")),
                UtilObject.getIntOrZero(param.get("pageSize")), total, body);
    }

    @RequestMapping(value = "/biz/stquery/get_warehouse_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject getWarehouseList(CurrentUser cUser) {

        int errorCode = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
        String errorString = EnumErrorCode.ERROR_CODE_FAILURE.getName();
        boolean errorStatus = false;
        List<Map<String, Object>> wareHouseList = new ArrayList<Map<String, Object>>();
        try {
            wareHouseList = dicWarehouseService.queryWarehouseList();
            errorCode = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
            errorStatus = true;
            errorString = EnumErrorCode.ERROR_CODE_SUCESS.getName();
        } catch (Exception e) {
            logger.error(UtilString.STRING_EMPTY, e);
        }

        JSONObject object = UtilResult.getResultToJson(errorStatus, errorCode, errorString, wareHouseList);

        UtilJSONConvert.deleteNull(object);

        return object;
    }

    @RequestMapping(value = "/biz/stquery/get_base_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryBaseInfo(CurrentUser cUser) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;

        JSONObject body = new JSONObject();

        try {
            body = stockInquireSerivce.queryBaseInfo(cUser);
        } catch (Exception e) {
            logger.error("============", e);
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
        }

        JSONObject obj = UtilResult.getResultToJson(status, error_code, body);
        UtilJSONConvert.setNullToEmpty(obj);
        return obj;
    }

    @RequestMapping(value = "/biz/stquery/query_loc_price_portable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    JSONObject queryLocPricePor(@RequestBody JSONObject json) {
        int error_code = EnumErrorCode.ERROR_CODE_SUCESS.getValue();
        boolean status = true;
        String msg = "";
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();

        SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        //统计年度月份
        params.put("beginDate", String.format("%s-%s", sdate.format(date), "01"));
        params.put("endDate", String.format("%s-%s", sdate.format(date), "31"));
       /* if (json.containsKey("year")) {
            if (null != json.get("year") && !json.get("year").equals("")) {
                params.put("beginDate", String.format("%s-%s", json.getString("year"), "01-01"));
                params.put("endDate", String.format("%s-%s", json.getString("year"), "12-31"));
            } else {
            }
        }*/
        //查询全国的库存情况
        if (json.containsKey("countries")) {
        }
        //查询某个城市的库存情况
        if (json.containsKey("city_id")) {
            if (null == json.get("city_id") || json.get("city_id").equals("")) {
                params.put("cityId", "");
            } else {
                params.put("cityId", json.getInt("city_id"));
            }
        }
        if (json.containsKey("board_id")) {
            if (null == json.get("board_id") || json.get("board_id").equals("")) {
            } else {
                params.put("boardId", json.getInt("board_id"));
            }
        }
        if (json.containsKey("corp_id")) {
            if (null == json.get("corp_id") || json.get("corp_id").equals("")) {
            } else {
                params.put("corpId", json.getInt("corp_id"));
            }
        }
        if (json.containsKey("type")) {
            if (null == json.get("type") || json.get("type").equals("")) {
                params.put("type", "order by wh_price desc");
            } else {
                params.put("type", String.format("order by %s desc", json.getString("type")));
            }
        } else {
            params.put("type", "order by wh_price desc");
        }
        if (json.containsKey("wh_id")) {
            if (null == json.get("wh_id") || json.get("wh_id").equals("")) {
            } else {
                params.put("whId", json.getInt("wh_id"));
            }
        }
        params.put("pda", "PAD");
        try {
            body = stockInquireSerivce.queryLocPrice(params);
        } catch (WMSException e) {
            error_code = e.getErrorCode();
            msg = e.getMessage();
            status = false;
        } catch (Exception e) {
            error_code = EnumErrorCode.ERROR_CODE_FAILURE.getValue();
            status = false;
            logger.error("--------------=============-库存结构分析查询失败==============", e);
        }

        return UtilResult.getResultToJson(status, error_code,msg,body);
    }

    public JSONObject analysisStock() {
        return null;
    }

}
