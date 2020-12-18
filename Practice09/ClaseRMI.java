import java.rmi.*;
import java.rmi.server.*;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI{
	public int N = 4;

	public ClaseRMI() throws RemoteException{
		super( );
	}

	@Override
	public int[][] multiplica_matrices(int[][] A,int[][] B) throws RemoteException{
		int[][] C = new int[N/2][N/2];
		for(int i = 0; i < N/2; i++)
			for(int j = 0; j < N/2; j++)
				for(int k = 0; k < N; k++)
					C[i][j] += A[i][k] * B[j][k];
		return C;
	}
}