import java.rmi.Remote;

import raytracer.ServiceCalcul;

public interface ServiceDistributeur extends Remote{
    public void ajouterTravailleur(ServiceCalcul proxy);
} 
