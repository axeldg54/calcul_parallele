import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    public static void main (String[] args) throws RemoteException, NotBoundException {
        String serveur = "charlemagne.iutnc.univ-lorraine.fr";
        int port = 3333;
        if (args.length > 0) serveur = args[0];
        if (args.length > 1) port = Integer.parseInt(args[1]);
        Registry reg = null;
        reg = LocateRegistry.getRegistry(serveur, port);

        System.out.println("\n--------------------------------------");
        System.out.println("Liste des services disponibles:");
        for (String s: reg.list()) {
            System.out.println(s);
        }
        System.out.println("--------------------------------------\n");

        ServiceDistributeur distributeur = (ServiceDistributeur) reg.lookup("tableauBlanc");
        TableauBlanc tableauBlanc = new TableauBlanc();
        tableauBlanc.serviceDistributeur = distributeur;
        ServiceTableauBlanc proxy = (ServiceTableauBlanc) UnicastRemoteObject.exportObject(tableauBlanc, 0);
        distributeur.enregistrerClient(proxy);
    }
}