/**
 * Miscellaneous commands
 * <p/>
 * Mostly includes the P type function, listed as:
 * _P,[...],p
 * where p is the effect parameter to apply:
 * H: Horizontal flip
 * V: Vertical flip
 * A: Additive-blend color
 * The three universal parameters seem to have no effect on these.
 */

/**
 * ParameterEvent.java
 * A part of the JOSG project.
 * For more information about this project, visit one of the
 * main classes (Storyboard.java/StoryboardEvent.java,...)
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * Literally known as the odd bunch. They perform a variety of options that
 * are probably of some use. I believe that timing and easing
 * has little effect on this method, since tweening/animation does not apply
 * to these. The flip happens immediately on the start time.
 *
 * Still extends StoryboardEvent.java, even though it really shouldn't.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public class ParameterEvent extends StoryboardEvent {

    protected String type = "";

    public ParameterEvent(int easing, int startTime, int endTime,
                         String type) {
        this.easing = easing;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;

        //type checking, since there are 3 different events within
        //parameter event, and nobody wants to make 3 dummy subclasses
        //just for this.
        if (type.equals("H")) {
            e = EventType.PH;
        } else if (type.equals("V")) {
            e = EventType.PV;
        } else if (type.equals("A")) {
            e = EventType.PA;
        }
    }

    public String getType() {
        return type;
    }

    //since time isn't a big factor here, and since there's only
    //a on and off position for these, previous checking isn't
    //necessary.
    public boolean previousChecking(StoryboardEvent o) {
        return false;
    }

    public String toString() {
        return " P" + "," + easing + "," + startTime +
                "," + endTime + "," + type;
    }

}
