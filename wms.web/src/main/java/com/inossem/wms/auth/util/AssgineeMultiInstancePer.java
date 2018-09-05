package com.inossem.wms.auth.util;

import java.util.Arrays;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class AssgineeMultiInstancePer implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		// TODO Auto-generated method stub
		System.out.println("设置会签环节的人员.");
		execution.setVariable("pers",Arrays.asList("张三", "李四", "王五", "赵六"));
	}

}
