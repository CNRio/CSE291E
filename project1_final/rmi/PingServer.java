package rmi;

public interface PingServer {
	String ping(int idNumber) throws RMIException;
}
