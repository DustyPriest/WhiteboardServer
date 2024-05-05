import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {

            try {

                LocateRegistry.createRegistry(1099);
                IRemoteWhiteboard remoteMath = new RemoteWhiteboard();
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("RemoteWhiteboard", remoteMath);

            }catch(Exception e) {
                e.printStackTrace();
            }
    }
}
