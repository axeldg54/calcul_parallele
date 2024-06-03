import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {

    void enregistrerClient(ServiceTableauBlanc service) throws RemoteException;
}