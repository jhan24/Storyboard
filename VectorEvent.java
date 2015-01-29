/**
 * Vector Scaling Commands:
 * General format for vector scaling is:
 * _V,[...],startx,starty,endx,endy
 * <p/>
 * The process is very similar to normal
 * scaling, except that proportions do not have
 * to be kept in restraint.
 * <p/>
 * You can resize the length (X) of the image a different
 * value than the height (Y) of the image.
 * <p/>
 * startX, startY are the scale factor at the beginning.
 * endX, endY are the scale factor at the end of the animation.
 * 1 = 100%, 2 = 200%, and so on. Decimals are permitted.
 */

/**
 * VectorEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The VectorEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class VectorEvent extends StoryboardEvent {

    protected double startX = 0;
    protected double startY = 0;
    protected double endX = 0;
    protected double endY = 0;

    //constructor
    public VectorEvent(int easing, int startTime, int endTime,
                      double startX, double startY, double endX,
                      double endY) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        e = EventType.V;
    }

    //getters
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }

    /**
     * Previous checking for vector event, it also
     * takes previous values from scale event as well
     * as vector events (since both do scaling).
     *
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof VectorEvent) {
            VectorEvent v = (VectorEvent) o;
            if (v.getEndTime() <= this.startTime) {
                this.startX = v.getEndX();
                this.startY = v.getEndY();
                return true;
            }
        } else if (o instanceof ScaleEvent) {
            ScaleEvent s = (ScaleEvent) o;
            if (s.getEndTime() <= this.startTime) {
                this.startX = s.getEndScaling();
                this.startY = s.getEndScaling();
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return " V," + easing + "," + startTime + "," + endTime +
                "," + startX + "," + startY + "," + endX + "," +
                endY;
    }

}
