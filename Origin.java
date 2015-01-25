/**
 * A part of the JOSG project.
 *
 * JOSBGL stands for Java osu! Storyboard Generation Language.
 * For more information on this project, check the documentation
 * in the other class files.
 *
 * Created by Erina on 1/24/2015.
 *
 * A enumerated object that lists the possible values that
 * a user can store for the "Origin" property for objects
 * in storyboards.
 *
 * Access a enum by writing "ENUMNAME.propertyname."
 * Ex: Layer.Background would call the Background property
 * from the layer enum.
 *
 * Enums are nice to have since they can also be ordered
 * in priority, and also restrict user input so that they
 * can't input anything that would break the program.
 *
 * For more information, visit
 * http://docs.oracle.com/javase/8/docs/api/java/lang/Enum.html
 *
 * Changelog:
 * 1/24/2015: Layer ENUM created.
 *
 * @author Erina
 * @version 1.00
 */
public enum Origin {
    TopLeft, TopCentre, TopRight, CentreLeft, Centre, CentreRight, BottomLeft,
    BottomCentre, BottomRight
}
