package stackpulse.queryAPI.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Keywords", description = "Query technology keyword frequency and trends across recent and old job postings")
public class KeywordController
{
    private final KeywordQueryService keywordQueryService;

    public KeywordController(KeywordQueryService keywordQueryService)
    {
        this.keywordQueryService = keywordQueryService;
    }

    @Operation(summary = "Get top keywords",
            description = "Returns the most frequently occurring technology keywords across all job postings, optionally filtered to a recent time window")
    @ApiResponses ({@ApiResponse(responseCode = "200", description = "Successfully retrieved keyword frequencies",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = KeywordFrequencyDTO.class)))) })
    @GetMapping("/top")
    public ResponseEntity<List<KeywordFrequencyDTO>> getTopKeywords(
            @Parameter(description = "Maximum number of keywords to return", example = "20")
            @RequestParam(defaultValue = "20") int limit,
            @Parameter(description = "If provided, only counts keywords from job postings scraped within this many days")
            @RequestParam(required = false) Integer days)
    {
        if (days == null) return ResponseEntity.ok().body(keywordQueryService.getTopKeywords(limit));
        else return ResponseEntity.ok().body(keywordQueryService.getTopKeywordsSince(days, limit));
    }

    @Operation(summary = "Get trending keywords",
            description = "Returns keywords that have grown the most in frequency over a recent period compared to the prior equivalent period")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successfully retrieved trending keywords",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = KeywordTrendDTO.class)))) })
    @GetMapping("/trending")
    public ResponseEntity<List<KeywordTrendDTO>> getTrendingKeywords(
            @Parameter(description = "Maximum number of keywords to return", example = "20")
            @RequestParam(defaultValue = "20") int limit,
            @Parameter(description = "Number of days in each comparison period", example = "30")
            @RequestParam(defaultValue = "30") int days)
    {
        return ResponseEntity.ok().body(keywordQueryService.getTrendingKeywords(days, limit));
    }
}
