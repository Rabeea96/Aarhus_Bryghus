package gui;

import controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Controller instans
    Controller controller = Controller.getInstance();

    // linkattributter til de nye vinduer der bliver åbnet
    private Salg_vælgPrisliste salg;
    private VælgRundvisning_vindue rundvisning_vindue;
    private Udlejning_vindue udlejning_vindue;
    private Administration administration;
    private Oversigt_over_salg oversigt_over_salg;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        controller.createSomeObjects();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hovedmenu");
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // det nye vindue der åbnes når der klikkes på knappen salg
        salg = new Salg_vælgPrisliste("Salg");

        // det nye vindue der åbnes når der klikkes på knappen rundvisning
        rundvisning_vindue = new VælgRundvisning_vindue("Rundvisning");

        // det nye vindue der åbnes når der klikkes på knappen udlejning
        udlejning_vindue = new Udlejning_vindue("Udlejning");

        // det nye vindue der åbnes når der klikkes på knappen administration
        administration = new Administration("Administration");

        // det nye vindue der åbnes når der klikkes på knappen oversigt over salg
        oversigt_over_salg = new Oversigt_over_salg("Oversigt over salg");

    }

    public void initContent(GridPane pane) {
        pane.setPadding(new Insets(50));
        pane.setHgap(20);
        pane.setVgap(10);
        pane.setGridLinesVisible(false);

        // salgsknap
        Button btnSalg = new Button("Salg");
        pane.add(btnSalg, 0, 0);
        btnSalg.setOnAction(event -> salgAction());
        btnSalg.setPrefHeight(40);
        btnSalg.setPrefWidth(70);

        // rundvisningsknap
        Button btnRundvisning = new Button("Rundvisning");
        pane.add(btnRundvisning, 1, 0);
        btnRundvisning.setOnAction(event -> rundvisningAction());
        btnRundvisning.setPrefHeight(40);
        btnRundvisning.setPrefWidth(110);

        // udlejningsknap
        Button btnUdlejning = new Button("Udlejning");
        pane.add(btnUdlejning, 2, 0);
        btnUdlejning.setOnAction(event -> udlejningAction());
        btnUdlejning.setPrefHeight(40);
        btnUdlejning.setPrefWidth(90);

        // administrationsknap
        Button btnAdministration = new Button("Administration");
        pane.add(btnAdministration, 3, 0);
        btnAdministration.setOnAction(event -> administrationAction());
        btnAdministration.setPrefHeight(40);
        btnAdministration.setPrefWidth(140);

        // oversigt over salg- knap
        Button btnOversigt_over_salg = new Button("Oversigt over salg");
        pane.add(btnOversigt_over_salg, 4, 0);
        btnOversigt_over_salg.setOnAction(event -> oversigt_over_salgAction());
        btnOversigt_over_salg.setPrefHeight(40);
        btnOversigt_over_salg.setPrefWidth(180);

        // et billede af Aarhus Bryghus
        Image img = new Image("AarhusBryghus.jpg");
        ImageView imgVw = new ImageView(img);
        imgVw.setFitWidth(614.4);
        imgVw.setFitHeight(443.7);

        // imageviewet bliver smidt ind i en HBox sådan så at billedet kan centreres
        HBox hBox = new HBox();
        hBox.getChildren().add(imgVw);
        hBox.setAlignment(Pos.CENTER);
        pane.add(hBox, 0, 3, 5, 1); // fylder 5 kolonner og 1 række
    }

    // -------------------------------------------------------------------------

    // salgvinduet vises
    public void salgAction() {
        salg.showAndWait();
    }

    // rundvisningsvinduet vises
    public void rundvisningAction() {
        rundvisning_vindue.showAndWait();
    }

    // udlejningsvinduet vises
    public void udlejningAction() {
        udlejning_vindue.showAndWait();
    }

    // administrationsvinduet vises (med tabs)
    public void administrationAction() {
        administration.showAndWait();
    }

    // oversigt-over-salgsvinduet vises (med tabs)
    public void oversigt_over_salgAction() {
        oversigt_over_salg.showAndWait();
    }

}
