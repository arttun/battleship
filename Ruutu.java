/**
 * Kuvaa pelialueen yksitt�ist� ruutua. Toimii konkreettisena graafisen k�ytt�liittym�n painikkeena.
 * Pit�� kirjaa ruudun tilasta, sijainnista pelialueella sek� ruudussa mahdollisesti olevasta laivasta.
 * Luokka sis�lt�� staattisia int-tyyppisi� luokkavakioita, 
 * joita k�ytet��n kuvaamaan laivan tilaa ja suuntaa sek� etenemissuuntaa pelialueella.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Ruutu extends JButton {

	// ruudun tilaa kuvaavat vakiot
	
	/**
	 * Kuvaa tyhj�n ruudun tilaa.
	 */
	public static final int TYHJA = 0;
	
	/**
	 * Kuvaa sellaisen ruudun, jossa on huti, tilaa.
	 */
	public static final int HUTI = 1;
	
	/**
	 * Kuvaa sellaisen ruudun, jossa on laiva, tilaa.
	 */
	public static final int LAIVA = 2;
	
	/**
	 * Kuvaa sellaisen ruudun, jossa on osuma, tilaa.
	 */
	public static final int OSUMA = 3;
	
	/**
	 * Kuvaa sellaisen ruudun, jossa on uponnut laiva, tilaa.
	 */
	public static final int UPONNUT = 4;
	
	/**
	 * Ammutun ruudun tilaa kuvaava vakioarvo. 
	 * Ei ole koskaan ruudun tilana, mutta metodi saattaa palauttaa t�m�n arvon.
	 */
	public static final int AMMUTTU = 5;
	
	// laivan suuntaa kuvaavat vakiot
	
	/**
	 * Vaakasuuntaisen laivan tilaa kuvaava vakioarvo.
	 */
	public static final int VAAKA = 0;
	
	/**
	 * Pystysuuntaisen laivan tilaa kuvaava vakioarvo.
	 */
	public static final int PYSTY = 1;
	
	// liikkumissuuntaa pelialueella kuvaavat vakiot
	// on t�rke��, ett� VAAKA=OIKEA ja PYSTY=ALAS
	
	/**
	 * Vakioarvo, joka kuvaa liikkumissuuntaa oikealle.
	 */
	public static final int OIKEA = 0;
	
	/**
	 * Vakioarvo, joka kuvaa liikkumissuuntaa alas.
	 */
	public static final int ALAS = 1;
	
	/**
	 * Vakioarvo, joka kuvaa liikkumissuuntaa vasemmalle.
	 */
	public static final int VASEN = 2;
	
	/**
	 * Vakioarvo, joka kuvaa liikkumissuuntaa yl�s.
	 */
	public static final int YLOS = 3;
	
	/**
	 * Vakioarvo, joka kuvaa m��rittelem�t�nt� liikkumissuuntaa (tietokoneen teko�lyalgoritmin k�ytt�m�).
	 */
	public static final int MAARITTELEMATON = 4;
	
	// ruudun mitat
	
	/**
	 * Ruudun sivun pituus.
	 */
	public static final int SIVU = 20;
	
	/**
	 * Ruudun koko, joka m��r�ytyy sen sivun pituudesta.
	 */
	public static final Dimension KOKO = new Dimension(SIVU, SIVU);
	
	// ruudun ominaisuudet
	
	/**
	 * Ruudun tila. Sallitut arvot TYHJA, HUTI, LAIVA, OSUMA ja UPONNUT.
	 */
	private int tila;
	
	/**
	 * Ruudussa oleva laiva tai null, jos ruudussa ei ole laivaa.
	 */
	private Laiva ruudunLaiva;
	
	/**
	 * Ruudun x-koordinaatti pelialueella. Sallitut arvot 0...pelialueen leveys-1.
	 */
	private int x;
	
	/**
	 * Ruudun y-koordinaatti pelialueella. Sallitut arvot 0...pelialueen korkeus-1.
	 */
	private int y;
	
	/**
	 * Luo uuden alkutilaisen (tyhj�, ei laivaa) ruudun, jonka koordinaatit ovat parametrien mukaiset.
	 * K�ytett�ess� on huolehdittava siit�, ett� ruudun koordinaatit ovat pelialueella.
	 * 
	 * @param x		ruudun x-koordinaatti, oltava ep�negatiivinen
	 * @param y		ruudun y-koordinaatti, oltava ep�negatiivinen
	 */	
	public Ruutu(int x, int y){
		super(new ImageIcon("grafiikka/meri.gif"));
		this.setSize(KOKO);
		this.setMinimumSize(KOKO);
		this.setMaximumSize(KOKO);
		this.setPreferredSize(KOKO);
		this.tila = TYHJA;
		this.ruudunLaiva = null;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Hakee ruudun x-koordinaatin.
	 * 
	 * @return	ruudun x-koordinaatti
	 */
	public int haeX() {
		return this.x;
	}
	
	/**
	 * Hakee ruudun y-koordinaatin.
	 * 
	 * @return	ruudun y-koordinaatti
	 */
	public int haeY() {
		return this.y;
	}
	
	/**
	 * Hakee ruudun tilan.
	 * 
	 * @return	ruudun tilan kertova arvo
	 */	
	public int haeTila(){
		return this.tila;
	}
	
	/**
	 * Muuttaa ruudun tilan ja grafiikan halutun tilan mukaiseksi.
	 * Jos tilaksi halutaan asettaa LAIVA,
	 * ruudun grafiikka vaihdetaan vain, jos parametri vaihdaKuva=true
	 * (jotta tietokoneen asettamat laivat eiv�t n�y ihmiselle).
	 * 
	 * @param tila			arvo, joka kertoo ruudun uuden tilan; sallitut arvot HUTI, LAIVA, OSUMA, UPONNUT, TYHJA
	 * @param vaihdaKuva	jos false, ruudun grafiikkaa ei vaihdeta silloin kun tilaksi asetetaan laiva;
	 * 						jos true, vaihdetaan
	 */	
	public void asetaTila(int tila, boolean vaihdaKuva) {
		if (tila == HUTI) {
			this.tila = HUTI;
			this.setIcon(new ImageIcon("grafiikka/huti.gif"));
			return;
		}
		else if (tila == LAIVA) {
			this.tila = LAIVA;
			if (vaihdaKuva)
				this.setIcon(new ImageIcon("grafiikka/laiva.gif"));
			return;
		}
		else if (tila == OSUMA) {
			this.tila = OSUMA;
			this.setIcon(new ImageIcon("grafiikka/osuma.gif"));
			return;
		}
		else if (tila == UPONNUT) {
			this.tila = UPONNUT;
			this.setIcon(new ImageIcon("grafiikka/uponnut.gif"));
			return;
		}
		else if (tila == TYHJA) {		// t�t� ei k�ytet� t�ss� peliversiossa
			this.tila = TYHJA;
			this.setIcon(new ImageIcon("grafiikka/meri.gif"));
		}
	}
	
	/**
	 * Asettaa ruudulle laivan.
	 * 
	 * @param laiva		asetettava laiva
	 */
	public void asetaRuudunLaiva(Laiva laiva) {
		this.ruudunLaiva = laiva;
	}
	
	/**
	 * Ottaa vastaan iskun ruutuun ja vaihtaa tilan sen edellisen tilan mukaan.
	 * 
	 * @return	ampumistulos eli arvo, joka kuvaa ruudun uutta ampumisen j�lkeist� tilaa 
	 */	
	public int ammu() {
		if(this.tila == TYHJA) {						// tyhja => huti
			this.asetaTila(HUTI, true);
			return HUTI;
		}
		else if(this.tila == LAIVA) {					// laiva => osuma
			this.asetaTila(OSUMA, true);
			if(this.ruudunLaiva.upposiko() == true) {	// tarkistetaan, onko kaikkiin laivan ruutuihin ammuttu
				this.ruudunLaiva.upota();				// laiva huolehtii, ett� kaikkien laivalle kuuluvien ruutujen tilaksi/grafiikaksi vaihdetaan UPONNUT
				return UPONNUT;
			}
			else
				return OSUMA;							// osui, mutta ei uponnut
		}
		else
			return AMMUTTU; 							// ruutuun oli jo ammuttu
	}
}
