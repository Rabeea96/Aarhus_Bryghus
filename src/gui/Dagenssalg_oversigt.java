package gui;

import container.Container;
import controller.Controller;
import model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

//extender gridpane da det er et tab
public class Dagenssalg_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();
    // Container instans
    Container container = Container.getInstance();
    private DatePicker dpDato;
    private TableView<Salg> table = new TableView<>();
    private ObservableList<Salg> data;

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

        // produkt
        TableColumn<Salg, String> produktCol = new TableColumn<>("Produkt");
        produktCol.setMinWidth(400);
        // hver index i arraylisten fra metoden getNavn_pris_antal() kommer på en linje
        // for sig - dvs. produktnavn, pris og antal vises på hver linje i den samme
        // celle
        produktCol.setCellValueFactory(param -> {
            String result = String.join("\n", param.getValue().getNavn_pris_antal());
            return new SimpleStringProperty(result);
        });
        table.getColumns().add(produktCol);

        // samletpris
        TableColumn<Salg, Integer> samletPrisCol = new TableColumn<>("Samlet pris");
        samletPrisCol.setMinWidth(100);
        samletPrisCol.setCellValueFactory(new PropertyValueFactory<Salg, Integer>("samletPris"));
        table.getColumns().add(samletPrisCol);

        // betalingsform
        TableColumn<Salg, String> betalingsformCol = new TableColumn<>("Betalingsform");
        betalingsformCol.setMinWidth(150);
        betalingsformCol.setCellValueFactory(new PropertyValueFactory<Salg, String>("betalingsmiddel"));
        table.getColumns().add(betalingsformCol);

        // kolonnerne bliver smidt ind i tabellen
        this.add(table, 0, 4);

    }

    private void visDagenssalg_Action() {
        if (dpDato.getValue() != null) {

            data = FXCollections.observableArrayList(controller.getDagenssalg(dpDato.getValue()));
            table.setItems(data);

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Dagenssalg_oversigt");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges en dato");
            alert.show();
        }

    }

}
