/**
 * Pieni ilmoitusikkuna erilaisia viestejä varten; ilmoittaa joko pelin voitosta tai jostakin muusta asiasta.
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

public class Ilmoitusikkuna extends JFrame implements ActionListener {

	/**
	 * Ilmoitusikkunan painike, joka sulkee ikkunan 
	 * ja mikäli kyseessä on pelin voitosta kertova ikkuna,
	 * aloittaa pelituloksen tarkistamisen mahdollista tuloslistalle merkitsemistä varten.
	 */
	private JButton nappi;
	
	/**
	 * Voitosta kertovalla ikkunalla viite Tuloslista-olioon, joka ohjaa pelituloksen käsittelyä;
	 * muulla ikkunalla null.
	 */
	private Tuloslista lista;
	
	/**
	 * Luo yleiskäyttöisen ilmoitusikkunan, jossa on haluttu otsikko, viesti ja sulkemispainike.
	 * 
	 * @param otsikko			ikkunan otsikko
	 * @param viesti			ikkunan viesti
	 */
	public Ilmoitusikkuna(String otsikko, String viesti) {		
		super();
		this.setTitle(otsikko);
		
		this.lista = null;
		
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
		
		JLabel teksti = new JLabel(viesti);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(teksti, gbc);
		this.add(teksti);
		
		this.nappi = new JButton("OK");
		this.nappi.addActionListener(this);		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(this.nappi, gbc);
		this.add(this.nappi);
		
		this.pack();
	    this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Luo pelin voitosta kertovan ilmoitusikkunan.
	 * 
	 * @param lista		Tuloslista-olio, joka ohjaa pelituloksen käsittelyä (ja loi voittoikkunan)
	 */
	public Ilmoitusikkuna(Tuloslista lista) {		
		super();
		this.setTitle("Voitit!");
		
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
		
		JLabel teksti = new JLabel("Onneksi olkoon, voitit pelin!");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(teksti, gbc);
		this.add(teksti);
		
		this.nappi = new JButton("OK");
		this.nappi.addActionListener(this);		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(this.nappi, gbc);
		this.add(this.nappi);
		
		this.pack();
	    this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Käsittelee ikkunassa ilmenneet tapahtumat 
	 * eli nappia painettaessa sulkee ikkunan ja 
	 * mikäli kyseessä on voittoikkuna, aloittaa pelituloksen tarkastamisen.
	 * 
	 * @param tapahtuma		ikkunassa ilmennyt tapahtuma
	 */

	public void actionPerformed(ActionEvent tapahtuma) {
		if(tapahtuma.getSource() == this.nappi) {
			if(this.lista != null) {
				this.lista.tarkistaTulos();
			}
			this.dispose();
		}
	}	
}