package projekt.komunikator.klient;


import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Klient {
	
	JTextArea odebrane;
	JTextField wiadomosc;
	BufferedReader odbiorca;
	PrintWriter nadawca;
	Socket gniazdo;

	
	/**
	 * 
	 * @ Metoda main sluzaca do tworzenia obiektu Klient
	 */
	public static void main(String[] args) {
		Klient user = new Klient();
		user.budowanie();
	}
	/**
	 * metoda budowanie sluzaca do stworzenia okienka komunikatora wraz z jego funkcjonalnosciami
	 */
	public void budowanie() {
		JFrame okienko = new JFrame("komunikator gadu-gadu");
		JPanel panel = new JPanel();
		
		odebrane = new JTextArea(12,40);
		odebrane.setLineWrap(true);
		odebrane.setEditable(false);
		
		JScrollPane scrollowanie = new JScrollPane(odebrane);
		scrollowanie.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollowanie.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		wiadomosc = new JTextField(40);
		JButton wyslij = new JButton("Wyslij wiadomosc");

		wyslij.setBounds(0, 100, 30, 50);
		wyslij.getColorModel();
		wyslij.addActionListener(new Wysylanie());
		panel.add(scrollowanie);
		panel.add(wiadomosc);
		panel.add(wyslij);
		Konfiguracja();
		
		// okreslanie pierwszej metody ktora wywola nowy watek
		Thread watekOdbierania = new Thread(new OdbiorcaKomunikatow());
		// nowy watek zostanie utworzony dopoki nie zostanie wywolana metoda start 
		watekOdbierania.start();
		
		
		okienko.getContentPane().add(BorderLayout.CENTER,panel);
		okienko.setSize(500, 270);
		okienko.setVisible(true);
		panel.setBackground(Color.darkGray);
		odebrane.setBackground(Color.LIGHT_GRAY);
	}
	/**
	 * metoda Konfiguracja sluzaca do ustawiania polaczenia za pomoca HOST-a i PORTu
	 * 
	 */
	private void Konfiguracja() {
		try {
			gniazdo = new Socket("192.168.0.192",50014);
			InputStreamReader czytelnikS = new InputStreamReader(gniazdo.getInputStream());
			odbiorca = new BufferedReader(czytelnikS); // laczymy strumien buffered z input Streamem oraz wejsciem gniazda
			nadawca = new PrintWriter(gniazdo.getOutputStream());
			System.out.println("siec podlaczona");
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public class Wysylanie implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				nadawca.println("uzytkownik :"+wiadomosc.getText());
				// sprawdzanie czy wszystkie dane z buferu zostaly wyswietlone
				nadawca.flush();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
			wiadomosc.setText("");
			wiadomosc.requestFocus();
		}
	}
	
	/**
	 * 
	 * obsluga watkow
	 *
	 */
	public class OdbiorcaKomunikatow implements Runnable{
		public void run() {
			String MaszWiadomosc;
			// tutaj podajemy zadanie ktore watek ma wykonywac, run zostaje umieszczone na spodzie nowego stosu
			try {
				while((MaszWiadomosc=odbiorca.readLine())!=null) {
					System.out.println("wyswietlono");
					odebrane.append(MaszWiadomosc+ "\n");
				}
			}
			catch(Exception ex) {ex.printStackTrace();
		}
	}
	
}




}










