package rmi;

import java.net.*;
import java.lang.reflect.*;

public abstract class Stub
{
    @SuppressWarnings("unchecked")
	public static <T> T create(Class<T> c, Skeleton<T> skeleton)
        throws UnknownHostException
    {
    	//Check whether c or skeleton is 
    	if(c == null || skeleton == null){
            throw new NullPointerException();
        }
    	//Check whether C is the interface. In the test case, it is BadInterface.
    	if(!c.isInterface()) throw new Error("Not an interface");

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
        
        if(skeleton.ServAddr == null) {
    		throw new IllegalStateException();
    	}
        InetSocketAddress address = new InetSocketAddress(skeleton.ServAddr, skeleton.port_num);
        InvocationHandler handler = new StubProxyHandler(address, c);
        return (T) java.lang.reflect.Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] {c}, handler);
    }

    @SuppressWarnings("unchecked")
	public static <T> T create(Class<T> c, Skeleton<T> skeleton,
                               String hostname)
    {
    	if(c == null || skeleton == null || hostname == null){
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
    	if (skeleton.port_num == 9999) {
    		throw new IllegalStateException();
    	}
       
        InetSocketAddress address = new InetSocketAddress(hostname, skeleton.port_num);
        InvocationHandler handler = new StubProxyHandler(address, c);
        return (T) java.lang.reflect.Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] {c}, handler);
    }

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> c, InetSocketAddress address)
    {
		if(c == null || address == null){
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
        
		InvocationHandler handler = new StubProxyHandler(address, c);
        return (T) java.lang.reflect.Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] {c}, handler);
    }

}
