/**
 * Ikkuna, jossa kysytään käyttäjän nimi pelituloksen tuloslistalle kirjaamista varten.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Kyselyikkuna extends JFrame implements ActionListener {
	
	/**
	 * Tekstikenttä nimeä varten.
	 */
	private JTextField kentta;
	
	/**
	 * Painike, jota painamalla hyväksytään nimi.
	 */
	private JButton nappi;
	
	/**
	 * Tuloslista-olio, joka loi kyselyikkunan ja sisältää tiedot pelaajan pelituloksesta.
	 */
	private Tuloslista lista;
	
	/**
	 * Luo kyselyikkunan, jossa kerrotaan, monenneksiko pelaaja sijoittui tuloslistalla, 
	 * ja pyydetään kirjoittamaan nimi tekstikenttään. 
	 * "OK"-painikkeen painaminen kirjaa tuloksen listalle.
	 * 
	 * @param lista			Tuloslista-olio, joka sisältää tiedon pelaajan pelituloksesta (ja loi kyselyikkunan)
	 */	
	public Kyselyikkuna(Tuloslista lista) {
		super();
		this.setTitle("Onnittelut!");
		
		this.lista = lista;
		
		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.weightx = 100;
		gbc.weighty = 100;
		gbc.insets = new Insets(5, 10, 5, 10);
		
		JLabel onnittelut = new JLabel("Sijoituit tuloslistalla sijalle "+this.lista.haeSija()+"!");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(onnittelut, gbc);
		this.add(onnittelut);
		
		JLabel annanimi = new JLabel("Anna nimesi:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(annanimi, gbc);
		this.add(annanimi);
		
		this.kentta = new JTextField("", 15);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbl.setConstraints(this.kentta, gbc);
		this.add(this.kentta);
		
		this.nappi = new JButton("OK");
		this.nappi.addActionListener(this);	
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbl.setConstraints(this.nappi, gbc);
		this.add(this.nappi);
		
		this.pack();
	    this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Käsittelee ikkunassa ilmenneet tapahtumat 
	 * eli nappia painettaessa sulkee ikkunan ja kirjaa pelaajan nimen ja yritysten lukumäärän listalle.
	 * Tarvittaessa katkaisee yli 15 merkkiä pitkät nimet.
	 * 
	 * @param tapahtuma		ikkunassa ilmennyt tapahtuma
	 */
	public void actionPerformed(ActionEvent tapahtuma) {
		if(tapahtuma.getSource() == this.nappi) {
			String nimi = this.kentta.getText();
			if (nimi.length() != 0) {
				if (nimi.length() > 15) {
					nimi = nimi.substring(0, 15);	// katkaistaan liian pitkä nimi
				}
				this.lista.kirjaaTulos(nimi);		
				this.dispose();
			}
		}
	}	
}