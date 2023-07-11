
package hanwha.neo.branch.ss.datacat_agent.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hanwha.neo.branch.ss.datacat_agent.entity.ExecutionLogEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageMailEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.ScriptEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.UserEntity;
import hanwha.neo.branch.ss.datacat_agent.repository.ExecutionLogRepository;
import hanwha.neo.branch.ss.datacat_agent.repository.MessageMailRepository;
import hanwha.neo.branch.ss.datacat_agent.repository.MessageRepository;
import hanwha.neo.branch.ss.datacat_agent.repository.ScriptRepository;
import hanwha.neo.branch.ss.datacat_agent.repository.UserRepository;

public class DatacatAgentServiceImpl implements DatacatAgentService {
    @Autowired
    ExecutionLogRepository executionLogRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ScriptRepository scriptRepository;
    @Autowired
    MessageMailRepository messageMailRepository;

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
    public List<MessageMailEntity> selectItrmMail(String sent) {
        return messageMailRepository.findBySent(sent);
    }

    @Override
    public List<ScriptEntity> readScript(String hostname) {
        return scriptRepository.findByHostnameContaining(hostname);
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
    public void updateMailstatus(Long pid) {
        MessageMailEntity messageMailEntity = messageMailRepository.findBySeq(pid);
        messageMailEntity.setSent(1);
        messageMailRepository.save(messageMailEntity);
    }

}