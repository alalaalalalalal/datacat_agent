package hanwha.neo.branch.ss.datacat_agent.entity;

import lombok.Data;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Data
@Entity(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long pid;

    @Column(name = "payload")
    private String payload;

    @Column(name = "sent")
    private int sent;

    @Column(name = "resent")
    private int resent;

    @Column(name = "sentAt")
    private Timestamp sentAt;

    @Column(name = "jobId")
    private int jobId;

    public MessageEntity(String payload, int sent, int resent, Timestamp sentAt, int jobId) {
        this.payload = payload;
        this.sent = sent;
        this.resent = resent;
        this.sentAt = sentAt;
        this.jobId = jobId;
    }
}
