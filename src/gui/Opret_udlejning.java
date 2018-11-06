package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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

// klassen extender Stage klassen da det er et undervindue
public class Opret_udlejning extends Stage {
    Controller controller = Controller.getInstance();
    private DatePicker dpFraDato, dpTilDato;
    private TextField txfTidspunkt, txfProduktpris;
    private FadølsAnlægsUdlejning_ordre ordre = null;
    private ListView<Produktgruppe> lvwProduktgrupper;
    private ListView<Produkt> lvwProdukter;
    private ListView<String> lvwOrdre;
    private ToggleGroup betalingsmiddelGroup = new ToggleGroup();
    private RadioButton rbKontant, rbDankort, rbMobilepay, rbRegning, rbKlippekort;
    private Spinner<Integer> spinner;
    private Label lblTotalPris_tallet, lblError;
    private ArrayList<String> ordrelinjer = new ArrayList<>();
    private ArrayList<Produkt> fustager = new ArrayList<>();
    private ArrayList<Produkt> kulsyrer = new ArrayList<>();
    private ArrayList<Produkt> anlæg = new ArrayList<>();
    private Prisliste prisliste = null;
    private ArrayList<Integer> antal = new ArrayList<>();
    private ArrayList<Produktpris> produktpriser = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private ArrayList<Produkt> produkter_i_ordre = new ArrayList<>();
    private ArrayList<Double> priser_i_ordre = new ArrayList<>();
    private Produktgruppe produktgruppe;
    private Produkt produkt;
    private double samletPris;

    public Opret_udlejning(String title) {
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

    private void initContent(GridPane pane) {
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        // fra dato
        Label lblFraDato = new Label("Fra dato:");
        pane.add(lblFraDato, 0, 0);

        dpFraDato = new DatePicker();
        pane.add(dpFraDato, 0, 1);
        dpFraDato.setEditable(false);

        // til dato
        Label lblTilDato = new Label("Til dato:");
        pane.add(lblTilDato, 0, 2);

        dpTilDato = new DatePicker();
        dpFraDato.setOnMouseClicked(event -> dpTilDato.setDisable(false));
        pane.add(dpTilDato, 0, 3);
        dpTilDato.setEditable(false);
        dpTilDato.setDisable(true);
        // sikrer at der ikke kan vælges en tilDato der er før fraDato
        dpTilDato.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate fraDato = dpFraDato.getValue();

                setDisable(empty || date.compareTo(fraDato) < 0);
            }
        });

        // returneringstidspunkt
        Label lblReturneringstidspunkt = new Label("Returneringstidspunkt:");
        pane.add(lblReturneringstidspunkt, 0, 4);

        txfTidspunkt = new TextField();
        pane.add(txfTidspunkt, 0, 5);

        // produktgrupper
        Label lblProduktgrupper = new Label("Produktgrupper:");
        pane.add(lblProduktgrupper, 2, 0);

        lvwProduktgrupper = new ListView<>();
        lvwProduktgrupper.setPrefHeight(300);
        pane.add(lvwProduktgrupper, 2, 1, 1, 7);
        lvwProduktgrupper.getItems().setAll(controller.getUdlejningsProduktgrupper());

        ChangeListener<Produktgruppe> listener = (ov, oldString, newString) -> selectionChangedProduktgrupper();
        lvwProduktgrupper.getSelectionModel().selectedItemProperty().addListener(listener);

        // produkter
        Label lblProdukter = new Label("Produkter:");
        pane.add(lblProdukter, 3, 0);

        lvwProdukter = new ListView<>();
        lvwProdukter.setPrefHeight(300);
        pane.add(lvwProdukter, 3, 1, 1, 7);

        ChangeListener<Produkt> listener2 = (ov, oldString, newString) -> selectionChangedProdukter();
        lvwProdukter.getSelectionModel().selectedItemProperty().addListener(listener2);

        // produktpris
        Label lblProduktpris = new Label("Pris pr. stk");

        txfProduktpris = new TextField();
        txfProduktpris.setEditable(false);

        // produktantal
        Label lblProduktAntal = new Label("Antal");

        spinner = new Spinner<>();

        // antallet kan mindst være 1 og maksimum 20 pr. produkt - som standard er den
        // sat til 0
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0);
        spinner.setValueFactory(valueFactory);

        // total pris (selve ordet)
        Label lblTotalPris_ordet = new Label("Total pris: ");

        HBox totalPris_ordet_box = new HBox();
        totalPris_ordet_box.getChildren().add(lblTotalPris_ordet);
        pane.add(totalPris_ordet_box, 4, 8);

        // den totale pris der bliver opdateret løbende (selve tallet)
        lblTotalPris_tallet = new Label("0");

        HBox totalPris_tallet_box = new HBox();
        totalPris_tallet_box.getChildren().add(lblTotalPris_tallet);
        totalPris_tallet_box.setAlignment(Pos.BASELINE_RIGHT);
        pane.add(totalPris_tallet_box, 5, 8);

        // knapper for tilføj og fjern en produkt fra ordre
        Button btnTilføjProdukt = new Button("Tilføj til ordre");
        btnTilføjProdukt.setOnAction(event -> btnTilføjProduktAction());
        btnTilføjProdukt.setPrefSize(160, 40);
        Button btnFjernProdukt = new Button("Fjern fra ordre");
        btnFjernProdukt.setOnAction(event -> btnFjernProduktAction());
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
        pane.add(hbox, 3, 8, 1, 2);

        // ordre
        Label lblOrdre = new Label("Ordre:");
        pane.add(lblOrdre, 4, 0);

        lvwOrdre = new ListView<>();
        lvwOrdre.setPrefHeight(300);
        lvwOrdre.setPrefWidth(500);
        pane.add(lvwOrdre, 4, 1, 2, 7);

        // betalingsmiddel
        Label lblBetalingsmiddel = new Label("Betalingsmiddel");
        pane.add(lblBetalingsmiddel, 0, 12);

        // Radiobuttons for betalingsmiddel
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

        pane.add(betalingsmiddelBox, 0, 13, 8, 1);
        betalingsmiddelGroup.selectedToggleProperty().addListener(event -> rbBetalingsMiddelAction());

        // opret udlejning- knap
        Button btnOpretUdlejning = new Button("Opret udlejning");
        btnOpretUdlejning.setOnAction(event -> btnOpretUdlejningAction());
        btnOpretUdlejning.setPrefSize(225, 60);
        pane.add(btnOpretUdlejning, 0, 16);

        // label der viser fejl
        lblError = new Label();
        pane.add(lblError, 0, 17, 4, 1);
        lblError.setStyle("-fx-text-fill: red");
    }

    // fjerner et produkt fra ordren
    private void btnFjernProduktAction() {
        int index = lvwOrdre.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            Produkt produkt = produkter_i_ordre.get(index);

            ordrelinjer.remove(index);
            antal.remove(index);
            produktpriser.remove(index);
            produkter.remove(index);
            lvwOrdre.getItems().setAll(ordrelinjer);
            produkter_i_ordre.remove(index);
            priser_i_ordre.remove(index);

            opdaterSamletPris();

            if (produkt instanceof Fustage) {
                fustager.remove(produkt);
            } else if (produkt instanceof Kulsyre) {
                kulsyrer.remove(produkt);
            } else if (produkt instanceof Anlæg) {
                anlæg.remove(produkt);
            }
        }
    }

    // Tilføjer et produkt til ordren
    private void btnTilføjProduktAction() {
        String navn = "";
        Produktpris produktpris = null;
        produkt = lvwProdukter.getSelectionModel().getSelectedItem();
        String ordrelinje = "";
        int antal = spinner.getValue();
        double pris = 0;

        produkt = lvwProdukter.getSelectionModel().getSelectedItem();
        if (produkt != null && antal > 0) {
            navn = produkt.getNavn();
            for (Prisliste p : controller.getPrislister()) {
                if (p.getNavn().equals("Butik")) {
                    prisliste = p;
                }
            }
            for (Produktpris pr : produkt.getProduktpriser()) {
                if (pr.getPrisliste().equals(prisliste)) {

                    produktpris = pr;
                    pris = pr.getPris() * antal;
                }

                if (produkter_i_ordre.contains(produkt) == false) {
                    priser_i_ordre.add(pr.getPris());
                }
            }
            // hvis produktet ikke eksisterer i ordre
            if (produkter_i_ordre.contains(produkt) == false) {

                produkter_i_ordre.add(produkt);

                // hvis produktet er fra eksempelvis Fustage produktgruppen - tilføjes produktet
                // til fustager-arraylisten
                if (produktgruppe.getNavn().equals("Fustage")) {
                    fustager.add(produkt);
                } else if (produktgruppe.getNavn().equals("Kulsyre")) {
                    kulsyrer.add(produkt);
                } else if (produktgruppe.getNavn().equals("Anlæg")) {
                    anlæg.add(produkt);
                }

                ordrelinje = "Antal: " + antal + "  \t  Navn: " + navn + "  \t  Pris: " + pris;
                ordrelinjer.add(ordrelinje);
                this.antal.add(antal);
                produktpriser.add(produktpris);
                produkter.add(produkt);
                lvwOrdre.getItems().setAll(ordrelinjer);
                opdaterSamletPris();
                lblError.setText("");
                spinner.getValueFactory().setValue(0);

                // hvis produktet allerede eksisterer i ordre
            } else {
                for (int i = 0; i < ordrelinjer.size(); i++) {
                    if (ordrelinjer.get(i).contains(navn)) {
                        antal = this.antal.get(i) + spinner.getValue();
                        pris = produktpris.getPris() * antal;
                        this.antal.set(i, antal);
                        ordrelinjer.set(i, "Antal: " + antal + "  \t  Navn: " + navn + "  \t  Pris: " + pris);
                        lvwOrdre.getItems().setAll(ordrelinjer);
                        opdaterSamletPris();
                        spinner.getValueFactory().setValue(0);
                        lblError.setText("");
                    }
                }
            }

        } else {
            lblError.setText("Der skal vælges et produkt i produktlisten samt antal af produktet");
        }

        opdaterSamletPris();

    }

    // Opdaterer listviewet produkter, når en produktgruppe vælges.
    private void selectionChangedProduktgrupper() {
        produktgruppe = lvwProduktgrupper.getSelectionModel().getSelectedItem();
        if (produktgruppe != null) {
            lvwProdukter.getItems().setAll(produktgruppe.getProdukter());
        }
    }

    // når der vælges et produkt fra produkter-listviewet vises prisen pr. stk i
    // 'pris pr. stk'- feltet
    private void selectionChangedProdukter() {
        Produkt produkt = lvwProdukter.getSelectionModel().getSelectedItem();

        if (produkt != null) {

            for (Prisliste p : controller.getPrislister()) {
                if (p.getNavn().equals("Butik")) {
                    prisliste = p;
                }
            }

            // produktprisen hentes ud fra den valgte prisliste
            for (Produktpris pp : produkt.getProduktpriser()) {
                if (pp.getPrisliste().equals(prisliste)) {
                    txfProduktpris.setText(pp.getPris() + "");
                }
            }
        }
    }

    // Returnere det betalingsmiddel der er valgt.
    private String rbBetalingsMiddelAction() {
        String betalingsmiddel = "";

        if (betalingsmiddelGroup.getSelectedToggle() != null) {
            betalingsmiddel = ((RadioButton) betalingsmiddelGroup.getSelectedToggle()).getText();
        } else {
            betalingsmiddel = null;
        }
        return betalingsmiddel;
    }

    private void opdaterSamletPris() {
        samletPris = 0;

        for (int i = 0; i < produkter_i_ordre.size(); i++) {
            samletPris = samletPris + (priser_i_ordre.get(i) * antal.get(i));
        }

        lblTotalPris_tallet.setText(samletPris + "");
    }

    // Opretter et salg af udlejning
    private Ordre btnOpretUdlejningAction() {
        if (dpFraDato.getValue() != null && dpTilDato.getValue() != null && txfTidspunkt.getText().length() > 0
                && rbBetalingsMiddelAction() != null) {

            // hvis der er indtastet et gyldigt tidspunkt
            if (controller.timeIsValid(txfTidspunkt.getText()) == true) {
                if (fustager.size() != 0 && kulsyrer.size() != 0 && anlæg.size() != 0) {
                    LocalDate fraDato = dpFraDato.getValue();
                    LocalDate tilDato = dpTilDato.getValue();
                    LocalTime tidspunkt = LocalTime.parse(txfTidspunkt.getText());

                    String betaling = rbBetalingsMiddelAction();
                    Betalingsmiddel betalingsmiddel = Betalingsmiddel.valueOf(betaling);

                    Prisliste prisliste = null;
                    for (Prisliste pl : controller.getPrislister()) {
                        if (pl.getNavn().equals("Butik")) {
                            prisliste = pl;
                        }
                    }
                    ordre = (FadølsAnlægsUdlejning_ordre) controller.createFadølsAnlægsUdlejning_ordre(betalingsmiddel,
                            prisliste, fraDato, tilDato, tidspunkt, fustager, kulsyrer, anlæg);
                    for (int i = 0; i < antal.size(); i++) {
                        controller.createOrdrelinje(antal.get(i), produktpriser.get(i), ordre);
                    }
                    lblError.setText("");

                } else {
                    lblError.setText("Der skal vælges mindst 1 fustage, kulsyre og anlæg");
                }
            } else {
                lblError.setText("Der skal angives et gyldigt tidspunkt i formatet HH:MM");
            }
        } else {
            lblError.setText("Der skal vælges en dato, der skal angives et tidspunkt og betalingsmiddel skal vælges");
        }

        if (ordre != null && fustager.size() != 0 && kulsyrer.size() != 0 && anlæg.size() != 0) {

            // pant beregnes
            int pant = controller.beregnPant(produkter_i_ordre, this.antal);

            // nulstiller værdierne
            dpFraDato.getEditor().clear();
            dpTilDato.getEditor().clear();
            txfTidspunkt.clear();
            lvwProdukter.getItems().clear();
            lvwOrdre.getItems().clear();
            betalingsmiddelGroup.selectToggle(null);
            txfProduktpris.clear();
            spinner.getValueFactory().setValue(0);
            lvwProduktgrupper.getItems().setAll(controller.getUdlejningsProduktgrupper());
            produkter_i_ordre.clear();
            priser_i_ordre.clear();
            antal.clear();
            fustager.clear();
            kulsyrer.clear();
            anlæg.clear();
            ordrelinjer.clear();
            opdaterSamletPris();

            hide();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Opret_udlejning");
            alert.setHeaderText("");
            alert.setContentText("Udlejning er oprettet \nPant til betaling nu: " + pant
                    + " kr. \nDen samlede pris er: " + controller.beregnPris(ordre)
                    + " kr. \nTil betaling ved returnering: " + (controller.beregnPris(ordre) - pant) + " kr.");

            alert.show();
        }

        return ordre;
    }
}
