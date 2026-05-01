package stackpulse.queryAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordTrendDTO
{
    private String keyword;
    private long recentCount;
    private long priorCount;
    private long delta;

    public KeywordTrendDTO(String keyword, long recentCount, long priorCount)
    {
        this.keyword = keyword;
        this.recentCount = recentCount;
        this.priorCount = priorCount;
        this.delta = recentCount - priorCount;
    }
}