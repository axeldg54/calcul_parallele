import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceCalcul extends Remote {
    public Image compute(Case c, Scene scene) throws RemoteException;
}
