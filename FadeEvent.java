/**
 * Fading Methods
 * General format for writing in SB is:
 * "_F,[...],startOpacity,endOpacity"
 * <p/>
 * Non-universal parameters for fade:
 * startOpacity, endOpacity
 * <p/>
 * 0 is invisible, and 1 is fully visible.
 */

/**
 * FadeEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The FadeEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the fade event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class FadeEvent extends StoryboardEvent {

    protected double startingOpacity = 0;
    protected double endingOpacity = 0;

    /**
     * Constructor that stores variables into
     * their proper places
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startingOpacity the initial opacity of the object (0 to 1)
     * @param endingOpacity   the final opacity of the object
     */
    public FadeEvent(int easing, int startTime, int endTime,
                     double startingOpacity, double endingOpacity) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingOpacity = startingOpacity;
        this.endingOpacity = endingOpacity;
        e = EventType.F;
    }

    //getter methods
    public double getEndingOpacity() {
        return endingOpacity;
    }

    public double getStartingOpacity() {
        return endingOpacity;
    }

    /**
     * Previous checking for FadeEvent
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof FadeEvent) {
            FadeEvent f = (FadeEvent) o;
            if (f.getEndTime() <= this.startTime) {
                this.startingOpacity = f.getEndingOpacity();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the event line that will be printed into the
     * .osb file
     *
     * @return String with the event line
     */
    public String toString() {
        return " F," + easing + "," + startTime + "," + endTime +
                "," + startingOpacity + "," + endingOpacity;
    }
}
