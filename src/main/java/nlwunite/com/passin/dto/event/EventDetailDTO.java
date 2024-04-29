package nlwunite.com.passin.dto.event;

public record EventDetailDTO(
        String id,
        String title,
        String details,
        String slug,
        Integer maximumAttendee,
        Integer attendeesAmount) {

}
