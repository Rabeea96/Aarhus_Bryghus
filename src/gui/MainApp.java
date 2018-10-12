package gui;

import container.Container;
import controller.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Container instans
    Controller controller = Controller.getInstance();

    // linkattributter til de nye vinduer der bliver åbnet
    private Salg_vindue salg;
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
        salg = new Salg_vindue("Salg");

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

        // salgssiden
        Button btnSalg = new Button("Salg");
        pane.add(btnSalg, 0, 0);
        btnSalg.setOnAction(event -> salgAction());

        // administrationssiden
        Button btnAdministration = new Button("Administration");
        pane.add(btnAdministration, 1, 0);
        btnAdministration.setOnAction(event -> administrationAction());

        // oversigt over salg- siden
        Button btnOversigt_over_salg = new Button("Oversigt over salg");
        pane.add(btnOversigt_over_salg, 2, 0);
        btnOversigt_over_salg.setOnAction(event -> oversigt_over_salgAction());

    }

    // -------------------------------------------------------------------------

    // salgvinduet vises
    public void salgAction() {
        salg.showAndWait();
    }

    // administrationsvinduet vises (med tabs)
    public void administrationAction() {
        administration.showAndWait();
    }

    // oversigt over salgsvinduet vises (med tabs)
    public void oversigt_over_salgAction() {
        oversigt_over_salg.showAndWait();
    }

}
