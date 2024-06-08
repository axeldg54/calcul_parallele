import java.time.Duration;
import java.time.Instant;

public class Calcul implements ServiceCalcul{

    @Override
    public Image compute(Case c, Scene scene){
        Instant debut = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : "+ c.getX() +","+ c.getY()
                    +"\n - Taille "+ c.getLargeur() + "x" + c.getHauteur());
        Image image = scene.compute(c.getX(), c.getY(), c.getLargeur(), c.getHauteur());
        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :"+duree+" ms");

        

        return image;
    }
}
