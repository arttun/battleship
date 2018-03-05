/**
 * Kuvaa ihmispelaajan vastustajaa tietokonetta ja toteuttaa tietokoneen pelaamiseen vaadittavan teko‰lyn.
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.util.Random;

public class Tietokone {
	
	/**
	 * Pelikerta, johon tietokone osallistuu.
	 */
	private Peli peli;
	
	/**
	 * Tietokoneen oma pelialue.
	 */
	private Pelialue tietokoneenPelialue;
	
	/**
	 * Ihmispelaajan pelialue.
	 */
	private Pelialue ihmisenPelialue;
	
	/**
	 * Satunnaisluku/totuusarvogeneraattori.
	 */
	private Random generaattori;
	
	// tietokoneen muisti teko‰ly‰ varten:
	
	/**
	 * Kertoo, onko jonkin laivan upottaminen kesken: true, jos kyll‰; false, jos ei.
	 */
	private boolean upotusKesken;

	/**
	 * Ruutu, jossa saatiin osuma ja aloitettiin tietyn laivan upottaminen.
	 */
	private Ruutu aloitusRuutu;
	
	/**
	 * Suunta, johon l‰hdettiin etenem‰‰n ensimm‰isen osuman j‰lkeen tietty‰ laivaa upotettaessa.
	 */
	private int aloitusSuunta;
	
	/**
	 * Edellisen kierroksen pommitusten etenemissuunta.
	 */
	private int edellinenSuunta;
	
	/**
	 * Edellisell‰ kierroksella ammuttu ruutu.
	 */
	private Ruutu edellinenRuutu;
	
	/**
	 * Kertoo, osuttiinko edellisell‰ kierroksella: true, jos kyll‰; false, jos ei.
	 */
	private boolean edellinenOsui;

	/**
	 * Luo alkutilaisen tietokone-olion.
	 * 
	 * @param peli		peli(kerta), johon tietokonepelaaja liittyy
	 */
	public Tietokone(Peli peli) {
		this.peli = peli;
		this.tietokoneenPelialue = this.peli.haeTietokoneenPelialue();
		this.ihmisenPelialue = this.peli.haeIhmisenPelialue();
		this.upotusKesken = false;
		this.aloitusRuutu = null;
		this.aloitusSuunta = Ruutu.MAARITTELEMATON;
		this.edellinenSuunta = Ruutu.MAARITTELEMATON;
		this.edellinenRuutu = null;	
		this.edellinenOsui = false;
		this.generaattori = new Random();
	}
	
	/**
	 * Sijoittaa pelikertaan liittyv‰t laivat satunnaisesti tietokoneen pelialueelle yksi kerrallaan.
	 */
	public void asetaLaivat() {
		for(int i=0; i<this.peli.haeLaivojenPituudet().length; ++i) {
			Laiva laiva = new Laiva(this.peli.haeLaivojenPituudet()[i]);
			boolean asetusOnnistui = false;
			while(!asetusOnnistui) {	// while-silmukasta poistutaan vasta, kun laiva on onnistuneesti asetettu
				Ruutu ruutu = arvoRuutu(this.tietokoneenPelialue);
				while(ruutu.haeTila() != Ruutu.TYHJA) {
					ruutu = arvoRuutu(this.tietokoneenPelialue);
				}
				int suunta = arvoLaivanSuunta();
				asetusOnnistui = this.tietokoneenPelialue.asetaLaiva(laiva, ruutu, suunta, false);
				// (yll‰) testatessa false:n vaihtaminen true:ksi asettaa tietokoneen laivat n‰kyviin
			}
		}
	}
	
	/**
	 * Ampuu teko‰lyn p‰‰tt‰m‰‰ ampumatonta ruutua ja p‰ivitt‰‰ tietokoneen muistin ampumistuloksen perusteella.
	 * 
	 * @return	ampumistulos eli arvo, joka kuvaa ammutun ruudun uutta ampumisen j‰lkeist‰ tilaa
	 */	
	public int pommita() {
		Ruutu kohde = laskeSeuraavaKohde();
		int tulos = kohde.ammu();
		if(tulos == Ruutu.HUTI) {
			this.edellinenOsui = false;
			return Ruutu.HUTI;
		}
		else if(tulos == Ruutu.OSUMA) {
			this.edellinenOsui = true;
			this.edellinenRuutu = kohde;
			if(!this.upotusKesken) {	// ei oltu upottamassa tietty‰ laivaa => aloitetaan tietyn laivan upotus
				this.upotusKesken = true;
				this.aloitusRuutu = kohde;
			}
			return Ruutu.OSUMA;
		}
		else if (tulos == Ruutu.UPONNUT) {
			this.upotusKesken = false;
			this.aloitusRuutu = null;
			this.aloitusSuunta = Ruutu.MAARITTELEMATON;
			this.edellinenSuunta = Ruutu.MAARITTELEMATON;
			this.edellinenRuutu = null;
			this.edellinenOsui = false;
			return Ruutu.UPONNUT;
		}
		else {
			return Ruutu.AMMUTTU;	// k‰‰nt‰j‰‰ varten, t‰nne ei pit‰isi ikin‰ p‰‰ty‰, 
									// koska laskeSeuraavaKohde()-metodi palauttaa ampumattoman ruudun
			}
	}	
	
	/**
	 * Arpoo annetulta pelialueelta satunnaisen ruudun.
	 * 
	 * @param alue		pelialue, jolta arvotaan ruutu
	 * @return			satunnainen ruutu annetulta pelialueelta
	 */	
	public Ruutu arvoRuutu(Pelialue alue) {
		int x = arvoX();
		int y = arvoY();
		return alue.haeRuutu(x, y);			
	}
	
	/**
	 * Arpoo satunnaisen x-koordinaatin eli luvun v‰lilt‰ 0 ... pelialueen leveys-1.
	 * 
	 * @return	satunnainen pelialueen x-koordinaatti
	 */
	public int arvoX() {
		return this.generaattori.nextInt(this.peli.haeLeveys());
	}
	
	/**
	 * Palauttaa satunnaisen y-koordinaatin eli luvun v‰lilt‰ 0 ... pelialueen korkeus-1.
	 * 
	 * @return	satunnainen pelialueen y-koordinaatti
	 */
	public int arvoY() {
		return this.generaattori.nextInt(this.peli.haeKorkeus());
	}
	
	/**
	 * Arpoo laivan suunnan (vaaka tai pysty).
	 * 
	 * @return	satunnainen arvo, joka kuvaa laivan suuntaa; joko VAAKA tai PYSTY (m‰‰ritelty luokassa Ruutu)
	 */
	public int arvoLaivanSuunta() {
		boolean totuusarvo = this.generaattori.nextBoolean();
		if(totuusarvo)
			return Ruutu.VAAKA;
		else
			return Ruutu.PYSTY;
	}
	
	/**
	 * Arpoo satunnaisen kulkusuunnan (oikea, vasen, ylos tai alas).
	 * 
	 * @return		arvo, joka kuvaa kulkusuuntaa; OIKEA, ALAS, VASEN tai YLOS (m‰‰ritelty luokassa Ruutu)
	 */
	public int arvoSuunta() {
		int suunta = this.generaattori.nextInt(4);
		if(suunta == 0)
			return Ruutu.OIKEA;
		else if(suunta == 1)
			return Ruutu.ALAS;
		else if(suunta == 2)
			return Ruutu.VASEN;
		else
			return Ruutu.YLOS;
	}
	
	/**
	 * Vaihtaa tietokoneen pommitusten etenemissuuntaa edellisen suunnan ja aloitussuunnan perusteella.
	 * 
	 * @return	arvo, joka kuvaa uutta etenemissuuntaa; OIKEA, ALAS, VASEN tai YLOS (m‰‰ritelty luokassa Ruutu)
	 */	
	public int vaihdaSuunta() {
		if(this.edellinenSuunta == Ruutu.MAARITTELEMATON) {
			this.aloitusSuunta = arvoSuunta();
			this.edellinenSuunta = this.aloitusSuunta;
			return aloitusSuunta;
		}
		else if(this.aloitusSuunta == Ruutu.YLOS) {
			if(this.edellinenSuunta == Ruutu.YLOS) {
				this.edellinenSuunta = Ruutu.ALAS;
				return Ruutu.ALAS;
			}
			else if(this.edellinenSuunta == Ruutu.ALAS) {
				this.edellinenSuunta = Ruutu.OIKEA;
				return Ruutu.OIKEA;
			}
			else if(this.edellinenSuunta == Ruutu.OIKEA) {
				this.edellinenSuunta = Ruutu.VASEN;
				return Ruutu.VASEN;
			}
			else {// this.edellinenSuunta == Ruutu.VASEN
				this.edellinenSuunta = Ruutu.YLOS;
				return Ruutu.YLOS;
			}
		}		
		else if(this.aloitusSuunta == Ruutu.ALAS) {
			if(this.edellinenSuunta == Ruutu.ALAS) {
				this.edellinenSuunta = Ruutu.YLOS;
				return Ruutu.YLOS;
			}
			else if(this.edellinenSuunta == Ruutu.YLOS) {
				this.edellinenSuunta = Ruutu.OIKEA;
				return Ruutu.OIKEA;
			}
			else if(this.edellinenSuunta == Ruutu.OIKEA) {
				this.edellinenSuunta = Ruutu.VASEN;
				return Ruutu.VASEN;
			}
			else { // this.edellinenSuunta == Ruutu.VASEN
				this.edellinenSuunta = Ruutu.ALAS;
				return Ruutu.ALAS;
			}
		}
		else if(this.aloitusSuunta == Ruutu.OIKEA) {
			if(this.edellinenSuunta == Ruutu.OIKEA) {
				this.edellinenSuunta = Ruutu.VASEN;
				return Ruutu.VASEN;
			}
			else if(this.edellinenSuunta == Ruutu.VASEN) {
				this.edellinenSuunta = Ruutu.YLOS;
				return Ruutu.YLOS;
			}
			else if(this.edellinenSuunta == Ruutu.YLOS) {
				this.edellinenSuunta = Ruutu.ALAS;
				return Ruutu.ALAS;
			}
			else {// this.edellinenSuunta == Ruutu.ALAS
				this.edellinenSuunta = Ruutu.OIKEA;
				return Ruutu.OIKEA;
			}
		}	
		else { // this.aloitusSuunta == Ruutu.VASEN
			if(this.edellinenSuunta == Ruutu.VASEN) {
				this.edellinenSuunta = Ruutu.OIKEA;
				return Ruutu.OIKEA;
			}
			else if(this.edellinenSuunta == Ruutu.OIKEA) {
				this.edellinenSuunta = Ruutu.YLOS;
				return Ruutu.YLOS;
			}
			else if(this.edellinenSuunta == Ruutu.YLOS) {
				this.edellinenSuunta = Ruutu.ALAS;
				return Ruutu.ALAS;
			}
			else {// this.edellinenSuunta == Ruutu.ALAS
				this.edellinenSuunta = Ruutu.VASEN;
				return Ruutu.VASEN;
			}
		}	
	}
	
	/**
	 * Tutkii, onko ihmisen pelialueella ruutuja, joissa on osuma, mutta ei uponnutta laivaa.
	 * 
	 * @return		taulukko ruuduista, joissa on osuma;
	 * 				tai null, jos irto-osumia ei lˆytynyt
	 */	
	public Ruutu[] onkoIrtoOsumia() {
		Ruutu[] irtoOsumat = new Ruutu[this.peli.haeLaivojenYhteispituus()];
		int i=0;
		for(int y=0; y<this.ihmisenPelialue.haeKorkeus(); ++y) {
			for(int x=0; x<this.ihmisenPelialue.haeLeveys(); ++x) {
				if(this.ihmisenPelialue.haeRuutu(x, y).haeTila() == Ruutu.OSUMA) {
					irtoOsumat[i] = this.ihmisenPelialue.haeRuutu(x, y);
					++i;
				}
			}
		}
		if(i>0)
			return irtoOsumat;
		else
			return null;
	}
	
	/**
	 * Laskee edellisten pommitusten tulosten perusteella 
	 * j‰rkev‰n seuraavan ampumattoman ruudun pommitettavaksi.
	 * 
	 * @return Ruutu	ruutu, jota ei ole ammuttu ja joka on j‰rkev‰ seuraava pommituskohde
	 */
	public Ruutu laskeSeuraavaKohde() {
		Ruutu seuraava;
		if(this.upotusKesken) {			// ollaan upottamassa tietty‰ laivaa
			if(this.edellinenOsui) {	// edellinen osui
				// jatketaan edellisest‰ ruudusta siihen suuntaan, johon oltiin menossa
				seuraava = this.ihmisenPelialue.siirry(this.edellinenRuutu, this.edellinenSuunta, 1);
				while(true) {			
					if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
						return seuraava;
					// jos jatkaminen edelliseen suuntaan ei ole mahdollista, 
					// l‰hdet‰‰n aloitusruudusta uuteen (m‰‰r‰ttyyn) suuntaan:
					seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, vaihdaSuunta(), 1);
				}
			}
			else {	// ollaan upottamassa tietty‰ laivaa, edellinen oli huti
				while(true) {
					// l‰hdet‰‰n aloitusruudusta uuteen (m‰‰r‰ttyyn) suuntaan
					seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, vaihdaSuunta(), 1);
					if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
						return seuraava;
				}
			}
		}
		else {	// ei oltu upottamassa mit‰‰n tietty‰ laivaa
			Ruutu[] irtoOsumat = onkoIrtoOsumia();
			if(irtoOsumat != null) {	// jos irto-osumia on
					Ruutu[] rivissaOlevat = new Ruutu[this.peli.haeLaivojenYhteispituus()];
					int suunta = Ruutu.VAAKA;		// k‰‰nt‰j‰n vaatima
					int k=0;						// k k‰y l‰pi irtoOsumat-taulukkoa
					// tarkistetaan, onko samalla vaakarivill‰ irtoruutuja
					for(int i=0; irtoOsumat[i] != null; ++i) {	
						for(int j=i+1; irtoOsumat[j] != null; ++j) {
							if(irtoOsumat[j].haeY() == irtoOsumat[i].haeY() && (irtoOsumat[j].haeX()-irtoOsumat[i].haeX()) == (j-i)) {
								if(k==0) {
									rivissaOlevat[0] = irtoOsumat[i];
									++k;
								}
								rivissaOlevat[k] = irtoOsumat[j];
								++k;
							}	
						}
						if(k!=0) {
							suunta = Ruutu.VAAKA;
							break;	// yksi vaakarivi on lˆytynyt, ei tutkita en‰‰ muita mahdollisia rivej‰
						}
					}
					if(k==0) {	// vaakarivej‰ ei ollut
						// tarkistetaan, onko samalla pystyrivill‰ irtoruutuja
						for(int i=0; irtoOsumat[i] != null; ++i) {	
							for(int j=i+1; irtoOsumat[j] != null; ++j) {
								if(irtoOsumat[j].haeX() == irtoOsumat[i].haeX() && (irtoOsumat[j].haeY()-irtoOsumat[i].haeY()) == (j-i)) {
									if(k==0) {
										rivissaOlevat[0] = irtoOsumat[i];
										++k;
									}
									rivissaOlevat[k] = irtoOsumat[j];
									++k;
								}	
							}
							if(k!=0) {
								suunta = Ruutu.PYSTY;
								break;	// yksi pystyrivi on lˆytynyt, ei tutkita en‰‰ muita mahdollisia rivej‰
							}
						}
					}
					if(k==0) {	// riviss‰ olevia ei ollut => valitaan irto-osumista ensimm‰inen ja kohdellaan sit‰ ensimm‰isen‰ osumana
						this.upotusKesken = true;
						this.aloitusRuutu = irtoOsumat[0];
						this.edellinenRuutu = this.aloitusRuutu;
						this.edellinenOsui = true;
						this.aloitusSuunta = arvoSuunta();
						this.edellinenSuunta = this.aloitusSuunta;
						seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, this.aloitusSuunta, 1);
						while(true) {
							if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
								return seuraava;
							seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, vaihdaSuunta(), 1);
						}
					}
					else {	// riviss‰ olevia lˆytyi
						Ruutu mahdollinen;
						if(suunta == Ruutu.VAAKA) {	// vaakarivi
							// tarkistetaan, onko vaakarivin vasemmanpuoleinen ruutu ampumaton
							mahdollinen = this.ihmisenPelialue.siirry(rivissaOlevat[0], Ruutu.VASEN, 1);
							if(mahdollinen.haeTila() == Ruutu.TYHJA || mahdollinen.haeTila() == Ruutu.LAIVA) {
								System.out.println("Jatketaan vaakarivin vasemmanpuoleiseen ruutuun.");
								this.upotusKesken = true;
								this.aloitusRuutu = rivissaOlevat[k-1];
								this.edellinenRuutu = rivissaOlevat[0];
								this.edellinenOsui = true;
								this.aloitusSuunta = Ruutu.VASEN;
								this.edellinenSuunta = this.aloitusSuunta;
								return mahdollinen;
							}
							// tarkistetaan, onko vaakarivin oikeanpuoleinen ruutu ampumaton
							mahdollinen = this.ihmisenPelialue.siirry(rivissaOlevat[k-1], Ruutu.OIKEA, 1);
							if(mahdollinen.haeTila() == Ruutu.TYHJA || mahdollinen.haeTila() == Ruutu.LAIVA) {
								System.out.println("Jatketaan vaakarivin oikeanpuoleiseen ruutuun.");
								this.upotusKesken = true;
								this.aloitusRuutu = rivissaOlevat[0];
								this.edellinenRuutu = rivissaOlevat[k-1];
								this.edellinenOsui = true;
								this.aloitusSuunta = Ruutu.OIKEA;
								this.edellinenSuunta = this.aloitusSuunta;
								return mahdollinen;
							}
							// upotettava laiva ei voi olla lˆydetyll‰ vaakarivill‰ 
							// => l‰hdet‰‰n etenem‰‰n rivin vasemmanpuolimmaisesta osumasta 
							// vapaaseen suuntaan (ylˆs tai alas) (jompikumpi v‰ltt‰m‰tt‰ vapaa)
							this.upotusKesken = true;
							this.aloitusRuutu = rivissaOlevat[0];
							this.edellinenRuutu = rivissaOlevat[0];
							this.edellinenOsui = true;
							this.aloitusSuunta = Ruutu.YLOS;
							this.edellinenSuunta = this.aloitusSuunta;
							seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, this.aloitusSuunta, 1);
							while(true) {
								if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
									return seuraava;
								seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, vaihdaSuunta(), 1);
							}
						}
						else {	// pystyrivi
							// tarkistetaan, onko pystyrivin yl‰puolinen ruutu ampumaton
							mahdollinen = this.ihmisenPelialue.siirry(rivissaOlevat[0], Ruutu.YLOS, 1);
							if(mahdollinen.haeTila() == Ruutu.TYHJA || mahdollinen.haeTila() == Ruutu.LAIVA) {
								System.out.println("Jatketaan pystyrivin yl‰puoliseen ruutuun.");
								this.upotusKesken = true;
								this.aloitusRuutu = rivissaOlevat[k-1];
								this.edellinenRuutu = rivissaOlevat[0];
								this.edellinenOsui = true;
								this.aloitusSuunta = Ruutu.YLOS;
								this.edellinenSuunta = this.aloitusSuunta;
								return mahdollinen;
							}
							// tarkistetaan, onko pystyrivin alapuolinen ruutu ampumaton
							mahdollinen = this.ihmisenPelialue.siirry(rivissaOlevat[k-1], Ruutu.ALAS, 1);
							if(mahdollinen.haeTila() == Ruutu.TYHJA || mahdollinen.haeTila() == Ruutu.LAIVA) {
								System.out.println("Jatketaan pystyrivin alapuoliseen ruutuun.");
								this.upotusKesken = true;
								this.aloitusRuutu = rivissaOlevat[0];
								this.edellinenRuutu = rivissaOlevat[k-1];
								this.edellinenOsui = true;
								this.aloitusSuunta = Ruutu.ALAS;
								this.edellinenSuunta = this.aloitusSuunta;
								return mahdollinen;
							}
							// upotettava laiva ei voi olla lˆydetyll‰ pystyrivill‰ 
							// => l‰hdet‰‰n etenem‰‰n rivin vasemmanpuolimmaisesta osumasta 
							// vapaaseen suuntaan (vasemmalle tai oikealle) (jompikumpi v‰ltt‰m‰tt‰ vapaa)
							this.upotusKesken = true;
							this.aloitusRuutu = rivissaOlevat[0];
							this.edellinenRuutu = rivissaOlevat[0];
							this.edellinenOsui = true;
							this.aloitusSuunta = Ruutu.VASEN;
							this.edellinenSuunta = this.aloitusSuunta;
							seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, this.aloitusSuunta, 1);
							while(true) {
								if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
									return seuraava;
								seuraava = this.ihmisenPelialue.siirry(this.aloitusRuutu, vaihdaSuunta(), 1);
							}
						}
					}
			}	
			else { // irto-osumia ei ole => arvotaan satunnainen ampumaton ruutu
				while(true) {
					seuraava = arvoRuutu(this.ihmisenPelialue);
					if(seuraava.haeTila() == Ruutu.TYHJA || seuraava.haeTila() == Ruutu.LAIVA)
						return seuraava;
				}
			}
		}
	} // laskeSeuraavaKohde() p‰‰ttyy
}