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
@Entity(name = "job")
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long pid;

    @Column(name = "program")
    private String program;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Timestamp updatedAt;

    @CreationTimestamp
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "updatedById")
    private int updatedById;

    @Column(name = "createdById")
    private int createdById;

    @Column(name = "categorySubId")
    private int categorySubId;

    public JobEntity(String program, Timestamp updatedAt, Timestamp createdAt, String name, int updatedById,
            int createdById, int categorySubId) {
        this.program = program;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.name = name;
        this.updatedById = updatedById;
        this.createdById = createdById;
        this.categorySubId = categorySubId;
    }
}
