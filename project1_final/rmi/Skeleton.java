package rmi;

import java.net.*;
import java.io.*;
import java.lang.reflect.Method;

public class Skeleton<T>
{
	//Used, but do not know why it is needed.
	public int port_num = 9999;
	public String ServAddr = null;
	private T server;
	public boolean SkRunning;
	public Class<T> c;
	//public String Serv_addr = null;
	
	//Connect this skeleton with the tcpserver
	private TCPserver<T> tcpServer;
	
    public Skeleton(Class<T> c, T server)
    {    	
    	if(c == null || server == null){
            throw new NullPointerException();
        }
    	if(!c.isInterface()) throw new Error("error");
    	
    	Method[] methods = c.getMethods();
        for (Method method : methods) {
            int count = 0;
            Class<?>[] exceptions = method.getExceptionTypes();
            for (Class<?> exception : exceptions) {
                if (exception.getName().equals(RMIException.class.getName())) {
                    count++;
                }
            }
            if (count == 0){
                throw new Error("Not a remote interface");
            }
        }

    	this.server = server;
    	this.c = c;
    }

    public Skeleton(Class<T> c, T server, InetSocketAddress address)
    {
    	if(c == null || server == null){
            throw new NullPointerException();
        }
    	if(!c.isInterface()) throw new Error("error");
    	
    	Method[] methods = c.getMethods();
        for (Method method : methods) {
            int count = 0;
            Class<?>[] exceptions = method.getExceptionTypes();
            for (Class<?> exception : exceptions) {
                if (exception.getName().equals(RMIException.class.getName())) {
                    count++;
                }
            }
            if (count == 0){
                throw new Error("Not a remote interface");
            }
        }
        
        if (address != null){
        	this.ServAddr = address.getHostName();
        	this.port_num = address.getPort();
        }
    	this.server = server;
    	this.c = c;
    }

    
    protected void stopped(Throwable cause)
    {
    	System.out.println("Skeleton stopped");
    }

    protected boolean listen_error(Exception exception)
    {
        return false;
    }

    protected void service_error(RMIException exception)
    {
    	System.out.println("Skeleton service_error");
    }

    public synchronized void start() throws RMIException
    {
    	if(SkRunning == false){
    		SkRunning = true;
    		try {
        		tcpServer = new TCPserver<T>(server, port_num, this);
        		this.ServAddr = tcpServer.getServAddress();
        		this.port_num = tcpServer.getPort();
            } catch (IOException e) {
                e.printStackTrace();
            }	
    		tcpServer.start();
    	}
    		
    }

    public synchronized void stop() 
    {
    	SkRunning = false;
		tcpServer.shut();
    }
}