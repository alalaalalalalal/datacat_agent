package hanwha.neo.branch.ss.datacat_agent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hanwha.neo.branch.ss.datacat_agent.entity.MessageMailEntity;

public interface MessageMailRepository extends JpaRepository<MessageMailEntity, Long> {

    List<MessageMailEntity> findBySent(String sent);
}
