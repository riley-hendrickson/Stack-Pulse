package stackpulse.scraper.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdzunaCompany
{
    @JsonProperty("display_name")
    private String companyName;
}
