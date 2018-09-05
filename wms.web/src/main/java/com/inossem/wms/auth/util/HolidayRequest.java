package com.inossem.wms.auth.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;

public class HolidayRequest {

	public static void main(String[] args) {
//		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
//				.setJdbcUrl("jdbc:mysql://rm-2zew5j8dkh82h56plo.mysql.rds.aliyuncs.com:3306/wms?characterEncoding=utf8&useSSL=true")
//				.setJdbcUsername("root")
//				.setJdbcPassword("!@#QWE123qwe")
//				.setJdbcDriver("com.mysql.jdbc.Driver")
//				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		//	 InputStream in = HolidayRequest.class.getClassLoader().getResourceAsStream("flowable.cfg.xml");
		//	 ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(in);
		//	  
		
	//	ProcessEngine processEngine = cfg.buildProcessEngine();

		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("mybatis/holiday-request.bpmn20.xml")
				.deploy();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId())
				.singleResult();
		System.out.println("Found process definition : " + processDefinition.getName());


		RuntimeService runtimeService = processEngine.getRuntimeService();

		Scanner scanner= new Scanner(System.in);

		System.out.println("Who are you?");
		String employee = scanner.nextLine();

		System.out.println("How many holidays do you want to request?");
		Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

		System.out.println("Why do you need them?");
		String description = scanner.nextLine();


		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("employee", employee);
		variables.put("nrOfHolidays", nrOfHolidays);
		variables.put("description", description);
		ProcessInstance processInstance =
				runtimeService.startProcessInstanceByKey("holidayRequest", variables);




		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();


		System.out.println("You have " + tasks.size() + " tasks:");
		for (int i=0; i<tasks.size(); i++) {
			System.out.println((i+1) + ") " + tasks.get(i).getName());
		}

		System.out.println("Which task would you like to complete?");
		int taskIndex = Integer.valueOf(scanner.nextLine());
		Task task = tasks.get(taskIndex - 1);
		Map<String, Object> processVariables = taskService.getVariables(task.getId());
		System.out.println(processVariables.get("employee") + " wants " +
				processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

		boolean approved = scanner.nextLine().toLowerCase().equals("y");
		variables = new HashMap<String, Object>();
		variables.put("approved", approved);
		taskService.complete(task.getId(), variables);


		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricActivityInstance> activities =
				historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstance.getId())
				.finished()
				.orderByHistoricActivityInstanceEndTime().asc()
				.list();




		for (HistoricActivityInstance activity : activities) {
			System.out.println(activity.getActivityId() + " took "
					+ activity.getDurationInMillis() + " milliseconds");
		}
		System.out.println(	);


	}

}