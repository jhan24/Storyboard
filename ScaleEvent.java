/**
 * Created by Jinsong on 1/26/2015.
 */

/**
 * Scaling Commands
 * General format for scaling images in SB is:
 * _S, [...], startScale, endScale
 * <p/>
 * Non-universal parameters for move:
 * startScale, endScale
 * <p/>
 * startScale is the scale factor at the beginning of the animation,
 * while endScale is at the end. 1 = 100%, 2 = 200%, and so on.
 * Decimals are permitted.
 */

/**
 * ScaleEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The ScaleEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * PLEASE NOTE that this function IMPLIES that the current
 * sprite has the SAME scaling (percentage-wise) both horizontally
 * and vertically, meaning that the object's image is still a proportion of
 * its original size. If not, an exception will be thrown, and an error will
 * occur. Use Vector Scaling if you need to!
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */

public class ScaleEvent extends StoryboardEvent {

    protected double startScaling = 0;
    protected double endScaling = 0;

    //constructor for sacling
    public ScaleEvent(int easing, int startTime, int endTime,
                     double startScaling, double endScaling) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startScaling = startScaling;
        this.endScaling = endScaling;
        e = EventType.S;
    }

    //getters
    public double getStartScaling() {
        return startScaling;
    }

    public double getEndScaling() {
        return endScaling;
    }

    /**
     * Previous checking for Scale Event. This is a lot more
     * complex since the program checks to make sure that
     * the previous values for scaling (if from a vector event)
     * have the same x ratio and y ratio proportions. If not,
     * an exception/warning is thrown.
     *
     * Scale Event can take previous values from both
     * scale and vectorScale.
     *
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof ScaleEvent) {
            ScaleEvent s = (ScaleEvent) o;
            if (s.getEndTime() <= this.startTime) {
                this.startScaling = s.getEndScaling();
                return true;
            }
        } else if (o instanceof VectorEvent) {
            VectorEvent v = (VectorEvent) o;
            if (v.getEndTime() <= this.startTime) {
                try {
                    if (v.getEndY() != v.getEndX()) {
                        throw new ProportionDisagreementException(this.startTime);
                    }
                } catch (ProportionDisagreementException e) {
                    System.out.println(e.getMessage());
                }
                this.startScaling = v.getEndX();
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return " S," + easing + "," + startTime + "," + endTime +
                "," + startScaling + "," + endScaling;
    }
}
