package stackpulse.scraper.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "known_keywords")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KnownKeyword
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String keyword;
    @Column(columnDefinition = "TEXT[]")
    private List<String> aliases;
    private String category;
    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
