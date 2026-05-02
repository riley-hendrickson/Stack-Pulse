package stackpulse.queryAPI.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stackpulse.queryAPI.dtos.KeywordFrequencyDTO;
import stackpulse.queryAPI.dtos.KeywordTrendDTO;
import stackpulse.queryAPI.services.KeywordQueryService;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController
{
    private final KeywordQueryService keywordQueryService;

    public KeywordController(KeywordQueryService keywordQueryService)
    {
        this.keywordQueryService = keywordQueryService;
    }
    @GetMapping("/top")
    public ResponseEntity<List<KeywordFrequencyDTO>> getTopKeywords(@RequestParam(defaultValue = "20") int limit,
                                                                    @RequestParam(required = false) Integer days)
    {
        if(days == null) return ResponseEntity.ok().body(keywordQueryService.getTopKeywords(limit));
        else return ResponseEntity.ok().body(keywordQueryService.getTopKeywordsSince(days, limit));
    }
    @GetMapping("/trending")
    public ResponseEntity<List<KeywordTrendDTO>> getTrendingKeywords(@RequestParam(defaultValue = "20") int limit,
                                                                     @RequestParam(defaultValue = "30") int days)
    {
        return ResponseEntity.ok().body(keywordQueryService.getTrendingKeywords(days, limit));
    }
}
