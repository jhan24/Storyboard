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
 * WARNING: Although we are using a sorted collection so you don't necessarily have to
 * enter events in the order that they start (time-wise), you still want to group
 * all same types of events together especially if you're using the program's
 * memory to infer the previous values (such as previous position, opacity, etc.)
 * If the events of that type aren't in order, the code will be using the wrong
 * previous values!
 */

import java.util.Set;
import java.util.TreeSet;

/**
 * Storyboard.java
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * This is the MAIN BACKBONE of the entire program.
 *
 * The storyboard abstract class which outlines the basic template
 * for all sprites and objects that will be placed onto a storyboard.
 * Basically defines what ALL OBJECTS on a storyboard can do, regardless
 * of type. For example, both SPRITES and ANIMATIONS in a storyboard
 * can use the fade method, can use the move method, etc. All of these
 * universal functions have been implemented here. Other functions
 * specific to the storyboard object will be written in the
 * respective subclass.
 *
 * Currently, only Sprite.java extends this class.
 * This class extends Comparable, which allows for
 * the ordering of other Storyboard objects based on
 * certain criteria (layering, and then startTime)
 *
 * Changelog:
 * 1/23/2015: First draft interface created. Outlines basic functions used by sprites.
 * 1/24/2015: Changed to abstract class to work with Comparable interface.
 * 1/27/2015: Moved over all universal methods
 *
 * @author Erina
 * @version 1.00
 */
public abstract class Storyboard implements Comparable<Storyboard> {

    /**
     * General hints and tips:
     * <p/>
     * A sprite is a non-animated object.
     * The general format to declare a sprite is as follows:
     * Sprite, "layer", "origin", "file path", x, y
     * <p/>
     * An animation is a object that changes its appearance over time.
     * The general format to declare an animated object is as follows:
     * Animation, "layer", "origin", "filepath", x, y, frameCount, frameDelay, looptype
     * <p/>
     * For animations, specify a filename like "sliderball.png," and name your files
     * "sliderball0.png" to "sliderball9.png" for a 10-frame animation.
     * <p/>
     * frameCount: number of frames in the animation
     * frameDelay: delay in milliseconds between each frame
     * loopType: either LoopForever or LoopOnce. Thus, use For-Loops in Java if you want to
     * loop a specified set amount.
     * <p/>
     * Note that objects in the .osb file are written in Z-Order, meaning that the order
     * is determined by the the order in which the files appear in the .osu file. Thus,
     * you may have to declare multiple instances of a file in order to keep the order
     * consistent with how the objects appear. I'll try to write the code so that
     * normal users don't have to take this fact into account.
     * <p/>
     * Layer: Background, Fail, Pass, Foreground. BG and FG are ALWAYS visible.
     * <p/>
     * Origin: TopLeft, TopCentre, TopRight, CentreLeft, Centre, CentreRight,
     * BottomLeft, BottomCentre, BottomRight. Not entirely sure what this does.
     * Default the object to centre and then move it to a specified location
     * using the Move command.
     * <p/>
     * X, Y: The position of the image. (0,0) is the top left corner while
     * (640,480) / (1366,768) is the bottom right. The latter is for the latest
     * widescreen storyboarding.
     * <p/>
     * File Path: Relative path of the image file, which should be in the same
     * directory as the .osu file. File path MUST be in double quotes.
     * Ex: ""SB\dark.jpg"
     * <p/>
     * Event Types: Fade, Move, Scale, Vector Scale (width+height separate), Rotate,
     * Colour, Loop, Event-Triggered Loop, Parameters
     * <p/>
     * Easing: 0 (none), 1 (start fast and slow down), 2 (start slow and speed up)
     * <p/>
     * Start time and End time should be self-explanatory.
     * <p/>
     * The typical event statement should be written as:
     * _event,easing,startTime,endTime,[params]
     * where the params are dependent upon which event-type you are running.
     * <p/>
     * All commands are written with the assumption that [...] exists.
     * This [...] refers to the parameters "easing, startTime, endTime."
     * <p/>
     * Good luck!
     */


    //-----------------------------------------Variables---------------------------------------------

    /**
     * Creating variables to be used in this class. Most of these store
     * important information about the object that was created.
     */
    protected Layer layer;
    protected Origin origin;
    protected String filePath = "";
    protected TreeSet<StoryboardEvent> compilation;
    protected int x = 0;
    protected int y = 0;
    protected int startTime = 0;

    //-----------------------------------------Helper Methods---------------------------------------------

    /**
     * This method adds in any event that was called by the user
     * and stores it into a TreeSet, where the events are all
     * ordered by their starting time (i.e. sorts them in order in
     * real time). This allows for the SB object and its events
     * to be easily printed if necessary.
     * <p>
     * Also calls several methods that do some checking over
     * the current events. It checks to see if there are multiple
     * events of the same type that start at the same time,
     * events of the same type that overlap with their start+end times,
     * and makes an attempt to grab the previous values of the last event
     * that occurred and stores them in as the start values for the event
     * if the user specifies for the event to do so.
     *
     * Also, if the event
     * is earlier than any of the events that have been called,
     * the start time of the current SB object is set to that time. This
     * makes the ordering of SB objects when printing in the text file a
     * lot more simple.
     *
     * Really useful, right? And you thought you'd just keep programming
     * in osu's little text file/design window, huh?
     *
     * For more information about TreeSets, visit
     * http://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html
     *
     * @param event the event to be added to the compilation TreeSet
     * @param previousCheck whether or not the user wants to retrieve
     *                      previous values of the same event
     */
    public void addToCompilation(StoryboardEvent event, boolean previousCheck) {
        int temp = event.getStartTime();

        //if this event starts earlier than the current starting time of the object,
        //make this the new start time of the sb object.
        if (this.startTime == -1 || this.startTime > temp) {
            this.startTime = temp;
        }

        //add the event into the compilation!
        compilation.add(event);

        //variable for looping
        boolean breakLoop = false;

        //overlap checking AND duplicate checking among all the elements in
        //the compilation. Duplicate Check IGNORES an event being a reference
        //itself, since that should only happen with the two events being
        //checked are "e" and "event," meaning that it's the current event
        //checking with itself that just went into the compilation.
        //tl;dr don't worry about it, it works. I think.
        if (!this.compilation.isEmpty()) {
            for (StoryboardEvent e : compilation) {
                event.overlapCheck(e);
                event.duplicateCheck(e);
            }
        }

        //Previous check, if the user specifies for it. Applies the values
        //of the same event type that happened previously to the current event.
        if (!this.compilation.isEmpty() && previousCheck == true) {
            Set<StoryboardEvent> stuff = compilation.descendingSet();
            for (StoryboardEvent e : stuff) {
                breakLoop = event.previousChecking(e);
                if (breakLoop) {
                    //probably not the best idea to use break, but I'm too lazy.
                    break;
                }
            }
        }
    }

    //-----------------------------------------Getter / Setters---------------------------------------------

    /**
     * Just a bunch of getters and setters for variables used in this program.
     * Not all methods are used, but it's safe to have access to them.
     *
     * I figured that there's not much of a point in resetting variable values
     * for this, so I left out all the setter methods.
     */

    public Layer getLayerENUM() {
        return layer;
    }

    public String getLayer() {
        return layer.toString();
    }

    public Origin getOriginENUM() {
        return origin;
    }

    public String getOrigin() {
        return origin.toString();
    }

    public String getFilePath() {
        return filePath;
    }

    public int getStartTime() {
        return startTime;
    }

    //-----------------------------------------Events---------------------------------------------

    /**
     * A bunch of event methods start here. The general format is that
     * the values that the user specifies goes into creating a new
     * instance of the event type, which then gets added to the
     * current TreeSet compilation, where it does a bunch of stuff
     * as listed in the documentation of addToCompilation.
     */
    //-----------------------------------------Fading---------------------------------------------

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
     * Default fading method here, input in the correct parameters
     * and it'll change the current sprite's opacity from one value
     * to another.
     *
     * @param easing       how the animation is done (tweening, default to zero)
     * @param startTime    the time that this event begins
     * @param endTime      the time that this even ends
     * @param startOpacity the initial opacity of the object (0 to 1)
     * @param endOpacity   the final opacity of the object
     */
    public void fade(int easing, int startTime, int endTime, double startOpacity,
                     double endOpacity) {
        StoryboardEvent fade = new FadeEvent(easing, startTime, endTime, startOpacity,
                endOpacity);
        addToCompilation(fade, false);
    }

    /**
     * A different fading method, used for when you have
     * a previous opacity for the current sprite stored,
     * and you'd like to easily use it again for the
     * initial opacity.
     *
     * @param easing     how the animation is done (tweening, default to zero)
     * @param startTime  the time that this event begins
     * @param endTime    the time that this even ends
     * @param endOpacity the final opacity of the object
     */
    public void fade(int easing, int startTime, int endTime,
                     double endOpacity) {
        StoryboardEvent fade = new FadeEvent(easing, startTime, endTime, 0,
                endOpacity);
        addToCompilation(fade, true);
    }

    //-----------------------------------------Movement---------------------------------------------

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
     * General movement method, if you input in the correct parameters
     * the sprite will move from a specified initial position to
     * its specified ending position.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startX    the starting X position
     * @param startY    the starting Y position
     * @param endX      the ending X position
     * @param endY      the ending Y position
     */
    public void move(int easing, int startTime, int endTime, int startX,
                     int startY, int endX, int endY) {
        StoryboardEvent move = new MoveEvent(easing, startTime, endTime, startX,
                startY, endX, endY);
        addToCompilation(move, false);
    }

    /**
     * Another movement method, for when you want to use
     * an object that already has a initialized position
     * on the SB and would like to move it usually those
     * position points.
     * <p>
     * Functions like these will be often included in order
     * to save the user/SB programmer work. You no longer
     * need to remember where the object was previously, the
     * code will do it for you!
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param endX      the ending X position
     * @param endY      the ending Y position
     */
    public void move(int easing, int startTime, int endTime, int endX, int endY) {
        StoryboardEvent move = new MoveEvent(easing, startTime, endTime, 0,
                0, endX, endY);
        addToCompilation(move, true);
    }

    /**
     * Movement function for movement only in a horizontal
     * direction.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startX    the starting horizontal position
     * @param endX      the ending horizontal position
     */
    public void moveX(int easing, int startTime, int endTime, int startX,
                      int endX) {
        StoryboardEvent move = new MoveXEvent(easing, startTime, endTime,
                startX, endX);
        addToCompilation(move, true);
    }

    /**
     * Movement function for movement only in a horizontal
     * direction, assuming that you are using the previous
     * X position that the sprite already has.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param endX      the ending horizontal position
     */
    public void moveX(int easing, int startTime, int endTime,
                      int endX) {
        StoryboardEvent move = new MoveXEvent(easing, startTime, endTime,
                0, endX);
        addToCompilation(move, true);
    }

    /**
     * Movement function for movement only in a vertical
     * direction.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startY    the starting horizontal position
     * @param endY      the ending horizontal position
     */
    public void moveY(int easing, int startTime, int endTime, int startY,
                      int endY) {
        StoryboardEvent move = new MoveYEvent(easing, startTime, endTime,
                startY, endY);
        addToCompilation(move, true);
    }

    /**
     * Movement function for movement only in a vertical
     * direction, assuming that you are using the sprite's
     * previous y location.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param endY      the ending horizontal position
     */
    public void moveY(int easing, int startTime, int endTime,
                      int endY) {
        StoryboardEvent move = new MoveYEvent(easing, startTime, endTime,
                0, endY);
        addToCompilation(move, true);
    }

    //-----------------------------------------Scaling---------------------------------------------

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
     * General scaling method. If you input the correct
     * parameters, the sprite will resize itself from
     * an initial scaling to a final scaling. 1 = 100%,
     * and 100% is the original size of the image.
     * <p>
     * PLEASE NOTE that this function IMPLIES that the current
     * sprite has the SAME scaling (percentage-wise) both horizontally
     * and vertically, meaning that the object's image is still a proportion of
     * its original size. If not, an exception will be thrown, and an error will
     * occur. Use Vector Scaling if you need to!
     *
     * @param easing       how the animation is done (tweening, default to zero)
     * @param startTime    the time that this event begins
     * @param endTime      the time that this event ends
     * @param startScaling the initial scale factor
     * @param endScaling   the ending scale factor
     */
    public void scale(int easing, int startTime, int endTime, double startScaling,
                      double endScaling) {
        StoryboardEvent scale = new ScaleEvent(easing, startTime, endTime,
                startScaling, endScaling);
        addToCompilation(scale, false);
    }

    /**
     * Another scaling method, for when you want to use
     * the object's previous scaling as the initial scale.
     * Make sure that the object has been initialized with
     * a scaling already before using!
     * <p>
     * PLEASE NOTE that this function IMPLIES that the current
     * sprite has the SAME scaling (percentage-wise) both horizontally
     * and vertically, meaning that the object's image is still a proportion of
     * its original size. If not, an exception will be thrown, and an error will
     * occur. Use Vector Scaling if you need to!
     *
     * @param easing     how the animation is done (tweening, default to zero)
     * @param startTime  the time that this event begins
     * @param endTime    the time that this event ends
     * @param endScaling the ending scale factor
     */
    public void scale(int easing, int startTime, int endTime,
                      double endScaling) {
        StoryboardEvent scale = new ScaleEvent(easing, startTime, endTime,
                0, endScaling);
        addToCompilation(scale, true);
    }

    //-----------------------------------------Vector Scaling---------------------------------------------

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
     * Default vector scaling method. If you follow the parameters,
     * the sprite will resize itself by a certain scale X-wise and
     * by a certain scale (that may or may not be the same number) Y-wise.
     * This is a good way to resize objects out of their original
     * proportions, if necessary.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param startX    the initial horizontal scale factor
     * @param startY    the initial vertical scale factor
     * @param endX      the final horizontal scale factor
     * @param endY      the final vertical scale factor
     */
    public void vectorScale(int easing, int startTime, int endTime, double startX,
                            double startY, double endX, double endY) {
        StoryboardEvent scale = new VectorEvent(easing, startTime, endTime,
                startX, startY, endX, endY);
        addToCompilation(scale, false);
    }

    /**
     * Another vector scaling method, for when you have
     * previous scaling in-tact and you'd like to use it
     * for the initial scaling proportions when vector
     * resizing.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param endX      the final horizontal scale factor
     * @param endY      the final vertical scale factor
     */
    public void vectorScale(int easing, int startTime, int endTime, double endX, double endY) {
        StoryboardEvent scale = new VectorEvent(easing, startTime, endTime,
                0, 0, endX, endY);
        addToCompilation(scale, true);
    }

    //-----------------------------------------Rotation---------------------------------------------

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
     * General method for rotation. If you specify the
     * correct parameters, the sprite will rotate from
     * its original angle to a new angle. 0 is the default value.
     * Positive angle means clockwise rotation. Note that angles
     * are written in RADIANS for some stupid reason.
     *
     * @param easing     how the animation is done (tweening, default to zero)
     * @param startTime  the time that this event begins
     * @param endTime    the time that this event ends
     * @param startAngle the beginning angle to rotate from
     * @param endAngle   the final angle of rotation
     */
    public void rotate(int easing, int startTime, int endTime, double startAngle,
                       double endAngle) {
        StoryboardEvent rotate = new RotationEvent(easing, startTime, endTime,
                startAngle, endAngle);
        addToCompilation(rotate, false);
    }

    /**
     * Another method for rotation, when you want to use
     * the current angle of rotation that the sprite
     * currently has as the initial starting rotating
     * angle for this event.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param endAngle  the final angle of rotation
     */
    public void rotate(int easing, int startTime, int endTime,
                       double endAngle) {
        StoryboardEvent rotate = new RotationEvent(easing, startTime, endTime,
                0, endAngle);
        addToCompilation(rotate, true);
    }

    //-----------------------------------------Color---------------------------------------------

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
     * General color-changing method, which manipulates the color
     * of the current sprite through the RGB scale. Enter the beginning
     * color value and the ending values and this will occur.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param r1        the initial red value of RGB
     * @param g1        the initial green value of RGB
     * @param b1        the initial blue value of RGB
     * @param r2        the final red value of RGB
     * @param g2        the final green value of RGB
     * @param b2        the final blue value of RGB
     */
    public void color(int easing, int startTime, int endTime, int r1, int g1,
                      int b1, int r2, int g2, int b2) {
        StoryboardEvent color = new ColorEvent(easing, startTime, endTime,
                r1, g1, b1, r2, g2, b2);
        addToCompilation(color, false);
    }

    /**
     * Another color-changing method, when you want to use the previous
     * values of RGB color-changing for the new color event.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param r2        the final red value of RGB
     * @param g2        the final green value of RGB
     * @param b2        the final blue value of RGB
     */
    public void color(int easing, int startTime, int endTime, int r2, int g2, int b2) {
        StoryboardEvent color = new ColorEvent(easing, startTime, endTime,
                0, 0, 0, r2, g2, b2);
        addToCompilation(color, false);
    }

    //-----------------------------------------Miscellaneous---------------------------------------------

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
     * Flips the current sprite horizontally. I believe that timing and easing
     * has little effect on this method, since tweening/animation does not apply
     * to these. The flip happens immediately on the start time.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     */
    public void flipH(int easing, int startTime, int endTime) {
        StoryboardEvent param = new ParameterEvent(easing, startTime, endTime,
                "H");
        addToCompilation(param, false);
    }

    /**
     * Flips the current sprite vertically. I believe that timing and easing
     * has little effect on this method, since tweening/animation does not apply
     * to these. The flip happens immediately on the start time.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     */
    public void flipV(int easing, int startTime, int endTime) {
        StoryboardEvent param = new ParameterEvent(easing, startTime, endTime,
                "V");
        addToCompilation(param, false);
    }

    /**
     * Changes the current sprite to have additive-blend coloring.
     * I believe that timing and easing has little effect on this method,
     * since tweening/animation does not apply to these.
     * The coloring change happens immediately on the start time.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     */
    public void additive(int easing, int startTime, int endTime) {
        StoryboardEvent param = new ParameterEvent(easing, startTime, endTime,
                "A");
        addToCompilation(param, false);
    }

}

//EOF: END OF FILE