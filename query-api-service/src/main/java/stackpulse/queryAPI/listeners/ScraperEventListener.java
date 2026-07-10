package stackpulse.queryAPI.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import stackpulse.queryAPI.events.ScrapeCompletedEvent;

import java.util.Set;

@Component
public class ScraperEventListener
{
    private static final Logger log = LoggerFactory.getLogger(ScraperEventListener.class);

    private final RedisTemplate<String, String> redisTemplate;

    public ScraperEventListener(RedisTemplate<String, String> redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "scrape-completed", groupId = "query-api-cache-invalidation")
    public void onScrapeCompleted(ScrapeCompletedEvent event)
    {
        log.info("Received scrape-completed event: {} new postings at {}", event.newPostingsFound(), event.completedAt());

        Set<String> keys = redisTemplate.keys("keywords:*");
        if (keys != null && !keys.isEmpty())
        {
            redisTemplate.delete(keys);
            log.info("Evicted {} entries from redis", keys.size());
        }
    }
}
