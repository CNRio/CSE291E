import rmi.*;

public class ServerTest {
	public static void main(String[] args) {
		PingServer server = new EchoServer();
        Skeleton<PingServer> skeleton = new Skeleton<PingServer>(PingServer.class, server);
        System.out.println(skeleton.SkRunning);
        try {
        	System.out.println(skeleton.SkRunning);
            skeleton.start();
            System.out.println(skeleton.SkRunning);
        } catch (RMIException e) {
            e.printStackTrace();
        }
        //skeleton.stop();
    }
}
