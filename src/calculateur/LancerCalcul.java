import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerCalcul {
    public static void main(String[] args) {
        String ip=args[0];
        int port=Integer.parseInt(args[1]);

        Registry reg=null;

        ServiceDistributeur rDistributeur=null;
        ServiceCalcul calcul=new Calcul();
        ServiceCalcul rdCalcul;

        try{
            reg=LocateRegistry.getRegistry(ip, port);
            rDistributeur=(ServiceDistributeur) reg.lookup("distributeur");

            rdCalcul=(ServiceCalcul)UnicastRemoteObject.exportObject(calcul, 0);
            
            rDistributeur.ajouterTravailleur(rdCalcul);
        }
        catch(NotBoundException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch(AccessException e){
            System.out.println("Erreur lookup");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch(RemoteException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }

        
    }
}
