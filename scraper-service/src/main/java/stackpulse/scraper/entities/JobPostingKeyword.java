package stackpulse.scraper.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_posting_keywords")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingKeyword
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String keyword;
    @Column(updatable = false, insertable = false)
    private LocalDateTime scrapedAt;

    @ManyToOne
    @JoinColumn(name = "posting_id")
    private JobPosting jobPosting;
}
