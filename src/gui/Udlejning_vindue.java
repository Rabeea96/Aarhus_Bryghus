package gui;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Opret_udlejning opret_udlejning;
    private Afslut_udlejning afslut_udlejning;

    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // det nye vindue der åbnes når der klikkes på knappen opret udlejning
        opret_udlejning = new Opret_udlejning("Opret udlejning");

        // det nye vindue der åbnes når der klikkes på knappen afslut udlejning
        afslut_udlejning = new Afslut_udlejning("Afslut udlejning");

        Button btnOpretUdlejning = new Button("Opret udlejning");
        btnOpretUdlejning.setOnAction(event -> btnOpretUdlejningAction());
        pane.add(btnOpretUdlejning, 0, 0);
        btnOpretUdlejning.setPrefHeight(40);
        btnOpretUdlejning.setPrefWidth(150);

        Button btnAfslutUdlejning = new Button("Afslut udlejning");
        btnAfslutUdlejning.setOnAction(event -> btnAfslutUdlejningAction());
        pane.add(btnAfslutUdlejning, 1, 0);
        btnAfslutUdlejning.setPrefHeight(40);
        btnAfslutUdlejning.setPrefWidth(190);
    }

    // åbner vinduet for oprettelse af en udlejning
    private void btnOpretUdlejningAction() {
        opret_udlejning.showAndWait();
    }

    // åbner vinduet for at afslutte en udlejning
    private void btnAfslutUdlejningAction() {
        afslut_udlejning.showAndWait();
    }
}
