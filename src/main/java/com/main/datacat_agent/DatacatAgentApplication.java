package com.main.datacat_agent;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.core.script.Script;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.main.datacat_agent.entity.ExecutionLogEntity;
import com.main.datacat_agent.entity.ScriptEntity;
import com.main.datacat_agent.service.DatacatAgentService;
import com.main.datacat_agent.service.DatacatAgentServiceImpl;
import com.main.datacat_agent.service.InfluxDbConnector;
import com.main.datacat_agent.service.MysqlConnector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DatacatAgentApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(DatacatAgentApplication.class, args);
		
	}
	@Bean
	public DatacatAgentService getDatacatAgentService(){
		return  new DatacatAgentServiceImpl();
	}
	@Override
	public void run(String... args) throws Exception {		
		
		while(true){
			List<ScriptEntity> scriptList = getDatacatAgentService().readScript();
			for(ScriptEntity scriptEntity : scriptList){
				if(scriptEntity != null){
					if(scriptEntity.getJobId() == 1){
						executeK8s(scriptEntity);
					}else if(scriptEntity.getJobId() == 2){
						MysqlConnector mysqlConnector = new MysqlConnector();
						log.info("mysql 스크립트 : "+ scriptEntity.getCommand());
						String mysqlReturn = mysqlConnector.executeMysql(scriptEntity.getCommand());
						// log.info("실행 결과 : "+mysqlReturn);
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();
						if(!mysqlReturn.equals("0")){//정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, mysqlReturn, timestamp, scriptId));
						}else{//비정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, mysqlReturn, timestamp, scriptId));
						}
					}else if(scriptEntity.getJobId() == 3){
						log.info("influx 스크립트 : "+ scriptEntity.getCommand());
						String result = executeInfluxQuery(scriptEntity.getCommand());
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();
						if(!result.equals("0")){//정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
						}else{//비정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
						}
					}
					

				}
					
			}	
			Thread.sleep(1000 * 5); //5초에 한번씩 체크
		}
			// Thread.sleep(1000 * 60 * 5); //5분에 한번씩 체크
			
		}
		 
	

	public String executeInfluxQuery(String q){
		InfluxDbConnector inConn = new InfluxDbConnector();
		return inConn.queryData(q);
	}

	public void executeK8s(ScriptEntity scriptEntity ){

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	//현재시간 구함
		timestamp = new Timestamp(System.currentTimeMillis());
		String[] scriptCommand = {"/bin/sh", "-c", scriptEntity.getCommand()};
		log.info("스크립트 log={}", scriptCommand[2]);
		int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();
		
		Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);
		StringBuilder scriptResult = new StringBuilder();
		String result = "";
		if(lastExcutionAt == null ){ // 최초실행
			scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
			result = scriptResult.toString();

			if(!result.equals("0")){//정상
				getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
			}else{//비정상
				getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
			}
		}else{
			String lastExecStamp = String.valueOf(lastExcutionAt.getTime()); //마지막 실행 시간
			Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
		
			scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
			result = scriptResult.toString();

			log.info("실행결과 = {}", result);
			//스크립트 실행
			Timestamp result_tmp = Timestamp.valueOf(result);
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastExecDate);
			cal.add(Calendar.MINUTE, scriptEntity.getRepeatInterval()); //마지막 실행결과 시간 + 인터벌
			result_tmp.setTime(cal.getTime().getTime());

			// if(cal.getTime().compareTo(result_tmp)>0){ //만약 최종시작일 + 인터벌이 현재 시각보다 클경우 (마지막 실행 2시  인터벌 120분 현재시각 4시 30분이면  2시+120분 = 4시 이므로 실행 해야함)
				log.info("스크립트 id={}",scriptId);
				if(!result.equals("0")){//정상
					getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
				}else{//비정상
					getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
				}
			// }	
		}
	}
}



