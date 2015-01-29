/**
 * Created by Jinsong on 1/27/2015.
 */
public class DuplicateEventException extends Exception {

    public DuplicateEventException(int time) {
        super("WARNING: There is a duplicate event of the same type " +
                "that occurs during this time. \nThis is most likely " +
                "unintentional! This warning " +
                "occurred at time " + time + ".");
    }

}
