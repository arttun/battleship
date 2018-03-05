/**
 * Toteuttaa laivanupotuspelin graafisen käyttöliittymäikkunan.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Laivanupotus extends JFrame implements ActionListener {

	/**
	 * Menossa oleva pelikerta.
	 */
	private Peli peli;
	
	// pysyvät käyttöliittymäkomponentit (navigointipainikkeet):
	
	/**
	 * "Uusi peli" -navigointipainike.
	 */
	private JButton uusipeli;
	
	/**
	 * "Lopeta"-navigointipainike.
	 */
	private JButton lopeta;
	
	/**
	 * "Parhaat tulokset" -navigointipainike.
	 */
	private JButton tulokset;
	
	// mahdollisesti pelikerroittain muuttuvat käyttöliittymäkomponentit:
	
	/**
	 * Teksti, joka kertoo vasemmanpuoleisen pelialueen omistajan (tässä versiossa aina "Pelaaja").
	 */
	private JLabel otsikko1;
	
	/**
	 * Teksti, joka kertoo oikeanpuoleisen pelialueen omistajan (tässä versiossa aina "Tietokone").
	 */
	private JLabel otsikko2;
	
	/**
	 * Teksti, joka näyttää pelaajan uponneiden laivojen lukumäärän.
	 */
	private JLabel ihmisenTilastot;
	
	/**
	 * Teksti, joka näyttää vastustajan (tietokoneen) uponneiden laivojen lukumäärän.
	 */
	private JLabel tietokoneenTilastot;
	
	/**
	 * Teksti, joka näyttää pelaajan ampumiskertojen lukumäärän.
	 */
	private JLabel yritysTilastot;
	
	/**
	 * Ihmisen pelialue.
	 */
	private Pelialue ihmisenPelialue;
	
	/**
	 * Vastustajan (tietokoneen) pelialue.
	 */
	private Pelialue tietokoneenPelialue;
	
	/**
	 * Pelin ilmoituslaatikko.
	 */
	private JTextArea ilmoitusLaatikko;
	
	// layouttia varten	
	
	/**
	 * Layoutmanageri.
	 */
	private GridBagLayout gbl;
	
	/**
	 * Layoutmanageriin liittyvät vakiot.
	 */
	private GridBagConstraints gbc;
	
	/**
	 * Luo pelin graafisen käyttöliittymäikkunan ja uuden pelikerran.
	 */	
	public Laivanupotus() {
		
		this.peli = new Peli();		
		
		this.gbl = new GridBagLayout();
		this.setLayout(gbl);
		this.gbc = new GridBagConstraints();
		
		// asetetaan muuttuvat käyttöliittymäkomponentit
		this.asetaKomponentit();
		
		// asetetaan pysyvät käyttöliittymäkomponentit		
		this.uusipeli = new JButton("Uusi peli");
		this.uusipeli.addActionListener(this);
		this.lopeta = new JButton("Lopeta");
		this.lopeta.addActionListener(this);
		this.tulokset = new JButton("Parhaat tulokset");
		this.tulokset.addActionListener(this);
		
		JPanel nappulat = new JPanel(new FlowLayout());
		
	    nappulat.add(this.uusipeli);
	    nappulat.add(this.lopeta);
	    nappulat.add(this.tulokset);
	    
	    this.gbc.fill = GridBagConstraints.NONE;
	    asetaGBCVakiot(this.gbc, 0, 5, GridBagConstraints.CENTER);
	    this.gbl.setConstraints(nappulat, this.gbc);
	    this.add(nappulat);
	   
	    this.setResizable(false);
		this.setTitle("Laivanupotus");
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Asettaa tietyt GridBagConstraints-vakiot parametrien mukaisiksi. (apumetodi)
	 * 
	 * @param gbc		muutettavat GridBagConstraints-vakiot
	 * @param x			komponentin solun x-koordinaatti
	 * @param y			komponentin solun y-koordinaatti
	 * @param a			arvo, joka kertoo komponentin sijoittelun solussa
	 */
	public void asetaGBCVakiot(GridBagConstraints gbc, int x, int y, int a) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.anchor = a;
	}
	
	/**
	 * Asettaa pelikerroittain vaihtuvat komponentit käyttöliittymään.
	 */	
	public void asetaKomponentit() {		
		this.gbc.fill = GridBagConstraints.NONE;
		this.gbc.ipadx = 0;
		this.gbc.ipady = 0;
		this.gbc.weightx = 100;
		this.gbc.weighty = 100;
		this.gbc.gridwidth = 1;
		this.gbc.gridheight = 1;
		this.gbc.insets = new Insets(5, 10, 5, 10);
		
		this.otsikko1 = new JLabel("Pelaaja");
		this.otsikko2 = new JLabel("Tietokone");
		this.ihmisenTilastot = this.peli.haeIhmisenTilastot();
		this.tietokoneenTilastot = this.peli.haeTietokoneenTilastot();
		this.yritysTilastot = this.peli.haeYritysTilastot();
		this.ihmisenPelialue = this.peli.haeIhmisenPelialue();
		this.tietokoneenPelialue = this.peli.haeTietokoneenPelialue();
		this.ilmoitusLaatikko = this.peli.haeIlmoitukset();
		
		asetaGBCVakiot(this.gbc, 0, 0, GridBagConstraints.WEST);	
		this.gbl.setConstraints(this.otsikko1, this.gbc);
	    this.add(this.otsikko1);
	    
	    asetaGBCVakiot(this.gbc, 1, 0, GridBagConstraints.EAST);	
	    this.gbl.setConstraints(this.otsikko2, this.gbc);
	    this.add(this.otsikko2);
	    
	    asetaGBCVakiot(this.gbc, 0, 1, GridBagConstraints.WEST);	
	    this.gbl.setConstraints(this.ihmisenPelialue, this.gbc);
	    this.add(this.ihmisenPelialue);
	    
	    asetaGBCVakiot(this.gbc, 1, 1, GridBagConstraints.EAST);	
	    this.gbl.setConstraints(this.tietokoneenPelialue, this.gbc);
	    this.add(this.tietokoneenPelialue);
	    
	    asetaGBCVakiot(this.gbc, 0, 2, GridBagConstraints.WEST);	
	    this.gbl.setConstraints(this.ihmisenTilastot, this.gbc);
	    this.add(this.ihmisenTilastot);
	    
	    asetaGBCVakiot(this.gbc, 1, 2, GridBagConstraints.EAST);	
	    this.gbl.setConstraints(this.tietokoneenTilastot, this.gbc);
	    this.add(this.tietokoneenTilastot);
	    	
	    this.gbc.gridwidth = 2;
	    asetaGBCVakiot(gbc, 0, 3, GridBagConstraints.WEST);	
	    this.gbl.setConstraints(yritysTilastot, this.gbc);
	    this.add(this.yritysTilastot);
	    
		this.gbc.fill = GridBagConstraints.HORIZONTAL;
	    asetaGBCVakiot(this.gbc, 0, 4, GridBagConstraints.CENTER);	
	    this.gbl.setConstraints(this.ilmoitusLaatikko, this.gbc);
	    this.add(this.ilmoitusLaatikko);
	}
	
	/**
	 * Käsittelee käyttöliittymäkomponenttien tapahtumat 
	 * ja toteuttaa Uusi peli-, Lopeta- ja Parhaat tulokset -nappien toiminnan.
	 * 
	 * @param tapahtuma		käyttöliittymäikkunassa ilmennyt tapahtuma
	 */	
	public void actionPerformed(ActionEvent tapahtuma) {
		Object aiheuttaja = tapahtuma.getSource();
		
		if(aiheuttaja == this.uusipeli) {
			// poistetaan komponentit, jotka uutta peliä luodessa mahdollisesti vaihdetaan
			this.remove(this.otsikko1);
			this.remove(this.otsikko2);
			this.remove(this.ihmisenTilastot);
			this.remove(this.tietokoneenTilastot);
			this.remove(this.yritysTilastot);
			this.remove(this.ihmisenPelialue);
			this.remove(this.tietokoneenPelialue);
			this.remove(this.ilmoitusLaatikko);
			
			// luodaan uusi pelikerta
			this.peli = new Peli();

			// lisätään uudet komponentit
			this.asetaKomponentit();
			
			// tulostetaan ohjeet käyttäjälle
			this.peli.ilmoita(	"Aseta omalle pelialueellesi laiva, jonka pituus on "+Peli.LAIVOJENPITUUDET[0]+".\n" +
								"Vasen hiirinäppäin: vaakasuuntainen laiva klikatusta ruudusta oikealle.\n" +
								"Oikea hiirinäppäin: pystysuuntainen laiva klikatusta ruudusta alaspäin.");
		}
		
		else if(aiheuttaja == this.lopeta)
			System.exit(0);
		
		else if(aiheuttaja == this.tulokset) {
			Tulosikkuna tulokset = new Tulosikkuna();
		}
	}		
	
	/**
	 * Luo käyttöliittymäikkunan näytölle.
	 * 
	 * @param args		komentoriviparametrit (eivät vaikuta ohjelman toimintaan)
	 */	
	public static void main(String[] args) {
		Laivanupotus peliIkkuna = new Laivanupotus();
	}
}