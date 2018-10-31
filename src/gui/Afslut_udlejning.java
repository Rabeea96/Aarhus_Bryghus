package gui;

import java.util.ArrayList;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.FadølsAnlægsUdlejning_ordre;
import model.Fustage;
import model.Kulsyre;
import model.Ordre;
import model.Ordrelinje;
import model.Produkt;
import model.Salg;

//klassen extender Stage klassen da det er et undervindue
public class Afslut_udlejning extends Stage {

    Controller controller = Controller.getInstance();
    private FadølsAnlægsUdlejning_ordre ordre;
    private ListView<Ordre> lvwOrdrer;
    private ListView<String> lvwProdukter;
    private TextField txfForbrug, txfPris;
    private ArrayList<String> produkter = new ArrayList<>();
    private ArrayList<Integer> antal = new ArrayList<>();
    private ArrayList<Double> pris = new ArrayList<>();
    private Label lblSamletPris, lblError;
    private double samletPris;
    private double ordrelinjePris;
    private double procent;
    private double nyOrdrelinjePris;
    private double returVærdi;
    private int index;
    private int pant;

    public Afslut_udlejning(String title) {
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

        // ordrer
        Label lblOrdrer = new Label("Ordrer:");
        pane.add(lblOrdrer, 0, 0);

        lvwOrdrer = new ListView<>();
        lvwOrdrer.setPrefHeight(300);
        pane.add(lvwOrdrer, 0, 1, 1, 5);
        lvwOrdrer.getItems().setAll(controller.getAktiveUdlejninger());

        // kalder på en metoden selectionChanged() hver gang der bliver valgt en ordre
        ChangeListener<Ordre> listener1 = (ov, oldString, newString) -> selectionChangedOrdrer();
        lvwOrdrer.getSelectionModel().selectedItemProperty().addListener(listener1);

        // produkter
        Label lblProdukter = new Label("Produkter");
        pane.add(lblProdukter, 1, 0);

        lvwProdukter = new ListView<>();
        pane.add(lvwProdukter, 1, 1, 1, 5);
        lvwProdukter.setPrefWidth(300);

        ChangeListener<String> listener2 = (ov, oldString, newString) -> selectionChangedProdukter();
        lvwProdukter.getSelectionModel().selectedItemProperty().addListener(listener2);

        // pris på produkt
        Label lblOrdrelinjePris = new Label("Pris på produkt:");
        pane.add(lblOrdrelinjePris, 2, 0);

        txfPris = new TextField();
        txfPris.setMaxWidth(60);
        txfPris.setEditable(false);

        Label lblKroner = new Label("kr.");

        // forbrugt mængde
        Label lblForbrug = new Label("Forbrugt mængde i procent:");
        pane.add(lblForbrug, 2, 2);

        txfForbrug = new TextField();
        txfForbrug.setMaxWidth(60);
        // Sikre at der kun kan indtastes tal i txfForbrug
        txfForbrug.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txfForbrug.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Label lblProcent = new Label("%");

        // denne hbox indeholder pris-feltet samt 'kr.' label
        HBox hbox1 = new HBox();
        hbox1.setSpacing(10);
        hbox1.getChildren().add(txfPris);
        hbox1.getChildren().add(lblKroner);
        pane.add(hbox1, 2, 1, 2, 1);

        // denne hbox indeholder forbrugt mængde- feltet samt '%' label
        HBox hbox2 = new HBox();
        hbox2.setSpacing(10);
        hbox2.getChildren().add(txfForbrug);
        hbox2.getChildren().add(lblProcent);
        pane.add(hbox2, 2, 3, 2, 1);

        // opdater pris- knap
        Button btnOpdaterPris = new Button("Opdater pris");
        btnOpdaterPris.setOnAction(event -> btnOpdaterPrisAction());
        pane.add(btnOpdaterPris, 2, 4);

        // samletpris
        lblSamletPris = new Label("Samlet pris: " + samletPris + " kr.");
        pane.add(lblSamletPris, 2, 5);

        // registrer udlejning- knap
        Button btnRegistrerUdlejning = new Button("Afslut udlejning");
        btnRegistrerUdlejning.setOnAction(event -> btnAfslutUdlejningAction());
        btnRegistrerUdlejning.setPrefSize(190, 40);
        pane.add(btnRegistrerUdlejning, 0, 6);

        // label der viser fejl
        lblError = new Label();
        pane.add(lblError, 0, 7, 3, 1);
        lblError.setStyle("-fx-text-fill: red");

    }

    // de ændringer der sker når en ordrelinje vælges
    private void selectionChangedProdukter() {
        if (lvwProdukter.getSelectionModel().getSelectedItem() != null) {
            index = lvwProdukter.getSelectionModel().getSelectedIndex();
        }
        ordrelinjePris = this.pris.get(index) * antal.get(index);

        txfPris.setText(ordrelinjePris + "");
    }

    // de ændringer der sker når en ordre vælges
    private void selectionChangedOrdrer() {
        ordre = (FadølsAnlægsUdlejning_ordre) lvwOrdrer.getSelectionModel().getSelectedItem();
        if (ordre != null) {
            for (Ordrelinje o : ordre.getOrdrelinjer()) {
                produkter.add(o.getAntal() + " " + o.getProduktpris().getProdukt().getNavn());
                antal.add(o.getAntal());
                pris.add(o.getProduktpris().getPris());
                samletPris = ordre.samletpris();

                lblSamletPris.setText("Samlet pris: " + samletPris + " kr.");
            }

        }
        if (produkter.isEmpty() == false) {
            lvwProdukter.getItems().setAll(produkter);
        }
    }

    // opdaterer priser
    private void btnOpdaterPrisAction() {
        if (lvwProdukter.getSelectionModel().getSelectedItem() != null) {
            if (txfForbrug.getText().length() > 0) {

                // den samelde pris opdateres
                opdaterSamletPris();

            } else {
                lblError.setText("Udfyld forbrugt mængde i tal");
            }
        } else {
            lblError.setText("Vælg et produkt før prisen opdateres");
        }
    }

    // opdater samlet pris
    private void opdaterSamletPris() {
        index = lvwProdukter.getSelectionModel().getSelectedIndex();
        procent = Double.parseDouble(txfForbrug.getText());

        produkter.set(index, produkter.get(index) + " " + procent + "%");
        ordrelinjePris = pris.get(index) * antal.get(index);
        returVærdi = ordrelinjePris * controller.omdanTalTilProcent(procent);
        nyOrdrelinjePris = ordrelinjePris - returVærdi;
        pris.set(index, nyOrdrelinjePris);
        lvwProdukter.getItems().setAll(produkter);
        samletPris = (samletPris - returVærdi);
        txfPris.setText("");
        txfForbrug.setText("");
        lblSamletPris.setText("Samlet pris: " + samletPris + " kr.");
    }

    // afslutter en udlejning og fjerner udlejningen fra aktive udlejninger
    private void btnAfslutUdlejningAction() {
        if (ordre != null) {

            // pant beregnes
            pant = 0;

            for (int i = 0; i < ordre.getOrdrelinjer().size(); i++) {

                Produkt produkt = ordre.getOrdrelinjer().get(i).getProduktpris().getProdukt();

                if (produkt instanceof Fustage) {
                    pant = pant + (this.antal.get(i) * 200);
                } else if (produkt instanceof Kulsyre) {
                    pant = pant + (this.antal.get(i) * 1000);
                }
            }

            ordre.setStatus(false); // den markerer udlejningsordren som afsluttet
            hide();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Afslut_udlejning");
            alert.setHeaderText("");
            alert.setContentText("Udlejning er afsluttet \nOrdrens samlede pris: " + samletPris
                    + " kr. \nPant betalt ved bestilling: " + pant + " kr \nTil betaling: " + (samletPris - pant)
                    + " kr.");

            alert.show();
            for (Salg s : controller.getSalg()) {
                // når ordre-ID matcher en salgs-ID så printes produkterne
                if (s.getCounter() == ordre.getOrdreCounter()) {
                    s.setSamletPris(samletPris);
                }
            }
            lvwOrdrer.getItems().setAll(controller.getAktiveUdlejninger());
            produkter.clear();
            lvwProdukter.getItems().setAll(produkter);
        } else {
            lblError.setText("Der skal vælges en ordre at afslutte");
        }

    }
}
