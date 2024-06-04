import java.util.ArrayList;

public class DecoupeImage {
    Scene scene;
    Disp disp;

    public DecoupeImage(Scene sc, Disp disp) {
        this.scene = sc;
        this.disp = disp;
    }

    public ArrayList<Case> decouper(int largeur, int hauteur, int nbDecoupe) {
        assert largeur == hauteur;
        assert largeur % nbDecoupe == 0;

        ArrayList<Case> liste = new ArrayList<>();

        int l = largeur / nbDecoupe;
        int h = hauteur / nbDecoupe;
        for (int x = 0; x < largeur; x+= l) {
            for (int y = 0; y < hauteur; y+=h) {
                liste.add(new Case(x, y, l, h));
            }
        }
        return liste;
    }
}