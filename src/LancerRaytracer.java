import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;

import decoupe.*;
import raytracer.Disp;
import raytracer.Scene;
import raytracer.Image;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
     
    public static void main(String[] args){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="./src/simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512, nbDecoupe = 16;
        
        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2) {
                    hauteur = Integer.parseInt(args[2]);
                    if (args.length > 3) {
                        nbDecoupe = Integer.parseInt(args[3]);
                    }
                }
            }
        }else{
            System.out.println(aide);
        }


        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);
        
        // Calcul de l'image de la scène les paramètres : 
        // - x0 et y0 : correspondant au coin haut à gauche
        // - l et h : hauteur et largeur de l'image calculée
        // Ici on calcule toute l'image (0,0) -> (largeur, hauteur)

        DecoupeImage decoupeImage = new DecoupeImage(scene, disp);
        ArrayList<Case> liste = decoupeImage.decouper(largeur, hauteur, nbDecoupe);

        for (Case c : liste) {
            // Chronométrage du temps de calcul
            Instant debut = Instant.now();
            System.out.println("Calcul de l'image :\n - Coordonnées : "+ c.getX() +","+ c.getY()
                    +"\n - Taille "+ largeur + "x" + hauteur);
            Image image = scene.compute(c.getX(), c.getY(), c.getLargeur(), c.getHauteur());
            Instant fin = Instant.now();

            long duree = Duration.between(debut, fin).toMillis();

            System.out.println("Image calculée en :"+duree+" ms");

            // Affichage de l'image calculée
            disp.setImage(image, c.getX(), c.getY());
        }
    }
}