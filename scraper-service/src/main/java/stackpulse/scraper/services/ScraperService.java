package stackpulse.scraper.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import stackpulse.scraper.client.MuseClient;
import stackpulse.scraper.entities.JobPosting;
import stackpulse.scraper.entities.JobPostingKeyword;
import stackpulse.scraper.models.MuseJobResult;
import stackpulse.scraper.models.MuseResponse;
import stackpulse.scraper.repositories.JobPostingKeywordRepository;
import stackpulse.scraper.repositories.JobPostingRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ScraperService
{
    private final MuseClient museClient;
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingKeywordRepository jobPostingKeywordRepository;
    private final ObjectMapper objectMapper;
    private final KeywordMatchingService keywordMatchingService;

    public ScraperService(MuseClient museClient, JobPostingRepository jobPostingRepository,
                          ObjectMapper objectMapper, KeywordMatchingService keywordMatchingService,  JobPostingKeywordRepository jobPostingKeywordRepository)
    {
        this.museClient = museClient;
        this.jobPostingRepository = jobPostingRepository;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
        this.keywordMatchingService = keywordMatchingService;
        this.jobPostingKeywordRepository = jobPostingKeywordRepository;
    }

    public void scrape()
    {
        try
        {
            for(int i = 1; i <= 5; i++)
            {
                String jobs = museClient.fetchJobs(i);
                MuseResponse response = objectMapper.readValue(jobs, MuseResponse.class);
                for(MuseJobResult jobResult : response.getResults())
                {
                    if(jobPostingRepository.existsByExternalId(String.valueOf(jobResult.getId()))) continue;
                    JobPosting newPosting = JobPosting.builder()
                            .externalId(String.valueOf(jobResult.getId()))
                            .title(jobResult.getName())
                            .description(Jsoup.parse(jobResult.getContents()).text())
                            .company(jobResult.getCompany().getCompanyName())
                            .source("Muse")
                            .build();


                    jobPostingRepository.save(newPosting);
                    List<String> matches = keywordMatchingService.findMatches(newPosting.getDescription());
                    for(String match : matches)
                    {
                        JobPostingKeyword newKeyword = JobPostingKeyword.builder()
                                .jobPosting(newPosting)
                                .keyword(match)
                                .build();
                        jobPostingKeywordRepository.save(newKeyword);
                    }
                }
            }
        } catch (IOException e)
        {
            System.out.println("Error while scraping jobs: " + e.getMessage());
        }
    }
}
