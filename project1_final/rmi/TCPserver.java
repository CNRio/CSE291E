package rmi;

import java.net.*;
import java.io.*;

public class TCPserver<T> extends Thread {
	protected T server = null;
    protected ServerSocket serverSocket = null;
    protected Skeleton<?> skeleton = null;
    private Exception Cause1 = null;
    
    public TCPserver(T server, int port, Skeleton<?> skeleton) throws IOException {
    	this.server = server;
    	this.skeleton = skeleton;
    	this.serverSocket = new ServerSocket(port);
    }
    
    @Override
    public void run() {
    	while(skeleton.SkRunning){
    		try{
    			 Socket socket = serverSocket.accept();
    			 (new Thread(new TCPindividual<T>(server, socket, skeleton))).start();
    			 
    		}catch(IOException e) {
    			skeleton.SkRunning = false;
    			Cause1 = e;
            }
    	}
    	skeleton.stopped(Cause1);
    } 
    public void shut() {
    	try { 
    		this.serverSocket.close();
        }catch (IOException e) {			
			System.err.println("Unable to close the connection");
		}  	
    }
    public String getServAddress(){
    	return this.serverSocket.getInetAddress().getHostName();
    }
    public int getPort(){
    	return this.serverSocket.getLocalPort();
    }
}
