import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur, Serializable{

    @Override
    public void ajouterTravailleur(ServiceCalcul proxy) throws RemoteException{
        proxys.add(proxy);
    }
}
