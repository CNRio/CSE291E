package rmi;

public class EchoServer implements PingServer {
    @Override
    public String ping(int idNumber) {
    	System.out.println("idNumber: " + idNumber);
    	System.out.println("Server replied: " + idNumber + " Pong");
        return idNumber + " Pong";
    }
    
}