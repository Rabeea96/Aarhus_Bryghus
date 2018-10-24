package gui;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

public class OpretPrisliste extends Stage {

    public OpretPrisliste(String title) {
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
    private TextField txfNavn;
    private Prisliste prisliste = null;

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label lblNavn = new Label("Navn");
        pane.add(lblNavn, 0, 0);

        txfNavn = new TextField();
        pane.add(txfNavn, 0, 1);

        Button btnOpret = new Button("Opret");
        pane.add(btnOpret, 0, 2);
        btnOpret.setOnAction(event -> opretAction());

    }

    private Prisliste opretAction() {
        String navn = txfNavn.getText();

        if (txfNavn.getText().length() > 0) {
            prisliste = controller.createPrisliste(navn);
            hide();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Opret prisliste");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges et navn for prislisten");
            alert.show();
        }

        return prisliste;

    }

}
