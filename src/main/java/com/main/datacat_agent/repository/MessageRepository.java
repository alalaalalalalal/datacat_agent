package com.main.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.datacat_agent.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    
}
