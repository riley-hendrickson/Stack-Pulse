package stackpulse.scraper.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdzunaResponse
{
    @JsonProperty("results")
    private List<AdzunaJobResult> jobResults;
    @JsonProperty("count")
    private int count;
}
