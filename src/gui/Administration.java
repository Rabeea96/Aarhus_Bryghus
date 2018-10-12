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

public class Administration extends Stage {

    public Administration(String title) {
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
        Tab tab1 = new Tab("Produktgruppe");
        tabPane.getTabs().add(tab1);
        Produktgruppe_oversigt produktgruppe = new Produktgruppe_oversigt();
        tab1.setContent(produktgruppe);

        // tab2
        Tab tab2 = new Tab("Produkt");
        tabPane.getTabs().add(tab2);
        Produkt_oversigt produkt = new Produkt_oversigt();
        tab2.setContent(produkt);

        // tab3
        Tab tab3 = new Tab("Prisliste");
        tabPane.getTabs().add(tab3);
        Prisliste_oversigt prisliste = new Prisliste_oversigt();
        tab3.setContent(prisliste);

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