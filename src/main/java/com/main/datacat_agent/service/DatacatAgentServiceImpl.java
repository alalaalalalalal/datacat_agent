

package com.main.datacat_agent.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.main.datacat_agent.entity.ExecutionLogEntity;
import com.main.datacat_agent.entity.MessageEntity;
import com.main.datacat_agent.entity.ScriptEntity;
import com.main.datacat_agent.entity.UserEntity;
import com.main.datacat_agent.repository.ExecutionLogRepository;
import com.main.datacat_agent.repository.MessageRepository;
import com.main.datacat_agent.repository.ScriptRepository;
import com.main.datacat_agent.repository.UserRepository;

public class DatacatAgentServiceImpl implements DatacatAgentService {
    @Autowired
    ExecutionLogRepository executionLogRepository;    
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ScriptRepository scriptRepository;

    private static InfluxDbConnector infConn = InfluxDbConnector.getInstance(); 
    protected InfluxDB infDB = infConn.getDb();


    @Override
    public Object insertScriptResult(ExecutionLogEntity executionLogEntity) {
        return executionLogRepository.save(executionLogEntity);
    }


    @Override
    public Object insertMessage(MessageEntity messageEntity) {
        return messageRepository.save(messageEntity);
    }


    @Override
    public Object insertUser(UserEntity UserEntity) {
        return userRepository.save(UserEntity);
    }


    @Override
    public List<ScriptEntity> readScript() {   
        return scriptRepository.findAll();
    }



    @Override
    public StringBuilder execShellScript(String[] script) {
        try {
            // Run script
            Process process = Runtime.getRuntime().exec(script);

            // Read output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // System.out.println(output.toString());

            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }


    @Override
    public Timestamp readScriptExecutionAt(int scriptId) {
        return executionLogRepository.findLastExecutionAtByScriptId(scriptId);
        
    }


    @Override
    public void registerScriptSchedule(List<ScriptEntity> scriptList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerScriptSchedule'");
    }


    @Override
    public String getSSHResponse(String source) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSSHResponse'");
    }


    @Override
    public QueryResult getInfluxStatus(String query) {
         //위에서 만들어준 접속용 인플럭스DB 연결 클래스

        Query q = new Query(query, "isl");
        QueryResult ask = infDB.query(q);  //List + Map형태의 객체(CRUD 전부)
        return ask;
    }

}