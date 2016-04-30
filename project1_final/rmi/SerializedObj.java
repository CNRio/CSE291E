package rmi;
import java.io.Serializable;

public class SerializedObj implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object returnObj = null;
	
	public SerializedObj(Object serverReturn) {
		this.returnObj = serverReturn;
	}
	public Object getReturnObj() {
		return returnObj;
	}
}
