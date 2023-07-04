package hanwha.neo.branch.ss.datacat_agent.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.*;

import jakarta.persistence.*;

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
