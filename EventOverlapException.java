/**
 * Created by Jinsong on 1/26/2015.
 */
public class EventOverlapException extends Exception {

    public EventOverlapException(int time) {
        super("WARNING: One of your events currently begins in the middle " +
                "of another of the same event type.\nMake sure that this is " +
                "intentional, else unintended results may occur! " +
                "Event happened at start time " + time + ".");
    }
}
