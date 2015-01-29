/**
 * Color commands:
 * General format for coloring is:
 * _C,[...],r1,g1,b1,r2,g2,b2
 * <p/>
 * r1, g1, b1 is the starting component-wise color (in RGB)
 * r2, g2, b2 is the ending component-wise color
 * <p/>
 * Sprites with 255,255,255 will be their original colour
 * after this is applied. Sprites with 0,0,0 will be totally
 * black. Anywhere in between will result in subtractive coloring.
 * Thus, use bright greyscale sprites - they work the best!
 */

/**
 * ColorEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The ColorEvent class extends the StoryboardEvent.java
 * class. This class is treated as an instance
 * of the move event that occurs in osu!'s storyboards.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class ColorEvent extends StoryboardEvent {

    protected int r1, r2, b1, b2, g1, g2 = 0;

    //constructor that puts arguments into variables
    public ColorEvent(int easing, int startTime, int endTime,
                     int r1, int g1, int b1, int r2, int g2, int b2) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.r1 = r1;
        this.r2 = r2;
        this.b1 = b1;
        this.b2 = b2;
        this.g1 = g1;
        this.g2 = g2;
        e = EventType.C;
    }

    //getters
    public int getR1() {
        return r1;
    }

    public int getG1() {
        return g1;
    }

    public int getB1() {
        return b1;
    }

    public int getR2() {
        return r2;
    }

    public int getG2() {
        return g2;
    }

    public int getB2() {
        return b2;
    }

    /**
     * Previous checking for ColorEvent
     *
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public boolean previousChecking(StoryboardEvent o) {
        if (o instanceof ColorEvent) {
            ColorEvent c = (ColorEvent) o;
            if (c.getEndTime() <= this.startTime) {
                this.r1 = c.getR2();
                this.b1 = c.getB2();
                this.g1 = c.getG2();
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return " C," + easing + "," + startTime + "," + endTime +
                "," + r1 + "," + g1 + "," + b1 + "," +
                r2 + "," + g2 + "," + b2;
    }

}
