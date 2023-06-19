package com.main.datacat_agent;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
					log.info("스크립트 리드 성공");
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					String hour = sdf.format(timestamp).replace(":", "");
					String starTtime = scriptEntity.getStartTime().toString().replace(":", "");
					String endTime = scriptEntity.getEndTime().toString().replace(":", "");

					if(Integer.valueOf(hour) >= Integer.valueOf(starTtime) && Integer.valueOf(hour) <= Integer.valueOf(endTime)){ // 언제부터 언제까지 실행해야 하는지 체크
						log.info("스크립트 실행 성공");
						log.info(hour + " / " + starTtime + " / " + endTime);
						log.info("id : " + scriptEntity.getPid());
						executeK8s(scriptEntity);
					}else{
						log.info("스크립트 시간 체크로 제외됨");
					}
				}					
			}	
			Thread.sleep(1000 * 60 * 5); //5분에 한번씩 체크
		}
	}
		 
	

	public String executeInfluxQuery(String q){
		InfluxDbConnector inConn = new InfluxDbConnector();
		return inConn.queryData(q);
	}

	public void executeK8s(ScriptEntity scriptEntity ){

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		

	//현재시간 구함
		String[] scriptCommand = {"/bin/sh", "-c", scriptEntity.getCommand() };
		// String[] scriptCommand = {"/bin/sh", "-c", "sudo ssh -i ~/SIT_DEV_DP_KEY_DBSUB_Virginia.pem ec2-user@10.157.16.71 \"influx -database isl -execute 'SELECT pointsWrittenOK FROM \"_internal\".\"monitor\".\"httpd\" order by time desc limit 2'\" | head -4| tail -1 | awk '{ print $2}'"};
		// sudo ssh -i SIT_DEV_DP_KEY_DBSUB_Virginia.pem ec2-user@10.157.16.71 "influx -database isl -execute 'SELECT pointsWrittenOK FROM \"_internal\".\"monitor\".\"httpd\" order by time desc limit 2'" | head -4| tail -1 | awk '{ print $2}'
		// String[] scriptCommand = {"/bin/sh", "-c", "mysql -h dev-dp-db1-cluster-virginia-instance-1.c8vihicq2w3y.us-east-1.rds.amazonaws.com -N -u sithome -psit0911! -P 33060 -e \"SELECT if((TIMESTAMPDIFF(MINUTE, sysdate(),reg_dt)) >= 5, 1,0) AS TIMESTAMPDIFF FROM uep.tb_mntrg_item_raw_data ORDER BY reg_dt desc LIMIT 1;\""};
		log.info("스크립트 log={}", scriptCommand[2]);
		int scriptId = Long.valueOf(scriptEntity.getPid()).intValue();
		
		Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);
		StringBuilder scriptResult = new StringBuilder();
		String result = "";
		if(lastExcutionAt == null ){ // 최초실행
			scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
			result = scriptResult.toString();
			//true 가 0 false 가 0 아닌것
			if(!result.equals("0") || result.length() > 1){//비정상
				getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result.length() > 1 ? "toolong" : result , timestamp, scriptId));
			}else{//정상
				getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
			}
		}else{
			String lastExecStamp = String.valueOf(lastExcutionAt.getTime()); //마지막 실행 시간
			Date lastExecDate = new Date(Long.parseLong(lastExecStamp));
			//스크립트 실행
			timestamp = new Timestamp(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastExecDate);
			cal.add(Calendar.MINUTE, scriptEntity.getRepeatInterval()); //마지막 실행결과 시간 + 인터벌
			if(cal.getTime().compareTo(timestamp)<=0){ //만약 최종시작일 + 인터벌이 현재 시각보다 클경우 (마지막 실행 2시  인터벌 120분 현재시각 4시 30분이면  2시+120분 = 4시 이므로 실행 해야함)
				scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
				result = scriptResult.toString();
				
				log.info("실행결과 = {}", result);
			
				if(!result.equals("0") || result.length() > 1){//비정상, 결과가 0이 아닌것 전부비정상
					getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result.length() > 1 ? "toolong" : result, timestamp, scriptId));
				}else{//정상
					getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
				}
			}
		}
	}
}



