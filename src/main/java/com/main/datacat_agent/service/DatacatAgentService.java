
package com.main.datacat_agent.service;

import java.sql.Timestamp;
import java.util.List;
import com.main.datacat_agent.entity.ExecutionLogEntity;
import com.main.datacat_agent.entity.MessageEntity;
import com.main.datacat_agent.entity.ScriptEntity;
import com.main.datacat_agent.entity.UserEntity;

public interface DatacatAgentService {
    // 쉘 스크립트 수행 결과 DB저장
    public Object insertScriptResult(ExecutionLogEntity execcutionLogEntity);

    // 메세지발송
    public Object insertMessage(MessageEntity messageEntity);

    // 유저 등록
    public Object insertUser(UserEntity UserEntity);

    // 스크립트 읽어오기
    public List<ScriptEntity> readScript(String hostname);

    // 스크립트 실행
    public StringBuilder execShellScript(String[] script);

    // 스크립트 최종 실행 시간 조회
    public Timestamp readScriptExecutionAt(int scriptId);

    // 스크립트 스케줄 등록
    public void registerScriptSchedule(List<ScriptEntity> scriptList);

    // ssh 스크립트 실행
    public String getSSHResponse(String source);
}
