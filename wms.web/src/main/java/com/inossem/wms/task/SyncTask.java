package com.inossem.wms.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.inossem.wms.service.biz.ICommonService;

@Component("syncTask")
public class SyncTask {
	private static final Logger logger = LoggerFactory.getLogger(SyncTask.class);
	// @Autowired
	// private IDHYSService dhysService;
	//
	// @Autowired
	// private IRKGLService rkglService;

	// @Autowired
	// private IHDZMService hdzmService;

	// @Autowired
	// private IWLMSService wlmsService;

	@Autowired
	private ICommonService commonService;

	// quartz CronExpression表达式
	//@Scheduled(cron = "0 30 * * * ?")
	public void testCron() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.print("每小时自动同步任务    ");
		System.out.println(sdf.format(new Date()));
		try {
			// 同步sap数据
			// dhysService.syncSapYSD();
			// 未生成入库单的数据生成入库单
			// rkglService.initYsrkSap("sap");

			// hdzmService.syncSapMkpf();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	//@Scheduled(cron = "0 0,30 * * * ?")
	public void syncSapMakt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.print("每半小时自动同步任务    ");
		System.out.println(sdf.format(new Date()));
		try {
			// wlmsService.syncSapMakt("");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨一点
	// @Scheduled(cron = "0 5,15,25,35,45,55 * * * ?") // 每天凌晨一点
	public void syncSapPrice() {
		System.out.print("每天凌晨一点");
		System.out.println(new Date());
		try {
			commonService.bufferPrice();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	// @Scheduled(cron = "0/5 * * * * ?") // 每5秒钟执行一次
	// public void testCron() {
	// System.out.print("定时任务测试testCron");
	// System.out.println(new Date());
	// }

	// @Scheduled(fixedRate = 5000, initialDelay = 1000) //
	// 第一次服务器启动一秒后执行，以后每过5秒执行一次，具体情况请查看Scheduled的方法
	// public void test() {
	// System.out.print(" 定时任务测试test");
	// System.out.println(new Date());
	// }

}