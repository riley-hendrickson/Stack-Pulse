package stackpulse.queryAPI.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import stackpulse.queryAPI.dtos.KeywordFrequencyDTO;
import stackpulse.queryAPI.dtos.KeywordTrendDTO;
import stackpulse.queryAPI.repositories.JobPostingKeywordRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordQueryService
{
    private final JobPostingKeywordRepository jobPostingKeywordRepository;

    public KeywordQueryService(JobPostingKeywordRepository jobPostingKeywordRepository)
    {
        this.jobPostingKeywordRepository = jobPostingKeywordRepository;
    }

    public List<KeywordFrequencyDTO> getTopKeywords(int limit)
    {
        List<Object[]> results = jobPostingKeywordRepository.findTopKeywords(PageRequest.of(0, limit));
        return results.stream()
                .map(result -> new KeywordFrequencyDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue()))
                .toList();
    }

    public List<KeywordFrequencyDTO> getTopKeywordsSince(int days, int limit)
    {
        List<Object[]> results = jobPostingKeywordRepository.findTopKeywordsSince(LocalDateTime.now().minusDays(days), PageRequest.of(0, limit));
        return results.stream()
                .map(result -> new KeywordFrequencyDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue()))
                .toList();
    }

    public List<KeywordTrendDTO> getTrendingKeywords(int days, int limit)
    {
        List<Object[]> results = jobPostingKeywordRepository.findTrendingKeywords(LocalDateTime.now().minusDays(days), LocalDateTime.now().minusDays(days + days),  PageRequest.of(0, limit));
        return results.stream()
                .map(result -> new KeywordTrendDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue(),
                        ((Number) result[2]).longValue()))
                .toList();
    }
}
