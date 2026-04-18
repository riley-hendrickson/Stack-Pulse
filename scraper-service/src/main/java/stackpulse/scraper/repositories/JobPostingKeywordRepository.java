package stackpulse.scraper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stackpulse.scraper.entities.JobPostingKeyword;

public interface JobPostingKeywordRepository extends JpaRepository<JobPostingKeyword, Long>
{

}
