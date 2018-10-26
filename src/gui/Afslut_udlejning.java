package gui;

import java.util.ArrayList;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Ordre;
import model.Ordrelinje;

//klassen extender Stage klassen da det er et undervindue
public class Afslut_udlejning extends Stage {
	
	Controller controller = Controller.getInstance();
	Ordre ordre;
	private ListView<Ordre> lvwOrdrer;
	private ListView<String> lvwOrdrelinjer;
	private TextField txfForbrug, txfPris;
	private ArrayList<String> ordrelinjer = new ArrayList<>();
	private ArrayList<Integer> antal = new ArrayList<>();
	private ArrayList<Double> pris = new ArrayList<>();
	private Label lblSamletPris;
	private double samletPris;
	private double ordrelinjePris;
	private double procent;
	private double nyOrdrelinjePris;
	private double returVærdi;


    public Afslut_udlejning(String title) {
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
        
        Label lblOrdrer = new Label("Ordrer:");
    	pane.add(lblOrdrer, 0, 0);	
    	
    	lvwOrdrer = new ListView<>();
    	lvwOrdrer.setPrefHeight(300);
    	pane.add(lvwOrdrer, 0, 1, 1, 5);
        lvwOrdrer.getItems().setAll(controller.getAktiveUdlejninger());
        
     // kalder på en metoden selectionChanged() hver gang der bliver valgt en ordre
        ChangeListener<Ordre> listener1 = (ov, oldString, newString) -> selectionChangedOrdrer();
        lvwOrdrer.getSelectionModel().selectedItemProperty().addListener(listener1);

    	Label lblProdukterIOrdre = new Label("Ordrelinjer");
    	pane.add(lblProdukterIOrdre, 1, 0);
    	
    	lvwOrdrelinjer = new ListView<>();
    	lvwOrdrelinjer.setOnMouseClicked(event -> lvwOrdrelinjerOnClick());
    	pane.add(lvwOrdrelinjer, 1, 1, 1, 5);
    	
//    	ChangeListener<String> listener2 = (ov, oldString, newString) -> selectionChangedOrdrelinjer();
//        lvwOrdrelinjer.getSelectionModel().selectedItemProperty().addListener(listener2);
//    	
    	Label lblOrdrelinjePris = new Label("Pris på ordrelinje:");
    	pane.add(lblOrdrelinjePris, 2, 0);
    	
    	txfPris = new TextField();
    	pane.add(txfPris, 2, 1);
    	
    	Label lblForbrug = new Label("Forbrugt mængde i procent:");
    	pane.add(lblForbrug, 2, 2);
    	
    	txfForbrug = new TextField();
    	pane.add(txfForbrug, 2, 3);
    	
    	Button btnOpdaterPris = new Button("Opdater pris");
    	btnOpdaterPris.setOnAction(event -> btnOpdaterPrisAction());
    	pane.add(btnOpdaterPris, 2, 4);
    	
    	lblSamletPris = new Label("Samlet pris: " + samletPris + " kr.");
    	pane.add(lblSamletPris, 2, 5);
    	
    	Button btnRegistrerUdlejning = new Button("Registrer udlejning");
    	btnRegistrerUdlejning.setOnAction(event ->btnRegistrerUdlejningAction());
    	pane.add(btnRegistrerUdlejning, 2, 6);
    	
    }
    
    private void lvwOrdrelinjerOnClick() {
        int index = lvwOrdrelinjer.getSelectionModel().getSelectedIndex();
        ordrelinjePris = this.pris.get(index) * antal.get(index);
        txfPris.setText(ordrelinjePris + "");
    }

	private void selectionChangedOrdrer() {
        ordre = lvwOrdrer.getSelectionModel().getSelectedItem();
        if (ordre != null) {
        	for (Ordrelinje o : ordre.getOrdrelinjer()) {
        	ordrelinjer.add(o.getAntal() +" " +  o.getProduktpris().getProdukt().getNavn());
        	antal.add(o.getAntal());
        	pris.add(o.getProduktpris().getPris());
        	samletPris = ordre.samletpris();
        	opdaterSamletPris();
        }
        	
        }
        if(ordrelinjer.isEmpty() == false) {
        lvwOrdrelinjer.getItems().setAll(ordrelinjer);
        }
	}
		
	private void btnOpdaterPrisAction() {
		if (lvwOrdrelinjer.getSelectionModel().getSelectedItem() != null) {
			if (txfForbrug.getText() != null) {
				
				int index = lvwOrdrelinjer.getSelectionModel().getSelectedIndex();
				procent = Double.parseDouble(txfForbrug.getText());
				
				ordrelinjer.set(index, ordrelinjer.get(index) + " " + procent + "%");
				ordrelinjePris = pris.get(index) * antal.get(index);
				returVærdi = ordrelinjePris * iProcent(procent);
				nyOrdrelinjePris = ordrelinjePris - returVærdi;
				pris.set(index, nyOrdrelinjePris);
				txfPris.setText(nyOrdrelinjePris + "");
			    lvwOrdrelinjer.getItems().setAll(ordrelinjer);				
				samletPris = (samletPris - returVærdi);
				opdaterSamletPris();
				txfForbrug.setText("");
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Afslut_udlejning");
	            alert.setHeaderText("");
	            alert.setContentText("Udfyld forbrugfelt");
	            alert.show();
			}
		} else {
			 Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Afslut_udlejning");
	            alert.setHeaderText("");
	            alert.setContentText("Vælg en ordrelinje før prisen opdateres");
	            alert.show();
		}
	}
	private void opdaterSamletPris() {
		lblSamletPris.setText("Samlet pris: " + samletPris + " kr." );
	}
	
	public double iProcent(double procent) {
        double p = (1 - procent / 100);

        return p;
    }
	//TODO : fjern ordre fra liste over aktive udlejninger. Medtag
	private void btnRegistrerUdlejningAction() {
		if (ordre != null) { 
		hide();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Afslut_udlejning");
        alert.setHeaderText("");
        alert.setContentText("Udlejning er afsluttet \nOrdrens samlede pris: " + samletPris + " kr. \nPant betalt ved besilling: " + " kr \nTil betaling: " + samletPris + " - pant kr.");
        
        alert.show();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Afslut_udlejning");
	        alert.setHeaderText("");
	        alert.setContentText("Der skal vælges en ordre at afslutte");
	        alert.show();
			
		}
		
		
	}
}
