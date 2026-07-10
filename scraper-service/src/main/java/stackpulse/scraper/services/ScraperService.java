package stackpulse.scraper.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stackpulse.scraper.client.MuseClient;
import stackpulse.scraper.entities.JobPosting;
import stackpulse.scraper.entities.JobPostingKeyword;
import stackpulse.scraper.events.ScrapeCompletedEvent;
import stackpulse.scraper.models.MuseJobResult;
import stackpulse.scraper.models.MuseResponse;
import stackpulse.scraper.repositories.JobPostingKeywordRepository;
import stackpulse.scraper.repositories.JobPostingRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class ScraperService
{
    private final MuseClient museClient;
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingKeywordRepository jobPostingKeywordRepository;
    private final ObjectMapper objectMapper;
    private final KeywordMatchingService keywordMatchingService;
    private final KafkaTemplate<String, ScrapeCompletedEvent> kafkaTemplate;

    public ScraperService(MuseClient museClient, JobPostingRepository jobPostingRepository,
                          ObjectMapper objectMapper, KeywordMatchingService keywordMatchingService,
                          JobPostingKeywordRepository jobPostingKeywordRepository, KafkaTemplate<String, ScrapeCompletedEvent> kafkaTemplate)
    {
        this.museClient = museClient;
        this.jobPostingRepository = jobPostingRepository;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
        this.keywordMatchingService = keywordMatchingService;
        this.jobPostingKeywordRepository = jobPostingKeywordRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void scrape()
    {
        try
        {
            int currentPage = 1, newPostingsFound = 0;
            while (newPostingsFound < 50)
            {
                String jobs = museClient.fetchJobs(currentPage++);
                MuseResponse response = objectMapper.readValue(jobs, MuseResponse.class);

                // if the API returns an empty page, there are no more results
                if (response.getResults().isEmpty())
                {
                    System.out.println(">>> No more results");
                    break;
                }

                for (MuseJobResult jobResult : response.getResults())
                {
                    if (jobPostingRepository.existsByExternalId(String.valueOf(jobResult.getId()))) continue;

                    JobPosting newPosting = JobPosting.builder()
                            .externalId(String.valueOf(jobResult.getId()))
                            .title(jobResult.getName())
                            .description(Jsoup.parse(jobResult.getContents()).text())
                            .company(jobResult.getCompany().getCompanyName())
                            .source("Muse")
                            .build();

                    jobPostingRepository.save(newPosting);
                    newPostingsFound++;

                    List<String> matches = keywordMatchingService.findMatches(newPosting.getDescription());
                    for (String match : matches)
                    {
                        JobPostingKeyword newKeyword = JobPostingKeyword.builder()
                                .jobPosting(newPosting)
                                .keyword(match)
                                .build();
                        jobPostingKeywordRepository.save(newKeyword);
                    }

                    if (newPostingsFound >= 50)
                    {
                        System.out.println(">>> Maximum number of new postings reached");
                        break;
                    }
                }
            }
            kafkaTemplate.send("scrape-completed", new ScrapeCompletedEvent(Instant.now(), newPostingsFound));
        } catch (IOException e)
        {
            System.out.println("Error while scraping jobs: " + e.getMessage());
        }
    }
}
