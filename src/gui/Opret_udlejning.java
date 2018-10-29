package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import container.Container;
import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

//klassen extender Stage klassen da det er et undervindue
public class Opret_udlejning extends Stage {
	Controller controller = Controller.getInstance();
    Container container = Container.getInstance();
    private DatePicker dpFraDato, dpTilDato;
    private TextField txfTidspunkt;
    private FadølsAnlægsUdlejning_ordre ordre = null;
    private ListView<Produktgruppe> lvwProduktgrupper;
    private ListView<Produkt> lvwProdukter;
    private ListView<String> lvwOrdre;
    private ToggleGroup betalingsmiddelGroup = new ToggleGroup();
    private RadioButton rbKontant, rbDankort, rbMobilepay, rbRegning, rbKlippekort;
    private Spinner<Integer> spinner;
    private Label lblSamletPris;
    private ArrayList<String> ordrelinjer = new ArrayList<>();
    private ArrayList<Produkt> fustager = new ArrayList<>();
    private ArrayList<Produkt> kulsyrer = new ArrayList<>();
    private ArrayList<Produkt> anlæg = new ArrayList<>();
    private Prisliste prisliste = null;
    private ArrayList<Integer> antal = new ArrayList<>();
    private ArrayList<Produktpris> produktpriser = new ArrayList<>();
    private ArrayList<Integer> fustageAntal = new ArrayList<>();
    private ArrayList<Integer> kulsyreAntal = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private Produktgruppe produktgruppe;
    private Produkt produkt;
    private double samletPris;
    private int pant;

    public Opret_udlejning(String title) {
        initStyle(StageStyle.UTILITY);
        setMinHeight(100);
        setMinWidth(200);
        setResizable(false);
        setTitle(title);
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane);
        // Scene scene = new Scene(pane, 700, 300, Color.WHITE);
        setScene(scene);
    }
    
    private void initContent(GridPane pane) {
    	pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);
         
    	Label lblFraDato = new Label("Fra dato:");
    	pane.add(lblFraDato, 0, 0);	
    	
    	dpFraDato = new DatePicker();
    	pane.add(dpFraDato, 0, 1);
    	dpFraDato.setEditable(false);
    	
    	Label lblTilDato = new Label("Til dato:");
    	pane.add(lblTilDato, 0, 2);	
    	
    	dpTilDato = new DatePicker();
    	dpFraDato.setOnMouseClicked(event -> dpTilDato.setDisable(false));
    	pane.add(dpTilDato, 0, 3);
    	dpTilDato.setEditable(false);
    	dpTilDato.setDisable(true);
    	
    	 dpTilDato.setDayCellFactory(picker -> new DateCell() {
             @Override
             public void updateItem(LocalDate date, boolean empty) {
                 super.updateItem(date, empty);
                 LocalDate fraDato = dpFraDato.getValue();

                 setDisable(empty || date.compareTo(fraDato) < 0);
             }
         });
    	
    	Label lblReturneringstidspunkt = new Label("Returneringstidspunkt:");
    	pane.add(lblReturneringstidspunkt, 0, 4);
    	
    	txfTidspunkt = new TextField();
    	pane.add(txfTidspunkt, 0, 5);
    	
    	Label lblProduktgrupper = new Label("Produktgrupper:");
    	pane.add(lblProduktgrupper, 2, 0);	
    	
    	lvwProduktgrupper = new ListView<>();
    	lvwProduktgrupper.setPrefHeight(300);
    	pane.add(lvwProduktgrupper, 2, 1, 1, 7);
    	lvwAddProduktgrupper();
    	
    	ChangeListener<Produktgruppe> listener = (ov, oldString, newString) -> selectionChangedProduktgrupper();
        lvwProduktgrupper.getSelectionModel().selectedItemProperty().addListener(listener);
    	
    	Label lblProdukter = new Label("Produkter:");
    	pane.add(lblProdukter, 3, 0);
    	
    	lvwProdukter = new ListView<>();
    	lvwProdukter.setPrefHeight(300);
    	lvwProdukter.setMaxWidth(250);
    	pane.add(lvwProdukter, 3, 1, 4, 7);
    	
    	Label lblAntal = new Label("Antal:");
    	pane.add(lblAntal, 3, 8);
    	
    	
    	spinner = new Spinner<>();
    	spinner.setMaxWidth(80);
    	pane.add(spinner, 4, 8);
    	
    	//Antal af produkt der skal tilføjes til ordren skal mindst være en.
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999);
    	spinner.setValueFactory(valueFactory);
    	
    	Button btnTilføjProdukt = new Button("tilføj til ordre");
    	btnTilføjProdukt.setOnAction(event -> btnTilføjProduktAction());
    	pane.add(btnTilføjProdukt, 5, 8);
    	
    	Label lblOrdre = new Label("Ordre:");
    	pane.add(lblOrdre, 7 , 0);
   
    	lvwOrdre = new ListView<>();
    	lvwOrdre.setPrefHeight(300);
    	lvwOrdre.setPrefWidth(400);
    	pane.add(lvwOrdre, 7, 1, 2, 7);
           
        Button btnFjernProdukt = new Button("fjern fra ordre");
        btnFjernProdukt.setOnAction(event -> btnFjernProduktAction());
        pane.add(btnFjernProdukt, 7, 8);
        
        lblSamletPris = new Label("Samlet pris: " + 0.00 + " kr.");
        pane.add(lblSamletPris, 8, 8);
        
        Label lblBetalingsmiddel = new Label("Betalingsmiddel");
        pane.add(lblBetalingsmiddel, 0, 8);
        
        // Radiobuttons for betalingsmiddel
        HBox betalingsmiddelBox = new HBox();
        betalingsmiddelBox.setSpacing(20);
        betalingsmiddelGroup = new ToggleGroup();
        rbKontant = new RadioButton("KONTANT");
        betalingsmiddelBox.getChildren().add(rbKontant);
        rbKontant.setToggleGroup(betalingsmiddelGroup);
 
        rbDankort = new RadioButton("DANKORT");
        betalingsmiddelBox.getChildren().add(rbDankort);
        rbDankort.setToggleGroup(betalingsmiddelGroup);
       
        rbMobilepay = new RadioButton("MOBILEPAY");
        betalingsmiddelBox.getChildren().add(rbMobilepay);
        rbMobilepay.setToggleGroup(betalingsmiddelGroup);

        rbRegning = new RadioButton("REGNING");
        betalingsmiddelBox.getChildren().add(rbRegning);
        rbRegning.setToggleGroup(betalingsmiddelGroup);

        rbKlippekort = new RadioButton("KLIPPEKORT");
        betalingsmiddelBox.getChildren().add(rbKlippekort);
        rbKlippekort.setToggleGroup(betalingsmiddelGroup);
        
        pane.add(betalingsmiddelBox, 0, 9, 8, 1);
        betalingsmiddelGroup.selectedToggleProperty().addListener(event -> rbBetalingsMiddelAction());
        
        Button btnOpretUdlejning = new Button("Opret udlejning");
        btnOpretUdlejning.setOnAction(event -> btnOpretUdlejningAction());
        pane.add(btnOpretUdlejning, 8, 9);  
    }
    
    // Tilføjer de 3 produktgrupper der kan indegå i en fadølsudlejning til listview
	private void lvwAddProduktgrupper() {
    	ArrayList<Produktgruppe> udlejningsproduktgrupper = new ArrayList<>();
    	lvwProduktgrupper.getItems().removeAll(lvwProduktgrupper.getItems());
    	for(Produktgruppe p : controller.getProduktgrupper()) {
    		if (p.getNavn().equals("Fustage") || 
    				(p.getNavn().equals("Kulsyre") || 
    						(p.getNavn().equals("Anlæg")))) {
    			udlejningsproduktgrupper.add(p);
    		}
    	}
    	lvwProduktgrupper.getItems().setAll(udlejningsproduktgrupper);
    }
    
    
    //Fjerner ordrelinje fra ordren.
	private void btnFjernProduktAction() {
		int index = lvwOrdre.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
        	if (produktgruppe.getNavn().equals("Fustage")) {
    			fustager.remove(index);
    			fustageAntal.remove(index);
    		}
    		else if	(produktgruppe.getNavn().equals("Kulsyre")) {
        			kulsyrer.remove(index);
        			kulsyreAntal.remove(index);
    		}
    		else if (produktgruppe.getNavn().equals("Anlæg")) {
    			anlæg.remove(index);
    			anlæg.remove(index);
    		}
        	samletPris = samletPris - (produktpriser.get(index).getPris() * antal.get(index));
        	opdaterSamletPris();
			lvwOrdre.getItems().remove(index);
            ordrelinjer.remove(index);
            antal.remove(index);
            produktpriser.remove(index);
            produkter.remove(index);
        }		
	}
	//Tilføjer odrelinje til ordren.
	private void btnTilføjProduktAction() {
		String navn = "";
		Produktpris produktpris = null;
		produkt = lvwProdukter.getSelectionModel().getSelectedItem();
		String ordrelinje = "";
		int antal = spinner.getValue();
		double pris = 0;
		
    	if (produkt != null) {
    		navn = produkt.getNavn();
    		for (Prisliste p : controller.getPrislister()) {
        		if (p.getNavn().equals("Butik")) {
        			prisliste = p;
        		}
        	}
        	for (Produktpris pr : produkt.getProduktpriser()) {
        		if (pr.getPrisliste().equals(prisliste)) {
        			
        			produktpris = pr;
        			pris = pr.getPris() * antal;
        		}
        	}    		
    		if (produkter.contains(produkt) == false) {   		
    		
    		
    		if (produktgruppe.getNavn().equals("Fustage")) {
    			fustager.add(produkt);
    			fustageAntal.add(antal);
    		}
    		else if	(produktgruppe.getNavn().equals("Kulsyre")) {
        			kulsyrer.add(produkt);
        			kulsyreAntal.add(antal);
    		}
    		else if (produktgruppe.getNavn().equals("Anlæg")) {
    			anlæg.add(produkt);
    		}
    		
        	
        	ordrelinje = "antal: " + antal + " \t navn: " + navn + " \t pris: " + pris;
        	ordrelinjer.add(ordrelinje);
        	this.antal.add(antal);
        	produktpriser.add(produktpris);
        	produkter.add(produkt);
        	lvwOrdre.getItems().setAll(ordrelinjer);
        	spinner.getValueFactory().setValue(0);
        	samletPris += pris;
    		
    	} else { ordrelinje = "antal: " + antal + " \t navn: " + navn + " \t pris: " + pris;
    		for (int i = 0; i < ordrelinjer.size(); i++) {
    		if (ordrelinjer.get(i).contains(navn)) {
    			antal = this.antal.get(i) + spinner.getValue();
    			pris = produktpris.getPris() * antal;  			
    			this.antal.set(i, antal);
    			ordrelinjer.set(i, "antal: " + antal + " \t navn: " + navn + " \t pris: " + pris);
    			lvwOrdre.getItems().setAll(ordrelinjer);
    			spinner.getValueFactory().setValue(0);
    			samletPris = pris;
    		}
    	}
    		
    	
    	}
    		selectionChangedProduktgrupper();
    	}  else { Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Opret_udlejning");
        alert.setHeaderText("");
        alert.setContentText(
                "Der skal vælges et produkt i produktlisten");
        alert.show();
    	}
    	
    	opdaterSamletPris();
    	
	}
	
	//Opdaterer listviewet produkter.
	private void lvwProdukterUpdate() {
        lvwProdukter.getItems().removeAll(lvwProdukter.getItems());

        Produktgruppe produktgruppe = lvwProduktgrupper.getSelectionModel().getSelectedItem();
        if (produktgruppe != null) {
            lvwProdukter.getItems().setAll(produktgruppe.getProdukter());
        }
    }

	//Opdaterer listviewet produkter, når en produktgruppe vælges.
	private void selectionChangedProduktgrupper() {
		lvwProdukterUpdate();
		produktgruppe = lvwProduktgrupper.getSelectionModel().getSelectedItem();
		if (produktgruppe != null) {
			lvwProdukter.getItems().setAll(produktgruppe.getProdukter());
		}
	}
	
	//Returnere det betalingsmiddel der er valgt.
	private String rbBetalingsMiddelAction() {
        String betalingsmiddel = "";

        if (betalingsmiddelGroup.getSelectedToggle() != null) {
            betalingsmiddel = ((RadioButton) betalingsmiddelGroup.getSelectedToggle()).getText();
        } else {
            betalingsmiddel = null;
        }
        return betalingsmiddel;
    }
	//Tjekker om returneringstidspunkt har korrek syntaks.
	public boolean timeIsValid(String s) {
        try {
            LocalTime.parse(s);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
	}
	
	private void opdaterSamletPris() {
		lblSamletPris.setText("Samlet pris: " + samletPris + " kr.");
	}
	//Opretter et salg af udlejning
	private Ordre btnOpretUdlejningAction() {
		if (dpFraDato.getValue() != null && dpTilDato.getValue() != null && txfTidspunkt.getText().length() > 0 && rbBetalingsMiddelAction() != null) {

            // hvis der er indtastet et gyldigt tidspunkt
            if (timeIsValid(txfTidspunkt.getText()) == true) {
            	if (fustager.size() != 0 && kulsyrer.size() != 0 && anlæg.size() != 0) {  
		             LocalDate fraDato = dpFraDato.getValue();
		             LocalDate tilDato = dpTilDato.getValue();
		             LocalTime tidspunkt = LocalTime.parse(txfTidspunkt.getText());
		          
		             String betaling = rbBetalingsMiddelAction();
		             Betalingsmiddel betalingsmiddel = Betalingsmiddel.valueOf(betaling);
		               
		             Prisliste prisliste = null;
		             for (Prisliste pl : controller.getPrislister()) {
		                 if (pl.getNavn().equals("Butik")) {
		                     prisliste = pl;
		                 }
		             }
		             ordre = (FadølsAnlægsUdlejning_ordre) controller.createFadølsAnlægsUdlejning_ordre(betalingsmiddel, prisliste, fraDato, tilDato, tidspunkt, fustager, kulsyrer, anlæg);
		             for(int i = 0; i<antal.size(); i++) {
		            	 controller.createOrdrelinje(antal.get(i), produktpriser.get(i), ordre);
		             }
		             
		           
		       } else {
		        	 Alert alert = new Alert(AlertType.INFORMATION);
			            alert.setTitle("Opret_udlejning");
			            alert.setHeaderText("");
			            alert.setContentText(
			                    "Der skal vælges mindst 1 fustage, kulsyre og anlæg");
			            alert.show();
		       }
            } else {
		           Alert alert = new Alert(AlertType.INFORMATION);
		           alert.setTitle("Opret_udlejning");
		           alert.setHeaderText("");
		           alert.setContentText("Der skal angives et gyldigt tidspunkt i formatet HH:MM");
		           alert.show();  
		       }
			} else {
		            Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setTitle("Opret_udlejning");
		            alert.setHeaderText("");
		            alert.setContentText(
		                    "Der skal vælges en dato, der skal angives et tidspunkt og betalingsmiddel skal vælges");
		            alert.show();
		    }
        
	

		if (ordre != null)
		{
			 hide();
	         Alert alert = new Alert(AlertType.INFORMATION);
	         alert.setTitle("Opret_udlejning");
	         alert.setHeaderText("");
	         alert.setContentText("Udlejning er oprettet \nPant til betaling nu: " + ordre.getPant() + " kr. \nDen samlede pris er: " + controller.beregnPris(ordre) + " kr. \nTil betaling ved returnering: " + (controller.beregnPris(ordre) - pant) + " kr.");
	         
	         alert.show();
			 }
			
		
		
		
		return ordre;
	}
}	
