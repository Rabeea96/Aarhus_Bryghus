package gui;

import controller.Controller;
import model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

// extender gridpane da det er et tab
public class Dagenssalg_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();
    private DatePicker dpDato;
    private TableView<Salg> table = new TableView<>();
    private ObservableList<Salg> data;
    private Label lblError;

    public Dagenssalg_oversigt() {

        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        // dato
        dpDato = new DatePicker();
        this.add(dpDato, 0, 0);
        dpDato.setEditable(false);

        // knap for at vise dagenssalg
        Button btnVisDagenssalg = new Button("Vis dagenssalg");
        this.add(btnVisDagenssalg, 0, 1);
        btnVisDagenssalg.setOnAction(event -> visDagenssalg_Action());

        // produkt-kolonne
        TableColumn<Salg, String> produktCol = new TableColumn<>("Produkt");
        produktCol.setMinWidth(450);
        // hver index i arraylisten fra metoden getNavn_pris_antal() kommer på en linje
        // for sig - dvs. produktnavn, pris og antal vises på hver linje i den samme
        // celle
        produktCol.setCellValueFactory(param -> {
            String result = String.join("\n", param.getValue().getNavn_pris_antal());
            return new SimpleStringProperty(result);
        });
        table.getColumns().add(produktCol);

        // samletpris-kolonne
        TableColumn<Salg, Integer> samletPrisCol = new TableColumn<>("Samlet pris");
        samletPrisCol.setMinWidth(100);
        samletPrisCol.setCellValueFactory(new PropertyValueFactory<Salg, Integer>("samletPris"));
        table.getColumns().add(samletPrisCol);

        // betalingsform-kolonne
        TableColumn<Salg, String> betalingsformCol = new TableColumn<>("Betalingsform");
        betalingsformCol.setMinWidth(150);
        betalingsformCol.setCellValueFactory(new PropertyValueFactory<Salg, String>("betalingsmiddel"));
        table.getColumns().add(betalingsformCol);

        // tabellen tilføjes til vinduet
        this.add(table, 0, 4);

        // label der viser fejl
        lblError = new Label();
        this.add(lblError, 0, 5);
        lblError.setStyle("-fx-text-fill: red");

    }

    // viser oversigt over dagenssalg i tabellen
    private void visDagenssalg_Action() {

        // hvis en dato fra kalenderen er valgt
        if (dpDato.getValue() != null) {

            data = FXCollections.observableArrayList(controller.getDagenssalg(dpDato.getValue()));
            table.setItems(data);

            lblError.setText("");

        } else {
            lblError.setText("Der skal vælges en dato");
        }

    }

}
