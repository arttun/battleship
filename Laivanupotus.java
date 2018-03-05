/**
 * Toteuttaa laivanupotuspelin graafisen k�ytt�liittym�ikkunan.
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
	
	// pysyv�t k�ytt�liittym�komponentit (navigointipainikkeet):
	
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
	
	// mahdollisesti pelikerroittain muuttuvat k�ytt�liittym�komponentit:
	
	/**
	 * Teksti, joka kertoo vasemmanpuoleisen pelialueen omistajan (t�ss� versiossa aina "Pelaaja").
	 */
	private JLabel otsikko1;
	
	/**
	 * Teksti, joka kertoo oikeanpuoleisen pelialueen omistajan (t�ss� versiossa aina "Tietokone").
	 */
	private JLabel otsikko2;
	
	/**
	 * Teksti, joka n�ytt�� pelaajan uponneiden laivojen lukum��r�n.
	 */
	private JLabel ihmisenTilastot;
	
	/**
	 * Teksti, joka n�ytt�� vastustajan (tietokoneen) uponneiden laivojen lukum��r�n.
	 */
	private JLabel tietokoneenTilastot;
	
	/**
	 * Teksti, joka n�ytt�� pelaajan ampumiskertojen lukum��r�n.
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
	 * Layoutmanageriin liittyv�t vakiot.
	 */
	private GridBagConstraints gbc;
	
	/**
	 * Luo pelin graafisen k�ytt�liittym�ikkunan ja uuden pelikerran.
	 */	
	public Laivanupotus() {
		
		this.peli = new Peli();		
		
		this.gbl = new GridBagLayout();
		this.setLayout(gbl);
		this.gbc = new GridBagConstraints();
		
		// asetetaan muuttuvat k�ytt�liittym�komponentit
		this.asetaKomponentit();
		
		// asetetaan pysyv�t k�ytt�liittym�komponentit		
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
	 * Asettaa pelikerroittain vaihtuvat komponentit k�ytt�liittym��n.
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
	 * K�sittelee k�ytt�liittym�komponenttien tapahtumat 
	 * ja toteuttaa Uusi peli-, Lopeta- ja Parhaat tulokset -nappien toiminnan.
	 * 
	 * @param tapahtuma		k�ytt�liittym�ikkunassa ilmennyt tapahtuma
	 */	
	public void actionPerformed(ActionEvent tapahtuma) {
		Object aiheuttaja = tapahtuma.getSource();
		
		if(aiheuttaja == this.uusipeli) {
			// poistetaan komponentit, jotka uutta peli� luodessa mahdollisesti vaihdetaan
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

			// lis�t��n uudet komponentit
			this.asetaKomponentit();
			
			// tulostetaan ohjeet k�ytt�j�lle
			this.peli.ilmoita(	"Aseta omalle pelialueellesi laiva, jonka pituus on "+Peli.LAIVOJENPITUUDET[0]+".\n" +
								"Vasen hiirin�pp�in: vaakasuuntainen laiva klikatusta ruudusta oikealle.\n" +
								"Oikea hiirin�pp�in: pystysuuntainen laiva klikatusta ruudusta alasp�in.");
		}
		
		else if(aiheuttaja == this.lopeta)
			System.exit(0);
		
		else if(aiheuttaja == this.tulokset) {
			Tulosikkuna tulokset = new Tulosikkuna();
		}
	}		
	
	/**
	 * Luo k�ytt�liittym�ikkunan n�yt�lle.
	 * 
	 * @param args		komentoriviparametrit (eiv�t vaikuta ohjelman toimintaan)
	 */	
	public static void main(String[] args) {
		Laivanupotus peliIkkuna = new Laivanupotus();
	}
}