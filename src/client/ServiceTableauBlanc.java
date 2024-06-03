import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceTableauBlanc extends Remote {
    void afficherMessage(Dessin dessin) throws RemoteException;
    void enregistrerClient(ServiceTableauBlanc service) throws RemoteException;

    void distribuerMessage(Dessin dessin) throws RemoteException;
}
