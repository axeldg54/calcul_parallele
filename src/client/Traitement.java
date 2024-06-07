import java.awt.*;
import java.util.Scanner;

public class Traitement implements Runnable {

    ServiceCalcul proxy;
    Scene scene;
    Case caseImage;
    Image image;

    public Traitement(ServiceCalcul p, Scene s, Case c) {
        this.proxy = p;
        this.scene = s;
        this.caseImage = c;
    }

    @Override
    public void run() {
        image = proxy.compute(caseImage, scene);
    }
}
