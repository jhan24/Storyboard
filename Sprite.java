/**
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
 * Also, overloads many methods so that there's much more flexibility.
 * Main benefit of this is that you don't need to enter previous values
 * if you don't want to - if you want to move an object from its previous
 * location to a new location, the program will have stored its previous
 * location in memory already. Useful, right?
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

/**
 * JOSBGL PROGRAM INFORMATION
 * <p/>
 * Being written in Java, this is basically a different way to generate
 * .sb files and creating storyboard without being limited by the in-game
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
 *
 * For one, by utilizing the TreeMap and other collections classes in Java,
 * the events will automatically be sorted by order. You also don't
 * have to constantly remember where your object was last placed, since
 * the code will store your previous scaling, rotation angle, location,
 * opacity, and more. Useful, right?
 * <p/>
 * Currently only supportive of sprites in storyboards. Animation and
 * sfx support to be added, although I don't think maybe people
 * utilize sound effects in their storyboards...I think that would just
 * be weird, although I guess games like Project Diva
 * and the original Ouendan do that.
 * <p/>
 * Seeing as how SBs have evolved in osu!, there's probably not much of a
 * reason to use SFXs in SBs.
 * <p/>
 * TL;DR a simpler way to program storyboards using Java.
 */

import java.util.Collection;
import java.util.TreeMap;

public class Sprite extends Storyboard implements Comparable<Storyboard> {

    //-----------------------------------------Variables---------------------------------------------

    /**
     * Creating variables to be used in this class. Most of these store
     * important information about the whereabouts of the current
     * sprite, and what it's doing.
     */
    private Layer layer;
    private Origin origin;
    private String filePath = "";
    private TreeMap<Double, String> compilation;
    private int x = 0;
    private int y = 0;
    private double scaleX = 0;
    private double scaleY = 0;
    private boolean proportionsKept;
    private double angle = 0;
    private double opacity = 0;
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private int startTime = 0;

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
        scaleX = 1;
        scaleY = 1;
        proportionsKept = true;
        this.startTime = -2;

        //sends the header line for the sprite over to the compilation
        Object[] list = {"Sprite", layer, origin, "\"" + filePath + "\"",
                x, y};

        //TreeMap to store the events for the sprite in order of the time
        //that they appear in.
        compilation = new TreeMap<Double, String>();

        //the start time of the sprite is currently set to -2.
        //this will be changed when an event for the sprite has been created.
        //its set to -2 in the case that an event starts at time 0, then startTime
        //will be at -1.
        addToCompilation(list, this.startTime);
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

    //-----------------------------------------Getter / Setters---------------------------------------------

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

    //-----------------------------------------Helper Methods---------------------------------------------

    /**
     * This method concatenates a new storyboard line to
     * the current string which stores the sprite header line
     * and any modifications to the sprite. Basically serves
     * as a helper method to make printing easier.
     * <p/>
     * It's probably not the best idea to create an array
     * of type Object and throw in all sorts of junk in it,
     * but it works well for the purpose here.
     *
     * The startTime of the new event as well as the line that
     * will be printed into the SB will be placed into a TreeMap,
     * which then orders the events by their starting time.
     *
     * For more information, visit
     * http://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
     *
     * @param list a list of all elements to be printed
     */
    public void addToCompilation(Object[] list, int startTime) {

        String sbLine = "";

        if (!this.compilation.isEmpty()) {
            sbLine += "\n";
        }

        for (Object l : list) {
            sbLine += l.toString();
            sbLine += ",";
        }

        double timeKey = startTime;

        //removes the extra last comma
        sbLine = sbLine.substring(0, sbLine.length() - 1);

        //checks to see if the heading event is still negative
        if (!this.compilation.isEmpty() && (this.compilation.firstKey() > startTime - 1 ||
                this.compilation.firstKey() == -2)) {

            //if so, change it so that it equals the time of the first event minus 1.
            //this will guarantee that the header for the Sprite runs right before
            //the first actual event, making timing and ordering a lot more efficient.
            String temp = this.compilation.get(this.compilation.firstKey());
            this.compilation.remove(this.compilation.firstKey());
            this.startTime = startTime;
            this.compilation.put(timeKey - 1, temp);
        }

        //checks to make sure that there doesn't already exist a key
        //for this event's start time.
        if (this.compilation.containsKey(timeKey)) {
            while (this.compilation.containsKey(timeKey)) {
                timeKey += 0.01;
            }
        }

        //puts the values into the compilation treemap
        this.compilation.put(timeKey, sbLine);

    }

    /**
     * Proportion checking helper method for the scaling method.
     * Checks to see if the X and Y scaling of the object is the
     * same (does it match up to the original proportions?) If not,
     * the scaling functions will cease to run.
     */
    public void proportionChecking() {
        //intellij told me to do this so here it is
        proportionsKept = scaleX == scaleY;
    }

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
        Object[] list = {" F", easing, startTime, endTime, startOpacity,
                endOpacity};
        addToCompilation(list, startTime);
        opacity = endOpacity;
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
        Object[] list = {" F", easing, startTime, endTime, opacity,
                endOpacity};
        addToCompilation(list, startTime);
        opacity = endOpacity;
    }

    /**
     * A fade out method. This one is for when you know
     * you want an object to fade out from its current opacity.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this even ends
     */
    public void fadeOut(int easing, int startTime, int endTime) {
        Object[] list = {" F", easing, startTime, endTime, opacity,
                0};
        addToCompilation(list, startTime);
        opacity = 0;
    }

    /**
     * Another fade out event, for when you want an object
     * to fade out from fully visible to invisible (1 to 0).
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this even ends
     */
    public void fadeOutFull(int easing, int startTime, int endTime) {
        Object[] list = {" F", easing, startTime, endTime, 1,
                0};
        addToCompilation(list, startTime);
        opacity = 0;
    }

    /**
     * A fade in event, for when you know
     * you want an object to fade in from its current opacity.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this even ends
     */
    public void fadeIn(int easing, int startTime, int endTime) {
        Object[] list = {" F", easing, startTime, endTime, opacity,
                1};
        addToCompilation(list, startTime);
        opacity = 1;
    }

    /**
     * Another fade in event, for when you want an object
     * to fade in fully (from 0 to 1).
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this even ends
     */
    public void fadeInFull(int easing, int startTime, int endTime) {
        Object[] list = {" F", easing, startTime, endTime, 0,
                1};
        addToCompilation(list, startTime);
        opacity = 1;
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
        Object[] list = {" M", easing, startTime, endTime, startX,
                startY, endX, endY};
        addToCompilation(list, startTime);
        x = endX;
        y = endY;
    }

    /**
     * Another movement method, for when you want to use
     * an object that already has a initialized position
     * on the SB and would like to move it usually those
     * position points.
     * <p/>
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
        Object[] list = {" M", easing, startTime, endTime, x,
                y, endX, endY};
        addToCompilation(list, startTime);
        x = endX;
        y = endY;
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
        Object[] list = {" MX", easing, startTime, endTime, startX,
                endX};
        addToCompilation(list, startTime);
        x = endX;
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
        Object[] list = {" MX", easing, startTime, endTime, x,
                endX};
        addToCompilation(list, startTime);
        x = endX;
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
        Object[] list = {" MY", easing, startTime, endTime, startY,
                endY};
        addToCompilation(list, startTime);
        y = endY;
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
        Object[] list = {" MY", easing, startTime, endTime, y,
                endY};
        addToCompilation(list, startTime);
        y = endY;
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
     * <p/>
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
        try {
            if (!proportionsKept) {
                throw new ProportionDisagreementException();
            }
        } catch (ProportionDisagreementException e) {
            System.out.println(e.getMessage());
        }
        Object[] list = {" S", easing, startTime, endTime, startScaling,
                endScaling};
        addToCompilation(list, startTime);
        scaleX = endScaling;
        scaleY = endScaling;
    }

    /**
     * Another scaling method, for when you want to use
     * the object's previous scaling as the initial scale.
     * Make sure that the object has been initialized with
     * a scaling already before using!
     * <p/>
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
        try {
            if (!proportionsKept) {
                throw new ProportionDisagreementException();
            }
        } catch (ProportionDisagreementException e) {
            System.out.println(e.getMessage());
        }
        Object[] list = {" S", easing, startTime, endTime, scaleX,
                endScaling};
        addToCompilation(list, startTime);
        scaleX = endScaling;
        scaleY = endScaling;
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
        Object[] list = {" V", easing, startTime, endTime, startX,
                startY, endX, endY};
        addToCompilation(list, startTime);
        scaleX = endX;
        scaleY = endY;
        proportionChecking();
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
        Object[] list = {" V", easing, startTime, endTime, scaleX,
                scaleY, endX, endY};
        addToCompilation(list, startTime);
        scaleX = endX;
        scaleY = endY;
        proportionChecking();
    }

    /**
     * Vector scaling when ONLY modifying the horizontal length
     * of the object. Note that this method assumes that you will
     * be keeping the same vertical scaling, so the scaleY will be
     * assumed from previous.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param start     the starting x scale factor
     * @param end       the ending x scale factor
     */
    public void vectorScaleX(int easing, int startTime, int endTime, double start,
                             double end) {
        Object[] list = {" V", easing, startTime, endTime, start,
                scaleY, end, scaleY};
        addToCompilation(list, startTime);
        scaleX = end;
        proportionChecking();
    }

    /**
     * Vector scaling when ONLY modifying the horizontal length
     * of the object. This method assumes that both the horizontal
     * and vertical scaling are the same as previous.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param end       the ending x scale factor
     */
    public void vectorScaleX(int easing, int startTime, int endTime,
                             double end) {
        Object[] list = {" V", easing, startTime, endTime, scaleX,
                scaleY, end, scaleY};
        addToCompilation(list, startTime);
        scaleX = end;
        proportionChecking();
    }

    /**
     * Vector scaling when ONLY modifying the vertical length
     * of the object. Note that this method assumes that you will
     * be keeping the same horizontal scaling, so the scaleX will be
     * assumed from previous.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param start     the starting x scale factor
     * @param end       the ending x scale factor
     */
    public void vectorScaleY(int easing, int startTime, int endTime, double start,
                             double end) {
        Object[] list = {" V", easing, startTime, endTime, scaleX,
                start, scaleX, end};
        addToCompilation(list, startTime);
        scaleY = end;
        proportionChecking();
    }

    /**
     * Vector scaling when ONLY modifying the vertical length
     * of the object. This method assumes that both the horizontal
     * and vertical scaling are the same as previous.
     *
     * @param easing    how the animation is done (tweening, default to zero)
     * @param startTime the time that this event begins
     * @param endTime   the time that this event ends
     * @param end       the ending x scale factor
     */
    public void vectorScaleY(int easing, int startTime, int endTime,
                             double end) {
        Object[] list = {" V", easing, startTime, endTime, scaleX,
                scaleY, scaleX, end};
        addToCompilation(list, startTime);
        scaleY = end;
        proportionChecking();
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
        Object[] list = {" R", easing, startTime, endTime, startAngle,
                endAngle};
        addToCompilation(list, startTime);
        angle = endAngle;
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
        Object[] list = {" R", easing, startTime, endTime, angle,
                endAngle};
        addToCompilation(list, startTime);
        angle = endAngle;
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
        Object[] list = {" C", easing, startTime, endTime, r1, g1, b1,
                r2, g2, b2};
        addToCompilation(list, startTime);
        red = r2;
        green = g2;
        blue = b2;
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
        Object[] list = {" C", easing, startTime, endTime, red, green, blue,
                r2, g2, b2};
        addToCompilation(list, startTime);
        red = r2;
        green = g2;
        blue = b2;
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
        Object[] list = {" P", easing, startTime, endTime, "H"};
        addToCompilation(list, startTime);
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
        Object[] list = {" P", easing, startTime, endTime, "V"};
        addToCompilation(list, startTime);
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
        Object[] list = {" P", easing, startTime, endTime, "A"};
        addToCompilation(list, startTime);
    }

    //-----------------------------------------Cleaning---------------------------------------------


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
        Collection temp = compilation.values();
        String printing = "";

        for (Object e : temp) {
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
        if (null == other) { return false; }
        if (this == other) { return true; }
        if (!(other instanceof Sprite)) { return false; }
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
     *      zero if equal, and positive if greater.
     */
    public int compareTo(Storyboard other) {
        if (this.layer.compareTo(other.getLayerENUM()) < 0) {
            return -1;
        } else if (this.layer.compareTo(other.getLayerENUM()) > 0) {
            return 1;
        } else {
            return this.startTime - other.getStartTime();
        }
    }

    //-----------------------------------------Testing---------------------------------------------

    public TreeMap<Double, String> getCompilation() {
        return compilation;
    }
}
