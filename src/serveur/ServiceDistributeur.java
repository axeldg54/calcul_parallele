import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote{
    public void ajouterTravailleur(ServiceCalcul proxy) throws RemoteException;
} 
