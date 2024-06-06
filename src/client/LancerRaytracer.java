import java.awt.*;
import java.time.Instant;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.util.ArrayList;


public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [serveur] [port] [fichier-scène] [largeur] [hauteur] [nombre de découpe]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
     
    public static void main(String[] args){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="../simple.txt";

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
        }else{
            System.out.println(aide);
        }


        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        //decoupe de l'image et récuparation de l'image découpé dans une liste
        DecoupeImage decoupeImage = new DecoupeImage(scene, disp);
        ArrayList<Case> liste = decoupeImage.decouper(largeur, hauteur, nbDecoupe);

        Registry reg = null;    

        try {
            reg = LocateRegistry.getRegistry(serveur,port);
        } catch (RemoteException remoteException){
            System.out.println("Exception remote");
        }

        try {
            ServiceDistributeur serviceDistributeur = (ServiceDistributeur) reg.lookup("distributeur");
            ArrayList<ServiceCalcul> proxys = serviceDistributeur.proxys;

            // Toutes les combinaisons de traitement avec les proxys et les cases sous forme d'objet Traitement
            List<Traitement> traitements = new ArrayList<>();
            for (Proxy proxy : proxys) {
                for (C c : liste) {
                    traitements.add(new Traitement(proxy, scene, c));
                }
            }

            // La classe ThreadTravailleur qui va exécuter les traitements
            // Accès à la liste synchronisé pour éviter que plusieurs threads ne fassent le même traitement
            class ThreadTravailleur extends Thread {
                private final List<Traitement> traitements;

                public ThreadTravailleur(List<Traitement> traitements) {
                    this.traitements = traitements;
                }

                @Override
                public void run() {
                    while (true) {
                        Traitement traitement;
                        synchronized (traitements) {
                            if (traitements.isEmpty()) {
                                break;
                            }
                            traitement = traitements.getItem(0);
                            traitements.remove(0);
                        }
                        traitement.run();
                    }
                }
            }

            // Limite de threads
            // On crée 10 threads qui vont exécuter les traitements
            int maxThreads = 10;
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < maxThreads; i++) {
                Thread thread = new ThreadTravailleur(traitements);
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (ConnectException exception){
            System.out.println("Problème de connexion à l'annuaire");
        } catch (NotBoundException exception){
            System.out.println("Nom de service inconnu");
        } catch (AccessException e) {
            System.out.println("Donnée inaccessible");
        } catch (RemoteException e) {
            System.out.println("Exception remote");
        }

/**
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
        */
    }	
}
