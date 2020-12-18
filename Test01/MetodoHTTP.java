import java.io.*;
import java.net.*;

public class MetodoHTTP{
	public static void main(String args[]){
		URL url = new URL("http://sisdis.sytes.net:8080/Servicio/rest/ws/prueba");
		HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
		conexion.setDoOutput(true);
		conexion.setRequestMethod("POST");
		conexion.setRequestProperty("Content-Type","application/x-www-urlencoded");
		Strin parametros = "a=1000";
		OutputStream os = conexion.getOutputStream();
		os.write(parametros.getBytes());
		os.flush();
		if(conexion.getResponseCode()!=HttpURLConnection.HTTP_OK)
			throw new RuntimeException("Codigo de error HTTP: "+conexion.getResponseCode());
		BufferedReader br = new BufferedReader(new InputStream((conexion.getInputStream())));
		String respuesta;
		while((respuesta = br.readLine())!=null)
			System.out.println(respuesta);
		conexion.disconnect();
	}
}