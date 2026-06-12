package stackpulse.queryAPI.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A technology keyword and its frequency across job postings")
public class KeywordFrequencyDTO
{
    @Schema(description = "The technology keyword", example = "java")
    private String keyword;

    @Schema(description = "Number of job postings containing this keyword", example = "142")
    private long frequency;
}
