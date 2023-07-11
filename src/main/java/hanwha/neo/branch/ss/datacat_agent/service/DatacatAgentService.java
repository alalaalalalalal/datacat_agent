
package hanwha.neo.branch.ss.datacat_agent.service;

import java.sql.Timestamp;
import java.util.List;

import hanwha.neo.branch.ss.datacat_agent.entity.ExecutionLogEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.MessageMailEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.ScriptEntity;
import hanwha.neo.branch.ss.datacat_agent.entity.UserEntity;

public interface DatacatAgentService {
    // 쉘 스크립트 수행 결과 DB저장
    public Object insertScriptResult(ExecutionLogEntity execcutionLogEntity);

    // 메세지발송
    public Object insertMessage(MessageEntity messageEntity);

    // 유저 등록
    public Object insertUser(UserEntity UserEntity);

    // 스크립트 읽어오기
    public List<ScriptEntity> readScript(String hostname);

    // itrm 메일 읽어오기
    public List<MessageMailEntity> selectItrmMail(String sent);

    // itrm 메일 상태업데이트
    public void updateMailstatus(Long pid);

    // 스크립트 실행
    public StringBuilder execShellScript(String[] script);

    // 스크립트 최종 실행 시간 조회
    public Timestamp readScriptExecutionAt(int scriptId);

    // 스크립트 스케줄 등록
    public void registerScriptSchedule(List<ScriptEntity> scriptList);

    // ssh 스크립트 실행
    public String getSSHResponse(String source);
}
