/**
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 * For more information on this project, check the documentation
 * in the other class files.
 *
 * Created by Erina on 1/24/2015.
 *
 * An exception for the other classes. This exception runs whenever
 * a user tries to scale an object when the object's previous x-scale
 * factor and previous y-scale factor are not the same. This is because
 * the scale method depends on both to be the same in order for the
 * resize to work - otherwise results will be skewed. The exception will
 * only result in a warning, not in a program termination.
 *
 * The warning will also print the time of the event that caused this
 * warning
 *
 * Changelog:
 * 1/24/2015: Exception created.
 *
 * @author Erina
 * @version 1.00
 */

public class ProportionDisagreementException extends Exception {

    public ProportionDisagreementException(int time) {
        super("WARNING: The length and height of an object " +
                "does not match its original proportions when a " +
                "normal scale event occurred.\nThis may result in " +
                "skewed proportions and unintended results. This warning " +
                "occurred at time " + time + ".");
    }
}
