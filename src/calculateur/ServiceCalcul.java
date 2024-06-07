import java.rmi.Remote;

public interface ServiceCalcul extends Remote {
    public Image compute(Case c, Scene scene);
}
