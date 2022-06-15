/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Business b = cmbLocale.getValue();
    	if(b == null) {
    		txtResult.setText("Seleziona un locale!");
    		return;
    	}
    	
    	double x;
    	try {
			x = Double.parseDouble(txtX.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			txtResult.setText("Inserisci un numero valido tra 0 e 1!");
			return;
		}
    	if(x<0 || x>1) {
    		txtResult.setText("Inserisci un numero tra 0 e 1!");
			return;
    	}
    	
    	List<Business> path = this.model.calcolaPercorso(b, x);
    	txtResult.setText("Percorso migliore:\n");
    	for(Business bb: path) {
    		txtResult.appendText(bb+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String c = cmbCitta.getValue();
    	if(c == null) {
    		txtResult.setText("Seleziona una città!");
    		return;
    	}
    		
    	Integer a = cmbAnno.getValue();
    	if(a == null) {
    		txtResult.setText("Seleziona un anno!");
    		return;
    	}
    		
    	String s = this.model.creaGrafo(c, a);
    	txtResult.setText(s);
    	
    	List<Business> businesses = this.model.getBusinesses();
    	cmbLocale.getItems().clear();
    	cmbLocale.getItems().addAll(businesses);
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	String c = cmbCitta.getValue();
    	if(c == null) {
    		txtResult.setText("Seleziona una città!");
    		return;
    	}
    		
    	Integer a = cmbAnno.getValue();
    	if(a == null) {
    		txtResult.setText("Seleziona un anno!");
    		return;
    	}
    	
    	if(this.model.getGraph() == null) {
    		String s = this.model.creaGrafo(c, a);
        	txtResult.setText(s);
    	}
    	
    	String ss = this.model.localeMigliore();
    	txtResult.setText(ss);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbAnno.getItems().add(2005);
    	this.cmbAnno.getItems().add(2006);
    	this.cmbAnno.getItems().add(2007);
    	this.cmbAnno.getItems().add(2008);
    	this.cmbAnno.getItems().add(2009);
    	this.cmbAnno.getItems().add(2010);
    	this.cmbAnno.getItems().add(2011);
    	this.cmbAnno.getItems().add(2012);
    	this.cmbAnno.getItems().add(2013);
    	
    	List<String> cities = this.model.getAllCities();
    	cmbCitta.getItems().addAll(cities);
    }
}
