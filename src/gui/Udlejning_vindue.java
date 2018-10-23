package gui;

import java.util.ArrayList;
import container.Container;
import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    ArrayList<Produkt> anlæg = new ArrayList<>();
    private Prisliste prisliste = null;
    
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
    	pane.add(dpTilDato, 0, 3);
    	dpTilDato.setEditable(false);
    	
    	Label lblReturneringstidspunkt = new Label("Returneringstidspunkt:");
    	pane.add(lblReturneringstidspunkt, 0, 4);
    	
    	txfTidspunkt = new TextField();
    	pane.add(txfTidspunkt, 0, 5);
    	
    	Label lblProduktgrupper = new Label("Produktgrupper:");
    	pane.add(lblProduktgrupper, 2, 0);	
    	
    	lvwProduktgrupper = new ListView<>();
    	lvwProduktgrupper.setOnMouseClicked(event -> lvwProduktgrupperOnClick());
    	lvwProduktgrupper.setPrefHeight(300);
    	pane.add(lvwProduktgrupper, 2, 1, 1, 4);
    	
    	Label lblProdukter = new Label("Produkter:");
    	pane.add(lblProdukter, 3, 0);
    	
    	lvwProdukter = new ListView<>();
    	lvwProdukter.setPrefHeight(300);
    	pane.add(lvwProdukter, 3, 1, 3, 4);
    	
    	Label lblAntal = new Label("Antal:");
    	pane.add(lblAntal, 3, 5);
    	
    	spinner = new Spinner<>();
    	spinner.setMaxWidth(80);
    	spinner.setEditable(true);
    	pane.add(spinner, 4, 5);
    	
    	SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
    	spinner.setValueFactory(valueFactory);
    	
    	Button btnTilføjProdukt = new Button("tilføj");
    	btnTilføjProdukt.setOnAction(event -> btnTilføjProduktAction());
    	pane.add(btnTilføjProdukt, 5, 5);
    	
    	Label lblOrdre = new Label("Ordre:");
    	pane.add(lblOrdre, 6 , 0);
   
    	lvwOrdrer = new ListView<>();
    	lvwOrdrer.setPrefHeight(300);
    	lvwOrdrer.setPrefWidth(375);
    	pane.add(lvwOrdrer, 6, 1, 1, 4);
    	
    	
    	
           
        Button btnFjernProdukt = new Button("fjern");
        btnFjernProdukt.setOnAction(event -> btnFjernProduktAction());
        pane.add(btnFjernProdukt, 6, 5);
        
        Label lblBetalingsmiddel = new Label("Betalingsmiddel");
        pane.add(lblBetalingsmiddel, 0, 8);
        
        HBox betalingsmiddelBox = new HBox();
        betalingsmiddelBox.setSpacing(20);
        betalingsmiddelGroup = new ToggleGroup();
        rbKontant = new RadioButton("Kontant");
        betalingsmiddelBox.getChildren().add(rbKontant);
        rbKontant.setToggleGroup(betalingsmiddelGroup);
 
        rbDankort = new RadioButton("Dankort");
        betalingsmiddelBox.getChildren().add(rbDankort);
        rbDankort.setToggleGroup(betalingsmiddelGroup);
       
        rbMobilepay = new RadioButton("Mobilepay");
        betalingsmiddelBox.getChildren().add(rbMobilepay);
        rbMobilepay.setToggleGroup(betalingsmiddelGroup);

        rbRegning = new RadioButton("Regning");
        betalingsmiddelBox.getChildren().add(rbRegning);
        rbRegning.setToggleGroup(betalingsmiddelGroup);

        rbKlippekort = new RadioButton("Klippekort");
        betalingsmiddelBox.getChildren().add(rbKlippekort);
        rbKlippekort.setToggleGroup(betalingsmiddelGroup);
        
        pane.add(betalingsmiddelBox, 0, 9, 8, 1);
        
        Button btnOpretUdlejning = new Button("Opret udlejning");
        //btnOpretUdlejning.setOnAction(event -> btnOpretUdlejningAction());
        pane.add(btnOpretUdlejning, 6, 9);  
        
        lvwAddProduktgrupper();
    }
    
   

	private void lvwAddProduktgrupper() {
    	ArrayList<Produktgruppe> udlejningsproduktgrupper = new ArrayList<>();
    	lvwProduktgrupper.getItems().removeAll(lvwProduktgrupper.getItems());
    	for(Produktgruppe p : controller.getProduktgrupper())
    	{
    		if (p.getNavn().equals("fustage") || 
    				(p.getNavn().equals("kulsyre") || 
    						(p.getNavn().equals("anlæg"))))
    		{
    			udlejningsproduktgrupper.add(p);
    		}
    	}
    	lvwProduktgrupper.getItems().setAll(udlejningsproduktgrupper);
    }
    
//    private Ordre btnOpretUdlejningAction() {
//    	Ordre ordre = controller.createFadølsAnlægsUdlejning_konkret_ordre(rbKlippekort, prisliste, dpFraDato, dpTilDato, txfTidspunkt, fustager, kulsyrer, anlæg);
//	}
    
	private void btnFjernProduktAction() {
		String selected = lvwOrdrer.getSelectionModel().getSelectedItem();

        if (selected != null) {
            lvwOrdrer.getItems().remove(selected);
        }		
	}
	
	private void btnTilføjProduktAction() {
		Produkt produkt = lvwProdukter.getSelectionModel().getSelectedItem();
		String ordrelinje = "";
    	
    	if (produkt != null)
    	{
    		String navn = produkt.getNavn();
    		int antal = spinner.getValue();
    		double pris = 0;
    		
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
        			pris = pr.getPris() * antal;
        		}
        	}
        	ordrelinje = "antal: " + antal + " \t navn: " + navn + "\t pris: " + pris;
        	ordrelinjer.add(ordrelinje);
        	lvwOrdrer.getItems().setAll(ordrelinjer);
    	}
	}
	
	private void lvwProdukterUpdate() {
        lvwProdukter.getItems().removeAll(lvwProdukter.getItems());

        Produktgruppe selected = lvwProduktgrupper.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lvwProdukter.getItems().setAll(selected.getProdukter());
        }
    }

	private void lvwProduktgrupperOnClick() {
		lvwProdukterUpdate();
		Produktgruppe selected = lvwProduktgrupper.getSelectionModel().getSelectedItem();
		if (selected != null) {
			lvwProdukter.getItems().setAll(selected.getProdukter());
	}
	}
	

}
