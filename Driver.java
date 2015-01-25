/**
 * testing driver, for testing the storyboard project.
 *
 * @author Erina
 */
public class Driver {

    public static void main(String[] args) {
        Sprite a = new Sprite(Layer.Foreground, Origin.Centre, "sb\\etc\\line.png",
                25, 50);
        a.fade(0, 1500, 1900, 0, 0.5);
        a.fade(0, 1900, 2000, 0.25);
        a.move(0, 900, 1800, 25, 50, 125, 150);
        a.move(0, 1800, 3000, 500, 500);
        a.rotate(0, 800, 900, 0, 75);
        a.scale(0, 800, 900, 1, 1.5);

        Sprite b = new Sprite(Layer.Foreground, Origin.Centre, "sb\\etc\\pen.png",
                25, 50);
        b.move(0, 1200, 3000, 500, 500);
        b.rotate(0, 2600, 2900, 0, 75);
        b.scale(0, 2000, 3000, 4);
        Sprite c = new Sprite(Layer.Foreground, Origin.TopLeft, "sb\\etc\\star.png",
                25, 50);
        c.fade(0, 0, 200, 0, 0.5);
        c.fade(0, 900, 2000, 0.25);
        c.move(0, 100, 500, 25, 50, 125, 150);
        c.move(0, 900, 3000, 500, 500);
        c.rotate(0, 300, 900, 0, 75);
        c.scale(0, 300, 900, 1, 1.5);

        Sprite d = new Sprite(Layer.Foreground, Origin.TopRight, "sb\\etc\\aye.png",
                25, 50);
        d.fade(0, 0, 200, 0, 0.5);
        d.fade(0, 900, 2000, 0.25);

        Sprite e = new Sprite(Layer.Background, Origin.Centre, "sb\\etc\\crazy.png",
                25, 50);
        e.scale(0, 5000, 6000, 3.4);
        e.vectorScale(0, 6000, 7000, 2.5, 4.6);
        e.vectorScale(0, 7000, 9000, 3.5, 3.5);
        e.scale(0, 9000, 9500, 2);


        Writer writer = new Writer();
        Sprite[] objects = {a, b, c, d, e};
        writer.writeToFile(objects);
        writer.closing();
    }

}
