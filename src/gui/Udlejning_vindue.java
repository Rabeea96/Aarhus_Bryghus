package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import container.Container;
import controller.Controller;
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
public class Udlejning_vindue extends Stage {

    public Udlejning_vindue(String title) {
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
    
    Controller controller = Controller.getInstance();
    Container container = Container.getInstance();
    private DatePicker dpFraDato, dpTilDato;
    private TextField txfTidspunkt;
    private ListView<Produktgruppe> lvwProduktgrupper;
    private ListView<Produkt> lvwProdukter;
    private ListView<String> lvwOrdrer;
    private ToggleGroup betalingsmiddelGroup = new ToggleGroup();
    private RadioButton rbKontant, rbDankort, rbMobilepay, rbRegning, rbKlippekort;
    private Spinner<Integer> spinner;
    private ArrayList<String> ordrelinjer = new ArrayList<>();
    private ArrayList<Produkt> fustager = new ArrayList<>();
    private ArrayList<Produkt> kulsyrer = new ArrayList<>();
    private ArrayList<Produkt> anlæg = new ArrayList<>();
    private Prisliste prisliste = null;
    private Ordre ordre = null;
    private ArrayList<Integer> antal = new ArrayList<>();
    private ArrayList<Produktpris> produktpriser = new ArrayList<>();
    private ArrayList<Integer> fustageAntal = new ArrayList<>();
    private ArrayList<Integer> kulsyreAntal = new ArrayList<>();
    private Produktgruppe produktgruppe;
    private Produkt produkt;
    private int pant;
    
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
    	lvwProduktgrupper.setOnMouseClicked(event -> lvwProduktgrupperOnClick());
    	lvwProduktgrupper.setPrefHeight(300);
    	pane.add(lvwProduktgrupper, 2, 1, 1, 7);
    	lvwAddProduktgrupper();
    	
    	Label lblProdukter = new Label("Produkter:");
    	pane.add(lblProdukter, 3, 0);
    	
    	lvwProdukter = new ListView<>();
    	lvwProdukter.setPrefHeight(300);
    	pane.add(lvwProdukter, 3, 1, 3, 7);
    	
    	Label lblAntal = new Label("Antal:");
    	pane.add(lblAntal, 3, 8);
    	
    	
    	spinner = new Spinner<>();
    	spinner.setMaxWidth(80);
    	spinner.setEditable(true);
    	pane.add(spinner, 4, 8);
    	
    	//Antal af produkt der skal tilføjes til ordren skal mindst være en.
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999);
    	spinner.setValueFactory(valueFactory);
    	
    	Button btnTilføjProdukt = new Button("tilføj");
    	btnTilføjProdukt.setOnAction(event -> btnTilføjProduktAction());
    	pane.add(btnTilføjProdukt, 5, 8);
    	
    	Label lblOrdre = new Label("Ordre:");
    	pane.add(lblOrdre, 6 , 0);
   
    	lvwOrdrer = new ListView<>();
    	lvwOrdrer.setPrefHeight(300);
    	lvwOrdrer.setPrefWidth(375);
    	pane.add(lvwOrdrer, 6, 1, 1, 7);
           
        Button btnFjernProdukt = new Button("fjern");
        btnFjernProdukt.setOnAction(event -> btnFjernProduktAction());
        pane.add(btnFjernProdukt, 6, 8);
        
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
        pane.add(btnOpretUdlejning, 6, 9);  
    }
    
    // Tilføjer de 3 produktgrupper der kan indegå i en fadølsudlejning til listview
	private void lvwAddProduktgrupper() {
    	ArrayList<Produktgruppe> udlejningsproduktgrupper = new ArrayList<>();
    	lvwProduktgrupper.getItems().removeAll(lvwProduktgrupper.getItems());
    	for(Produktgruppe p : controller.getProduktgrupper())
    	{
    		if (p.getNavn().equals("Fustage") || 
    				(p.getNavn().equals("Kulsyre") || 
    						(p.getNavn().equals("Anlæg"))))
    		{
    			udlejningsproduktgrupper.add(p);
    		}
    	}
    	lvwProduktgrupper.getItems().setAll(udlejningsproduktgrupper);
    }
    
    
    //Fjerner ordrelinje fra ordren.
	private void btnFjernProduktAction() {
		int index = lvwOrdrer.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
        	if (produktgruppe.getNavn().equals("Fustage")) {
    			fustager.remove(produkt);
    		}
    		else if	(produktgruppe.getNavn().equals("Kulsyre")) {
        			kulsyrer.remove(produkt);
    		}
    		else if (produktgruppe.getNavn().equals("Anlæg")) {
    			anlæg.remove(produkt);
    		}
            lvwOrdrer.getItems().remove(index);
            ordrelinjer.remove(index);
            antal.remove(index);
            produktpriser.remove(index);
        }		
	}
	//Tilføjer odrelinje til ordren.
	private void btnTilføjProduktAction() {
		produkt = lvwProdukter.getSelectionModel().getSelectedItem();
		String ordrelinje = "";
		
    	
    	if (produkt != null)
    	{
    		String navn = produkt.getNavn();
    		int antal = spinner.getValue();
    		Produktpris produktpris = null;
    		double pris = 0;
    		
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
    		
        	for (Prisliste p : controller.getPrislister())
        	{
        		if (p.getNavn().equals("Butik"))
        		{
        			prisliste = p;
        		}
        	}
        	for (Produktpris pr : produkt.getProduktpriser())
        	{
        		if (pr.getPrisliste().equals(prisliste))
        		{
        			
        			produktpris = pr;
        			pris = pr.getPris() * antal;
        		}
        	}
        	ordrelinje = "antal: " + antal + " \t navn: " + navn + "\t pris: " + pris;
        	ordrelinjer.add(ordrelinje);
        	this.antal.add(antal);
        	produktpriser.add(produktpris);
        	lvwOrdrer.getItems().setAll(ordrelinjer);
    	}
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
	private void lvwProduktgrupperOnClick() {
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
	
	//Opretter et salg af udlejning
	private Ordre btnOpretUdlejningAction() {
		if (dpFraDato.getValue() != null && dpTilDato.getValue() != null && txfTidspunkt.getText().length() > 0 && rbBetalingsMiddelAction() != null) {

            // hvis der er indtastet et gyldigt tidspunkt
            if (timeIsValid(txfTidspunkt.getText()) == true) {
            	
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
                 ordre = controller.createFadølsAnlægsUdlejning_ordre(betalingsmiddel, prisliste, fraDato, tilDato, tidspunkt, fustager, kulsyrer, anlæg);
                 for(int i = 0; i<antal.size(); i++)
                 {
                	 controller.createOrdrelinje(antal.get(i), produktpriser.get(i), ordre);
                 }
                 
               
           } else {
               Alert alert = new Alert(AlertType.INFORMATION);
               alert.setTitle("Udlejning_vindue");
               alert.setHeaderText("");
               alert.setContentText("Der skal angives et gyldigt tidspunkt i formatet HH:MM");
               alert.show();  
           }
        	
		} 
            else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Udlejning_vindue");
                alert.setHeaderText("");
                alert.setContentText(
                        "Der skal vælges en dato, der skal angives et tidspunkt og betalingsmiddel skal vælges");
                alert.show();
        }
		if (ordre != null)
		{
			 if (fustager.isEmpty() == false) {
				 
				 for (int i = 0; i < fustager.size(); i++)
				 {
					 pant += (fustageAntal.get(i) * 200);
					 System.out.println(pant);
				 }
			 }
			 if (kulsyrer.isEmpty() == false) {
				 for (int i = 0; i < kulsyrer.size(); i++)
				 {
					 pant += (kulsyreAntal.get(i) * 1000);
					 System.out.println(pant);
				 }
			 }
			 hide();
	         Alert alert = new Alert(AlertType.INFORMATION);
	         alert.setTitle("Udlejnings_vindue");
	         alert.setHeaderText("");
	         alert.setContentText("Udlejning er oprettet \nPant til betaling nu: " + pant + " kr. \nDen samlede pris er: " + controller.beregnPris(ordre) + " kr. \nTil betaling ved returnering: " + (controller.beregnPris(ordre) - pant) + " kr.");
	         
	         alert.show();
		}
		
		
		return ordre;
	}
}
