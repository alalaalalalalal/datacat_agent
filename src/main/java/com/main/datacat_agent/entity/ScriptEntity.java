package com.main.datacat_agent.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

import org.hibernate.annotations.*;

import jakarta.persistence.*;

@Data
@Entity(name="script")
@NoArgsConstructor
public class ScriptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long pid;
    
    @Column(name="program")
    private String program;

    @UpdateTimestamp
    @Column(name="updatedAt")
    private Timestamp updatedAt;

    @CreationTimestamp
    @Column(name="createdAt")
    private Timestamp createdAt;

    @Column(name="jobId")
    private int jobId;

    @Column(name="hostname")
    private String hostname;

    @Column(name="command")
    private String command;

    @Column(name="comment")
    private String comment;

    @Column(name="lastResult")
    private String lastResult;

    @Column(name="usable")
    private String usable;

    @Column(name="repeatInterval")
    private int repeatInterval;

    @Column(name="updatedById")
    private int updatedById;

    @Column(name="createdById")
    private int createdById;

    @Column(name="regionId")
    private int regionId;

    @Column(name="managerId")
    private int managerId;

    @Column(name="managerGroupId")
    private int managerGroupId;

    @Column(name="startTime")
    private Time startTime;

    @Column(name="endTime")
    private Time endTime;



    
    public ScriptEntity(long id, String program, Timestamp updatedAt, Timestamp createdAt, int jobId, String hostname, String command, String comment, String lastResult, String usable, int repeatInterval, int updatedById, int createdById, int regionId, int managerId, int managerGroupId, Time startTime, Time endTime) {
        this.pid = id;
        this.program = program;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.jobId = jobId;
        this.hostname = hostname;
        this.command = command;
        this.comment = comment;
        this.lastResult = lastResult;
        this.usable = usable;
        this.repeatInterval = repeatInterval;
        this.updatedById = updatedById;
        this.createdById = createdById;
        this.regionId = regionId;
        this.managerId = managerId;
        this.managerGroupId = managerGroupId;
        this.startTime = startTime;
        this.endTime = endTime;
        
    }
}
