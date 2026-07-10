package stackpulse.scraper.events;

import java.time.Instant;

public record ScrapeCompletedEvent(Instant completedAt, int newPostingsFound)
{
}
