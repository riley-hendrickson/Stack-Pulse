package stackpulse.queryAPI.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stackpulse.queryAPI.entities.JobPostingKeyword;

import java.time.LocalDateTime;
import java.util.List;

public interface JobPostingKeywordRepository extends JpaRepository<JobPostingKeyword, Long>
{
    @Query("SELECT jpk.keyword, COUNT(jpk) as frequency " +
            "FROM JobPostingKeyword jpk " +
            "GROUP BY jpk.keyword " +
            "ORDER BY frequency DESC")
    List<Object[]> findTopKeywords(Pageable pageable);

    @Query("SELECT jpk.keyword, COUNT(jpk) as frequency " +
            "FROM JobPostingKeyword jpk " +
            "WHERE jpk.scrapedAt >= :since " +
            "GROUP BY jpk.keyword " +
            "ORDER BY frequency DESC")
    List<Object[]> findTopKeywordsSince(@Param("since") LocalDateTime since, Pageable pageable);

    @Query("SELECT jpk.keyword, " +
            "SUM(CASE WHEN jpk.scrapedAt >= :periodStart THEN 1 ELSE 0 END) as recent, " +
            "SUM(CASE WHEN jpk.scrapedAt < :periodStart AND jpk.scrapedAt >= :priorStart THEN 1 ELSE 0 END) as prior " +
            "FROM JobPostingKeyword jpk " +
            "WHERE jpk.scrapedAt >= :priorStart " +
            "GROUP BY jpk.keyword " +
            "ORDER BY (SUM(CASE WHEN jpk.scrapedAt >= :periodStart THEN 1 ELSE 0 END) - " +
            "SUM(CASE WHEN jpk.scrapedAt < :periodStart AND jpk.scrapedAt >= :priorStart THEN 1 ELSE 0 END)) DESC")
    List<Object[]> findTrendingKeywords(@Param("periodStart") LocalDateTime periodStart,
                                        @Param("priorStart") LocalDateTime priorStart,
                                        Pageable pageable);
}
