package gui;

import java.time.LocalDate;
import java.util.ArrayList;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

public class Salg_vindue extends Stage {

    public Salg_vindue(String title) {
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
    private ListView<Produktgruppe> lvwProduktgruppe;
    private ListView<Produkt> lvwProdukt;
    private ListView<String> lvwOrdre;
    private TextField txfProduktpris, txfRabat;
    private Spinner<Integer> spinner;
    private Label lblTotalPris_tallet, lblRabat;
    private RadioButton rbØnskerRabatJa, rbØnskerRabatNej, rbProcent, rbKroner, rbKontant, rbDankort, rbMobilepay,
            rbRegning, rbKlippekort;
    private ToggleGroup ønskerRabatGroup, rabatGroup, betalingsmiddelGroup;
    private HBox rabatBox;
    private Produkt produkt = null;
    private ArrayList<String> valgteProdukter = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private ArrayList<Produktgruppe> produktgrupper = new ArrayList<>();
    private double totalPris;
    private ArrayList<Double> pris_liste = new ArrayList<>();
    private ArrayList<Produktpris> produktpriser_liste = new ArrayList<>();
    private ArrayList<Integer> antal_liste = new ArrayList<>();

    public void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // produktgruppe
        Label lblProduktgruppe = new Label("Produktgruppe");
        pane.add(lblProduktgruppe, 0, 0);

        lvwProduktgruppe = new ListView<>();
        pane.add(lvwProduktgruppe, 0, 1);
        // alle produktgrupper undtagen Rundvisning-produktgruppen tilføjes til
        // listviewet
        for (Produktgruppe pg : controller.getProduktgrupper()) {
            if (pg.getNavn().equals("Rundvisning") == false) {
                produktgrupper.add(pg);
            }
        }
        lvwProduktgruppe.getItems().setAll(produktgrupper);
        lvwProduktgruppe.setPrefHeight(250);

        ChangeListener<Produktgruppe> listener1 = (ov, oldString, newString) -> selectionChangedProduktgruppe();
        lvwProduktgruppe.getSelectionModel().selectedItemProperty().addListener(listener1);

        // produkt
        Label lblProdukt = new Label("Produkt");
        pane.add(lblProdukt, 1, 0);

        lvwProdukt = new ListView<>();
        pane.add(lvwProdukt, 1, 1);
        lvwProdukt.setPrefHeight(250);

        ChangeListener<Produkt> listener2 = (ov, oldString, newString) -> selectionChangedProdukt();
        lvwProdukt.getSelectionModel().selectedItemProperty().addListener(listener2);

        // produktpris
        Label lblProduktpris = new Label("Pris pr. stk");

        txfProduktpris = new TextField();
        txfProduktpris.setEditable(false);

        // produktantal
        Label lblProduktAntal = new Label("Antal");

        spinner = new Spinner<>();

        // antallet kan mindst være 1 og maksimum 20 pr. produkt - tallet bliver sat til
        // 1 når et produkt vælges
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);
        spinner.setValueFactory(valueFactory);

        // ordre
        Label lblOrdre = new Label("Ordre");
        pane.add(lblOrdre, 2, 0);

        lvwOrdre = new ListView<>();
        pane.add(lvwOrdre, 2, 1, 2, 1);
        lvwOrdre.setPrefHeight(250);
        lvwOrdre.setPrefWidth(500);

        // total pris (selve ordet)
        Label lblTotalPris_ordet = new Label("Total pris: ");

        HBox totalPris_ordet_box = new HBox();
        totalPris_ordet_box.getChildren().add(lblTotalPris_ordet);
        pane.add(totalPris_ordet_box, 2, 2);

        // den totale pris der bliver opdateret løbende (selve tallet)
        lblTotalPris_tallet = new Label(totalPris + "");

        HBox totalPris_tallet_box = new HBox();
        totalPris_tallet_box.getChildren().add(lblTotalPris_tallet);
        totalPris_tallet_box.setAlignment(Pos.BASELINE_RIGHT);
        pane.add(totalPris_tallet_box, 3, 2);

        // knapper for tilføj og fjern en produkt samt produktpris fra ordre
        Button btnTilføjProdukt = new Button("Tilføj til ordre");
        btnTilføjProdukt.setOnAction(event -> tilføjProduktAction());
        btnTilføjProdukt.setPrefSize(160, 40);
        Button btnFjernProdukt = new Button("Fjern fra ordre");
        btnFjernProdukt.setOnAction(event -> fjernProduktAction());
        btnFjernProdukt.setPrefSize(150, 40);

        // en vertikal box der indeholder produktpris-label, produktpris-feltet samt
        // tilføj-knappen
        VBox vbox1 = new VBox();
        vbox1.setSpacing(20);
        vbox1.getChildren().add(lblProduktpris);
        vbox1.getChildren().add(txfProduktpris);
        vbox1.getChildren().add(btnTilføjProdukt);

        // en vertikal box der indeholder produktantal-label, spinner-feltet samt
        // fjern-knappen
        VBox vbox2 = new VBox();
        vbox2.setSpacing(20);
        vbox2.getChildren().add(lblProduktAntal);
        vbox2.getChildren().add(spinner);
        vbox2.getChildren().add(btnFjernProdukt);

        // en horizontal box der indeholder begge vertikal-boxene
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.getChildren().add(vbox1);
        hbox.getChildren().add(vbox2);
        hbox.setPrefWidth(300);
        pane.add(hbox, 1, 2, 1, 2);

        Label lblØnskerRabat = new Label("Ønskes rabat");
        pane.add(lblØnskerRabat, 0, 6);

        // radiobuttons for om der skal gives rabat
        HBox ønskerRabatBox = new HBox();
        ønskerRabatBox.setSpacing(20);
        ønskerRabatGroup = new ToggleGroup();
        rbØnskerRabatJa = new RadioButton("Ja");
        ønskerRabatBox.getChildren().add(rbØnskerRabatJa);
        rbØnskerRabatJa.setToggleGroup(ønskerRabatGroup);
        rbØnskerRabatNej = new RadioButton("Nej");
        ønskerRabatBox.getChildren().add(rbØnskerRabatNej);
        rbØnskerRabatNej.setToggleGroup(ønskerRabatGroup);
        pane.add(ønskerRabatBox, 0, 7);
        ønskerRabatGroup.selectedToggleProperty().addListener(event -> rbØnskerRabatAction());
        // rabat er som standard sat til 'Nej'
        ønskerRabatGroup.getToggles().get(1).setSelected(true);

        lblRabat = new Label("Angiv hvordan:");
        pane.add(lblRabat, 1, 6);
        lblRabat.setDisable(true);

        // radiobuttons for om rabatten skal angives i kroner eller i procent
        rabatBox = new HBox();
        rabatBox.setSpacing(20);
        rabatGroup = new ToggleGroup();
        rbKroner = new RadioButton("Kroner");
        rabatBox.getChildren().add(rbKroner);
        rbKroner.setToggleGroup(rabatGroup);
        rbProcent = new RadioButton("Procent");
        rabatBox.getChildren().add(rbProcent);
        rbProcent.setToggleGroup(rabatGroup);
        pane.add(rabatBox, 1, 7);
        rabatGroup.selectedToggleProperty().addListener(event -> rbRabatAction());
        // rabat er som standard sat til 'Kroner'
        rabatGroup.getToggles().get(0).setSelected(true);
        rabatBox.setDisable(true);

        txfRabat = new TextField();
        pane.add(txfRabat, 1, 8);
        txfRabat.setDisable(true);

        Label lblBetalingsmiddel = new Label("Betalingsmiddel");
        pane.add(lblBetalingsmiddel, 0, 9);

        // radiobuttons for betalingsmiddel
        HBox betalingsmiddelBox = new HBox();
        betalingsmiddelBox.setSpacing(20);
        betalingsmiddelGroup = new ToggleGroup();
        rbKontant = new RadioButton("KONTANT");
        betalingsmiddelBox.getChildren().add(rbKontant);
        rbKontant.setToggleGroup(betalingsmiddelGroup);
        rbDankort = new RadioButton("DANKORT");
        betalingsmiddelBox.getChildren().add(rbDankort);
        rbDankort.setToggleGroup(betalingsmiddelGroup);
        rbMobilepay = new RadioButton("MOBILEPAY");
        betalingsmiddelBox.getChildren().add(rbMobilepay);
        rbMobilepay.setToggleGroup(betalingsmiddelGroup);
        rbRegning = new RadioButton("REGNING");
        betalingsmiddelBox.getChildren().add(rbRegning);
        rbRegning.setToggleGroup(betalingsmiddelGroup);
        rbKlippekort = new RadioButton("KLIPPEKORT");
        betalingsmiddelBox.getChildren().add(rbKlippekort);
        rbKlippekort.setToggleGroup(betalingsmiddelGroup);

        pane.add(betalingsmiddelBox, 0, 10, 2, 1);
        betalingsmiddelGroup.selectedToggleProperty().addListener(event -> rbBetalingsMiddelAction());

        Button btnOpretSalg = new Button("Opret salg");
        pane.add(btnOpretSalg, 0, 13);
        btnOpretSalg.setPrefSize(150, 60);
        btnOpretSalg.setOnAction(event -> registrerSalgAction());

    }

    // opretter en ordre samt registrerer et salg
    private Ordre registrerSalgAction() {
        Ordre ordre = null;

        // hvis der er valgt nogle produkter i ordren og betalingsmiddel er valgt
        if (valgteProdukter.isEmpty() == false && rbBetalingsMiddelAction() != null) {

            String betaling = rbBetalingsMiddelAction();
            Betalingsmiddel betalingsmiddel = Betalingsmiddel.valueOf(betaling);
            boolean rabat = rbØnskerRabatAction();
            int rabatten = 0;
            Strategy_giv_rabat rabat_form = rbRabatAction();
            LocalDate dato = LocalDate.now();
            Prisliste prisliste = Salg_vælgPrisliste.getPrisliste();

            // hvis der ønskes rabat
            if (rabat == true) {
                // hvis rabatten er udfyldt
                if (txfRabat.getText().length() > 0) {
                    // hvis rabatten er udfyldt i tal
                    if (numberIsValid(txfRabat.getText()) == true) {
                        rabatten = Integer.parseInt(txfRabat.getText());
                        ordre = controller.createOrdre(betalingsmiddel, dato, prisliste, rabat_form, rabatten);
                    } else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Salg_vindue");
                        alert.setHeaderText("");
                        alert.setContentText("Rabatten skal angives i tal");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Salg_vindue");
                    alert.setHeaderText("");
                    alert.setContentText("Rabatten skal udfyldes");
                    alert.show();
                }

                // ellers oprettes ordren uden rabat
            } else {
                ordre = controller.createOrdre(betalingsmiddel, dato, prisliste);
            }

            // ordren bliver oprettet, vinduet skjules og man vender tilbage til
            // hovedmenuen og der popper et pop-up vindue der viser den samlede pris
            if (ordre != null) {
                for (int i = 0; i < valgteProdukter.size(); i++) {
                    controller.createOrdrelinje(antal_liste.get(i), produktpriser_liste.get(i), ordre);
                }
                hide();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Salg_vindue");
                alert.setHeaderText("");
                alert.setContentText(
                        "Ordren er oprettet \nDen samlede pris er: " + controller.beregnPris(ordre) + " kr.");
                alert.show();
            }

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Salg_vindue");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges nogle produkter for ordren samt betalingsmiddel skal angives");
            alert.show();
        }

        return ordre;
    }

    // henter det valgte produktgruppe fra produktgruppe-listview
    private void selectionChangedProduktgruppe() {
        Produktgruppe produktgruppe = lvwProduktgruppe.getSelectionModel().getSelectedItem();

        if (produktgruppe != null) {
            lvwProdukt.getItems().setAll(produktgruppe.getProdukter());
        }
    }

    // henter det valgte produkt fra produkt-listview
    private void selectionChangedProdukt() {
        if (lvwProdukt.getSelectionModel().getSelectedItem() != null) {

            produkt = lvwProdukt.getSelectionModel().getSelectedItem();

            // produktprisen hentes ud fra den valgte prisliste i den forrige vindue
            for (Produktpris pp : produkt.getProduktpriser()) {
                if (pp.getPrisliste().equals(Salg_vælgPrisliste.getPrisliste())) {
                    txfProduktpris.setText(pp.getPris() + "");
                }
            }
        }
    }

    // tilføjer produkt til ordre
    private void tilføjProduktAction() {

        if (produkt != null && spinner.getValue() > 0 && txfProduktpris.getText().length() > 0) {

            // hvis produktet ikke allerede er blevet tilføjet til ordren
            if (produkter.contains(produkt) == false) {
                double pris = Double.parseDouble(txfProduktpris.getText());
                int antal = spinner.getValue();
                Produktpris produktpris = null;

                // produktpris-objektet hentes ud fra den valgte prisliste i den forrige vindue
                for (Produktpris pp : produkt.getProduktpriser()) {
                    if (pp.getPrisliste().equals(Salg_vælgPrisliste.getPrisliste())) {
                        produktpris = pp;
                    }
                }

                valgteProdukter.add("Produkt: " + produkt.getNavn() + " \t Pris: " + pris + " \t Antal: " + antal);
                lvwOrdre.getItems().setAll(valgteProdukter);
                produkter.add(produkt);
                pris_liste.add(pris);
                antal_liste.add(antal);
                produktpriser_liste.add(produktpris);

                // den totale pris opdateres
                totalPris = totalPris + (pris * antal);
                lblTotalPris_tallet.setText(totalPris + "");

                // nulstiller felterne når produktet tilføjes til ordren
                txfProduktpris.clear();
                spinner.getValueFactory().setValue(0);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Salg_vindue");
                alert.setHeaderText("");
                alert.setContentText("Produktet er allerede tilføjet til ordren");
                alert.show();
            }

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Salg_vindue");
            alert.setHeaderText("");
            alert.setContentText("Der skal vælges et produkt samt et antal af produktet");
            alert.show();
        }
    }

    // fjerner produkt fra ordre
    private void fjernProduktAction() {
        if (lvwOrdre.getSelectionModel().getSelectedItem() != null) {
            // henter index for det valgte element
            int index = lvwOrdre.getSelectionModel().getSelectedIndex();

            // opdaterer listviewet samt de relevante lister
            valgteProdukter.remove(index);
            produkter.remove(index);
            lvwOrdre.getItems().setAll(valgteProdukter);

            // opdaterer den totale pris
            totalPris = totalPris - (pris_liste.get(index) * antal_liste.get(index));
            lblTotalPris_tallet.setText(totalPris + "");
            pris_liste.remove(index);
            antal_liste.remove(index);
            produktpriser_liste.remove(index);

        }

    }

    // hvis der ønskes rabat tændes rabatboxen hvor der kan angives om det er i
    // procent eller kroner og tekstfeltet hvor man angiver rabatten
    private boolean rbØnskerRabatAction() {
        boolean rabat = false;

        if (ønskerRabatGroup.getToggles().get(0).isSelected() == true) {
            lblRabat.setDisable(false);
            rabatBox.setDisable(false);
            txfRabat.setDisable(false);
            rabat = true;
        }

        return rabat;
    }

    private Strategy_giv_rabat rbRabatAction() {
        Strategy_giv_rabat strategy = null;

        if (rabatGroup.getToggles().get(0).isSelected() == true) {
            strategy = new Giv_rabat_i_kroner();

        } else if (rabatGroup.getToggles().get(1).isSelected() == true) {
            strategy = new Giv_rabat_i_procent();

        }

        return strategy;
    }

    private String rbBetalingsMiddelAction() {
        String betalingsmiddel = "";
        double rabat = 0;

        if (betalingsmiddelGroup.getSelectedToggle() != null) {
            betalingsmiddel = ((RadioButton) betalingsmiddelGroup.getSelectedToggle()).getText();
            boolean ønskerRabat = ønskerRabatGroup.getToggles().get(0).isSelected();
            boolean kroner = rabatGroup.getToggles().get(0).isSelected();

            // den totale pris opdateres
            if (ønskerRabat == true && txfRabat.getText().length() > 0) {
                rabat = Double.parseDouble(txfRabat.getText());

                // hvis rabatten er i kroner
                if (kroner == true) {
                    totalPris = totalPris - rabat;

                    // eller hvis rabatten er i procent
                } else {
                    totalPris = totalPris * (1.0 - rabat / 100);
                }

            }

            // den totale pris opdateres
            lblTotalPris_tallet.setText(totalPris + "");

        } else {
            betalingsmiddel = null;
        }

        return betalingsmiddel;

    }

    // bruges til at checke om rabatten er indtastet i et gyldigt format - dvs. i
    // tal og ikke bogstaver
    private boolean numberIsValid(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
