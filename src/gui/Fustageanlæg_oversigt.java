package gui;

import container.Container;
import controller.Controller;
import model.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

//extender gridpane da det er et tab
public class Fustageanlæg_oversigt extends GridPane {

    // Controller instans
    Controller controller = Controller.getInstance();
    // Container instans
    Container container = Container.getInstance();
    private ListView<Ordre> lvwOrdre;
    private TextArea txaProdukter;

    public Fustageanlæg_oversigt() {

        setGridLinesVisible(false);
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        Label lblUdlejninger = new Label("Oversigt over aktive udlejninger:");
        this.add(lblUdlejninger, 0, 0);

        lvwOrdre = new ListView<>();
        this.add(lvwOrdre, 0, 1);
        lvwOrdre.getItems().setAll(controller.getAktiveUdlejninger());

        // kalder på en metoden selectionChanged() hver gang der bliver valgt en ordre
        ChangeListener<Ordre> listener = (ov, oldString, newString) -> selectionChanged();
        lvwOrdre.getSelectionModel().selectedItemProperty().addListener(listener);

        Label lblProdukter = new Label("Oversigt over ikke returnerede produkter i udlejningen:");
        this.add(lblProdukter, 1, 0);

        txaProdukter = new TextArea();
        txaProdukter.setPrefWidth(400);
        this.add(txaProdukter, 1, 1);
    }

    private void selectionChanged() {
        Ordre ordre = lvwOrdre.getSelectionModel().getSelectedItem();

        if (ordre != null) {

            for (Salg s : container.getSalg()) {
                // når ordre-ID matcher en salgs-ID så printes produkterne
                if (s.getCounter() == ordre.getOrdreCounter()) {
                    for (String p : s.getNavn_pris_antal()) {
                        txaProdukter.appendText(p + "\n");
                    }
                }
            }
        }
    }

}
