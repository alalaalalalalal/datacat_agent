package com.main.datacat_agent;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.main.datacat_agent.entity.ExecutionLogEntity;
import com.main.datacat_agent.entity.ScriptEntity;
import com.main.datacat_agent.service.DatacatAgentService;
import com.main.datacat_agent.service.DatacatAgentServiceImpl;
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
		MysqlConnector mysqlConnector = new MysqlConnector();
		String mysqlReturn = mysqlConnector.executeMysql("SELECT if((TIMESTAMPDIFF(MINUTE, sysdate(),reg_dt)) >= 5, 1,0) AS TIMESTAMPDIFF FROM uep.tb_mntrg_item_raw_data ORDER BY reg_dt desc LIMIT 1;");
		log.info("실행 결과 : "+mysqlReturn);
		String q = "SELECT time,nodeID,pointsWrittenOK FROM \"_internal\".\"monitor\".\"httpd\" order by time desc limit 30";
		executeInfluxQuery(q);
		// while(true){
			List<ScriptEntity> scriptList = getDatacatAgentService().readScript();
			for(ScriptEntity scriptEntity : scriptList){
				if(scriptEntity != null){
					//현재시간 구함
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String strStamp = String.valueOf(timestamp.getTime());
					Date date = new Date(Long.parseLong(strStamp));
					String[] scriptCommand = {"/bin/sh", "-c", scriptEntity.getCommand()};
					// String scriptCommand = scriptEntity.getCommand();
					log.info("스크립트 log={}", scriptCommand[2]);
					int scriptId = scriptEntity.getJobId();
					Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);
					StringBuilder scriptResult = new StringBuilder();
					String result = "";
					// System.out.println(result);
					if(lastExcutionAt == null ){
						scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
						result = scriptResult.toString();
						if(!result.equals("0")){//정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
						}else{//비정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
						}
						
					}else{
						String lastExecStamp = String.valueOf(lastExcutionAt.getTime());
						Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
					

						log.info("실행결과 = {}", result);
						//스크립트 실행
						scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
						result = scriptResult.toString();
						log.info("실행결과 = {}", result);
						System.out.println(result);
						Timestamp result_tmp = Timestamp.valueOf(result);
						Calendar cal = Calendar.getInstance();
						cal.setTime(result_tmp);
						cal.add(Calendar.MINUTE, scriptEntity.getRepeatInterval());
						result_tmp.setTime(cal.getTime().getTime());
						System.out.println(result_tmp);
						log.info("실행결과 = {}", result);
						log.info("실행결과 + 인터벌 = {}", result_tmp.toString());
						if(!result.equals("0")){//정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
						}else{//비정상
							getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
						}
				
							
					}
					
				}	

			}
			// Thread.sleep(1000 * 60 * 5); //5분에 한번씩 체크
		// 	Thread.sleep(1000 * 5); //5초에 한번씩 체크
		// }
		 
	}

	public void executeInfluxQuery(String q){
		
		QueryResult infQueryResult =  getDatacatAgentService().getInfluxStatus(q);
		List<Result> infQueryResultList = infQueryResult.getResults();
		for(Result infQueryResultRow : infQueryResultList){
			List<Series> test = infQueryResultRow.getSeries();
			for(Series testRow : test){
				List<List<Object>> values = testRow.getValues();
				for(List<Object> value : values){
					log.info("인플럭스 실행결과 = {}", value.get(2));
					log.info("\n");
				}
			}
		}
	}
}
