package stackpulse.queryAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordFrequencyDTO
{
    private String keyword;
    private long frequency;
}
