import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur, Serializable{

    ArrayList<ServiceCalcul> proxys = new ArrayList<>();

    @Override
    public void ajouterTravailleur(ServiceCalcul proxy) throws RemoteException{
        proxys.add(proxy);
        System.out.println("AJOUT DU PROXY");
    }

    public ArrayList<ServiceCalcul> getProxys() throws RemoteException {
        return proxys;
    }
}

