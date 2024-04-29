package nlwunite.com.passin.controllers;


import lombok.RequiredArgsConstructor;
import nlwunite.com.passin.dto.attendee.AttendeeIdDTO;
import nlwunite.com.passin.dto.attendee.AttendeeRequestDTO;
import nlwunite.com.passin.dto.attendee.AttendeesListResponseDTO;
import nlwunite.com.passin.dto.event.EventIdDTO;
import nlwunite.com.passin.dto.event.EventRequestDTO;
import nlwunite.com.passin.dto.event.EventResponseDTO;
import nlwunite.com.passin.services.AttendeeService;
import nlwunite.com.passin.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventService.getEventDetail(eventId);

        return ResponseEntity.ok(event);
    }

    @PostMapping()
    public ResponseEntity<EventIdDTO> postEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventId = this.eventService.createEvent(body);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand((eventId.eventId())).toUri();
        return ResponseEntity.created(uri).body(eventId);
    }

    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeesListResponseDTO attendeeList = this.attendeeService.getEventsAttendee(eventId);

        return ResponseEntity.ok(attendeeList);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable  String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body((attendeeIdDTO));
    }

}
