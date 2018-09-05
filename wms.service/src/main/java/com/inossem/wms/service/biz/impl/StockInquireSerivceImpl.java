package com.inossem.wms.service.biz.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.inossem.wms.dao.dic.DicBoardMapper;
import com.inossem.wms.dao.dic.DicCityMapper;
import com.inossem.wms.dao.dic.DicCorpMapper;
import com.inossem.wms.dao.dic.DicWarehouseMapper;
import com.inossem.wms.model.auth.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.inossem.wms.dao.biz.BizStockInputItemMapper;
import com.inossem.wms.dao.biz.BizStockInputTransportItemMapper;
import com.inossem.wms.dao.biz.BizStockOutputItemMapper;
import com.inossem.wms.dao.biz.BizStockTransportItemCwMapper;
import com.inossem.wms.dao.stock.StockBatchMapper;
import com.inossem.wms.exception.WMSException;
import com.inossem.wms.service.biz.IStockInquireService;
import com.inossem.wms.util.UtilObject;
import com.mysql.fabric.xmlrpc.base.Array;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("stockInquireSerivce")
public class StockInquireSerivceImpl implements IStockInquireService {

    @Resource
    private DicBoardMapper boardDao;

    @Resource
    private CommonServiceImpl commonService;

    @Resource
    private DicCorpMapper corpDao;

    @Autowired
    private DicWarehouseMapper dicWarehouseDao;

    @Resource
    private BizStockInputTransportItemMapper stockTransDao;

    @Resource
    private BizStockInputItemMapper stockInputDao;

    @Resource
    private BizStockOutputItemMapper stockOutputDao;

    @Autowired
    private StockBatchMapper stockBatchDao;

    @Autowired
    private BizStockTransportItemCwMapper stockTransCwDao;

    @Override
    public List<Map<String, Object>> queryInputStockOutAndInOnPaging(Map<String, Object> param) {
        return stockInputDao.queryInputStockOutAndInOnPaging(param);
    }

    @Override
    public List<Map<String, Object>> queryTransStockOutAndInOnPaging(Map<String, Object> param) {
        return stockTransDao.queryTransStockOutAndInOnPaging(param);
    }

    @Override
    public List<Map<String, Object>> queryTransStockCwOutAndInOnPaging(Map<String, Object> param) {
        return stockTransCwDao.queryTransStockCwOutAndInOnPaging(param);
    }

    @Override
    public List<Map<String, Object>> queryOutputStockOutAndInOnPaging(Map<String, Object> param) {
        return stockOutputDao.queryOutputStockOutAndInOnPaging(param);
    }

    @Override
    public List<Map<String, Object>> queryStockDaysOnPaging(Map<String, Object> param) {
        return stockBatchDao.queryStockDaysOnPaging(param);
    }

    @Override
    public List<Map<String, Object>> getNewMaps(JSONObject re, List<Map<String, Object>> stockDaysMaps)
            throws Exception {
        if (re.getString("RETURNVALUE").equals("S")) {
            if (re.get("map") != null) {

                @SuppressWarnings("unchecked")
                Map<String, BigDecimal> map = (Map<String, BigDecimal>) re.get("map");

                for (int i = 0; i < stockDaysMaps.size(); i++) {
                    String key = stockDaysMaps.get(i).get("mat_code") + "-" + stockDaysMaps.get(i).get("fty_code");
                    String qty = stockDaysMaps.get(i).get("qty") == null ? ""
                            : stockDaysMaps.get(i).get("qty").toString();
                    BigDecimal verpr = new BigDecimal(0);// 移动平均价
                    if ("".equals(map.get(key)) || map.get(key) == null) {
                        verpr = new BigDecimal(0);
                    } else {
                        verpr = map.get(key);
                    }

                    stockDaysMaps.get(i).put("move_average_sum", verpr);// 移动平均价
                    stockDaysMaps.get(i).put("stock_sum", verpr.multiply(new BigDecimal(qty)).doubleValue());// 库存金额
                }
            }
        }
        return stockDaysMaps;
    }

    @Override
    public Map<String, Object> queryPrice(Map<String, Object> param) {
        return stockTransDao.queryPrice(param);
    }

    @Override
    public Map<String, Object> queryLocPrice(Map<String, Object> param) {
        List<Map<String, Object>> whLocList = stockBatchDao.queryLocPrice(param);
        List<Map<String, Object>> locList = stockBatchDao.queryLocPriceForCity(param);
        if (whLocList.isEmpty() || locList.isEmpty()) {
            throw new WMSException("没有数据");
        }
        List<Map<String, Object>> cityNum = stockBatchDao.queryCityNumberForLocPrice(param);
        int locNum = stockBatchDao.queryLocNumberForLocPrice(param);
        List<Map<String, Object>> whLocPriList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> LocPriList = new ArrayList<Map<String, Object>>();

        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> countOther = new HashMap<String, Object>();

        // DecimalFormat otherFormat = new DecimalFormat("#.###%");
        int count = 0;
        double other_price = 0.0;
        double other_qty = 0.0;
        NumberFormat nt = NumberFormat.getPercentInstance();
        // 设置百分数精确度4即保留两位小数
        nt.setMinimumFractionDigits(2);
        BigDecimal sumPrice = new BigDecimal(0); // 总金额
        BigDecimal price = new BigDecimal(0);// 各存库金额
        BigDecimal otherPrice = new BigDecimal(0);// 存库金额
        BigDecimal otherQty = new BigDecimal(0);// 存库数量
        BigDecimal sumQty = new BigDecimal(0);//总数量
        BigDecimal qty = new BigDecimal(0);//各存库金额数量
        for (Map<String, Object> map : whLocList) {
            price = UtilObject.getBigDecimalOrZero(map.get("wh_price")).divide(new BigDecimal(10000));//万元
            sumPrice = sumPrice.add(price);
            qty = UtilObject.getBigDecimalOrZero(map.get("qty")).divide(new BigDecimal(1000));//吨
            sumQty = sumQty.add(qty);
        }

        for (Map<String, Object> resultmap : whLocList) {
            Map<String, Object> whLocMap = new HashMap<String, Object>();
            count++;
            price = UtilObject.getBigDecimalOrZero(resultmap.get("wh_price")).divide(new BigDecimal(10000));
            qty = UtilObject.getBigDecimalOrZero(resultmap.get("qty")).divide(new BigDecimal(1000));//吨
            if (count < 5) {
                //   whLocMap.put("wh_loc", String.valueOf(resultmap.get("wh_code")) + " " + resultmap.get("location_code"));
                whLocMap.put("wh_name", String.valueOf(resultmap.get("wh_code")) + "-" + resultmap.get("wh_name"));
                whLocMap.put("wh_price", price);
                whLocMap.put("qty", qty);
                whLocMap.put("wh_id", resultmap.get("wh_id"));
                whLocMap.put("price_percentage", nt.format(price.divide(sumPrice, BigDecimal.ROUND_HALF_UP).doubleValue()));
                whLocMap.put("qty_percentage", nt.format(qty.divide(sumQty, BigDecimal.ROUND_HALF_UP).doubleValue()));
                whLocPriList.add(whLocMap);
            } else {
                other_price += price.divide(sumPrice, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                otherPrice = price.add(otherPrice);
                other_qty += qty.divide(sumPrice, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                otherQty = qty.add(otherQty);
            }
        }
        countOther.put("price_percentage", nt.format(other_price));
        countOther.put("wh_price", otherPrice);
        countOther.put("qty_percentage", nt.format(other_qty));
        countOther.put("qty", otherQty);
        countOther.put("wh_name", "其他");
        whLocPriList.add(countOther);
        for (Map<String, Object> resultmap : locList) {
            Map<String, Object> whLocMap = new HashMap<String, Object>();
            price = UtilObject.getBigDecimalOrZero(resultmap.get("wh_price")).divide(new BigDecimal(10000));
            qty = UtilObject.getBigDecimalOrZero(resultmap.get("qty")).divide(new BigDecimal(1000));//吨
            //   whLocMap.put("wh_loc", String.valueOf(resultmap.get("wh_code")) + " " + resultmap.get("location_code"));
            whLocMap.put("loc_name", String.valueOf(resultmap.get("location_code")) + "-" + resultmap.get("location_name"));
            whLocMap.put("wh_price", price);
            whLocMap.put("qty", qty);
        //    whLocMap.put("wh_id", resultmap.get("wh_id"));
            whLocMap.put("location_id", resultmap.get("location_id"));
            whLocMap.put("price_percentage", nt.format(price.divide(sumPrice, BigDecimal.ROUND_HALF_UP).doubleValue()));
            whLocMap.put("qty_percentage", nt.format(qty.divide(sumQty, BigDecimal.ROUND_HALF_UP).doubleValue()));
            LocPriList.add(whLocMap);
        }
        resultMap.put("wh_list", whLocPriList);
        resultMap.put("loc_list", LocPriList);
        resultMap.put("sum_price", sumPrice);
        resultMap.put("sum_qty", sumQty);
        resultMap.put("city_list", cityNum);
        resultMap.put("city_num", cityNum.get(0).get("city_num"));
        resultMap.put("loc_num", locNum);

        return resultMap;
    }

    @Override
    public List<Map<String, Object>> queryLocPriceforCity(Map<String, Object> param) throws Exception {

        return stockBatchDao.queryLocPriceForCity(param);
    }

    @Override
    public Map<String, Object> queryLocPricePortable(Map<String, Object> param) throws Exception {
        return null;
    }

    public List<Map<String, Object>> downloadLocPrice(Map<String, Object> params) {
        return stockBatchDao.downloadLocPrice(params);
    }

   /* @Override
    public Map<String, Object> queryLocPricePortable() {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Map<String, Object>> whLocList = stockBatchDao.queryLocPrice(param);

        List<Map<String, Object>> whLocPriList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> chartInfoList = new ArrayList<Map<String, Object>>();
        // List<Map<String, Object>> xAxisList = new ArrayList<Map<String,
        // Object>>();
        Map<String, Object> countOther = new HashMap<String, Object>();
        Map<String, Object> body = new HashMap<String, Object>();

        // DecimalFormat otherFormat = new DecimalFormat("#.###%");
        int count = 0;
        double other = 0.0;
        NumberFormat nt = NumberFormat.getPercentInstance();
        // 设置百分数精确度4即保留两位小数
        nt.setMinimumFractionDigits(2);
        BigDecimal sumPrice = new BigDecimal(0); // 总金额
        BigDecimal price = new BigDecimal(0);// 各存库金额
        BigDecimal otherPrice = new BigDecimal(0);// after4 存库金额
        for (Map<String, Object> map : whLocList) {
            price = UtilObject.getBigDecimalOrZero(map.get("num"));
            sumPrice = sumPrice.add(price);
        }

        for (int i = 0; i < 1; i++) {
            Map<String, Object> chartmap = new HashMap<String, Object>();
            Map<String, Object> chartmap2 = new HashMap<String, Object>();
            chartmap.put("name", "仓库金额");
            chartmap.put("type", 0);
            chartmap.put("dependence", 0);
            chartmap2.put("name", "金额占比");
            chartmap2.put("type", 0);
            chartmap2.put("dependence", 1);
            chartInfoList.add(chartmap);
            chartInfoList.add(chartmap2);
        }

        for (Map<String, Object> resultmap : whLocList) {
            Map<String, Object> whLocMap = new HashMap<String, Object>();
            count++;

            if (count < 5) {
                whLocMap.put("xAxisName",
                        String.valueOf(resultmap.get("wh_code")) + " " + resultmap.get("location_code"));
                List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String, Object>>();
                Map<String, Object> value = new HashMap<String, Object>();
                Map<String, Object> value1 = new HashMap<String, Object>();
                value.put("yAxisValue", UtilObject.getBigDecimalOrZero(resultmap.get("num")));
                yAxisValueList.add(value);
                value1.put("yAxisValue", String.valueOf(nt.format(UtilObject.getBigDecimalOrZero(resultmap.get("num"))
                        .divide(sumPrice, BigDecimal.ROUND_HALF_UP).doubleValue())).split("%")[0]);
                yAxisValueList.add(value1);
                whLocMap.put("yAxisValueList", yAxisValueList);

                whLocPriList.add(whLocMap);
            } else {
                other += UtilObject.getBigDecimalOrZero(resultmap.get("num")).divide(sumPrice, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                otherPrice = UtilObject.getBigDecimalOrZero(resultmap.get("num")).add(otherPrice);
            }
            *//*
     * if (count > 3) { other +=
     * Double.parseDouble(StringUtils.substringBefore(String.valueOf(
     * whLocMap.get("percentage")), "%"))/100; whLocMap.clear();
     * whLocMap.put("other", otherFormat.format(other)); }
     *//*
        }
        // countOther.put("other", nt.format(other));
        // countOther.put("otherPrice", otherPrice);
        countOther.put("xAxisName", "other");
        List<Map<String, Object>> yAxisValueList = new ArrayList<Map<String, Object>>();
        Map<String, Object> value = new HashMap<String, Object>();
        Map<String, Object> value1 = new HashMap<String, Object>();
        value.put("yAxisValue", otherPrice);
        yAxisValueList.add(value);
        value1.put("yAxisValue", String.valueOf(nt.format(other)).split("%")[0]);
        yAxisValueList.add(value1);
        countOther.put("yAxisValueList", yAxisValueList);
        whLocPriList.add(countOther);

        body.put("leftAxisMaxValue", sumPrice);
        body.put("rightAxisMaxValue", "100");
        body.put("leftAxisUnit", "元");
        body.put("rightAxisUnit", "%");
        body.put("title", "库存结构统计");
        body.put("chartInfoList", chartInfoList);
        body.put("xAxisList", whLocPriList);
        return body;
    }*/

    @Override
    public List<Map<String, Object>> queryFailMES(Map<String, Object> param) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = stockInputDao.queryFailMESOnPaging(param);
        if (list.size() == 0 || null == list) {
            throw new WMSException("无相应数据");
        }
        return list;
    }

    @Override
    public JSONObject queryBaseInfo(CurrentUser cUser) throws Exception {

        JSONObject result = new JSONObject();
        result.put("base_info", stockBatchDao.queryBaseInfo(cUser.getUserId()));
        // result.put("board_list", boardDao.selectAllBoard());
        result.put("board_list", commonService.getBordList());
        result.put("board_id", 1);
        result.put("corp_id", 1);
        // result.put("corp_list", corpDao.selectAllCorpIdAndCorpNameOfCorpList());
        return result;
    }

    @Override
    public List<Map<String, Object>> queryStockOutAndInOnPaging(Map<String, Object> param) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONArray type = (JSONArray) param.get("biz_types");
        if (type.size() == 0) {
            throw new WMSException("请选择一个类型");
        } else {
            list = stockBatchDao.queryStockOutAndInOnPaging(param);
            if (list.size() == 0 || null == list) {
                throw new WMSException("无相应数据");
            }
        }
        return list;
    }
}
