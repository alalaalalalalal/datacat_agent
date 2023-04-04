package com.main.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.datacat_agent.entity.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
    
}
