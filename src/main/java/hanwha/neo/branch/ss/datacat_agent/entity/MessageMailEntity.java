package hanwha.neo.branch.ss.datacat_agent.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "message_mail")
@NoArgsConstructor
public class MessageMailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private long seq;

    @Column(name = "mail_subject")
    private String mailSubject;

    @Column(name = "mail_contents")
    private String mailContents;

    @Column(name = "mail_gb")
    private int mailGb;

    @Column(name = "mail_recv_group")
    private String mailRecvGroup;

    @Column(name = "sent")
    private int sent;

    @UpdateTimestamp
    @Column(name = "sentAt")
    private Timestamp sentAt;
    
    @Column(name = "createdById")
    private int createdById;

    @CreationTimestamp
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updatedById")
    private int updatedById;

    @CreationTimestamp
    @Column(name = "updatedAt")
    private Timestamp updatedAt;
 

    public MessageMailEntity(String mailSubject, String mailContents, int mailGb, String mailRecvGroup, int sent, Timestamp sentAt, int updatedById, Timestamp updatedAt) {
        this.mailSubject = mailSubject;
        this.mailContents = mailContents;
        this.mailGb = mailGb;
        this.mailRecvGroup = mailRecvGroup;
        this.sent = sent;
        this.sentAt = sentAt;
        this.updatedById = updatedById;
        this.updatedAt = updatedAt;
    }
}
