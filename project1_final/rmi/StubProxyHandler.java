package rmi;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;


public class StubProxyHandler implements InvocationHandler, Serializable {
    protected InetSocketAddress serverSockAddr;
    private Class c;
    public StubProxyHandler(InetSocketAddress address, Class c) {
        this.serverSockAddr = address;
        this.c = c;
    }

	@Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
		if(method.equals(Object.class.getMethod("toString"))) {
            return this.serverSockAddr.toString() + this.c.toString();
        }
        
        if(method.equals(Object.class.getMethod("hashCode"))) {                 
            return this.serverSockAddr.hashCode() ^ this.c.hashCode();
        }
	
		
		if(method.equals(Object.class.getMethod("equals", Object.class))) {

			if(objects.length != 1 || objects[0] == null || !java.lang.reflect.Proxy.isProxyClass(objects[0].getClass()))
				return false;
			InvocationHandler test1 = java.lang.reflect.Proxy.getInvocationHandler(objects[0]);
			if(!(test1 instanceof StubProxyHandler)) return false;
			StubProxyHandler result1 = (StubProxyHandler)java.lang.reflect.Proxy.getInvocationHandler(proxy);
			StubProxyHandler result2 = (StubProxyHandler)java.lang.reflect.Proxy.getInvocationHandler(objects[0]);
			
			return (result1.getLocalClass().equals(result2.getLocalClass()) && 
				result1.getAddress().equals(result2.getAddress())) ? true : false;
		}

		boolean flag;
		SerializedObj res = null;
        Socket socket = null;
        try {
            socket = new Socket(serverSockAddr.getAddress(), serverSockAddr.getPort());
            ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(method.getName());
            outputStream.writeObject(method.getParameterTypes());
            outputStream.writeObject(method.getReturnType().getName());
            outputStream.writeObject(objects);            
            outputStream.flush();
            flag = (boolean) inputStream.readObject();
            res = (SerializedObj) inputStream.readObject();      
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
        	throw new RMIException(e.getCause());
        } 
        if(!flag) throw (Exception) res.getReturnObj();	
    	return res.getReturnObj();
        
    }
    public InetSocketAddress getAddress() {
		return serverSockAddr; 
	}
    public Class getLocalClass() {
    	return this.c;
    }
 
}
