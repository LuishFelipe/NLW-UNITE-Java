package nlwunite.com.passin.services;

import lombok.RequiredArgsConstructor;
import nlwunite.com.passin.domain.attendee.Attendee;
import nlwunite.com.passin.domain.event.Event;
import nlwunite.com.passin.domain.event.exceptions.EventFullException;
import nlwunite.com.passin.domain.event.exceptions.EventNotFoundException;
import nlwunite.com.passin.dto.attendee.AttendeeIdDTO;
import nlwunite.com.passin.dto.attendee.AttendeeRequestDTO;
import nlwunite.com.passin.dto.event.EventIdDTO;
import nlwunite.com.passin.dto.event.EventRequestDTO;
import nlwunite.com.passin.dto.event.EventResponseDTO;
import nlwunite.com.passin.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    private String createSlug(String text){
        String normalize = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalize.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID:"+eventId));
    }

    public EventResponseDTO getEventDetail(String eventId){
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetailS(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        if(event.getMaximumAttendees() <= attendeeList.size()){
            throw new EventFullException("Event is full");
        }

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee((newAttendee));

        return new AttendeeIdDTO(newAttendee.getId());

    }
}
