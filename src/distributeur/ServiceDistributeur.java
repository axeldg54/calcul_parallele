import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServiceDistributeur extends Remote{
    //public ArrayList<ServiceCalcul> proxys = new ArrayList<ServiceCalcul>();

    
    public void ajouterTravailleur(ServiceCalcul proxy) throws RemoteException;

    public ArrayList<ServiceCalcul> getProxys() throws RemoteException;;
} 
