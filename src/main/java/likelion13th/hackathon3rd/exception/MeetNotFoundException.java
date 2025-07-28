package likelion13th.hackathon3rd.exception;

public class MeetNotFoundException extends RuntimeException {
    
    public MeetNotFoundException(String message) {
        super(message);
    }
    
    public MeetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}