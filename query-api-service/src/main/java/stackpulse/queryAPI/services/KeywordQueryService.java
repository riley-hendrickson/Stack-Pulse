package stackpulse.queryAPI.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import stackpulse.queryAPI.dtos.KeywordFrequencyDTO;
import stackpulse.queryAPI.dtos.KeywordTrendDTO;
import stackpulse.queryAPI.repositories.JobPostingKeywordRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordQueryService
{
    private final JobPostingKeywordRepository jobPostingKeywordRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration CACHE_TTL = Duration.ofHours(23);

    public KeywordQueryService(JobPostingKeywordRepository jobPostingKeywordRepository, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper)
    {
        this.jobPostingKeywordRepository = jobPostingKeywordRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<KeywordFrequencyDTO> getTopKeywords(int limit)
    {
        String key = "keywords:top:all:limit:" + limit;
        // check redis for cached result
        try
        {
            String cached = redisTemplate.opsForValue().get(key);
            if(cached != null)
            {
                // deserialize the json string back into a list<KeywordFrequencyDTO>
                return objectMapper.readValue(cached, new TypeReference<List<KeywordFrequencyDTO>>(){});
            }
        }
        catch(Exception e)
        {
            // if redis is down or something else goes wrong, just fall through to the db query
        }

        // if we don't have a cached result, query the db
        List<Object[]> results = jobPostingKeywordRepository.findTopKeywords(PageRequest.of(0, limit));
        List<KeywordFrequencyDTO> dtos = results.stream()
                .map(result -> new KeywordFrequencyDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue()))
                .toList();

        // serialize the list to a json string and store in redis cache
        try
        {
            String serialized = objectMapper.writeValueAsString(dtos);
            redisTemplate.opsForValue().set(key, serialized, CACHE_TTL);
        }
        catch(Exception e)
        {
            // if we can't write to redis, that's fine, just return db result
        }

        return dtos;
    }

    public List<KeywordFrequencyDTO> getTopKeywordsSince(int days, int limit)
    {
        String key = "keywords:top:days:" + days + ":limit:" + limit;

        // check redis for cached result
        try
        {
            String cached = redisTemplate.opsForValue().get(key);
            if(cached != null)
            {
                // deserialize the json string back into a list<KeywordFrequencyDTO>
                return objectMapper.readValue(cached, new TypeReference<List<KeywordFrequencyDTO>>(){});
            }
        }
        catch(Exception e)
        {
            // if redis is down or something else goes wrong, just fall through to the db query
        }

        // if we don't have a cached result, query the db
        List<Object[]> results = jobPostingKeywordRepository.findTopKeywordsSince(LocalDateTime.now().minusDays(days), PageRequest.of(0, limit));
        List<KeywordFrequencyDTO> dtos = results.stream()
                .map(result -> new KeywordFrequencyDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue()))
                .toList();

        // serialize the list to a json string and store in redis cache
        try
        {
            String serialized = objectMapper.writeValueAsString(dtos);
            redisTemplate.opsForValue().set(key, serialized, CACHE_TTL);
        }
        catch(Exception e){}

        return dtos;
    }

    public List<KeywordTrendDTO> getTrendingKeywords(int days, int limit)
    {
        String key = "keywords:trending:days:" + days + ":limit:" + limit;

        try
        {
            String cached = redisTemplate.opsForValue().get(key);
            if(cached != null)
            {
                return objectMapper.readValue(cached, new TypeReference<List<KeywordTrendDTO>>(){});
            }
        }
        catch(Exception e){}

        List<Object[]> results = jobPostingKeywordRepository.findTrendingKeywords(LocalDateTime.now().minusDays(days), LocalDateTime.now().minusDays(days + days),  PageRequest.of(0, limit));
        List<KeywordTrendDTO> dtos = results.stream()
                .map(result -> new KeywordTrendDTO(
                        ((String) result[0]),
                        ((Number) result[1]).longValue(),
                        ((Number) result[2]).longValue()))
                .toList();

        try
        {
            String serialized = objectMapper.writeValueAsString(dtos);
            redisTemplate.opsForValue().set(key, serialized, CACHE_TTL);
        }
        catch(Exception e){}

        return dtos;
    }
}
