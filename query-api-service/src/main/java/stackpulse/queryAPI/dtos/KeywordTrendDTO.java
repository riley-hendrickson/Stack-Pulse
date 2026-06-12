package stackpulse.queryAPI.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A technology keyword and its associated trend across two time periods")
public class KeywordTrendDTO
{
    @Schema(description = "The technology keyword", example = "kubernetes")
    private String keyword;

    @Schema(description = "Occurrences in the recent period", example = "87")
    private long recentCount;

    @Schema(description = "Occurrences in the prior equivalent period", example = "54")
    private long priorCount;

    @Schema(description = "Difference between recent and prior counts", example = "33")
    private long delta;

    public KeywordTrendDTO(String keyword, long recentCount, long priorCount)
    {
        this.keyword = keyword;
        this.recentCount = recentCount;
        this.priorCount = priorCount;
        this.delta = recentCount - priorCount;
    }
}