/**
 * Ohjaa pelituloksen k‰sittely‰ pelaajan voiton j‰lkeen. 
 * Ilmoittaa voitosta ja tarkistaa, p‰‰sikˆ pelaaja tuloslistalle. 
 * Myˆnteisess‰ tapauksessa ilmoittaa listalle p‰‰syst‰, kysyy pelaajan nimen, 
 * p‰ivitt‰‰ ja n‰ytt‰‰ tuloslistan.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Tuloslista {

	/**
	 * Tuloslistalla s‰ilytett‰vien tulosten lukum‰‰r‰.
	 */
	public static final int LISTANPITUUS = 5;
	
	/**
	 * Pelaajan ampumisyrityskertojen lukum‰‰r‰.
	 */
	private int yrityslkm;
	
	/**
	 * Pelaajan sijoitus.
	 */
	private int sija;
	
	/**
	 * Pelaajan nimi.
	 */
	private String nimi;
	
	/**
	 * Pit‰‰ kirjaa pelituloksen ominaisuuksista (yritysten lukum‰‰r‰, sijoitus, nimi) 
	 * sen k‰sittelyn aikana ja luo aluksi ikkunan, jossa ilmoitetaan pelin voitosta.
	 * 
	 * @param yrityslkm		pelaajan ampumisyritysten lukum‰‰r‰
	 */
	public Tuloslista(int yrityslkm) {		
		this.yrityslkm = yrityslkm;
		this.sija = 0;
		this.nimi = null;
		
		Ilmoitusikkuna voitto = new Ilmoitusikkuna(this);
	}
	
	/**
	 * Palauttaa pelaajan sijoituksen.
	 */
	public int haeSija() {
		return this.sija;
	}
	
	/**
	 * Tarkistaa, onko pelaajan yritysten lukum‰‰r‰ tarpeeksi alhainen listalle p‰‰syyn. 
	 * Myˆnteisess‰ tapauksessa luo kyselyikkunan, jossa ilmoittaa sijoituksesta pelaajalle ja kysyy nimen, 
	 * kielteisess‰ tapauksessa ei.
	 * 
	 * @exception FileNotFoundException		jos tuloslista-tiedostoa ei lˆydy
	 */
	public void tarkistaTulos() {		
		if(this.yrityslkm <= 0) {	// virheellinen yritysten m‰‰r‰
			return;
		}
		
		File tiedosto = new File("tuloslista/tuloslista.txt");
		Scanner luku;
		
		try {
			luku = new Scanner(tiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Tuloslista-tiedostoa ei lˆydy!");
			return;
		}
		
		int vanhatulos;
		String rivi;
		int sija = 0;
		
		int i=0;
		while (luku.hasNextLine()) {
			++i;
			rivi = luku.nextLine();
			if(i%2!=0) {
				vanhatulos = Integer.parseInt(rivi);
				if (vanhatulos == 0 || yrityslkm < vanhatulos) {
					sija = (i+1)/2;					
					break;
				}
			}		
		}
		luku.close();
		
		this.sija = sija;
		
		if (this.sija != 0) {
			Kyselyikkuna kysynimi = new Kyselyikkuna(this);
		}
	}
	
	/**
	 * P‰ivitt‰‰ tuloslistan, eli lis‰‰ pelaajan tuloksen ja nimen listalle oikeaan kohtaan, 
	 * ja lopuksi luo tuloslistaikkunan.
	 * T‰t‰ metodia k‰ytet‰‰n vasta, kun on tarkistettu, ett‰ pelaajan tulos oikeuttaa listalle p‰‰syyn.
	 * 
	 * @param nimi							pelaajan nimi
	 * @exception FileNotFoundException		jos tuloslista- tai aputiedostoa ei lˆydy
	 */	
	public void kirjaaTulos(String nimi) {		
		if(this.sija < 1 || this.sija > LISTANPITUUS) { // virheellinen sijoitus
			return;
		}
		
		if(this.yrityslkm <= 0) {	// virheellinen yritysten m‰‰r‰
			return;
		}
		
		this.nimi = nimi;
		
		File tiedosto = new File("tuloslista/tuloslista.txt");	
		File aputiedosto = new File("tuloslista/uusitulos.txt");
		Scanner luku;
		PrintWriter aputulos;
		
		try {
			luku = new Scanner(tiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Tuloslista-tiedostoa ei lˆydy!");
			return;
		}	// jos tuloslistaa ei lˆydy, tee uusi?
		try {
			aputulos = new PrintWriter(aputiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Aputiedostoa ei voitu luoda!");
			return;
		}
		
		String rivi;
		
		// kelataan tuloslistalla oikeaan kohtaan
		for(int i=1; i<this.sija; ++i) {
			rivi = luku.nextLine();
			aputulos.println(rivi);
			rivi = luku.nextLine();
			aputulos.println(rivi);
		}

		// lis‰t‰‰n uusi tulos oikeaan kohtaan
		aputulos.println(this.yrityslkm);
		aputulos.println(this.nimi);

		// lis‰t‰‰n vanhan tuloslistan loppuosa uuden tuloksen j‰lkeen
		while (luku.hasNextLine()) {
			rivi = luku.nextLine();
			aputulos.println(rivi);
		}
		
		luku.close();
		aputulos.close();
		
		Scanner apuluku;
		PrintWriter tulos;
		
		try {
			apuluku = new Scanner(aputiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Aputiedostoa ei lˆydy!");
			return;
		}
		try {
			tulos = new PrintWriter(tiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Tiedostoa ei voitu luoda!");
			return;
		}
		
		// kopioidaan aputiedoston alusta oikea m‰‰r‰ tuloksia varsinaiselle tuloslistalle
		for(int j=0; j<2*LISTANPITUUS; ++j) {
			rivi = apuluku.nextLine();
			tulos.println(rivi);
		}
		apuluku.close();
		tulos.close();
		
		// n‰ytet‰‰n tuloslista ikkunassa
		Tulosikkuna tulokset = new Tulosikkuna();
	}
}
