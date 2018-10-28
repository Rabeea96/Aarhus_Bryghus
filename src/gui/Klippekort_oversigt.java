package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;

import controller.Controller;

// extender gridpane da det er et tab
public class Klippekort_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();
    private TextField txfKlippekort, txfKlip;
    private DatePicker dpStart, dpSlut;
    private Label lblError;

    public Klippekort_oversigt() {

        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        // fra dato
        Label lblFra_dato = new Label("Dato fra perioden:");
        this.add(lblFra_dato, 0, 0);

        dpStart = new DatePicker();
        this.add(dpStart, 1, 0);
        dpStart.setEditable(false);
        // først når brugeren vælger en startdato er det muligt at vælge en slutdato
        dpStart.setOnMouseClicked(event -> dpSlut.setDisable(false));

        // til dato
        Label lblTil_dato = new Label("Dato til perioden:");
        this.add(lblTil_dato, 0, 1);

        dpSlut = new DatePicker();
        this.add(dpSlut, 1, 1);
        dpSlut.setEditable(false);
        // først når brugeren vælger en startdato er det muligt at vælge en slutdato
        dpSlut.setDisable(true);

        // der kan ikke vælges en slutdato der er før startdatoen
        dpSlut.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = dpStart.getValue();

                setDisable(empty || date.compareTo(startDate) < 0);
            }
        });

        // knap for at vise oversigt
        Button btnVisOversigt = new Button("Vis oversigt");
        this.add(btnVisOversigt, 0, 2);
        btnVisOversigt.setOnAction(event -> visOversigt_Action());

        // antal solgte klippekort
        Label lblAntal_klippekort = new Label("Antal solgte klippekort:");
        this.add(lblAntal_klippekort, 0, 4);

        // brugeren kan ikke skrive noget i klippekort-feltet
        txfKlippekort = new TextField();
        this.add(txfKlippekort, 1, 4);
        txfKlippekort.setEditable(false);
        txfKlippekort.setDisable(true);
        txfKlippekort.setStyle("-fx-opacity: 1");

        // antal brugte klip
        Label lblAntal_klip = new Label("Antal brugte klip:");
        this.add(lblAntal_klip, 0, 5);

        // brugeren kan ikke skrive noget i klip-feltet
        txfKlip = new TextField();
        this.add(txfKlip, 1, 5);
        txfKlip.setEditable(false);
        txfKlip.setDisable(true);
        txfKlip.setStyle("-fx-opacity: 1");

        // label der viser fejl
        lblError = new Label();
        this.add(lblError, 0, 6, 2, 1);
        lblError.setStyle("-fx-text-fill: red");

    }

    // viser oversigt over brugte klip samt solge klippekort i den valgte periode
    private void visOversigt_Action() {
        if (dpStart.getValue() != null && dpSlut.getValue() != null) {

            int antal_klippekort = controller.getAntal_solgte_klippekort(dpStart.getValue(), dpSlut.getValue());
            int antal_klip = controller.getAntal_brugte_klip(dpStart.getValue(), dpSlut.getValue());

            txfKlippekort.setText(antal_klippekort + "");
            txfKlip.setText(antal_klip + "");

            lblError.setText("");

        } else {
            lblError.setText("Der skal vælges en startdato og en slutdato");
        }
    }

}
