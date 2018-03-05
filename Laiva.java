/**
 * Kuvaa pelissä käytettävää laivaa. 
 * Pitää kirjaa laivan pituudesta ja sijainnista pelialueen ruuduissa.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

public class Laiva {

	/**
	 * Laivan pituus, oltava positiivinen.
	 */
	private int pituus;
	
	/**
	 * Laivan sijaintiruudut taulukossa.
	 */
	private Ruutu[] laivanRuudut;
	
	/**
	 * Luo uuden laivan, mutta ei vielä sijoita laivaa minnekään. Käyttäjä huolehtii siitä, ettei laivan pituus ole liian suuri.
	 * 
	 * @param pituus	laivan pituus, oltava positiivinen
	 */
	public Laiva(int pituus) {
		if (pituus > 0)
			this.pituus = pituus;
		else
			this.pituus = 1;
		this.laivanRuudut = null;
	}
	
	/**
	 * Hakee laivan pituuden.
	 * 
	 * @return	laivan pituus
	 */
	public int haePituus() {
		return this.pituus;
	}
	
	/**
	 * Asettaa laivan sijainnin. Metodia käytettäessä on huolehdittava siitä, että sijaintiruudut ovat kelvolliset.
	 * 
	 * @param ruudut	taulukko, joka sisältää laivan sijaintiruudut; 
	 * 					taulukon pituuden on oltava sama kuin laivan
	 */
	public void asetaLaivanRuudut(Ruutu[] ruudut) {
		if(ruudut.length == this.pituus)
			this.laivanRuudut = ruudut;
	}
	
	/**
	 * Kertoo, onko laiva uponnut eli onko kaikkiin sen sijaintiruutuihin ammuttu.
	 * 
	 * @return true, jos laiva on uponnut; false, jos laiva ei ole uponnut
	 */
	public boolean upposiko() {
		if(this.laivanRuudut == null)		// !!! LISÄTTY TESTATESSA
			return false;
		for(int i=0; i<this.pituus; ++i) {
			if(this.laivanRuudut[i].haeTila() == Ruutu.LAIVA)	// löytyi ruutu, jota ei ole ammuttu
				return false;
		}
		return true;		// tämä palautuslause suoritetaan, jos kaikki ruudut oli ammuttu
	}
	
	/**
	 * Upottaa laivan eli asettaa kaikkien laivalle kuuluvien ruutujen tilaksi uponnut 
	 * ja samalla vaihtaa ruutujen grafiikan oikeaksi.
	 */
	public void upota() {
		for(int i=0; i<this.pituus; ++i)
			this.laivanRuudut[i].asetaTila(Ruutu.UPONNUT, true);
	}
}