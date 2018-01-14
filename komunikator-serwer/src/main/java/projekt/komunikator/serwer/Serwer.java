package projekt.komunikator.serwer;


import java.io.*;
import java.net.*;
import java.util.*;
/**
 * 
 * @author michal
 *
 */
public class Serwer {
	
	public static final int PORT = 50014;

	ArrayList output;
	
	public class obslugaKomunikatora implements Runnable{
		BufferedReader odbiorca;
		Socket gniazdo;
		
		
		public obslugaKomunikatora(Socket clientSocket) {
			try {
				gniazdo= clientSocket;
				InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
				odbiorca = new BufferedReader(isReader);
			}
			catch(Exception ex) {ex.printStackTrace();}}
			
		public void run() {
			String wiadomosc;
			try {
				while((wiadomosc=odbiorca.readLine())!=null) {
					System.out.println("wyswietlono");
					rozsylanie(wiadomosc);
					
				}
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		}
		
		public static void main (String[] args) {
			new Serwer().uruchamianie();
		}
		
		public void uruchamianie() {
			output = new ArrayList<PrintWriter>();
			try {
				// aplikacja oczekuje na przesylane na port dane
				ServerSocket serverSocket = new ServerSocket(PORT);
				// realizacja i obsluga nadsylanych danych przez uzytkownika
				while(true) {
					//wstrzymanie dzialania programu az do momentu odebrania danych, nastepnie zwraca gniazdo
					Socket gniazdoKlienta= serverSocket.accept();
					PrintWriter nadawca = new PrintWriter(gniazdoKlienta.getOutputStream());
					output.add(nadawca);
					
					Thread t = new Thread(new obslugaKomunikatora(gniazdoKlienta));
					t.start();
					System.out.println("polaczenie z serwerem");
					
				}
			}
			catch(Exception ex) {ex.printStackTrace();}
			
		}
		
		public void rozsylanie(String message) {
			Iterator it = output.iterator();
			while(it.hasNext()) {
				try {
					PrintWriter nadawca = (PrintWriter) it.next();
					nadawca.println(message);
					nadawca.flush();
				}
				catch(Exception ex) {ex.printStackTrace();}
			}}}
