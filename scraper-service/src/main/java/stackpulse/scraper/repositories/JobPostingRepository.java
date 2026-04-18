package stackpulse.scraper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stackpulse.scraper.entities.JobPosting;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long>
{
    boolean existsByAdzunaId(String adzunaId);
}
