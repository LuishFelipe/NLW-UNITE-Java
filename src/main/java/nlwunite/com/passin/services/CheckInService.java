package nlwunite.com.passin.services;


import lombok.AllArgsConstructor;
import nlwunite.com.passin.domain.attendee.Attendee;
import nlwunite.com.passin.domain.checkin.CheckIn;
import nlwunite.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import nlwunite.com.passin.repositories.CheckinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
        if(isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked-in");

    }

    public void CheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(newCheckIn);
    }

    public Optional<CheckIn> getCheckIn(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }

}
