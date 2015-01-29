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
 * Sprite.java
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * This is the first extension of the Storyboard abstract class,
 * extending it to be used with Sprite objects present on the
 * storyboard. All the normal SB functions can be used, as well
 * as native java language functions (such as loops, arrays,
 * collections, and anything else you wish) can be applied.
 * It's a lot easier to work with than just typing stuff
 * into notepad!
 *
 * This class extends Storyboard.java, which contains all
 * methods that are universal to storyboard objects.
 * This class extends Comparable, which allows for
 * the ordering of other Storyboard objects based on
 * certain criteria (layering, and then startTime)
 *
 * WARNING: Although we are using a sorted collection so you don't necessarily have to
 * enter events in the order that they start (time-wise), you still want to group
 * all same types of events together especially if you're using the program's
 * memory to infer the previous values (such as previous position, opacity, etc.)
 * If the events of that type aren't in order, the code will be using the wrong
 * previous values!
 *
 * Changelog:
 * 1/24/2015: Finished first draft. Application appears to work as intended.
 *
 * @author Erina
 * @version 1.00
 */

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class Sprite extends Storyboard implements Comparable<Storyboard> {

    //-----------------------------------------Constructors---------------------------------------------

    /**
     * Constructor for Sprite. This is the typical constructor,
     * which takes in the same parameters as the original osu
     * sb language requires.
     *
     * @param layer    the layer that the sprite is on
     * @param origin   sets a marker that determines at what position
     *                 events are applied to the image
     * @param filePath location of the sprite with respect to the
     *                 folder that the storyboard is located.
     * @param x        initial x position of the sprite
     * @param y        initial y position of the sprite
     */
    public Sprite(Layer layer, Origin origin, String filePath,
                  int x, int y) {
        this.layer = layer;
        this.origin = origin;
        this.filePath = filePath;
        this.x = x;
        this.y = y;
        this.startTime = -1;

        //TreeMap to store the events for the sprite in order of the time
        //that they appear in.
        compilation = new TreeSet<StoryboardEvent>();
    }

    /**
     * Simpler constructor for Sprite.
     * Use this if you really don't care about the initial values.
     *
     * @param filePath the location of the sprite with respect to the
     *                 folder that the storyboard is located.
     */
    public Sprite(String filePath) {
        this(Layer.Foreground, Origin.Centre, filePath,
                0, 0);
    }

    //-----------------------------------------Vitals---------------------------------------------

    /**
     * A bunch of vital Java methods that help make this program work.
     */

    /**
     * The default to string method, which prints out the stuff onto
     * the text file.
     *
     * @return the text to be printed into the SB file
     */
    public String toString() {
        String printing = "Sprite," + layer + "," + origin + "," +
                "\"" + filePath + "\"" + "," + x + "," + y;

        for (Object e : compilation) {
            printing += "\n";
            printing += e.toString();
        }

        return printing;
    }

    /**
     * Equals method to see if two sprites are the same as
     * one another.
     *
     * @param other the other sprite that we're comparing to
     * @return true if the two sprites are the same, false otherwise
     */
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof Sprite)) {
            return false;
        }
        Sprite that = (Sprite) other;

        return layer.equals(that.getLayer()) &&
                origin.equals(that.getOrigin()) &&
                filePath.equals(that.getFilePath()) &&
                startTime == that.getStartTime();
    }

    /**
     * HashCode method that should go in hand with equals, in case
     * the sprite class is ever used in a hashed collection or
     * something similar.
     *
     * @return an int that represents the hashcode of the object
     */
    public int hashCode() {
        int result = 17;
        result = 6 * layer.hashCode();
        result = 6 * origin.hashCode();
        result = 6 * filePath.hashCode();
        result = 6 * startTime;
        return result;
    }

    /**
     * Compares two different Sprites with each other, this will
     * ultimately and most importantly used to figure
     * out which sprite will be printed first. The natural
     * ordering of sprites is based on when their first
     * event occurs.
     *
     * @param other the sprite that we're comparing to
     * @return negative if this is less than other object,
     * zero if equal, and positive if greater.
     */
    public int compareTo(Storyboard other) {
        if (this.layer.compareTo(other.getLayerENUM()) < 0) {
            return -1;
        } else if (this.layer.compareTo(other.getLayerENUM()) > 0) {
            return 1;
        } else {
            if (other instanceof Sprite) {
                return this.startTime - other.getStartTime();
            }
            return -1;
        }
    }

    //-----------------------------------------Testing---------------------------------------------

}