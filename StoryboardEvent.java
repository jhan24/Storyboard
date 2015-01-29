/**
 * JOSBGL PROGRAM INFORMATION
 * <p/>
 * Welcome to JOSBGL: Java osu! Storyboard Generation Language!
 * <p/>
 * Being written in Java, this is basically a different way to generate
 * .sb files and creating storyboards in osu! without being limited by the in-game
 * designer or by the very outdated code that the .sb file works.
 * <p/>
 * In other words, you can use the native functions and support of
 * Java to generate .sb files (meaning native support of looping, arrays,
 * and all that good stuff that you would have a lot of trouble doing
 * by directly editing the .sb file).
 * <p/>
 * In other words, this is very similar to the SGL program, except for
 * the fact that this one requires you to know Java to take full
 * advantage of the program. Of course, if you do know Java, then
 * you'll understand that this one has a lot more potential...
 * <p/>
 * For example, you can use all of java's features to aid you in creating
 * animations. This means you can easily implement loops, arrays,
 * ArrayLists, collections classes, and anything else that you need
 * from java.
 * <p/>
 * If that's not enough, I've written several built-in
 * exceptions (warnings in this case since they're all caught by
 * Try-Catch statements and are suppressed) and also a previous
 * value obtainer - you don't need to specify the previous
 * values of events that you're writing if you want the program
 * to auto-assume based on the last event of the same type that
 * you called.
 * <p/>
 * You will still want to write most of your lines in numerical order,
 * or at least group them in numerical order by the same event type.
 * If I wanted to make sure that you could stick the events in any order
 * and have previous-checking still work, the O(n) (resource intensity/
 * algorithm intensity) would skyrocket through the roof. It'd probably
 * have to be O(2^n), if not worse, and I'm not willing to do that.
 * <p/>
 * This program is currently only supportive of sprites in storyboards.
 * Animation and sfx support to be added, although I don't think maybe people
 * utilize sound effects in their storyboards...I think that would just
 * be weird, although I guess games like Project Diva
 * and the original Ouendan do that.
 * <p/>
 * Seeing as how SBs have evolved in osu!, there's probably not much of a
 * reason to use SFXs in SBs.
 * <p/>
 * The overall structure of this program is as follows. There is a
 * Storyboard abstract class which implements all methods universal
 * for all objects in a storyboard. Its subclasses (Sprite and Animation)
 * will extend the Storyboard class and implement their own methods
 * if necessary. Animation has YET to be implemented.
 *
 * All events are their own class, and each one extends StoryboardEvent,
 * which contains all the methods universal to an event. This easily allows
 * multiple events to be compared to each other, and allows for the easy
 * checking for duplicate events of the same type that start at the same point,
 * or events of the same type that overlap with each other. Also allows for
 * proportion checking (look in Vector/ScaleEvent if interested).
 *
 * This format also allows the events to be sorted in order of their
 * starting time, making it easy to find the previous values of the
 * event if necessary. You don't need to input the starting values of
 * an event if you just want to use the last one that was used.
 * i.e. for a fade event, you can just type the end opacity, and the
 * program will infer that the start opacity is the end opacity of the
 * last fade event, if one has occurred.
 *
 * The abstract-class extending thing also applies to SB objects, meaning
 * that the SB objects are ordered in Layer first and then based on when
 * their first event began. This allows for easy printing in numerical
 * order!
 *
 * Yes, if you're wondering, this is pretty dang memory-intensive.
 * A lot of things are O(n) if not worse. This is due to all the
 * checking that goes on (overlap/duplicate/previous).
 * That's probably not good when you're trying to create a
 * 20-minute long SB with over a million events,
 * but...if worst comes to worst, just split
 * up the events/file and copy+paste the sections
 * together yourself. Still better than nothing, right?
 * Don't worry, that epic storyboard made by 11t
 * (I think the song is EOS or something) is only
 * 250,000 lines. I'm pretty sure this can handle that
 * It might start failing once you reach the millions
 * though...
 *
 * I'll definitely work on memory optimization, but that
 * probably won't happen till I get this program to a working,
 * stable state first.
 *
 * TL;DR a simpler way to program storyboards using Java.
 */

/**
 * StoryboardEvent.java
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * This is the BACKBONE for how all events are treated.
 *
 * The storyboard event abstract class sets a framework
 * for how almost all storyboard events work and run.
 * It implements the variables that all events should
 * have (startTime, endTime, easing, etc.), and
 * universal methods as well, such as equals,
 * compareTo, overlap checking, duplicate
 * checking, etc.
 *
 * By implementing the Comparable class, the events
 * can be ordered based on start time. This also
 * allows for subclasses to override previous
 * checking, allowing them to have their own
 * method of getting previous variables, since
 * each event has different types of variables!
 * This is truly OOP programming at its finest!
 *
 * (lol not rly)
 *
 * Currently, ColorEvent, FadeEvent, MoveEvent, MoveXEvent, MoveYEvent,
 * ParameterEvent, RotationEvent, ScaleEvent, VectorEvent
 * extend this class.
 *
 * Changelog:
 * 1/27/2015: Finished draft.
 *
 * @author Erina
 * @version 1.00
 */
public abstract class StoryboardEvent implements Comparable<StoryboardEvent> {

    //variables that are universal to all/most storyboard events
    protected int startTime = 0;
    protected int endTime = 0;
    protected int easing = 0;
    protected EventType e = EventType.NULL;
    protected String sbLine = "";

    //-----------------------------------------Getter / Setters---------------------------------------------

    //getter methods for universal variables for storyboard events
    //to access them.

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getEasing() {
        return easing;
    }

    public EventType getEventType() {
        return e;
    }

    //-----------------------------------------Vitals---------------------------------------------

    /**
     * Overrides compareTo from java.lang.Comparable, which
     * allows for the sorting of events by startTime.
     * Note that if startTime is the same as the other
     * object's startTime, it checks to see if the event
     * type is also the same. If they aren't the same, it
     * returns -1. This is because if compareTo returns
     * 0, it means two objects are equal. If this happens
     * in a TreeSet, they will overwrite each other since
     * TreeSet uses compareTo for equals checking, and
     * not the equals method!
     *
     * @param other storyboard event that you want to compare to
     * @return negative if this event comes earlier, positive if
     * this event comes later, and zero if equal.
     */
    public int compareTo(StoryboardEvent other) {
        if (startTime == other.getStartTime()) {
            if (this.e != other.getEventType()) {
                return -1;
            }
        }
        return this.startTime - other.getStartTime();
    }

    /**
     * Overwrites equals method in java.lang.Object.
     * Checks whether or not two objects are equal.
     *
     * If they are equal, but not a reference of each
     * other, a DuplicateEventException is thrown, which
     * will cause a warning message to be printed in the
     * terminal window.
     *
     * @param other storyboard event to be compared with
     * @return true if equal, false if not.
     */
    public boolean equals(Object other) {
        try {
            if (null == other) {
                return false;
            }
            if (this == other) {
                return true;
            }
            if (!(other instanceof StoryboardEvent)) {
                return false;
            }
            StoryboardEvent that = (StoryboardEvent) other;
            if (this.e.equals(that.getEventType()) &&
                    startTime == that.getStartTime()) {
                throw new DuplicateEventException(startTime);
            }
        } catch (DuplicateEventException e) {
            System.out.println(e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Overwrites hashcode, vital java thing to do.
     *
     * @return int the object's new hashcode
     */
    public int hashCode() {
        int result = 17;
        result += 6 * startTime;
        result += 6 * e.ordinal();
        return result;
    }

    /**
     * Does a check to see if this event and
     * another event's start time and end time
     * overlap with each other. The two events
     * must be of the same type. If so,
     * EventOverlapException is thrown, which
     * prints a warning message in the terminal
     * window.
     *
     * @param o storyboard event to be compared with
     */
    public void overlapCheck(StoryboardEvent o) {
        try {
            if (o.getEventType() == this.e) { //check if events are of the same type
                if (o.getEndTime() > startTime &&
                        o.getStartTime() < startTime) {
                    throw new EventOverlapException(startTime);
                } else if (startTime < o.getStartTime() &&
                        endTime > o.getStartTime()) {
                    throw new EventOverlapException(startTime);
                }
            }
        } catch (EventOverlapException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Does a check to see if this event is
     * equal to another event, meaning do
     * two or more of the same event start
     * at the same time? If so, throw
     * the exception that is thrown from
     * equals method.
     *
     * @param o storyboard event to be compared with
     */
    public boolean duplicateCheck(StoryboardEvent o) {
        return this.equals(o);
    }

    /**
     * prints off the event line, differs for each
     * event type, so this is overriden in each of the
     * subclasses and left abstract here.
     *
     * @return String the event line to be printed
     */
    public abstract String toString();

    /**
     * Checks and obtains the previous values for the starting
     * values of this current event. The variables that it looks
     * for is different depending on event type, so this
     * is declared abstract and will be overriden in all
     * subclasses.
     *
     * @param o storyboard event to be compared with, and to
     *          attempt to extract previous values out of
     * @return true if values were obtained, false if this event
     * cannot take the previous values from the other (maybe
     * due to the fact that the other event doesn't come previous
     * to current, or if the event type aren't the same).
     */
    public abstract boolean previousChecking(StoryboardEvent o);
}
