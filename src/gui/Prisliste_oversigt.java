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

        // oversigt over prislister
        Label lblPrisliste = new Label("Oversigt over prislister");
        this.add(lblPrisliste, 0, 0);

        lvwPrisliste = new ListView<>();
        this.add(lvwPrisliste, 0, 1);
        lvwPrisliste.getItems().setAll(controller.getPrislister());
        lvwPrisliste.setPrefWidth(300);

        ChangeListener<Prisliste> listener = (ov, oldString, newString) -> selectionChanged();
        lvwPrisliste.getSelectionModel().selectedItemProperty().addListener(listener);

        // oversigt over produkter i prisliste
        Label lblProdukter = new Label("Oversigt over produkter i prisliste");
        this.add(lblProdukter, 1, 0);

        lvwProduktpris = new ListView<>();
        this.add(lvwProduktpris, 1, 1);
        lvwProduktpris.setPrefWidth(350);

        // opret prisliste- knap
        Button btnOpretPrisliste = new Button("Opret prisliste");
        this.add(btnOpretPrisliste, 0, 2);
        btnOpretPrisliste.setOnAction(event -> opretPrislisteAction());

    }

    // henter produkter samt priser for den valgte prisliste-objekt
    public void selectionChanged() {

        Prisliste prisliste = lvwPrisliste.getSelectionModel().getSelectedItem();
        ArrayList<String> produktpriser = new ArrayList<>();

        for (String key : controller.henteProdukterOgPriserIPrisliste(prisliste).keySet()) {
            produktpriser.add((key + " " + controller.henteProdukterOgPriserIPrisliste(prisliste).get(key)) + " kr.");
        }

        if (prisliste != null) {
            lvwProduktpris.getItems().setAll(produktpriser);
        }

    }

    // åbner opret prisliste- vinduet
    private void opretPrislisteAction() {
        OpretPrisliste opretPrisliste = new OpretPrisliste("Opret prisliste");
        opretPrisliste.showAndWait();

        // opdaterer listviewet over prislister
        lvwPrisliste.getItems().setAll(controller.getPrislister());

    }

}
