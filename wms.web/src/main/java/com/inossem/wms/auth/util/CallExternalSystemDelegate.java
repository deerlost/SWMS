package com.inossem.wms.auth.util;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CallExternalSystemDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		// TODO Auto-generated method stub
		 System.out.println("Calling the external system for employee "
		            + execution.getVariable("employee"));

	}

}
