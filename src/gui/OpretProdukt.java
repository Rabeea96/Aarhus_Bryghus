package gui;

import java.util.ArrayList;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

public class OpretProdukt extends Stage {

    public OpretProdukt(String title) {
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
    private TextField txfNavn, txfPris, txfLiter, txfKg, txfHaner, txfAntalØl, txfAntalGlas;
    private Label lblLiter, lblKg, lblAnlæg, lblØl, lblGlas;
    private ListView<Produktgruppe> lvwProduktgruppe;
    private ListView<String> lvwProduktpris;
    private Produktgruppe produktgruppe = null;
    private ComboBox<Prisliste> cbPrislister;
    private ArrayList<String> produktpriser = new ArrayList<>();
    private ArrayList<Integer> priser = new ArrayList<>();
    private ArrayList<Prisliste> prislister = new ArrayList<>();

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label lblNavn = new Label("Navn");
        pane.add(lblNavn, 0, 0);

        txfNavn = new TextField();
        pane.add(txfNavn, 0, 1);

        // liter for fustage
        lblLiter = new Label("Liter");
        pane.add(lblLiter, 0, 2);
        lblLiter.setDisable(true);

        txfLiter = new TextField();
        pane.add(txfLiter, 0, 3);
        txfLiter.setDisable(true);

        // kg for kulsyre
        lblKg = new Label("Kg");
        pane.add(lblKg, 0, 4);
        lblKg.setDisable(true);

        txfKg = new TextField();
        pane.add(txfKg, 0, 5);
        txfKg.setDisable(true);

        // antal haner for anlæg
        lblAnlæg = new Label("Anlæg");
        pane.add(lblAnlæg, 0, 6);
        lblAnlæg.setDisable(true);

        txfHaner = new TextField();
        pane.add(txfHaner, 0, 7);
        txfHaner.setDisable(true);

        // antal øl for sampakning
        lblØl = new Label("Øl");
        pane.add(lblØl, 0, 8);
        lblØl.setDisable(true);

        txfAntalØl = new TextField();
        pane.add(txfAntalØl, 0, 9);
        txfAntalØl.setDisable(true);

        // antal glas for sampakning
        lblGlas = new Label("Glas");
        pane.add(lblGlas, 0, 10);
        lblGlas.setDisable(true);

        txfAntalGlas = new TextField();
        pane.add(txfAntalGlas, 0, 11);
        txfAntalGlas.setDisable(true);

        // produktgrupper
        Label lblProduktgruppe = new Label("Produktgruppe");
        pane.add(lblProduktgruppe, 1, 0);

        lvwProduktgruppe = new ListView<>();
        lvwProduktgruppe.getItems().setAll(controller.getProduktgrupper());
        pane.add(lvwProduktgruppe, 1, 1, 1, 11);

        ChangeListener<Produktgruppe> listener = (ov, oldString, newString) -> selectionChanged();
        lvwProduktgruppe.getSelectionModel().selectedItemProperty().addListener(listener);

        Label lblPrislister = new Label("Tilføj i prisliste");
        pane.add(lblPrislister, 0, 12);

        // prislister i dropdown-menu
        cbPrislister = new ComboBox<>();
        cbPrislister.getItems().addAll(controller.getPrislister());
        cbPrislister.setPromptText("Vælg prisliste");
        pane.add(cbPrislister, 0, 13);
        ChangeListener<Prisliste> listenerPrislister = (ov, oldString, newString) -> setPrislisteAction();
        cbPrislister.getSelectionModel().selectedItemProperty().addListener(listenerPrislister);

        Label lblPris = new Label("Produktpris");
        pane.add(lblPris, 0, 14);

        txfPris = new TextField();
        pane.add(txfPris, 0, 15);

        // knapper for tilføj og fjern en produktpris samt prisliste
        HBox produktPriserKnapBox = new HBox();
        Button btnTilføjProduktpris = new Button("Tilføj");
        btnTilføjProduktpris.setOnAction(event -> tilføjProduktprisAction());
        Button btnFjernProduktpris = new Button("Fjern");
        btnFjernProduktpris.setOnAction(event -> fjernProduktprisAction());
        produktPriserKnapBox.getChildren().addAll(btnTilføjProduktpris, btnFjernProduktpris);
        produktPriserKnapBox.setSpacing(20);
        pane.add(produktPriserKnapBox, 0, 16);

        Label lblProduktpriser = new Label("Produktpriser i prislister");
        pane.add(lblProduktpriser, 1, 12);

        lvwProduktpris = new ListView<>();
        lvwProduktpris.setPrefHeight(200);
        pane.add(lvwProduktpris, 1, 13, 1, 4);

        Button btnOpret = new Button("Opret produkt");
        pane.add(btnOpret, 0, 17);
        btnOpret.setOnAction(event -> opretAction());

    }

    private Produkt opretAction() {
        String navn = txfNavn.getText();
        int antal_øl = 0;
        int antal_glas = 0;
        Produkt produkt = null;

        // hvis navnet er udfyldt samt produktgruppe er valgt og der er lavet mindst en
        // produktpris for produktet
        if (txfNavn.getText().length() > 0 && produktgruppe != null && produktpriser.size() > 0) {

            // produktet oprettes
            for (Produktgruppe pg : controller.getProduktgrupper()) {
                if (pg.getNavn().equals("Simpelt produkt")) {
                    produkt = controller.createSimpel_produkt(navn, pg);

                } else if (pg.getNavn().equals("Fustage")) {

                    if (txfLiter.getText().length() > 0 && numberIsValid(txfLiter.getText()) == true) {
                        int liter = Integer.parseInt(txfLiter.getText());
                        produkt = controller.createFustage(navn, pg, liter);
                    }

                } else if (pg.getNavn().equals("Kulsyre")) {

                    if (txfKg.getText().length() > 0 && numberIsValid(txfKg.getText()) == true) {
                        int kg = Integer.parseInt(txfKg.getText());
                        produkt = controller.createKulsyre(navn, pg, kg);
                    }

                } else if (pg.getNavn().equals("Anlæg")) {

                    if (txfHaner.getText().length() > 0 && numberIsValid(txfHaner.getText()) == true) {
                        int antalHaner = Integer.parseInt(txfHaner.getText());
                        produkt = controller.createAnlæg(navn, pg, antalHaner);
                    }

                } else if (pg.getNavn().equals("Rundvisning")) {
                    produkt = controller.createRundvisning(navn, pg);

                } else if (pg.getNavn().equals("Klippekort")) {
                    produkt = controller.createKlippekort(navn, pg);

                } else if (pg.getNavn().equals("Sampakning")) {

                    if (txfAntalØl.getText().length() > 0 && numberIsValid(txfAntalØl.getText()) == true
                            && txfAntalGlas.getText().length() > 0 && numberIsValid(txfAntalGlas.getText()) == true) {
                        antal_øl = Integer.parseInt(txfAntalØl.getText());
                        antal_glas = Integer.parseInt(txfAntalGlas.getText());
                        produkt = controller.createSampakning(navn, pg, antal_øl, antal_glas);

                    } else if (txfAntalØl.getText().length() > 0 && numberIsValid(txfAntalØl.getText()) == true
                            && txfAntalGlas.getText().length() == 0) {
                        produkt = controller.createSampakning(navn, pg, antal_øl);
                    }
                }
            }

            // produktpriserne oprettes
            for (int i = 0; i < prislister.size(); i++) {
                controller.createProduktpris(prislister.get(i), priser.get(i), produkt);
            }
            hide();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Opret produkt");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges et navn for produktet samt produktgruppe og mindst én produktpris");
            alert.show();
        }
        return produkt;

    }

    private void selectionChanged() {
        Produktgruppe lvwSelectedItem = lvwProduktgruppe.getSelectionModel().getSelectedItem();

        if (lvwSelectedItem != null) {
            produktgruppe = lvwProduktgruppe.getSelectionModel().getSelectedItem();

            if (produktgruppe.getNavn().equals("Fustage")) {
                lblLiter.setDisable(false);
                txfLiter.setDisable(false);
            } else if (produktgruppe.getNavn().equals("Kulsyre")) {
                lblKg.setDisable(false);
                txfKg.setDisable(false);
            } else if (produktgruppe.getNavn().equals("Anlæg")) {
                lblAnlæg.setDisable(false);
                txfHaner.setDisable(false);
            } else if (produktgruppe.getNavn().equals("Sampakning")) {
                lblØl.setDisable(false);
                txfAntalØl.setDisable(false);
                lblGlas.setDisable(false);
                txfAntalGlas.setDisable(false);
            }
        }
    }

    private Prisliste setPrislisteAction() {
        Prisliste prisliste = null;

        if (cbPrislister.getValue() != null && cbPrislister.getSelectionModel().getSelectedItem() != null) {
            prisliste = cbPrislister.getSelectionModel().getSelectedItem();
        }

        return prisliste;
    }

    // bruges til at checke om prisen er indtastet i et gyldigt format - dvs. i
    // tal og ikke bogstaver
    private boolean numberIsValid(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void tilføjProduktprisAction() {

        // tjekker om prisen er indtastet samt om det er i tal og om en prisliste er
        // valgt
        if (txfPris.getText().length() > 0 && cbPrislister.getValue() != null
                && numberIsValid(txfPris.getText()) == true) {

            // hvis der ikke i forvejen er oprettet en produktpris i den valgte prisliste
            if (Controller.arraylistSearch(prislister, cbPrislister.getValue()) == false) {

                int pris = Integer.parseInt(txfPris.getText());

                // prisliste samt pris tilføjes til en liste som står for at indsætte værdierne
                // i listviewet
                produktpriser.add(setPrislisteAction().toString() + " " + pris);
                // prisen tilføjes en liste
                priser.add(pris);
                // prisliste tilføjes en liste
                prislister.add(cbPrislister.getValue());

                lvwProduktpris.getItems().setAll(produktpriser);

                txfPris.clear();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Opret produktpris");
                alert.setHeaderText("");
                alert.setContentText("Der kan ikke tilføjes flere produktpriser i den samme prisliste");
                alert.show();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Opret produktpris");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges en prisliste samt prisen skal angives i tal");
            alert.show();
        }

    }

    private void fjernProduktprisAction() {
        int index = lvwProduktpris.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            produktpriser.remove(index);
            lvwProduktpris.getItems().setAll(produktpriser);
            priser.remove(index);
            prislister.remove(index);
        }
    }

}
