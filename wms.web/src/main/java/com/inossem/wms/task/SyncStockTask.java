package com.inossem.wms.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inossem.wms.service.biz.ICommonService;


@Component("syncStockTask")
public class SyncStockTask {

	private static final Logger logger = LoggerFactory.getLogger(SyncStockTask.class);
	
	@Autowired
	private ICommonService commonService;
	
	
	@Scheduled(cron = "0 0 1 * * ?")
	private void synErpBatch() {
		logger.info("同步ERP批次记录任务:----------------------开始");
		try {
			commonService.bufferERPBatch();;
		} catch (Exception e) {
			logger.error("同步ERP批次记录任务", e.getMessage());
			e.printStackTrace();
		}
		logger.info("同步ERP批次记录任务:----------------------结束");
	}
	
	@Scheduled(cron = "0 0 2 * * ?")
	private void synErpPrice() {
		logger.info("同步ERP移动平均价记录任务:----------------------开始");
		try {
			commonService.bufferPrice();;
		} catch (Exception e) {
			logger.error("同步ERP移动平均价记录任务", e.getMessage());
			e.printStackTrace();
		}
		logger.info("同步ERP移动平均价记录任务:----------------------结束");
	}
	
	@Scheduled(cron = "0 0 3 1 * ?")
	private void beginStock() {
		logger.info("批次库存初期任务:----------------------开始");
		try {
			commonService.insertStockBatchBegin();
		} catch (Exception e) {
			logger.error("批次库存初期任务", e.getMessage());
			e.printStackTrace();
		}
		logger.info("批次库存初期任务:----------------------结束");
	}
	
	
	@Scheduled(cron = "0 0 4 1 * ?")
	private void deleteMes() {
		logger.info("删除mes同步失败记录任务:----------------------开始");
		try {
			commonService.deleteDateByDays(15);
		} catch (Exception e) {
			logger.error("删除mes同步失败记录任务", e.getMessage());
			e.printStackTrace();
		}
		logger.info("删除mes同步失败记录任务:----------------------结束");
	}
	
	
	
//	@RequestMapping("/auth/synStockTask")
//	public void run1() {
//		try {
//			commonService.insertStockBatchBegin();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
