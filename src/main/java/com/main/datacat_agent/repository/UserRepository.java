package com.main.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.datacat_agent.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
}
