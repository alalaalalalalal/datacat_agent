package hanwha.neo.branch.ss.datacat_agent.repository;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hanwha.neo.branch.ss.datacat_agent.entity.ExecutionLogEntity;

public interface ExecutionLogRepository extends JpaRepository<ExecutionLogEntity, Long> {

    @Query(value = "select max(executedAt) as executedAt from execution_log e where e.scriptId = :scriptId")
    Timestamp findLastExecutionAtByScriptId(@Param(value = "scriptId") int scriptId);
}
