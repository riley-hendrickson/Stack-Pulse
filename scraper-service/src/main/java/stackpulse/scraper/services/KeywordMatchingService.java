package stackpulse.scraper.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import stackpulse.scraper.entities.KnownKeyword;
import stackpulse.scraper.repositories.KnownKeywordRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordMatchingService
{
    private final KnownKeywordRepository knownKeywordRepository;
    private List<KnownKeyword> knownKeywords;

    public KeywordMatchingService(KnownKeywordRepository knownKeywordRepository)
    {
        this.knownKeywordRepository = knownKeywordRepository;
    }

    @PostConstruct
    public void loadKeywords()
    {
        this.knownKeywords = knownKeywordRepository.findAll();
    }

    public List<String> findMatches(String description)
    {


        return null;
    }
}
