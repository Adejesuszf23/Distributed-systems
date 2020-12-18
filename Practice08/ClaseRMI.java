import java.rmi.*;
import java.rmi.server.*;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI{
	public ClaseRMI() throws RemoteException{
		super( );
	}

	@Override
	public String mayusculas(String s) throws RemoteException{
		return s.toUpperCase();
	}
	
	@Override
	public int suma(int a,int b){
		return a + b;
	}

	@Override
	public long checksum(int[][] m) throws RemoteException{
		long s = 0;
		for(int i = 0; i < m.length; i++)
			for(int j = 0; j < m[0].length; j++)
				s += m[i][j];
		return s;
	}
}