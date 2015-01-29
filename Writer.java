/**
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 *
 * Created by Erina on 1/24/2015.
 *
 * An writer for the other classes. You need to instantiate
 * an instance of this class in order to write all the
 * storyboard lines to an actual osb file. Send the objects
 * that you have written to the writer in any order as an
 * array or ArrayList, and the program will sort them in
 * order of their first event, and print them out
 * accordingly.
 *
 * Utilizes Java 8, including lambda expressions.
 * For more information, visit
 * http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 *
 * Changelog:
 * 1/24/2015: Writer class created.
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


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Writer {

    private PrintWriter writer;
    private ArrayList<Storyboard> backingArray;

    public Writer() {
        this("storyboard.osb");
    }

    public Writer(String fileName) {
        try {
            writer = new PrintWriter(fileName);
        } catch (Exception e) {
            System.out.println("File Error! Check your file name/conventions.");
        }
        backingArray = new ArrayList<Storyboard>();
        writer.println("[Events]");
        writer.println("//Background and Video events");
        writer.flush();
    }

    public void writeToFile(Sprite[] elements) {
        ArrayList<Storyboard> temp = new ArrayList<Storyboard>();
        temp.addAll(Arrays.asList(elements));
        writeToFile(temp);
    }

    public void writeToFile(ArrayList<Storyboard> elements) {
        boolean background = false;
        boolean fail = false;
        boolean pass = false;
        boolean foreground = false;

        backingArray.addAll(elements);
        Collections.sort(backingArray, (a, b) -> a.compareTo(b));
        for (Storyboard s : backingArray) {
            if (s.getLayerENUM() == Layer.Background) {
                if (!background) {
                    writer.println("//Storyboard Layer 0 (Background)");
                    background = true;
                }
            } else if (s.getLayerENUM() == Layer.Fail) {
                if (!background) {
                    writer.println("//Storyboard Layer 0 (Background)");
                    background = true;
                }
                if (!fail) {
                    writer.println("//Storyboard Layer 1 (Fail)");
                    fail = true;
                }
            } else if (s.getLayerENUM() == Layer.Pass) {
                if (!background) {
                    writer.println("//Storyboard Layer 0 (Background)");
                    background = true;
                }
                if (!fail) {
                    writer.println("//Storyboard Layer 1 (Fail)");
                    fail = true;
                }
                if (!pass) {
                    writer.println("//Storyboard Layer 2 (Pass)");
                    pass = true;
                }
            } else if (s.getLayerENUM() == Layer.Foreground) {
                if (!background) {
                    writer.println("//Storyboard Layer 0 (Background)");
                    background = true;
                }
                if (!fail) {
                    writer.println("//Storyboard Layer 1 (Fail)");
                    fail = true;
                }
                if (!pass) {
                    writer.println("//Storyboard Layer 2 (Pass)");
                    pass = true;
                }
                if (!foreground) {
                    writer.println("//Storyboard Layer 3 (Foreground)");
                    foreground = true;
                }
            }
            writer.println(s.toString());
        }
        writer.flush();
    }

    public void closing() {
        writer.println("//Storyboard Sound Samples");
        writer.flush();
    }





}
