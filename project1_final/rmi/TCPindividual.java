package rmi;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
//import java.util.ArrayList;

public class TCPindividual<T> implements Runnable {
	protected T server;
    protected Socket socket;
    protected Skeleton<?> skeleton = null;
    protected SerializedObj ret = null;
    Object res = null;
    
    public TCPindividual(T server, Socket socket, Skeleton<?> skeleton) {
        this.server = server;
        this.socket = socket;
        this.skeleton = skeleton;
    }
    @Override
    public void run() {
    	try {
    		boolean flag = true;
            ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            String methodName = (String) inputStream.readObject();
            Object paraTypes = inputStream.readObject();
            Object returnType = inputStream.readObject();
            Object methods = inputStream.readObject();
            
            
            try{
            	Method method = skeleton.c.getMethod(methodName, (Class[])paraTypes);
            	/*if(!returnType.equals(method.getReturnType().getName()) ) {	
    				Throwable t = new RMIException("Return Type Erroe");
    				ret = new SerializedObj(t);
    			}*/
            	Object res = method.invoke(server, (Object[])methods);
            	ret = new  SerializedObj(res);
            }catch(IllegalAccessException | IllegalArgumentException e){
				Throwable t = new RMIException(e.getCause());
				ret = new SerializedObj(t);
				flag = false;
			} catch(InvocationTargetException e) {
				//new RMIException(e.getCause());
				ret = new SerializedObj(e.getCause());
				flag = false;
			} catch (NoSuchMethodException | SecurityException e){
				Throwable t = new RMIException(e.getCause());
				ret = new SerializedObj(t);
				flag = false;
			} 
            outputStream.writeObject(flag);
            outputStream.writeObject(ret);
            outputStream.flush();
            this.socket.close();
        } catch (ClassNotFoundException | SecurityException | IOException e){
			skeleton.service_error(new RMIException(e.getCause()));
		} 
    }
    
}