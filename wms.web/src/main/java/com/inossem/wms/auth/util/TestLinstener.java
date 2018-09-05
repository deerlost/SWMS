package com.inossem.wms.auth.util;

import org.flowable.engine.delegate.DelegateTask;
import org.flowable.engine.delegate.TaskListener;

public class TestLinstener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		System.out.print(delegateTask.getId() + " - " + delegateTask.getProcessInstanceId() + " - " + delegateTask.getEventName() +" - "+ delegateTask.getTaskDefinitionKey());
	}

}
