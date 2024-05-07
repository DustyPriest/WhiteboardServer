import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
            IRemoteWhiteboard remoteWhiteboard;
            try {

                LocateRegistry.createRegistry(1099);
                remoteWhiteboard = new RemoteWhiteboard();
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("RemoteWhiteboard", remoteWhiteboard);
                new ServerGUI((RemoteWhiteboard) remoteWhiteboard);

            }catch(Exception e) {
                e.printStackTrace();
            }

    }
}
