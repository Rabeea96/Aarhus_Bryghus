package gui;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

public class OpretProduktgruppe extends Stage {

    public OpretProduktgruppe(String title) {
        initStyle(StageStyle.UTILITY);
        setMinHeight(250);
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
    private Produktgruppe produktgruppe = null;
    private Label lblError;

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // navn
        Label lblNavn = new Label("Navn");
        pane.add(lblNavn, 0, 0);

        txfNavn = new TextField();
        pane.add(txfNavn, 0, 1);

        // opret-knappen
        Button btnOpret = new Button("Opret");
        pane.add(btnOpret, 0, 4);
        btnOpret.setOnAction(event -> opretAction());

        // label der viser fejl
        lblError = new Label();
        pane.add(lblError, 0, 5, 2, 1);
        lblError.setStyle("-fx-text-fill: red");

    }

    private Produktgruppe opretAction() {
        String navn = txfNavn.getText();

        if (txfNavn.getText().length() > 0) {
            produktgruppe = controller.createProduktgruppe(navn);
            hide();
        } else {
            lblError.setText("Der skal vælges et navn \nfor produktgruppen");
        }

        return produktgruppe;
    }

}
