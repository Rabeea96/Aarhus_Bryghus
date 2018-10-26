package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

//klassen extender Stage klassen da det er et undervindue
public class Rundvisning_vindue extends Stage {

    public Rundvisning_vindue(String title) {
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

    private DatePicker dpDato;
    private TextField txfTime, txfRabat;
    private Spinner<Integer> spinner;
    private RadioButton rbStudierabatJa, rbStudierabatNej, rbØnskerRabatJa, rbØnskerRabatNej, rbProcent, rbKroner,
            rbKontant, rbDankort, rbMobilepay, rbRegning, rbKlippekort;
    private ToggleGroup studierabatGroup, ønskerRabatGroup, rabatGroup, betalingsmiddelGroup;
    private HBox rabatBox;
    private Ordre ordre = null;
    private Produktpris rundvisning_butik = null;
    private int antalPersoner;
    // Controller instans
    Controller controller = Controller.getInstance();

    private void initContent(GridPane pane) {

        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label lblDato = new Label("Dato");
        pane.add(lblDato, 0, 0);

        dpDato = new DatePicker();
        pane.add(dpDato, 0, 1);
        dpDato.setEditable(false);

        Label lblTime = new Label("Tidspunkt");
        pane.add(lblTime, 0, 2);

        txfTime = new TextField();
        pane.add(txfTime, 0, 3);

        Label lblAntalDeltagere = new Label("Antal deltagere");
        pane.add(lblAntalDeltagere, 1, 0);

        spinner = new Spinner<>();
        pane.add(spinner, 1, 1);

        // der kan mindst være 15 personer og maksimum 75 personer for en rundvisning
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 75);
        spinner.setValueFactory(valueFactory);

        Label lblStudieRabat = new Label("Studierabat:");
        pane.add(lblStudieRabat, 1, 2);

        // radiobuttons for om der skal gives studierabat
        HBox studierabatBox = new HBox();
        studierabatBox.setSpacing(20);
        studierabatGroup = new ToggleGroup();
        rbStudierabatJa = new RadioButton("Ja");
        studierabatBox.getChildren().add(rbStudierabatJa);
        rbStudierabatJa.setToggleGroup(studierabatGroup);
        rbStudierabatNej = new RadioButton("Nej");
        studierabatBox.getChildren().add(rbStudierabatNej);
        rbStudierabatNej.setToggleGroup(studierabatGroup);
        pane.add(studierabatBox, 1, 3);
        studierabatGroup.selectedToggleProperty().addListener(event -> rbStudieRabatAction());
        // studierabat er som standard sat til 'Nej'
        studierabatGroup.getToggles().get(1).setSelected(true);

        Label lblØnskerRabat = new Label("Ønskes rabat");
        pane.add(lblØnskerRabat, 0, 4);

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
        pane.add(ønskerRabatBox, 0, 5);
        ønskerRabatGroup.selectedToggleProperty().addListener(event -> rbØnskerRabatAction());
        // rabat er som standard sat til 'Nej'
        ønskerRabatGroup.getToggles().get(1).setSelected(true);

        Label lblRabat = new Label("Hvis ja, angiv hvordan:");
        pane.add(lblRabat, 1, 4);

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
        pane.add(rabatBox, 1, 5);
        rabatGroup.selectedToggleProperty().addListener(event -> rbRabatAction());
        // rabat er som standard sat til 'Kroner'
        rabatGroup.getToggles().get(0).setSelected(true);
        rabatBox.setDisable(true);

        txfRabat = new TextField();
        pane.add(txfRabat, 1, 6);
        txfRabat.setDisable(true);

        Label lblBetalingsmiddel = new Label("Betalingsmiddel");
        pane.add(lblBetalingsmiddel, 0, 7);

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

        pane.add(betalingsmiddelBox, 0, 8, 2, 1);
        betalingsmiddelGroup.selectedToggleProperty().addListener(event -> rbBetalingsMiddelAction());

        Button btnRegistrereRundvisning = new Button("Registrere rundvisning");
        pane.add(btnRegistrereRundvisning, 0, 9);
        btnRegistrereRundvisning.setOnAction(event -> registrerRundvisningAction());

    }

    private boolean rbStudieRabatAction() {
        boolean studierabat = false;

        if (studierabatGroup.getToggles().get(0).isSelected() == true) {
            studierabat = true;
        }

        return studierabat;
    }

    // hvis der ønskes rabat tændes rabatboxen hvor der kan angives om det er i
    // procent eller kroner og tekstfeltet hvor man angiver rabatten
    private boolean rbØnskerRabatAction() {
        boolean rabat = false;

        if (ønskerRabatGroup.getToggles().get(0).isSelected() == true) {
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

        if (betalingsmiddelGroup.getSelectedToggle() != null) {
            betalingsmiddel = ((RadioButton) betalingsmiddelGroup.getSelectedToggle()).getText();
        } else {
            betalingsmiddel = null;
        }

        return betalingsmiddel;

    }

    // bruges til at checke om tidspunktet er indtastet i et gyldigt format
    public boolean timeIsValid(String s) {
        try {
            LocalTime.parse(s);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // bruges til at checke om rabatten er indtastet i et gyldigt format - dvs. i
    // tal og ikke bogstaver
    public boolean numberIsValid(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private Ordre registrerRundvisningAction() {
        // hvis dato, tidspunkt og betaling er udfyldte
        if (dpDato.getValue() != null && txfTime.getText().length() > 0 && rbBetalingsMiddelAction() != null) {

            // hvis der er indtastet et gyldigt tidspunkt
            if (timeIsValid(txfTime.getText()) == true) {

                LocalDate dato = dpDato.getValue();
                LocalTime tidspunkt = LocalTime.parse(txfTime.getText());
                boolean studierabat = rbStudieRabatAction();
                String betaling = rbBetalingsMiddelAction();
                Betalingsmiddel betalingsmiddel = Betalingsmiddel.valueOf(betaling);
                antalPersoner = spinner.getValue();
                boolean rabat = rbØnskerRabatAction();
                int rabatten = 0;
                Strategy_giv_rabat rabat_form = rbRabatAction();
                Produktgruppe produktgruppe = null;
                for (Produktgruppe p : controller.getProduktgrupper()) {
                    if (p.getNavn().equals("Rundvisning")) {
                        produktgruppe = p;
                    }
                }
                Prisliste prisliste = produktgruppe.getProdukter().get(0).getProduktpriser().get(0).getPrisliste();
                rundvisning_butik = produktgruppe.getProdukter().get(0).getProduktpriser().get(0);

                // hvis der ønskes rabat
                if (rabat == true) {
                    // hvis rabatten er udfyldt
                    if (txfRabat.getText().length() > 0) {
                        // hvis rabatten er udfyldt i tal
                        if (numberIsValid(txfRabat.getText()) == true) {
                            rabatten = Integer.parseInt(txfRabat.getText());
                            ordre = controller.createRundvisning_ordre(betalingsmiddel, dato, prisliste, rabat_form,
                                    rabatten, tidspunkt, studierabat);
                        }
                    }

                } else {
                    ordre = controller.createRundvisning_ordre(betalingsmiddel, dato, prisliste, tidspunkt,
                            studierabat);
                }

            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Rundvisning_vindue");
                alert.setHeaderText("");
                alert.setContentText("Der skal angives et gyldigt tidspunkt i formatet HH:MM");
                alert.show();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rundvisning_vindue");
            alert.setHeaderText("");
            alert.setContentText(
                    "Der skal vælges en dato, der skal angives et tidspunkt og betalingsmiddel skal vælges");
            alert.show();
        }

        // rundvisningen bliver oprettet, vinduet skjules og man vender tilbage til
        // hovedmenuen og der popper et pop-up vindue der viser den samlede pris
        if (ordre != null) {
            controller.createOrdrelinje(antalPersoner, rundvisning_butik, ordre);
            hide();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Rundvisning_vindue");
            alert.setHeaderText("");
            alert.setContentText("Ordren er oprettet \nDen samlede pris er: " + controller.beregnPris(ordre) + " kr.");
            alert.show();
        }

        return ordre;
    }
}
