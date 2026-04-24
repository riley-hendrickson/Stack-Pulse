package stackpulse.scraper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import stackpulse.scraper.services.ScraperService;

@SpringBootApplication
public class Main implements CommandLineRunner
{
    private final ScraperService scraperService;

    public Main(ScraperService scraperService)
    {
        this.scraperService = scraperService;
    }
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        scraperService.scrape();
    }
}