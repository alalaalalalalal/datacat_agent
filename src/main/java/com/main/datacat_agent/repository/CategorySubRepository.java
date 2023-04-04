package com.main.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.datacat_agent.entity.CategorySubEntity;

public interface CategorySubRepository extends JpaRepository<CategorySubEntity, Long> {
    
}
