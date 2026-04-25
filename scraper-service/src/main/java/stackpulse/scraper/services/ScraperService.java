package stackpulse.scraper.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import stackpulse.scraper.client.MuseClient;
import stackpulse.scraper.entities.JobPosting;
import stackpulse.scraper.models.MuseJobResult;
import stackpulse.scraper.models.MuseResponse;
import stackpulse.scraper.repositories.JobPostingRepository;

import java.io.IOException;

@Service
public class ScraperService
{
    private final MuseClient museClient;
    private final JobPostingRepository jobPostingRepository;
    private final ObjectMapper objectMapper;

    public ScraperService(MuseClient museClient, JobPostingRepository jobPostingRepository, ObjectMapper objectMapper)
    {
        this.museClient = museClient;
        this.jobPostingRepository = jobPostingRepository;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
    }

    public void scrape()
    {
        try
        {
            for(int i = 0; i < 5; i++)
            {
                String jobs = museClient.fetchJobs( i);
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
            }
            }
        } catch (IOException e)
        {
            System.out.println("Error while scraping jobs: " + e.getMessage());
        }
    }
}
