/**
 * Kuvaa yht‰ pelikertaa ja toimii pelin ohjausoliona.
 * Sis‰lt‰‰ tiedot pelikerran ominaisuuksista, pelin tilanteesta 
 * sek‰ tiettyyn pelikertaan liittyvist‰ graafisen k‰yttˆliittym‰n komponenteista.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Peli {

	// pelikerran ominaisuuksien oletusarvot
	
	/**
	 * Pelialueen oletusleveys.
	 */
	public static final int LEVEYS = 10;
	
	/**
	 * Pelialueen oletuskorkeus.
	 */
	public static final int KORKEUS = 10;
	
	/**
	 * Laivojen oletuspituudet taulukossa.
	 */
	public static final int[] LAIVOJENPITUUDET = {5, 4, 3, 3, 2};
	
	// pelikerran ominaisuudet (t‰ss‰ ohjelmaversiossa vakioita)
	
	/**
	 * Pelialueen leveys.
	 */
	private int leveys;
	
	/**
	 * Pelialueen korkeus.
	 */
	private int korkeus;
	
	/**
	 * Laivojen pituudet taulukossa.
	 */
	private int[] laivojenPituudet;
	
	/**
	 * Laivojen yhteispituus. (tietokoneen teko‰lyalgoritmin tarvitsema)
	 */
	private int laivojenYhteispituus;
	
	/**
	 * Tietokonevastustaja.
	 */
	private Tietokone tietokone;
	
	// graafisen k‰yttˆliittym‰n pelikertaan tarvittavat komponentit:
	
	/**
	 * Pelaajan pelialue.
	 */
	private Pelialue ihmisenPelialue;
	
	/**
	 * Vastustajan (tietokoneen) pelialue.
	 */
	private Pelialue tietokoneenPelialue;
	
	/**
	 * Pelin ilmoituslaatikko, jossa kerrotaan mm. ampumistuloksista.
	 */
	private JTextArea ilmoitukset;
	
	/**
	 * Teksti, joka n‰ytt‰‰ pelaajan ampumiskertojen lukum‰‰r‰n.
	 */
	private JLabel yritysTilastot;
	
	/**
	 * Teksti, joka n‰ytt‰‰ pelaajan uponneiden laivojen lukum‰‰r‰n.
	 */
	private JLabel ihmisenTilastot;
	
	/**
	 * Teksti, joka n‰ytt‰‰ vastustajan (tietokoneen) uponneiden laivojen lukum‰‰r‰n.
	 */
	private JLabel tietokoneenTilastot;
	
	// pelin tilannetta kuvaavat muuttujat:
	
	/**
	 * Kertoo, onko peli kesken (true) vai p‰‰ttynyt (false).
	 */
	private boolean peliKesken;
	
	/**
	 * Kertoo, onko laivojen asettaminen kesken (true) vai ei (false).
	 */
	private boolean asetusKesken;
	
	/**
	 * Pit‰‰ kirjaa asetettavan laivan numerosta LAIVOJENPITUUDET-taulukossa.
	 */
	private int asetettavanLaivanNro;
	
	/**
	 * Pelaajan yrityskertojen lukum‰‰r‰.
	 */
	private int yritystenlkm;
	
	/**
	 * Pelaajan upotettujen laivojen lukum‰‰r‰.
	 */
	private int ihmisenLaivojaUpotettu;
	
	/**
	 * Vastustajan (tietokoneen) upotettujen laivojen lukum‰‰r‰.
	 */
	private int tietokoneenLaivojaUpotettu;

	/**
	 * Luo uuden pelikerran, asettaa pelin alkutilaan, 
	 * k‰skee tietokonetta asettamaan laivat ja n‰ytt‰‰ pelaajalle ohjeet.
	 */
	public Peli() {
		this.leveys = LEVEYS;
		this.korkeus = KORKEUS;
		this.laivojenPituudet = LAIVOJENPITUUDET;
		int yhteispituus = 0;
		for(int i=0; i<laivojenPituudet.length; ++i)
			 yhteispituus += laivojenPituudet[i];
		this.laivojenYhteispituus = yhteispituus;
		this.ihmisenPelialue = new Pelialue(this.leveys, this.korkeus, this, true);
		this.tietokoneenPelialue = new Pelialue(this.leveys, this.korkeus, this, false);		
		this.tietokone = new Tietokone(this);		
		this.peliKesken = true;
		this.asetusKesken = true;
		this.asetettavanLaivanNro = 0;
		this.yritystenlkm = 0;
		this.ihmisenLaivojaUpotettu = 0;
		this.tietokoneenLaivojaUpotettu = 0;
		this.ilmoitukset = new JTextArea("", 3, 30);
		this.yritysTilastot = new JLabel("Yrityksi‰: "+this.yritystenlkm);
		this.ihmisenTilastot = new JLabel("Omia laivoja uponnut: "+this.ihmisenLaivojaUpotettu+"/"+this.laivojenPituudet.length);
		this.tietokoneenTilastot = new JLabel("Tietokoneen laivoja uponnut: "+this.tietokoneenLaivojaUpotettu+"/"+this.laivojenPituudet.length);
		
		this.tietokone.asetaLaivat();
		
		this.ilmoita(	"Aseta omalle pelialueellesi laiva, jonka pituus on "+this.laivojenPituudet[0]+".\n" +
						"Vasen hiirin‰pp‰in: vaakasuuntainen laiva klikatusta ruudusta oikealle.\n" +
						"Oikea hiirin‰pp‰in: pystysuuntainen laiva klikatusta ruudusta alasp‰in.");
	}
	
	/**
	 * Hakee pelialueiden leveyden.
	 * 
	 * @return	pelialueiden leveys
	 */
	public int haeLeveys() {
		return this.leveys;
	}
	
	/**
	 * Hakee pelialueiden korkeuden.
	 * 
	 * @return	pelialueiden korkeus
	 */
	public int haeKorkeus() {
		return this.korkeus;
	}
	
	/**
	 * Hakee laivojen pituudet.
	 * 
	 * @return	laivojen pituudet taulukossa
	 */
	public int[] haeLaivojenPituudet() {
		return this.laivojenPituudet;
	}
	
	/**
	 * Hakee laivojen yhteispituuden.
	 * 
	 * @return	laivojen yhteispituus
	 */
	public int haeLaivojenYhteispituus() {
		return this.laivojenYhteispituus;
	}
	
	/**
	 * Hakee pelaajan pelialueen. (k‰yttˆliittym‰‰ ja tietokoneen teko‰ly‰ varten)
	 * 
	 * @return	pelaajan pelialue
	 */
	public Pelialue haeIhmisenPelialue() {
		return this.ihmisenPelialue;
	}
	
	/**
	 * Hakee vastustajan (tietokoneen) pelialueen. (k‰yttˆliittym‰‰ ja tietokoneen teko‰ly‰ varten)
	 * 
	 * @return	vastustajan (tietokoneen) pelialue
	 */
	public Pelialue haeTietokoneenPelialue() {
		return this.tietokoneenPelialue;
	}
	
	/**
	 * Hakee pelin ilmoitusalueen. (k‰yttˆliittym‰‰ varten)
	 * 
	 * @return	pelin ilmoitusalue
	 */
	public JTextArea haeIlmoitukset() {
		return this.ilmoitukset;
	}
	
	/**
	 * Tulostaa viestin pelin ilmoitusalueelle.
	 * 
	 * @param viesti	tulostettava viesti
	 */
	public void ilmoita(String viesti) {
		this.ilmoitukset.setText(viesti);
	}
	
	/**
	 * Hakee ilmoitusalueen t‰m‰nhetkisen tekstin.
	 * 
	 * @return	ilmoitusalueen teksti
	 */
	public String haeIlmoitusteksti() {
		return this.ilmoitukset.getText();
	}

	/**
	 * Hakee yrityskertojen m‰‰r‰n n‰ytt‰v‰n JLabelin. (k‰yttˆliittym‰‰ varten)
	 * 
	 * @return JLabel	yrityskertojen lukum‰‰r‰n kertova JLabel
	 */	
	public JLabel haeYritysTilastot() {
		return this.yritysTilastot;
	}

	/**
	 * Hakee pelaajan upotettujen laivojen m‰‰r‰n n‰ytt‰v‰n JLabelin. (k‰yttˆliittym‰‰ varten)
	 * 
	 * @return JLabel	pelaajan upotettujen laivojen lukum‰‰r‰n kertova JLabel
	 */	
	public JLabel haeIhmisenTilastot() {
		return this.ihmisenTilastot;
	}
	
	/**
	 * Hakee vastustajan (tietokoneen) upotettujen laivojen m‰‰r‰n n‰ytt‰v‰n JLabelin. (k‰yttˆliittym‰‰ varten)
	 * 
	 * @return JLabel	vastustajan (tietokoneen) upotettujen laivojen lukum‰‰r‰n kertova JLabel
	 */	
	public JLabel haeTietokoneenTilastot() {
		return this.tietokoneenTilastot;
	}
	
	/**
	 * Kasvattaa pelaajan ampumisyritysten lukum‰‰r‰‰ yhdell‰ ja p‰ivitt‰‰ vastaavan JLabelin.
	 */
	public void lisaaYritys() {
		this.yritystenlkm++;
		this.yritysTilastot.setText("Yrityksi‰: "+this.yritystenlkm);
	}
	
	/**
	 * Kasvattaa pelaajan upotettujen laivojen lukum‰‰r‰‰ yhdell‰ ja p‰ivitt‰‰ vastaavan JLabelin.
	 */
	public void lisaaIhmisenUpotettu() {
		this.ihmisenLaivojaUpotettu++;
		this.ihmisenTilastot.setText("Omia laivoja uponnut: "+this.ihmisenLaivojaUpotettu+"/"+LAIVOJENPITUUDET.length);	
	}	
	
	/**
	 * Kasvattaa vastustajan (tietokoneen) upotettujen laivojen lukum‰‰r‰‰ yhdell‰ ja p‰ivitt‰‰ vastaavan JLabelin.
	 */
	public void lisaaTietokoneenUpotettu() {
		this.tietokoneenLaivojaUpotettu++;
		this.tietokoneenTilastot.setText("Tietokoneen laivoja uponnut: "+this.tietokoneenLaivojaUpotettu+"/"+LAIVOJENPITUUDET.length);	
	}
	
	/**
	 * Ohjaa pelin kulkua k‰ytt‰j‰n pelialueella antamien hiirikomentojen (klikkausten) perusteella. 
	 * Jos laivojen asetus on kesken ja hiiritapahtuma tuli pelaajan pelialueelta, 
	 * yritt‰‰ asettaa pelialueelle asetettavan laivan. Jos asetus ei onnistunu, ilmoittaa pelaajalle. 
	 * Jos asetettava laiva oli viimeinen, lopettaa asetuksen ja k‰skee pelaajaa ampumaan vihollislaivastoa. 
	 * Jos asetus on ohi ja peli kesken, ampuu pelaajan klikkaamaa ruutua ja ilmoittaa tuloksen pelaajalle. 
	 * Mik‰li pelaaja upotti tietokoneen viimeisen laivan, lopettaa pelin ja ilmoittaa voitosta. 
	 * Jos pelaajan ampumisvuoro ei p‰‰ttynyt voittoon, k‰skee tietokoneen ampua ja ilmoittaa 
	 * tietokoneen ampumistuloksen. Jos tietokone upotti pelaajan viimeisen laivan, ilmoittaa pelaajalle h‰viˆst‰.
	 * 
	 * @param tapahtuma		k‰ytt‰j‰n hiirell‰ aiheuttama tapahtuma
	 * @param alue			pelialue, josta hiiritapahtuma tuli
	 */	
	public void kasitteleTapahtuma(MouseEvent tapahtuma, Pelialue alue) {
		Object aiheuttaja = tapahtuma.getComponent();
		if (aiheuttaja instanceof Ruutu) {	
			Ruutu ruutu = (Ruutu)aiheuttaja;
			if(this.asetusKesken) {
				if(alue.omistajaOnIhminen()) {
					Laiva asetettava = new Laiva(this.laivojenPituudet[this.asetettavanLaivanNro]);
					int suunta = 0;
					if(tapahtuma.getButton() == MouseEvent.BUTTON1)
						suunta = Ruutu.VAAKA;
					else if(tapahtuma.getButton() == MouseEvent.BUTTON3)
						suunta = Ruutu.PYSTY;
					boolean asetusOnnistui = alue.asetaLaiva(asetettava, ruutu, suunta, true);
					if(asetusOnnistui) {
						this.asetettavanLaivanNro++;
						if(this.asetettavanLaivanNro >= this.laivojenPituudet.length) {
							this.asetusKesken = false;
							this.ilmoita("Laivat on asetettu ja merisota voi alkaa!\nAmmu vihollisen laivoja!");							
						}
						else
							this.ilmoita("Aseta omalle pelialueellesi laiva, jonka pituus on "+this.laivojenPituudet[this.asetettavanLaivanNro]+".");
					}
					else
						this.ilmoita("Laiva ei mahdu osoittamaasi kohtaan! Yrit‰ uudelleen.");
				}
			}
			else if(this.peliKesken) {
				if(!alue.omistajaOnIhminen()) {
					int tulos = ruutu.ammu();
					if(tulos == Ruutu.HUTI) {
						this.lisaaYritys();
						ilmoita("Huti meni!");
					}
					else if(tulos == Ruutu.OSUMA) {
						lisaaYritys();
						ilmoita("Osuit laivaan!");
					}
					else if(tulos == Ruutu.UPONNUT) {
						lisaaYritys();
						lisaaTietokoneenUpotettu();
						ilmoita("Osuit ja upotit laivan!");
						if(this.tietokoneenLaivojaUpotettu == this.laivojenPituudet.length) {
							this.peliKesken = false;
							ilmoita("Upotit kaikki laivat ja voitit!");
							Tuloslista lista = new Tuloslista(this.yritystenlkm);	
							// aloitetaan pelituloksen tarkistus tuloslistaa varten ja ilmoitetaan samalla voitosta
						}
					}
					if(this.peliKesken && tulos != Ruutu.AMMUTTU) {
						int tietokoneenTulos = this.tietokone.pommita();
						if(tietokoneenTulos == Ruutu.HUTI)
							ilmoita(haeIlmoitusteksti()+"\nTietokone ampui huti!");
						else if(tietokoneenTulos == Ruutu.OSUMA)
							ilmoita(haeIlmoitusteksti()+"\nTietokone osui laivaasi!");
						else if(tietokoneenTulos == Ruutu.UPONNUT) {
							lisaaIhmisenUpotettu();
							ilmoita(haeIlmoitusteksti()+"\nTietokone upotti laivasi!");
							if(this.ihmisenLaivojaUpotettu == this.laivojenPituudet.length) {
								this.peliKesken = false;
								ilmoita(haeIlmoitusteksti()+"\nKaikki laivasi on upotettu! H‰visit!");
								Ilmoitusikkuna havisit = new Ilmoitusikkuna("H‰visit!", "H‰visit pelin!");
							}
						}
					}
				}
			}
		}
	}
}