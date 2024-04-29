package nlwunite.com.passin.services;

import lombok.RequiredArgsConstructor;
import nlwunite.com.passin.domain.attendee.Attendee;
import nlwunite.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisterException;
import nlwunite.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import nlwunite.com.passin.domain.checkin.CheckIn;
import nlwunite.com.passin.dto.attendee.AttendeeBadgeDTO;
import nlwunite.com.passin.dto.attendee.AttendeeDetails;
import nlwunite.com.passin.dto.attendee.AttendeesListResponseDTO;
import nlwunite.com.passin.dto.attendee.BadgeDTO;
import nlwunite.com.passin.repositories.AttendeeRepository;
import nlwunite.com.passin.repositories.CheckinRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

         return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String eventId, String email){
        Optional<Attendee> isRegister = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isRegister.isPresent()) throw new AttendeeAlreadyRegisterException("Attendee is already register!");
    }

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public AttendeeBadgeDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found by ID: "+attendeeId));

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/checkin").buildAndExpand(attendeeId).toUri().toString();

        BadgeDTO badgeDTO = new BadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeDTO(badgeDTO);
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.CheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found by ID: "+attendeeId));
    }
}
