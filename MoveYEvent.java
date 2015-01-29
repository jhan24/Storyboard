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
 * MoveYEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The MoveYEvent class extends the MoveEvent class
 * which extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class MoveYEvent extends MoveEvent {

    public MoveYEvent(int easing, int startTime, int endTime,
                      int startY, int endY) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startY = startY;
        this.endY = endY;
        e = EventType.MY;
    }

    /**
     * Previous checking in this case only checks if the
     * other object is an instance of MoveEvent. This is because
     * MoveX and MoveY ALWAYS infers that the user is taking
     * previous values, so the X and Y coordinates will always
     * be stored for both events, regardless of whether or not
     * they are actually used/printed.
     *
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
                this.startY = m.getEndY();
                this.startX = m.getEndX();
                this.endX = m.getEndX();
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return " MY," + easing + "," + startTime + "," + endTime +
                "," + startY + ","  + endY;
    }
}
