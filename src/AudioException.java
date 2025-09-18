/*
 * Class creates AudioException 
 */
public class AudioException extends Exception {
    /*
     * One constructor for only message
     */
    public AudioException(String message) {
        super(message);
    }

    /*
     * Other constructor for message and other error message
     */
    public AudioException(String message, Throwable cause) {
        super(message, cause);
    }
}
