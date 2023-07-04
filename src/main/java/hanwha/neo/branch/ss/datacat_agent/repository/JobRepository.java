package hanwha.neo.branch.ss.datacat_agent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hanwha.neo.branch.ss.datacat_agent.entity.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, Long> {

}
