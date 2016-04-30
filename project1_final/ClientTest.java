import rmi.*;

import java.net.InetSocketAddress;

public class ClientTest {
	public static void main(String[] args) {
        String serverName = args[0];
		int port = Integer.parseInt(args[1]);

        PingServer stub1 = makePingServer(serverName, port);
        PingServer stub2 = makePingServer(serverName, port);

        //Test for PingPongServer
        int[] idNumbers = {1001, 1003, 1222, 7312};
        pingTest(stub1, idNumbers);
    }

    public static PingServer makePingServer(String serverName, int port) {
        InetSocketAddress address = new InetSocketAddress(serverName, port);
        return Stub.create(PingServer.class, address);
    }

    public static void pingTest(PingServer stub, int[] idNumbers) {
        int count = idNumbers.length;
        for (int i = 0; i < idNumbers.length; i ++) {
            String result;
			try {
				result = stub.ping(idNumbers[i]);
				System.out.println("Ping idNumber: " + idNumbers[i]);
	            System.out.println("Server replied: " + result);
	            if (result.equals(idNumbers[i] + " Pong")) count --;
			} catch (RMIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        System.out.println(idNumbers.length + " Test Completed, " + count + " Tests Failed");
    }
}
