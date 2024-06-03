import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

public class Distributeur implements ServiceDistributeur {

    ArrayList<ServiceTableauBlanc> listeServices;
    ArrayList<Dessin> dessins;

    public Distributeur() {
        listeServices = new ArrayList<ServiceTableauBlanc>();
        dessins = new ArrayList<Dessin>();
    }

    @Override
    public void enregistrerClient(ServiceTableauBlanc service) throws RemoteException {
        // on donne le nouveau arrivant au ancien
        listeServices.forEach((s) -> {
            try {
                s.enregistrerClient(service);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        // on donne au nouvel arrivant les anciens
        listeServices.forEach((s) -> {
            try {
                service.enregistrerClient(s);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        // on ajoute le nouveau à la liste
        listeServices.add(service);

        // on donne au nouveau les dessins
        dessins.forEach((d) -> {
            try {
                service.afficherMessage(d);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
