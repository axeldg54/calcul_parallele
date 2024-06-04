package raytracer;

import java.rmi.Remote;

public interface ServiceScene extends Remote {
    public Image compute(int x0, int y0, int w, int h); 
}
