/**
 * Movement Commands
 * General format for moving images in SB is:
 * _M,[...],startX,startY,endX,endY
 * _MX,[...],startX,endX
 * _MY,[...],startY,endY
 * <p/>
 * Non-universal parameters for move:
 * startX, startY, endX, endY
 * <p/>
 * Should be self explanatory - enter pixels for the starting
 * and ending position. Remember that (0,0) is the top left while
 * (640,480) / (1366,768) is the bottom right.
 *
 * NOTE: Based on further intel, I have realized that the top left
 * and right are not the actual listed values. Go do some trial
 * and error yourself and find out.
 */

/**
 * MoveEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The MoveEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class MoveEvent extends StoryboardEvent {

    protected int startX = 0;
    protected int startY = 0;
    protected int endX = 0;
    protected int endY = 0;

    public MoveEvent() {

    }

    /**
     * Default constructor for MoveEvent, puts arguments
     * into variables.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startX    the starting X position
     * @param startY    the starting Y position
     * @param endX      the ending X position
     * @param endY      the ending Y position
     */
    public MoveEvent(int easing, int startTime, int endTime,
                     int startX, int startY, int endX, int endY) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        e = EventType.M;
    }

    //getter variables
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    /**
     * Previous checking for MoveEvent
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof MoveEvent) {
            MoveEvent m = (MoveEvent) o;
            if (m.getEndTime() <= this.startTime) {
                this.startX = m.getEndX();
                this.startY = m.getEndY();
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
        return " M," + easing + "," + startTime + "," + endTime +
                "," + startX + "," + startY + "," + endX + "," +
                endY;
    }

}
