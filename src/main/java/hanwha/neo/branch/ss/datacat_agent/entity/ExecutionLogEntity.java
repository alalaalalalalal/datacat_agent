package hanwha.neo.branch.ss.datacat_agent.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "execution_log")
public class ExecutionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long pid;

    @Column(name = "status")
    private int status;

    @Column(name = "result")
    private String result;

    @CreationTimestamp
    @Column(name = "executedAt")
    private Timestamp executedAt;

    @Column(name = "scriptId")
    private int scriptId;

    public ExecutionLogEntity(int status, String result, Timestamp executedAt, int scriptId) {
        this.status = status;
        this.result = result;
        this.executedAt = executedAt;
        this.scriptId = scriptId;
    }
}
