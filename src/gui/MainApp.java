package gui;

import java.time.LocalDate;

import container.Container;
import controller.*;
import model.Ordre;
import model.Produktgruppe;

public class MainApp {

    public static void main(String[] args) {
        // Controller instans
        Controller controller = Controller.getInstance();

        // Container instans
        Container container = Container.getInstance();

        controller.createSomeObjects();

        // for (Produktgruppe p : container.getProduktgrupper()) {
        // System.out.println(p.getNavn() + ": " + p.getProdukter());
        // }

        // giver rabat i procent
        // Context context = new Context(new Giv_rabat_i_procent());
        // System.out.println(context.executeStrategy(10, container.getOrdre().get(0)));

        // give rabat i kroner
        // context = new Context(new Giv_rabat_i_kroner());
        // System.out.println(context.executeStrategy(100,
        // container.getOrdre().get(0)));

        // oversigt over dagens salg
        controller.getDagenssalg(LocalDate.of(2018, 10, 8));

        for (Ordre o : container.getOrdre()) {
            System.out.println(controller.beregnPris(o));
        }

        for (String s : container.getSalg()) {
            System.out.println(s);
        }

    }

}
