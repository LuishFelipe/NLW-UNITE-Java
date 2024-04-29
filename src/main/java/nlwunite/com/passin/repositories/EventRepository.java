package nlwunite.com.passin.repositories;

import nlwunite.com.passin.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {


}
