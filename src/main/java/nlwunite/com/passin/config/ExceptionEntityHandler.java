package nlwunite.com.passin.config;

import nlwunite.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisterException;
import nlwunite.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import nlwunite.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import nlwunite.com.passin.domain.event.exceptions.EventFullException;
import nlwunite.com.passin.domain.event.exceptions.EventNotFoundException;
import nlwunite.com.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyRegisterException.class)
    public ResponseEntity handleAttendeeAlreadyRegister(AttendeeAlreadyRegisterException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
