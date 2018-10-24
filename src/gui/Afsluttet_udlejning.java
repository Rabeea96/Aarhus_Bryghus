package gui;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//klassen extender Stage klassen da det er et undervindue
public class Afsluttet_udlejning extends Stage {

    public Afsluttet_udlejning(String title) {
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
        // TODO Auto-generated method stub

    }

    Controller controller = Controller.getInstance();

}
