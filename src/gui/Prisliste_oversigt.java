package gui;

import java.util.ArrayList;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import model.*;

// extender gridpane da det er et tab
public class Prisliste_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();

    private ListView<Prisliste> lvwPrisliste;
    private ListView<String> lvwProduktpris;

    public Prisliste_oversigt() {
        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        Label lblPrisliste = new Label("Oversigt over prislister");
        this.add(lblPrisliste, 0, 0);

        lvwPrisliste = new ListView<>();
        this.add(lvwPrisliste, 0, 1);
        lvwPrisliste.getItems().setAll(controller.getPrislister());

        ChangeListener<Prisliste> listener = (ov, oldString, newString) -> selectionChanged();
        lvwPrisliste.getSelectionModel().selectedItemProperty().addListener(listener);

        Label lblProdukter = new Label("Oversigt over produkter i prisliste");
        this.add(lblProdukter, 1, 0);

        lvwProduktpris = new ListView<>();
        this.add(lvwProduktpris, 1, 1);
        lvwProduktpris.setPrefWidth(350);

        Button btnOpretPrisliste = new Button("Opret prisliste");
        this.add(btnOpretPrisliste, 0, 2);
        btnOpretPrisliste.setOnAction(event -> opretPrislisteAction());

    }

    public void selectionChanged() {

        Prisliste prisliste = lvwPrisliste.getSelectionModel().getSelectedItem();
        ArrayList<String> produktpriser = new ArrayList<>();

        for (String key : controller.henteProdukterIPrisliste(prisliste).keySet()) {
            produktpriser.add((key + " " + controller.henteProdukterIPrisliste(prisliste).get(key)) + " kr.");
        }

        if (prisliste != null) {
            lvwProduktpris.getItems().setAll(produktpriser);
        }

    }

    // opretter en ny prisliste
    private void opretPrislisteAction() {
        OpretPrisliste opretPrisliste = new OpretPrisliste("Opret prisliste");
        opretPrisliste.showAndWait();

        // opdaterer listviewet over prislister
        lvwPrisliste.getItems().setAll(controller.getPrislister());

    }

}
