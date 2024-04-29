package nlwunite.com.passin.domain.attendee.exceptions;

public class AttendeeAlreadyRegisterException extends RuntimeException{
    public AttendeeAlreadyRegisterException(String message){
        super(message);
    }

}
