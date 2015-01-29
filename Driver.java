/**
 * Sample storyboard driver
 *
 * @author Erina
 */

import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) {

        //Sample storyboard object
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
        c.move(0, 0, 100, 200, 200, 300, 300);
        c.moveX(0, 100, 200, 150);
        c.moveY(0, 200, 300, 150);
        c.move(0, 300, 400, 1000, 1000);

        Sprite d = new Sprite(Layer.Foreground, Origin.TopRight, "sb\\etc\\aye.png",
                25, 50);
        d.fade(0, 0, 200, 0, 0.5);
        d.fade(0, 900, 2000, 0.25);
        d.fade(0, 2000, 2100, 0.8);
        d.fade(0, 2000, 2100, 0.5);
        d.move(0, 2000, 2100, 50, 100, 200, 300);
        d.fade(0, 2100, 2200, 1);
        d.fade(0, 2300, 2400, 0);
        d.fade(0, 2400, 2500, 0.25);

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


