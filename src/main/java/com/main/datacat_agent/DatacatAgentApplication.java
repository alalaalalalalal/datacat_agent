package com.main.datacat_agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.SSH2AWS;
import com.main.datacat_agent.entity.ExecutionLogEntity;
import com.main.datacat_agent.entity.ScriptEntity;
import com.main.datacat_agent.service.DatacatAgentService;
import com.main.datacat_agent.service.DatacatAgentServiceImpl;

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
					//현재시간 구함
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					String strStamp = String.valueOf(timestamp.getTime());
					Date date = new Date(Long.parseLong(strStamp));
					String scriptCommand = scriptEntity.getCommand();
					int scriptId = scriptEntity.getJobId();
					Timestamp lastExcutionAt = getDatacatAgentService().readScriptExecutionAt(scriptId);

					StringBuilder scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
					String result = scriptResult.toString();
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
						// if(lastExecDate.compareTo(date)>=0){ //스크립트 마지막 실행시간 + 인터벌이 현재시간보다 이후일 경우에만 실행

							//스크립트 실행
							scriptResult = getDatacatAgentService().execShellScript(scriptCommand);
							result = scriptResult.toString();
							// System.out.println(result);
							if(!result.equals("0")){//정상
								getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(1, result, timestamp, scriptId));
							}else{//비정상
								getDatacatAgentService().insertScriptResult( new ExecutionLogEntity(0, result, timestamp, scriptId));
							}
				
							
						// }
					}
					
				}	

			}
			// Thread.sleep(1000 * 60 * 5); //5분에 한번씩 체크
			Thread.sleep(1000 * 5); //5초에 한번씩 체크
		}
		 
	}
}
