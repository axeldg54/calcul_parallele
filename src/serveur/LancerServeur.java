import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeur {
    public static void main(String[] args) {
        int port=3333;
        if(args.length > 0) port=Integer.parseInt(args[0]);

        ServiceDistributeur distributeur = new Distributeur();

        ServiceDistributeur serviceDistributeur=null;

        try {
            serviceDistributeur = (ServiceDistributeur) UnicastRemoteObject.exportObject(distributeur, 0);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("service : L'exportation n'a pas fonctionné");
            System.exit(1);
        }

        Registry reg = null;
        try {
            reg = LocateRegistry.getRegistry(port);
        } catch (ConnectException e) {
            System.out.println("service : Le registry n'a pas ete trouve.");
            System.exit(1);
        }
        catch(RemoteException e){
            System.out.println("service : Erreur lors de la recuperation de l'annuaire.");
            System.exit(1);
        }

        try {
            reg.rebind("distributeur", serviceDistributeur);
        } catch (RemoteException e) {
            System.out.println("service : probleme enregistrement proxy");
        }
    }
}
