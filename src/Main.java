import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {

            try {

                IRemoteWhiteboard remoteMath = new RemoteWhiteboard();
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("RemoteWhiteboard", remoteMath);

            }catch(Exception e) {
                e.printStackTrace();
            }
    }
}
