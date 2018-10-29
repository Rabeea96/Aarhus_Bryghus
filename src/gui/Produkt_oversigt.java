package gui;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import model.*;

// extender gridpane da det er et tab
public class Produkt_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();

    private ListView<Produkt> lvwProdukt;
    private ListView<Produktpris> lvwProduktpris;

    public Produkt_oversigt() {
        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        // oversigt over produkter
        Label lblProdukt = new Label("Oversigt over produkter");
        this.add(lblProdukt, 0, 0);

        lvwProdukt = new ListView<>();
        this.add(lvwProdukt, 0, 1);
        lvwProdukt.getItems().setAll(controller.getProdukter());
        lvwProdukt.setPrefWidth(300);

        ChangeListener<Produkt> listener = (ov, oldString, newString) -> selectionChanged();
        lvwProdukt.getSelectionModel().selectedItemProperty().addListener(listener);

        // produktets priser i prislisterne hvor produktet indgår
        Label lblPrisliste = new Label("Produktets priser i prislisterne hvor produktet indgår");
        this.add(lblPrisliste, 1, 0);

        lvwProduktpris = new ListView<>();
        this.add(lvwProduktpris, 1, 1);

        // opret produkt- knap
        Button btnOpretProdukt = new Button("Opret produkt");
        this.add(btnOpretProdukt, 0, 2);
        btnOpretProdukt.setOnAction(event -> opretProduktAction());

    }

    // henter produkterpriser samt prislister for den valgte produkt
    private void selectionChanged() {
        Produkt produkt = lvwProdukt.getSelectionModel().getSelectedItem();

        if (produkt != null) {
            lvwProduktpris.getItems().setAll(produkt.getProduktpriser());
        }
    }

    // opretter et nyt produkt
    private void opretProduktAction() {
        OpretProdukt opretProdukt = new OpretProdukt("Opret produkt");
        opretProdukt.showAndWait();

        // opdaterer listviewet over produkter
        lvwProdukt.getItems().setAll(controller.getProdukter());

    }

}
