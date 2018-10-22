package gui;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
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

        Label lblGruppe = new Label("Oversigt over produktgrupper");
        this.add(lblGruppe, 0, 0);

        lvwGruppe = new ListView<>();
        this.add(lvwGruppe, 0, 1);
        lvwGruppe.getItems().setAll(controller.getProduktgrupper());

        ChangeListener<Produktgruppe> listener = (ov, oldString, newString) -> selectionChanged();
        lvwGruppe.getSelectionModel().selectedItemProperty().addListener(listener);

        Label lblProdukt = new Label("Produkter i produktgruppe");
        this.add(lblProdukt, 1, 0);

        lvwProdukt = new ListView<>();
        this.add(lvwProdukt, 1, 1);

    }

    private void selectionChanged() {

        Produktgruppe produktgruppe = lvwGruppe.getSelectionModel().getSelectedItem();

        if (produktgruppe != null) {
            lvwProdukt.getItems().setAll(produktgruppe.getProdukter());
        }
    }
}
