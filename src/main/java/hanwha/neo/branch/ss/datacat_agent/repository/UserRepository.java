package hanwha.neo.branch.ss.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hanwha.neo.branch.ss.datacat_agent.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
