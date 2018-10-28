package gui;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

//klassen extender Stage klassen da det er et undervindue
public class Salg_vælgPrisliste extends Stage {

    public Salg_vælgPrisliste(String title) {
        initStyle(StageStyle.UTILITY);
        setMinHeight(100);
        setMinWidth(200);
        setResizable(false);
        setTitle(title);
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane);
        setScene(scene);
    }

    // Controller instans
    Controller controller = Controller.getInstance();
    private ListView<Prisliste> lvwPrisliste;
    private static Prisliste prisliste = null;
    private Label lblError;

    private void initContent(GridPane pane) {

        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // vælg prisliste
        Label lblPrisliste = new Label("Vælg prisliste");
        pane.add(lblPrisliste, 0, 0);

        lvwPrisliste = new ListView<>();
        pane.add(lvwPrisliste, 0, 1);
        lvwPrisliste.getItems().setAll(controller.getPrislister());
        lvwPrisliste.setPrefHeight(250);

        ChangeListener<Prisliste> listener = (ov, oldString, newString) -> selectionChanged();
        lvwPrisliste.getSelectionModel().selectedItemProperty().addListener(listener);

        // vælg- knap
        Button btnVælgPrisliste = new Button("Vælg");
        pane.add(btnVælgPrisliste, 0, 2);
        btnVælgPrisliste.setOnAction(event -> vælgPrislisteAction());

        // label der viser fejl
        lblError = new Label();
        pane.add(lblError, 0, 3);
        lblError.setStyle("-fx-text-fill: red");

    }

    // åbner salgsvinduet med priserne der matcher den valgte prisliste
    private void vælgPrislisteAction() {
        if (prisliste != null) {
            Salg_vindue salg_vindue = new Salg_vindue("Salg");
            salg_vindue.showAndWait();
            lblError.setText("");
        } else {
            lblError.setText("Der skal vælges en prisliste");
        }
    }

    // den valgte prisliste bliver gemt i en statisk variabel
    private void selectionChanged() {
        Prisliste lvwSelectedItem = lvwPrisliste.getSelectionModel().getSelectedItem();

        if (lvwSelectedItem != null) {
            prisliste = lvwPrisliste.getSelectionModel().getSelectedItem();
        }
    }

    // metoden bliver brugt til at hente prisliste-objektet i Salgs vinduet
    public static Prisliste getPrisliste() {
        return prisliste;
    }
}