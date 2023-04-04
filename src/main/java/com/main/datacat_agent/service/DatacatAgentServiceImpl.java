

package com.main.datacat_agent.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
    public StringBuilder execShellScript(String script) {
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

}