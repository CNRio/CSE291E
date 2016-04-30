package rmi;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;

public class TCPhandler<T> implements Runnable {
	protected T server;
    protected Socket socket;
    
    public TCPhandler(T server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            String methodName = (String) inputStream.readObject();
            int parameterNum = (Integer) inputStream.readObject();
            ArrayList<Class<?>> parameterTypeList = new ArrayList<Class<?>>();
            ArrayList<Object> parameterValueList = new ArrayList<Object>();
            for (int i=0; i<parameterNum; i++) {
                parameterTypeList.add((Class<?>) inputStream.readObject());
                parameterValueList.add(inputStream.readObject());
            }

            Method method = server.getClass().getMethod(methodName, parameterTypeList.toArray(new Class<?>[parameterNum]));
            Object res = method.invoke(server, parameterValueList.toArray());

            outputStream.writeObject(res);
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
}
