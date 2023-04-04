package com.main.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.datacat_agent.entity.CategoryMainEntity;

public interface CategoryMainRepository extends JpaRepository<CategoryMainEntity, Long> {
    
}
