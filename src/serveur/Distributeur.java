import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur{
    private ArrayList<ServiceScene> proxys;

    public Distributeur(){
        proxys=new ArrayList<>();
    }

    @Override
    public void ajouterTravailleur(ServiceScene proxy) {
        proxys.add(proxy);
    }
}
