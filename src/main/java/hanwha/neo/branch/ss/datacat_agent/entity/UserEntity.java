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

@Data
@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long pid;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Timestamp updatedAt;

    @CreationTimestamp
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public UserEntity(Timestamp updatedAt, Timestamp createdAt, String username, String password) {
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.username = username;
        this.password = password;
    }
}
