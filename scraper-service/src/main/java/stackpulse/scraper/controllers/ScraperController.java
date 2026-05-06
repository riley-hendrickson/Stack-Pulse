package stackpulse.scraper.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stackpulse.scraper.services.ScraperService;

@RestController
@RequestMapping("/scraper")
public class ScraperController
{
    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService)
    {
        this.scraperService = scraperService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> triggerScrape()
    {
        scraperService.scrape();
        return ResponseEntity.accepted().body("Scrape completed successfully.");
    }
}
