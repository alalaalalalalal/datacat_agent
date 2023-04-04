package com.main.datacat_agent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.datacat_agent.entity.ScriptEntity;

public interface ScriptRepository extends JpaRepository<ScriptEntity, Long> {
    //@Query("SELECT id, program, updatedAt, createdAt, jobId, hostname, command, comment, lastResult, usable, interval, updatedById, createdById, regionId, managerId, managerGroupId FROM script s WHERE s.usable = :usable")
    List<ScriptEntity> findByUsable(String usable);
}
