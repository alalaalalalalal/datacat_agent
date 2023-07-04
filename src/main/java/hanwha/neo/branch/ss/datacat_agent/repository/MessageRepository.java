package hanwha.neo.branch.ss.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hanwha.neo.branch.ss.datacat_agent.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

}
