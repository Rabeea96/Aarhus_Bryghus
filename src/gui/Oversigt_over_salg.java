package gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Oversigt_over_salg extends Stage {

    public Oversigt_over_salg(String title) {
        setTitle(title);
        Group root = new Group();
        // størrelsen på vinduet med tabs samt baggrundsfarven til vinduet
        Scene scene = new Scene(root, 700, 300, Color.WHITE);
        setScene(scene);
        // tabpane bliver brugt til at tilføje tabs i
        TabPane tabPane = new TabPane();
        // det skal ikke være muligt at fjerne/annullere et tab
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        BorderPane borderPane = new BorderPane();

        // tab1
        Tab tab1 = new Tab("Dagenssalg");
        tabPane.getTabs().add(tab1);
        Dagenssalg_oversigt dagenssalg = new Dagenssalg_oversigt();
        tab1.setContent(dagenssalg);

        // tab2
        Tab tab2 = new Tab("Klippekort");
        tabPane.getTabs().add(tab2);
        Klippekort_oversigt klippekort = new Klippekort_oversigt();
        tab2.setContent(klippekort);

        // tab3
        Tab tab3 = new Tab("Fustageanlæg");
        tabPane.getTabs().add(tab3);
        Fustageanlæg_oversigt fustageanlæg = new Fustageanlæg_oversigt();
        tab3.setContent(fustageanlæg);

        // tilføjer lidt luft omkring vindueskanten
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);

        initStyle(StageStyle.UTILITY);
        setMinHeight(100);
        setMinWidth(200);
        setResizable(false);
    }

}