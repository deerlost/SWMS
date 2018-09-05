package com.inossem.wms.auth.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;

import com.inossem.wms.model.page.PageHelper;

public class TestMultiInstance {
	private static int lastKeyword(String text, String keyword, int index) {
		int i = text.lastIndexOf(keyword, index - 1);
		if (i < 0 || i >= text.length()) {
			return i;
		} else {
			if ((i == 0 || text.charAt(i - 1) <= ' ')
					&& (i + keyword.length() >= text.length() || text.charAt(i + keyword.length()) <= ' ')) {
				return i;
			} else {
				return lastKeyword(text, keyword, i);
			}
		}
	}

	private static boolean isSeparator(char c) {
		if (c <= ' ') {
			return true;
		} else {
			switch (c) {
			case '(':
				return true;
			// break;
			case ')':
				return true;
			// break;
			default:
				return false;
			// break;
			}
		}
	}

	public static void main(String[] args) {
		String sqlLowercase = "select ssafd,(select 1 )  from asdf asfd order by internal_order_code  desc order";

		int aaa = PageHelper.topFrom(sqlLowercase.toLowerCase());

		int bbb = PageHelper.lastKeyword(sqlLowercase.toLowerCase(), "order", sqlLowercase.length());
		int a = sqlLowercase.lastIndexOf("order");
		int b = sqlLowercase.lastIndexOf("order", a - 1);
		char c = sqlLowercase.charAt(b + 5);

		// int b=sqlLowercase.lastIndexOf(")");

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		TaskService taskService = processEngine.getTaskService();
		Deployment deploy = repositoryService.createDeployment()
				.addClasspathResource("mybatis/multiInstances.bpmn20.xml").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploy.getId()).singleResult();
		System.out.println("Found process definition : " + processDefinition.getName());

		System.out.println(deploy.getId() + " " + deploy.getName());
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("mulitiInstance", new MulitiInstanceCompleteTask());
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("multiInstances", variables);
		System.out.println(pi.getId() + " " + pi.getActivityId());
		Task task1 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("张三").singleResult();
		System.out.println(task1.getId() + " - " + task1.getAssignee() + " - " + task1.getProcessInstanceId() + " - "
				+ task1.getProcessDefinitionId());
		Task task2 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("李四").singleResult();
		System.out.println(task2.getId() + " - " + task2.getAssignee() + " - " + task2.getProcessInstanceId() + " - "
				+ task2.getProcessDefinitionId());
		Task task3 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("王五").singleResult();
		System.out.println(task3.getId() + " - " + task3.getAssignee() + " - " + task3.getProcessInstanceId() + " - "
				+ task3.getProcessDefinitionId());
		Task task4 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("赵六").singleResult();
		if (task4 != null) {
			System.out.println(task4.getId() + " - " + task4.getAssignee() + " - " + task4.getProcessInstanceId()
					+ "  - " + task4.getProcessDefinitionId());

		}
		Task task5 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("钱七").singleResult();
		System.out.println(task5);
		taskService.complete(task1.getId());
		taskService.complete(task2.getId());
		taskService.complete(task3.getId());
		Task task6 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("钱七").singleResult();
		System.out.println(task6);
		taskService.complete(task4.getId());
		Task task7 = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee("钱七").singleResult();
		System.out.println(task7);
		taskService.complete(task7.getId());
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId())
				.singleResult();
		if (null == processInstance) {
			System.out.println("流程完成");
		}

	}
}
