package gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import model.*;
import controller.Controller;

// extender gridpane da det er et tab
public class Produktgruppe_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();

    private ListView<Produktgruppe> lvwGruppe;
    private ListView<Produkt> lvwProdukt;

    public Produktgruppe_oversigt() {
        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        // oversigt over produktgrupper
        Label lblGruppe = new Label("Oversigt over produktgrupper");
        this.add(lblGruppe, 0, 0);

        lvwGruppe = new ListView<>();
        this.add(lvwGruppe, 0, 1);
        lvwGruppe.getItems().setAll(controller.getProduktgrupper());
        lvwGruppe.setPrefWidth(300);

        ChangeListener<Produktgruppe> listener = (ov, oldString, newString) -> selectionChanged();
        lvwGruppe.getSelectionModel().selectedItemProperty().addListener(listener);

        // produkter i produktgruppe
        Label lblProdukt = new Label("Produkter i produktgruppe");
        this.add(lblProdukt, 1, 0);

        lvwProdukt = new ListView<>();
        this.add(lvwProdukt, 1, 1);

        // opret produktgruppe- knap
        Button btnOpretProduktgruppe = new Button("Opret produktgruppe");
        this.add(btnOpretProduktgruppe, 0, 2);
        btnOpretProduktgruppe.setOnAction(event -> opretProduktgruppeAction());

    }

    // henter produkter for den valgte produktgruppe-objekt
    private void selectionChanged() {

        Produktgruppe produktgruppe = lvwGruppe.getSelectionModel().getSelectedItem();

        if (produktgruppe != null) {
            lvwProdukt.getItems().setAll(produktgruppe.getProdukter());
        }

    }

    // opretter et nyt produktgruppe
    private void opretProduktgruppeAction() {
        OpretProduktgruppe opretProduktgruppe = new OpretProduktgruppe("Opret produktgruppe");
        opretProduktgruppe.showAndWait();

        // opdaterer listviewet over produktgrupper
        lvwGruppe.getItems().setAll(controller.getProduktgrupper());

    }
}
