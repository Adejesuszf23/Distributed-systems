import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;

public class ConexionHOST{
	public static void main(String args[])throws Exception{
		Socket conexion = new Socket("sisdis.sytes.net",10000);
		DataInputStream entrada = new DataInputStream(conexion.getInputStream());
		DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
		salida.writeInt(2);
		salida.writeDouble(200.0);
		double result = entrada.readDouble();
		System.out.println(result);
		salida.close();
		entrada.close();
		conexion.close();
	}
}