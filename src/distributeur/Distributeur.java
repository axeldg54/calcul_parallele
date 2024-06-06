import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur, Serializable{

    /**
     * Ajoute un petit chinois en plus pour faire le calcul
     * Salaire de 1€ par jour
     *
     * @param proxy
     * @throws RemoteException
     */
    @Override
    public void ajouterTravailleur(ServiceCalcul proxy) throws RemoteException{
        proxys.add(proxy);
    }
}
