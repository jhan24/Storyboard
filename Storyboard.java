/**
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/23/2015.
 *
 * The storyboard abstract class which outlines the basic template
 * for all sprites and objects that will be placed onto a storyboard.
 * Basically defines what objects on a storyboard can do, but does not
 * implement any of the actual functions. This
 *
 * Changelog:
 * 1/23/2015: First draft interface created. Outlines basic functions used by sprites.
 * 1/24/2015: Changed to abstract class to work with Comparable interface.
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

    private int startTime = 0;
    private String layer = "";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Fading Commands
     * General format for writing in SB is:
     * "_F,[...],startOpacity,endOpacity"
     * <p/>
     * Non-universal parameters for fade:
     * startOpacity, endOpacity
     * <p/>
     * 0 is invisible, and 1 is fully visible.
     */
    abstract void fade(int easing, int startTime, int endTime, double startOpacity,
              double endOpacity);

    abstract void fadeOut(int easing, int startTime, int endTime);

    abstract void fadeIn(int easing, int startTime, int endTime);

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
     * <p/>
     * NOTE: Based on further intel, I have realized that the top left
     * and right are not the actual listed values. Go do some trial
     * and error yourself and find out.
     */
    abstract void move(int easing, int startTime, int endTime, int startX,
              int startY, int endX, int endY);

    abstract void moveX(int easing, int startTime, int endTime, int startX,
               int endX);

    abstract void moveY(int easing, int startTime, int endTime, int startY,
               int endY);

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
    abstract void scale(int easing, int startTime, int endTime, double startScaling,
               double endScaling);

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
    abstract void vectorScale(int easing, int startTime, int endTime, double startX,
                     double startY, double endX, double endY);

    abstract void vectorScaleX(int easing, int startTime, int endTime, double start,
                      double end);

    abstract void vectorScaleY(int easing, int startTime, int endTime, double start,
                      double end);

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

    abstract void rotate(int easing, int startTime, int endTime, double startAngle,
                double endAngle);

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

    abstract void color(int easing, int startTime, int endTime, int r1, int g1,
               int b1, int r2, int g2, int b2);

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
    abstract void flipH(int easing, int startTime, int endTime);

    abstract void flipV(int easing, int startTime, int endTime);

    abstract void additive(int easing, int startTime, int endTime);

    abstract int getStartTime();

    abstract String getLayer();

    abstract Layer getLayerENUM();

}
