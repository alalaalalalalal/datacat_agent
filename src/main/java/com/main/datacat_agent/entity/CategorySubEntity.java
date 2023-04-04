package com.main.datacat_agent.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.*;

import jakarta.persistence.*;

@Data
@Entity(name="category_sub")
public class CategorySubEntity {
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

    @Column(name="name")
    private String name;

    @Column(name="updatedById")
    private int updatedById;

    @Column(name="createdById")
    private int createdById;

    @Column(name="categoryMainId")
    private long categoryMainId;


    public CategorySubEntity(String program, Timestamp updatedAt, Timestamp createdAt, String name, int updatedById, int createdById, long categoryMainId) {
        this.program = program;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.name = name;
        this.updatedById = updatedById;
        this.createdById = createdById;
        this.categoryMainId = categoryMainId;
    }
}
