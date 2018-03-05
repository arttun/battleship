/**
 * Kuvaa pelialuetta. Toimii konkreettisena graafisen käyttöliittymän komponenttina, 
 * joka luo pelialueen ruudut ja piirtää niistä koostuvan tietyn kokoisen ruudukon.
 * Tarkkailee käyttäjän klikkauksia pelialueella ja lähettää hiiritapahtumat Peli-oliolle käsiteltäviksi.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

public class Pelialue extends JPanel implements MouseListener {
	
	/**
	 * Pelialueen ruudut matriisissa.
	 */
	private Ruutu[][] ruudut;
	
	/**
	 * Pelialueen leveys, oltava positiivinen.
	 */
	private int leveys;
	
	/**
	 * Pelialueen korkeus, oltava positiivinen.
	 */
	private int korkeus;	
	
	/**
	 * Kertoo pelialueen omistajan: true, jos kyseessä on ihmispelaajan alue; false, jos tietokoneen.
	 */
	private boolean omistajaOnIhminen;
	
	/**
	 * Pelikerta, johon pelialue liittyy.
	 */
	private Peli peli;
	
	/**
	 * Luo tietyn kokoisen alkutilaisen pelialueen ja sen sisältämät ruudut.
	 * 
	 * @param leveys	pelialueen leveys, oltava positiivinen
	 * @param korkeus	pelialueen korkeus, oltava positiivinen
	 * @param peli		peli(kerta), johon pelialue liittyy
	 * @param omistaja	true, jos pelialueen omistaja on ihminen; false muuten
	 */
	public Pelialue(int leveys, int korkeus, Peli peli, boolean omistaja) {
		if (leveys > 0)
			this.leveys = leveys;
		else
			this.leveys = Peli.LEVEYS;		// oletusleveys
		if (korkeus > 0)
			this.korkeus = korkeus;
		else
			this.korkeus = Peli.KORKEUS;	// oletuskorkeus
		Dimension alueenKoko = new Dimension(this.leveys*Ruutu.SIVU, this.korkeus*Ruutu.SIVU);
		this.setLayout(new GridLayout(korkeus, leveys));
		this.setSize(alueenKoko);
		this.setPreferredSize(alueenKoko);
		this.setMinimumSize(alueenKoko);
		this.setMaximumSize(alueenKoko);
		this.ruudut = new Ruutu[this.korkeus][this.leveys];
		for(int i=0; i<this.korkeus; ++i) {
			for(int j=0; j<this.leveys; ++j) {
				this.ruudut[i][j] = new Ruutu(j, i);
				this.ruudut[i][j].addMouseListener(this);
				this.add(ruudut[i][j]);
			}
		}
		this.peli = peli;
		this.omistajaOnIhminen = omistaja;
	}
	
	/**
	 * Kertoo pelialueen omistajan.
	 * 
	 * @return	true, jos pelialue on ihmisen; false muuten
	 */
	public boolean omistajaOnIhminen() {
		return this.omistajaOnIhminen;
	}
	
	/**
	 * Hakee pelialueen leveyden.
	 * 
	 * @return	pelialueen leveys
	 */
	public int haeLeveys() {
		return this.leveys;
	}
	
	/**
	 * Hakee pelialueen korkeuden.
	 * 
	 * @return	pelialueen korkeus
	 */
	public int haeKorkeus() {
		return this.korkeus;
	}
	
	/**
	 * Hakee ruudun, joka sijaitsee pelialueella parametrien ilmaisemissa koordinaateissa.
	 * Pelialueen origo (0,0) on vasen ylänurkka ja
	 * siitä oikealle ja alaspäin olevat koordinaatit ovat positiivisia.
	 * 
	 * @param x			ruudun x-koordinaatti (vaaka-akseli)
	 * @param y			ruudun y-koordinaatti (pystyakseli)
	 * @return			Ruutu, joka sijaitsee pelialueella parametrina annetuissa koordinaateissa. 
	 * 					Jos parametrit ovat virheelliset, palauttaa ruudun, joka sijaitsee kohdassa (0,0).
	 */
	public Ruutu haeRuutu(int x, int y) {
		if (0 <= x && x < this.leveys && 0 <= y && y < this.korkeus)
			return this.ruudut[y][x];
		else
			return this.ruudut[0][0];	// tämä ruutu on ainakin olemassa
	}
	
	/**
	 * Hakee annetusta ruudusta halutulla etäisyydellä halutussa suunnassa sijaitsevan ruudun.
	 * 
	 * @param ruutu		ruutu, josta lähdetään
	 * @param suunta	arvo, joka kertoo kulkusuunnan; sallitut arvot YLOS, ALAS, VASEN tai OIKEA (määritelty luokassa Ruutu)
	 * @param matka		montako ruutua kuljetaan, oltava epänegatiivinen
	 * @return			Ruutu, joka sijaitsee lähtöruudusta halutussa suunnassa halutulla etäisyydellä.
	 * 					Jos parametrit ovat virheelliset (matka tai suunta virheellinen tai pelialueen rajat tulevat vastaan),
	 * 					palautetaan parametrina annettu lähtöruutu.
	 */	
	public Ruutu siirry(Ruutu ruutu, int suunta, int matka) {
		if(matka < 0)
			return ruutu;
		int x = ruutu.haeX();
		int y = ruutu.haeY();
		if(suunta == Ruutu.YLOS) {
			if(y-matka < 0)
				return ruutu;
			else
				return this.haeRuutu(x, y-matka);
		}
		else if(suunta == Ruutu.ALAS) {
			if(y+matka >= this.korkeus)
				return ruutu;
			else
				return this.haeRuutu(x, y+matka);
		}
		else if(suunta == Ruutu.VASEN) {
			if(x-matka < 0)
				return ruutu;
			else
				return this.haeRuutu(x-matka, y);
		}
		else if(suunta == Ruutu.OIKEA) {
			if(x+matka >= this.leveys)
				return ruutu;
			else
				return this.haeRuutu(x+matka, y);
		}
		else	// virheellinen suunta
			return ruutu;
	}
	
	/**
	 * Asettaa annetun laivan pelialueelle haluttuun kohtaan halutun suuntaisena (vaaka- tai pystysuuntaan), 
	 * jos se on mahdollista.
	 * Vaakasuuntainen laiva asetetaan parametrina annetusta ruudusta oikealle, 
	 * pystysuuntainen parametrina annetusta ruudusta alaspäin.
	 * Asettaa laivan näkyviin, jos parametrin naytaLaiva arvo on true, muuten ei.
	 * Lähettää myös asetetulle laivalle taulukon sen sijaintiruuduista.
	 * 
	 * @param laiva			asetettava laiva
	 * @param ruutu			laivan ensimmäinen sijaintiruutu (vasemmanpuoleisin/ylin)
	 * @param suunta		arvo, joka kertoo laivan suunnan; sallitut arvot VAAKA tai PYSTY (määritelty luokassa Ruutu)
	 * @param naytaLaiva	true, jos laiva asetetaan näkyviin; false, jos ei
	 * @return				true, jos laivan asettaminen onnistui; false, jos ei
	 */	
	public boolean asetaLaiva(Laiva laiva, Ruutu ruutu, int suunta, boolean naytaLaiva) {
		if(suunta != Ruutu.VAAKA && suunta != Ruutu.PYSTY) {	// virheellinen suunta
			return false;
		}
		if(mahtuukoLaiva(laiva, ruutu, suunta)) {
			Ruutu[] ruudut = new Ruutu[laiva.haePituus()];
			for(int i=0; i<laiva.haePituus(); ++i) {
				Ruutu muokattava = siirry(ruutu, suunta, i);
				muokattava.asetaTila(Ruutu.LAIVA, naytaLaiva);
				muokattava.asetaRuudunLaiva(laiva);	// ruudulle tieto sen laivasta
				ruudut[i] = muokattava;
			}
			laiva.asetaLaivanRuudut(ruudut);	// laivalle tieto sen ruuduista
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Tarkistaa, mahtuuko laiva pelialueelle halutusta ruudusta haluttuun suuntaan niin,
	 * ettei se mene toisen laivan päälle.
	 * 
	 * @param laiva		tarkistettava laiva
	 * @param ruutu		aloitusruutu
	 * @param suunta	laivan suunta; sallitut arvot VAAKA/OIKEA, PYSTY/ALAS, VASEN, YLOS
	 * @return boolean	true, jos mahtuu; false, jos ei
	 */	
	public boolean mahtuukoLaiva(Laiva laiva, Ruutu ruutu, int suunta) {
		if(ruutu.haeTila() != Ruutu.TYHJA)	// tarkistetaan ensimmäinen ruutu erikseen, koska muiden ruutujen käsittelytapa ei sovi siihen
			return false;
		for(int i=1; i<laiva.haePituus(); ++i) {
			Ruutu tarkistettava = siirry(ruutu, suunta, i);
			if(tarkistettava == ruutu || tarkistettava.haeTila() != Ruutu.TYHJA)
				// joko siirry-metodi palautti aloitusruudun eli ruutu, 
				// johon olisi haluttu siirtyä ei sijainnut pelialueella,
				// tai siirry-metodin palauttama ruutu ei ollut tyhjä
				return false;
		}
		return true;	// tämä palautuslause suoritetaan, jos kaikki sijoitusruudut olivat kelvollisia
	}

	/**
	 * Käskee pelialueeseen liittyvän pelin käsittelemään pelialueella ilmenneen hiiritapahtuman.
	 * 
	 * @param tapahtuma		pelialueella ilmennyt hiiritapahtuma
	 */
	public void mouseClicked(MouseEvent tapahtuma) {
		this.peli.kasitteleTapahtuma(tapahtuma, this);
	}
	
	/* 
	 * Seuraavat neljä metodia eivät tee mitään, mutta niiden on oltava mukana, 
	 * koska luokka toteuttaa MouseListener-rajapinnan.
	 */
	
	/**
	 * Ei tee mitään. (MouseListener-rajapinnan vaatima metodi)
	 * 
	 * @param tapahtuma		pelialueella ilmennyt hiiritapahtuma
	 */
	public void mousePressed(MouseEvent tapahtuma) {}
	
	/**
	 * Ei tee mitään. (MouseListener-rajapinnan vaatima metodi)
	 * 
	 * @param tapahtuma		pelialueella ilmennyt hiiritapahtuma
	 */
	public void mouseEntered(MouseEvent tapahtuma) {}
	
	/**
	 * Ei tee mitään. (MouseListener-rajapinnan vaatima metodi)
	 * 
	 * @param tapahtuma		pelialueella ilmennyt hiiritapahtuma
	 */
	public void mouseExited(MouseEvent tapahtuma) {}
	
	/**
	 * Ei tee mitään. (MouseListener-rajapinnan vaatima metodi)
	 * 
	 * @param tapahtuma		pelialueella ilmennyt hiiritapahtuma
	 */
	public void mouseReleased(MouseEvent tapahtuma) {}
}
