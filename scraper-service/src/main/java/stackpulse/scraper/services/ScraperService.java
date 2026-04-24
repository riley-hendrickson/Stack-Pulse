package stackpulse.scraper.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import stackpulse.scraper.client.AdzunaClient;
import stackpulse.scraper.entities.JobPosting;
import stackpulse.scraper.models.AdzunaJobResult;
import stackpulse.scraper.models.AdzunaResponse;
import stackpulse.scraper.repositories.JobPostingRepository;

import java.io.IOException;

@Service
public class ScraperService
{
    private final AdzunaClient adzunaClient;
    private final JobPostingRepository jobPostingRepository;
    private final ObjectMapper objectMapper;

    public ScraperService(AdzunaClient adzunaClient, JobPostingRepository jobPostingRepository, ObjectMapper objectMapper)
    {
        this.adzunaClient = adzunaClient;
        this.jobPostingRepository = jobPostingRepository;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
    }

    public void scrape()
    {
        try
        {
            String jobs = adzunaClient.fetchJobs("Backend Developer", 1);
            AdzunaResponse response = objectMapper.readValue(jobs, AdzunaResponse.class);
            for(AdzunaJobResult jobResult : response.getJobResults())
            {
                if(jobPostingRepository.existsByAdzunaId(jobResult.getId())) continue;

                JobPosting newPosting = JobPosting.builder()
                        .adzunaId(jobResult.getId())
                        .title(jobResult.getTitle())
                        .description(jobResult.getDescription())
                        .company(jobResult.getCompany().getCompanyName())
                        .source("adzuna")
                        .build();


                jobPostingRepository.save(newPosting);
            }
        } catch (IOException e)
        {
            System.out.println("Error while scraping jobs: " + e.getMessage());
        }
    }
}
