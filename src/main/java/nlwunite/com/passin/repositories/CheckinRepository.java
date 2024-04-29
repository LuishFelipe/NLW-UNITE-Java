package nlwunite.com.passin.repositories;

import nlwunite.com.passin.domain.checkin.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<CheckIn, String> {
    Optional<CheckIn> findByAttendeeId(String attendeeId);

}
