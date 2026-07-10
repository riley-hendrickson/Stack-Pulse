package stackpulse.queryAPI.events;

import java.time.Instant;

public record ScrapeCompletedEvent(Instant completedAt, int newPostingsFound)
{
}
