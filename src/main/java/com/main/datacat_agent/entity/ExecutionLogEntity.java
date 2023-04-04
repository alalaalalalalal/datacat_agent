package com.main.datacat_agent.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.*;

import jakarta.persistence.*;
import jakarta.persistence.NamedQuery;



@Data
@Entity(name="execution_log")
public class ExecutionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long pid;

    @Column(name="status")
    private int status;

    @Column(name="result")
    private String result;

    @CreationTimestamp
    @Column(name="executedAt")
    private Timestamp executedAt;

    @Column(name="scriptId")
    private int scriptId;

    public ExecutionLogEntity(int status, String result, Timestamp executedAt, int scriptId) {
        this.status = status;
        this.result = result;
        this.executedAt = executedAt;
        this.scriptId = scriptId;
    }
}
