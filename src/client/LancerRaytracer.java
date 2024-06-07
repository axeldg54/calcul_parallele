import java.awt.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.Instant;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [serveur] [port] [fichier-scène] [largeur] [hauteur] [nombre de découpe]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
     
    public static void main(String[] args){

        // Le fichier de description de la scène si pas fournie
        String fichier_description = "../simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512, nbDecoupe = 16;

        String serveur = "localhost";
        int port = 1099;
        
        if (args.length > 0){
            serveur = args[0];
            if (args.length > 1){
                port = Integer.parseInt(args[1]);
                if(args.length > 2){
                    fichier_description = args[2];
                    if(args.length > 3){
                        largeur = Integer.parseInt(args[3]);
                        if(args.length > 4) {
                            hauteur = Integer.parseInt(args[4]);
                            if (args.length > 5) {
                                nbDecoupe = Integer.parseInt(args[5]);
                        }
                    }
                }
            }
        }
        } else {
            System.out.println(aide);
        }

        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        // découpe de l'image et récupération de l'image découpée dans une liste
        DecoupeImage decoupeImage = new DecoupeImage(scene, disp);
        List<Case> cases = Collections.synchronizedList(decoupeImage.decouper(largeur, hauteur, nbDecoupe));
        synchronized (cases) {
            Collections.shuffle(cases);
        }

        Registry reg = null;    

        try {
            reg = LocateRegistry.getRegistry(serveur, port);
        } catch (RemoteException remoteException) {
            System.out.println("Exception remote");
        }

        try {
            ServiceDistributeur serviceDistributeur = (ServiceDistributeur) reg.lookup("distributeur");
            ArrayList<ServiceCalcul> proxys = serviceDistributeur.getProxys();
            
            for (int i = 0; i < proxys.size(); i++) {
                ServiceCalcul proxy = proxys.get(i);
                ThreadTravailleur threadTravailleur = new ThreadTravailleur(proxy, scene, cases,disp);
                threadTravailleur.start();
            }

        } catch (ConnectException exception) {
            System.out.println("Problème de connexion à l'annuaire");
        } catch (NotBoundException exception) {
            System.out.println("Nom de service inconnu");
        } catch (AccessException e) {
            System.out.println("Donnée inaccessible");
        } catch (RemoteException e) {
            System.out.println("Exception remote");
        }
    }    

    public static class ThreadTravailleur extends Thread {
        ServiceCalcul calculateur;
        Scene scene;
        List<Case> cases;
        Disp disp;

        Image imageRes = null;

        ThreadTravailleur(ServiceCalcul calc, Scene scene, List<Case> cases, Disp disp){
            calculateur = calc;
            this.scene = scene;
            this.cases = cases;
            this.disp = disp;
            imageRes = null;
        }

        public void run() {
            try {
                while (true) {
                    Case tile = null;
                    synchronized (cases) {
                        if (!cases.isEmpty()) {
                            tile = cases.remove(0);
                        } else {
                            break;
                        }
                    }
                    if (tile != null) {
                        imageRes = calculateur.compute(tile, scene);
                        disp.setImage(imageRes,tile.getX(),tile.getY());
                    }
                }
            } catch (UndeclaredThrowableException e){
                System.out.println("Calculateur introuvable");
            }
        }
    }
}
