package stackpulse.scraper.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPosting
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String externalId;
    private String title;
    private String company;
    private String description;
    @Column(nullable = false)
    private String source;
    @Column(updatable = false, insertable = false)
    private LocalDateTime scrapedAt;

    @OneToMany(mappedBy = "jobPosting")
    private List<JobPostingKeyword> keywords;
}
