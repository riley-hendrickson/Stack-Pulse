package stackpulse.scraper.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import stackpulse.scraper.entities.KnownKeyword;
import stackpulse.scraper.repositories.KnownKeywordRepository;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class KeywordMatchingService
{
    private final KnownKeywordRepository knownKeywordRepository;
    private Map<Pattern, String> keywordPatterns;

    public KeywordMatchingService(KnownKeywordRepository knownKeywordRepository)
    {
        this.knownKeywordRepository = knownKeywordRepository;
    }

    @PostConstruct
    public void loadKeywords()
    {
        keywordPatterns = new HashMap<>();
        List<KnownKeyword> knownKeywords = knownKeywordRepository.findAll();
        for(KnownKeyword knownKeyword : knownKeywords)
        {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(knownKeyword.getKeyword()) + "\\b", Pattern.CASE_INSENSITIVE);
            keywordPatterns.put(pattern, knownKeyword.getKeyword());
            if(knownKeyword.getAliases() != null)
            {
                for(String alias : knownKeyword.getAliases())
                {
                    Pattern aliasPattern = Pattern.compile("\\b" + Pattern.quote(alias) + "\\b", Pattern.CASE_INSENSITIVE);
                    keywordPatterns.put(aliasPattern, knownKeyword.getKeyword());
                }
            }
        }
    }

    public List<String> findMatches(String description)
    {
        Set<String> matches = new HashSet<>();

        for(Map.Entry<Pattern, String> entry : keywordPatterns.entrySet())
        {
            if(matches.contains(entry.getValue())) continue;

            if(entry.getKey().matcher(description).find()) matches.add(entry.getValue());
        }

        return matches.stream().toList();
    }
}
