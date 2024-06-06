import java.awt.*;
import java.util.Scanner;

public class Traitement implements Runnable {

    ServiceCalcul proxy;
    Scene scene;
    Case caseImage;

    public Traitement(ServiceCalcul p, Scene s, Case c) {
        this.proxy = p;
        this.scene = s;
        this.caseImage = c;
    }

    @Override
    public void run() {
        Image image = proxy.compute(caseImage, scene);
    }
}
