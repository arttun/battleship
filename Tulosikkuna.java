/**
 * Ikkuna, joka näyttää tuloslistan. 
 * 
 * @author Arttu Nieminen
 * @version 1.0
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Tulosikkuna extends JFrame implements ActionListener {

	/**
	 * Ikkunan "Sulje"-painike.
	 */
	private JButton nappi;
	
	/**
	 * Luo tuloslistan näyttävän ikkunan.
	 * 
	 * @exception FileNotFoundException		jos tuloslista-tiedostoa ei löydy
	 */
	public Tulosikkuna() {		
		super();
		this.setTitle("Parhaat tulokset");
		
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
		
		File tiedosto = new File("tuloslista/tuloslista.txt");	
		Scanner luku;
		
		String lista = "Sija\tYrityksiä\tNimi\n";
		
		try {
			luku = new Scanner(tiedosto);
		} catch (FileNotFoundException e) {
			Ilmoitusikkuna virhe = new Ilmoitusikkuna("Virhe!", "Tuloslista-tiedostoa ei löydy!");
			return;
		}
		
		// luetaan tulokset tiedostosta
		for(int i=0; i<Tuloslista.LISTANPITUUS; ++i) {
			lista = lista+"\n"+(i+1)+"\t"+luku.nextLine()+"\t"+luku.nextLine();
		}
		
		JTextArea tekstialue = new JTextArea(lista);
		tekstialue.setMargin(new Insets(5, 5, 5, 5));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(tekstialue, gbc);
		this.add(tekstialue);
		
		this.nappi = new JButton("Sulje");
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
	 * Käsittelee ikkunan tapahtumat eli sulkemisnappia painettaessa sulkee ikkunan.
	 * 
	 * @param tapahtuma		ikkunassa ilmennyt tapahtuma
	 */	
	public void actionPerformed(ActionEvent tapahtuma) {
		if(tapahtuma.getSource() == this.nappi) {			
			this.dispose();
		}
	}	
}
