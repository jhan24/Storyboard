/**
 * Rotation Commands:
 * General format for rotating is:
 * _R,[...],startAngle,endAngle
 * <p/>
 * Angles are written IN RADIANS! That unit circle you learned
 * back in trig is coming back to haunt you once again...
 * <p/>
 * Positive angles are clockwise rotation.
 */

/**
 * RotationEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The RotationEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class RotationEvent extends StoryboardEvent {

    protected double startAngle = 0;
    protected double endAngle = 0;

    //constructor stores arguments into variables
    public RotationEvent(int easing, int startTime, int endTime,
                      double startAngle, double endAngle) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        e = EventType.R;
    }

    //getters
    public double getStartAngle() {
        return startAngle;
    }

    public double getEndAngle() {
        return endAngle;
    }

    /**
     * Previous checking for rotation.
     *
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof RotationEvent) {
            RotationEvent r = (RotationEvent) o;
            if (r.getEndTime() <= this.startTime) {
                this.startAngle = r.getEndAngle();
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return " R," + easing + "," + startTime + "," + endTime +
                "," + startAngle + "," + endAngle;
    }
}
