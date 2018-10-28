package gui;

import java.util.ArrayList;

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

// klassen extender Stage klassen da det er et undervindue
public class VælgRundvisning_vindue extends Stage {

    public VælgRundvisning_vindue(String title) {
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
    private ListView<String> lvwRundvisning;
    private ArrayList<Produkt> rundvisninger = new ArrayList<>();
    private ArrayList<String> førsteDel = new ArrayList<>();
    private static Produkt rundvisning = null;
    private Label lblError;

    private void initContent(GridPane pane) {

        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // vælg rundvisning
        Label lblPrisliste = new Label("Vælg rundvisning");
        pane.add(lblPrisliste, 0, 0);

        lvwRundvisning = new ListView<>();
        pane.add(lvwRundvisning, 0, 1);

                for (Produkt p : controller.getProdukter()) {
                    if(p instanceof Rundvisning)
                    // så smides alle rundvisningerne i en liste
                    rundvisninger.add(p);
                    String produktnavn = p.getNavn();

                    // for hver rundvisning gemmer vi det første del af rundvisnings-navnet som er
                    // før kommaet
                    String[] produktnavne = produktnavn.split(",");
                    førsteDel.add(produktnavne[0]);
                }

        // de første dele af rundvisningsnavnene smides i listviewet
        lvwRundvisning.getItems().setAll(førsteDel);
        lvwRundvisning.setPrefHeight(250);

        ChangeListener<String> listener = (ov, oldString, newString) -> selectionChanged();
        lvwRundvisning.getSelectionModel().selectedItemProperty().addListener(listener);

        // vælg- knap
        Button btnVælgPrisliste = new Button("Vælg");
        pane.add(btnVælgPrisliste, 0, 2);
        btnVælgPrisliste.setOnAction(event -> vælgRundvisningAction());

        // label der viser fejl
        lblError = new Label();
        pane.add(lblError, 0, 3);
        lblError.setStyle("-fx-text-fill: red");

    }

    // åbner rundvisningsvinduet
    private void vælgRundvisningAction() {
        if (rundvisning != null) {
            Rundvisning_vindue rundvisning_vindue = new Rundvisning_vindue("Rundvisning");
            rundvisning_vindue.showAndWait();
            lblError.setText("");
        } else {
            lblError.setText("Der skal vælges en rundvisning");
        }
    }

    // den valgte rundvisning bliver gemt i en statisk variabel
    private void selectionChanged() {
        int index = lvwRundvisning.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            rundvisning = rundvisninger.get(index);
        }
    }

    // metoden bliver brugt til at hente rundvisnings-objektet i
    // rundvisnings-vinduet
    public static Produkt getRundvisning() {
        return rundvisning;
    }
}