package stackpulse.scraper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stackpulse.scraper.entities.KnownKeyword;

public interface KnownKeywordRepository extends JpaRepository<KnownKeyword, Long>
{

}
